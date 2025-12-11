/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Unit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnJProductSalesOrg}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnJProductSalesOrg extends GenericItem
{
	/** Qualifier of the <code>JnJProductSalesOrg.numeratorSUOM</code> attribute **/
	public static final String NUMERATORSUOM = "numeratorSUOM";
	/** Qualifier of the <code>JnJProductSalesOrg.numeratorDUOM</code> attribute **/
	public static final String NUMERATORDUOM = "numeratorDUOM";
	/** Qualifier of the <code>JnJProductSalesOrg.salesOrg</code> attribute **/
	public static final String SALESORG = "salesOrg";
	/** Qualifier of the <code>JnJProductSalesOrg.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>JnJProductSalesOrg.disContinue</code> attribute **/
	public static final String DISCONTINUE = "disContinue";
	/** Qualifier of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute **/
	public static final String ECOMMERCEFLAG = "ecommerceFlag";
	/** Qualifier of the <code>JnJProductSalesOrg.salesUnitOfMeasure</code> attribute **/
	public static final String SALESUNITOFMEASURE = "salesUnitOfMeasure";
	/** Qualifier of the <code>JnJProductSalesOrg.deliveryUnitOfMeasure</code> attribute **/
	public static final String DELIVERYUNITOFMEASURE = "deliveryUnitOfMeasure";
	/** Qualifier of the <code>JnJProductSalesOrg.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute **/
	public static final String COLDCHAINPRODUCT = "coldChainProduct";
	/** Qualifier of the <code>JnJProductSalesOrg.active</code> attribute **/
	public static final String ACTIVE = "active";
	/** Qualifier of the <code>JnJProductSalesOrg.offlineDate</code> attribute **/
	public static final String OFFLINEDATE = "offlineDate";
	/** Qualifier of the <code>JnJProductSalesOrg.sector</code> attribute **/
	public static final String SECTOR = "sector";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(NUMERATORSUOM, AttributeMode.INITIAL);
		tmp.put(NUMERATORDUOM, AttributeMode.INITIAL);
		tmp.put(SALESORG, AttributeMode.INITIAL);
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(DISCONTINUE, AttributeMode.INITIAL);
		tmp.put(ECOMMERCEFLAG, AttributeMode.INITIAL);
		tmp.put(SALESUNITOFMEASURE, AttributeMode.INITIAL);
		tmp.put(DELIVERYUNITOFMEASURE, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(COLDCHAINPRODUCT, AttributeMode.INITIAL);
		tmp.put(ACTIVE, AttributeMode.INITIAL);
		tmp.put(OFFLINEDATE, AttributeMode.INITIAL);
		tmp.put(SECTOR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive()
	{
		return isActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive()
	{
		return isActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final Boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final boolean value)
	{
		setActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute.
	 * @return the coldChainProduct
	 */
	public Boolean isColdChainProduct(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, COLDCHAINPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute.
	 * @return the coldChainProduct
	 */
	public Boolean isColdChainProduct()
	{
		return isColdChainProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute. 
	 * @return the coldChainProduct
	 */
	public boolean isColdChainProductAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isColdChainProduct( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute. 
	 * @return the coldChainProduct
	 */
	public boolean isColdChainProductAsPrimitive()
	{
		return isColdChainProductAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute. 
	 * @param value the coldChainProduct
	 */
	public void setColdChainProduct(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, COLDCHAINPRODUCT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute. 
	 * @param value the coldChainProduct
	 */
	public void setColdChainProduct(final Boolean value)
	{
		setColdChainProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute. 
	 * @param value the coldChainProduct
	 */
	public void setColdChainProduct(final SessionContext ctx, final boolean value)
	{
		setColdChainProduct( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.coldChainProduct</code> attribute. 
	 * @param value the coldChainProduct
	 */
	public void setColdChainProduct(final boolean value)
	{
		setColdChainProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.deliveryUnitOfMeasure</code> attribute.
	 * @return the deliveryUnitOfMeasure
	 */
	public Unit getDeliveryUnitOfMeasure(final SessionContext ctx)
	{
		return (Unit)getProperty( ctx, DELIVERYUNITOFMEASURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.deliveryUnitOfMeasure</code> attribute.
	 * @return the deliveryUnitOfMeasure
	 */
	public Unit getDeliveryUnitOfMeasure()
	{
		return getDeliveryUnitOfMeasure( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.deliveryUnitOfMeasure</code> attribute. 
	 * @param value the deliveryUnitOfMeasure
	 */
	public void setDeliveryUnitOfMeasure(final SessionContext ctx, final Unit value)
	{
		setProperty(ctx, DELIVERYUNITOFMEASURE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.deliveryUnitOfMeasure</code> attribute. 
	 * @param value the deliveryUnitOfMeasure
	 */
	public void setDeliveryUnitOfMeasure(final Unit value)
	{
		setDeliveryUnitOfMeasure( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.disContinue</code> attribute.
	 * @return the disContinue
	 */
	public Boolean isDisContinue(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, DISCONTINUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.disContinue</code> attribute.
	 * @return the disContinue
	 */
	public Boolean isDisContinue()
	{
		return isDisContinue( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.disContinue</code> attribute. 
	 * @return the disContinue
	 */
	public boolean isDisContinueAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isDisContinue( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.disContinue</code> attribute. 
	 * @return the disContinue
	 */
	public boolean isDisContinueAsPrimitive()
	{
		return isDisContinueAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.disContinue</code> attribute. 
	 * @param value the disContinue
	 */
	public void setDisContinue(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, DISCONTINUE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.disContinue</code> attribute. 
	 * @param value the disContinue
	 */
	public void setDisContinue(final Boolean value)
	{
		setDisContinue( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.disContinue</code> attribute. 
	 * @param value the disContinue
	 */
	public void setDisContinue(final SessionContext ctx, final boolean value)
	{
		setDisContinue( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.disContinue</code> attribute. 
	 * @param value the disContinue
	 */
	public void setDisContinue(final boolean value)
	{
		setDisContinue( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute.
	 * @return the ecommerceFlag
	 */
	public Boolean isEcommerceFlag(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ECOMMERCEFLAG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute.
	 * @return the ecommerceFlag
	 */
	public Boolean isEcommerceFlag()
	{
		return isEcommerceFlag( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute. 
	 * @return the ecommerceFlag
	 */
	public boolean isEcommerceFlagAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEcommerceFlag( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute. 
	 * @return the ecommerceFlag
	 */
	public boolean isEcommerceFlagAsPrimitive()
	{
		return isEcommerceFlagAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute. 
	 * @param value the ecommerceFlag
	 */
	public void setEcommerceFlag(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ECOMMERCEFLAG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute. 
	 * @param value the ecommerceFlag
	 */
	public void setEcommerceFlag(final Boolean value)
	{
		setEcommerceFlag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute. 
	 * @param value the ecommerceFlag
	 */
	public void setEcommerceFlag(final SessionContext ctx, final boolean value)
	{
		setEcommerceFlag( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.ecommerceFlag</code> attribute. 
	 * @param value the ecommerceFlag
	 */
	public void setEcommerceFlag(final boolean value)
	{
		setEcommerceFlag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.numeratorDUOM</code> attribute.
	 * @return the numeratorDUOM
	 */
	public String getNumeratorDUOM(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NUMERATORDUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.numeratorDUOM</code> attribute.
	 * @return the numeratorDUOM
	 */
	public String getNumeratorDUOM()
	{
		return getNumeratorDUOM( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.numeratorDUOM</code> attribute. 
	 * @param value the numeratorDUOM
	 */
	public void setNumeratorDUOM(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NUMERATORDUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.numeratorDUOM</code> attribute. 
	 * @param value the numeratorDUOM
	 */
	public void setNumeratorDUOM(final String value)
	{
		setNumeratorDUOM( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.numeratorSUOM</code> attribute.
	 * @return the numeratorSUOM
	 */
	public String getNumeratorSUOM(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NUMERATORSUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.numeratorSUOM</code> attribute.
	 * @return the numeratorSUOM
	 */
	public String getNumeratorSUOM()
	{
		return getNumeratorSUOM( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.numeratorSUOM</code> attribute. 
	 * @param value the numeratorSUOM
	 */
	public void setNumeratorSUOM(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NUMERATORSUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.numeratorSUOM</code> attribute. 
	 * @param value the numeratorSUOM
	 */
	public void setNumeratorSUOM(final String value)
	{
		setNumeratorSUOM( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.offlineDate</code> attribute.
	 * @return the offlineDate
	 */
	public Date getOfflineDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, OFFLINEDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.offlineDate</code> attribute.
	 * @return the offlineDate
	 */
	public Date getOfflineDate()
	{
		return getOfflineDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.offlineDate</code> attribute. 
	 * @param value the offlineDate
	 */
	public void setOfflineDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, OFFLINEDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.offlineDate</code> attribute. 
	 * @param value the offlineDate
	 */
	public void setOfflineDate(final Date value)
	{
		setOfflineDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRODUCTCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.salesOrg</code> attribute.
	 * @return the salesOrg
	 */
	public String getSalesOrg(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SALESORG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.salesOrg</code> attribute.
	 * @return the salesOrg
	 */
	public String getSalesOrg()
	{
		return getSalesOrg( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.salesOrg</code> attribute. 
	 * @param value the salesOrg
	 */
	public void setSalesOrg(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SALESORG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.salesOrg</code> attribute. 
	 * @param value the salesOrg
	 */
	public void setSalesOrg(final String value)
	{
		setSalesOrg( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.salesUnitOfMeasure</code> attribute.
	 * @return the salesUnitOfMeasure
	 */
	public Unit getSalesUnitOfMeasure(final SessionContext ctx)
	{
		return (Unit)getProperty( ctx, SALESUNITOFMEASURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.salesUnitOfMeasure</code> attribute.
	 * @return the salesUnitOfMeasure
	 */
	public Unit getSalesUnitOfMeasure()
	{
		return getSalesUnitOfMeasure( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.salesUnitOfMeasure</code> attribute. 
	 * @param value the salesUnitOfMeasure
	 */
	public void setSalesUnitOfMeasure(final SessionContext ctx, final Unit value)
	{
		setProperty(ctx, SALESUNITOFMEASURE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.salesUnitOfMeasure</code> attribute. 
	 * @param value the salesUnitOfMeasure
	 */
	public void setSalesUnitOfMeasure(final Unit value)
	{
		setSalesUnitOfMeasure( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.sector</code> attribute.
	 * @return the sector
	 */
	public String getSector(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SECTOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.sector</code> attribute.
	 * @return the sector
	 */
	public String getSector()
	{
		return getSector( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.sector</code> attribute. 
	 * @param value the sector
	 */
	public void setSector(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SECTOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.sector</code> attribute. 
	 * @param value the sector
	 */
	public void setSector(final String value)
	{
		setSector( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.status</code> attribute.
	 * @return the status
	 */
	public String getStatus(final SessionContext ctx)
	{
		return (String)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJProductSalesOrg.status</code> attribute.
	 * @return the status
	 */
	public String getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final String value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJProductSalesOrg.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final String value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
}
