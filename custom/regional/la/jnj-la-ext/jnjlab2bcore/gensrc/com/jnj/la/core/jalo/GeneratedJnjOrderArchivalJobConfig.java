/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjOrderArchivalJobConfig}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjOrderArchivalJobConfig extends GenericItem
{
	/** Qualifier of the <code>JnjOrderArchivalJobConfig.countryCode</code> attribute **/
	public static final String COUNTRYCODE = "countryCode";
	/** Qualifier of the <code>JnjOrderArchivalJobConfig.businessSectorConfig</code> attribute **/
	public static final String BUSINESSSECTORCONFIG = "businessSectorConfig";
	/** Qualifier of the <code>JnjOrderArchivalJobConfig.active</code> attribute **/
	public static final String ACTIVE = "active";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(COUNTRYCODE, AttributeMode.INITIAL);
		tmp.put(BUSINESSSECTORCONFIG, AttributeMode.INITIAL);
		tmp.put(ACTIVE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.active</code> attribute.
	 * @return the active - Is configuration active for country?
	 */
	public Boolean isActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.active</code> attribute.
	 * @return the active - Is configuration active for country?
	 */
	public Boolean isActive()
	{
		return isActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.active</code> attribute. 
	 * @return the active - Is configuration active for country?
	 */
	public boolean isActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.active</code> attribute. 
	 * @return the active - Is configuration active for country?
	 */
	public boolean isActiveAsPrimitive()
	{
		return isActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.active</code> attribute. 
	 * @param value the active - Is configuration active for country?
	 */
	public void setActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.active</code> attribute. 
	 * @param value the active - Is configuration active for country?
	 */
	public void setActive(final Boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.active</code> attribute. 
	 * @param value the active - Is configuration active for country?
	 */
	public void setActive(final SessionContext ctx, final boolean value)
	{
		setActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.active</code> attribute. 
	 * @param value the active - Is configuration active for country?
	 */
	public void setActive(final boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.businessSectorConfig</code> attribute.
	 * @return the businessSectorConfig - Business Sector
	 */
	public String getBusinessSectorConfig(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BUSINESSSECTORCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.businessSectorConfig</code> attribute.
	 * @return the businessSectorConfig - Business Sector
	 */
	public String getBusinessSectorConfig()
	{
		return getBusinessSectorConfig( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.businessSectorConfig</code> attribute. 
	 * @param value the businessSectorConfig - Business Sector
	 */
	public void setBusinessSectorConfig(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BUSINESSSECTORCONFIG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.businessSectorConfig</code> attribute. 
	 * @param value the businessSectorConfig - Business Sector
	 */
	public void setBusinessSectorConfig(final String value)
	{
		setBusinessSectorConfig( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.countryCode</code> attribute.
	 * @return the countryCode - Country Code
	 */
	public String getCountryCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COUNTRYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalJobConfig.countryCode</code> attribute.
	 * @return the countryCode - Country Code
	 */
	public String getCountryCode()
	{
		return getCountryCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.countryCode</code> attribute. 
	 * @param value the countryCode - Country Code
	 */
	public void setCountryCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COUNTRYCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalJobConfig.countryCode</code> attribute. 
	 * @param value the countryCode - Country Code
	 */
	public void setCountryCode(final String value)
	{
		setCountryCode( getSession().getSessionContext(), value );
	}
	
}
