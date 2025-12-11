/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.converters.populators.cpsia;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTCpscContactData;
import com.jnj.facades.data.JnjGTCpscTestDetailData;
import com.jnj.facades.data.JnjGTCpsiaData;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnjGTCpscContactModel;
import com.jnj.core.model.JnjGTCpscTestDetailModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnJProductModel;


/**
 * This is the populator for the CPSIA certificate details.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCpsiaCertificateDataPopulator implements Populator<JnjGTProductCpscDetailModel, JnjGTCpsiaData>
{

	private static final String DATE_FORMAT = "MM/dd/yyyy";
	@Resource(name = "productService")
	protected JnJGTProductService jnjProductService;

//	@Autowired
//	private SessionService sessionService;
//
//	@Autowired
//	private UserService userService;

	/**
	 * Populates data attributes of JnjGTInvoiceOrderData from the source instance of JnjGTInvoiceModel.
	 */
	@Override
	public void populate(final JnjGTProductCpscDetailModel source, final JnjGTCpsiaData target) throws ConversionException
	{

		target.setProductCode(source.getProductCode());

		// Fetching the product model to set product name
		if (StringUtils.isNotEmpty(source.getProductCode()))
		{
			final JnJProductModel product = jnjProductService.getProductModelByCode(source.getProductCode(), null);
			if (product != null)
			{
				target.setProductName(StringUtils.isEmpty(product.getName()) ? ((JnJProductModel) product).getMdmDescription()
						: product.getName());
				//target.setDescription(product.getDescription()); /*GTR-1788 */
			}
		}
		/*GTR-1788 Starts*/
		target.setDescription(source.getCpscRuleDescription());
		/*GTR-1788 Ends*/

		target.setLotNumber(source.getLotNumber());
		target.setOutercase(source.getOutercase());
		target.setUpc(source.getUpcCode());
		target.setAddressLotNumber(source.getAddressLotNumber());
		target.setCertificateCreator(source.getCertificateCreater());
		target.setCpsiComments(source.getCpsiComments());
		target.setDeleted(String.valueOf(source.getDeleted()));
		target.setMfgCompanyCode(source.getMfgCompanyCode());
		target.setModifiedBy(source.getModifiedBy());
		if (null != source.getCertificateCreateDate())
		{
			target.setCertificateCreateDate(JnJCommonUtil.formatDate(source.getCertificateCreateDate(), DATE_FORMAT));
		}
		if (null != source.getCertificateModifiedDate())
		{
			target.setCertificateModifiedDate(JnJCommonUtil.formatDate(source.getCertificateModifiedDate(), DATE_FORMAT));
		}
		if (null != source.getMfdDate())
		{
			target.setMfdDate(source.getMfdDate());
		}
		if (null != source.getModifiedDate())
		{
			target.setModifiedDate(JnJCommonUtil.formatDate(source.getModifiedDate(), DATE_FORMAT));
		}

		if (CollectionUtils.isNotEmpty(source.getCpscContactDetails()))
		{
			JnjGTCpscContactData JnjGTCpscContactData = null;
			final List<JnjGTCpscContactData> jnjGTCpscContactDataList = new ArrayList<JnjGTCpscContactData>();
			for (final JnjGTCpscContactModel jnjGTCpscContactModel : source.getCpscContactDetails())
			{
				JnjGTCpscContactData = new JnjGTCpscContactData();
				JnjGTCpscContactData.setAddressAdditionalInfo(jnjGTCpscContactModel.getAddressAdditionalInfo());
				JnjGTCpscContactData.setAddressline1(jnjGTCpscContactModel.getAddressline1());
				JnjGTCpscContactData.setAddressline2(jnjGTCpscContactModel.getAddressline2());
				JnjGTCpscContactData.setAddressType(jnjGTCpscContactModel.getAddressType());
				JnjGTCpscContactData.setCity(jnjGTCpscContactModel.getCity());
				JnjGTCpscContactData.setCountry(jnjGTCpscContactModel.getCountry());
				JnjGTCpscContactData.setEmailId(jnjGTCpscContactModel.getEmailId());
				JnjGTCpscContactData.setName(jnjGTCpscContactModel.getName());
				JnjGTCpscContactData.setPhoneNumber(jnjGTCpscContactModel.getPhoneNumber());
				JnjGTCpscContactData.setState(jnjGTCpscContactModel.getState());
				JnjGTCpscContactData.setZipCode(jnjGTCpscContactModel.getZipCode());

				jnjGTCpscContactDataList.add(JnjGTCpscContactData);
			}
			target.setCpscContactDataList(jnjGTCpscContactDataList);
		}
		if (CollectionUtils.isNotEmpty(source.getCpscTestDetails()))
		{
			JnjGTCpscTestDetailData jnjGTCpscTestDetailData = null;
			final List<JnjGTCpscTestDetailData> jnjGTCpscTestDetailDataList = new ArrayList<JnjGTCpscTestDetailData>();
			for (final JnjGTCpscTestDetailModel jnjGTCpscTestDetailModel : source.getCpscTestDetails())
			{
				jnjGTCpscTestDetailData = new JnjGTCpscTestDetailData();
				jnjGTCpscTestDetailData.setAddressLine1(jnjGTCpscTestDetailModel.getAddressLine1());
				jnjGTCpscTestDetailData.setAddressLine2(jnjGTCpscTestDetailModel.getAddressLine2());
				jnjGTCpscTestDetailData.setCity(jnjGTCpscTestDetailModel.getCity());
				jnjGTCpscTestDetailData.setCountry(jnjGTCpscTestDetailModel.getCountry());
				jnjGTCpscTestDetailData.setPhoneNumber(jnjGTCpscTestDetailModel.getPhoneNumber());
				jnjGTCpscTestDetailData.setState(jnjGTCpscTestDetailModel.getState());
				jnjGTCpscTestDetailData.setStudyComments(jnjGTCpscTestDetailModel.getStudyComments());
				jnjGTCpscTestDetailData.setStudyNumber(jnjGTCpscTestDetailModel.getStudyNumber());
				jnjGTCpscTestDetailData.setThirdPartyName(jnjGTCpscTestDetailModel.getThirdPartyName());
				jnjGTCpscTestDetailData.setZipCode(jnjGTCpscTestDetailModel.getZipCode());
				if (null != jnjGTCpscTestDetailModel.getTestingDate())
				{
					jnjGTCpscTestDetailData.setTestingDate(JnJCommonUtil.formatDate(jnjGTCpscTestDetailModel.getTestingDate(),
							DATE_FORMAT));
				}
				jnjGTCpscTestDetailDataList.add(jnjGTCpscTestDetailData);
			}
			target.setCpscTestDetailList(jnjGTCpscTestDetailDataList);
		}
	}
}
