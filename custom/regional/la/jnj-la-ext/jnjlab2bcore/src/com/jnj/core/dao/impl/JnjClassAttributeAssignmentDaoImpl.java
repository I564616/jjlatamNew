/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.core.dao.impl;

import com.jnj.core.dao.JnjClassAttributeAssignmentDao;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides implementation method to retrieve classification attribute assignment model
 * based on classification attribute and classification class
 */
public class JnjClassAttributeAssignmentDaoImpl extends AbstractItemDao implements JnjClassAttributeAssignmentDao {

    private static final Logger LOG = Logger.getLogger(JnjClassAttributeAssignmentDaoImpl.class);

    private static final String CLASS_ATTRIBUTE_ASSIGNMENT_BY_CODES ="SELECT {a.pk} FROM {"+ClassAttributeAssignmentModel._TYPECODE+" AS a JOIN "+ ClassificationClassModel._TYPECODE+" AS b ON {a.classificationClass}={b.pk}" +
            " JOIN ClassificationAttribute AS c ON {a.classificationAttribute}={c.pk}} WHERE {c.code}=?classificationAttribute" +
            " AND {b.code}=?classificationClass";
    private static final String CLASSIFICATION_CLASS="classificationClass";
    private static final String CLASSIFICATION_ATTR="classificationAttribute";

    @Override
    public ClassAttributeAssignmentModel getClassAttributeAssignmentModel (final String classificationAttribute, final String classificationClass) {

        ClassAttributeAssignmentModel result = null;

        try {
            final Map queryParams = new HashMap();
            queryParams.put(CLASSIFICATION_ATTR, classificationAttribute);
            queryParams.put(CLASSIFICATION_CLASS, classificationClass);

            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(CLASS_ATTRIBUTE_ASSIGNMENT_BY_CODES);
            fQuery.addQueryParameters(queryParams);

            result = getFlexibleSearchService().searchUnique(fQuery);
        }
        catch (final ModelNotFoundException | AmbiguousIdentifierException exception) {
            LOG.error("Error while executing query - "+ CLASS_ATTRIBUTE_ASSIGNMENT_BY_CODES +" Details of error ", exception);
        }
        return result;
    }
}
