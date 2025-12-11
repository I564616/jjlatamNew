package com.jnj.la.b2b.jnjlaselloutaddon.jalo;

import com.jnj.la.b2b.jnjlaselloutaddon.constants.JnjlaselloutaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class JnjlaselloutaddonManager extends GeneratedJnjlaselloutaddonManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( JnjlaselloutaddonManager.class.getName() );
	
	public static final JnjlaselloutaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (JnjlaselloutaddonManager) em.getExtension(JnjlaselloutaddonConstants.EXTENSIONNAME);
	}
	
}
