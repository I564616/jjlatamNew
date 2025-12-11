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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjLAOrderPermutationMatrix}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjLAOrderPermutationMatrix extends GenericItem
{
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute **/
	public static final String HASCREATED = "hasCreated";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute **/
	public static final String HASUNDERANALYSIS = "hasUnderAnalysis";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute **/
	public static final String HASBEINGPROCESSED = "hasBeingProcessed";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute **/
	public static final String HASINPICKING = "hasInPicking";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute **/
	public static final String HASPARTIALLYINVOICED = "hasPartiallyInvoiced";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute **/
	public static final String HASINVOICED = "hasInvoiced";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute **/
	public static final String HASPARTIALLYSHIPPED = "hasPartiallyShipped";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute **/
	public static final String HASSHIPPED = "hasShipped";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute **/
	public static final String HASPARTIALLYDELIVERED = "hasPartiallyDelivered";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute **/
	public static final String HASDELIVERED = "hasDelivered";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute **/
	public static final String HASBACKORDERED = "hasBackordered";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute **/
	public static final String HASCANCELLED = "hasCancelled";
	/** Qualifier of the <code>JnjLAOrderPermutationMatrix.status</code> attribute **/
	public static final String STATUS = "status";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(HASCREATED, AttributeMode.INITIAL);
		tmp.put(HASUNDERANALYSIS, AttributeMode.INITIAL);
		tmp.put(HASBEINGPROCESSED, AttributeMode.INITIAL);
		tmp.put(HASINPICKING, AttributeMode.INITIAL);
		tmp.put(HASPARTIALLYINVOICED, AttributeMode.INITIAL);
		tmp.put(HASINVOICED, AttributeMode.INITIAL);
		tmp.put(HASPARTIALLYSHIPPED, AttributeMode.INITIAL);
		tmp.put(HASSHIPPED, AttributeMode.INITIAL);
		tmp.put(HASPARTIALLYDELIVERED, AttributeMode.INITIAL);
		tmp.put(HASDELIVERED, AttributeMode.INITIAL);
		tmp.put(HASBACKORDERED, AttributeMode.INITIAL);
		tmp.put(HASCANCELLED, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute.
	 * @return the hasBackordered
	 */
	public Boolean isHasBackordered(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASBACKORDERED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute.
	 * @return the hasBackordered
	 */
	public Boolean isHasBackordered()
	{
		return isHasBackordered( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute. 
	 * @return the hasBackordered
	 */
	public boolean isHasBackorderedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasBackordered( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute. 
	 * @return the hasBackordered
	 */
	public boolean isHasBackorderedAsPrimitive()
	{
		return isHasBackorderedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute. 
	 * @param value the hasBackordered
	 */
	public void setHasBackordered(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASBACKORDERED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute. 
	 * @param value the hasBackordered
	 */
	public void setHasBackordered(final Boolean value)
	{
		setHasBackordered( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute. 
	 * @param value the hasBackordered
	 */
	public void setHasBackordered(final SessionContext ctx, final boolean value)
	{
		setHasBackordered( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBackordered</code> attribute. 
	 * @param value the hasBackordered
	 */
	public void setHasBackordered(final boolean value)
	{
		setHasBackordered( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute.
	 * @return the hasBeingProcessed
	 */
	public Boolean isHasBeingProcessed(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASBEINGPROCESSED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute.
	 * @return the hasBeingProcessed
	 */
	public Boolean isHasBeingProcessed()
	{
		return isHasBeingProcessed( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute. 
	 * @return the hasBeingProcessed
	 */
	public boolean isHasBeingProcessedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasBeingProcessed( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute. 
	 * @return the hasBeingProcessed
	 */
	public boolean isHasBeingProcessedAsPrimitive()
	{
		return isHasBeingProcessedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute. 
	 * @param value the hasBeingProcessed
	 */
	public void setHasBeingProcessed(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASBEINGPROCESSED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute. 
	 * @param value the hasBeingProcessed
	 */
	public void setHasBeingProcessed(final Boolean value)
	{
		setHasBeingProcessed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute. 
	 * @param value the hasBeingProcessed
	 */
	public void setHasBeingProcessed(final SessionContext ctx, final boolean value)
	{
		setHasBeingProcessed( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasBeingProcessed</code> attribute. 
	 * @param value the hasBeingProcessed
	 */
	public void setHasBeingProcessed(final boolean value)
	{
		setHasBeingProcessed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute.
	 * @return the hasCancelled
	 */
	public Boolean isHasCancelled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASCANCELLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute.
	 * @return the hasCancelled
	 */
	public Boolean isHasCancelled()
	{
		return isHasCancelled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute. 
	 * @return the hasCancelled
	 */
	public boolean isHasCancelledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasCancelled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute. 
	 * @return the hasCancelled
	 */
	public boolean isHasCancelledAsPrimitive()
	{
		return isHasCancelledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute. 
	 * @param value the hasCancelled
	 */
	public void setHasCancelled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASCANCELLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute. 
	 * @param value the hasCancelled
	 */
	public void setHasCancelled(final Boolean value)
	{
		setHasCancelled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute. 
	 * @param value the hasCancelled
	 */
	public void setHasCancelled(final SessionContext ctx, final boolean value)
	{
		setHasCancelled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCancelled</code> attribute. 
	 * @param value the hasCancelled
	 */
	public void setHasCancelled(final boolean value)
	{
		setHasCancelled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute.
	 * @return the hasCreated
	 */
	public Boolean isHasCreated(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASCREATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute.
	 * @return the hasCreated
	 */
	public Boolean isHasCreated()
	{
		return isHasCreated( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute. 
	 * @return the hasCreated
	 */
	public boolean isHasCreatedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasCreated( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute. 
	 * @return the hasCreated
	 */
	public boolean isHasCreatedAsPrimitive()
	{
		return isHasCreatedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute. 
	 * @param value the hasCreated
	 */
	public void setHasCreated(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASCREATED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute. 
	 * @param value the hasCreated
	 */
	public void setHasCreated(final Boolean value)
	{
		setHasCreated( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute. 
	 * @param value the hasCreated
	 */
	public void setHasCreated(final SessionContext ctx, final boolean value)
	{
		setHasCreated( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasCreated</code> attribute. 
	 * @param value the hasCreated
	 */
	public void setHasCreated(final boolean value)
	{
		setHasCreated( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute.
	 * @return the hasDelivered
	 */
	public Boolean isHasDelivered(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASDELIVERED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute.
	 * @return the hasDelivered
	 */
	public Boolean isHasDelivered()
	{
		return isHasDelivered( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute. 
	 * @return the hasDelivered
	 */
	public boolean isHasDeliveredAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasDelivered( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute. 
	 * @return the hasDelivered
	 */
	public boolean isHasDeliveredAsPrimitive()
	{
		return isHasDeliveredAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute. 
	 * @param value the hasDelivered
	 */
	public void setHasDelivered(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASDELIVERED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute. 
	 * @param value the hasDelivered
	 */
	public void setHasDelivered(final Boolean value)
	{
		setHasDelivered( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute. 
	 * @param value the hasDelivered
	 */
	public void setHasDelivered(final SessionContext ctx, final boolean value)
	{
		setHasDelivered( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasDelivered</code> attribute. 
	 * @param value the hasDelivered
	 */
	public void setHasDelivered(final boolean value)
	{
		setHasDelivered( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute.
	 * @return the hasInPicking
	 */
	public Boolean isHasInPicking(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASINPICKING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute.
	 * @return the hasInPicking
	 */
	public Boolean isHasInPicking()
	{
		return isHasInPicking( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute. 
	 * @return the hasInPicking
	 */
	public boolean isHasInPickingAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasInPicking( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute. 
	 * @return the hasInPicking
	 */
	public boolean isHasInPickingAsPrimitive()
	{
		return isHasInPickingAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute. 
	 * @param value the hasInPicking
	 */
	public void setHasInPicking(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASINPICKING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute. 
	 * @param value the hasInPicking
	 */
	public void setHasInPicking(final Boolean value)
	{
		setHasInPicking( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute. 
	 * @param value the hasInPicking
	 */
	public void setHasInPicking(final SessionContext ctx, final boolean value)
	{
		setHasInPicking( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInPicking</code> attribute. 
	 * @param value the hasInPicking
	 */
	public void setHasInPicking(final boolean value)
	{
		setHasInPicking( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute.
	 * @return the hasInvoiced
	 */
	public Boolean isHasInvoiced(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASINVOICED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute.
	 * @return the hasInvoiced
	 */
	public Boolean isHasInvoiced()
	{
		return isHasInvoiced( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute. 
	 * @return the hasInvoiced
	 */
	public boolean isHasInvoicedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasInvoiced( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute. 
	 * @return the hasInvoiced
	 */
	public boolean isHasInvoicedAsPrimitive()
	{
		return isHasInvoicedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute. 
	 * @param value the hasInvoiced
	 */
	public void setHasInvoiced(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASINVOICED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute. 
	 * @param value the hasInvoiced
	 */
	public void setHasInvoiced(final Boolean value)
	{
		setHasInvoiced( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute. 
	 * @param value the hasInvoiced
	 */
	public void setHasInvoiced(final SessionContext ctx, final boolean value)
	{
		setHasInvoiced( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasInvoiced</code> attribute. 
	 * @param value the hasInvoiced
	 */
	public void setHasInvoiced(final boolean value)
	{
		setHasInvoiced( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute.
	 * @return the hasPartiallyDelivered
	 */
	public Boolean isHasPartiallyDelivered(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASPARTIALLYDELIVERED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute.
	 * @return the hasPartiallyDelivered
	 */
	public Boolean isHasPartiallyDelivered()
	{
		return isHasPartiallyDelivered( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute. 
	 * @return the hasPartiallyDelivered
	 */
	public boolean isHasPartiallyDeliveredAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasPartiallyDelivered( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute. 
	 * @return the hasPartiallyDelivered
	 */
	public boolean isHasPartiallyDeliveredAsPrimitive()
	{
		return isHasPartiallyDeliveredAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute. 
	 * @param value the hasPartiallyDelivered
	 */
	public void setHasPartiallyDelivered(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASPARTIALLYDELIVERED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute. 
	 * @param value the hasPartiallyDelivered
	 */
	public void setHasPartiallyDelivered(final Boolean value)
	{
		setHasPartiallyDelivered( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute. 
	 * @param value the hasPartiallyDelivered
	 */
	public void setHasPartiallyDelivered(final SessionContext ctx, final boolean value)
	{
		setHasPartiallyDelivered( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyDelivered</code> attribute. 
	 * @param value the hasPartiallyDelivered
	 */
	public void setHasPartiallyDelivered(final boolean value)
	{
		setHasPartiallyDelivered( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute.
	 * @return the hasPartiallyInvoiced
	 */
	public Boolean isHasPartiallyInvoiced(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASPARTIALLYINVOICED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute.
	 * @return the hasPartiallyInvoiced
	 */
	public Boolean isHasPartiallyInvoiced()
	{
		return isHasPartiallyInvoiced( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute. 
	 * @return the hasPartiallyInvoiced
	 */
	public boolean isHasPartiallyInvoicedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasPartiallyInvoiced( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute. 
	 * @return the hasPartiallyInvoiced
	 */
	public boolean isHasPartiallyInvoicedAsPrimitive()
	{
		return isHasPartiallyInvoicedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute. 
	 * @param value the hasPartiallyInvoiced
	 */
	public void setHasPartiallyInvoiced(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASPARTIALLYINVOICED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute. 
	 * @param value the hasPartiallyInvoiced
	 */
	public void setHasPartiallyInvoiced(final Boolean value)
	{
		setHasPartiallyInvoiced( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute. 
	 * @param value the hasPartiallyInvoiced
	 */
	public void setHasPartiallyInvoiced(final SessionContext ctx, final boolean value)
	{
		setHasPartiallyInvoiced( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyInvoiced</code> attribute. 
	 * @param value the hasPartiallyInvoiced
	 */
	public void setHasPartiallyInvoiced(final boolean value)
	{
		setHasPartiallyInvoiced( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute.
	 * @return the hasPartiallyShipped
	 */
	public Boolean isHasPartiallyShipped(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASPARTIALLYSHIPPED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute.
	 * @return the hasPartiallyShipped
	 */
	public Boolean isHasPartiallyShipped()
	{
		return isHasPartiallyShipped( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute. 
	 * @return the hasPartiallyShipped
	 */
	public boolean isHasPartiallyShippedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasPartiallyShipped( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute. 
	 * @return the hasPartiallyShipped
	 */
	public boolean isHasPartiallyShippedAsPrimitive()
	{
		return isHasPartiallyShippedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute. 
	 * @param value the hasPartiallyShipped
	 */
	public void setHasPartiallyShipped(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASPARTIALLYSHIPPED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute. 
	 * @param value the hasPartiallyShipped
	 */
	public void setHasPartiallyShipped(final Boolean value)
	{
		setHasPartiallyShipped( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute. 
	 * @param value the hasPartiallyShipped
	 */
	public void setHasPartiallyShipped(final SessionContext ctx, final boolean value)
	{
		setHasPartiallyShipped( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasPartiallyShipped</code> attribute. 
	 * @param value the hasPartiallyShipped
	 */
	public void setHasPartiallyShipped(final boolean value)
	{
		setHasPartiallyShipped( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute.
	 * @return the hasShipped
	 */
	public Boolean isHasShipped(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASSHIPPED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute.
	 * @return the hasShipped
	 */
	public Boolean isHasShipped()
	{
		return isHasShipped( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute. 
	 * @return the hasShipped
	 */
	public boolean isHasShippedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasShipped( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute. 
	 * @return the hasShipped
	 */
	public boolean isHasShippedAsPrimitive()
	{
		return isHasShippedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute. 
	 * @param value the hasShipped
	 */
	public void setHasShipped(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASSHIPPED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute. 
	 * @param value the hasShipped
	 */
	public void setHasShipped(final Boolean value)
	{
		setHasShipped( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute. 
	 * @param value the hasShipped
	 */
	public void setHasShipped(final SessionContext ctx, final boolean value)
	{
		setHasShipped( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasShipped</code> attribute. 
	 * @param value the hasShipped
	 */
	public void setHasShipped(final boolean value)
	{
		setHasShipped( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute.
	 * @return the hasUnderAnalysis
	 */
	public Boolean isHasUnderAnalysis(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HASUNDERANALYSIS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute.
	 * @return the hasUnderAnalysis
	 */
	public Boolean isHasUnderAnalysis()
	{
		return isHasUnderAnalysis( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute. 
	 * @return the hasUnderAnalysis
	 */
	public boolean isHasUnderAnalysisAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHasUnderAnalysis( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute. 
	 * @return the hasUnderAnalysis
	 */
	public boolean isHasUnderAnalysisAsPrimitive()
	{
		return isHasUnderAnalysisAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute. 
	 * @param value the hasUnderAnalysis
	 */
	public void setHasUnderAnalysis(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HASUNDERANALYSIS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute. 
	 * @param value the hasUnderAnalysis
	 */
	public void setHasUnderAnalysis(final Boolean value)
	{
		setHasUnderAnalysis( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute. 
	 * @param value the hasUnderAnalysis
	 */
	public void setHasUnderAnalysis(final SessionContext ctx, final boolean value)
	{
		setHasUnderAnalysis( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.hasUnderAnalysis</code> attribute. 
	 * @param value the hasUnderAnalysis
	 */
	public void setHasUnderAnalysis(final boolean value)
	{
		setHasUnderAnalysis( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLAOrderPermutationMatrix.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLAOrderPermutationMatrix.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
}
