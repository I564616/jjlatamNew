/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLaActiveUserReportEmailProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaActiveUserReportEmailProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLaActiveUserReportEmailProcess.activeUserReportData</code> attribute **/
	public static final String ACTIVEUSERREPORTDATA = "activeUserReportData";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(ACTIVEUSERREPORTDATA, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaActiveUserReportEmailProcess.activeUserReportData</code> attribute.
	 * @return the activeUserReportData
	 */
	public Map getActiveUserReportData(final SessionContext ctx)
	{
		return (Map)getProperty( ctx, ACTIVEUSERREPORTDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaActiveUserReportEmailProcess.activeUserReportData</code> attribute.
	 * @return the activeUserReportData
	 */
	public Map getActiveUserReportData()
	{
		return getActiveUserReportData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaActiveUserReportEmailProcess.activeUserReportData</code> attribute. 
	 * @param value the activeUserReportData
	 */
	public void setActiveUserReportData(final SessionContext ctx, final Map value)
	{
		setProperty(ctx, ACTIVEUSERREPORTDATA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaActiveUserReportEmailProcess.activeUserReportData</code> attribute. 
	 * @param value the activeUserReportData
	 */
	public void setActiveUserReportData(final Map value)
	{
		setActiveUserReportData( getSession().getSessionContext(), value );
	}
	
}
