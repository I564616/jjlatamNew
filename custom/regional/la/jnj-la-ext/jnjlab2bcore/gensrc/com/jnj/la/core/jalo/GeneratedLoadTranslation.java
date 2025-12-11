/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJProduct;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Unit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem LoadTranslation}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedLoadTranslation extends GenericItem
{
	/** Qualifier of the <code>LoadTranslation.b2bUnit</code> attribute **/
	public static final String B2BUNIT = "b2bUnit";
	/** Qualifier of the <code>LoadTranslation.productId</code> attribute **/
	public static final String PRODUCTID = "productId";
	/** Qualifier of the <code>LoadTranslation.custUOM</code> attribute **/
	public static final String CUSTUOM = "custUOM";
	/** Qualifier of the <code>LoadTranslation.custMaterialNum</code> attribute **/
	public static final String CUSTMATERIALNUM = "custMaterialNum";
	/** Qualifier of the <code>LoadTranslation.baseUOM</code> attribute **/
	public static final String BASEUOM = "baseUOM";
	/** Qualifier of the <code>LoadTranslation.customerUOM</code> attribute **/
	public static final String CUSTOMERUOM = "customerUOM";
	/** Qualifier of the <code>LoadTranslation.denominator</code> attribute **/
	public static final String DENOMINATOR = "denominator";
	/** Qualifier of the <code>LoadTranslation.numerator</code> attribute **/
	public static final String NUMERATOR = "numerator";
	/** Qualifier of the <code>LoadTranslation.custDevUom</code> attribute **/
	public static final String CUSTDEVUOM = "custDevUom";
	/** Qualifier of the <code>LoadTranslation.catalogId</code> attribute **/
	public static final String CATALOGID = "catalogId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(B2BUNIT, AttributeMode.INITIAL);
		tmp.put(PRODUCTID, AttributeMode.INITIAL);
		tmp.put(CUSTUOM, AttributeMode.INITIAL);
		tmp.put(CUSTMATERIALNUM, AttributeMode.INITIAL);
		tmp.put(BASEUOM, AttributeMode.INITIAL);
		tmp.put(CUSTOMERUOM, AttributeMode.INITIAL);
		tmp.put(DENOMINATOR, AttributeMode.INITIAL);
		tmp.put(NUMERATOR, AttributeMode.INITIAL);
		tmp.put(CUSTDEVUOM, AttributeMode.INITIAL);
		tmp.put(CATALOGID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.b2bUnit</code> attribute.
	 * @return the b2bUnit
	 */
	public B2BUnit getB2bUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, B2BUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.b2bUnit</code> attribute.
	 * @return the b2bUnit
	 */
	public B2BUnit getB2bUnit()
	{
		return getB2bUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.b2bUnit</code> attribute. 
	 * @param value the b2bUnit
	 */
	public void setB2bUnit(final SessionContext ctx, final B2BUnit value)
	{
		setProperty(ctx, B2BUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.b2bUnit</code> attribute. 
	 * @param value the b2bUnit
	 */
	public void setB2bUnit(final B2BUnit value)
	{
		setB2bUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.baseUOM</code> attribute.
	 * @return the baseUOM
	 */
	public Unit getBaseUOM(final SessionContext ctx)
	{
		return (Unit)getProperty( ctx, BASEUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.baseUOM</code> attribute.
	 * @return the baseUOM
	 */
	public Unit getBaseUOM()
	{
		return getBaseUOM( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.baseUOM</code> attribute. 
	 * @param value the baseUOM
	 */
	public void setBaseUOM(final SessionContext ctx, final Unit value)
	{
		setProperty(ctx, BASEUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.baseUOM</code> attribute. 
	 * @param value the baseUOM
	 */
	public void setBaseUOM(final Unit value)
	{
		setBaseUOM( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.catalogId</code> attribute.
	 * @return the catalogId
	 */
	public String getCatalogId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CATALOGID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.catalogId</code> attribute.
	 * @return the catalogId
	 */
	public String getCatalogId()
	{
		return getCatalogId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.catalogId</code> attribute. 
	 * @param value the catalogId
	 */
	public void setCatalogId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CATALOGID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.catalogId</code> attribute. 
	 * @param value the catalogId
	 */
	public void setCatalogId(final String value)
	{
		setCatalogId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.custDevUom</code> attribute.
	 * @return the custDevUom
	 */
	public Unit getCustDevUom(final SessionContext ctx)
	{
		return (Unit)getProperty( ctx, CUSTDEVUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.custDevUom</code> attribute.
	 * @return the custDevUom
	 */
	public Unit getCustDevUom()
	{
		return getCustDevUom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.custDevUom</code> attribute. 
	 * @param value the custDevUom
	 */
	public void setCustDevUom(final SessionContext ctx, final Unit value)
	{
		setProperty(ctx, CUSTDEVUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.custDevUom</code> attribute. 
	 * @param value the custDevUom
	 */
	public void setCustDevUom(final Unit value)
	{
		setCustDevUom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.custMaterialNum</code> attribute.
	 * @return the custMaterialNum
	 */
	public String getCustMaterialNum(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTMATERIALNUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.custMaterialNum</code> attribute.
	 * @return the custMaterialNum
	 */
	public String getCustMaterialNum()
	{
		return getCustMaterialNum( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.custMaterialNum</code> attribute. 
	 * @param value the custMaterialNum
	 */
	public void setCustMaterialNum(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTMATERIALNUM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.custMaterialNum</code> attribute. 
	 * @param value the custMaterialNum
	 */
	public void setCustMaterialNum(final String value)
	{
		setCustMaterialNum( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.customerUOM</code> attribute.
	 * @return the customerUOM
	 */
	public String getCustomerUOM(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.customerUOM</code> attribute.
	 * @return the customerUOM
	 */
	public String getCustomerUOM()
	{
		return getCustomerUOM( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.customerUOM</code> attribute. 
	 * @param value the customerUOM
	 */
	public void setCustomerUOM(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.customerUOM</code> attribute. 
	 * @param value the customerUOM
	 */
	public void setCustomerUOM(final String value)
	{
		setCustomerUOM( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.custUOM</code> attribute.
	 * @return the custUOM
	 */
	public Unit getCustUOM(final SessionContext ctx)
	{
		return (Unit)getProperty( ctx, CUSTUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.custUOM</code> attribute.
	 * @return the custUOM
	 */
	public Unit getCustUOM()
	{
		return getCustUOM( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.custUOM</code> attribute. 
	 * @param value the custUOM
	 */
	public void setCustUOM(final SessionContext ctx, final Unit value)
	{
		setProperty(ctx, CUSTUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.custUOM</code> attribute. 
	 * @param value the custUOM
	 */
	public void setCustUOM(final Unit value)
	{
		setCustUOM( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.denominator</code> attribute.
	 * @return the denominator
	 */
	public Integer getDenominator(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, DENOMINATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.denominator</code> attribute.
	 * @return the denominator
	 */
	public Integer getDenominator()
	{
		return getDenominator( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.denominator</code> attribute. 
	 * @return the denominator
	 */
	public int getDenominatorAsPrimitive(final SessionContext ctx)
	{
		Integer value = getDenominator( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.denominator</code> attribute. 
	 * @return the denominator
	 */
	public int getDenominatorAsPrimitive()
	{
		return getDenominatorAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.denominator</code> attribute. 
	 * @param value the denominator
	 */
	public void setDenominator(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, DENOMINATOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.denominator</code> attribute. 
	 * @param value the denominator
	 */
	public void setDenominator(final Integer value)
	{
		setDenominator( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.denominator</code> attribute. 
	 * @param value the denominator
	 */
	public void setDenominator(final SessionContext ctx, final int value)
	{
		setDenominator( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.denominator</code> attribute. 
	 * @param value the denominator
	 */
	public void setDenominator(final int value)
	{
		setDenominator( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.numerator</code> attribute.
	 * @return the numerator
	 */
	public Integer getNumerator(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, NUMERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.numerator</code> attribute.
	 * @return the numerator
	 */
	public Integer getNumerator()
	{
		return getNumerator( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.numerator</code> attribute. 
	 * @return the numerator
	 */
	public int getNumeratorAsPrimitive(final SessionContext ctx)
	{
		Integer value = getNumerator( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.numerator</code> attribute. 
	 * @return the numerator
	 */
	public int getNumeratorAsPrimitive()
	{
		return getNumeratorAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.numerator</code> attribute. 
	 * @param value the numerator
	 */
	public void setNumerator(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, NUMERATOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.numerator</code> attribute. 
	 * @param value the numerator
	 */
	public void setNumerator(final Integer value)
	{
		setNumerator( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.numerator</code> attribute. 
	 * @param value the numerator
	 */
	public void setNumerator(final SessionContext ctx, final int value)
	{
		setNumerator( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.numerator</code> attribute. 
	 * @param value the numerator
	 */
	public void setNumerator(final int value)
	{
		setNumerator( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.productId</code> attribute.
	 * @return the productId
	 */
	public JnJProduct getProductId(final SessionContext ctx)
	{
		return (JnJProduct)getProperty( ctx, PRODUCTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LoadTranslation.productId</code> attribute.
	 * @return the productId
	 */
	public JnJProduct getProductId()
	{
		return getProductId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.productId</code> attribute. 
	 * @param value the productId
	 */
	public void setProductId(final SessionContext ctx, final JnJProduct value)
	{
		setProperty(ctx, PRODUCTID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LoadTranslation.productId</code> attribute. 
	 * @param value the productId
	 */
	public void setProductId(final JnJProduct value)
	{
		setProductId( getSession().getSessionContext(), value );
	}
	
}
