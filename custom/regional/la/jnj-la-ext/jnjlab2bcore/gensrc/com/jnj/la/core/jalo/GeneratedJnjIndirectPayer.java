/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjIndirectPayer}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjIndirectPayer extends GenericItem
{
	/** Qualifier of the <code>JnjIndirectPayer.country</code> attribute **/
	public static final String COUNTRY = "country";
	/** Qualifier of the <code>JnjIndirectPayer.indirectPayer</code> attribute **/
	public static final String INDIRECTPAYER = "indirectPayer";
	/** Qualifier of the <code>JnjIndirectPayer.indirectPayerName</code> attribute **/
	public static final String INDIRECTPAYERNAME = "indirectPayerName";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(COUNTRY, AttributeMode.INITIAL);
		tmp.put(INDIRECTPAYER, AttributeMode.INITIAL);
		tmp.put(INDIRECTPAYERNAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectPayer.country</code> attribute.
	 * @return the country
	 */
	public Country getCountry(final SessionContext ctx)
	{
		return (Country)getProperty( ctx, COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectPayer.country</code> attribute.
	 * @return the country
	 */
	public Country getCountry()
	{
		return getCountry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectPayer.country</code> attribute. 
	 * @param value the country
	 */
	public void setCountry(final SessionContext ctx, final Country value)
	{
		setProperty(ctx, COUNTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectPayer.country</code> attribute. 
	 * @param value the country
	 */
	public void setCountry(final Country value)
	{
		setCountry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectPayer.indirectPayer</code> attribute.
	 * @return the indirectPayer
	 */
	public String getIndirectPayer(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INDIRECTPAYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectPayer.indirectPayer</code> attribute.
	 * @return the indirectPayer
	 */
	public String getIndirectPayer()
	{
		return getIndirectPayer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectPayer.indirectPayer</code> attribute. 
	 * @param value the indirectPayer
	 */
	public void setIndirectPayer(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INDIRECTPAYER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectPayer.indirectPayer</code> attribute. 
	 * @param value the indirectPayer
	 */
	public void setIndirectPayer(final String value)
	{
		setIndirectPayer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectPayer.indirectPayerName</code> attribute.
	 * @return the indirectPayerName
	 */
	public String getIndirectPayerName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INDIRECTPAYERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectPayer.indirectPayerName</code> attribute.
	 * @return the indirectPayerName
	 */
	public String getIndirectPayerName()
	{
		return getIndirectPayerName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectPayer.indirectPayerName</code> attribute. 
	 * @param value the indirectPayerName
	 */
	public void setIndirectPayerName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INDIRECTPAYERNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectPayer.indirectPayerName</code> attribute. 
	 * @param value the indirectPayerName
	 */
	public void setIndirectPayerName(final String value)
	{
		setIndirectPayerName( getSession().getSessionContext(), value );
	}
	
}
