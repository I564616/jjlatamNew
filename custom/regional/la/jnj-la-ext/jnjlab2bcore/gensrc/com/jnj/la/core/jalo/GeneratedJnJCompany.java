/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.catalog.jalo.Company;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.catalog.jalo.Company JnJCompany}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnJCompany extends Company
{
	/** Qualifier of the <code>JnJCompany.masterCompany</code> attribute **/
	public static final String MASTERCOMPANY = "masterCompany";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Company.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(MASTERCOMPANY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJCompany.masterCompany</code> attribute.
	 * @return the masterCompany
	 */
	public Boolean isMasterCompany(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, MASTERCOMPANY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJCompany.masterCompany</code> attribute.
	 * @return the masterCompany
	 */
	public Boolean isMasterCompany()
	{
		return isMasterCompany( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJCompany.masterCompany</code> attribute. 
	 * @return the masterCompany
	 */
	public boolean isMasterCompanyAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isMasterCompany( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJCompany.masterCompany</code> attribute. 
	 * @return the masterCompany
	 */
	public boolean isMasterCompanyAsPrimitive()
	{
		return isMasterCompanyAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJCompany.masterCompany</code> attribute. 
	 * @param value the masterCompany
	 */
	public void setMasterCompany(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, MASTERCOMPANY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJCompany.masterCompany</code> attribute. 
	 * @param value the masterCompany
	 */
	public void setMasterCompany(final Boolean value)
	{
		setMasterCompany( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJCompany.masterCompany</code> attribute. 
	 * @param value the masterCompany
	 */
	public void setMasterCompany(final SessionContext ctx, final boolean value)
	{
		setMasterCompany( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJCompany.masterCompany</code> attribute. 
	 * @param value the masterCompany
	 */
	public void setMasterCompany(final boolean value)
	{
		setMasterCompany( getSession().getSessionContext(), value );
	}
	
}
