/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.converters.populator;

import com.jnj.core.constants.Jnjb2bCoreConstants;

import de.hybris.platform.commercefacades.product.ProductOption;

import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.model.ProductDocumentsModel;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.services.JnjSalesOrgCustService;
import com.jnj.facades.data.JnjGTMddSpecificationData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.data.ProductDocumentsData;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;

import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import com.jnj.la.core.services.JnjLoadTranslationService;

import de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.jnj.facades.product.JnjLatamProductFacade;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

public class JnjLaOrderEntryProductPopulator extends JnjGTOrderEntryProductPopulator {
    protected static final String EMPTY_FIELD = "";
    private static final String OTHER_ATTRIBUTES = "OTHER ATTRIBUTES";
    private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC);
    private static final Integer MAX_PRODUCT_COUNT = 3;
    private static final String CART_PRODUCT_REFERENCE_SIZE = "cart.product.references.size";

    private static final Logger LOG = Logger.getLogger(JnjLaOrderEntryProductPopulator.class);


    @Autowired
    protected JnjLatamProductFacade jnjLatamProductFacade;
    @Autowired
	private ConfigurationService configurationService;
    
    @Autowired
    protected CommonI18NService commonI18NService;
    @Autowired
    private JnjLoadTranslationService jnjLoadTranslationService;
    @Autowired
    private JnJProductService jnjProductService;
    private Populator<ProductModel, ProductData> productGalleryImagesPopulator;

    @Autowired
    private SessionService sessionService;

    @Autowired
    protected JnjSalesOrgCustService jnjSalesOrgCustService;

    /**
     * Sets the Franchise and Division values, based on the Level 2 and level 1 super categories
     * associated with the product.
     *
     * @param source
     * @param mddspecificationData
     */
    private static void setFranchiseAndDivision(final JnJProductModel source, final JnjGTMddSpecificationData mddspecificationData) {
        for (final CategoryModel levelTwoCategory : source.getSupercategories()) {
            mddspecificationData.setDivision(levelTwoCategory.getName());
            if (CollectionUtils.isNotEmpty(levelTwoCategory.getSupercategories())) {
                mddspecificationData.setFranchise(levelTwoCategory.getSupercategories().get(0).getName());
            }
            if (levelTwoCategory.getSupercategories() != null) {
                break;
            }
        }
    }

    private static boolean checkDimension(Double dimension) {
        return (null != dimension
                && Double.compare(dimension.doubleValue(), 0) != 0);
    }

    private static void setVolumeData(final JnjLaProductData target, final JnjUomConversionModel jnJUomConversionModel, final DecimalFormat decimalFormat ){
        int countForDimension=0;
        if (checkDimension(jnJUomConversionModel.getHeight())) {
            target.setHeight(decimalFormat.format(jnJUomConversionModel.getHeight()));
            countForDimension++;
        }
        if (checkDimension(jnJUomConversionModel.getLength())) {
            target.setLength(decimalFormat.format(jnJUomConversionModel.getLength()));
            countForDimension++;
        }
        if (checkDimension(jnJUomConversionModel.getWidth())) {
            target.setWidth(decimalFormat.format(jnJUomConversionModel.getWidth()));
            countForDimension++;
        }
        if (countForDimension==3) {
            target.setVolume(decimalFormat.format(jnJUomConversionModel.getLength()
                    * jnJUomConversionModel.getWidth()
                    * jnJUomConversionModel.getHeight()));
        }
    }

    @Override
    public void populate(final ProductModel productModel, final ProductData productData) {
        super.populate(productModel, productData);
        final List<ProductReferenceTypeEnum> referenceType = new ArrayList();
        List<ProductReferenceData> productReferences = null;
        if (productData instanceof JnjLaProductData) {
            populateJnjProductSpecification(productModel, productData);
            LOG.info("JnjLaProductData :::"+ productData.getCode());
            populateProductStatusCode(productModel, productData);
            referenceType.add(ProductReferenceTypeEnum.SUBSTITUTE_PRODUCTS);
            try {
                productReferences = jnjLatamProductFacade.getSubstituteProductReferences(productModel.getCode(), referenceType, PRODUCT_OPTIONS, configurationService
                        .getConfiguration()
                        .getInteger(CART_PRODUCT_REFERENCE_SIZE, MAX_PRODUCT_COUNT));

                productData.setProductReferences(productReferences);
            } catch (final UnknownIdentifierException exe) {
                LOG.error("The specified product did not find: ", exe);
            } catch(final Exception e) {
                LOG.error("Error while fetching the substitute product: ", e);
            }
        }
        getProductGalleryImagesPopulator().populate(productModel, productData);
    }

    private static void populateUomData(final JnJLaProductModel source, final JnjLaProductData target,final JnjUomDTO custDelUOM) {
        if (null != custDelUOM) {
            target.setDeliveryUnit(custDelUOM.getUnitDimension());
            target.setQuantity(Integer.valueOf(custDelUOM.getSalesUnitsCount()));
        } else {
            target.setDeliveryUnit((source.getDeliveryUnitOfMeasure() != null) ? source.getUnit().getName() : null);
            Integer numeratorD = null;
            Integer numeratorS = null;
            Integer quantity = null;
            if (null != source.getNumeratorDUOM()) {
                numeratorD = Integer.valueOf(source.getNumeratorDUOM());
            }
            if (null != source.getNumeratorSUOM()) {
                numeratorS = Integer.valueOf(source.getNumeratorSUOM());
            }
            if (null != numeratorD && null != numeratorS) {
                quantity = Integer.valueOf(numeratorD.intValue() / numeratorS.intValue());
            }
            if (null != quantity) {
                target.setQuantity(quantity);
            }
        }
    }

    private static void populateMiscData(final JnJLaProductModel source, final JnjLaProductData target){
        target.setStatus((source.getModStatus() != null) ? source.getModStatus().toString() : EMPTY_FIELD);
        target.setSalesUnit((source.getUnit() != null) ? source.getUnit().getName() : null);
        target.setSalesUnitCode((source.getUnit() != null) ? source.getUnit().getCode() : null);
        target.setDeliveryUnitCode(
                (source.getDeliveryUnitOfMeasure() != null) ? source.getDeliveryUnitOfMeasure().getCode() : null);
        target.setBaseUnit((source.getBaseUnitOfMeasure() != null) ? source.getBaseUnitOfMeasure().getName() : null);
        target.setBaseUnitCode((source.getBaseUnitOfMeasure() != null) ? source.getBaseUnitOfMeasure().getCode() : null);
        target.setNumeratorDUOM(source.getNumeratorDUOM());
        target.setNumeratorSUOM(source.getNumeratorSUOM());
        target.setDiscontinue(source.getDisContinue());
        if (StringUtils.isNotEmpty(source.getNumeratorDUOM()) && StringUtils.isNotEmpty(source.getNumeratorSUOM())) {
            final Integer multiplicity = Integer
                    .valueOf(Integer.parseInt(source.getNumeratorDUOM()) / Integer.parseInt(source.getNumeratorSUOM()));
            target.setMultiplicity(multiplicity);
        }
        target.setEan(source.getEan());
        target.setMedicalSpeciality(source.getMedicalSpecialty());
        target.setStockingtype(source.getStockingType());
    }

    private static void populateDimensionData(final JnJLaProductModel source, final JnjLaProductData target, final DecimalFormat decimalFormat){
        for (final JnjUomConversionModel jnJUomConversionModel : source.getUomDetails()) {
            final String unit = jnJUomConversionModel.getUOM().getCode();

            if (null != source.getDeliveryUnitOfMeasure() && unit.equalsIgnoreCase(source.getDeliveryUnitOfMeasure().getCode())) {

                target.setShipWeight(jnJUomConversionModel.getShippingWeight());
                target.setShippingUnit(jnJUomConversionModel.getShippingUnit());

                setVolumeData(target,jnJUomConversionModel,decimalFormat);

                if (null != jnJUomConversionModel.getVolumeUnit()) {
                    target.setVolumeUnit(jnJUomConversionModel.getVolumeUnit().getCode());
                }
            }
        }
    }

    private void populateJnjProductSpecification(final ProductModel productModel, final ProductData productData) {
        final JnJLaProductModel source = (JnJLaProductModel) productModel;
        final JnjLaProductData target = (JnjLaProductData) productData;

        final DecimalFormat decimalFormat = new DecimalFormat("0.0");
        final JnjUomDTO custDelUOM = jnjLoadTranslationService.getCustDelUomMapping((JnJProductModel) productModel);
        populateUomData(source,target,custDelUOM);
        populateP360Data(source, target);

        final JnjGTMddSpecificationData mddSpecificationData = new JnjGTMddSpecificationData();
        setFranchiseAndDivision(source, mddSpecificationData);
        target.setMddSpecification(mddSpecificationData);

        populateMiscData(source,target);
        populateDimensionData(source,target,decimalFormat);

        target.setDisplayPrice(Boolean.valueOf(isDisplayProductPrice(productModel)));
        target.setOriginCountry((source.getOriginCountry() != null) ? source.getOriginCountry().getName() : null);
        target.setSector(source.getSector());
        final Map<String, String> dataSheetNames = new HashMap<>();
        final List<MediaModel> dataSheets = (List<MediaModel>) source.getData_sheet();
        if (CollectionUtils.isNotEmpty(dataSheets)) {
            for (final MediaModel mediaModel : dataSheets) {
                if (mediaModel.getRealFileName().lastIndexOf(Jnjb2bCoreConstants.CONST_DOT) != -1) {
                    dataSheetNames.put(mediaModel.getCode(), mediaModel.getRealFileName().substring(0,
                            mediaModel.getRealFileName().lastIndexOf(Jnjb2bCoreConstants.CONST_DOT)));
                }
            }
            target.setDataSheets(dataSheetNames);
        }
        target.setCatalogId(source.getCatalogId());
        target.setEcommerceFlag(source.getEcommerceFlag());
    }

    /**
     * Evaluates if the product price has to be displayed or not depending on the level 1 category parameter.
     *
     * @param product
     * @return boolean
     */
    private static boolean isDisplayProductPrice(final ProductModel product) {
        for (final CategoryModel superCategory : product.getSupercategories()) {
            if (superCategory.getShowProductPrice() != null && !superCategory.getShowProductPrice()) {
                return false;
            }
        }
        return true;
    }

    /**
     * populates data from P360 for the product
     *
     * @param source the product model
     * @param target the product data
     */
    private void populateP360Data(final JnJProductModel source, final JnjLaProductData target) {

        Locale locale = LocaleUtils.toLocale(commonI18NService.getCurrentLanguage().getIsocode());

        target.setFranchiseName(source.getFranchiseName());
        target.setDescriptionText(source.getDescriptionText(locale));
        if (StringUtils.isNotEmpty(source.getDescription(locale))) {
            target.setDescription(source.getDescription(locale));
        }
        if (StringUtils.isNotEmpty(source.getShortOverview(locale))) {
            target.setShortOverview(source.getShortOverview(locale));
        }
        target.setBrandDescription(source.getAboutTheBrand(locale));
        setClassifications(source, target);

        if (CollectionUtils.isNotEmpty(source.getProductDocumentlist())) {
            List<ProductDocumentsData> documents = new ArrayList<>();
            for (ProductDocumentsModel document : source.getProductDocumentlist()) {
                ProductDocumentsData documentData = new ProductDocumentsData();
                documentData.setName(document.getName());
                documentData.setUrlLink(document.getUrlLink());
                documents.add(documentData);
            }
            target.setDocumentsList(documents);
        }
    }

    /**
     * populates classification attributes for the product
     *
     * @param productModel the product model
     * @param target       the product data
     */
    public void setClassifications(final JnJProductModel productModel, final JnjLaProductData target) {
        final List<ProductFeatureModel> features = productModel.getFeatures();
        final Map<String, Map<String, String>> rootFeatures = new HashMap<>();
        final Map<String, Map<String, String>> sortedRootFeatures = new HashMap<>();
        final Map<String, Object> featureMap = new HashMap<>();

        for (final ProductFeatureModel feature : features) {
            final Map<String, String> newFeatures = new HashMap<>();

            if (checkFeatureLanguage(feature) && checkFeatureVisibility(feature) ) {
                String featureRootName;
                String featureName;
                if (feature.getClassificationAttributeAssignment().getClassificationAttribute().getName().contains(Jnjb2bCoreConstants.SYMBOL_UNDERSCORE)) {
                    featureRootName = feature.getClassificationAttributeAssignment().getClassificationAttribute().getName()
                            .split(Jnjb2bCoreConstants.SYMBOL_UNDERSCORE)[0];
                    featureName = feature.getClassificationAttributeAssignment().getClassificationAttribute().getName()
                            .split(Jnjb2bCoreConstants.SYMBOL_UNDERSCORE)[1];
                } else {
                    featureRootName = OTHER_ATTRIBUTES;
                    featureName = feature.getClassificationAttributeAssignment().getClassificationAttribute().getName();
                }
                getRootFeatures(rootFeatures, feature, newFeatures, featureRootName, featureName);
            }
        }
        for (final Map.Entry<String, Map<String, String>> unSortedRootFeatures : rootFeatures.entrySet()) {
            final String featureKey = unSortedRootFeatures.getKey();
            final Map<String, String> featureValues = unSortedRootFeatures.getValue();
            final Map<String, String> sortedFeatures = featureValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            sortedRootFeatures.put(featureKey, sortedFeatures);

        }
        target.setStructuredClassifications(sortedRootFeatures);

        for (final ProductFeatureModel feature : features) {
            if (null != feature.getClassificationAttributeAssignment()
                    && null != feature.getClassificationAttributeAssignment().getClassificationAttribute()
                    && null != feature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()) {
                featureMap.put(feature.getClassificationAttributeAssignment().getClassificationAttribute().getCode(), feature);
            }
        }
        final Map<String, Object> sortedFeatureMap = featureMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        target.setClassAttributeFeatureMap(sortedFeatureMap);
    }

    /**
     * Populates Root Features of Classification
     *
     * @param rootFeatures    the root features map
     * @param feature         the feature
     * @param newFeatures     the new features map
     * @param featureRootName the feature root name
     * @param featureName     the feature name
     */
    public void getRootFeatures(final Map<String, Map<String, String>> rootFeatures, final ProductFeatureModel feature,
                                final Map<String, String> newFeatures, final String featureRootName, final String featureName) {
        final String featureValue = (String) feature.getValue();

        if (rootFeatures.containsKey(featureRootName)) {
            final Map<String, String> existingFeatures = rootFeatures.get(featureRootName);
            existingFeatures.put(featureName, featureValue);
            rootFeatures.get(featureRootName).putAll(existingFeatures);
        } else {
            newFeatures.put(featureName, featureValue);
            rootFeatures.put(featureRootName, newFeatures);
        }
    }

    private void populateProductStatusCode(final ProductModel productModel, final ProductData productData) {
        final JnJLaProductModel source = (JnJLaProductModel) productModel;
        final JnjLaProductData target = (JnjLaProductData) productData;
        String productStatusCode = null;
        List<JnJProductSalesOrgModel> salesOrgList = source.getSalesOrgList();
        if (CollectionUtils.isEmpty(salesOrgList)) {
            productStatusCode = source.getProductStatusCode();
        } else {
            Map<String, String> sectorSalesOrgMapping = getSessionService()
                    .getAttribute(Jnjb2bCoreConstants.Order.B2BUNIT_SALESORG_MAP);
            if (MapUtils.isEmpty(sectorSalesOrgMapping)) {
                sectorSalesOrgMapping = getJnjSalesOrgCustService().getSectorAndSalesOrgMapping();
            }
            String sector = source.getSector();
            String productSector = StringUtils.isBlank(sector) ? StringUtils.EMPTY : sector.toUpperCase(Locale.getDefault());
            String salesOrg = sectorSalesOrgMapping.get(productSector);
            JnJProductSalesOrgModel salesOrgModel = salesOrgList.stream().filter(prodSalesOrg -> StringUtils.equalsIgnoreCase(salesOrg, prodSalesOrg.getSalesOrg())).findFirst().orElse(null);
            if (Objects.nonNull(salesOrgModel)) {
                productStatusCode = salesOrgModel.getStatus();
            }
            else {
                productStatusCode = source.getProductStatusCode();
            }
        }
        target.setProductStatusCode(productStatusCode);
    }

    public Populator<ProductModel, ProductData> getProductGalleryImagesPopulator() {
        return productGalleryImagesPopulator;
    }

    public void setProductGalleryImagesPopulator(Populator<ProductModel, ProductData> productGalleryImagesPopulator) {
        this.productGalleryImagesPopulator = productGalleryImagesPopulator;
    }

    private boolean checkFeatureLanguage(final ProductFeatureModel feature) {
        return (feature.getLanguage() != null && feature.getLanguage().getIsocode().equals(commonI18NService.getCurrentLanguage().getIsocode()));
    }

    private static boolean checkFeatureVisibility(final ProductFeatureModel feature){
		return (null != feature.getClassificationAttributeAssignment()
		&& feature.getClassificationAttributeAssignment().getVisibility() == ClassificationAttributeVisibilityEnum.VISIBLE);
	}

    public SessionService getSessionService() {
        return sessionService;
    }

    public JnjSalesOrgCustService getJnjSalesOrgCustService() {
        return jnjSalesOrgCustService;
    }
}
