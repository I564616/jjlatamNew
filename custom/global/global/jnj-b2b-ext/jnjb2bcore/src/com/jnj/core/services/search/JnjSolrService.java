/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.search;

import java.util.Collection;


/**
 * This class is created for adding new api for getting the field name along with the collection of Strings for which we
 * need to restrict searching.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjSolrService
{
	/**
	 * Used to get collection of Strings which will needs to be restricted for field query.
	 * 
	 * @return List of discontinued Products
	 */
	public Collection<String> getRestrictedValues();

	/**
	 * Used to get String value from configuration, that indicates the field which will be used as field query.
	 * 
	 * @return String from config
	 */
	public String getRestrictedField();
	
	/**
	 * Used to get collection of Strings which will needs to be restricted for field query.
	 *
	 * @return List of discontinued Products
	 */
	public Collection<String> getEnabledManufacturerAIDValues();
	
	public String getRestrictedManufacturerField();

}
