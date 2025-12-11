/**
 * 
 */
package com.jnj.utils;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author ar11
 * 
 */
public class order_utility
{
	public void populateMddOrderStatus(final OrderModel orderModel)
	{
		
		OrderStatus orderStatus = null;
		int invoicedStatusCount = 0;
		int shippedStatusCount = 0;
		int cancelledStatusCount = 0;
		int confirmedStatusCount = 0;
		int acceptedStatusCount = 0;
		int backorderedStatusCount = 0;
		int pendingStatusCount = 0;
		final int totalEntries = orderModel.getEntries().size();

		for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
		{
			final OrderEntryStatus orderLineStatus = (orderEntry.getStatus() != null) ? OrderEntryStatus.valueOf(orderEntry
					.getStatus()) : null;
			
			if (OrderEntryStatus.INVOICED.equals(orderLineStatus))
			{
				
				invoicedStatusCount++;
			}
			else if (OrderEntryStatus.SHIPPED.equals(orderLineStatus))
			{
				shippedStatusCount++;
			}
			else if (OrderEntryStatus.CANCELLED.equals(orderLineStatus))
			{
				cancelledStatusCount++;
			}
			else if (OrderEntryStatus.CONFIRMED.equals(orderLineStatus))
			{
				confirmedStatusCount++;
			}
			else if (OrderEntryStatus.ITEM_ACCEPTED.equals(orderLineStatus))
			{
				acceptedStatusCount++;
			}
			else if (OrderEntryStatus.BACKORDERED.equals(orderLineStatus))
			{
				
				backorderedStatusCount++;
			}
			else if (OrderEntryStatus.PENDING.equals(orderLineStatus))
			{
				
				pendingStatusCount++;
			}
		}

		if (orderStatus == null)
		{
		
		    if (invoicedStatusCount != 0 && invoicedStatusCount == (totalEntries - cancelledStatusCount))
			{
		   	 
				orderStatus = OrderStatus.INVOICED;
			}
			else if (shippedStatusCount != 0 && shippedStatusCount == (totalEntries - cancelledStatusCount))
			{
				orderStatus = OrderStatus.SHIPPED;
			}
			else if (invoicedStatusCount > 0)
			{
				orderStatus = OrderStatus.PARTIALLY_INVOICED;
			}
			else if (shippedStatusCount > 0)
			{
				orderStatus = OrderStatus.PARTIALLY_SHIPPED;
			}
			else if (confirmedStatusCount > 0)
			{
				orderStatus = OrderStatus.RELEASED;
			}
			else if (acceptedStatusCount > 0)
			{
				orderStatus = OrderStatus.ACCEPTED;
			}
			else if (backorderedStatusCount != 0 && backorderedStatusCount == (totalEntries - cancelledStatusCount))
			{
				
				orderStatus = OrderStatus.BACKORDERED;
			}
			else if (cancelledStatusCount == totalEntries)
			{
				orderStatus = OrderStatus.CANCELLED;
			}
			else if (pendingStatusCount == totalEntries)
			{
				orderStatus = OrderStatus.PENDING;
			}
			else
			{
				orderStatus = OrderStatus.INCOMPLETE;
			}
		}
		orderModel.setStatus(orderStatus);
	}


}
