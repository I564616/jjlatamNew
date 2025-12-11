/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2BUnit;
import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnJLaUserAccountPreference}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnJLaUserAccountPreference extends GenericItem
{
	/** Qualifier of the <code>JnJLaUserAccountPreference.user</code> attribute **/
	public static final String USER = "user";
	/** Qualifier of the <code>JnJLaUserAccountPreference.account</code> attribute **/
	public static final String ACCOUNT = "account";
	/** Qualifier of the <code>JnJLaUserAccountPreference.periodicity</code> attribute **/
	public static final String PERIODICITY = "periodicity";
	/** Qualifier of the <code>JnJLaUserAccountPreference.orderTypes</code> attribute **/
	public static final String ORDERTYPES = "orderTypes";
	/** Qualifier of the <code>JnJLaUserAccountPreference.consolidatedEmailFrequency</code> attribute **/
	public static final String CONSOLIDATEDEMAILFREQUENCY = "consolidatedEmailFrequency";
	/** Qualifier of the <code>JnJLaUserAccountPreference.dayOfTheWeek</code> attribute **/
	public static final String DAYOFTHEWEEK = "dayOfTheWeek";
	/** Qualifier of the <code>JnJLaUserAccountPreference.daysOfTheMonth</code> attribute **/
	public static final String DAYSOFTHEMONTH = "daysOfTheMonth";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USER, AttributeMode.INITIAL);
		tmp.put(ACCOUNT, AttributeMode.INITIAL);
		tmp.put(PERIODICITY, AttributeMode.INITIAL);
		tmp.put(ORDERTYPES, AttributeMode.INITIAL);
		tmp.put(CONSOLIDATEDEMAILFREQUENCY, AttributeMode.INITIAL);
		tmp.put(DAYOFTHEWEEK, AttributeMode.INITIAL);
		tmp.put(DAYSOFTHEMONTH, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.account</code> attribute.
	 * @return the account
	 */
	public JnJB2BUnit getAccount(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, ACCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.account</code> attribute.
	 * @return the account
	 */
	public JnJB2BUnit getAccount()
	{
		return getAccount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.account</code> attribute. 
	 * @param value the account
	 */
	public void setAccount(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, ACCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.account</code> attribute. 
	 * @param value the account
	 */
	public void setAccount(final JnJB2BUnit value)
	{
		setAccount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.consolidatedEmailFrequency</code> attribute.
	 * @return the consolidatedEmailFrequency
	 */
	public EnumerationValue getConsolidatedEmailFrequency(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, CONSOLIDATEDEMAILFREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.consolidatedEmailFrequency</code> attribute.
	 * @return the consolidatedEmailFrequency
	 */
	public EnumerationValue getConsolidatedEmailFrequency()
	{
		return getConsolidatedEmailFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.consolidatedEmailFrequency</code> attribute. 
	 * @param value the consolidatedEmailFrequency
	 */
	public void setConsolidatedEmailFrequency(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, CONSOLIDATEDEMAILFREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.consolidatedEmailFrequency</code> attribute. 
	 * @param value the consolidatedEmailFrequency
	 */
	public void setConsolidatedEmailFrequency(final EnumerationValue value)
	{
		setConsolidatedEmailFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.dayOfTheWeek</code> attribute.
	 * @return the dayOfTheWeek
	 */
	public EnumerationValue getDayOfTheWeek(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, DAYOFTHEWEEK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.dayOfTheWeek</code> attribute.
	 * @return the dayOfTheWeek
	 */
	public EnumerationValue getDayOfTheWeek()
	{
		return getDayOfTheWeek( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.dayOfTheWeek</code> attribute. 
	 * @param value the dayOfTheWeek
	 */
	public void setDayOfTheWeek(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, DAYOFTHEWEEK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.dayOfTheWeek</code> attribute. 
	 * @param value the dayOfTheWeek
	 */
	public void setDayOfTheWeek(final EnumerationValue value)
	{
		setDayOfTheWeek( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.daysOfTheMonth</code> attribute.
	 * @return the daysOfTheMonth
	 */
	public List<String> getDaysOfTheMonth(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, DAYSOFTHEMONTH);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.daysOfTheMonth</code> attribute.
	 * @return the daysOfTheMonth
	 */
	public List<String> getDaysOfTheMonth()
	{
		return getDaysOfTheMonth( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.daysOfTheMonth</code> attribute. 
	 * @param value the daysOfTheMonth
	 */
	public void setDaysOfTheMonth(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, DAYSOFTHEMONTH,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.daysOfTheMonth</code> attribute. 
	 * @param value the daysOfTheMonth
	 */
	public void setDaysOfTheMonth(final List<String> value)
	{
		setDaysOfTheMonth( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.orderTypes</code> attribute.
	 * @return the orderTypes
	 */
	public List<EnumerationValue> getOrderTypes(final SessionContext ctx)
	{
		List<EnumerationValue> coll = (List<EnumerationValue>)getProperty( ctx, ORDERTYPES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.orderTypes</code> attribute.
	 * @return the orderTypes
	 */
	public List<EnumerationValue> getOrderTypes()
	{
		return getOrderTypes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.orderTypes</code> attribute. 
	 * @param value the orderTypes
	 */
	public void setOrderTypes(final SessionContext ctx, final List<EnumerationValue> value)
	{
		setProperty(ctx, ORDERTYPES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.orderTypes</code> attribute. 
	 * @param value the orderTypes
	 */
	public void setOrderTypes(final List<EnumerationValue> value)
	{
		setOrderTypes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.periodicity</code> attribute.
	 * @return the periodicity
	 */
	public EnumerationValue getPeriodicity(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, PERIODICITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.periodicity</code> attribute.
	 * @return the periodicity
	 */
	public EnumerationValue getPeriodicity()
	{
		return getPeriodicity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.periodicity</code> attribute. 
	 * @param value the periodicity
	 */
	public void setPeriodicity(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, PERIODICITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.periodicity</code> attribute. 
	 * @param value the periodicity
	 */
	public void setPeriodicity(final EnumerationValue value)
	{
		setPeriodicity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser(final SessionContext ctx)
	{
		return (JnJB2bCustomer)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaUserAccountPreference.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final JnJB2bCustomer value)
	{
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaUserAccountPreference.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final JnJB2bCustomer value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
}
