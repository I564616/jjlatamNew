/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Order;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLAOrderStatusChangeProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLAOrderStatusChangeProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLAOrderStatusChangeProcess.order</code> attribute **/
	public static final String ORDER = "order";
	/** Qualifier of the <code>JnjLAOrderStatusChangeProcess.periodicity</code> attribute **/
	public static final String PERIODICITY = "periodicity";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(ORDER, AttributeMode.INITIAL);
		tmp.put(PERIODICITY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderStatusChangeProcess.order</code> attribute.
	 * @return the order
	 */
	public Order getOrder(final SessionContext ctx)
	{
		return (Order)getProperty( ctx, ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderStatusChangeProcess.order</code> attribute.
	 * @return the order
	 */
	public Order getOrder()
	{
		return getOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderStatusChangeProcess.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final SessionContext ctx, final Order value)
	{
		setProperty(ctx, ORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderStatusChangeProcess.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final Order value)
	{
		setOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderStatusChangeProcess.periodicity</code> attribute.
	 * @return the periodicity
	 */
	public EnumerationValue getPeriodicity(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, PERIODICITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderStatusChangeProcess.periodicity</code> attribute.
	 * @return the periodicity
	 */
	public EnumerationValue getPeriodicity()
	{
		return getPeriodicity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderStatusChangeProcess.periodicity</code> attribute. 
	 * @param value the periodicity
	 */
	public void setPeriodicity(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, PERIODICITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderStatusChangeProcess.periodicity</code> attribute. 
	 * @param value the periodicity
	 */
	public void setPeriodicity(final EnumerationValue value)
	{
		setPeriodicity( getSession().getSessionContext(), value );
	}
	
}
