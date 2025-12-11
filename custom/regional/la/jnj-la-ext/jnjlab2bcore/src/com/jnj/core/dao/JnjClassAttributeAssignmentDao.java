/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.core.dao;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

/**
 * This interface provides method to retrieve classification attribute assignment model
 * based on classification attribute and classification class
 */
public interface JnjClassAttributeAssignmentDao {

    /**
     * This method returns ClassAttributeAssignmentModel for ClassificationAttribute and ClassificationClass
     *
     * @param classificationAttribute
     * @param classificationClass
     * @return ClassAttributeAssignmentModel
     */
    public ClassAttributeAssignmentModel getClassAttributeAssignmentModel (final String classificationAttribute, final String classificationClass);

}
