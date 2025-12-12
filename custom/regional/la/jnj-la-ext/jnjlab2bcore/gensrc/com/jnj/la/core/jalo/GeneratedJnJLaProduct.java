/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJProduct;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.jalo.JnJProductSalesOrg;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.PartOfHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.product.Product JnJLaProduct}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnJLaProduct extends JnJProduct
{
	/** Qualifier of the <code>JnJLaProduct.salesOrgList</code> attribute **/
	public static final String SALESORGLIST = "salesOrgList";
	/** Qualifier of the <code>JnJLaProduct.division</code> attribute **/
	public static final String DIVISION = "division";
	/** Qualifier of the <code>JnJLaProduct.productSalesOrgModifiedTime</code> attribute **/
	public static final String PRODUCTSALESORGMODIFIEDTIME = "productSalesOrgModifiedTime";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(JnJProduct.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SALESORGLIST, AttributeMode.INITIAL);
		tmp.put(DIVISION, AttributeMode.INITIAL);
		tmp.put(PRODUCTSALESORGMODIFIEDTIME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaProduct.division</code> attribute.
	 * @return the division
	 */
	public String getDivision(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DIVISION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaProduct.division</code> attribute.
	 * @return the division
	 */
	public String getDivision()
	{
		return getDivision( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaProduct.division</code> attribute. 
	 * @param value the division
	 */
	public void setDivision(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DIVISION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaProduct.division</code> attribute. 
	 * @param value the division
	 */
	public void setDivision(final String value)
	{
		setDivision( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaProduct.productSalesOrgModifiedTime</code> attribute.
	 * @return the productSalesOrgModifiedTime - This is the modified time of the JnjproductSalesOrg associated with this Product. Added to enable force sync on Product when
	 *                         ProductSalesOrg are updated.
	 */
	public Date getProductSalesOrgModifiedTime(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, PRODUCTSALESORGMODIFIEDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaProduct.productSalesOrgModifiedTime</code> attribute.
	 * @return the productSalesOrgModifiedTime - This is the modified time of the JnjproductSalesOrg associated with this Product. Added to enable force sync on Product when
	 *                         ProductSalesOrg are updated.
	 */
	public Date getProductSalesOrgModifiedTime()
	{
		return getProductSalesOrgModifiedTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaProduct.productSalesOrgModifiedTime</code> attribute. 
	 * @param value the productSalesOrgModifiedTime - This is the modified time of the JnjproductSalesOrg associated with this Product. Added to enable force sync on Product when
	 *                         ProductSalesOrg are updated.
	 */
	public void setProductSalesOrgModifiedTime(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, PRODUCTSALESORGMODIFIEDTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaProduct.productSalesOrgModifiedTime</code> attribute. 
	 * @param value the productSalesOrgModifiedTime - This is the modified time of the JnjproductSalesOrg associated with this Product. Added to enable force sync on Product when
	 *                         ProductSalesOrg are updated.
	 */
	public void setProductSalesOrgModifiedTime(final Date value)
	{
		setProductSalesOrgModifiedTime( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaProduct.salesOrgList</code> attribute.
	 * @return the salesOrgList
	 */
	public List<JnJProductSalesOrg> getSalesOrgList(final SessionContext ctx)
	{
		List<JnJProductSalesOrg> coll = (List<JnJProductSalesOrg>)getProperty( ctx, SALESORGLIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaProduct.salesOrgList</code> attribute.
	 * @return the salesOrgList
	 */
	public List<JnJProductSalesOrg> getSalesOrgList()
	{
		return getSalesOrgList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaProduct.salesOrgList</code> attribute. 
	 * @param value the salesOrgList
	 */
	public void setSalesOrgList(final SessionContext ctx, final List<JnJProductSalesOrg> value)
	{
		new PartOfHandler<List<JnJProductSalesOrg>>()
		{
			@Override
			protected List<JnJProductSalesOrg> doGetValue(final SessionContext ctx)
			{
				return getSalesOrgList( ctx );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final List<JnJProductSalesOrg> _value)
			{
				final List<JnJProductSalesOrg> value = _value;
				setProperty(ctx, SALESORGLIST,value == null || !value.isEmpty() ? value : null );
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaProduct.salesOrgList</code> attribute. 
	 * @param value the salesOrgList
	 */
	public void setSalesOrgList(final List<JnJProductSalesOrg> value)
	{
		setSalesOrgList( getSession().getSessionContext(), value );
	}
	
}
