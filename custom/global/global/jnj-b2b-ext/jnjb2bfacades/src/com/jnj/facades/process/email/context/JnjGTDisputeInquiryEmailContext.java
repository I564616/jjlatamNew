/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTDisputeItemInquiryDto;
import com.jnj.core.dto.JnjGTDisputeOrderInquiryDto;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTDisputeInquiryProcessModel;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the email context for the dispute inquiry Email process.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTDisputeInquiryEmailContext extends CustomerEmailContext
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTDisputeInquiryEmailContext.class);
	private static final String BUSINESS_EMAIL_ADDRESS = "businessEmailAddress";
	private static final String DISPUTE_TYPES = "disputeTypes";
	@Autowired
	@Qualifier(value = "addressPopulator")
	private AddressPopulator addressPopulator;

	/**
	 * Instance of Dispute Inquiry Item data object.
	 */
	private final JnjGTDisputeItemInquiryDto disputeItemData = new JnjGTDisputeItemInquiryDto();

	/**
	 * Instance of Dispute Inquiry Order data object.
	 */
	private final JnjGTDisputeOrderInquiryDto disputeOrderData = new JnjGTDisputeOrderInquiryDto();

	/**
	 * Indicates if raised inquiry is an order dispute or item dispute.
	 */
	private Boolean disputeOrder;

	/**
	 * Constant for method name.
	 */
	private final String METHOD_INIT = "Context - init()";

	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		super.init(businessProcessModel, emailPageModel);

		if (businessProcessModel instanceof JnjGTDisputeInquiryProcessModel)
		{
			final JnjGTDisputeInquiryProcessModel disputeInquiryProcessModel = (JnjGTDisputeInquiryProcessModel) businessProcessModel;
			final List<String> DisputeTypes = new ArrayList<String>();
        	if (disputeInquiryProcessModel.getDisputeOrder() != null && disputeInquiryProcessModel.getDisputeOrder().booleanValue())
			{
				setDisputeOrder(true);
				disputeOrderData.setAccountNumber(disputeInquiryProcessModel.getAccountNumber());
				disputeOrderData.setInvoiceNumber(disputeInquiryProcessModel.getInvoiceNumber());
				disputeOrderData.setPurchaseOrderNumber(disputeInquiryProcessModel.getPurchaseOrderNumber());

				disputeOrderData.setPhoneNumber(disputeInquiryProcessModel.getPhoneNumber());
				disputeOrderData.setProductsAndlotInfo(disputeInquiryProcessModel.getProductsAndlotInfo());
				disputeOrderData.setProductsAndQuantity(disputeInquiryProcessModel.getProductsAndQuantity());
				disputeOrderData.setDisputedFees(disputeInquiryProcessModel.getDisputedFees());

				disputeOrderData.setCertificateAttached(disputeInquiryProcessModel.getCertificateAttached().booleanValue());
				disputeOrderData.setNewPONumber(disputeInquiryProcessModel.getNewPurchaseOrderNumber());
				disputeOrderData.setCorrectPurchaseOrderNumber(disputeInquiryProcessModel.getCorrectPurchaseOrderNumber());
				disputeOrderData.setKeepProductsShipped((disputeInquiryProcessModel.getKeepProductsShipped() == null) ? false
						: disputeInquiryProcessModel.getKeepProductsShipped().booleanValue());

				final AddressData shipToAddress = new AddressData();
				if (disputeInquiryProcessModel.getShipToAddress() != null)
				{
					addressPopulator.populate(disputeInquiryProcessModel.getShipToAddress(), shipToAddress);
				}

				final AddressData correctAddress = new AddressData();
				if (disputeInquiryProcessModel.getCorrectAddress() != null)
				{
					addressPopulator.populate(disputeInquiryProcessModel.getCorrectAddress(), correctAddress);
				}

				disputeOrderData.setCorrectAddress(correctAddress);
				disputeOrderData.setShipToAddress(shipToAddress);

				if (!StringUtils.isEmpty(disputeInquiryProcessModel.getCreatedFileName()))
				{
					put(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME, disputeInquiryProcessModel.getCreatedFileName());
					put("orderNumber",disputeInquiryProcessModel.getCreatedFileName());
					put(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH,
							Config.getParameter(Jnjb2bCoreConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_FILE_PATH_KEY));
					put(Jnjb2bCoreConstants.USE_DIRECT_PATH, "TRUE");
				}
				if (!disputeInquiryProcessModel.getProductsAndQuantity().isEmpty())
				{
					DisputeTypes.add("Over-Shipment");
				}
				if (!disputeInquiryProcessModel.getDisputedFees().isEmpty())
				{
					DisputeTypes.add("Dispute Fee");
				}
				if (!StringUtils.isEmpty(disputeInquiryProcessModel.getCorrectPurchaseOrderNumber()))
				{
					DisputeTypes.add("Incorrect PO");
				}
				if (disputeInquiryProcessModel.getCorrectAddress() != null)
				{
					DisputeTypes.add("Incorrect Address");
				}
				if (disputeInquiryProcessModel.getCertificateAttached().booleanValue())
				{
					DisputeTypes.add("Dispute Tax");
				}
				
					put("disputeOrderData",disputeOrderData);
			}
			else
			{
				setDisputeOrder(false);
				disputeItemData.setAccountNumber(disputeInquiryProcessModel.getAccountNumber());
				disputeItemData.setInvoiceNumber(disputeInquiryProcessModel.getInvoiceNumber());
				disputeItemData.setPurchaseOrderNumber(disputeInquiryProcessModel.getPurchaseOrderNumber());

				disputeItemData.setPhoneNumber(disputeInquiryProcessModel.getPhoneNumber());
				disputeItemData.setExpectedPrice(disputeInquiryProcessModel.getExpectedPrice());
				disputeItemData.setTotalDisputedAmount(disputeInquiryProcessModel.getTotalDisputedAmount());
				disputeItemData.setContractNumber(disputeInquiryProcessModel.getContractNumber());

				disputeItemData.setShortShippedProductCode(disputeInquiryProcessModel.getShortShippedProductCode());
				disputeItemData.setShortShippedOrderedQuantity(disputeInquiryProcessModel.getShortShippedOrderedQuantity());
				disputeItemData.setShortShippedReceivedQuantity(disputeInquiryProcessModel.getShortShippedReceivedQuantity());
				disputeItemData
						.setReplacementRequired((disputeInquiryProcessModel.getReplacementRequired() != null) ? disputeInquiryProcessModel
								.getReplacementRequired().booleanValue() : false);

				disputeItemData.setOverShippedProductCode(disputeInquiryProcessModel.getOverShippedProductCode());
				disputeItemData.setOverShippedOrderedQuantity(disputeInquiryProcessModel.getOverShippedOrderedQuantity());
				disputeItemData.setOverShippedReceivedQuantity(disputeInquiryProcessModel.getOverShippedReceivedQuantity());

				disputeItemData.setNewPurchaseOrderNumber(disputeInquiryProcessModel.getNewPurchaseOrderNumber());
				disputeItemData.setLotNumbers(disputeInquiryProcessModel.getLotNumbers());
				disputeItemData.setKeepProductsShipped((disputeInquiryProcessModel.getKeepProductsShipped() == null) ? false
						: disputeInquiryProcessModel.getKeepProductsShipped().booleanValue());
				
				/* AAOL-4059 */

				disputeItemData.setProductsAndQuantityPrice(disputeInquiryProcessModel.getProductsAndQuantityPrice());
				disputeItemData.setProductsAndDisputedPrice(disputeInquiryProcessModel.getProductsAndDisputedPrice());
				disputeItemData.setProductsAndCorrectPrice(disputeInquiryProcessModel.getProductsAndCorrectPrice());
				disputeItemData.setProductsAndContractNumber(disputeInquiryProcessModel.getProductsAndContractNumber());
				disputeItemData.setProductsAndInvoiceNumber(disputeInquiryProcessModel.getProductsAndInvoiceNumber());
				disputeItemData.setProductsAndQuantityOrdered(disputeInquiryProcessModel.getProductsAndQuantityOrdered());
				disputeItemData.setProductsAndQuantityRecieved(disputeInquiryProcessModel.getProductAndQtyReceived());

				disputeItemData.setProductsAndReplacement(disputeInquiryProcessModel.getProductAndReplacement());
				disputeItemData.setProductsAndInvoiceNumberShort(disputeInquiryProcessModel.getProductsAndInvoiceNumberShort());
				disputeItemData.setProductsAndQuantityOrderedOver(disputeInquiryProcessModel.getProductsAndQuantityOrderedOver());
				disputeItemData.setProductsAndQuantityReceivedOver(disputeInquiryProcessModel.getProductsAndQuantityRecievedOver());
				disputeItemData.setProductsAndLotNumbers(disputeInquiryProcessModel.getProductsAndLotNumbers());
				disputeItemData.setProductsAndInvoiceNumberOver(disputeInquiryProcessModel.getProductsAndInvoiceNumberOver());
				disputeItemData.setDisputeInvoiceNumber(disputeInquiryProcessModel.getDisputeInvoiceNumber());
				/* AAOL-4059 */
				if (!StringUtils.isEmpty(disputeInquiryProcessModel.getTotalDisputedAmount()))
				{
					DisputeTypes.add("Pricing Dispute");
				}
				if (!StringUtils.isEmpty(disputeInquiryProcessModel.getShortShippedProductCode()))
				{
					DisputeTypes.add("Short-Shipped");
				}
				if (!StringUtils.isEmpty(disputeInquiryProcessModel.getOverShippedProductCode()))
				{
					DisputeTypes.add("Over-Shipment");
				}
	                  put("disputeItemData",disputeItemData);
			}
			
			
			final CustomerModel customerModel = getCustomer(businessProcessModel);
        	final JnJB2bCustomerModel jnjCustomerModel = (JnJB2bCustomerModel) customerModel;
			/** Set the To Email address i.e. company CSR email **/
			put(EMAIL, Config.getParameter(Jnjb2bCoreConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_CSR_EMAIL_KEY));

			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_FROM_EMAIL_ADDRESS));
			
			if (customerModel instanceof JnJB2bCustomerModel)
			{
				
				put(BUSINESS_EMAIL_ADDRESS, jnjCustomerModel.getEmail());
			}
			else
			{
				put(BUSINESS_EMAIL_ADDRESS, getCustomerEmailResolutionService().getEmailForCustomer(customerModel));
			}
			
			//TODO [AR]: To be confirmed. 
			/** Set FROM_DISPLAY_NAME value based on the customer name, if not present then customer email id. **/
			put(FROM_DISPLAY_NAME, jnjCustomerModel.getName());
			put(DISPUTE_TYPES, DisputeTypes);
		}

	}

	public AddressPopulator getAddressPopulator()
	{
		return addressPopulator;
	}

	public JnjGTDisputeItemInquiryDto getDisputeItemData()
	{
		return disputeItemData;
	}

	public JnjGTDisputeOrderInquiryDto getDisputeOrderData()
	{
		return disputeOrderData;
	}

	/**
	 * @return the disputeOrder
	 */
	public Boolean getDisputeOrder()
	{
		return disputeOrder;
	}

	/**
	 * @param disputeOrder
	 *           the disputeOrder to set
	 */
	public void setDisputeOrder(final Boolean disputeOrder)
	{
		this.disputeOrder = disputeOrder;
	}


}
