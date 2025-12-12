/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob ExportERPOrderCronjob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedExportERPOrderCronjob extends CronJob
{
	/** Qualifier of the <code>ExportERPOrderCronjob.startDate</code> attribute **/
	public static final String STARTDATE = "startDate";
	/** Qualifier of the <code>ExportERPOrderCronjob.endDate</code> attribute **/
	public static final String ENDDATE = "endDate";
	/** Qualifier of the <code>ExportERPOrderCronjob.orderCodes</code> attribute **/
	public static final String ORDERCODES = "orderCodes";
	/** Qualifier of the <code>ExportERPOrderCronjob.orderTypes</code> attribute **/
	public static final String ORDERTYPES = "orderTypes";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(STARTDATE, AttributeMode.INITIAL);
		tmp.put(ENDDATE, AttributeMode.INITIAL);
		tmp.put(ORDERCODES, AttributeMode.INITIAL);
		tmp.put(ORDERTYPES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.endDate</code> attribute.
	 * @return the endDate
	 */
	public Date getEndDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.endDate</code> attribute.
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return getEndDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.endDate</code> attribute. 
	 * @param value the endDate
	 */
	public void setEndDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, ENDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.endDate</code> attribute. 
	 * @param value the endDate
	 */
	public void setEndDate(final Date value)
	{
		setEndDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.orderCodes</code> attribute.
	 * @return the orderCodes
	 */
	public List<String> getOrderCodes(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, ORDERCODES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.orderCodes</code> attribute.
	 * @return the orderCodes
	 */
	public List<String> getOrderCodes()
	{
		return getOrderCodes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.orderCodes</code> attribute. 
	 * @param value the orderCodes
	 */
	public void setOrderCodes(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, ORDERCODES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.orderCodes</code> attribute. 
	 * @param value the orderCodes
	 */
	public void setOrderCodes(final List<String> value)
	{
		setOrderCodes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.orderTypes</code> attribute.
	 * @return the orderTypes
	 */
	public List<EnumerationValue> getOrderTypes(final SessionContext ctx)
	{
		List<EnumerationValue> coll = (List<EnumerationValue>)getProperty( ctx, ORDERTYPES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.orderTypes</code> attribute.
	 * @return the orderTypes
	 */
	public List<EnumerationValue> getOrderTypes()
	{
		return getOrderTypes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.orderTypes</code> attribute. 
	 * @param value the orderTypes
	 */
	public void setOrderTypes(final SessionContext ctx, final List<EnumerationValue> value)
	{
		setProperty(ctx, ORDERTYPES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.orderTypes</code> attribute. 
	 * @param value the orderTypes
	 */
	public void setOrderTypes(final List<EnumerationValue> value)
	{
		setOrderTypes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.startDate</code> attribute.
	 * @return the startDate
	 */
	public Date getStartDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportERPOrderCronjob.startDate</code> attribute.
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return getStartDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.startDate</code> attribute. 
	 * @param value the startDate
	 */
	public void setStartDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, STARTDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExportERPOrderCronjob.startDate</code> attribute. 
	 * @param value the startDate
	 */
	public void setStartDate(final Date value)
	{
		setStartDate( getSession().getSessionContext(), value );
	}
	
}
