/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2BUnit;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjCmirConversion}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjCmirConversion extends GenericItem
{
	/** Qualifier of the <code>JnjCmirConversion.b2bUnitId</code> attribute **/
	public static final String B2BUNITID = "b2bUnitId";
	/** Qualifier of the <code>JnjCmirConversion.custUom</code> attribute **/
	public static final String CUSTUOM = "custUom";
	/** Qualifier of the <code>JnjCmirConversion.cmirUom</code> attribute **/
	public static final String CMIRUOM = "cmirUom";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(B2BUNITID, AttributeMode.INITIAL);
		tmp.put(CUSTUOM, AttributeMode.INITIAL);
		tmp.put(CMIRUOM, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCmirConversion.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, B2BUNITID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCmirConversion.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId()
	{
		return getB2bUnitId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCmirConversion.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, B2BUNITID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCmirConversion.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final JnJB2BUnit value)
	{
		setB2bUnitId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCmirConversion.cmirUom</code> attribute.
	 * @return the cmirUom
	 */
	public String getCmirUom(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CMIRUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCmirConversion.cmirUom</code> attribute.
	 * @return the cmirUom
	 */
	public String getCmirUom()
	{
		return getCmirUom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCmirConversion.cmirUom</code> attribute. 
	 * @param value the cmirUom
	 */
	public void setCmirUom(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CMIRUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCmirConversion.cmirUom</code> attribute. 
	 * @param value the cmirUom
	 */
	public void setCmirUom(final String value)
	{
		setCmirUom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCmirConversion.custUom</code> attribute.
	 * @return the custUom
	 */
	public String getCustUom(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCmirConversion.custUom</code> attribute.
	 * @return the custUom
	 */
	public String getCustUom()
	{
		return getCustUom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCmirConversion.custUom</code> attribute. 
	 * @param value the custUom
	 */
	public void setCustUom(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCmirConversion.custUom</code> attribute. 
	 * @param value the custUom
	 */
	public void setCustUom(final String value)
	{
		setCustUom( getSession().getSessionContext(), value );
	}
	
}
