/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 9:48:14 AM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob JnjCDLExportJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjCDLExportJob extends CronJob
{
	/** Qualifier of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute **/
	public static final String DATAFORLASTNOOFDAYS = "dataForLastNoOfDays";
	/** Qualifier of the <code>JnjCDLExportJob.startDate</code> attribute **/
	public static final String STARTDATE = "startDate";
	/** Qualifier of the <code>JnjCDLExportJob.endDate</code> attribute **/
	public static final String ENDDATE = "endDate";
	/** Qualifier of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute **/
	public static final String ISFETCHFROMDATERANGE = "isFetchFromDateRange";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(DATAFORLASTNOOFDAYS, AttributeMode.INITIAL);
		tmp.put(STARTDATE, AttributeMode.INITIAL);
		tmp.put(ENDDATE, AttributeMode.INITIAL);
		tmp.put(ISFETCHFROMDATERANGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute.
	 * @return the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public Integer getDataForLastNoOfDays(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, DATAFORLASTNOOFDAYS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute.
	 * @return the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public Integer getDataForLastNoOfDays()
	{
		return getDataForLastNoOfDays( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute. 
	 * @return the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public int getDataForLastNoOfDaysAsPrimitive(final SessionContext ctx)
	{
		Integer value = getDataForLastNoOfDays( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute. 
	 * @return the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public int getDataForLastNoOfDaysAsPrimitive()
	{
		return getDataForLastNoOfDaysAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute. 
	 * @param value the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public void setDataForLastNoOfDays(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, DATAFORLASTNOOFDAYS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute. 
	 * @param value the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public void setDataForLastNoOfDays(final Integer value)
	{
		setDataForLastNoOfDays( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute. 
	 * @param value the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public void setDataForLastNoOfDays(final SessionContext ctx, final int value)
	{
		setDataForLastNoOfDays( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.dataForLastNoOfDays</code> attribute. 
	 * @param value the dataForLastNoOfDays - Specifies the number of days from which the data to be pulled
	 */
	public void setDataForLastNoOfDays(final int value)
	{
		setDataForLastNoOfDays( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.endDate</code> attribute.
	 * @return the endDate
	 */
	public Date getEndDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.endDate</code> attribute.
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return getEndDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.endDate</code> attribute. 
	 * @param value the endDate
	 */
	public void setEndDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, ENDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.endDate</code> attribute. 
	 * @param value the endDate
	 */
	public void setEndDate(final Date value)
	{
		setEndDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute.
	 * @return the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public Boolean isIsFetchFromDateRange(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISFETCHFROMDATERANGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute.
	 * @return the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public Boolean isIsFetchFromDateRange()
	{
		return isIsFetchFromDateRange( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute. 
	 * @return the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public boolean isIsFetchFromDateRangeAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsFetchFromDateRange( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute. 
	 * @return the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public boolean isIsFetchFromDateRangeAsPrimitive()
	{
		return isIsFetchFromDateRangeAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute. 
	 * @param value the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public void setIsFetchFromDateRange(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISFETCHFROMDATERANGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute. 
	 * @param value the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public void setIsFetchFromDateRange(final Boolean value)
	{
		setIsFetchFromDateRange( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute. 
	 * @param value the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public void setIsFetchFromDateRange(final SessionContext ctx, final boolean value)
	{
		setIsFetchFromDateRange( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.isFetchFromDateRange</code> attribute. 
	 * @param value the isFetchFromDateRange - flag to identify fetch from Date Range
	 */
	public void setIsFetchFromDateRange(final boolean value)
	{
		setIsFetchFromDateRange( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.startDate</code> attribute.
	 * @return the startDate
	 */
	public Date getStartDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCDLExportJob.startDate</code> attribute.
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return getStartDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.startDate</code> attribute. 
	 * @param value the startDate
	 */
	public void setStartDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, STARTDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCDLExportJob.startDate</code> attribute. 
	 * @param value the startDate
	 */
	public void setStartDate(final Date value)
	{
		setStartDate( getSession().getSessionContext(), value );
	}
	
}
