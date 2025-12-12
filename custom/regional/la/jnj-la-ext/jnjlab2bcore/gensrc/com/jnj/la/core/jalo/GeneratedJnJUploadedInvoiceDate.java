/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnJUploadedInvoiceDate}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnJUploadedInvoiceDate extends GenericItem
{
	/** Qualifier of the <code>JnJUploadedInvoiceDate.user</code> attribute **/
	public static final String USER = "user";
	/** Qualifier of the <code>JnJUploadedInvoiceDate.filename</code> attribute **/
	public static final String FILENAME = "filename";
	/** Qualifier of the <code>JnJUploadedInvoiceDate.currentStatus</code> attribute **/
	public static final String CURRENTSTATUS = "currentStatus";
	/** Qualifier of the <code>JnJUploadedInvoiceDate.errorMessage</code> attribute **/
	public static final String ERRORMESSAGE = "errorMessage";
	/** Qualifier of the <code>JnJUploadedInvoiceDate.erased</code> attribute **/
	public static final String ERASED = "erased";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USER, AttributeMode.INITIAL);
		tmp.put(FILENAME, AttributeMode.INITIAL);
		tmp.put(CURRENTSTATUS, AttributeMode.INITIAL);
		tmp.put(ERRORMESSAGE, AttributeMode.INITIAL);
		tmp.put(ERASED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.currentStatus</code> attribute.
	 * @return the currentStatus
	 */
	public EnumerationValue getCurrentStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, CURRENTSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.currentStatus</code> attribute.
	 * @return the currentStatus
	 */
	public EnumerationValue getCurrentStatus()
	{
		return getCurrentStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.currentStatus</code> attribute. 
	 * @param value the currentStatus
	 */
	public void setCurrentStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, CURRENTSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.currentStatus</code> attribute. 
	 * @param value the currentStatus
	 */
	public void setCurrentStatus(final EnumerationValue value)
	{
		setCurrentStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.erased</code> attribute.
	 * @return the erased
	 */
	public Boolean isErased(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ERASED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.erased</code> attribute.
	 * @return the erased
	 */
	public Boolean isErased()
	{
		return isErased( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.erased</code> attribute. 
	 * @return the erased
	 */
	public boolean isErasedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isErased( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.erased</code> attribute. 
	 * @return the erased
	 */
	public boolean isErasedAsPrimitive()
	{
		return isErasedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.erased</code> attribute. 
	 * @param value the erased
	 */
	public void setErased(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ERASED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.erased</code> attribute. 
	 * @param value the erased
	 */
	public void setErased(final Boolean value)
	{
		setErased( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.erased</code> attribute. 
	 * @param value the erased
	 */
	public void setErased(final SessionContext ctx, final boolean value)
	{
		setErased( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.erased</code> attribute. 
	 * @param value the erased
	 */
	public void setErased(final boolean value)
	{
		setErased( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.errorMessage</code> attribute.
	 * @return the errorMessage
	 */
	public EnumerationValue getErrorMessage(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, ERRORMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.errorMessage</code> attribute.
	 * @return the errorMessage
	 */
	public EnumerationValue getErrorMessage()
	{
		return getErrorMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.errorMessage</code> attribute. 
	 * @param value the errorMessage
	 */
	public void setErrorMessage(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, ERRORMESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.errorMessage</code> attribute. 
	 * @param value the errorMessage
	 */
	public void setErrorMessage(final EnumerationValue value)
	{
		setErrorMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.filename</code> attribute.
	 * @return the filename
	 */
	public String getFilename(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FILENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.filename</code> attribute.
	 * @return the filename
	 */
	public String getFilename()
	{
		return getFilename( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.filename</code> attribute. 
	 * @param value the filename
	 */
	public void setFilename(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FILENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.filename</code> attribute. 
	 * @param value the filename
	 */
	public void setFilename(final String value)
	{
		setFilename( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser(final SessionContext ctx)
	{
		return (JnJB2bCustomer)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJUploadedInvoiceDate.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final JnJB2bCustomer value)
	{
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJUploadedInvoiceDate.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final JnJB2bCustomer value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
}
