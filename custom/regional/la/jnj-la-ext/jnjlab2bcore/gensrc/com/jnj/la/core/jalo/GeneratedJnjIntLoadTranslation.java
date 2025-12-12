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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjIntLoadTranslation}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjIntLoadTranslation extends GenericItem
{
	/** Qualifier of the <code>JnjIntLoadTranslation.customerNumber</code> attribute **/
	public static final String CUSTOMERNUMBER = "customerNumber";
	/** Qualifier of the <code>JnjIntLoadTranslation.jnjProductCode</code> attribute **/
	public static final String JNJPRODUCTCODE = "jnjProductCode";
	/** Qualifier of the <code>JnjIntLoadTranslation.customerProductCode</code> attribute **/
	public static final String CUSTOMERPRODUCTCODE = "customerProductCode";
	/** Qualifier of the <code>JnjIntLoadTranslation.baseUnitMeasure</code> attribute **/
	public static final String BASEUNITMEASURE = "baseUnitMeasure";
	/** Qualifier of the <code>JnjIntLoadTranslation.customerUnitMeasure</code> attribute **/
	public static final String CUSTOMERUNITMEASURE = "customerUnitMeasure";
	/** Qualifier of the <code>JnjIntLoadTranslation.numeratorConversion</code> attribute **/
	public static final String NUMERATORCONVERSION = "numeratorConversion";
	/** Qualifier of the <code>JnjIntLoadTranslation.denominatorConversion</code> attribute **/
	public static final String DENOMINATORCONVERSION = "denominatorConversion";
	/** Qualifier of the <code>JnjIntLoadTranslation.customerDevUom</code> attribute **/
	public static final String CUSTOMERDEVUOM = "customerDevUom";
	/** Qualifier of the <code>JnjIntLoadTranslation.fileName</code> attribute **/
	public static final String FILENAME = "fileName";
	/** Qualifier of the <code>JnjIntLoadTranslation.recordStatus</code> attribute **/
	public static final String RECORDSTATUS = "recordStatus";
	/** Qualifier of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute **/
	public static final String WRITEATTEMPTS = "writeAttempts";
	/** Qualifier of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute **/
	public static final String SENTTODASHBOARD = "sentToDashboard";
	/** Qualifier of the <code>JnjIntLoadTranslation.idocNumber</code> attribute **/
	public static final String IDOCNUMBER = "idocNumber";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CUSTOMERNUMBER, AttributeMode.INITIAL);
		tmp.put(JNJPRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(CUSTOMERPRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(BASEUNITMEASURE, AttributeMode.INITIAL);
		tmp.put(CUSTOMERUNITMEASURE, AttributeMode.INITIAL);
		tmp.put(NUMERATORCONVERSION, AttributeMode.INITIAL);
		tmp.put(DENOMINATORCONVERSION, AttributeMode.INITIAL);
		tmp.put(CUSTOMERDEVUOM, AttributeMode.INITIAL);
		tmp.put(FILENAME, AttributeMode.INITIAL);
		tmp.put(RECORDSTATUS, AttributeMode.INITIAL);
		tmp.put(WRITEATTEMPTS, AttributeMode.INITIAL);
		tmp.put(SENTTODASHBOARD, AttributeMode.INITIAL);
		tmp.put(IDOCNUMBER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.baseUnitMeasure</code> attribute.
	 * @return the baseUnitMeasure
	 */
	public String getBaseUnitMeasure(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BASEUNITMEASURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.baseUnitMeasure</code> attribute.
	 * @return the baseUnitMeasure
	 */
	public String getBaseUnitMeasure()
	{
		return getBaseUnitMeasure( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.baseUnitMeasure</code> attribute. 
	 * @param value the baseUnitMeasure
	 */
	public void setBaseUnitMeasure(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BASEUNITMEASURE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.baseUnitMeasure</code> attribute. 
	 * @param value the baseUnitMeasure
	 */
	public void setBaseUnitMeasure(final String value)
	{
		setBaseUnitMeasure( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerDevUom</code> attribute.
	 * @return the customerDevUom
	 */
	public String getCustomerDevUom(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERDEVUOM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerDevUom</code> attribute.
	 * @return the customerDevUom
	 */
	public String getCustomerDevUom()
	{
		return getCustomerDevUom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerDevUom</code> attribute. 
	 * @param value the customerDevUom
	 */
	public void setCustomerDevUom(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERDEVUOM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerDevUom</code> attribute. 
	 * @param value the customerDevUom
	 */
	public void setCustomerDevUom(final String value)
	{
		setCustomerDevUom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerNumber</code> attribute.
	 * @return the customerNumber
	 */
	public String getCustomerNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerNumber</code> attribute.
	 * @return the customerNumber
	 */
	public String getCustomerNumber()
	{
		return getCustomerNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerNumber</code> attribute. 
	 * @param value the customerNumber
	 */
	public void setCustomerNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerNumber</code> attribute. 
	 * @param value the customerNumber
	 */
	public void setCustomerNumber(final String value)
	{
		setCustomerNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerProductCode</code> attribute.
	 * @return the customerProductCode
	 */
	public String getCustomerProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERPRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerProductCode</code> attribute.
	 * @return the customerProductCode
	 */
	public String getCustomerProductCode()
	{
		return getCustomerProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerProductCode</code> attribute. 
	 * @param value the customerProductCode
	 */
	public void setCustomerProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERPRODUCTCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerProductCode</code> attribute. 
	 * @param value the customerProductCode
	 */
	public void setCustomerProductCode(final String value)
	{
		setCustomerProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerUnitMeasure</code> attribute.
	 * @return the customerUnitMeasure
	 */
	public String getCustomerUnitMeasure(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERUNITMEASURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.customerUnitMeasure</code> attribute.
	 * @return the customerUnitMeasure
	 */
	public String getCustomerUnitMeasure()
	{
		return getCustomerUnitMeasure( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerUnitMeasure</code> attribute. 
	 * @param value the customerUnitMeasure
	 */
	public void setCustomerUnitMeasure(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERUNITMEASURE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.customerUnitMeasure</code> attribute. 
	 * @param value the customerUnitMeasure
	 */
	public void setCustomerUnitMeasure(final String value)
	{
		setCustomerUnitMeasure( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.denominatorConversion</code> attribute.
	 * @return the denominatorConversion
	 */
	public String getDenominatorConversion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DENOMINATORCONVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.denominatorConversion</code> attribute.
	 * @return the denominatorConversion
	 */
	public String getDenominatorConversion()
	{
		return getDenominatorConversion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.denominatorConversion</code> attribute. 
	 * @param value the denominatorConversion
	 */
	public void setDenominatorConversion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DENOMINATORCONVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.denominatorConversion</code> attribute. 
	 * @param value the denominatorConversion
	 */
	public void setDenominatorConversion(final String value)
	{
		setDenominatorConversion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.fileName</code> attribute.
	 * @return the fileName
	 */
	public String getFileName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FILENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.fileName</code> attribute.
	 * @return the fileName
	 */
	public String getFileName()
	{
		return getFileName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.fileName</code> attribute. 
	 * @param value the fileName
	 */
	public void setFileName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FILENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.fileName</code> attribute. 
	 * @param value the fileName
	 */
	public void setFileName(final String value)
	{
		setFileName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.idocNumber</code> attribute.
	 * @return the idocNumber
	 */
	public String getIdocNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IDOCNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.idocNumber</code> attribute.
	 * @return the idocNumber
	 */
	public String getIdocNumber()
	{
		return getIdocNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.idocNumber</code> attribute. 
	 * @param value the idocNumber
	 */
	public void setIdocNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IDOCNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.idocNumber</code> attribute. 
	 * @param value the idocNumber
	 */
	public void setIdocNumber(final String value)
	{
		setIdocNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.jnjProductCode</code> attribute.
	 * @return the jnjProductCode
	 */
	public String getJnjProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, JNJPRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.jnjProductCode</code> attribute.
	 * @return the jnjProductCode
	 */
	public String getJnjProductCode()
	{
		return getJnjProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.jnjProductCode</code> attribute. 
	 * @param value the jnjProductCode
	 */
	public void setJnjProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, JNJPRODUCTCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.jnjProductCode</code> attribute. 
	 * @param value the jnjProductCode
	 */
	public void setJnjProductCode(final String value)
	{
		setJnjProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.numeratorConversion</code> attribute.
	 * @return the numeratorConversion
	 */
	public String getNumeratorConversion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NUMERATORCONVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.numeratorConversion</code> attribute.
	 * @return the numeratorConversion
	 */
	public String getNumeratorConversion()
	{
		return getNumeratorConversion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.numeratorConversion</code> attribute. 
	 * @param value the numeratorConversion
	 */
	public void setNumeratorConversion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NUMERATORCONVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.numeratorConversion</code> attribute. 
	 * @param value the numeratorConversion
	 */
	public void setNumeratorConversion(final String value)
	{
		setNumeratorConversion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.recordStatus</code> attribute.
	 * @return the recordStatus
	 */
	public EnumerationValue getRecordStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, RECORDSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.recordStatus</code> attribute.
	 * @return the recordStatus
	 */
	public EnumerationValue getRecordStatus()
	{
		return getRecordStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.recordStatus</code> attribute. 
	 * @param value the recordStatus
	 */
	public void setRecordStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, RECORDSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.recordStatus</code> attribute. 
	 * @param value the recordStatus
	 */
	public void setRecordStatus(final EnumerationValue value)
	{
		setRecordStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute.
	 * @return the sentToDashboard
	 */
	public Boolean isSentToDashboard(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SENTTODASHBOARD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute.
	 * @return the sentToDashboard
	 */
	public Boolean isSentToDashboard()
	{
		return isSentToDashboard( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute. 
	 * @return the sentToDashboard
	 */
	public boolean isSentToDashboardAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSentToDashboard( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute. 
	 * @return the sentToDashboard
	 */
	public boolean isSentToDashboardAsPrimitive()
	{
		return isSentToDashboardAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute. 
	 * @param value the sentToDashboard
	 */
	public void setSentToDashboard(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SENTTODASHBOARD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute. 
	 * @param value the sentToDashboard
	 */
	public void setSentToDashboard(final Boolean value)
	{
		setSentToDashboard( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute. 
	 * @param value the sentToDashboard
	 */
	public void setSentToDashboard(final SessionContext ctx, final boolean value)
	{
		setSentToDashboard( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.sentToDashboard</code> attribute. 
	 * @param value the sentToDashboard
	 */
	public void setSentToDashboard(final boolean value)
	{
		setSentToDashboard( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute.
	 * @return the writeAttempts
	 */
	public Integer getWriteAttempts(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, WRITEATTEMPTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute.
	 * @return the writeAttempts
	 */
	public Integer getWriteAttempts()
	{
		return getWriteAttempts( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute. 
	 * @return the writeAttempts
	 */
	public int getWriteAttemptsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getWriteAttempts( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute. 
	 * @return the writeAttempts
	 */
	public int getWriteAttemptsAsPrimitive()
	{
		return getWriteAttemptsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute. 
	 * @param value the writeAttempts
	 */
	public void setWriteAttempts(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, WRITEATTEMPTS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute. 
	 * @param value the writeAttempts
	 */
	public void setWriteAttempts(final Integer value)
	{
		setWriteAttempts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute. 
	 * @param value the writeAttempts
	 */
	public void setWriteAttempts(final SessionContext ctx, final int value)
	{
		setWriteAttempts( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIntLoadTranslation.writeAttempts</code> attribute. 
	 * @param value the writeAttempts
	 */
	public void setWriteAttempts(final int value)
	{
		setWriteAttempts( getSession().getSessionContext(), value );
	}
	
}
