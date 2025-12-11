/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2BUnit;
import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.PartOfHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLaConsolidatedEmailProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLaConsolidatedEmailProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLaConsolidatedEmailProcess.unit</code> attribute **/
	public static final String UNIT = "unit";
	/** Qualifier of the <code>JnjLaConsolidatedEmailProcess.users</code> attribute **/
	public static final String USERS = "users";
	/** Qualifier of the <code>JnjLaConsolidatedEmailProcess.defaultRecipients</code> attribute **/
	public static final String DEFAULTRECIPIENTS = "defaultRecipients";
	/** Qualifier of the <code>JnjLaConsolidatedEmailProcess.reportOrders</code> attribute **/
	public static final String REPORTORDERS = "reportOrders";
	/**
	* {@link OneToManyHandler} for handling 1:n REPORTORDERS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Order> REPORTORDERSHANDLER = new OneToManyHandler<Order>(
	CoreConstants.TC.ORDER,
	false,
	"emailProcess",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(UNIT, AttributeMode.INITIAL);
		tmp.put(USERS, AttributeMode.INITIAL);
		tmp.put(DEFAULTRECIPIENTS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.defaultRecipients</code> attribute.
	 * @return the defaultRecipients
	 */
	public List<String> getDefaultRecipients(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, DEFAULTRECIPIENTS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.defaultRecipients</code> attribute.
	 * @return the defaultRecipients
	 */
	public List<String> getDefaultRecipients()
	{
		return getDefaultRecipients( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.defaultRecipients</code> attribute. 
	 * @param value the defaultRecipients
	 */
	public void setDefaultRecipients(final SessionContext ctx, final List<String> value)
	{
		new PartOfHandler<List<String>>()
		{
			@Override
			protected List<String> doGetValue(final SessionContext ctx)
			{
				return getDefaultRecipients( ctx );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final List<String> _value)
			{
				final List<String> value = _value;
				setProperty(ctx, DEFAULTRECIPIENTS,value == null || !value.isEmpty() ? value : null );
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.defaultRecipients</code> attribute. 
	 * @param value the defaultRecipients
	 */
	public void setDefaultRecipients(final List<String> value)
	{
		setDefaultRecipients( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.reportOrders</code> attribute.
	 * @return the reportOrders
	 */
	public Collection<Order> getReportOrders(final SessionContext ctx)
	{
		return REPORTORDERSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.reportOrders</code> attribute.
	 * @return the reportOrders
	 */
	public Collection<Order> getReportOrders()
	{
		return getReportOrders( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.reportOrders</code> attribute. 
	 * @param value the reportOrders
	 */
	public void setReportOrders(final SessionContext ctx, final Collection<Order> value)
	{
		REPORTORDERSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.reportOrders</code> attribute. 
	 * @param value the reportOrders
	 */
	public void setReportOrders(final Collection<Order> value)
	{
		setReportOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to reportOrders. 
	 * @param value the item to add to reportOrders
	 */
	public void addToReportOrders(final SessionContext ctx, final Order value)
	{
		REPORTORDERSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to reportOrders. 
	 * @param value the item to add to reportOrders
	 */
	public void addToReportOrders(final Order value)
	{
		addToReportOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from reportOrders. 
	 * @param value the item to remove from reportOrders
	 */
	public void removeFromReportOrders(final SessionContext ctx, final Order value)
	{
		REPORTORDERSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from reportOrders. 
	 * @param value the item to remove from reportOrders
	 */
	public void removeFromReportOrders(final Order value)
	{
		removeFromReportOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.unit</code> attribute.
	 * @return the unit
	 */
	public JnJB2BUnit getUnit(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.unit</code> attribute.
	 * @return the unit
	 */
	public JnJB2BUnit getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.unit</code> attribute. 
	 * @param value the unit
	 */
	public void setUnit(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, UNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.unit</code> attribute. 
	 * @param value the unit
	 */
	public void setUnit(final JnJB2BUnit value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.users</code> attribute.
	 * @return the users
	 */
	public List<JnJB2bCustomer> getUsers(final SessionContext ctx)
	{
		List<JnJB2bCustomer> coll = (List<JnJB2bCustomer>)getProperty( ctx, USERS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaConsolidatedEmailProcess.users</code> attribute.
	 * @return the users
	 */
	public List<JnJB2bCustomer> getUsers()
	{
		return getUsers( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.users</code> attribute. 
	 * @param value the users
	 */
	public void setUsers(final SessionContext ctx, final List<JnJB2bCustomer> value)
	{
		new PartOfHandler<List<JnJB2bCustomer>>()
		{
			@Override
			protected List<JnJB2bCustomer> doGetValue(final SessionContext ctx)
			{
				return getUsers( ctx );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final List<JnJB2bCustomer> _value)
			{
				final List<JnJB2bCustomer> value = _value;
				setProperty(ctx, USERS,value == null || !value.isEmpty() ? value : null );
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaConsolidatedEmailProcess.users</code> attribute. 
	 * @param value the users
	 */
	public void setUsers(final List<JnJB2bCustomer> value)
	{
		setUsers( getSession().getSessionContext(), value );
	}
	
}
