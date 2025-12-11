/**
 * 
 */
package com.jnj.gt.service.surgeon;


import com.jnj.core.model.JnjGTSurgeonModel;


// TODO: Auto-generated Javadoc
/**
 * The Interface JnjGTSurgeonFeedService.
 * 
 * @author sakshi.kashiva
 */
public interface JnjGTSurgeonFeedService
{
	/**
	 * Gets the surgeon model by id.
	 * 
	 * @param surgeonId
	 *           the surgeon id
	 * @return the surgeon model
	 */
	public JnjGTSurgeonModel getJnjGTSurgeonModelById(String surgeonId);
}
