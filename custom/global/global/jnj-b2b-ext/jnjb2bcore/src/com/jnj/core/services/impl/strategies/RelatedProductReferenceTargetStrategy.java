/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.services.impl.strategies;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commerceservices.strategies.ProductReferenceTargetStrategy;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * Strategy to return related product reference
 */

public class RelatedProductReferenceTargetStrategy implements ProductReferenceTargetStrategy
{
	@Override
	public ProductModel getTarget(final ProductModel sourceProduct, final ProductReferenceModel reference)
	{
		return reference.getTarget();
	}



}