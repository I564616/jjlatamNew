package com.jnj.facades.vtex.order.populator;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class JnjLatamCustomOrderFieldsPopulator implements Populator<OrderModel, OrderHistoryData>
{
    @Override
    public void populate(OrderModel source, OrderHistoryData target) throws ConversionException
    {
        target.setCartReferenceNumber(source.getExternalOrderRefNumber());
        target.setSapOrderNumber(source.getSapOrderNumber());
        target.setSoldToAccount(source.getUnit().getUid());
        target.setShipToAccount(source.getShipToAccount());
    }
}
