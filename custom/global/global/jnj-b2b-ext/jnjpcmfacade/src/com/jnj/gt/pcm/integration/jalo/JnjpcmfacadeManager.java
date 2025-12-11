package com.jnj.gt.pcm.integration.jalo;

import com.jnj.gt.pcm.integration.constants.JnjpcmfacadeConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class JnjpcmfacadeManager extends GeneratedJnjpcmfacadeManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( JnjpcmfacadeManager.class.getName() );
	
	public static final JnjpcmfacadeManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (JnjpcmfacadeManager) em.getExtension(JnjpcmfacadeConstants.EXTENSIONNAME);
	}
	
}
