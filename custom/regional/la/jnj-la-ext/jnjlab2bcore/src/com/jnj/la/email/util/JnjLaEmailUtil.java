/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.email.util;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.OrderAndMessageData;
import com.jnj.facades.data.OrderConfirmationMessageData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjLatamCronjobErrorDTO;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.services.MessageService;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Order.ORDER_CONFIRMATION_FROM_EMAIL_KEY;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Order.ORDER_CONFIRMATION_FROM_EMAIL_NAME_KEY;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Order.ORDER_CONFIRMATION_FROM_EMAIL_DEFAULT;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.Order.ORDER_CONFIRMATION_FROM_EMAIL_NAME_DEFAULT;


/**
 *
 */
public class JnjLaEmailUtil
{
	@Autowired
	private B2BOrderService b2bOrderService;

	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private DefaultEmailService emailService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	protected MessageService messageService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	private JnjConfigService jnjConfigService;


	private VelocityEngine velocityEngine;
	private static final Logger LOGGER = Logger.getLogger(JnjLaCoreUtil.class);
	private static final Class className = JnjLaEmailUtil.class;

	public Boolean sendNotification(final CronJobModel cronJobModel, final Map<Object, String> errorRecords,
			final String otherErrors, final Class className, final String logFileName, final String vmFileName)
	{

		final String METHOD_NAME = "sendNotification()";

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, METHOD_NAME,
				"Sending Email from Upsert Customer cron job" + "-" + JnJCommonUtil.getCurrentDateTime(), className);


		catalogVersionService = (CatalogVersionService) ServicelayerUtils.getApplicationContext().getBean("catalogVersionService");


		final List<EmailAddressModel> toaddress = getToEmailAddressList();

		final String cronjobEmailAddress = Config.getParameter("error.email.from.address");
		final EmailAddressModel fromAddressModel = emailService.getOrCreateEmailAddressForEmail(cronjobEmailAddress,
				"JNJ Cronjob Issue Alert - " + Config.getParameter("deployment.env"));


		final String fileName = logFileName + (new Date().getTime()) + ".txt";
		final String errorFilePath = Config.getParameter("jnj.cronjobs.error.log.path");

