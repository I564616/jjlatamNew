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
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjCommercialUomConversion}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjCommercialUomConversion extends GenericItem
{
	/** Qualifier of the <code>JnjCommercialUomConversion.isoCode</code> attribute **/
	public static final String ISOCODE = "isoCode";
	/** Qualifier of the <code>JnjCommercialUomConversion.commercialCode</code> attribute **/
	public static final String COMMERCIALCODE = "commercialCode";
	/** Qualifier of the <code>JnjCommercialUomConversion.primaryCode</code> attribute **/
	public static final String PRIMARYCODE = "primaryCode";
	/** Qualifier of the <code>JnjCommercialUomConversion.measurement</code> attribute **/
	public static final String MEASUREMENT = "measurement";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ISOCODE, AttributeMode.INITIAL);
		tmp.put(COMMERCIALCODE, AttributeMode.INITIAL);
		tmp.put(PRIMARYCODE, AttributeMode.INITIAL);
		tmp.put(MEASUREMENT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.commercialCode</code> attribute.
	 * @return the commercialCode
	 */
	public String getCommercialCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMMERCIALCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.commercialCode</code> attribute.
	 * @return the commercialCode
	 */
	public String getCommercialCode()
	{
		return getCommercialCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.commercialCode</code> attribute. 
	 * @param value the commercialCode
	 */
	public void setCommercialCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMMERCIALCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.commercialCode</code> attribute. 
	 * @param value the commercialCode
	 */
	public void setCommercialCode(final String value)
	{
		setCommercialCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.isoCode</code> attribute.
	 * @return the isoCode
	 */
	public String getIsoCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ISOCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.isoCode</code> attribute.
	 * @return the isoCode
	 */
	public String getIsoCode()
	{
		return getIsoCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.isoCode</code> attribute. 
	 * @param value the isoCode
	 */
	public void setIsoCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ISOCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.isoCode</code> attribute. 
	 * @param value the isoCode
	 */
	public void setIsoCode(final String value)
	{
		setIsoCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.measurement</code> attribute.
	 * @return the measurement
	 */
	public String getMeasurement(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MEASUREMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.measurement</code> attribute.
	 * @return the measurement
	 */
	public String getMeasurement()
	{
		return getMeasurement( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.measurement</code> attribute. 
	 * @param value the measurement
	 */
	public void setMeasurement(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MEASUREMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.measurement</code> attribute. 
	 * @param value the measurement
	 */
	public void setMeasurement(final String value)
	{
		setMeasurement( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.primaryCode</code> attribute.
	 * @return the primaryCode
	 */
	public String getPrimaryCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRIMARYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCommercialUomConversion.primaryCode</code> attribute.
	 * @return the primaryCode
	 */
	public String getPrimaryCode()
	{
		return getPrimaryCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.primaryCode</code> attribute. 
	 * @param value the primaryCode
	 */
	public void setPrimaryCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRIMARYCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCommercialUomConversion.primaryCode</code> attribute. 
	 * @param value the primaryCode
	 */
	public void setPrimaryCode(final String value)
	{
		setPrimaryCode( getSession().getSessionContext(), value );
	}
	
}
