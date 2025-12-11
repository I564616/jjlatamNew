package com.jnj.gt.pcm.integration.jalo;

import com.jnj.gt.pcm.integration.constants.JnjpcmjobConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class JnjpcmjobManager extends GeneratedJnjpcmjobManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( JnjpcmjobManager.class.getName() );
	
	public static final JnjpcmjobManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (JnjpcmjobManager) em.getExtension(JnjpcmjobConstants.EXTENSIONNAME);
	}
	
}
