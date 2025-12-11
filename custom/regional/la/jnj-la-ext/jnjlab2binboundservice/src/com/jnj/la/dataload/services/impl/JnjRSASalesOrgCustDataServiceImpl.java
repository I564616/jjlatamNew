package com.jnj.la.dataload.services.impl;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjSalesOrgCustDTO;
import com.jnj.la.dataload.dao.JnjRSASalesOrgCustDao;
import com.jnj.la.dataload.services.JnjRSASalesOrgCustDataService;



public class JnjRSASalesOrgCustDataServiceImpl implements JnjRSASalesOrgCustDataService
{

	@Autowired
	protected JnjRSASalesOrgCustDao jnjRSASalesOrgCustDao;

	@Override
	public List<JnjSalesOrgCustDTO> pullDataFromRSA(final JnjIntegrationRSACronJobModel cronJobModel)
	{
		final String METHOD_NAME = "pullDataFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				JnjRSASalesOrgCustDataServiceImpl.class);

		final List<JnjSalesOrgCustDTO> rsaSalesOrgCustData = jnjRSASalesOrgCustDao.getSalesOrgDetails(cronJobModel);

		final List<JnjSalesOrgCustDTO> jnjSalesOrgCustData = new ArrayList<JnjSalesOrgCustDTO>();
		if (rsaSalesOrgCustData != null && !rsaSalesOrgCustData.isEmpty())
		{
			mapJnJSalesOrgCustData(rsaSalesOrgCustData, jnjSalesOrgCustData);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SALES_ORG_CUST, METHOD_NAME, "No data pulled from RSA sales org view",
					JnjRSASalesOrgCustDataServiceImpl.class);
		}


		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.END_OF_METHOD,
				JnjRSASalesOrgCustDataServiceImpl.class);
		return jnjSalesOrgCustData;
	}

	private void mapJnJSalesOrgCustData(final List<JnjSalesOrgCustDTO> rsaSalesOrgCustData,
			final List<JnjSalesOrgCustDTO> jnjSalesOrgCustData)
	{
		final String METHOD_NAME = "mapJnJSalesOrgCustData";
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				JnjRSASalesOrgCustDataServiceImpl.class);

		for (final JnjSalesOrgCustDTO rsaData : rsaSalesOrgCustData)
		{
			final JnjSalesOrgCustDTO jnjData = new JnjSalesOrgCustDTO();

			jnjData.setCustomerNum(rsaData.getCustomerNum());
			jnjData.setSalesOrg(rsaData.getSalesOrg());
			jnjData.setSector(rsaData.getSector());
			jnjData.setLastUpdatedDate(rsaData.getLastUpdatedDate());

			jnjSalesOrgCustData.add(jnjData);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.END_OF_METHOD,
				JnjRSASalesOrgCustDataServiceImpl.class);
	}
}
