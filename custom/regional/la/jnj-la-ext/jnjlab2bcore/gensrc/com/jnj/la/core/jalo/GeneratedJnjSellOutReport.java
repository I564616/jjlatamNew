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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjSellOutReport}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjSellOutReport extends GenericItem
{
	/** Qualifier of the <code>JnjSellOutReport.docName</code> attribute **/
	public static final String DOCNAME = "docName";
	/** Qualifier of the <code>JnjSellOutReport.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>JnjSellOutReport.company</code> attribute **/
	public static final String COMPANY = "company";
	/** Qualifier of the <code>JnjSellOutReport.user</code> attribute **/
	public static final String USER = "user";
	/** Qualifier of the <code>JnjSellOutReport.date</code> attribute **/
	public static final String DATE = "date";
	/** Qualifier of the <code>JnjSellOutReport.b2bUnitId</code> attribute **/
	public static final String B2BUNITID = "b2bUnitId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(DOCNAME, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(COMPANY, AttributeMode.INITIAL);
		tmp.put(USER, AttributeMode.INITIAL);
		tmp.put(DATE, AttributeMode.INITIAL);
		tmp.put(B2BUNITID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, B2BUNITID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId()
	{
		return getB2bUnitId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, B2BUNITID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final JnJB2BUnit value)
	{
		setB2bUnitId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.company</code> attribute.
	 * @return the company
	 */
	public String getCompany(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPANY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.company</code> attribute.
	 * @return the company
	 */
	public String getCompany()
	{
		return getCompany( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.company</code> attribute. 
	 * @param value the company
	 */
	public void setCompany(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPANY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.company</code> attribute. 
	 * @param value the company
	 */
	public void setCompany(final String value)
	{
		setCompany( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.customer</code> attribute.
	 * @return the customer
	 */
	public String getCustomer(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.customer</code> attribute.
	 * @return the customer
	 */
	public String getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final String value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.date</code> attribute.
	 * @return the date
	 */
	public Date getDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.date</code> attribute.
	 * @return the date
	 */
	public Date getDate()
	{
		return getDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.date</code> attribute. 
	 * @param value the date
	 */
	public void setDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, DATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.date</code> attribute. 
	 * @param value the date
	 */
	public void setDate(final Date value)
	{
		setDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.docName</code> attribute.
	 * @return the docName
	 */
	public String getDocName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DOCNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.docName</code> attribute.
	 * @return the docName
	 */
	public String getDocName()
	{
		return getDocName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.docName</code> attribute. 
	 * @param value the docName
	 */
	public void setDocName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DOCNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.docName</code> attribute. 
	 * @param value the docName
	 */
	public void setDocName(final String value)
	{
		setDocName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.user</code> attribute.
	 * @return the user
	 */
	public String getUser(final SessionContext ctx)
	{
		return (String)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSellOutReport.user</code> attribute.
	 * @return the user
	 */
	public String getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final String value)
	{
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSellOutReport.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final String value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
}
