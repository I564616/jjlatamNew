/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 * /


/**
 * Global class for all Jnjb2bbrowseandsearch constants. You can add global constants for your extension into this
 * class.
 */

package com.jnj.b2b.browseandsearch.constants;
public final class Jnjb2bbrowseandsearchConstants extends GeneratedJnjb2bbrowseandsearchConstants
{
	public static final String EXTENSIONNAME = "jnjb2bbrowseandsearch";
	public static final String SITE_NAME = "JNJ_SITE_NAME";
	public static final String MDD = "MDD";
	public static final String PCM = "PCM";
	public static final String EXPORT_EMAIL_ATTACHMENT_PATH_KEY = "export.email.attachment.path";
	public static final String DELETE_FILE = "deleteFile";
	public static final String EXPORT_EMAIL_ATTACHMENT_FILE_NAME = "attachmentFileName";
	
	
	private Jnjb2bbrowseandsearchConstants()
	{
		super();
		assert false;
	}

	public interface PLP
	{
		public static final String SEARCH_GROUPS = "storefront.search.group.results";
		public static final String ROOT_CATEGORY_CODE = "Categories";
		public static final String SEARCH_RESULT = "Search Results";
		public static final String DEFUALT_ACTIVE = ":relevance:status:ACTIVE";
		public static final String ONLY_ACTIVE = "relevance:status:ACTIVE";
		public static final String PLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD = "plp.pdfAndExcel.download.productThreshold";
		public static final String SLP_PDF_AND_EXCEL_DOWNLOAD_PRODUCT_THRESHOLD = "slp.pdfAndExcel.download.productThreshold";
	}

	public interface Product
	{
		public static final String DELIVERY_SALES_GTIN_UPDATE_TARGET_VERSION = "jnj.na.update.gtin.target.catalog.version";
		public static final String PRODUCT_GET_CONTRACT_PRICE_QUANTITY = "jnj.na.product.get.price.quantity";
		public static final String JNJ_UNIT_TYPE = "jnjGTUnit";
		public static final String CONSUMER_ACTIVE_PRODUCT_STATUS_KEY = "jnj.na.product.status.active";
		public static final String CONSUMER_INACTIVE_PRODUCT_STATUS_KEY = "jnj.na.product.status.inactive";
		public static final String SEARCH_RES_DOWNLOAD_FLAG = "isSearchResDownload";
		public static final String NON_REMAINING_PRO_CODE = "000000";
	}

	public interface Login
	{
		public static final String ORDERING_RIGHTS = "orderingRights";
		public static final String SHOW_CHANGE_ACCOUNT = "showChangeAccount";
	}
	// implement here constants used by this extension

	public interface PDP
	{
		public static final String SITE_LOGO_PATH_PROPERTY = "siteLogoPath";
		public static final String SITE_LOGO_PATH1_PROPERTY = "siteLogoPath1";
		public static final String JNJ_PRODUCT_IMAGE_URL_PROPERTY = "jnjProductImageUrl";
		public static final String PRODUCT_IMAGE_PROPERTY = "ProductImage";
		public static final String FRANCHISE_LOGO_PROPERTY = "franchiseLogo";
		public static final String JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY = "productDocumentsList";
		public static final String PDF_FILE_SUFFIX = "_JnJ";
		public static final String PDF_FILE_TEMPORARY_SUFFIX = "certificate";
		public static final String PDF_FILE_TYPE = ".pdf";
		public static final String PRODUCT_DATA_PROPERTY = "productData";
		public static final String IS_MDD_PROPERTY = "isMDDSite";
		public static final String SELL_QUANTITY_PROPERTY = "sellQty";
		public static final String SHIP_QUANTITY_PROPERTY = "shipQty";
		public static final String PRODUCT_CODE_PROPERTY = "productCode";
		public static final String GTIN_PROPERTY = "gtin";
		public static final String UPC_PROPERTY = "upc";
		public static final String SHORT_OVER_VIEW_PROPERTY = "shortOverView";
		public static final String ABOUT_BRAND_TAB_PROPERTY = "AboutTheBrand";
		public static final String PRODUCT_DETAILS_PROPERTY = "productDetails";
		public static final String ADDITIONAL_INFORMATION_PROPERTY = "additionalInformation";
		public static final String PRODUCT_SPECIFICATION_PROPERTY = "productSpecification";
		public static final String STATUS_PROPERTY = "status";
		public static final String UNIT_MEASURE_PROPERTY = "unitMeasure";
		public static final String DELIVERY_UNIT_MEASURE_PROPERTY = "deliveryUnitMeasure";
		public static final String DEPTH_PROPERTY = "depth";
		public static final String HEIGHT_PROPERTY = "height";
		public static final String WIDTH_PROPERTY = "width";
		public static final String VOLUME_PROPERTY = "volume";
		public static final String WEIGHT_PROPERTY = "weight";
		public static final String DIVISION_PROPERTY = "division";
		public static final String PRICE_PROPERTY = "price";
		public static final String INFINITY_PROPERTY = "infinity";
		public static final String NAN_PROPERTY = "nan";

		public static final String PRODUCT = "product";
		public static final String PRODUCT_DETAIL_PDF_PATH = "product.detail.pdf.vm.path";
		public static final String PRODUCT_DETAIL_PDF_VM = "productDetails.vm";
		public static final String SELL_QTY =  "popup.product.sellQty";
		public static final String SHIP_QTY ="popup.product.shipQty";
		public static final String PRODUCT_CODE ="product.detail.basic.productCode";
		public static final String GTIN ="product.detail.basic.gtin";
		public static final String UPC ="product.detail.basic.upc";
		public static final String SHORT_OVER_VIEW ="product.detail.shortOverviewName";
		public static final String ABOUT_BRAND_TAB ="product.detail.aboutTheBrandTab";
		public static final String PRODUCT_DETAILS_TAB ="product.detail.productDetailsTab";
		public static final String ADDITIONAL_INFORMATION_TAB="product.detail.additionalInformationTab";
		public static final String PRODUCT_SPECIFICATION="product.detail.product.specifications";
		public static final String DIVISION="product.detail.mddSpecification.division";
		public static final String STATUS="product.detail.specification.status";
		public static final String UNIT_MEASURE="product.detail.mddSpecification.unitOfMeasure";
		public static final String DELIVERY_UNIT_MEASURE="product.detail.mddSpecification.deliveryUnitOfMeasure";
		public static final String DEPTH="product.detail.mddSpecification.depth";
		public static final String HEIGHT="product.detail.mddSpecification.height";
		public static final String WIDTH="product.detail.mddSpecification.width";
		public static final String VOLUME="product.detail.mddSpecification.volume";
		public static final String WEIGHT="product.detail.mddSpecification.weight";
		public static final String PRICE="product.detail.product.price";
		public static final String INFINITY="product.detail.product.infinity";
		public static final String NAN="product.detail.product.nan";

		public static final String VELOCITY_ENGINE_FILE_RESOURCE_LOADER_CLASS = "file.resource.loader.class";
		public static final String VELOCITY_ENGINE_FILE_RESOURCE_LOADER_PATH = "file.resource.loader.path";
		public static final String VELOCITY_ENGINE_FILE_RESOURCE_LOADER_CACHE = "file.resource.loader.cache";
		public static final String VELOCITY_ENGINE_FILE_RESOURCE_LOADER_MODIFICATION_CHECK_INTERVAL = "file.resource.loader.modificationCheckInterval";
		public static final String VELOCITY_ENGINE_LIBRARY = "org.apache.velocity.runtime.resource.loader.FileResourceLoader";

	}
}
