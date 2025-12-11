package com.jnj.gt.jalo;

import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class Jnjgtb2binboundserviceManager extends GeneratedJnjgtb2binboundserviceManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( Jnjgtb2binboundserviceManager.class.getName() );
	
	public static final Jnjgtb2binboundserviceManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Jnjgtb2binboundserviceManager) em.getExtension(Jnjgtb2binboundserviceConstants.EXTENSIONNAME);
	}
	
}
