package com.jnj.gt.pcm.integration.core.jalo;

import com.jnj.gt.pcm.integration.core.constants.JnjpcmcoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class JnjpcmcoreManager extends GeneratedJnjpcmcoreManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( JnjpcmcoreManager.class.getName() );
	
	public static final JnjpcmcoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (JnjpcmcoreManager) em.getExtension(JnjpcmcoreConstants.EXTENSIONNAME);
	}
	
}
