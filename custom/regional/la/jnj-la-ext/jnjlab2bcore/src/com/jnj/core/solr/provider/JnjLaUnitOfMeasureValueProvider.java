/*
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 * @author prodri92
 */

package com.jnj.core.solr.provider;

import com.jnj.core.model.JnJProductModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class JnjLaUnitOfMeasureValueProvider extends JnjUnitOfMeasureValueProvider {

    private static final String DEFAULT_UNIT = "pieces";
    private static final String UNIT_OF_MEASURE_FORMAT = "%s_%d_%s_%s";
    private static final String NON_JNJ_PRODUCT_MODEL_ITEM = "Cannot evaluate units for non-JnJProductModel item";

    @Override
    public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model) throws FieldValueProviderException {
        if (model instanceof JnJProductModel) {
            return getFieldValues(indexConfig, indexedProperty, (JnJProductModel) model);
        } else {
            throw new FieldValueProviderException(NON_JNJ_PRODUCT_MODEL_ITEM);
        }
    }

    private Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final JnJProductModel model) {
        final JnJProductModel productModel = model;
        final Collection fieldValues = new ArrayList();
        if (indexedProperty.isLocalized()) {
            for (final LanguageModel language : indexConfig.getLanguages()) {
                final Locale locale = this.i18nService.getCurrentLocale();
                try {
                    this.i18nService.setCurrentLocale(commonI18NService.getLocaleForLanguage(language));

                    final String deliveryUnit = getUnitName(productModel.getDeliveryUnitOfMeasure());
                    final String salesUnit = getUnitName(productModel.getUnit());
                    final String baseUnit = getUnitCode(productModel.getBaseUnitOfMeasure());
                    final int multipleOf = getSaleItems(productModel);

                    final String unitOfMeasure = String.format(UNIT_OF_MEASURE_FORMAT, salesUnit, multipleOf, deliveryUnit, baseUnit);

                    fieldValues.addAll(createFieldValue(unitOfMeasure, indexedProperty, language));

                } finally {
                    this.i18nService.setCurrentLocale(locale);
                }
            }
        } else {
            fieldValues.addAll(createFieldValue(null, indexedProperty, null));
        }

        return fieldValues;
    }

    private String getUnitName(final UnitModel unit) {
        if (unit != null) {
            return unit.getName();
        } else {
            return DEFAULT_UNIT;
        }
    }

    private String getUnitCode(final UnitModel unit) {
        if (unit != null) {
            return unit.getCode();
        } else {
            return DEFAULT_UNIT;
        }
    }

}