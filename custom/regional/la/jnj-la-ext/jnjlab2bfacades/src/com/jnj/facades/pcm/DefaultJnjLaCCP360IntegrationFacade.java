/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.facades.pcm;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.JnjClassAttributeAssignmentService;
import com.jnj.gt.pcm.integration.data.JnjGTClassificationData;
import com.jnj.gt.pcm.integration.facade.impl.DefaultJnjCCP360IntegrationFacade;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * This class overrides the logic for populating classification attributes.
 * LATAM uses language from P360 response and assign it to ProductFeature model.
 */
public class DefaultJnjLaCCP360IntegrationFacade extends DefaultJnjCCP360IntegrationFacade {

    private static final String CLASSIFICATION_SYSTEM = "pcmMedicalClassCatalog";
    private static final String CLASSIFICATION_SYSTEM_VERSION = "1.0";
    private static final String CLASSIFICATION_NAME_VERSION = "pcmMedicalClassCatalog/1.0/";
    private JnjClassAttributeAssignmentService jnjClassAttributeAssignmentService;
    private ClassificationSystemService classificationSystemService;

    private static boolean isClassificationClassPresent(final JnjGTClassificationData classificationData, final JnJProductModel jnjProductModel){
        for (CategoryModel cat : jnjProductModel.getSupercategories()) {
            if (cat instanceof ClassificationClassModel && cat.getCode().equalsIgnoreCase(classificationData.getClassificationClass())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void populateClassificationattributes(final List<JnjGTClassificationData> classificationFeatureData,
                                                    final JnJProductModel jnjProductModel) {
        if (CollectionUtils.isNotEmpty(classificationFeatureData)) {
            for (final JnjGTClassificationData classificationData : classificationFeatureData) {
                //Fetch the ClassificationClass from database and assign it as supercategory for the product, if not already present
                ClassificationClassModel classificationClass = getClassificationSystemService().getClassForCode(getClassificationSystemService().getSystemVersion(CLASSIFICATION_SYSTEM, CLASSIFICATION_SYSTEM_VERSION), classificationData.getClassificationClass());
                boolean isClassificationClassPresent=isClassificationClassPresent(classificationData, jnjProductModel);
                if (!isClassificationClassPresent) {
                    Set<CategoryModel> categories = new HashSet<>();
                    categories.addAll(jnjProductModel.getSupercategories());
                    categories.add(classificationClass);
                    jnjProductModel.setSupercategories(categories);
                    super.getModelService().save(jnjProductModel);
                }

                FeatureList featureList = super.getClassificationService().getFeatures(jnjProductModel);
                final String classificationFeatureName = classificationData.getFeatureName().toLowerCase(Locale.ENGLISH);
                String code = CLASSIFICATION_NAME_VERSION + classificationData.getClassificationClass() + "." + classificationFeatureName;
                LocalizedFeature feature = (LocalizedFeature) featureList.getFeatureByCode(code);
                boolean isNewFeature = false;
                if (null == feature) {
                    feature=addLocalizedFeature(classificationData);
                    isNewFeature = true;

                }
                if (null != feature) {
                    addLocalizedFeatureValue(jnjProductModel, classificationData, featureList, feature, isNewFeature);
                }
            }
        }

    }

    private LocalizedFeature addLocalizedFeature(final JnjGTClassificationData classificationData){
        ClassAttributeAssignmentModel classAttrAssignment = getJnjClassAttributeAssignmentService().getClassAttributeAssignment(classificationData.getFeatureName(), classificationData.getClassificationClass());
        if (null != classAttrAssignment) {
            return new LocalizedFeature(classAttrAssignment, null, Locale.of(classificationData.getLanguage()));
        }
        return null;
    }

    private void addLocalizedFeatureValue(final JnJProductModel jnjProductModel,
                                          final JnjGTClassificationData classificationData, FeatureList featureList, final LocalizedFeature feature, boolean isNewFeature) {
        if (feature != null) {
            FeatureValue value = feature.getValue(Locale.of(classificationData.getLanguage()));
            if (value != null) {
                value.setValue(classificationData.getFeatureValue());
            } else {
                value = new FeatureValue(classificationData.getFeatureValue());
                feature.addValue(value, Locale.of(classificationData.getLanguage()));
            }
            if (isNewFeature) {
                List<Feature> features = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(featureList.getFeatures())) {
                    features.addAll(featureList.getFeatures());
                }
                features.add(feature);
                featureList = new FeatureList(features);
            }
            super.getClassificationService().setFeatures(jnjProductModel, featureList);
        }
    }

    public JnjClassAttributeAssignmentService getJnjClassAttributeAssignmentService() {
        return jnjClassAttributeAssignmentService;
    }

    public void setJnjClassAttributeAssignmentService(JnjClassAttributeAssignmentService jnjClassAttributeAssignmentService) {
        this.jnjClassAttributeAssignmentService = jnjClassAttributeAssignmentService;
    }

    public ClassificationSystemService getClassificationSystemService() {
        return this.classificationSystemService;
    }

    public void setClassificationSystemService(ClassificationSystemService classificationSystemService) {
        this.classificationSystemService = classificationSystemService;
    }
}
