/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.core.services.impl;

import com.jnj.core.dao.JnjClassAttributeAssignmentDao;
import com.jnj.core.services.JnjClassAttributeAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JnjClassAttributeAssignmentServiceImpl implements JnjClassAttributeAssignmentService {

    private static final Logger LOG = Logger.getLogger(JnjClassAttributeAssignmentServiceImpl.class);

    @Autowired
    protected JnjClassAttributeAssignmentDao jnjClassAttributeAssignmentDao;


    @Override
    public ClassAttributeAssignmentModel getClassAttributeAssignment(final String classificationAttribute,final String classificationClass) {

        final String methodName = "getClassAttributeAssignmentModel()";
        ClassAttributeAssignmentModel classAttributeAssignmentModel = null;
        try
        {
            classAttributeAssignmentModel = getJnjClassAttributeAssignmentDao().getClassAttributeAssignmentModel(classificationAttribute, classificationClass);
        }
        catch (final Exception ex)
        {
            LOG.error("Error while executing method "+methodName +" Details of error -", ex);
        }

        return classAttributeAssignmentModel;
    }

    public JnjClassAttributeAssignmentDao getJnjClassAttributeAssignmentDao() {
        return jnjClassAttributeAssignmentDao;
    }

    public void setJnjClassAttributeAssignmentDao(JnjClassAttributeAssignmentDao jnjClassAttributeAssignmentDao) {
        this.jnjClassAttributeAssignmentDao = jnjClassAttributeAssignmentDao;
    }
}
