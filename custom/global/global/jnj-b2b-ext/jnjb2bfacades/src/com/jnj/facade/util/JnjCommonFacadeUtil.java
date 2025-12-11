/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facade.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.ui.Model;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCommonFacadeUtil
{
	public String getMessageFromImpex(String messageCode);
	
	public String getMessageFromImpex(String messageCode,final Locale locale);
	
	public String getMessageFromPropertiesFile(String messageCode);

	public List<String> getName(String name);

	public String createMediaLogoURL();

	public Map<String, String> getDropDownFromConfig(String configId);

	public String getMessageForCode(final String messageCode, final Locale locale, final String... params);
	
	public String getMediaURL(final String catalogId, final String catalogVersion, final String mediaCode);
	
	public String getMediaAbsoluteURL(String relativeURL, StringBuffer requestURL);
	
	public List<String> getPermissions();
	
	public boolean getPermissionsFlagsForCommercialUserGroup(final String group);
}
