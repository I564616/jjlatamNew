/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 9:48:14 AM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjLAEmailMatrix}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLAEmailMatrix extends GenericItem
{
	/** Qualifier of the <code>JnjLAEmailMatrix.previousStatus</code> attribute **/
	public static final String PREVIOUSSTATUS = "previousStatus";
	/** Qualifier of the <code>JnjLAEmailMatrix.currentStatus</code> attribute **/
	public static final String CURRENTSTATUS = "currentStatus";
	/** Qualifier of the <code>JnjLAEmailMatrix.sendEmail</code> attribute **/
	public static final String SENDEMAIL = "sendEmail";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(PREVIOUSSTATUS, AttributeMode.INITIAL);
		tmp.put(CURRENTSTATUS, AttributeMode.INITIAL);
		tmp.put(SENDEMAIL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.currentStatus</code> attribute.
	 * @return the currentStatus
	 */
	public EnumerationValue getCurrentStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, CURRENTSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.currentStatus</code> attribute.
	 * @return the currentStatus
	 */
	public EnumerationValue getCurrentStatus()
	{
		return getCurrentStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.currentStatus</code> attribute. 
	 * @param value the currentStatus
	 */
	public void setCurrentStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, CURRENTSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.currentStatus</code> attribute. 
	 * @param value the currentStatus
	 */
	public void setCurrentStatus(final EnumerationValue value)
	{
		setCurrentStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.previousStatus</code> attribute.
	 * @return the previousStatus
	 */
	public EnumerationValue getPreviousStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, PREVIOUSSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.previousStatus</code> attribute.
	 * @return the previousStatus
	 */
	public EnumerationValue getPreviousStatus()
	{
		return getPreviousStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.previousStatus</code> attribute. 
	 * @param value the previousStatus
	 */
	public void setPreviousStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, PREVIOUSSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.previousStatus</code> attribute. 
	 * @param value the previousStatus
	 */
	public void setPreviousStatus(final EnumerationValue value)
	{
		setPreviousStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute.
	 * @return the sendEmail
	 */
	public Boolean isSendEmail(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SENDEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute.
	 * @return the sendEmail
	 */
	public Boolean isSendEmail()
	{
		return isSendEmail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute. 
	 * @return the sendEmail
	 */
	public boolean isSendEmailAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSendEmail( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute. 
	 * @return the sendEmail
	 */
	public boolean isSendEmailAsPrimitive()
	{
		return isSendEmailAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute. 
	 * @param value the sendEmail
	 */
	public void setSendEmail(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SENDEMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute. 
	 * @param value the sendEmail
	 */
	public void setSendEmail(final Boolean value)
	{
		setSendEmail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute. 
	 * @param value the sendEmail
	 */
	public void setSendEmail(final SessionContext ctx, final boolean value)
	{
		setSendEmail( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAEmailMatrix.sendEmail</code> attribute. 
	 * @param value the sendEmail
	 */
	public void setSendEmail(final boolean value)
	{
		setSendEmail( getSession().getSessionContext(), value );
	}
	
}
