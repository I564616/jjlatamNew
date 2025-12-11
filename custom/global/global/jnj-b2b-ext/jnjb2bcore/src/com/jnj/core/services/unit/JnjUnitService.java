/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.unit;

import de.hybris.platform.core.model.product.UnitModel;


/**
 * The interface for JnjUnitServiceImpl for UOM related operations.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjUnitService
{
	UnitModel getUnit(final String unitCode);
}
