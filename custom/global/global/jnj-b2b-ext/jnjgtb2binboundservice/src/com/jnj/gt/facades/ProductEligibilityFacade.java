/**
 *
 */
package com.jnj.gt.facades;

import com.jnj.gt.core.model.JnjInterfaceCronJobModel;


/**
 * @author dnagamon
 * 
 */
public interface ProductEligibilityFacade
{
	void processProductEligibilityFeed(final String impexFileName, final JnjInterfaceCronJobModel interfaceCronJob);
}
