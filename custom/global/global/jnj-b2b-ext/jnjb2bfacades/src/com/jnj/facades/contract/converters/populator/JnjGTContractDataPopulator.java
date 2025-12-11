/**
 *
 */
package com.jnj.facades.contract.converters.populator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;

//import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTContractDataPopulator implements Populator<JnjContractModel, JnjContractData>
{
	@Autowired
	JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Resource(name = "jnJGTCartFacade")
	JnjGTCartFacade jnjGTCartFacade;
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	private Converter<JnjContractEntryModel, JnjContractEntryData> jnjContractEntryDataConverter;

	/**
	 * @return the jnjContractEntryDataConverter
	 */
	public Converter<JnjContractEntryModel, JnjContractEntryData> getJnjContractEntryDataConverter()
	{
		return jnjContractEntryDataConverter;
	}


	/**
	 * @param jnjContractEntryDataConverter
	 *           the jnjContractEntryDataConverter to set
	 */
	public void setJnjContractEntryDataConverter(
			final Converter<JnjContractEntryModel, JnjContractEntryData> jnjContractEntryDataConverter)
	{
		this.jnjContractEntryDataConverter = jnjContractEntryDataConverter;
	}


	@Override
	public void populate(final JnjContractModel source, final JnjContractData target) throws ConversionException
	{
		int entryCount = target.getEntryCount() == null ? 0 : target.getEntryCount().intValue();
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (StringUtils.isNotEmpty(source.getUnit().getUid()))
		{
			target.setDistributorCustomer(source.getUnit().getUid().trim());
		}
		if (StringUtils.isNotEmpty(source.getECCContractNum()))
		{
			target.setEccContractNum(source.getECCContractNum().trim());
		}
		if (StringUtils.isNotEmpty(source.getCRMContractNum()))
		{
			target.setCrmContractNum(source.getCRMContractNum().trim());
		}
		if (StringUtils.isNotEmpty(source.getTenderNum()))
		{
			target.setTenderNum(source.getTenderNum().trim());
		}
		//Defect 34987--Shriya Tiwari---Start--
		if ((source.getActive().booleanValue()))
		{

			target.setStatus(jnjCommonFacadeUtil.getMessageFromImpex("contract.status.active"));//35381 fixed
		}
		//---End---
		if (null != source.getContractOrderReason() && !StringUtils.isEmpty(source.getContractOrderReason()))
		{
			target.setContractOrderReason(jnjCommonFacadeUtil.getMessageFromImpex("contract." + source.getContractOrderReason()));
		}
		if (StringUtils.isNotEmpty(source.getDocumentType().getCode()))
		{
			target.setDocumentType((source.getDocumentType().getCode()).trim());
		}
		if (StringUtils.isNotEmpty(source.getIndirectCustomer()))
		{
			target.setIndirectCustomer(source.getIndirectCustomer().trim());
			//35406 fixed Start
			final CountryModel countryModel = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
			final JnjIndirectCustomerModel jnjIndirectCustomerModel = jnjGTCartFacade.fetchIndirectCustomerName(source
					.getIndirectCustomer().trim(), countryModel);
			if (jnjIndirectCustomerModel != null)
			{
				target.setIndirectCustomerName(jnjIndirectCustomerModel.getIndirectCustomerName());
			}
			else
			{
				final JnjIndirectCustomerModel jnjIndirectCustModel = jnjGTCartFacade.fetchIndirectCustomerName(source
						.getIndirectCustomer().trim());
				if (jnjIndirectCustModel != null)
				{
					target.setIndirectCustomerName(jnjIndirectCustModel.getIndirectCustomerName());
				}
			}
			//35406 fixed end
		}
		target.setTotalAmount(source.getTotalAmount());
		target.setBalanceAmount(source.getBalanceAmount());
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setActive(source.getActive());
		target.setTotalItem(String.valueOf(source.getJnjContractEntries().size()));

		final Double totalAmount = (source.getTotalAmount() == null) ? Double.valueOf("0.0") : source.getTotalAmount();
		final Double balanceAmount = (source.getBalanceAmount() == null) ? Double.valueOf("0.0") : source.getBalanceAmount();
		final BigDecimal consumedAmt = BigDecimal.valueOf(totalAmount.doubleValue()).subtract(
				BigDecimal.valueOf(balanceAmount.doubleValue()));
		target.setConsumedAmount(Double.valueOf(consumedAmt.doubleValue()));
		target.setLastUpdatedTime(source.getModifiedtime());
		final List<JnjContractEntryModel> jnjContractEntryList = source.getJnjContractEntries();
		if (CollectionUtils.isNotEmpty(jnjContractEntryList))
		{

			final List<JnjContractEntryData> jnjContractEntryDataList = new ArrayList<JnjContractEntryData>(
					jnjContractEntryList.size());
			if (entryCount == 0 || entryCount > jnjContractEntryList.size())
			{
				entryCount = jnjContractEntryList.size();
			}
			for (int i = 0; i < entryCount; i++)
			{
				jnjContractEntryDataList.add(getJnjContractEntryDataConverter().convert(jnjContractEntryList.get(i),
						new JnjContractEntryData()));
			}
			target.setContractEntryList(jnjContractEntryDataList);
		}
		else
		{
			target.setContractEntryList(null);
		}

	}
}
