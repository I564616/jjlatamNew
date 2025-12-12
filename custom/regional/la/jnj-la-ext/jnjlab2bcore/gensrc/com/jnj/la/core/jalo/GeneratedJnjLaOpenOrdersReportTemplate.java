/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjLaOpenOrdersReportTemplate}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaOpenOrdersReportTemplate extends GenericItem
{
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.user</code> attribute **/
	public static final String USER = "user";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.templateName</code> attribute **/
	public static final String TEMPLATENAME = "templateName";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.selectedAccountIds</code> attribute **/
	public static final String SELECTEDACCOUNTIDS = "selectedAccountIds";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.quickSelection</code> attribute **/
	public static final String QUICKSELECTION = "quickSelection";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.fromDate</code> attribute **/
	public static final String FROMDATE = "fromDate";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.toDate</code> attribute **/
	public static final String TODATE = "toDate";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.orderType</code> attribute **/
	public static final String ORDERTYPE = "orderType";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.orderNumber</code> attribute **/
	public static final String ORDERNUMBER = "orderNumber";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.shipTo</code> attribute **/
	public static final String SHIPTO = "shipTo";
	/** Qualifier of the <code>JnjLaOpenOrdersReportTemplate.reportColumns</code> attribute **/
	public static final String REPORTCOLUMNS = "reportColumns";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USER, AttributeMode.INITIAL);
		tmp.put(TEMPLATENAME, AttributeMode.INITIAL);
		tmp.put(SELECTEDACCOUNTIDS, AttributeMode.INITIAL);
		tmp.put(QUICKSELECTION, AttributeMode.INITIAL);
		tmp.put(FROMDATE, AttributeMode.INITIAL);
		tmp.put(TODATE, AttributeMode.INITIAL);
		tmp.put(ORDERTYPE, AttributeMode.INITIAL);
		tmp.put(ORDERNUMBER, AttributeMode.INITIAL);
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(SHIPTO, AttributeMode.INITIAL);
		tmp.put(REPORTCOLUMNS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.fromDate</code> attribute.
	 * @return the fromDate
	 */
	public String getFromDate(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FROMDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.fromDate</code> attribute.
	 * @return the fromDate
	 */
	public String getFromDate()
	{
		return getFromDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.fromDate</code> attribute. 
	 * @param value the fromDate
	 */
	public void setFromDate(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FROMDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.fromDate</code> attribute. 
	 * @param value the fromDate
	 */
	public void setFromDate(final String value)
	{
		setFromDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.orderNumber</code> attribute.
	 * @return the orderNumber
	 */
	public String getOrderNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ORDERNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.orderNumber</code> attribute.
	 * @return the orderNumber
	 */
	public String getOrderNumber()
	{
		return getOrderNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.orderNumber</code> attribute. 
	 * @param value the orderNumber
	 */
	public void setOrderNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ORDERNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.orderNumber</code> attribute. 
	 * @param value the orderNumber
	 */
	public void setOrderNumber(final String value)
	{
		setOrderNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.orderType</code> attribute.
	 * @return the orderType
	 */
	public String getOrderType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ORDERTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.orderType</code> attribute.
	 * @return the orderType
	 */
	public String getOrderType()
	{
		return getOrderType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.orderType</code> attribute. 
	 * @param value the orderType
	 */
	public void setOrderType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ORDERTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.orderType</code> attribute. 
	 * @param value the orderType
	 */
	public void setOrderType(final String value)
	{
		setOrderType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRODUCTCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.quickSelection</code> attribute.
	 * @return the quickSelection
	 */
	public String getQuickSelection(final SessionContext ctx)
	{
		return (String)getProperty( ctx, QUICKSELECTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.quickSelection</code> attribute.
	 * @return the quickSelection
	 */
	public String getQuickSelection()
	{
		return getQuickSelection( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.quickSelection</code> attribute. 
	 * @param value the quickSelection
	 */
	public void setQuickSelection(final SessionContext ctx, final String value)
	{
		setProperty(ctx, QUICKSELECTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.quickSelection</code> attribute. 
	 * @param value the quickSelection
	 */
	public void setQuickSelection(final String value)
	{
		setQuickSelection( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.reportColumns</code> attribute.
	 * @return the reportColumns
	 */
	public String getReportColumns(final SessionContext ctx)
	{
		return (String)getProperty( ctx, REPORTCOLUMNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.reportColumns</code> attribute.
	 * @return the reportColumns
	 */
	public String getReportColumns()
	{
		return getReportColumns( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.reportColumns</code> attribute. 
	 * @param value the reportColumns
	 */
	public void setReportColumns(final SessionContext ctx, final String value)
	{
		setProperty(ctx, REPORTCOLUMNS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.reportColumns</code> attribute. 
	 * @param value the reportColumns
	 */
	public void setReportColumns(final String value)
	{
		setReportColumns( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.selectedAccountIds</code> attribute.
	 * @return the selectedAccountIds
	 */
	public List<String> getSelectedAccountIds(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, SELECTEDACCOUNTIDS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.selectedAccountIds</code> attribute.
	 * @return the selectedAccountIds
	 */
	public List<String> getSelectedAccountIds()
	{
		return getSelectedAccountIds( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.selectedAccountIds</code> attribute. 
	 * @param value the selectedAccountIds
	 */
	public void setSelectedAccountIds(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, SELECTEDACCOUNTIDS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.selectedAccountIds</code> attribute. 
	 * @param value the selectedAccountIds
	 */
	public void setSelectedAccountIds(final List<String> value)
	{
		setSelectedAccountIds( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.shipTo</code> attribute.
	 * @return the shipTo
	 */
	public String getShipTo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SHIPTO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.shipTo</code> attribute.
	 * @return the shipTo
	 */
	public String getShipTo()
	{
		return getShipTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.shipTo</code> attribute. 
	 * @param value the shipTo
	 */
	public void setShipTo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SHIPTO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.shipTo</code> attribute. 
	 * @param value the shipTo
	 */
	public void setShipTo(final String value)
	{
		setShipTo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.templateName</code> attribute.
	 * @return the templateName
	 */
	public String getTemplateName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TEMPLATENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.templateName</code> attribute.
	 * @return the templateName
	 */
	public String getTemplateName()
	{
		return getTemplateName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.templateName</code> attribute. 
	 * @param value the templateName
	 */
	public void setTemplateName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TEMPLATENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.templateName</code> attribute. 
	 * @param value the templateName
	 */
	public void setTemplateName(final String value)
	{
		setTemplateName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.toDate</code> attribute.
	 * @return the toDate
	 */
	public String getToDate(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TODATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.toDate</code> attribute.
	 * @return the toDate
	 */
	public String getToDate()
	{
		return getToDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.toDate</code> attribute. 
	 * @param value the toDate
	 */
	public void setToDate(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TODATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.toDate</code> attribute. 
	 * @param value the toDate
	 */
	public void setToDate(final String value)
	{
		setToDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser(final SessionContext ctx)
	{
		return (JnJB2bCustomer)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaOpenOrdersReportTemplate.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final JnJB2bCustomer value)
	{
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaOpenOrdersReportTemplate.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final JnJB2bCustomer value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
}
