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
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjSAPErrorTranslationTable}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjSAPErrorTranslationTable extends GenericItem
{
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.key</code> attribute **/
	public static final String KEY = "key";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.errorMessageType</code> attribute **/
	public static final String ERRORMESSAGETYPE = "errorMessageType";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.scope</code> attribute **/
	public static final String SCOPE = "scope";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute **/
	public static final String STARTPOSTION = "startPostion";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute **/
	public static final String ENDPOSITION = "endPosition";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute **/
	public static final String TANSLATEDMESSAGE = "tanslatedMessage";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute **/
	public static final String NOOFEXTRACTIONS = "noOfExtractions";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute **/
	public static final String FIRSTEXTRACTION = "firstExtraction";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute **/
	public static final String SECONDEXTRACTION = "secondExtraction";
	/** Qualifier of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute **/
	public static final String THIRDEXTRACTION = "thirdExtraction";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(KEY, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(ERRORMESSAGETYPE, AttributeMode.INITIAL);
		tmp.put(SCOPE, AttributeMode.INITIAL);
		tmp.put(STARTPOSTION, AttributeMode.INITIAL);
		tmp.put(ENDPOSITION, AttributeMode.INITIAL);
		tmp.put(TANSLATEDMESSAGE, AttributeMode.INITIAL);
		tmp.put(NOOFEXTRACTIONS, AttributeMode.INITIAL);
		tmp.put(FIRSTEXTRACTION, AttributeMode.INITIAL);
		tmp.put(SECONDEXTRACTION, AttributeMode.INITIAL);
		tmp.put(THIRDEXTRACTION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute.
	 * @return the endPosition
	 */
	public Integer getEndPosition(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ENDPOSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute.
	 * @return the endPosition
	 */
	public Integer getEndPosition()
	{
		return getEndPosition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute. 
	 * @return the endPosition
	 */
	public int getEndPositionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getEndPosition( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute. 
	 * @return the endPosition
	 */
	public int getEndPositionAsPrimitive()
	{
		return getEndPositionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute. 
	 * @param value the endPosition
	 */
	public void setEndPosition(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ENDPOSITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute. 
	 * @param value the endPosition
	 */
	public void setEndPosition(final Integer value)
	{
		setEndPosition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute. 
	 * @param value the endPosition
	 */
	public void setEndPosition(final SessionContext ctx, final int value)
	{
		setEndPosition( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.endPosition</code> attribute. 
	 * @param value the endPosition
	 */
	public void setEndPosition(final int value)
	{
		setEndPosition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.errorMessageType</code> attribute.
	 * @return the errorMessageType
	 */
	public EnumerationValue getErrorMessageType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, ERRORMESSAGETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.errorMessageType</code> attribute.
	 * @return the errorMessageType
	 */
	public EnumerationValue getErrorMessageType()
	{
		return getErrorMessageType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.errorMessageType</code> attribute. 
	 * @param value the errorMessageType
	 */
	public void setErrorMessageType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, ERRORMESSAGETYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.errorMessageType</code> attribute. 
	 * @param value the errorMessageType
	 */
	public void setErrorMessageType(final EnumerationValue value)
	{
		setErrorMessageType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute.
	 * @return the firstExtraction
	 */
	public Integer getFirstExtraction(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, FIRSTEXTRACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute.
	 * @return the firstExtraction
	 */
	public Integer getFirstExtraction()
	{
		return getFirstExtraction( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute. 
	 * @return the firstExtraction
	 */
	public int getFirstExtractionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getFirstExtraction( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute. 
	 * @return the firstExtraction
	 */
	public int getFirstExtractionAsPrimitive()
	{
		return getFirstExtractionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute. 
	 * @param value the firstExtraction
	 */
	public void setFirstExtraction(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, FIRSTEXTRACTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute. 
	 * @param value the firstExtraction
	 */
	public void setFirstExtraction(final Integer value)
	{
		setFirstExtraction( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute. 
	 * @param value the firstExtraction
	 */
	public void setFirstExtraction(final SessionContext ctx, final int value)
	{
		setFirstExtraction( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.firstExtraction</code> attribute. 
	 * @param value the firstExtraction
	 */
	public void setFirstExtraction(final int value)
	{
		setFirstExtraction( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.key</code> attribute.
	 * @return the key
	 */
	public String getKey(final SessionContext ctx)
	{
		return (String)getProperty( ctx, KEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.key</code> attribute.
	 * @return the key
	 */
	public String getKey()
	{
		return getKey( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.key</code> attribute. 
	 * @param value the key
	 */
	public void setKey(final SessionContext ctx, final String value)
	{
		setProperty(ctx, KEY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.key</code> attribute. 
	 * @param value the key
	 */
	public void setKey(final String value)
	{
		setKey( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute.
	 * @return the noOfExtractions
	 */
	public Integer getNoOfExtractions(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, NOOFEXTRACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute.
	 * @return the noOfExtractions
	 */
	public Integer getNoOfExtractions()
	{
		return getNoOfExtractions( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute. 
	 * @return the noOfExtractions
	 */
	public int getNoOfExtractionsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getNoOfExtractions( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute. 
	 * @return the noOfExtractions
	 */
	public int getNoOfExtractionsAsPrimitive()
	{
		return getNoOfExtractionsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute. 
	 * @param value the noOfExtractions
	 */
	public void setNoOfExtractions(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, NOOFEXTRACTIONS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute. 
	 * @param value the noOfExtractions
	 */
	public void setNoOfExtractions(final Integer value)
	{
		setNoOfExtractions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute. 
	 * @param value the noOfExtractions
	 */
	public void setNoOfExtractions(final SessionContext ctx, final int value)
	{
		setNoOfExtractions( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.noOfExtractions</code> attribute. 
	 * @param value the noOfExtractions
	 */
	public void setNoOfExtractions(final int value)
	{
		setNoOfExtractions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.scope</code> attribute.
	 * @return the scope
	 */
	public EnumerationValue getScope(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, SCOPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.scope</code> attribute.
	 * @return the scope
	 */
	public EnumerationValue getScope()
	{
		return getScope( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.scope</code> attribute. 
	 * @param value the scope
	 */
	public void setScope(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, SCOPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.scope</code> attribute. 
	 * @param value the scope
	 */
	public void setScope(final EnumerationValue value)
	{
		setScope( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute.
	 * @return the secondExtraction
	 */
	public Integer getSecondExtraction(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, SECONDEXTRACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute.
	 * @return the secondExtraction
	 */
	public Integer getSecondExtraction()
	{
		return getSecondExtraction( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute. 
	 * @return the secondExtraction
	 */
	public int getSecondExtractionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getSecondExtraction( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute. 
	 * @return the secondExtraction
	 */
	public int getSecondExtractionAsPrimitive()
	{
		return getSecondExtractionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute. 
	 * @param value the secondExtraction
	 */
	public void setSecondExtraction(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, SECONDEXTRACTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute. 
	 * @param value the secondExtraction
	 */
	public void setSecondExtraction(final Integer value)
	{
		setSecondExtraction( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute. 
	 * @param value the secondExtraction
	 */
	public void setSecondExtraction(final SessionContext ctx, final int value)
	{
		setSecondExtraction( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.secondExtraction</code> attribute. 
	 * @param value the secondExtraction
	 */
	public void setSecondExtraction(final int value)
	{
		setSecondExtraction( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute.
	 * @return the startPostion
	 */
	public Integer getStartPostion(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, STARTPOSTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute.
	 * @return the startPostion
	 */
	public Integer getStartPostion()
	{
		return getStartPostion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute. 
	 * @return the startPostion
	 */
	public int getStartPostionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getStartPostion( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute. 
	 * @return the startPostion
	 */
	public int getStartPostionAsPrimitive()
	{
		return getStartPostionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute. 
	 * @param value the startPostion
	 */
	public void setStartPostion(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, STARTPOSTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute. 
	 * @param value the startPostion
	 */
	public void setStartPostion(final Integer value)
	{
		setStartPostion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute. 
	 * @param value the startPostion
	 */
	public void setStartPostion(final SessionContext ctx, final int value)
	{
		setStartPostion( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.startPostion</code> attribute. 
	 * @param value the startPostion
	 */
	public void setStartPostion(final int value)
	{
		setStartPostion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute.
	 * @return the tanslatedMessage
	 */
	public String getTanslatedMessage(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedJnjSAPErrorTranslationTable.getTanslatedMessage requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TANSLATEDMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute.
	 * @return the tanslatedMessage
	 */
	public String getTanslatedMessage()
	{
		return getTanslatedMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute. 
	 * @return the localized tanslatedMessage
	 */
	public Map<Language,String> getAllTanslatedMessage(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TANSLATEDMESSAGE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute. 
	 * @return the localized tanslatedMessage
	 */
	public Map<Language,String> getAllTanslatedMessage()
	{
		return getAllTanslatedMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute. 
	 * @param value the tanslatedMessage
	 */
	public void setTanslatedMessage(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedJnjSAPErrorTranslationTable.setTanslatedMessage requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TANSLATEDMESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute. 
	 * @param value the tanslatedMessage
	 */
	public void setTanslatedMessage(final String value)
	{
		setTanslatedMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute. 
	 * @param value the tanslatedMessage
	 */
	public void setAllTanslatedMessage(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TANSLATEDMESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.tanslatedMessage</code> attribute. 
	 * @param value the tanslatedMessage
	 */
	public void setAllTanslatedMessage(final Map<Language,String> value)
	{
		setAllTanslatedMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute.
	 * @return the thirdExtraction
	 */
	public Integer getThirdExtraction(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, THIRDEXTRACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute.
	 * @return the thirdExtraction
	 */
	public Integer getThirdExtraction()
	{
		return getThirdExtraction( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute. 
	 * @return the thirdExtraction
	 */
	public int getThirdExtractionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getThirdExtraction( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute. 
	 * @return the thirdExtraction
	 */
	public int getThirdExtractionAsPrimitive()
	{
		return getThirdExtractionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute. 
	 * @param value the thirdExtraction
	 */
	public void setThirdExtraction(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, THIRDEXTRACTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute. 
	 * @param value the thirdExtraction
	 */
	public void setThirdExtraction(final Integer value)
	{
		setThirdExtraction( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute. 
	 * @param value the thirdExtraction
	 */
	public void setThirdExtraction(final SessionContext ctx, final int value)
	{
		setThirdExtraction( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjSAPErrorTranslationTable.thirdExtraction</code> attribute. 
	 * @param value the thirdExtraction
	 */
	public void setThirdExtraction(final int value)
	{
		setThirdExtraction( getSession().getSessionContext(), value );
	}
	
}
