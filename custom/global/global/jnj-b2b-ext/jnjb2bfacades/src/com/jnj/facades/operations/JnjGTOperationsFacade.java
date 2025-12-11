/**
 * 
 */
package com.jnj.facades.operations;

import java.util.List;

import com.jnj.facades.data.JnjGTBroadCastData;



/**
 * This class acts for the Middle Level operations
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTOperationsFacade
{
	public List<JnjGTBroadCastData> getBroadCastMessages();

	public JnjGTBroadCastData getBroadCastDataForId(final String broadCastId);

}