		final Boolean statusWriteToLogFile = writeRecordsToLogFile(fileName, errorFilePath, errorRecords, otherErrors);
		boolean isEmailSentSuccessfully = false;
		if (statusWriteToLogFile.booleanValue())
		{

			final List<EmailAttachmentModel> emailAttachments = createAttachment(errorFilePath, fileName, cronJobModel);
			if (toaddress != null && !toaddress.isEmpty())
			{
				isEmailSentSuccessfully = sendEmail(toaddress, fromAddressModel, emailAttachments, cronJobModel, vmFileName);
			}
			else
			{
				JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME, METHOD_NAME,
						"Upsert Customer cronjob not sent. Destination email is empty " + "-" + JnJCommonUtil.getCurrentDateTime(),
						className);
				isEmailSentSuccessfully = false;
			}
		}
		return Boolean.valueOf(isEmailSentSuccessfully);
	}


	public List<EmailAddressModel> getToEmailAddressList()
	{

		List<EmailAddressModel> toAddress = null;
		EmailAddressModel addressModel = null;
		final String toEmailIDList = Config.getParameter("techSupportEmails");
		if (StringUtils.isNotBlank(toEmailIDList))
		{
			toAddress = new ArrayList<>();
			final String[] toList = toEmailIDList.trim().split(";");
			for (int iCount = 0; iCount < toList.length; iCount++)
			{
				final String emailaddress = toList[iCount];
				final String[] displayName = emailaddress.split(".", 1);
				addressModel = emailService.getOrCreateEmailAddressForEmail(emailaddress, displayName[0]);
				modelService.save(addressModel);
				toAddress.add(addressModel);
			}
		}
		return toAddress;
	}


	public Boolean writeRecordsToLogFile(final String fileName, final String errorFilePath, final Map<Object, String> errorRecords,
			final String otherErrors)
	{

		FileWriter fw = null;
		StringWriter sw = null;

		Boolean isSuccess = Boolean.FALSE;

		try
		{
			fw = new FileWriter(
					FilenameUtils.getFullPath(errorFilePath + fileName) + FilenameUtils.getName(errorFilePath + fileName));
			sw = new StringWriter();

			if (otherErrors != null && !otherErrors.isEmpty())
			{
				sw.write(otherErrors);
			}

			if (errorRecords != null)
			{

				for (final Map.Entry<Object, String> errorRecord : errorRecords.entrySet())
				{
					if (errorRecord.getKey() != null)
					{
						sw.write(errorRecord.getKey().toString());
					}
					sw.write("\n");
					if (errorRecord.getValue() != null)
					{
						sw.write(errorRecord.getValue());
					}
					sw.write("\n");
				}
			}
			fw.write(sw.toString());
			isSuccess = Boolean.TRUE;
		}
		catch (final Exception exception)
		{

			LOGGER.error(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB__METHOD_EXCEPTION + Logging.HYPHEN
					+ exception);
			isSuccess = Boolean.FALSE;
		}
		finally
		{
			try
			{
				fw.close();
			}
			catch (final Exception exception)
			{

				LOGGER.error(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB__METHOD_EXCEPTION
						+ Logging.HYPHEN + exception);
			}
			try
			{
				sw.close();
			}
			catch (final Exception exception)
			{

				LOGGER.error(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB__METHOD_EXCEPTION
						+ Logging.HYPHEN + exception);
			}

		}
		return isSuccess;
	}


	public List<EmailAttachmentModel> createAttachment(final String errorFilePath, final String fileName,
			final CronJobModel cronJobModel)
	{
		final EmailAttachmentModel attachment = modelService.create(EmailAttachmentModel.class);
		InputStream inputStream = null;

		final List<EmailAttachmentModel> attachmentModels = new ArrayList<>();
		try
		{
			inputStream = new FileInputStream(
					FilenameUtils.getFullPath(errorFilePath + fileName) + FilenameUtils.getName(errorFilePath + fileName));
			attachment.setCode(("log-" + cronJobModel.getCode() + "-" + fileName));
			attachment.setRealFileName("log-" + cronJobModel.getCode() + "-" + fileName);
			attachment.setCatalogVersion(catalogVersionService.getCatalogVersion("Default", "Online"));
			attachment.setMime("text/plain");
			attachment.setRemovable(Boolean.TRUE);
			attachment.setCronJob(cronJobModel);
			modelService.save(attachment);
			mediaService.setStreamForMedia(attachment, inputStream, ("log-" + cronJobModel.getCode() + "-" + fileName), "",
					mediaService.getRootFolder());
			attachmentModels.add(attachment);
		}
		catch (final Exception exception)
		{

			LOGGER.error(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB__METHOD_EXCEPTION + Logging.HYPHEN
					+ exception);
		}
		finally
		{
			try
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
			catch (final Exception exception)
			{

				LOGGER.error(Logging.FEED_CONTRACTS_JOB + Logging.HYPHEN + Logging.FEED_CONTRACTS_JOB__METHOD_EXCEPTION
						+ Logging.HYPHEN + exception);
			}
		}
		return attachmentModels;
	}


	public boolean sendEmail(final List<EmailAddressModel> toAddress, final EmailAddressModel fromAddress,
			final List<EmailAttachmentModel> attachments, final CronJobModel cronJobModel, final String vmFileName)
	{
		boolean isEmailSentSuccessfully = false;
		final EmailMessageModel emailMessageModel = modelService.create(EmailMessageModel.class);

		final VelocityContext context = new VelocityContext();

		final JnjLatamCronjobErrorDTO latamCronjobErrorDto = new JnjLatamCronjobErrorDTO();

		latamCronjobErrorDto.setCronJobName(cronJobModel.getCode());
		latamCronjobErrorDto.setStartDate(cronJobModel.getStartTime());
		latamCronjobErrorDto.setEndDate(cronJobModel.getEndTime());

		latamCronjobErrorDto.setResult(cronJobModel.getResult().getCode());
		context.put("ctx", latamCronjobErrorDto);




		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");


		LOGGER.info("Getting the vm file..");

		final String path = getClass().getResource(Config.getParameter("cronjob.error.email.template.path")).getPath();

		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf("/")));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");


		LOGGER.info("Initializing Velocity Engine START");

		velocityEngine.init();

		LOGGER.info("Initializing Velocity Engine DONE");
		final StringWriter writer = new StringWriter();
		if (StringUtils.isNotEmpty(vmFileName))
		{


			final Template template = velocityEngine.getTemplate(vmFileName);


			LOGGER.info("VM File fetched :: " + template);


			template.merge(context, writer);

			final String htmlString = String.valueOf(writer);

			LOGGER.info("templateScript EN :: " + writer);

			if (CollectionUtils.isNotEmpty(attachments))
			{
				emailMessageModel.setAttachments(attachments);
			}

			emailMessageModel.setBody(htmlString);
			emailMessageModel.setToAddresses(toAddress);

			emailMessageModel
					.setSubject("Test-VelocityTemplate : " + "Falied Records [" + Config.getParameter("deployment.env") + "]-->");
			emailMessageModel.setFromAddress(fromAddress);
			emailMessageModel.setReplyToAddress("latamjobsfailedrecords@jnjportal.com");

			modelService.save(emailMessageModel);
			isEmailSentSuccessfully = emailService.send(emailMessageModel);
		}

		return isEmailSentSuccessfully;
	}


	public boolean sendStatusChangeNotification(final CustomerData customer, final String orderCode, final String baseUrl,
			final String mediaLogoURL)
	{
		final List<EmailAddressModel> toFromAddress = new ArrayList<>();

		final EmailMessageModel emailMessageModel = modelService.create(EmailMessageModel.class);
		final OrderModel orderModel = b2bOrderService.getOrderForCode(orderCode);

		OrderData populatedOrderData = null;
		final OrderConfirmationMessageData messageData = createMessageData();
		try
		{
			populatedOrderData = orderConverter.convert(orderModel, new JnjGTOrderData());

			((JnjGTOrderData) populatedOrderData)
					.setOrderType(messageService.getMessageForCode("text.template.ordertype." + orderModel.getOrderType().getCode(),
					Locale.of(orderModel.getUser().getSessionLanguage().getIsocode())));

			messageData.setMyAccount(messageService.getMessageForCode("text.template.order.confirmation",
					Locale.of(orderModel.getUser().getSessionLanguage().getIsocode())));

			final JnjGTOrderData laOrderData = (JnjGTOrderData) populatedOrderData;

			final List<OrderEntryData> entriesForModification = new ArrayList<>();
			for (final OrderEntryData laOrderEntry : laOrderData.getEntries())
			{
				if (laOrderEntry instanceof JnjLaOrderEntryData)
				{
					final JnjLaOrderEntryData laOrderEntryForModification = (JnjLaOrderEntryData) laOrderEntry;
					final String lineStatus = messageService.getMessageForCode(
							"text.email.orderentry.status." + ((JnjLaOrderEntryData) laOrderEntry).getStatus(),
							Locale.of(orderModel.getUser().getSessionLanguage().getIsocode()));

					laOrderEntryForModification.setStatus(lineStatus);
					laOrderEntryForModification.setCatalogId(((JnjLaOrderEntryData) laOrderEntry).getCatalogId());
					entriesForModification.add(laOrderEntryForModification);
				}
			}
			((JnjGTOrderData) populatedOrderData).setEntries(entriesForModification);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage("Send Email Error", "sendStatusChangeNotification()", "Exception", e,
					JnjLaEmailUtil.class);
			return false;
		}
		((JnjGTOrderData) populatedOrderData).setSapOrderNumber(orderModel.getSapOrderNumber());
		final VelocityContext context = new VelocityContext();
		final OrderAndMessageData orderAndMessageData = new OrderAndMessageData();


		messageData.setBaseUrl(baseUrl);
		messageData.setMediaLogoURL(mediaLogoURL);
		orderAndMessageData.setMessages(messageData);
		orderAndMessageData.setOrderData(populatedOrderData);
		context.put("ctx", orderAndMessageData);
		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

		LOGGER.info("Getting the vm file..");

		final String path = getClass().getResource(Config.getParameter("standard.order.vm.path")).getPath();

		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf("/")));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");
		LOGGER.info("Initializing Velocity Engine START");

		velocityEngine.init();

		LOGGER.info("Initializing Velocity Engine DONE");
		final StringWriter writer = new StringWriter();

		final Template template = velocityEngine.getTemplate(
				"email-orderStatusNotificationBody_" + orderModel.getUser().getSessionLanguage().getIsocode() + ".vm", "UTF-8");

		LOGGER.info("VM File fetched :: ");

		template.merge(context, writer);

		final String htmlStringBody = String.valueOf(writer);
		emailMessageModel.setBody(htmlStringBody);

		final StringWriter writerSub = new StringWriter();

		final Template templateSub = velocityEngine.getTemplate(
				"email-orderStatusNotificationSubject_" + orderModel.getUser().getSessionLanguage().getIsocode() + ".vm");
		templateSub.merge(context, writerSub);

		final String htmlStringSub = String.valueOf(writerSub);

		emailMessageModel.setSubject(htmlStringSub);
		final EmailAddressModel email = emailService.getOrCreateEmailAddressForEmail(customer.getEmail(), customer.getName());
		toFromAddress.add(email);

		if (null == emailMessageModel.getToAddresses())
		{
			emailMessageModel.setToAddresses(toFromAddress);
		}

		String fromEmail = jnjConfigService.getConfigValueById(ORDER_CONFIRMATION_FROM_EMAIL_KEY);
		String fromEmailDisplayName = jnjConfigService.getConfigValueById(ORDER_CONFIRMATION_FROM_EMAIL_NAME_KEY);

		final EmailAddressModel fromEmailAddressModel = emailService.getOrCreateEmailAddressForEmail(
				StringUtils.isNotBlank(fromEmail) ? fromEmail : ORDER_CONFIRMATION_FROM_EMAIL_DEFAULT,
				StringUtils.isNotBlank(fromEmailDisplayName) ? fromEmailDisplayName : ORDER_CONFIRMATION_FROM_EMAIL_NAME_DEFAULT);
		emailMessageModel.setFromAddress(fromEmailAddressModel);
		emailMessageModel.setReplyToAddress(fromEmail);

		modelService.save(emailMessageModel);
		return emailService.send(emailMessageModel);
	}


	private OrderConfirmationMessageData createMessageData()

	{
		final OrderConfirmationMessageData messageData = new OrderConfirmationMessageData();
		final String methodName = "createMessageData()";

		final Locale locale = Locale.of("en");

		try
		{
			messageData.setOrderQuantity(messageService.getMessageForCode("order.mail.confirmation.orderQuantity", locale));

			messageData.setOrderTotal(messageService.getMessageForCode("order.mail.confirmation.orderTotal", locale));
			messageData.setDeliveryMethod(messageService.getMessageForCode("order.mail.confirmation.deliveryMethod", locale));
			messageData.setDeliveryAddress(messageService.getMessageForCode("order.mail.confirmation.deliveryAddress", locale));
			messageData.setFreeDelivery(messageService.getMessageForCode("order.mail.confirmation.freeDelivery", locale));
			messageData.setPaymentDetails(messageService.getMessageForCode("order.mail.confirmation.paymentDetails", locale));
			messageData
					.setReceivedPromotions(messageService.getMessageForCode("order.mail.confirmation.receivedPromotions", locale));
			messageData.setOrderTotals(messageService.getMessageForCode("order.mail.confirmation.orderTotals", locale));
			messageData.setSubtotal(messageService.getMessageForCode("order.mail.confirmation.subtotal", locale));
			messageData.setSavings(messageService.getMessageForCode("order.mail.confirmation.savings", locale));
			messageData.setDelivery(messageService.getMessageForCode("order.mail.confirmation.delivery", locale));
			messageData.setTotalPrice(messageService.getMessageForCode("order.mail.confirmation.totalPrice", locale));
			messageData
					.setParagraphContactUs(messageService.getMessageForCode("order.mail.confirmation.paragraphContactUs", locale));
			messageData.setParagraphBody(messageService.getMessageForCode("order.mail.confirmation.paragraphBody", locale));
			messageData.setTotal(messageService.getMessageForCode("order.mail.confirmation.total", locale));
			messageData.setItemPrice(messageService.getMessageForCode("order.mail.confirmation.itemPrice", locale));
			messageData.setQuantity(messageService.getMessageForCode("order.mail.confirmation.quantity", locale));
			messageData
					.setPurchaseOrderNumber(messageService.getMessageForCode("order.mail.confirmation.purchaseOrderNumber", locale));
			messageData.setDear(messageService.getMessageForCode("order.mail.confirmation.dear", locale));
			messageData.setSignature(messageService.getMessageForCode("order.mail.confirmation.signature", locale));
			messageData.setFree(messageService.getMessageForCode("order.mail.confirmation.free", locale));
			messageData.setMyAccount(messageService.getMessageForCode("text.template.order.confirmation", locale));
		}
		catch (final BusinessException e)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.CREATE_MESSAGE_DATA, methodName,
					e.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(), e, className);
		}

		return messageData;

	}

}
