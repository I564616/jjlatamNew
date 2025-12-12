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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjOrderArchivalResult}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjOrderArchivalResult extends GenericItem
{
	/** Qualifier of the <code>JnjOrderArchivalResult.orderList</code> attribute **/
	public static final String ORDERLIST = "orderList";
	/** Qualifier of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute **/
	public static final String RECORDSDELETEDCOUNT = "recordsDeletedCount";
	/** Qualifier of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute **/
	public static final String RECORDSFETCHEDCOUNT = "recordsFetchedCount";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ORDERLIST, AttributeMode.INITIAL);
		tmp.put(RECORDSDELETEDCOUNT, AttributeMode.INITIAL);
		tmp.put(RECORDSFETCHEDCOUNT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.orderList</code> attribute.
	 * @return the orderList - Holds list of Orders deleted
	 */
	public String getOrderList(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ORDERLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.orderList</code> attribute.
	 * @return the orderList - Holds list of Orders deleted
	 */
	public String getOrderList()
	{
		return getOrderList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.orderList</code> attribute. 
	 * @param value the orderList - Holds list of Orders deleted
	 */
	public void setOrderList(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ORDERLIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.orderList</code> attribute. 
	 * @param value the orderList - Holds list of Orders deleted
	 */
	public void setOrderList(final String value)
	{
		setOrderList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute.
	 * @return the recordsDeletedCount - Hold the number of records deleted
	 */
	public Integer getRecordsDeletedCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, RECORDSDELETEDCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute.
	 * @return the recordsDeletedCount - Hold the number of records deleted
	 */
	public Integer getRecordsDeletedCount()
	{
		return getRecordsDeletedCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute. 
	 * @return the recordsDeletedCount - Hold the number of records deleted
	 */
	public int getRecordsDeletedCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getRecordsDeletedCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute. 
	 * @return the recordsDeletedCount - Hold the number of records deleted
	 */
	public int getRecordsDeletedCountAsPrimitive()
	{
		return getRecordsDeletedCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute. 
	 * @param value the recordsDeletedCount - Hold the number of records deleted
	 */
	public void setRecordsDeletedCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, RECORDSDELETEDCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute. 
	 * @param value the recordsDeletedCount - Hold the number of records deleted
	 */
	public void setRecordsDeletedCount(final Integer value)
	{
		setRecordsDeletedCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute. 
	 * @param value the recordsDeletedCount - Hold the number of records deleted
	 */
	public void setRecordsDeletedCount(final SessionContext ctx, final int value)
	{
		setRecordsDeletedCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsDeletedCount</code> attribute. 
	 * @param value the recordsDeletedCount - Hold the number of records deleted
	 */
	public void setRecordsDeletedCount(final int value)
	{
		setRecordsDeletedCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute.
	 * @return the recordsFetchedCount - Holds list of record fetched
	 */
	public Integer getRecordsFetchedCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, RECORDSFETCHEDCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute.
	 * @return the recordsFetchedCount - Holds list of record fetched
	 */
	public Integer getRecordsFetchedCount()
	{
		return getRecordsFetchedCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute. 
	 * @return the recordsFetchedCount - Holds list of record fetched
	 */
	public int getRecordsFetchedCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getRecordsFetchedCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute. 
	 * @return the recordsFetchedCount - Holds list of record fetched
	 */
	public int getRecordsFetchedCountAsPrimitive()
	{
		return getRecordsFetchedCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute. 
	 * @param value the recordsFetchedCount - Holds list of record fetched
	 */
	public void setRecordsFetchedCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, RECORDSFETCHEDCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute. 
	 * @param value the recordsFetchedCount - Holds list of record fetched
	 */
	public void setRecordsFetchedCount(final Integer value)
	{
		setRecordsFetchedCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute. 
	 * @param value the recordsFetchedCount - Holds list of record fetched
	 */
	public void setRecordsFetchedCount(final SessionContext ctx, final int value)
	{
		setRecordsFetchedCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrderArchivalResult.recordsFetchedCount</code> attribute. 
	 * @param value the recordsFetchedCount - Holds list of record fetched
	 */
	public void setRecordsFetchedCount(final int value)
	{
		setRecordsFetchedCount( getSession().getSessionContext(), value );
	}
	
}
