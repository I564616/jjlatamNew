/**
 * 
 */
package com.jnj.facades.territory.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.territory.JnjGTTerritoryFacade;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.services.territory.JnjGTTerritoryService;


/**
 * @author komal.sehgal
 * 
 */
public class DefaultJnjGTTerritoryFacade implements JnjGTTerritoryFacade
{

	@Autowired
	protected JnjGTTerritoryService jnjGtTerritoryService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.facades.territory.JnjGTTerritoryFacade#getTerritoryDivisonModel(com.jnj.core.model.JnjGTTerritoryDivisonModel
	 * )
	 */
	@Override
	public JnjGTTerritoryDivisonModel getTerritoryDivisonModel(final JnjGTTerritoryDivisonModel jnjGTTerritoryDiv)
	{
		return jnjGtTerritoryService.getTerritoryDivisonModel(jnjGTTerritoryDiv);
	}
}
