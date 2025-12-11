/**
 * 
 */
/**
 * 
 */
package com.jnj.core.services.operations;

import java.util.Date;
import java.util.List;

import com.jnj.core.model.BroadcastMessageModel;


/**
 * This class Handles Business Logic For Operations Module
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTOperationsService
{
	public boolean logAuditData(String systemOrProcess, String descriptionOfEvent, String ipAddress, boolean isAuthorised,
			boolean isSuccess, Date timeOfEvent, String userId);

	public boolean cleanAllRecords();

	public List<BroadcastMessageModel> getBroadCastMessages();

	/**
	 * @param broadCastId
	 * @return
	 */
	public BroadcastMessageModel getBroadCastDataForId(String broadCastId);
}
