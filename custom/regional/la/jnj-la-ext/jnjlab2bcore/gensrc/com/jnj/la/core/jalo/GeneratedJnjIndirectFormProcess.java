/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjIndirectFormProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjIndirectFormProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjIndirectFormProcess.jnjCustomerFormMap</code> attribute **/
	public static final String JNJCUSTOMERFORMMAP = "jnjCustomerFormMap";
	/** Qualifier of the <code>JnjIndirectFormProcess.jnjCustomerDetails</code> attribute **/
	public static final String JNJCUSTOMERDETAILS = "jnjCustomerDetails";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(JNJCUSTOMERFORMMAP, AttributeMode.INITIAL);
		tmp.put(JNJCUSTOMERDETAILS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectFormProcess.jnjCustomerDetails</code> attribute.
	 * @return the jnjCustomerDetails - Attribute contains attributes of the customer Form that is used in this process.
	 */
	public Map<String,List<String>> getAllJnjCustomerDetails(final SessionContext ctx)
	{
		Map<String,List<String>> map = (Map<String,List<String>>)getProperty( ctx, JNJCUSTOMERDETAILS);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectFormProcess.jnjCustomerDetails</code> attribute.
	 * @return the jnjCustomerDetails - Attribute contains attributes of the customer Form that is used in this process.
	 */
	public Map<String,List<String>> getAllJnjCustomerDetails()
	{
		return getAllJnjCustomerDetails( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectFormProcess.jnjCustomerDetails</code> attribute. 
	 * @param value the jnjCustomerDetails - Attribute contains attributes of the customer Form that is used in this process.
	 */
	public void setAllJnjCustomerDetails(final SessionContext ctx, final Map<String,List<String>> value)
	{
		setProperty(ctx, JNJCUSTOMERDETAILS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectFormProcess.jnjCustomerDetails</code> attribute. 
	 * @param value the jnjCustomerDetails - Attribute contains attributes of the customer Form that is used in this process.
	 */
	public void setAllJnjCustomerDetails(final Map<String,List<String>> value)
	{
		setAllJnjCustomerDetails( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectFormProcess.jnjCustomerFormMap</code> attribute.
	 * @return the jnjCustomerFormMap - Attribute contains attributes of the Customer Form that is used in this process.
	 */
	public Map<String,Object> getAllJnjCustomerFormMap(final SessionContext ctx)
	{
		Map<String,Object> map = (Map<String,Object>)getProperty( ctx, JNJCUSTOMERFORMMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjIndirectFormProcess.jnjCustomerFormMap</code> attribute.
	 * @return the jnjCustomerFormMap - Attribute contains attributes of the Customer Form that is used in this process.
	 */
	public Map<String,Object> getAllJnjCustomerFormMap()
	{
		return getAllJnjCustomerFormMap( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectFormProcess.jnjCustomerFormMap</code> attribute. 
	 * @param value the jnjCustomerFormMap - Attribute contains attributes of the Customer Form that is used in this process.
	 */
	public void setAllJnjCustomerFormMap(final SessionContext ctx, final Map<String,Object> value)
	{
		setProperty(ctx, JNJCUSTOMERFORMMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjIndirectFormProcess.jnjCustomerFormMap</code> attribute. 
	 * @param value the jnjCustomerFormMap - Attribute contains attributes of the Customer Form that is used in this process.
	 */
	public void setAllJnjCustomerFormMap(final Map<String,Object> value)
	{
		setAllJnjCustomerFormMap( getSession().getSessionContext(), value );
	}
	
}
