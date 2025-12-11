/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.core.services;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

/**
 * This class contains the methods for getting the Classification attribute assignment model for classificationAttribute
 * and classificationClass.
 *
 * @author J&J
 */
public interface JnjClassAttributeAssignmentService {

    /**
     * This method returns ClassAttributeAssignmentModel for ClassificationAttribute and ClassificationClass
     *
     * @param classificationAttribute Classification attribute code
     * @param classificationClass Classification class code
     * @return ClassAttributeAssignmentModel
     */
    public ClassAttributeAssignmentModel getClassAttributeAssignment (final String classificationAttribute, final String classificationClass);
}
