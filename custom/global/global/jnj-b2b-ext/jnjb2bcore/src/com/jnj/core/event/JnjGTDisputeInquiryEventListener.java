/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.dto.JnjGTDisputeItemInquiryDto;
import com.jnj.core.dto.JnjGTDisputeOrderInquiryDto;
import com.jnj.core.model.JnjGTDisputeInquiryProcessModel;
//import com.jnj.na.facades.order.impl.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * Event Listener class responsible publish the <code>JnjGTDisputeInquiryEvent</code> along with setting dispute inquiry
 * data from event to the business process model.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTDisputeInquiryEventListener extends AbstractSiteEventListener<JnjGTDisputeInquiryEvent>
{
	protected static final Logger LOGGER = Logger.getLogger(JnjGTDisputeInquiryEventListener.class);

	protected ModelService modelService;

	protected BusinessProcessService businessProcessService;
	
	@Autowired
	@Qualifier(value = "addressReversePopulator")
	private AddressReversePopulator addressReversePopulator;
	
	@Autowired
	private UserService userService;

	@Override
	protected void onSiteEvent(final JnjGTDisputeInquiryEvent event)
	{
		final JnjGTDisputeInquiryProcessModel disputeInquiryProcessModel = (JnjGTDisputeInquiryProcessModel) businessProcessService
				.createProcess("disputeInquiryEmailProcess" + "-" + System.currentTimeMillis(), "jnjGTDisputeInquiryEmailProcess");

		disputeInquiryProcessModel.setCustomer(event.getCustomer());
		disputeInquiryProcessModel.setStore(event.getBaseStore());
		disputeInquiryProcessModel.setSite(event.getSite());
		disputeInquiryProcessModel.setCurrency(event.getCurrency());
		disputeInquiryProcessModel.setLanguage(event.getLanguage());


		//LOG
		JnjGTDisputeItemInquiryDto disputeItemData = null;
		JnjGTDisputeOrderInquiryDto disputeOrderData = null;
		if (event.isDisputeOrder() && event.getDisputeOrderInquiryData() != null)
		{
			disputeOrderData = event.getDisputeOrderInquiryData();
			disputeInquiryProcessModel.setDisputeOrder(Boolean.TRUE);
			disputeInquiryProcessModel.setAccountNumber(disputeOrderData.getAccountNumber());
			disputeInquiryProcessModel.setPurchaseOrderNumber(disputeOrderData.getPurchaseOrderNumber());

			disputeInquiryProcessModel.setPhoneNumber(disputeOrderData.getPhoneNumber());
			disputeInquiryProcessModel.setInvoiceNumber(disputeOrderData.getInvoiceNumber());
			
			disputeInquiryProcessModel.setNewPurchaseOrderNumber(disputeOrderData.getNewPONumber());
			disputeInquiryProcessModel.setProductsAndlotInfo(disputeOrderData.getProductsAndlotInfo());
			disputeInquiryProcessModel.setProductsAndQuantity(disputeOrderData.getProductsAndQuantity());
			disputeInquiryProcessModel.setDisputedFees(disputeOrderData.getDisputedFees());
			disputeInquiryProcessModel.setCorrectPurchaseOrderNumber(disputeOrderData.getCorrectPurchaseOrderNumber());
			disputeInquiryProcessModel.setKeepProductsShipped(disputeOrderData.isKeepProductsShipped());
			disputeInquiryProcessModel.setCertificateAttached(disputeOrderData.isCertificateAttached());
			disputeInquiryProcessModel.setCreatedFileName(disputeOrderData.getCreatedFileName());

			
			if (disputeOrderData.getShipToAddress() != null)
			{
				final AddressModel shipAddress = new AddressModel();
				addressReversePopulator.populate(disputeOrderData.getShipToAddress(), shipAddress);
				shipAddress.setOwner(userService.getCurrentUser());
				modelService.save(shipAddress);
				disputeInquiryProcessModel.setShipToAddress(shipAddress);
			}
			// GT missing functionality - Incorrect address - Dispute Order
			// disputeInquiryProcessModel.setCorrectAddress(event.getCorrectAddress());
			
			if(disputeOrderData.getCorrectAddress()!=null){
			final AddressModel correctAddress = new AddressModel();
			addressReversePopulator.populate(disputeOrderData.getCorrectAddress(), correctAddress);
			correctAddress.setOwner(userService.getCurrentUser());
			modelService.save(correctAddress);
			
			disputeInquiryProcessModel.setCorrectAddress(correctAddress);
			}
		}
		else
		{
			disputeItemData = event.getDisputeItemInquiryData();
			disputeInquiryProcessModel.setDisputeOrder(Boolean.FALSE);
			disputeInquiryProcessModel.setAccountNumber(disputeItemData.getAccountNumber());
			disputeInquiryProcessModel.setPurchaseOrderNumber(disputeItemData.getPurchaseOrderNumber());

			disputeInquiryProcessModel.setPhoneNumber(disputeItemData.getPhoneNumber());
			disputeInquiryProcessModel.setInvoiceNumber(disputeItemData.getInvoiceNumber());
			disputeInquiryProcessModel.setTotalDisputedAmount(disputeItemData.getTotalDisputedAmount());
			disputeInquiryProcessModel.setExpectedPrice(disputeItemData.getExpectedPrice());
			disputeInquiryProcessModel.setContractNumber(disputeItemData.getContractNumber());

			disputeInquiryProcessModel.setShortShippedProductCode(disputeItemData.getShortShippedProductCode());
			disputeInquiryProcessModel.setShortShippedOrderedQuantity(disputeItemData.getShortShippedOrderedQuantity());
			disputeInquiryProcessModel.setShortShippedReceivedQuantity(disputeItemData.getShortShippedReceivedQuantity());

			disputeInquiryProcessModel.setOverShippedProductCode(disputeItemData.getOverShippedProductCode());
			disputeInquiryProcessModel.setOverShippedOrderedQuantity(disputeItemData.getOverShippedOrderedQuantity());
			disputeInquiryProcessModel.setOverShippedReceivedQuantity(disputeItemData.getOverShippedReceivedQuantity());

			disputeInquiryProcessModel.setReplacementRequired(disputeItemData.isReplacementRequired());
			disputeInquiryProcessModel.setKeepProductsShipped(disputeItemData.isKeepProductsShipped());
			disputeInquiryProcessModel.setLotNumbers(disputeItemData.getLotNumbers());
			disputeInquiryProcessModel.setNewPurchaseOrderNumber(disputeItemData.getNewPurchaseOrderNumber());
			
			/*AAOL-4059*/
			disputeInquiryProcessModel.setProductsAndQuantityPrice(disputeItemData.getProductsAndQuantityPrice());
			LOGGER.info("#################6");
			disputeInquiryProcessModel.setProductsAndDisputedPrice(disputeItemData.getProductsAndDisputedPrice());
			disputeInquiryProcessModel.setProductsAndCorrectPrice(disputeItemData.getProductsAndCorrectPrice());
			disputeInquiryProcessModel.setProductsAndContractNumber(disputeItemData.getProductsAndContractNumber());
			disputeInquiryProcessModel.setProductsAndInvoiceNumber(disputeItemData.getProductsAndInvoiceNumber());
			disputeInquiryProcessModel.setProductsAndQuantityOrdered(disputeItemData.getProductsAndQuantityOrdered());

			disputeInquiryProcessModel.setProductAndQtyReceived(disputeItemData.getProductsAndQuantityRecieved());
			disputeInquiryProcessModel.setProductAndReplacement(disputeItemData.getProductsAndReplacement());
			disputeInquiryProcessModel.setProductsAndInvoiceNumberShort(disputeItemData.getProductsAndInvoiceNumberShort());
			disputeInquiryProcessModel.setProductsAndQuantityOrderedOver(disputeItemData.getProductsAndQuantityOrderedOver());
			disputeInquiryProcessModel
					.setProductsAndQuantityRecievedOver(disputeItemData.getProductsAndQuantityReceivedOver());
			disputeInquiryProcessModel.setProductsAndLotNumbers(disputeItemData.getProductsAndLotNumbers());
			disputeInquiryProcessModel.setProductsAndInvoiceNumberOver(disputeItemData.getProductsAndInvoiceNumberOver());

			disputeInquiryProcessModel.setDisputeType(disputeItemData.getDisputeType());
			disputeInquiryProcessModel.setDisputeItemNumber(disputeItemData.getDisputeItemNumber());
			disputeInquiryProcessModel.setOrderCode(disputeItemData.getOrderCode());
			disputeInquiryProcessModel.setDisputeInvoiceNumber(disputeItemData.getDisputeInvoiceNumber());

			disputeInquiryProcessModel.setDate(new Date());
			
		}

		try
		{
			modelService.save(disputeInquiryProcessModel);
		}
		catch (final ModelSavingException e)
		{
			LOGGER.error("Saving 'JnjGTDisputeInquiryProcessModel' has caused an exception: " + e.getMessage());
		}

		businessProcessService.startProcess(disputeInquiryProcessModel);
	}

	@Override
	protected boolean shouldHandleEvent(final JnjGTDisputeInquiryEvent arg0)
	{
		return true;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}
