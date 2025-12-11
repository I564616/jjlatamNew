/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
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
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjLAConsolidatedEmailRecipients}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLAConsolidatedEmailRecipients extends GenericItem
{
	/** Qualifier of the <code>JnjLAConsolidatedEmailRecipients.soldTo</code> attribute **/
	public static final String SOLDTO = "soldTo";
	/** Qualifier of the <code>JnjLAConsolidatedEmailRecipients.recipients</code> attribute **/
	public static final String RECIPIENTS = "recipients";
	/** Qualifier of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute **/
	public static final String ENABLED = "enabled";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(SOLDTO, AttributeMode.INITIAL);
		tmp.put(RECIPIENTS, AttributeMode.INITIAL);
		tmp.put(ENABLED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute.
	 * @return the enabled
	 */
	public Boolean isEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute.
	 * @return the enabled
	 */
	public Boolean isEnabled()
	{
		return isEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute. 
	 * @return the enabled
	 */
	public boolean isEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute. 
	 * @return the enabled
	 */
	public boolean isEnabledAsPrimitive()
	{
		return isEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final Boolean value)
	{
		setEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final SessionContext ctx, final boolean value)
	{
		setEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final boolean value)
	{
		setEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.recipients</code> attribute.
	 * @return the recipients
	 */
	public String getRecipients(final SessionContext ctx)
	{
		return (String)getProperty( ctx, RECIPIENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.recipients</code> attribute.
	 * @return the recipients
	 */
	public String getRecipients()
	{
		return getRecipients( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.recipients</code> attribute. 
	 * @param value the recipients
	 */
	public void setRecipients(final SessionContext ctx, final String value)
	{
		setProperty(ctx, RECIPIENTS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.recipients</code> attribute. 
	 * @param value the recipients
	 */
	public void setRecipients(final String value)
	{
		setRecipients( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.soldTo</code> attribute.
	 * @return the soldTo
	 */
	public JnJB2BUnit getSoldTo(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, SOLDTO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAConsolidatedEmailRecipients.soldTo</code> attribute.
	 * @return the soldTo
	 */
	public JnJB2BUnit getSoldTo()
	{
		return getSoldTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.soldTo</code> attribute. 
	 * @param value the soldTo
	 */
	public void setSoldTo(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, SOLDTO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAConsolidatedEmailRecipients.soldTo</code> attribute. 
	 * @param value the soldTo
	 */
	public void setSoldTo(final JnJB2BUnit value)
	{
		setSoldTo( getSession().getSessionContext(), value );
	}
	
}
