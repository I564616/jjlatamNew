/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jnj.core.services.JnJProductService;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnjGTProductLotModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
//import com.jnj.pcm.core.dto.CompletenessStatusData;


/**
 * This class is used to get information related to JnjGTProduct specific to JNJ NA requirement.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnJGTProductService extends JnJProductService
{

	/**
	 * 
	 * Checks and returns true if active user is a sales rep and product is of same division as of current user.
	 * 
	 * @param product
	 * @return boolean
	 */
	public boolean isProductDivisionSameAsUserDivision(final JnJProductModel product);

	/**
	 * 
	 * This is to check whether a product could be added into the cart.
	 * 
	 * @param product
	 * @param isIndexing
	 * @return boolean
	 *         <ul>
	 *         <li>true : Product is Saleable i.e. eligible to add to the cart</li>
	 *         <li>false :Product is not Saleable i.e. not eligible to add to the cart</li>
	 *         </ul>
	 */
	public boolean isProductSaleable(final JnJProductModel product, final boolean isIndexing);

	/**
	 * 
	 * This is to check whether a product could be added into the cart.
	 * 
	 * @param product
	 * @param currentSite
	 *           YTODO
	 * @return Error String
	 *         <ul>
	 *         <li>null : Product is Saleable i.e. eligible to add to the cart</li>
	 *         <li>message code :Product is not Saleable i.e. not eligible to add to the cart</li>
	 *         </ul>
	 */
	public String isProductSaleable(final JnJProductModel product, String currentSite);

	/**
	 * 
	 * This is to check whether a product could be browse through catalog navigation
	 * 
	 * @param product
	 * @return boolean
	 *         <ul>
	 *         <li>true : Product is Browsable</li>
	 *         <li>false :Product is not Browsable</li>
	 *         </ul>
	 */
	public boolean isProductBrowsable(final JnJProductModel product);

	/**
	 * 
	 * This is to check whether a product could be search via Search Page
	 * 
	 * @param product
	 * @return boolean
	 *         <ul>
	 *         <li>true : Product is Searchable</li>
	 *         <li>false :Product is not Searchable</li>
	 *         </ul>
	 */
	public boolean isProductSearchable(final JnJProductModel product);

	/**
	 * 
	 * This is to check whether a product could be viewed by PDP .
	 * 
	 * @param product
	 * @return boolean
	 *         <ul>
	 *         <li>true : Product is viewable via PDP</li>
	 *         <li>false :Product is not viewable via PDP</li>
	 *         </ul>
	 */
	public boolean isPDPViewable(final JnJProductModel product);

	/**
	 * 
	 * This is to get the delivery GTIN of given product.
	 * <ul>
	 * <li>In case of MDD : Delivery GTIN is the highest Code Level with a Ship Indicator = Y</li>
	 * </ul>
	 * 
	 * @param product
	 * @return JnjGTVariantProductModel
	 *         <ul>
	 */
	public JnjGTVariantProductModel getDeliveryGTIN(final ProductModel product);

	/**
	 * 
	 * This is to get the Sales GTIN of given product. *
	 * <ul>
	 * <li>In case of MDD : Sales GTIN is the highest Code Level with a Sales Indicator = Y</li>
	 * </ul>
	 * 
	 * @param product
	 * @return JnjGTVariantProductModel
	 * 
	 */
	public JnjGTVariantProductModel getSalesGTIN(final JnJProductModel product);

	/**
	 * 
	 * Returns product url .
	 * 
	 * @param product
	 * @return String
	 */
	public String getProductUrl(final ProductModel product);

	/**
	 * 
	 * This is to get product variant model based on given values.This value could be product code/GTIN/UPC number.
	 * 
	 * @param code
	 * 
	 * @return JnjGTVariantProductModel
	 *         <ul>
	 *         <li>null , if product is not available</li>
	 *         </ul>
	 */
	public ProductModel getProductByValue(final String code, final boolean ignoreUpc);

	/**
	 * This method returns the ProductModel associated with given code/ean
	 * 
	 * @param String
	 *           code or ean number
	 * @return ProductModel with same code/ean
	 */
	public ProductModel getProductByCodeOrEAN(final String code);

	/**
	 * Gets the jnj na product model by code by querying the db with admin.
	 * 
	 * @param code
	 *           the code
	 * @param catalogVersionModel
	 *           YTODO
	 * @return the product model by code
	 */
	public JnJProductModel getProductModelByCode(final String code, CatalogVersionModel catalogVersionModel);

	/**
	 * Retrieves <code>Unit</code> based on the code.
	 * 
	 * @param code
	 * @return UnitModel
	 */
	public UnitModel getUnitByCode(final String code);

	/**
	 * Finds product's lot expiration date from the associated Lot information.
	 * 
	 * @param productModel
	 * @return String
	 */
	public Date getProductLotExpirationDate(final JnJProductModel productModel, String batchNumber);

	/**
	 * Provides eligible values for product code/ean and url for a product to be used on Order History and Order Template
	 * Detail pages for proper PDP navigation and Add to Cart of products.
	 * 
	 * Returns List having:
	 * 
	 * <ul>
	 * <li>Product code/ean at index 0</li>
	 * <li>Product url at index 1</li>
	 * </ul>
	 * 
	 * @param product
	 * @param refrenceVariant
	 * @return List<String>
	 */
	public List<String> getEligibleUrlAndCodeForOrderHistoryAndTemplate(final JnJProductModel product,
			final JnjGTVariantProductModel refrenceVariant);

	/**
	 * Gets the jnj na product model by code by querying the db with admin for Order Template
	 * 
	 * @param code
	 *           the code
	 * @return the product model by code
	 */
	public JnJProductModel getProductForOrderTemplate(final String code, final String sourceId);

	/**
	 * Retrieves all CPSIA details for the products present in current session catalog.
	 * 
	 * @param sortBy
	 * @return Collection<JnjGTProductCpscDetailModel>
	 */
	public Collection<JnjGTProductCpscDetailModel> getConsumerProductsCpsia(final String sortBy);

	/**
	 * This method fetches the CPSIA data for the supplied product
	 * 
	 * @param productId
	 * @return JnjGTProductCpscDetailModel
	 */
	public JnjGTProductCpscDetailModel getCpsiaDataForProduct(final String productId);

	/**
	 * Relays call to the DAO layer to get all product based variants having either Ship or Sell Unit indicator set.
	 * 
	 * @param productPk
	 * @return Collection<JnjGTVariantProductModel>
	 */
	public Collection<JnjGTVariantProductModel> getVariantsOrderedByPkgLvlCode(final String productPk);

	/**
	 * 
	 * This is to get the launch status of given product.
	 * 
	 * @param product
	 *           the product
	 * @param onlyActive
	 *           boolean that determines whether only active products needs to be considered
	 * @return String
	 *         <ul>
	 */
	String getLaunchStatus(final JnJProductModel product, Boolean onlyActive);

	/**
	 * 
	 * This method is used to check the completeness indicator of the product and also the missing area from the product
	 * attributes.
	 * 
	 * @param productModel
	 * @return CompletenessStatusData
	 */

	//public CompletenessStatusData getCompletenessValueIndicator(final JnJProductModel productModel);

	/**
	 * This method is used to check if the mandatory attributes are completely filled for a product
	 * 
	 * @param productModel
	 * @return boolean
	 */
	public boolean mandatoryAttributesCheck(final JnJProductModel productModel);

	/**
	 * 
	 * Retrieves all new products which have their first ship effective date or new product start date lying between 7
	 * days thirty days from the current date.
	 * 
	 * @param catalogVersionModel
	 * @return List<JnJProductModel>
	 */

	public List<JnJProductModel> getNewlyActivatedProducts(final CatalogVersionModel catalogVersionModel);

	/**
	 * Creates the Product WORKFLOW used in PCM. To create newProductWorkflow set 'newProductWorkflow' to [true]. To
	 * create updateProductWorkflow set 'newProductWorkflow' to [false].
	 * 
	 * @param productModel
	 *           the product model
	 * @param newProductWorkflow
	 *           the new product WORKFLOW
	 * @throws BusinessException
	 *            the business exception
	 */
	void createProductWorkflow(final JnJProductModel productModel, final boolean newProductWorkflow) throws BusinessException;

	/**
	 * Gets the product lot info.
	 * 
	 * @param prouctCode
	 *           the prouct code
	 * @return the product lot info
	 */
	public List<JnjGTProductLotModel> getProductLotInfo(final String prouctCode, String batchNumber);

	/**
	 * Gets the jnj na product model by upcCode by querying the database with admin.
	 * 
	 * @param upcCode
	 *           the code
	 * @param catalogVersionModel
	 * @return the product model by Upc Code
	 */
	public List<JnJProductModel> getProductModelByExactUpcCode(final String upcCode, CatalogVersionModel catalogVersionModel);

	/**
	 * This method is used to fetch the list of products for download from the EPIC category page.
	 * 
	 * @param currentSite
	 * @param accountNo
	 * @return List<JnJProductModel>
	 */
	public List<JnJProductModel> getProductsForCategory(String currentSite, String accountNo);

	/**
	 * This method is used to create the excel file to be attached in the email file that is generated at the time of the
	 * MDD catalog download functionality
	 * 
	 * @param currentSite
	 * @param catalogProductsData
	 * @param currentAccount
	 * @return excel Sheet with the product data for that catalog
	 */

	public HSSFWorkbook createMDDExportFile(List<JnJProductModel> products, final String fileName);


	/**
	 * This method is used to create the excel file to be attached in the email file that is generated at the time of the
	 * CONS catalog download functionality
	 * 
	 * @param currentSite
	 * @param catalogProductsData
	 * @param currentAccount
	 * @return excel Sheet with the product data for that catalog
	 */

	public HSSFWorkbook createCONSExportFile(List<JnjGTProductData> catalogProductsData, String currentAccount,
			final String fileName);

	/**
	 * This method is used to fetch the list of products for download from the EPIC category page.
	 * 
	 * @return List<JnJProductModel>
	 */
	public List<JnJProductModel> getAllProductsForPCM(CatalogModel catalogModel);


	/**
	 * This method is used to delete old export file of MDD.
	 * 
	 * @return List<JnJProductModel>
	 */
	public void deleteOldMDDExportFile(String filePath);


	/**
	 * 
	 * This is to get MDD product variant model based on given code.
	 * 
	 * @param code
	 * 
	 * @return JnjGTVariantProductModel
	 * 
	 */
	public ProductModel getMDDDeliveryVariantByProdCode(final String code);


	/**
	 * 
	 * This is to get MDD decription in case the name is empty.
	 * 
	 * @param name
	 * 
	 * @return JnJProductModel
	 * 
	 */
	public String getProductName(final JnJProductModel JnJProductModel);

	public String getMediaURLForMDDExport();
	
	public String getMediaURLForPDFMDDExport();
	
	/**
	 * Validate product's mfg code for house order.
	 *
	 * @param product
	 *           the product
	 * @return true, if successful
	 */
	public boolean validateProductMfgForHouseOrder(final JnJProductModel product);

	/**
	 * Filter the list with the obsolete products
	 * @param selectedProductIds
	 * @return
	 */
    public StringBuilder getObsoleteProductList(String[] selectedProductIds);

	/**
	 * @param code
	 * @param catalogVersionModel
	 * @return
	 */
	public boolean isObsoleteProduct(final String code,final CatalogVersionModel catalogVersionModel);

	/**
	 * AFFG-1
	 * Changes for Products also bought Together Carousel
	 * @param productCode
	 * @param catalogVersion
	 * @return
	 */
	List<JnJProductModel> getProductsBoughtTogether(final String productCode, final CatalogVersionModel catalogVersion);

	String findProductPK(final String productCode, final CatalogVersionModel catalogVersion);
}
