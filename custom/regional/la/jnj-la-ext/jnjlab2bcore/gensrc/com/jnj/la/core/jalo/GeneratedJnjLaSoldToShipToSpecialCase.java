/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.jalo.JnjOrderType;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjLaSoldToShipToSpecialCase}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaSoldToShipToSpecialCase extends GenericItem
{
	/** Qualifier of the <code>JnjLaSoldToShipToSpecialCase.soldTo</code> attribute **/
	public static final String SOLDTO = "soldTo";
	/** Qualifier of the <code>JnjLaSoldToShipToSpecialCase.shipTo</code> attribute **/
	public static final String SHIPTO = "shipTo";
	/** Qualifier of the <code>JnjLaSoldToShipToSpecialCase.jnjOrderType</code> attribute **/
	public static final String JNJORDERTYPE = "jnjOrderType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(SOLDTO, AttributeMode.INITIAL);
		tmp.put(SHIPTO, AttributeMode.INITIAL);
		tmp.put(JNJORDERTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSoldToShipToSpecialCase.jnjOrderType</code> attribute.
	 * @return the jnjOrderType
	 */
	public JnjOrderType getJnjOrderType(final SessionContext ctx)
	{
		return (JnjOrderType)getProperty( ctx, JNJORDERTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSoldToShipToSpecialCase.jnjOrderType</code> attribute.
	 * @return the jnjOrderType
	 */
	public JnjOrderType getJnjOrderType()
	{
		return getJnjOrderType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSoldToShipToSpecialCase.jnjOrderType</code> attribute. 
	 * @param value the jnjOrderType
	 */
	public void setJnjOrderType(final SessionContext ctx, final JnjOrderType value)
	{
		setProperty(ctx, JNJORDERTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSoldToShipToSpecialCase.jnjOrderType</code> attribute. 
	 * @param value the jnjOrderType
	 */
	public void setJnjOrderType(final JnjOrderType value)
	{
		setJnjOrderType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSoldToShipToSpecialCase.shipTo</code> attribute.
	 * @return the shipTo
	 */
	public String getShipTo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SHIPTO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSoldToShipToSpecialCase.shipTo</code> attribute.
	 * @return the shipTo
	 */
	public String getShipTo()
	{
		return getShipTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSoldToShipToSpecialCase.shipTo</code> attribute. 
	 * @param value the shipTo
	 */
	public void setShipTo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SHIPTO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSoldToShipToSpecialCase.shipTo</code> attribute. 
	 * @param value the shipTo
	 */
	public void setShipTo(final String value)
	{
		setShipTo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSoldToShipToSpecialCase.soldTo</code> attribute.
	 * @return the soldTo
	 */
	public String getSoldTo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SOLDTO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaSoldToShipToSpecialCase.soldTo</code> attribute.
	 * @return the soldTo
	 */
	public String getSoldTo()
	{
		return getSoldTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSoldToShipToSpecialCase.soldTo</code> attribute. 
	 * @param value the soldTo
	 */
	public void setSoldTo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SOLDTO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaSoldToShipToSpecialCase.soldTo</code> attribute. 
	 * @param value the soldTo
	 */
	public void setSoldTo(final String value)
	{
		setSoldTo( getSession().getSessionContext(), value );
	}
	
}
