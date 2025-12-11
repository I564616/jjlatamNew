/**
 * 
 */
package com.jnj.core.services;

import java.util.HashMap;

import com.jnj.core.services.JnjContactUsService;


/**
 * @author balinder.singh
 * 
 */
public interface JnjGTContactUsService extends JnjContactUsService
{
	public HashMap<String, String> getSubjectDropDown();

	/**
	 * @param subjectID
	 * @return
	 */
	String getEmailForContractUs(String subjectID);

	/**
	 * @param subjectID
	 * @return
	 */
	String getSubjectForContractUs(String subjectID);
}
