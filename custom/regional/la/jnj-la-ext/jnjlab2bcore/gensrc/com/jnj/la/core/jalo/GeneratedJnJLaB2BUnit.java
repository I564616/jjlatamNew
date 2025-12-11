/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 11, 2025, 2:39:23 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2BUnit;
import com.jnj.core.jalo.JnJSalesOrgCustomer;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.jnj.core.jalo.JnJB2BUnit JnJLaB2BUnit}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnJLaB2BUnit extends JnJB2BUnit
{
	/** Qualifier of the <code>JnJLaB2BUnit.type</code> attribute **/
	public static final String TYPE = "type";
	/** Qualifier of the <code>JnJLaB2BUnit.salesOrg</code> attribute **/
	public static final String SALESORG = "salesOrg";
	/** Qualifier of the <code>JnJLaB2BUnit.cnpj</code> attribute **/
	public static final String CNPJ = "cnpj";
	/** Qualifier of the <code>JnJLaB2BUnit.bothIndicator</code> attribute **/
	public static final String BOTHINDICATOR = "bothIndicator";
	/** Qualifier of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute **/
	public static final String PARTIALDELIVFLAG = "partialDelivFlag";
	/** Qualifier of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute **/
	public static final String STDORDERFILEUPLOAD = "stdOrderFileUpload";
	/** Qualifier of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute **/
	public static final String ELIGIBLEFORSUBSTITUTES = "eligibleForSubstitutes";
	/** Qualifier of the <code>JnJLaB2BUnit.industryCode1</code> attribute **/
	public static final String INDUSTRYCODE1 = "industryCode1";
	/** Qualifier of the <code>JnJLaB2BUnit.customerFreightType</code> attribute **/
	public static final String CUSTOMERFREIGHTTYPE = "customerFreightType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(JnJB2BUnit.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(TYPE, AttributeMode.INITIAL);
		tmp.put(SALESORG, AttributeMode.INITIAL);
		tmp.put(CNPJ, AttributeMode.INITIAL);
		tmp.put(BOTHINDICATOR, AttributeMode.INITIAL);
		tmp.put(PARTIALDELIVFLAG, AttributeMode.INITIAL);
		tmp.put(STDORDERFILEUPLOAD, AttributeMode.INITIAL);
		tmp.put(ELIGIBLEFORSUBSTITUTES, AttributeMode.INITIAL);
		tmp.put(INDUSTRYCODE1, AttributeMode.INITIAL);
		tmp.put(CUSTOMERFREIGHTTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.bothIndicator</code> attribute.
	 * @return the bothIndicator
	 */
	public String getBothIndicator(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BOTHINDICATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.bothIndicator</code> attribute.
	 * @return the bothIndicator
	 */
	public String getBothIndicator()
	{
		return getBothIndicator( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.bothIndicator</code> attribute. 
	 * @param value the bothIndicator
	 */
	public void setBothIndicator(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BOTHINDICATOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.bothIndicator</code> attribute. 
	 * @param value the bothIndicator
	 */
	public void setBothIndicator(final String value)
	{
		setBothIndicator( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.cnpj</code> attribute.
	 * @return the cnpj
	 */
	public String getCnpj(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CNPJ);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.cnpj</code> attribute.
	 * @return the cnpj
	 */
	public String getCnpj()
	{
		return getCnpj( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.cnpj</code> attribute. 
	 * @param value the cnpj
	 */
	public void setCnpj(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CNPJ,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.cnpj</code> attribute. 
	 * @param value the cnpj
	 */
	public void setCnpj(final String value)
	{
		setCnpj( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.customerFreightType</code> attribute.
	 * @return the customerFreightType
	 */
	public String getCustomerFreightType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERFREIGHTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.customerFreightType</code> attribute.
	 * @return the customerFreightType
	 */
	public String getCustomerFreightType()
	{
		return getCustomerFreightType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.customerFreightType</code> attribute. 
	 * @param value the customerFreightType
	 */
	public void setCustomerFreightType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERFREIGHTTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.customerFreightType</code> attribute. 
	 * @param value the customerFreightType
	 */
	public void setCustomerFreightType(final String value)
	{
		setCustomerFreightType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute.
	 * @return the eligibleForSubstitutes
	 */
	public Boolean isEligibleForSubstitutes(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ELIGIBLEFORSUBSTITUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute.
	 * @return the eligibleForSubstitutes
	 */
	public Boolean isEligibleForSubstitutes()
	{
		return isEligibleForSubstitutes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute. 
	 * @return the eligibleForSubstitutes
	 */
	public boolean isEligibleForSubstitutesAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEligibleForSubstitutes( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute. 
	 * @return the eligibleForSubstitutes
	 */
	public boolean isEligibleForSubstitutesAsPrimitive()
	{
		return isEligibleForSubstitutesAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute. 
	 * @param value the eligibleForSubstitutes
	 */
	public void setEligibleForSubstitutes(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ELIGIBLEFORSUBSTITUTES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute. 
	 * @param value the eligibleForSubstitutes
	 */
	public void setEligibleForSubstitutes(final Boolean value)
	{
		setEligibleForSubstitutes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute. 
	 * @param value the eligibleForSubstitutes
	 */
	public void setEligibleForSubstitutes(final SessionContext ctx, final boolean value)
	{
		setEligibleForSubstitutes( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.eligibleForSubstitutes</code> attribute. 
	 * @param value the eligibleForSubstitutes
	 */
	public void setEligibleForSubstitutes(final boolean value)
	{
		setEligibleForSubstitutes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.industryCode1</code> attribute.
	 * @return the industryCode1
	 */
	public String getIndustryCode1(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INDUSTRYCODE1);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.industryCode1</code> attribute.
	 * @return the industryCode1
	 */
	public String getIndustryCode1()
	{
		return getIndustryCode1( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.industryCode1</code> attribute. 
	 * @param value the industryCode1
	 */
	public void setIndustryCode1(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INDUSTRYCODE1,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.industryCode1</code> attribute. 
	 * @param value the industryCode1
	 */
	public void setIndustryCode1(final String value)
	{
		setIndustryCode1( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute.
	 * @return the partialDelivFlag
	 */
	public Boolean isPartialDelivFlag(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, PARTIALDELIVFLAG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute.
	 * @return the partialDelivFlag
	 */
	public Boolean isPartialDelivFlag()
	{
		return isPartialDelivFlag( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute. 
	 * @return the partialDelivFlag
	 */
	public boolean isPartialDelivFlagAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isPartialDelivFlag( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute. 
	 * @return the partialDelivFlag
	 */
	public boolean isPartialDelivFlagAsPrimitive()
	{
		return isPartialDelivFlagAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute. 
	 * @param value the partialDelivFlag
	 */
	public void setPartialDelivFlag(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, PARTIALDELIVFLAG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute. 
	 * @param value the partialDelivFlag
	 */
	public void setPartialDelivFlag(final Boolean value)
	{
		setPartialDelivFlag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute. 
	 * @param value the partialDelivFlag
	 */
	public void setPartialDelivFlag(final SessionContext ctx, final boolean value)
	{
		setPartialDelivFlag( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.partialDelivFlag</code> attribute. 
	 * @param value the partialDelivFlag
	 */
	public void setPartialDelivFlag(final boolean value)
	{
		setPartialDelivFlag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.salesOrg</code> attribute.
	 * @return the salesOrg
	 */
	public List<JnJSalesOrgCustomer> getSalesOrg(final SessionContext ctx)
	{
		List<JnJSalesOrgCustomer> coll = (List<JnJSalesOrgCustomer>)getProperty( ctx, SALESORG);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.salesOrg</code> attribute.
	 * @return the salesOrg
	 */
	public List<JnJSalesOrgCustomer> getSalesOrg()
	{
		return getSalesOrg( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.salesOrg</code> attribute. 
	 * @param value the salesOrg
	 */
	public void setSalesOrg(final SessionContext ctx, final List<JnJSalesOrgCustomer> value)
	{
		setProperty(ctx, SALESORG,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.salesOrg</code> attribute. 
	 * @param value the salesOrg
	 */
	public void setSalesOrg(final List<JnJSalesOrgCustomer> value)
	{
		setSalesOrg( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute.
	 * @return the stdOrderFileUpload
	 */
	public Boolean isStdOrderFileUpload(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, STDORDERFILEUPLOAD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute.
	 * @return the stdOrderFileUpload
	 */
	public Boolean isStdOrderFileUpload()
	{
		return isStdOrderFileUpload( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute. 
	 * @return the stdOrderFileUpload
	 */
	public boolean isStdOrderFileUploadAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isStdOrderFileUpload( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute. 
	 * @return the stdOrderFileUpload
	 */
	public boolean isStdOrderFileUploadAsPrimitive()
	{
		return isStdOrderFileUploadAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute. 
	 * @param value the stdOrderFileUpload
	 */
	public void setStdOrderFileUpload(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, STDORDERFILEUPLOAD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute. 
	 * @param value the stdOrderFileUpload
	 */
	public void setStdOrderFileUpload(final Boolean value)
	{
		setStdOrderFileUpload( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute. 
	 * @param value the stdOrderFileUpload
	 */
	public void setStdOrderFileUpload(final SessionContext ctx, final boolean value)
	{
		setStdOrderFileUpload( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.stdOrderFileUpload</code> attribute. 
	 * @param value the stdOrderFileUpload
	 */
	public void setStdOrderFileUpload(final boolean value)
	{
		setStdOrderFileUpload( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.type</code> attribute.
	 * @return the type
	 */
	public String getType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJLaB2BUnit.type</code> attribute.
	 * @return the type
	 */
	public String getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.type</code> attribute. 
	 * @param value the type
	 */
	public void setType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJLaB2BUnit.type</code> attribute. 
	 * @param value the type
	 */
	public void setType(final String value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
}
