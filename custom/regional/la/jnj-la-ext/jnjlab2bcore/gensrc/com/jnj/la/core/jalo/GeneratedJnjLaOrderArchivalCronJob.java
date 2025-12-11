/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.jalo.JnjOrderArchivalJobConfig;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob JnjLaOrderArchivalCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaOrderArchivalCronJob extends CronJob
{
	/** Qualifier of the <code>JnjLaOrderArchivalCronJob.countryConfigList</code> attribute **/
	public static final String COUNTRYCONFIGLIST = "countryConfigList";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(COUNTRYCONFIGLIST, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOrderArchivalCronJob.countryConfigList</code> attribute.
	 * @return the countryConfigList
	 */
	public List<JnjOrderArchivalJobConfig> getCountryConfigList(final SessionContext ctx)
	{
		List<JnjOrderArchivalJobConfig> coll = (List<JnjOrderArchivalJobConfig>)getProperty( ctx, COUNTRYCONFIGLIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOrderArchivalCronJob.countryConfigList</code> attribute.
	 * @return the countryConfigList
	 */
	public List<JnjOrderArchivalJobConfig> getCountryConfigList()
	{
		return getCountryConfigList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOrderArchivalCronJob.countryConfigList</code> attribute. 
	 * @param value the countryConfigList
	 */
	public void setCountryConfigList(final SessionContext ctx, final List<JnjOrderArchivalJobConfig> value)
	{
		setProperty(ctx, COUNTRYCONFIGLIST,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOrderArchivalCronJob.countryConfigList</code> attribute. 
	 * @param value the countryConfigList
	 */
	public void setCountryConfigList(final List<JnjOrderArchivalJobConfig> value)
	{
		setCountryConfigList( getSession().getSessionContext(), value );
	}
	
}
