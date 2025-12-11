/**
 *
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTOrderHistoryData;

import de.hybris.platform.core.model.user.AddressModel;

import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.model.JnjGTSurgeryInfoModel;
import com.jnj.core.services.JnjGTOrderService;


/**
 * POpulator class responsible for converting and populating values from source instance <code>OrderModel</code> into
 * target instance <code>JnjGTOrderHistoryData</code>
 *
 * @author akash.rawat
 */
public class JnjGTOrderHistoryPopulator extends OrderHistoryPopulator
{
	private static final String MULTIPLE = "order.history.multiple.franchise";

	@Autowired
	private JnjGTOrderService jnjGTOrderService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	protected EnumerationService enumerationService;
	
	@Autowired
	protected I18NService i18nService;
	@Autowired
	protected MessageSource messageSource;
	
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	
	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);

		if (target instanceof JnjGTOrderHistoryData)
		{
			final JnjGTOrderHistoryData data = (JnjGTOrderHistoryData) target;
			final String emptyField = new String();

			data.setSapOrderNumber((source.getSapOrderNumber() != null) ? source.getSapOrderNumber(): source.getOrderNumber() );
			data.setOrdertype((source.getOrderType() != null) ? source.getOrderType().toString() : emptyField);
			data.setChannel((source.getPoType() != null) ? source.getPoType() : emptyField);

			String shipToNumber = null;
			/*Adding the "shipToNumberForConsumer" parameter for CHD-104 */
			String shipToNumberForConsumer = null;
			if (source.getDeliveryAddress() instanceof AddressModel)
			{
				final AddressModel shippingAddress = (AddressModel) source.getDeliveryAddress();
				shipToNumber = shippingAddress.getJnJAddressId();
				data.setShipToName(shippingAddress.getFirstname());
				final String regionString = (null != shippingAddress.getRegion()) ? Jnjb2bCoreConstants.CONST_COMMA
						+ Jnjb2bCoreConstants.SPACE + shippingAddress.getRegion().getName() : "";
				shipToNumberForConsumer = shippingAddress.getTown() + regionString;
			}
			if (Jnjb2bCoreConstants.MDD.equals(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)))
			{
				data.setShipToNumber((shipToNumber != null) ? shipToNumber : String.valueOf("0"));
			}
			else
			{
				data.setShipToNumber((shipToNumberForConsumer != null) ? shipToNumberForConsumer : String.valueOf("0"));
			}

			final JnjGTSurgeonModel surgeon = source.getSurgeon();
			// If surgeon model is present
			if (surgeon != null)
			{
				final StringBuilder surgeonName = new StringBuilder();

				// If first name is not blank
				if (StringUtils.isNotBlank(surgeon.getFirstName()))
				{
					surgeonName.append(surgeon.getFirstName()).append(" ");
				}
				// If last name is not blank
				if (StringUtils.isNotBlank(surgeon.getLastName()))
				{
					surgeonName.append(surgeon.getLastName());
				}
				data.setSurgeonName(surgeonName.toString());
			}
			
			//Changes for Order History Enhancement AAOL-4732,4796
			final JnjGTSurgeryInfoModel surgeryModel = source.getSurgeryInfo();
			
			if(surgeryModel!=null){
				
				if(surgeryModel.getCaseNumber()!=null){
					data.setPatientID(surgeryModel.getCaseNumber());
				}
				if(surgeryModel.getCaseDate()!=null){
					data.setCaseDate(surgeryModel.getCaseDate().toString());
				}
				
			}
			
			Set<String> franchiseList = new java.util.HashSet<String>();
			
			for (AbstractOrderEntryModel orderEntry:source.getEntries()){
				
				
				if(orderEntry!=null){
					
					String franchiseName = retrieveSuperCategory(orderEntry);
					if(franchiseName!=null){
						franchiseList.add(franchiseName);
					}
					
				}
			}
			if(franchiseList!=null && !(franchiseList.isEmpty())){
				if(franchiseList.size()>1){
					//If the order entry is not part of multiple franchise we will set the value for the column as "Multiple".
					data.setFranchise(Jnjb2bCoreConstants.MULTIPLE);
				}		
				else{
					data.setFranchise(franchiseList.iterator().next());
				}
			}else{
				//If the order entry is not part of any franchise we will set the value for the column as blank.
				data.setFranchise("");
			}
			
			
			/** Checking if MDD or Consumer **/
			if (Jnjb2bCoreConstants.MDD.equals(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)))
			{
				/** Setting Bill to address **/
				if (null != source.getPaymentAddress())
				{
					final StringBuilder billingAddress = new StringBuilder();
					if (StringUtils.isNotEmpty(source.getPaymentAddress().getCompany()))
					{
						billingAddress.append(source.getPaymentAddress().getCompany() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					if (StringUtils.isNotEmpty(source.getPaymentAddress().getLine1()))
					{
						billingAddress.append(source.getPaymentAddress().getLine1() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					if (StringUtils.isNotEmpty(source.getPaymentAddress().getLine2()))
					{
						billingAddress.append(source.getPaymentAddress().getLine2() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					if (null != source.getPaymentAddress().getCountry()
							&& StringUtils.isNotEmpty(source.getPaymentAddress().getCountry().getName()))
					{
						billingAddress.append(source.getPaymentAddress().getCountry().getName() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					/** Checking if the address string builder has not ended with a comma and a space **/
					if (String.valueOf(billingAddress).endsWith(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE))
					{
						data.setBillToName(billingAddress.substring(0, billingAddress.length() - 3));
					}
					else
					{
						data.setBillToName(String.valueOf(billingAddress));
					}
					/** Setting Bill to Number **/
					data.setBillToNumber(source.getPaymentAddress().getJnJAddressId());
				}
			}
			else
			{
				/** Setting Sold to address **/
				if (null != source.getUnit() && null != source.getUnit().getContactAddress())
				{
					final StringBuilder billingAddress = new StringBuilder();

					if (StringUtils.isNotEmpty(source.getUnit().getContactAddress().getCompany()))
					{
						billingAddress.append(source.getUnit().getContactAddress().getCompany() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					if (StringUtils.isNotEmpty(source.getUnit().getContactAddress().getLine1()))
					{
						billingAddress.append(source.getUnit().getContactAddress().getLine1() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					if (StringUtils.isNotEmpty(source.getUnit().getContactAddress().getLine2()))
					{
						billingAddress.append(source.getUnit().getContactAddress().getLine2() + Jnjb2bCoreConstants.CONST_COMMA
								+ Jnjb2bCoreConstants.SPACE);
					}
					if (null != source.getUnit().getContactAddress().getCountry()
							&& StringUtils.isNotEmpty(source.getUnit().getContactAddress().getCountry().getName()))
					{
						billingAddress.append(source.getUnit().getContactAddress().getCountry().getName()
								+ Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
					}
					/** Checking if the address string builder has not ended with a comma and a space **/
					if (String.valueOf(billingAddress).endsWith(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE))
					{
						data.setBillToName(billingAddress.substring(0, billingAddress.length() - 3));
					}
					else
					{
						data.setBillToName(String.valueOf(billingAddress));
					}
					/** Setting Bill to Number **/
					data.setBillToNumber(source.getUnit().getContactAddress().getJnJAddressId());
				}
			}
			data.setStatusDisplay((source.getStatus() != null) ? enumerationService.getEnumerationName(source.getStatus())
					: StringUtils.EMPTY);
			data.setSurgeonUpdatInd(jnjGTOrderService.isSurgeonUpdateEligible(source));
			data.setPoNumberUpdateInd(jnjGTOrderService.isPurchaseOrderUpdateEligible(source));
			data.setOrderOnPAHold(source.isOrderOnPAHold());

			
			if (source.getShippingDetails() != null && !source.getShippingDetails().isEmpty())
			{
				for (final JnjGTShippingDetailsModel shippingDetail : source.getShippingDetails())
				{
					if (null != shippingDetail.getActualDeliveryDate())
					{
						((JnjGTOrderHistoryData) target).setDeliveryDate(shippingDetail.getActualDeliveryDate().toString());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * This method is used to retrieve categories of order entries
	 * @param orderEntry
	 * @return
	 */
	private String retrieveSuperCategory(AbstractOrderEntryModel orderEntry) {
		
		String categoryName = null;
		/** iterating over the second level of super categories **/
		if(orderEntry!=null){
			if(orderEntry.getProduct()!=null){
				if(orderEntry.getProduct().getSupercategories()!=null && !(orderEntry.getProduct().getSupercategories().isEmpty())){
					for (final CategoryModel secondLevelSuperCategory : orderEntry.getProduct().getSupercategories())
					{
						/** iterating over the first level of super categories **/
						for (final CategoryModel firstLevelSuperCategory : secondLevelSuperCategory.getSupercategories())
						{
							/** operating company name fetched **/
							categoryName = firstLevelSuperCategory.getName();
							

							/** operating company name fetched hence breaking the loop **/
							break;
						}
						if (null != categoryName)
						{
							/** operating company name fetched hence breaking the loop **/
							break;
						}
					}
				}
			}
			
			
		}
		
		return categoryName;
	}
	
}