/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess JnjLAResetPasswordEmailProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLAResetPasswordEmailProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>JnjLAResetPasswordEmailProcess.jnjLAResetPasswordEmailDetails</code> attribute **/
	public static final String JNJLARESETPASSWORDEMAILDETAILS = "jnjLAResetPasswordEmailDetails";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(JNJLARESETPASSWORDEMAILDETAILS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAResetPasswordEmailProcess.jnjLAResetPasswordEmailDetails</code> attribute.
	 * @return the jnjLAResetPasswordEmailDetails - This attribute contains fields of the reset password requried for the email.
	 */
	public Map<String,String> getAllJnjLAResetPasswordEmailDetails(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, JNJLARESETPASSWORDEMAILDETAILS);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAResetPasswordEmailProcess.jnjLAResetPasswordEmailDetails</code> attribute.
	 * @return the jnjLAResetPasswordEmailDetails - This attribute contains fields of the reset password requried for the email.
	 */
	public Map<String,String> getAllJnjLAResetPasswordEmailDetails()
	{
		return getAllJnjLAResetPasswordEmailDetails( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAResetPasswordEmailProcess.jnjLAResetPasswordEmailDetails</code> attribute. 
	 * @param value the jnjLAResetPasswordEmailDetails - This attribute contains fields of the reset password requried for the email.
	 */
	public void setAllJnjLAResetPasswordEmailDetails(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, JNJLARESETPASSWORDEMAILDETAILS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAResetPasswordEmailProcess.jnjLAResetPasswordEmailDetails</code> attribute. 
	 * @param value the jnjLAResetPasswordEmailDetails - This attribute contains fields of the reset password requried for the email.
	 */
	public void setAllJnjLAResetPasswordEmailDetails(final Map<String,String> value)
	{
		setAllJnjLAResetPasswordEmailDetails( getSession().getSessionContext(), value );
	}
	
}
