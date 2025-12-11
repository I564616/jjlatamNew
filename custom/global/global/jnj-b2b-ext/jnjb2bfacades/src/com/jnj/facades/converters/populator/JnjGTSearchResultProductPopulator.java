/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTConsumerSpecificationData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.services.MessageService;
import com.jnj.core.services.JnJGTProductService;


/**
 * This is used to fetch the data from solr server as per JNJ NA req
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSearchResultProductPopulator extends SearchResultProductPopulator
{
	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected UserService userService;

	@Resource(name="b2bCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjGTB2BCommerceUserService;

	@Autowired
	protected MessageService messageService;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		// Pull the values directly from the SearchResult object

		populatePrices(source, target);
 
		if (target instanceof JnjGTProductData)
		{
			boolean isSalesRepDivisionCompatible = true;
			final JnjGTProductData jnjGTProductData = (JnjGTProductData) target;
			final String gtin = this.<String> getValue(source, "gtin");
			// From session get the current site i.e. MDD/CONS
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			if (StringUtils.isNotBlank(gtin))
			{
				jnjGTProductData.setGtin(gtin);
			}
			// Add field to map
			setUnits(source, jnjGTProductData);
			final String productStatusCode = this.<String> getValue(source,"productStatusCode");
			final String modStatus = this.<String> getValue(source, "modStatus");
			
			/*Boolean kitIndVar = false;
			if(null != this.<Boolean> getValue(source, "kitInd") ){
				kitIndVar = this.<Boolean> getValue(source, "kitInd");
			}
			*/ 
			
			final Boolean isKitInd = this.<Boolean> getValue(source, "kitInd"); 
            boolean isKitInd1 = false; 
            if (isKitInd != null) 
            { 
                    isKitInd1 = this.<Boolean> getValue(source, "kitInd").booleanValue(); 
            } 
           jnjGTProductData.setIsKitProduct(isKitInd1);
			
			
		
			/*jnjGTProductData.setIsKitProduct(kitIndVar.booleanValue());*/
			
            String  launchStatus =null;
           // for JJEPIC-128
			final Date firstShipEffectDate = this.<Date> getValue(source, "firstShipEffectDate");
			final Date newProductStartDate = this.<Date> getValue(source, "newProductStartDate");
			
			if ((firstShipEffectDate != null)
				   && (productStatusCode != null) 
				   &&  (modStatus != null)  
				   &&  (productStatusCode.equals(Jnjgtb2bCONSConstants.PRODUCT_CODE_STATUS_CON1) || productStatusCode.equals(Jnjgtb2bCONSConstants.PRODUCT_CODE_STATUS_CON2))
				   && currentSite.equals(Jnjb2bCoreConstants.CONS))
				{
					final Date today = new Date();
					//final Date todayPlusOne = DateUtils.addDays(today, 1);
					final Date todayPlusThirty = DateUtils.addDays(today, 30);
					final Date todayMinusSixM = DateUtils.addDays(today, -181);
				   // final 	boolean a = firstShipEffectDate.after(todayMinusSixM);
				  //  final boolean b = newProductStartDate.before(todayPlusThirty);
		        if (firstShipEffectDate != null && firstShipEffectDate.before(today)  && firstShipEffectDate.after(todayMinusSixM))
					{
						launchStatus = Jnjgtb2bCONSConstants.NEW;
					}
					else if(firstShipEffectDate != null && firstShipEffectDate.after(today) && firstShipEffectDate.before(todayPlusThirty))
					{
						launchStatus = Jnjgtb2bCONSConstants.COMING_SOON;
					}else if(productStatusCode.equals(Jnjgtb2bCONSConstants.PRODUCT_CODE_STATUS_DIS_CON)){
						launchStatus = Jnjgtb2bCONSConstants.DISCOUNTINUE;
					}
				}/*else if(currentSite.equals(Jnjb2bCoreConstants.MDD) &&  productStatusCode.equals(Jnjgtb2bCONSConstants.PRODUCT_CODE_STATUS_DIS_CON)){
					//here logic for mdd 
					launchStatus = Jnjgtb2bCONSConstants.DISCOUNTINUE;
				   } */
			/*JnJProductModel jnjproductmodel = this.<JnJProductModel> getValue(source,"code");
			launchStatus = getEpicCONSLaunchStatus(jnjproductmodel);*/
			
				jnjGTProductData.setLaunchStatus(launchStatus);
				
            
			final Boolean isSal = this.<Boolean> getValue(source, "isSaleable");
			boolean isProductSaleable = false;
			if (isSal != null)
			{
				isProductSaleable = this.<Boolean> getValue(source, "isSaleable").booleanValue();
			}

		
			 
			// If site is MDD, variant needs to be searched upon GTIN and Base Material Number
			if (currentSite.equals(Jnjb2bCoreConstants.MDD))
			{
				// Populate price for MDD product
				setPriceMdd(source, jnjGTProductData);

				// Populate the division compatibility indicator, if the user is a sales rep with place order privilege
				final Boolean isSalesRepUser = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);
				if (isSalesRepUser != null && isSalesRepUser.booleanValue())
				{
					isSalesRepDivisionCompatible = isProductDivisionSameAsUserDivision(this.<String> getValue(source, "salesOrgCode"));
				}

				
				if (StringUtils.isNotBlank(modStatus) && modStatus.equals(JnjGTModStatus.DISCONTINUED.getCode()))
				{
					jnjGTProductData.setDiscontinued(true);
				}
				else
				{
					jnjGTProductData.setDiscontinued(false);	
				}
				//AAOL-4150
				if(StringUtils.isNotBlank(modStatus) && modStatus.equals(JnjGTModStatus.OBSOLETE.getCode()))
				{
					jnjGTProductData.setObsolete(true);
				}
				else
				{
					jnjGTProductData.setObsolete(false);
				}
			 
				// If isPartialSaleable is false, check for international affiliate
				if (!isProductSaleable)
				{
					isProductSaleable = this.<Boolean> getValue(source, "availableInd") != null
							&& this.<Boolean> getValue(source, "availableInd").booleanValue()
							&& JnjGTModStatus.ACTIVE.equals(JnjGTModStatus.valueOf(modStatus))
							&& (jnjGTB2BUnitService.isCustomerInternationalAff() || (this.<Boolean> getValue(source, "jnjPortalInd") != null && this
									.<Boolean> getValue(source, "jnjPortalInd").booleanValue()));
				}

				final String message = (!isProductSaleable) ? Jnjgtb2bMDDConstants.Cart.NOT_SALEABLE
						: (!isSalesRepDivisionCompatible ? Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR : null);

				final String errorMessage = (message != null) ? messageService.getMessageForCode(message, null, target.getCode())
						: null;
				jnjGTProductData.setErrorMessage(errorMessage);

			}
			// If site is CONS, variant needs to be searched upon UPC and Base Material Number
			else if (currentSite.equals(Jnjb2bCoreConstants.CONS))
			{
				// Populate price for CONS product
				setPriceCons(source, jnjGTProductData);
				jnjGTProductData.setFirstShipEffective(this.<Date> getValue(source, "firstShipEffectDate"));
				jnjGTProductData.setStatus(this.<String> getValue(source, "currentStatus"));
				final String mdmDescription = this.<String> getValue(source, "mdmDescription");
				if (StringUtils.isEmpty(jnjGTProductData.getName()) && StringUtils.isNotEmpty(mdmDescription))
				{
					jnjGTProductData.setName(mdmDescription);
				}
				isProductSaleable=true;

			}
			// If site is PCM
			/*else if (currentSite.equals(JnjPCMCoreConstants.PCM))
			{
				jnjGTProductData.setStatus(this.<String> getValue(source, "currentStatus"));
				jnjGTProductData.setFirstShipEffective(this.<Date> getValue(source, "firstShipEffectDate"));
				jnjGTProductData.setImageAvailableInd(this.<Integer> getValue(source, "imageAvailableInd"));
				jnjGTProductData.setUpc(this.<String> getValue(source, "upc"));
				final JnjGTConsumerSpecificationData consumerSpecificationData = new JnjGTConsumerSpecificationData();
				consumerSpecificationData.setBrand(this.<String> getValue(source, "pcmBrand"));
				jnjGTProductData.setConsumerSpecification(consumerSpecificationData);
			}*/

			// If user is sales rep with division compatibility AND product is saleable, set the flag to true
			jnjGTProductData.setSaleableInd(Boolean.valueOf(isProductSaleable));
			jnjGTProductData.setSalesRepCompatibleInd(isSalesRepDivisionCompatible);

			jnjGTProductData.setBaseMaterialNumber(this.<String> getValue(source, "baseMaterialNumber"));
			jnjGTProductData.setProductVolume(this.<String> getValue(source, "volume"));
			jnjGTProductData.setProductWeight(this.<String> getValue(source, "weight"));
			
	
		}
	}

	/**
	 * @param source
	 * @param jnjGTProductData
	 */
	protected void setUnits(final SearchResultValueData source, final JnjGTProductData jnjGTProductData)
	{
		final String unitOfMeasure = this.<String> getValue(source, "uom");
		jnjGTProductData.setCode(source.getValues().get("baseMaterialNumber").toString());
		if (StringUtils.isNotEmpty(unitOfMeasure))
		{
			final String[] units = unitOfMeasure.split(Jnjb2bCoreConstants.Solr.COLON);
			//uom pattern  is - shipUOM:definationOfEaches:uomOfEaches
			if (units.length == 3)
			{
				jnjGTProductData.setDeliveryUnit(units[0]);
				jnjGTProductData.setNumerator(units[1]);
				jnjGTProductData.setSalesUnit(units[2]);
			}
		}

	}

	protected void setPriceMdd(final SearchResultValueData source, final JnjGTProductData jnjGTProductData)
	{
		final Collection<String> price = this.<Collection<String>> getValue(source, "price");
		if (CollectionUtils.isNotEmpty(price))
		{
			String defaultPrice = null;
			boolean priceSet = false;
			for (final Iterator iterator = price.iterator(); iterator.hasNext();)
			{
				final String priceString = (String) iterator.next();
				if (StringUtils.isNotEmpty(priceString))
				{
					final String[] priceValues = priceString.split(Jnjb2bCoreConstants.Solr.COLON);
					if (priceValues.length == 3)
					{
						final String upg = jnjGTB2BUnitService.getUPGCode(null, priceValues[1]);
						if (priceValues[0].equals(Jnjb2bCoreConstants.Solr.DEFAULTUG))
						{
							defaultPrice = priceValues[2];
						}

						if (StringUtils.isNotEmpty(upg) && priceValues[0].equals(upg))
						{
							jnjGTProductData.setListPrice(priceValues[2]);
							priceSet = true;
							break;
						}
					}
					if (!priceSet && null != defaultPrice)
					{
						jnjGTProductData.setListPrice(defaultPrice);
					}
				}
			}
		}
	}

	protected void setPriceCons(final SearchResultValueData source, final JnjGTProductData jnjGTProductData)
	{
		final Collection<String> prices = this.<Collection<String>> getValue(source, "price");
		if (CollectionUtils.isNotEmpty(prices))
		{
			for (final Iterator iterator = prices.iterator(); iterator.hasNext();)
			{
				final String price = (String) iterator.next();
				final String[] priceValues = price.split(Jnjb2bCoreConstants.Solr.COLON);
				if (priceValues.length == 2)
				{
					String upg = jnjGTB2BUnitService.getUPGCode(null, null);
					if (StringUtils.isBlank(upg))
					{
						upg = Jnjb2bCoreConstants.Solr.DEFAULTUG;
					}
					if (priceValues[0].equals(upg))
					{
						jnjGTProductData.setListPrice(priceValues[1]);
					}
				}
			}
		}
	}

	// This method will only be used in Mdd to get in order to validate whether the current product is having compatible division
	protected boolean isProductDivisionSameAsUserDivision(final String division)
	{
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentUser = ((JnJB2bCustomerModel) userService.getCurrentUser());
			final List<String> userDivisions = jnjGTB2BCommerceUserService.getUserDivisions(currentUser);
			return userDivisions.contains(division);
		}
		return false;
	}
	
	public String getEpicCONSLaunchStatus(final JnJProductModel product)
	{
		String launchStatus = null;
		final JnjGTModStatus modStatus = product.getModStatus();

		if (modStatus != null)
		{

			final Date firstShipDate = product.getFirstShipEffectDate();
			final Date prodStartDate = product.getNewProductStartDate();
			if (modStatus.equals(JnjGTModStatus.ACTIVE) && firstShipDate != null)
			{
				final Date today = new Date();
				//final Date todayPlusOne = DateUtils.addDays(today, 1);
				final Date todayPlusThirty = DateUtils.addDays(today, 30);
				final Date todayMinusSixM = DateUtils.addDays(today, -181);
				if (null != firstShipDate && firstShipDate.before(today) && firstShipDate.after(todayMinusSixM))
				{
					launchStatus = Jnjgtb2bCONSConstants.NEW;
				}
				else if (firstShipDate.after(today) && firstShipDate.before(todayPlusThirty))
				{
					launchStatus = Jnjgtb2bCONSConstants.COMING_SOON;
				}
			}
			else if (modStatus.equals(JnjGTModStatus.DISCONTINUED))
			{
				launchStatus = modStatus.getCode();
			}

		}
		return launchStatus;
	}
}
