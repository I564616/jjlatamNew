/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.constants;

/**
 * FlexiSearch Queries used in this extension.
 * 
 * @author Accenture
 * 
 * @version 1.0
 * 
 */
public final class JNJFlexiQueries
{

	/**
	 * Fetch the site by country and channel.
	 */
	public static final String SITE_BY_COUNTRY = "select {site.pk} from {CMSSite as site} "
			+ " where  {site.active}=1 and {site.baseCountry}=?country";


	/**
	 * Fetch all messages in a catalog version.
	 */
	public static final String MESSAGES_BY_CATALOGVERSION = "Select {pk} from {MessageItem} where {catalogVersion} in (?catalogVersions)";

	/**
	 * Fetch the message by code, catalogVersion and channel type.
	 */
	public static final String MESSAGE_BY_CODE = "Select {pk} from {MessageItem} where {uid}=?code and {catalogVersion} "
			+ "in (?catalogVersions)";


	/**
	 * Private Constructor for this class.
	 */
	private JNJFlexiQueries()
	{
		// Private constructor for this class.
	}
}
