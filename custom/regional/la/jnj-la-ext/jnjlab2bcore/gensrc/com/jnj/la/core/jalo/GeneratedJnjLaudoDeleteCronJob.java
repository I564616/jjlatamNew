/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob JnjLaudoDeleteCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaudoDeleteCronJob extends CronJob
{
	/** Qualifier of the <code>JnjLaudoDeleteCronJob.countryCode</code> attribute **/
	public static final String COUNTRYCODE = "countryCode";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(COUNTRYCODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaudoDeleteCronJob.countryCode</code> attribute.
	 * @return the countryCode
	 */
	public String getCountryCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COUNTRYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaudoDeleteCronJob.countryCode</code> attribute.
	 * @return the countryCode
	 */
	public String getCountryCode()
	{
		return getCountryCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaudoDeleteCronJob.countryCode</code> attribute. 
	 * @param value the countryCode
	 */
	public void setCountryCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COUNTRYCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaudoDeleteCronJob.countryCode</code> attribute. 
	 * @param value the countryCode
	 */
	public void setCountryCode(final String value)
	{
		setCountryCode( getSession().getSessionContext(), value );
	}
	
}
