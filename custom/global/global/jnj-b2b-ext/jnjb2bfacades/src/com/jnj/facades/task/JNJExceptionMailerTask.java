/*******************************************************************
 * Classification : GE Confidential
 * Project Name   : rmfacades
 * File Name 	   : RMExceptionMailerTask.java
 * Description    :  
 * Author         : iGATE Global Solutions Ltd.
 * Last Edited By : iGATE Global Solutions Ltd.
 * Version        : 1.0
 * Created on     : Dec 2, 2013
 * History        : 
 * Modified By    : 
 * Change Description: 
 * Copyright (C) 2013 General Electric Company. All rights reserved
 ******************************************************************/

package com.jnj.facades.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jnj.facades.data.JNJExceptionData;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;


/**
 * @author 711552
 * 
 */
public class JNJExceptionMailerTask implements TaskRunner<TaskModel>
{
	private SessionService sessionService;

	private ModelService modelService;

	private DefaultEmailService emailService;

	private UserService userService;


	public void run(final TaskService taskService, final TaskModel task) throws RetryLaterException
	{

		modelService = (ModelService) ServicelayerUtils.getApplicationContext().getBean("modelService");
		emailService = (DefaultEmailService) ServicelayerUtils.getApplicationContext().getBean("emailService");

		final EmailMessageModel emailMessageModel = modelService.create(EmailMessageModel.class);
		final List<EmailAddressModel> toaddress = new ArrayList<EmailAddressModel>();
		EmailAddressModel addressModel = null;
		final String toEmailIDList = Config.getParameter("techSupportEmails");
		if (StringUtils.isNotBlank(toEmailIDList))
		{
			final String[] toList = toEmailIDList.trim().split(";");
			for (int iCount = 0; iCount < toList.length; iCount++)
			{
				addressModel = modelService.create(EmailAddressModel.class);
				final String emailaddress = toList[iCount];
				final String[] displayName = emailaddress.split(".", 1);
				addressModel = emailService.getOrCreateEmailAddressForEmail(emailaddress, displayName[0]);
				modelService.save(addressModel);
				toaddress.add(addressModel);
			}
		}
		EmailAddressModel fromAddressModel = modelService.create(EmailAddressModel.class);
		fromAddressModel = emailService.getOrCreateEmailAddressForEmail("Exception@jnjportal.com",
				"JNJ Exception Alert - " + Config.getParameter("deployment.env"));
		final JNJExceptionData userData = (JNJExceptionData) task.getContext();
		emailMessageModel
				.setBody("<body style='background-color:#FFFFF0;margin:20px;'><table style='background-color:#FFFFFF;'><tr><td><table><tr><td>Exception occured on :"
						+ userData.getExceptionTime()
						+ " </td></tr></table></br>Exception Details : </br><table><tr><td>"
						+ userData.getExceptionString().substring(0,3700) + "</br></td></tr><table></tr></td></table></body>");
		emailMessageModel.setToAddresses(toaddress);
		StringBuffer subject = new StringBuffer();
		if(null!=userData.getCustomerData() && StringUtils.isNotBlank(userData.getCustomerData().getUid())){
			subject.append(userData.getCustomerData().getUid());
		}
		if(null!=userData.getCustomerData() && StringUtils.isNotBlank(userData.getCustomerData().getFirstName())){
			subject.append(", "+userData.getCustomerData().getFirstName());
		}
		if(null!=userData.getCustomerData() && StringUtils.isNotBlank(userData.getCustomerData().getLastName())){
			subject.append(" "+userData.getCustomerData().getLastName());
		}
		emailMessageModel.setSubject("Exception occured in JNJ ["+Config.getParameter("deployment.env")+"] Portal :"+subject.toString());
		emailMessageModel.setFromAddress(fromAddressModel);
		emailMessageModel.setReplyToAddress("Exception@jnjportal.com");
		modelService.save(emailMessageModel);
		emailService.send(emailMessageModel);
		if (emailMessageModel.isSent())
		{
			modelService.remove(emailMessageModel);
		}
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the emailService
	 */
	public DefaultEmailService getEmailService()
	{
		return emailService;
	}

	/**
	 * @param emailService
	 *           the emailService to set
	 */
	public void setEmailService(final DefaultEmailService emailService)
	{
		this.emailService = emailService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.task.TaskRunner#handleError(de.hybris.platform.task.TaskService,
	 * de.hybris.platform.task.TaskModel, java.lang.Throwable)
	 */
	@Override
	public void handleError(final TaskService arg0, final TaskModel arg1, final Throwable arg2)
	{
		// YTODO Auto-generated method stub

	}

}
