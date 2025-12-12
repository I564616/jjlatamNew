/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 10:30:00 AM                   ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob JnjCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjCronJob extends CronJob
{
	/** Qualifier of the <code>JnjCronJob.targetCatalog</code> attribute **/
	public static final String TARGETCATALOG = "targetCatalog";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(TARGETCATALOG, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCronJob.targetCatalog</code> attribute.
	 * @return the targetCatalog - Holds the enum, representing the catalog name on which Hide Category needs to be Processed.
	 */
	public EnumerationValue getTargetCatalog(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TARGETCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjCronJob.targetCatalog</code> attribute.
	 * @return the targetCatalog - Holds the enum, representing the catalog name on which Hide Category needs to be Processed.
	 */
	public EnumerationValue getTargetCatalog()
	{
		return getTargetCatalog( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCronJob.targetCatalog</code> attribute. 
	 * @param value the targetCatalog - Holds the enum, representing the catalog name on which Hide Category needs to be Processed.
	 */
	public void setTargetCatalog(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TARGETCATALOG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjCronJob.targetCatalog</code> attribute. 
	 * @param value the targetCatalog - Holds the enum, representing the catalog name on which Hide Category needs to be Processed.
	 */
	public void setTargetCatalog(final EnumerationValue value)
	{
		setTargetCatalog( getSession().getSessionContext(), value );
	}
	
}
