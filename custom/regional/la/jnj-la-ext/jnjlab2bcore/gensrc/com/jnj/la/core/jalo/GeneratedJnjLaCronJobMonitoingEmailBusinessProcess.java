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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLaCronJobMonitoingEmailBusinessProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaCronJobMonitoingEmailBusinessProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.cronJobMonitoringEmailData</code> attribute **/
	public static final String CRONJOBMONITORINGEMAILDATA = "cronJobMonitoringEmailData";
	/** Qualifier of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.country</code> attribute **/
	public static final String COUNTRY = "country";
	/** Qualifier of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.userEmailList</code> attribute **/
	public static final String USEREMAILLIST = "userEmailList";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CRONJOBMONITORINGEMAILDATA, AttributeMode.INITIAL);
		tmp.put(COUNTRY, AttributeMode.INITIAL);
		tmp.put(USEREMAILLIST, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.country</code> attribute.
	 * @return the country - Country.
	 */
	public String getCountry(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.country</code> attribute.
	 * @return the country - Country.
	 */
	public String getCountry()
	{
		return getCountry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.country</code> attribute. 
	 * @param value the country - Country.
	 */
	public void setCountry(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COUNTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.country</code> attribute. 
	 * @param value the country - Country.
	 */
	public void setCountry(final String value)
	{
		setCountry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.cronJobMonitoringEmailData</code> attribute.
	 * @return the cronJobMonitoringEmailData - List containing elements representing concatenated data for job monitoring report email.
	 */
	public List<String> getCronJobMonitoringEmailData(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, CRONJOBMONITORINGEMAILDATA);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.cronJobMonitoringEmailData</code> attribute.
	 * @return the cronJobMonitoringEmailData - List containing elements representing concatenated data for job monitoring report email.
	 */
	public List<String> getCronJobMonitoringEmailData()
	{
		return getCronJobMonitoringEmailData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.cronJobMonitoringEmailData</code> attribute. 
	 * @param value the cronJobMonitoringEmailData - List containing elements representing concatenated data for job monitoring report email.
	 */
	public void setCronJobMonitoringEmailData(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, CRONJOBMONITORINGEMAILDATA,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.cronJobMonitoringEmailData</code> attribute. 
	 * @param value the cronJobMonitoringEmailData - List containing elements representing concatenated data for job monitoring report email.
	 */
	public void setCronJobMonitoringEmailData(final List<String> value)
	{
		setCronJobMonitoringEmailData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.userEmailList</code> attribute.
	 * @return the userEmailList - Email.
	 */
	public String getUserEmailList(final SessionContext ctx)
	{
		return (String)getProperty( ctx, USEREMAILLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.userEmailList</code> attribute.
	 * @return the userEmailList - Email.
	 */
	public String getUserEmailList()
	{
		return getUserEmailList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.userEmailList</code> attribute. 
	 * @param value the userEmailList - Email.
	 */
	public void setUserEmailList(final SessionContext ctx, final String value)
	{
		setProperty(ctx, USEREMAILLIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoingEmailBusinessProcess.userEmailList</code> attribute. 
	 * @param value the userEmailList - Email.
	 */
	public void setUserEmailList(final String value)
	{
		setUserEmailList( getSession().getSessionContext(), value );
	}
	
}
