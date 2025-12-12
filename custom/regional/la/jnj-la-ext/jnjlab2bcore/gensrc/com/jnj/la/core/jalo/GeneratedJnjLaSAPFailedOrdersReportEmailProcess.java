/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 9:48:14 AM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLaSAPFailedOrdersReportEmailProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaSAPFailedOrdersReportEmailProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrdersReportData</code> attribute **/
	public static final String SAPFAILEDORDERSREPORTDATA = "sapFailedOrdersReportData";
	/** Qualifier of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrders</code> attribute **/
	public static final String SAPFAILEDORDERS = "sapFailedOrders";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SAPFAILEDORDERSREPORTDATA, AttributeMode.INITIAL);
		tmp.put(SAPFAILEDORDERS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrders</code> attribute.
	 * @return the sapFailedOrders
	 */
	public List<Order> getSapFailedOrders(final SessionContext ctx)
	{
		List<Order> coll = (List<Order>)getProperty( ctx, SAPFAILEDORDERS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrders</code> attribute.
	 * @return the sapFailedOrders
	 */
	public List<Order> getSapFailedOrders()
	{
		return getSapFailedOrders( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrders</code> attribute. 
	 * @param value the sapFailedOrders
	 */
	public void setSapFailedOrders(final SessionContext ctx, final List<Order> value)
	{
		setProperty(ctx, SAPFAILEDORDERS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrders</code> attribute. 
	 * @param value the sapFailedOrders
	 */
	public void setSapFailedOrders(final List<Order> value)
	{
		setSapFailedOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrdersReportData</code> attribute.
	 * @return the sapFailedOrdersReportData
	 */
	public Map getSapFailedOrdersReportData(final SessionContext ctx)
	{
		return (Map)getProperty( ctx, SAPFAILEDORDERSREPORTDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrdersReportData</code> attribute.
	 * @return the sapFailedOrdersReportData
	 */
	public Map getSapFailedOrdersReportData()
	{
		return getSapFailedOrdersReportData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrdersReportData</code> attribute. 
	 * @param value the sapFailedOrdersReportData
	 */
	public void setSapFailedOrdersReportData(final SessionContext ctx, final Map value)
	{
		setProperty(ctx, SAPFAILEDORDERSREPORTDATA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSAPFailedOrdersReportEmailProcess.sapFailedOrdersReportData</code> attribute. 
	 * @param value the sapFailedOrdersReportData
	 */
	public void setSapFailedOrdersReportData(final Map value)
	{
		setSapFailedOrdersReportData( getSession().getSessionContext(), value );
	}
	
}
