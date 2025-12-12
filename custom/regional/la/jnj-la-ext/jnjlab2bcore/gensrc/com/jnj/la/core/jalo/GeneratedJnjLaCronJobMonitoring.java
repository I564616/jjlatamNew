/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjLaCronJobMonitoring}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaCronJobMonitoring extends GenericItem
{
	/** Qualifier of the <code>JnjLaCronJobMonitoring.jobCode</code> attribute **/
	public static final String JOBCODE = "jobCode";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.country</code> attribute **/
	public static final String COUNTRY = "country";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.frequency</code> attribute **/
	public static final String FREQUENCY = "frequency";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.serviceLevelStartTime</code> attribute **/
	public static final String SERVICELEVELSTARTTIME = "serviceLevelStartTime";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.serviceLevelEndTime</code> attribute **/
	public static final String SERVICELEVELENDTIME = "serviceLevelEndTime";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.averageRunningDuration</code> attribute **/
	public static final String AVERAGERUNNINGDURATION = "averageRunningDuration";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.monitoringCode</code> attribute **/
	public static final String MONITORINGCODE = "monitoringCode";
	/** Qualifier of the <code>JnjLaCronJobMonitoring.monitored</code> attribute **/
	public static final String MONITORED = "monitored";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(JOBCODE, AttributeMode.INITIAL);
		tmp.put(COUNTRY, AttributeMode.INITIAL);
		tmp.put(FREQUENCY, AttributeMode.INITIAL);
		tmp.put(SERVICELEVELSTARTTIME, AttributeMode.INITIAL);
		tmp.put(SERVICELEVELENDTIME, AttributeMode.INITIAL);
		tmp.put(AVERAGERUNNINGDURATION, AttributeMode.INITIAL);
		tmp.put(MONITORINGCODE, AttributeMode.INITIAL);
		tmp.put(MONITORED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.averageRunningDuration</code> attribute.
	 * @return the averageRunningDuration
	 */
	public String getAverageRunningDuration(final SessionContext ctx)
	{
		return (String)getProperty( ctx, AVERAGERUNNINGDURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.averageRunningDuration</code> attribute.
	 * @return the averageRunningDuration
	 */
	public String getAverageRunningDuration()
	{
		return getAverageRunningDuration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.averageRunningDuration</code> attribute. 
	 * @param value the averageRunningDuration
	 */
	public void setAverageRunningDuration(final SessionContext ctx, final String value)
	{
		setProperty(ctx, AVERAGERUNNINGDURATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.averageRunningDuration</code> attribute. 
	 * @param value the averageRunningDuration
	 */
	public void setAverageRunningDuration(final String value)
	{
		setAverageRunningDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.country</code> attribute.
	 * @return the country
	 */
	public String getCountry(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.country</code> attribute.
	 * @return the country
	 */
	public String getCountry()
	{
		return getCountry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.country</code> attribute. 
	 * @param value the country
	 */
	public void setCountry(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COUNTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.country</code> attribute. 
	 * @param value the country
	 */
	public void setCountry(final String value)
	{
		setCountry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.frequency</code> attribute.
	 * @return the frequency
	 */
	public String getFrequency(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.frequency</code> attribute.
	 * @return the frequency
	 */
	public String getFrequency()
	{
		return getFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.frequency</code> attribute. 
	 * @param value the frequency
	 */
	public void setFrequency(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.frequency</code> attribute. 
	 * @param value the frequency
	 */
	public void setFrequency(final String value)
	{
		setFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.jobCode</code> attribute.
	 * @return the jobCode
	 */
	public String getJobCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, JOBCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.jobCode</code> attribute.
	 * @return the jobCode
	 */
	public String getJobCode()
	{
		return getJobCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.jobCode</code> attribute. 
	 * @param value the jobCode
	 */
	public void setJobCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, JOBCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.jobCode</code> attribute. 
	 * @param value the jobCode
	 */
	public void setJobCode(final String value)
	{
		setJobCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute.
	 * @return the monitored
	 */
	public Boolean isMonitored(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, MONITORED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute.
	 * @return the monitored
	 */
	public Boolean isMonitored()
	{
		return isMonitored( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute. 
	 * @return the monitored
	 */
	public boolean isMonitoredAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isMonitored( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute. 
	 * @return the monitored
	 */
	public boolean isMonitoredAsPrimitive()
	{
		return isMonitoredAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute. 
	 * @param value the monitored
	 */
	public void setMonitored(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, MONITORED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute. 
	 * @param value the monitored
	 */
	public void setMonitored(final Boolean value)
	{
		setMonitored( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute. 
	 * @param value the monitored
	 */
	public void setMonitored(final SessionContext ctx, final boolean value)
	{
		setMonitored( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.monitored</code> attribute. 
	 * @param value the monitored
	 */
	public void setMonitored(final boolean value)
	{
		setMonitored( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.monitoringCode</code> attribute.
	 * @return the monitoringCode
	 */
	public String getMonitoringCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MONITORINGCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.monitoringCode</code> attribute.
	 * @return the monitoringCode
	 */
	public String getMonitoringCode()
	{
		return getMonitoringCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.monitoringCode</code> attribute. 
	 * @param value the monitoringCode
	 */
	public void setMonitoringCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MONITORINGCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.monitoringCode</code> attribute. 
	 * @param value the monitoringCode
	 */
	public void setMonitoringCode(final String value)
	{
		setMonitoringCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.serviceLevelEndTime</code> attribute.
	 * @return the serviceLevelEndTime
	 */
	public String getServiceLevelEndTime(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SERVICELEVELENDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.serviceLevelEndTime</code> attribute.
	 * @return the serviceLevelEndTime
	 */
	public String getServiceLevelEndTime()
	{
		return getServiceLevelEndTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.serviceLevelEndTime</code> attribute. 
	 * @param value the serviceLevelEndTime
	 */
	public void setServiceLevelEndTime(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SERVICELEVELENDTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.serviceLevelEndTime</code> attribute. 
	 * @param value the serviceLevelEndTime
	 */
	public void setServiceLevelEndTime(final String value)
	{
		setServiceLevelEndTime( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.serviceLevelStartTime</code> attribute.
	 * @return the serviceLevelStartTime
	 */
	public String getServiceLevelStartTime(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SERVICELEVELSTARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaCronJobMonitoring.serviceLevelStartTime</code> attribute.
	 * @return the serviceLevelStartTime
	 */
	public String getServiceLevelStartTime()
	{
		return getServiceLevelStartTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.serviceLevelStartTime</code> attribute. 
	 * @param value the serviceLevelStartTime
	 */
	public void setServiceLevelStartTime(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SERVICELEVELSTARTTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaCronJobMonitoring.serviceLevelStartTime</code> attribute. 
	 * @param value the serviceLevelStartTime
	 */
	public void setServiceLevelStartTime(final String value)
	{
		setServiceLevelStartTime( getSession().getSessionContext(), value );
	}
	
}
