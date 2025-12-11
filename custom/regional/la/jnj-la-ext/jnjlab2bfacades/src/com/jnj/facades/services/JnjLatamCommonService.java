package com.jnj.facades.services;

import java.util.Map;

import org.springframework.ui.Model;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
/**
 * Created by aadrian2 on 02/06/2017.
 */
public interface JnjLatamCommonService {

    public Object buildShowATPFlagMap(final Model model, final String data);
    
    public Map<OrderEntryData, Boolean> showATPFlagMap(final Model model, final String data);
    
}
