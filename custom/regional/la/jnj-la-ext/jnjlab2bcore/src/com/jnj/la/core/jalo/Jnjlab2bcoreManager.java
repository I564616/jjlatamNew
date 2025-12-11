package com.jnj.la.core.jalo;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class Jnjlab2bcoreManager extends GeneratedJnjlab2bcoreManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( Jnjlab2bcoreManager.class.getName() );
	
	public static final Jnjlab2bcoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Jnjlab2bcoreManager) em.getExtension(Jnjlab2bcoreConstants.EXTENSIONNAME);
	}
	
}
