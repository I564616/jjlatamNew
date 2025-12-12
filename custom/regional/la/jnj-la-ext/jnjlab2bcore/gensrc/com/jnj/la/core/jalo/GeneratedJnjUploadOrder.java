/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Dec 12, 2025, 9:48:14 AM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.jalo.JnJB2BUnit;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem JnjUploadOrder}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjUploadOrder extends GenericItem
{
	/** Qualifier of the <code>JnjUploadOrder.fileNameId</code> attribute **/
	public static final String FILENAMEID = "fileNameId";
	/** Qualifier of the <code>JnjUploadOrder.fileName</code> attribute **/
	public static final String FILENAME = "fileName";
	/** Qualifier of the <code>JnjUploadOrder.date</code> attribute **/
	public static final String DATE = "date";
	/** Qualifier of the <code>JnjUploadOrder.errorMessageList</code> attribute **/
	public static final String ERRORMESSAGELIST = "errorMessageList";
	/** Qualifier of the <code>JnjUploadOrder.uploadFileStatus</code> attribute **/
	public static final String UPLOADFILESTATUS = "uploadFileStatus";
	/** Qualifier of the <code>JnjUploadOrder.uploadByUser</code> attribute **/
	public static final String UPLOADBYUSER = "uploadByUser";
	/** Qualifier of the <code>JnjUploadOrder.uploadByCustomer</code> attribute **/
	public static final String UPLOADBYCUSTOMER = "uploadByCustomer";
	/** Qualifier of the <code>JnjUploadOrder.trackingId</code> attribute **/
	public static final String TRACKINGID = "trackingId";
	/** Qualifier of the <code>JnjUploadOrder.b2bUnitId</code> attribute **/
	public static final String B2BUNITID = "b2bUnitId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(FILENAMEID, AttributeMode.INITIAL);
		tmp.put(FILENAME, AttributeMode.INITIAL);
		tmp.put(DATE, AttributeMode.INITIAL);
		tmp.put(ERRORMESSAGELIST, AttributeMode.INITIAL);
		tmp.put(UPLOADFILESTATUS, AttributeMode.INITIAL);
		tmp.put(UPLOADBYUSER, AttributeMode.INITIAL);
		tmp.put(UPLOADBYCUSTOMER, AttributeMode.INITIAL);
		tmp.put(TRACKINGID, AttributeMode.INITIAL);
		tmp.put(B2BUNITID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId(final SessionContext ctx)
	{
		return (JnJB2BUnit)getProperty( ctx, B2BUNITID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.b2bUnitId</code> attribute.
	 * @return the b2bUnitId
	 */
	public JnJB2BUnit getB2bUnitId()
	{
		return getB2bUnitId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final SessionContext ctx, final JnJB2BUnit value)
	{
		setProperty(ctx, B2BUNITID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.b2bUnitId</code> attribute. 
	 * @param value the b2bUnitId
	 */
	public void setB2bUnitId(final JnJB2BUnit value)
	{
		setB2bUnitId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.date</code> attribute.
	 * @return the date
	 */
	public Date getDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.date</code> attribute.
	 * @return the date
	 */
	public Date getDate()
	{
		return getDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.date</code> attribute. 
	 * @param value the date
	 */
	public void setDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, DATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.date</code> attribute. 
	 * @param value the date
	 */
	public void setDate(final Date value)
	{
		setDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.errorMessageList</code> attribute.
	 * @return the errorMessageList
	 */
	public List<String> getErrorMessageList(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, ERRORMESSAGELIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.errorMessageList</code> attribute.
	 * @return the errorMessageList
	 */
	public List<String> getErrorMessageList()
	{
		return getErrorMessageList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.errorMessageList</code> attribute. 
	 * @param value the errorMessageList
	 */
	public void setErrorMessageList(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, ERRORMESSAGELIST,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.errorMessageList</code> attribute. 
	 * @param value the errorMessageList
	 */
	public void setErrorMessageList(final List<String> value)
	{
		setErrorMessageList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.fileName</code> attribute.
	 * @return the fileName
	 */
	public String getFileName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FILENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.fileName</code> attribute.
	 * @return the fileName
	 */
	public String getFileName()
	{
		return getFileName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.fileName</code> attribute. 
	 * @param value the fileName
	 */
	public void setFileName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FILENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.fileName</code> attribute. 
	 * @param value the fileName
	 */
	public void setFileName(final String value)
	{
		setFileName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.fileNameId</code> attribute.
	 * @return the fileNameId
	 */
	public String getFileNameId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FILENAMEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.fileNameId</code> attribute.
	 * @return the fileNameId
	 */
	public String getFileNameId()
	{
		return getFileNameId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.fileNameId</code> attribute. 
	 * @param value the fileNameId
	 */
	public void setFileNameId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FILENAMEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.fileNameId</code> attribute. 
	 * @param value the fileNameId
	 */
	public void setFileNameId(final String value)
	{
		setFileNameId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.trackingId</code> attribute.
	 * @return the trackingId
	 */
	public String getTrackingId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TRACKINGID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.trackingId</code> attribute.
	 * @return the trackingId
	 */
	public String getTrackingId()
	{
		return getTrackingId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.trackingId</code> attribute. 
	 * @param value the trackingId
	 */
	public void setTrackingId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TRACKINGID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.trackingId</code> attribute. 
	 * @param value the trackingId
	 */
	public void setTrackingId(final String value)
	{
		setTrackingId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.uploadByCustomer</code> attribute.
	 * @return the uploadByCustomer
	 */
	public String getUploadByCustomer(final SessionContext ctx)
	{
		return (String)getProperty( ctx, UPLOADBYCUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.uploadByCustomer</code> attribute.
	 * @return the uploadByCustomer
	 */
	public String getUploadByCustomer()
	{
		return getUploadByCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.uploadByCustomer</code> attribute. 
	 * @param value the uploadByCustomer
	 */
	public void setUploadByCustomer(final SessionContext ctx, final String value)
	{
		setProperty(ctx, UPLOADBYCUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.uploadByCustomer</code> attribute. 
	 * @param value the uploadByCustomer
	 */
	public void setUploadByCustomer(final String value)
	{
		setUploadByCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.uploadByUser</code> attribute.
	 * @return the uploadByUser
	 */
	public String getUploadByUser(final SessionContext ctx)
	{
		return (String)getProperty( ctx, UPLOADBYUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.uploadByUser</code> attribute.
	 * @return the uploadByUser
	 */
	public String getUploadByUser()
	{
		return getUploadByUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.uploadByUser</code> attribute. 
	 * @param value the uploadByUser
	 */
	public void setUploadByUser(final SessionContext ctx, final String value)
	{
		setProperty(ctx, UPLOADBYUSER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.uploadByUser</code> attribute. 
	 * @param value the uploadByUser
	 */
	public void setUploadByUser(final String value)
	{
		setUploadByUser( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.uploadFileStatus</code> attribute.
	 * @return the uploadFileStatus
	 */
	public String getUploadFileStatus(final SessionContext ctx)
	{
		return (String)getProperty( ctx, UPLOADFILESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUploadOrder.uploadFileStatus</code> attribute.
	 * @return the uploadFileStatus
	 */
	public String getUploadFileStatus()
	{
		return getUploadFileStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.uploadFileStatus</code> attribute. 
	 * @param value the uploadFileStatus
	 */
	public void setUploadFileStatus(final SessionContext ctx, final String value)
	{
		setProperty(ctx, UPLOADFILESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUploadOrder.uploadFileStatus</code> attribute. 
	 * @param value the uploadFileStatus
	 */
	public void setUploadFileStatus(final String value)
	{
		setUploadFileStatus( getSession().getSessionContext(), value );
	}
	
}
