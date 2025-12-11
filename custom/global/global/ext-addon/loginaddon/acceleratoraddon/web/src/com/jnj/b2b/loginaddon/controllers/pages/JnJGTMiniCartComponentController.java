/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.controllers.cms.MiniCartComponentController;

import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
 
/**
 * Controller for CMS MiniCartComponent.
 */
public class JnJGTMiniCartComponentController extends MiniCartComponentController 
{
	
	/**
	 * This method has been modified so as to provide different view for PCM and EPIC sites
	 * @param <T>
	 */
	 
	@Override
	protected String getView(final MiniCartComponentModel component)
	{
	   return getView(LoginaddonControllerConstants.Views.Cms.minicart.ComponentPrefix);
		 
	}
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
}
