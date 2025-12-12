/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 9:48:14 AM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLaInvoiceNotificationEmailProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaInvoiceNotificationEmailProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailData</code> attribute **/
	public static final String INVOICENOTIFICATIONEMAILDATA = "invoiceNotificationEmailData";
	/** Qualifier of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailEntries</code> attribute **/
	public static final String INVOICENOTIFICATIONEMAILENTRIES = "invoiceNotificationEmailEntries";
	/** Qualifier of the <code>JnjLaInvoiceNotificationEmailProcess.b2bCustomerEmails</code> attribute **/
	public static final String B2BCUSTOMEREMAILS = "b2bCustomerEmails";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(INVOICENOTIFICATIONEMAILDATA, AttributeMode.INITIAL);
		tmp.put(INVOICENOTIFICATIONEMAILENTRIES, AttributeMode.INITIAL);
		tmp.put(B2BCUSTOMEREMAILS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaInvoiceNotificationEmailProcess.b2bCustomerEmails</code> attribute.
	 * @return the b2bCustomerEmails - List containing elements representing invoice
	 *                         notification details.
	 */
	public List<String> getB2bCustomerEmails(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, B2BCUSTOMEREMAILS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaInvoiceNotificationEmailProcess.b2bCustomerEmails</code> attribute.
	 * @return the b2bCustomerEmails - List containing elements representing invoice
	 *                         notification details.
	 */
	public List<String> getB2bCustomerEmails()
	{
		return getB2bCustomerEmails( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaInvoiceNotificationEmailProcess.b2bCustomerEmails</code> attribute. 
	 * @param value the b2bCustomerEmails - List containing elements representing invoice
	 *                         notification details.
	 */
	public void setB2bCustomerEmails(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, B2BCUSTOMEREMAILS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaInvoiceNotificationEmailProcess.b2bCustomerEmails</code> attribute. 
	 * @param value the b2bCustomerEmails - List containing elements representing invoice
	 *                         notification details.
	 */
	public void setB2bCustomerEmails(final List<String> value)
	{
		setB2bCustomerEmails( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailData</code> attribute.
	 * @return the invoiceNotificationEmailData - String containing elements representing concatenated data for Invoice Notification email.
	 */
	public String getInvoiceNotificationEmailData(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INVOICENOTIFICATIONEMAILDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailData</code> attribute.
	 * @return the invoiceNotificationEmailData - String containing elements representing concatenated data for Invoice Notification email.
	 */
	public String getInvoiceNotificationEmailData()
	{
		return getInvoiceNotificationEmailData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailData</code> attribute. 
	 * @param value the invoiceNotificationEmailData - String containing elements representing concatenated data for Invoice Notification email.
	 */
	public void setInvoiceNotificationEmailData(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INVOICENOTIFICATIONEMAILDATA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailData</code> attribute. 
	 * @param value the invoiceNotificationEmailData - String containing elements representing concatenated data for Invoice Notification email.
	 */
	public void setInvoiceNotificationEmailData(final String value)
	{
		setInvoiceNotificationEmailData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailEntries</code> attribute.
	 * @return the invoiceNotificationEmailEntries - List containing elements representing concatenated
	 *                         data for Invoice Notification email.
	 */
	public List<String> getInvoiceNotificationEmailEntries(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, INVOICENOTIFICATIONEMAILENTRIES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailEntries</code> attribute.
	 * @return the invoiceNotificationEmailEntries - List containing elements representing concatenated
	 *                         data for Invoice Notification email.
	 */
	public List<String> getInvoiceNotificationEmailEntries()
	{
		return getInvoiceNotificationEmailEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailEntries</code> attribute. 
	 * @param value the invoiceNotificationEmailEntries - List containing elements representing concatenated
	 *                         data for Invoice Notification email.
	 */
	public void setInvoiceNotificationEmailEntries(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, INVOICENOTIFICATIONEMAILENTRIES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaInvoiceNotificationEmailProcess.invoiceNotificationEmailEntries</code> attribute. 
	 * @param value the invoiceNotificationEmailEntries - List containing elements representing concatenated
	 *                         data for Invoice Notification email.
	 */
	public void setInvoiceNotificationEmailEntries(final List<String> value)
	{
		setInvoiceNotificationEmailEntries( getSession().getSessionContext(), value );
	}
	
}
