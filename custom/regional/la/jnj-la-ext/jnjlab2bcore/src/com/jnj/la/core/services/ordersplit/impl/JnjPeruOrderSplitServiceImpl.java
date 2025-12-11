package com.jnj.la.core.services.ordersplit.impl;

import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;
import com.jnj.la.core.util.JnjLaCoreUtil;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jnj.core.model.JnJB2BUnitModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by aadrian2 on 08/04/2017.
 */

@Service("peOrderSplitService")
public class JnjPeruOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService {

	@Autowired
	protected DefaultLatamOrderSplitServiceImpl defaultLatamOrderSplitServiceImpl;

	@Autowired
	protected JnjCencaOrderSplitServiceImpl jnjCencaOrderSplitService;

	@Autowired
	protected SessionService sessionService;
	
	protected JnjGTB2BUnitService jnjGTB2BUnitService;


	@Override
	public Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(final AbstractOrderModel abstOrderModel){
		//final JnJLaB2BUnitModel b2bUnit = sessionService.getAttribute("unit");
		
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		JnJLaB2BUnitModel b2bUnit = null;
		if(currentB2BUnit instanceof JnJLaB2BUnitModel)
		{
			b2bUnit = (JnJLaB2BUnitModel)currentB2BUnit;
		}
		
		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();
		String b2bUnitIsoCode;

		if(b2bUnit != null){
			final CountryModel b2bUnitCountry = b2bUnit.getCountry();
			if (b2bUnitCountry != null) {
				b2bUnitIsoCode = b2bUnitCountry.getIsocode();
				final List<String> peCountryList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_PE_VALID_COUNTRIES);
				final List<String> zordOrderCountryList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_PE_ZORD_VALID_COUNTRIES);

				if(peCountryList.contains(b2bUnitIsoCode)) {
					if (zordOrderCountryList.contains(b2bUnitIsoCode)) {
						Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderInformation = jnjCencaOrderSplitService.splitOrder(abstOrderModel);
						if (null != splitOrderInformation) {
							splitOrderMap.putAll(splitOrderInformation);
						}
					} else {
						splitOrderMap.putAll(defaultLatamOrderSplitServiceImpl.splitOrder(abstOrderModel));
					}
				}
			}
		}
		return splitOrderMap;
	}
	
	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public void setJnjGTB2BUnitService(JnjGTB2BUnitService jnjGTB2BUnitService) {
		this.jnjGTB2BUnitService = jnjGTB2BUnitService;
	}
}