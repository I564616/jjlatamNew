package com.jnj.la.core.services.ordersplit.impl;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;
import com.jnj.la.core.util.JnjLaCoreUtil;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JnjPuertoRicoOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService{

    protected JnjCencaOrderSplitServiceImpl jnjCencaOrderSplitService;

    protected JnjGTB2BUnitService jnjGTB2BUnitService;

    @Override
    public Map splitOrder(AbstractOrderModel abstOrderModel) {
        final String methodName = "Puerto Rico splitOrder()";

        final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
        JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, "currentB2BUnit is :" + currentB2BUnit, JnjPuertoRicoOrderSplitServiceImpl.class);
        JnJLaB2BUnitModel b2bUnit = null;
        if(currentB2BUnit instanceof JnJLaB2BUnitModel)
        {
            b2bUnit = (JnJLaB2BUnitModel)currentB2BUnit;
        }
        JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, "b2bUnit is :" + b2bUnit, JnjPuertoRicoOrderSplitServiceImpl.class);
        final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();
        String b2bUnitIsoCode;

        if(b2bUnit != null){
            final CountryModel b2bUnitCountry = b2bUnit.getCountry();
            if (b2bUnitCountry != null) {
                b2bUnitIsoCode = b2bUnitCountry.getIsocode();
                final List<String> prCountryList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_PR_VALID_COUNTRIES);

                if(prCountryList.contains(b2bUnitIsoCode)){
                    Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderInformation = jnjCencaOrderSplitService.splitOrder(abstOrderModel);
                    if (null != splitOrderInformation) {
                        splitOrderMap.putAll(splitOrderInformation);
                    }
                }else{
                    JnjGTCoreUtil.logDebugMessage(SPLIT_SERVICE, methodName,"Country not allowed to buy in Puerto Rico store",
                            JnjPuertoRicoOrderSplitServiceImpl.class);
                }
            }
        }
        return splitOrderMap;
    }

    public JnjGTB2BUnitService getJnjGTB2BUnitService() {
        return jnjGTB2BUnitService;
    }
    public void setJnjGTB2BUnitService(final JnjGTB2BUnitService jnjGTB2BUnitService) {this.jnjGTB2BUnitService = jnjGTB2BUnitService;}

    public JnjCencaOrderSplitServiceImpl getJnjCencaOrderSplitService() {
        return jnjCencaOrderSplitService;
    }

    public void setJnjCencaOrderSplitService(final JnjCencaOrderSplitServiceImpl jnjCencaOrderSplitService) {
        this.jnjCencaOrderSplitService = jnjCencaOrderSplitService;
    }
}
