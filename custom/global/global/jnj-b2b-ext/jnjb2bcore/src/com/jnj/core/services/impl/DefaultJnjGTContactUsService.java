/**
 * 
 */
package com.jnj.core.services.impl;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.HashMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.impl.DefaultJnjContactUsService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnjGTContactUsService;
//import com.jnj.pcm.constants.JnjPCMCoreConstants;


/**
 * @author balinder.singh
 * 
 */
public class DefaultJnjGTContactUsService extends DefaultJnjContactUsService implements JnjGTContactUsService
{

	@Autowired
	protected SessionService sessionService;

	public SessionService getSessionService() {
		return sessionService;
	}

	protected final String SUBJECT_LIST = "help.email.subject";
	protected final String EMAIL_LIST = "help.email.id";

	//FOR PCM
	protected final String SUBJECT_LIST_PCM = "help.email.subject.pcm";
	protected final String EMAIL_LIST_PCM = "help.email.id.pcm";

	@Override
	public HashMap<String, String> getSubjectDropDown()
	{
		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		String subjectList = SUBJECT_LIST;
		/*if (siteName.equals(JnjPCMCoreConstants.PCM))
		{
			subjectList = SUBJECT_LIST_PCM;
		}*/

		return getHashMap(subjectList);
	}

	@Override
	public String getEmailForContractUs(final String subjectID)
	{
		String returnValue = null;
		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		String emailList = EMAIL_LIST;
		/*if (siteName.equals(JnjPCMCoreConstants.PCM))
		{
			emailList = EMAIL_LIST_PCM;
		}*/
		final HashMap<String, String> emailHashMap = getHashMap(emailList);
		if (MapUtils.isNotEmpty(emailHashMap))
		{
			returnValue = emailHashMap.get(subjectID);
		}
		return returnValue;
	}

	@Override
	public String getSubjectForContractUs(final String subjectID)
	{
		String returnValue = null;

		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		String subjectList = SUBJECT_LIST;
		/*if (siteName.equals(JnjPCMCoreConstants.PCM))
		{
			subjectList = SUBJECT_LIST_PCM;
		}*/
		final HashMap<String, String> subjectHashMap = getHashMap(subjectList);
		if (MapUtils.isNotEmpty(subjectHashMap))
		{
			returnValue = subjectHashMap.get(subjectID);
		}
		return returnValue;
	}

	/**
	 * This method returns a hashmap of the property in the property file with format key1|value1,key2|value2....
	 * subjectListID will be the parameter name in the property file
	 * 
	 * @param subjectListID
	 * @return
	 */
	protected HashMap<String, String> getHashMap(final String subjectListID)
	{
		HashMap<String, String> returnHash = null;
		final String subjectList = Config.getParameter(subjectListID);
		if (StringUtils.isNotEmpty(subjectList))
		{
			returnHash = new HashMap<String, String>();
			for (final String subject : subjectList.split(Jnjb2bCoreConstants.SYMBOl_COMMA))
			{
				if (StringUtils.isNotEmpty(subject))
				{
					final String[] subjectKeyValue = StringUtils.split(subject, Jnjb2bCoreConstants.SYMBOl_PIPE);

					returnHash.put(subjectKeyValue[0], subjectKeyValue[1]);
				}
			}
		}
		return returnHash;
	}
}
