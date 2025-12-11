package com.jnj.facades.services.impl;

import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.services.JnjLatamCommonService;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aadrian2 on 02/06/2017.
 */
@Service
public class JnjLatamCommonServiceImpl implements JnjLatamCommonService{

    public static final String PRODUCT_SHIPPING_MAP = "productShippingMap";
    public static final String COUNTRY = "country";

    @Override
    public Object buildShowATPFlagMap(Model model, String data) {
        final Map<OrderEntryData, Boolean> showATPFlagMap = new HashMap<>();
        if (modelContainsKey(model, data)) {
            final AbstractOrderData cartData = (AbstractOrderData) extractDataFromModel(model, data);
            if (cartData.getEntries() != null) {
                for (final OrderEntryData orderEntryData : cartData.getEntries()) {
                    if (isPanamaCountry(model)) {
                        showATPFlagMap.put(orderEntryData, getPanamaShowATPFlagValue(model, orderEntryData));
                    } else {
                        showATPFlagMap.put(orderEntryData, Boolean.TRUE);
                    }
                }
            }
        }
        return showATPFlagMap;
    }
    
    @Override
    public Map<OrderEntryData, Boolean> showATPFlagMap(Model model, String data) {
        final Map<OrderEntryData, Boolean> showATPFlagMap = new HashMap<>();
        if (modelContainsKey(model, data)) {
            final AbstractOrderData cartData = (AbstractOrderData) extractDataFromModel(model, data);
            if (cartData.getEntries() != null) {
                for (final OrderEntryData orderEntryData : cartData.getEntries()) {
                	showATPFlagMap.put(orderEntryData, Boolean.TRUE);
                }
            }
        }
        return showATPFlagMap;
    }

    private Boolean modelContainsKey(final Model model, final String key) {
        return model.asMap().containsKey(key);
    }

    private Boolean getPanamaShowATPFlagValue(final Model model, final OrderEntryData orderEntryData) {
        Boolean panamaShowATPFlag = Boolean.FALSE;
        if (modelContainsKey(model, PRODUCT_SHIPPING_MAP)) {
            final String productCode = orderEntryData.getProduct().getCode();
            panamaShowATPFlag = handleATPFlag(orderEntryData, extractProductShipping(model, productCode));
        }
        return panamaShowATPFlag;
    }


    private String extractProductShipping(final Model model, final String productCode) {
        final Map<String, String> productShippingMap = (Map<String, String>) model.asMap().get("productShippingMap");
        return productShippingMap.get(productCode);
    }

    private Object extractDataFromModel(final Model model, final String key) {
        return model.asMap().get(key);
    }

    private Boolean isPanamaCountry(final Model model) {
        Boolean isPanama = Boolean.FALSE;
        if (modelContainsKey(model, COUNTRY)) {
            final String country = (String) extractDataFromModel(model, COUNTRY);
            if (StringUtils.equals(Jnjlab2bcoreConstants.COUNTRY_ISO_PANAMA, country)) {
                isPanama = Boolean.TRUE;
            }
        }
        return isPanama;
    }

    /**
     * Check itemCategory field
     * @param itemCategory
     * @return
     */
    public Boolean isNotItemCategoryZTAS(final String itemCategory) {
        Boolean isNotItemCategoryZTAS = Boolean.TRUE;
        if (StringUtils.equals(Jnjlab2bcoreConstants.ZTAS_ITEM_CATEGORY, itemCategory)){
            isNotItemCategoryZTAS = Boolean.FALSE;
        }
        return isNotItemCategoryZTAS;
    }

    /**
     * Check dropshipment shipper field
     * @param shipper
     * @return
     */
    private Boolean isShipperPA(final String shipper) {
        Boolean isShipperPA = Boolean.FALSE;
        if (StringUtils.equals(Jnjlab2bcoreConstants.COUNTRY_ISO_PANAMA, shipper)){
            isShipperPA = Boolean.TRUE;
        }
        return isShipperPA;
    }

    /**
     * Handles if an ATP info must be displayed for CENCA countries
     * @param orderEntryData
     * @param productShipping
     * @return
     */
    private Boolean handleATPFlag(OrderEntryData orderEntryData, final String productShipping) {
        Boolean showATPFlag = Boolean.TRUE;

        if (orderEntryData instanceof JnjLaOrderEntryData) {
            final JnjLaOrderEntryData jnjLaOrderEntryData = (JnjLaOrderEntryData) orderEntryData;

            if (jnjLaOrderEntryData.getProduct() != null && jnjLaOrderEntryData.getProduct() instanceof JnjLaProductData){
                final JnjLaProductData jnjLaProductData = (JnjLaProductData) jnjLaOrderEntryData.getProduct();

                if (StringUtils.equals(Jnjlab2bcoreConstants.MDD_SECTOR, jnjLaProductData.getSector())){
                    showATPFlag = isNotItemCategoryZTAS(jnjLaOrderEntryData.getItemCategory());
                }else if (StringUtils.equals(Jnjlab2bcoreConstants.PHR_SECTOR, jnjLaProductData.getSector())){
                    showATPFlag = isShipperPA(productShipping);
                }
            }
        }
        return showATPFlag;
    }
}
