/**
 * 
 */
package com.jnj.facades.help;

import java.util.HashMap;

import com.jnj.facades.help.JnjContactUsFacade;
import com.jnj.facades.data.JnjGTContactUsData;


/**
 * @author balinder.singh
 * 
 */
public interface JnjGTContactUsFacade extends JnjContactUsFacade
{
	public HashMap<String, String> getSubjectDropDown();

	public String getToEmailID(String subjectId);

	public void sendMail(JnjGTContactUsData jnjGTContactUsData);
}
