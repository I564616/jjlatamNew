/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 9:48:14 AM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2BUnit;
import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjUploadOrderSHA}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjUploadOrderSHA extends GenericItem
{
	/** Qualifier of the <code>JnjUploadOrderSHA.user</code> attribute **/
	public static final String USER = "user";
	/** Qualifier of the <code>JnjUploadOrderSHA.soldTo</code> attribute **/
	public static final String SOLDTO = "soldTo";
	/** Qualifier of the <code>JnjUploadOrderSHA.userFileHashId</code> attribute **/
	public static final String USERFILEHASHID = "userFileHashId";
	/** Qualifier of the <code>JnjUploadOrderSHA.userFileName</code> attribute **/
	public static final String USERFILENAME = "userFileName";
	/** Qualifier of the <code>JnjUploadOrderSHA.userFileTS</code> attribute **/
	public static final String USERFILETS = "userFileTS";
	/** Qualifier of the <code>JnjUploadOrderSHA.xmlFileName</code> attribute **/
	public static final String XMLFILENAME = "xmlFileName";
	/** Qualifier of the <code>JnjUploadOrderSHA.xmlFileStatus</code> attribute **/
	public static final String XMLFILESTATUS = "xmlFileStatus";
	/** Qualifier of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute **/
	public static final String XMLFILECOUNT = "xmlFileCount";
	/** Qualifier of the <code>JnjUploadOrderSHA.nodeId</code> attribute **/
	public static final String NODEID = "nodeId";
	/** Qualifier of the <code>JnjUploadOrderSHA.b2bUnitId</code> attribute **/
	public static final String B2BUNITID = "b2bUnitId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USER, AttributeMode.INITIAL);
		tmp.put(SOLDTO, AttributeMode.INITIAL);
		tmp.put(USERFILEHASHID, AttributeMode.INITIAL);
		tmp.put(USERFILENAME, AttributeMode.INITIAL);
		tmp.put(USERFILETS, AttributeMode.INITIAL);
		tmp.put(XMLFILENAME, AttributeMode.INITIAL);
		tmp.put(XMLFILESTATUS, AttributeMode.INITIAL);
		tmp.put(XMLFILECOUNT, AttributeMode.INITIAL);
		tmp.put(NODEID, AttributeMode.INITIAL);
		tmp.put(B2BUNITID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, B2BUNITID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId()
	{
		return getB2bUnitId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, B2BUNITID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final JnJB2BUnit value)
	{
		setB2bUnitId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.nodeId</code> attribute.
	 * @return the nodeId
	 */
	public Integer getNodeId(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, NODEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.nodeId</code> attribute.
	 * @return the nodeId
	 */
	public Integer getNodeId()
	{
		return getNodeId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.nodeId</code> attribute. 
	 * @return the nodeId
	 */
	public int getNodeIdAsPrimitive(final SessionContext ctx)
	{
		Integer value = getNodeId( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.nodeId</code> attribute. 
	 * @return the nodeId
	 */
	public int getNodeIdAsPrimitive()
	{
		return getNodeIdAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.nodeId</code> attribute. 
	 * @param value the nodeId
	 */
	public void setNodeId(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, NODEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.nodeId</code> attribute. 
	 * @param value the nodeId
	 */
	public void setNodeId(final Integer value)
	{
		setNodeId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.nodeId</code> attribute. 
	 * @param value the nodeId
	 */
	public void setNodeId(final SessionContext ctx, final int value)
	{
		setNodeId( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.nodeId</code> attribute. 
	 * @param value the nodeId
	 */
	public void setNodeId(final int value)
	{
		setNodeId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.soldTo</code> attribute.
	 * @return the soldTo
	 */
	public String getSoldTo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SOLDTO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.soldTo</code> attribute.
	 * @return the soldTo
	 */
	public String getSoldTo()
	{
		return getSoldTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.soldTo</code> attribute. 
	 * @param value the soldTo
	 */
	public void setSoldTo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SOLDTO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.soldTo</code> attribute. 
	 * @param value the soldTo
	 */
	public void setSoldTo(final String value)
	{
		setSoldTo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser(final SessionContext ctx)
	{
		return (JnJB2bCustomer)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.user</code> attribute.
	 * @return the user
	 */
	public JnJB2bCustomer getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final JnJB2bCustomer value)
	{
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final JnJB2bCustomer value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.userFileHashId</code> attribute.
	 * @return the userFileHashId
	 */
	public String getUserFileHashId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, USERFILEHASHID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.userFileHashId</code> attribute.
	 * @return the userFileHashId
	 */
	public String getUserFileHashId()
	{
		return getUserFileHashId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.userFileHashId</code> attribute. 
	 * @param value the userFileHashId
	 */
	public void setUserFileHashId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, USERFILEHASHID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.userFileHashId</code> attribute. 
	 * @param value the userFileHashId
	 */
	public void setUserFileHashId(final String value)
	{
		setUserFileHashId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.userFileName</code> attribute.
	 * @return the userFileName
	 */
	public String getUserFileName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, USERFILENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.userFileName</code> attribute.
	 * @return the userFileName
	 */
	public String getUserFileName()
	{
		return getUserFileName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.userFileName</code> attribute. 
	 * @param value the userFileName
	 */
	public void setUserFileName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, USERFILENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.userFileName</code> attribute. 
	 * @param value the userFileName
	 */
	public void setUserFileName(final String value)
	{
		setUserFileName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.userFileTS</code> attribute.
	 * @return the userFileTS
	 */
	public Date getUserFileTS(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, USERFILETS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.userFileTS</code> attribute.
	 * @return the userFileTS
	 */
	public Date getUserFileTS()
	{
		return getUserFileTS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.userFileTS</code> attribute. 
	 * @param value the userFileTS
	 */
	public void setUserFileTS(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, USERFILETS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.userFileTS</code> attribute. 
	 * @param value the userFileTS
	 */
	public void setUserFileTS(final Date value)
	{
		setUserFileTS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute.
	 * @return the xmlFileCount
	 */
	public Integer getXmlFileCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, XMLFILECOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute.
	 * @return the xmlFileCount
	 */
	public Integer getXmlFileCount()
	{
		return getXmlFileCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute. 
	 * @return the xmlFileCount
	 */
	public int getXmlFileCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getXmlFileCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute. 
	 * @return the xmlFileCount
	 */
	public int getXmlFileCountAsPrimitive()
	{
		return getXmlFileCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute. 
	 * @param value the xmlFileCount
	 */
	public void setXmlFileCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, XMLFILECOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute. 
	 * @param value the xmlFileCount
	 */
	public void setXmlFileCount(final Integer value)
	{
		setXmlFileCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute. 
	 * @param value the xmlFileCount
	 */
	public void setXmlFileCount(final SessionContext ctx, final int value)
	{
		setXmlFileCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileCount</code> attribute. 
	 * @param value the xmlFileCount
	 */
	public void setXmlFileCount(final int value)
	{
		setXmlFileCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileName</code> attribute.
	 * @return the xmlFileName
	 */
	public String getXmlFileName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, XMLFILENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileName</code> attribute.
	 * @return the xmlFileName
	 */
	public String getXmlFileName()
	{
		return getXmlFileName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileName</code> attribute. 
	 * @param value the xmlFileName
	 */
	public void setXmlFileName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, XMLFILENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileName</code> attribute. 
	 * @param value the xmlFileName
	 */
	public void setXmlFileName(final String value)
	{
		setXmlFileName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileStatus</code> attribute.
	 * @return the xmlFileStatus
	 */
	public EnumerationValue getXmlFileStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, XMLFILESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrderSHA.xmlFileStatus</code> attribute.
	 * @return the xmlFileStatus
	 */
	public EnumerationValue getXmlFileStatus()
	{
		return getXmlFileStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileStatus</code> attribute. 
	 * @param value the xmlFileStatus
	 */
	public void setXmlFileStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, XMLFILESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrderSHA.xmlFileStatus</code> attribute. 
	 * @param value the xmlFileStatus
	 */
	public void setXmlFileStatus(final EnumerationValue value)
	{
		setXmlFileStatus( getSession().getSessionContext(), value );
	}
	
}
