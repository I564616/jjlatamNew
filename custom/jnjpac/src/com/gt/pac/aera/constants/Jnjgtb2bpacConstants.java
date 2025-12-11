/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gt.pac.aera.constants;

import com.amazonaws.regions.Regions;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * Global class for all Jnjgtb2bpac constants. You can add global constants for your extension into this class.
 */
public final class Jnjgtb2bpacConstants extends GeneratedJnjgtb2bpacConstants
{
	public static final String EXTENSIONNAME = "jnjgtb2bpac";
	public static final String COMPANY = "Company";
	public static final String ORDERTYPE = "Order Type";
	public static final String ORDERNUMBER = "Order Number";
	public static final String LINENUMBER = "Line Number";
	public static final String SCHEDLINENUMBER = "Sched Line Number";
	public static final String CATALOGCODE = "Catalog Code";
	public static final String RECOMM_DELIVERY_DATE = "Recommended Deliv Date";
	public static final String DATA_SOURCE = "Data Source";
	public static final String OPEN_CONFIRMED_QUANTITY = "open confirmed quantity";
	public static final String SUB_FRANCHISE  = "Jnj Subfranchise Description Harmonized";
	public static final String TOTAL_QUANTITY_OF_REQUESTED_UNITS = "Order Qty Sales Units";
	public static final String AMOUNT_PENDING_DELIVERY = "Open Order Qty";
	public static final String QUANTITY_PENDING_STOCK = "Current Bo Qty";


	/**
	 * Comma separated list of cron jobs which will be run after successful execution of PAC HIVE Daily job which sets
	 * estimated delivery date on orders.
	 */
	public static final String PAC_AERA_DAILY_EMAIL_CRON_JOBS = "pac.aera.daily.email.cron.jobs";

	/**
	 * URL of AERA PAC HIVE API which when called returns an id of an Amazon S3 Bucket containing daily JSON files with
	 * estimated delivery dates for backorders.
	 */
	public static final String PAC_AERA_DAILY_UPDATE_URL = "pac.aera.daily.update.api.url";

	/**
	 * URL of AERA PAC HIVE API which when called returns an id of an Amazon S3 Bucket containing real-time (each 15-30 min)
	 * JSON files with estimated delivery dates for backorders.
	 */
	public static final String PAC_AERA_REALTIME_UPDATE_URL = "pac.aera.realtime.update.api.url";

	/**
	 * API key which can be used to access PAC HIVE AERA API (pac.aera.*.update.api.url).
	 * This key will be sent as "x-api-key" HTTP request header while calling "pac.aera.*.update.api.url".
	 */
	public static final String PAC_AMAZON_S3_ACCESS_KEY = "pac.amazon.s3.access.key";

	/**
	 * AWS S3 access key for accessing the bucket received from "pac.aera.*.update.api.url".
	 */
	public static final String PAC_AMAZON_S3_SECRET_KEY = "pac.amazon.s3.secret.key";

	/**
	 * AWS region name which can be used to access the bucket received from "pac.aera.*.update.api.url".
	 *
	 * Possible values are taken from com.amazonaws.regions.Regions: us-gov-west-1 us-east-1 us-east-2 us-west-1 us-west-2
	 * eu-west-1 eu-central-1 ap-south-1 ap-southeast-1 ap-southeast-2 ap-northeast-1 ap-northeast-2 sa-east-1 cn-north-1
	 */
	public static final String PAC_AMAZON_S3_REGION = "pac.amazon.s3.region";
	public static final String PAC_AMAZON_S3_REGION_DEFAULT = Regions.US_EAST_1.getName();

	/**
	 * Absolute path of the temporary directory on the Hybris server to which JSON files get downloaded from the AWS S3
	 * Bucket received from "pac.aera.daily.update.api.url".
	 * The value of this property must end with "/".
	 */
	public static final String PAC_PROCESSING_DAILY_FILE_PATH = "pac.amazon.s3.temp.fileprocessing.daily.path";
	//NOSONAR disable CERT, MSC03-J This line is not hardcoded, it is fallback value.
	public static final String PAC_PROCESSING_DAILY_FILE_PATH_DEFAULT = "/NFS_DATA/transfer/incoming/deposit/PACDateAutomation/";

	/**
	 * Prefix of each file which will get stored inside "pac.amazon.s3.temp.fileprocessing.daily.path".
	 */
	public static final String PAC_PROCESSING_DAILY_FILE_NAME_PREFIX = "pac.amazon.s3.temp.fileprocessing.daily.file.name.prefix";
	public static final String PAC_PROCESSING_DAILY_FILE_NAME_PREFIX_DEFAULT = "daily_";

	/**
	 * Absolute path of the temporary directory on the Hybris server to which JSON files get downloaded from the AWS S3
	 * Bucket received from "pac.aera.realtime.update.api.url".
	 * The value of this property must end with "/".
	 */
	public static final String PAC_PROCESSING_REALTIME_FILE_PATH = "pac.amazon.s3.temp.fileprocessing.realtime.path";
	//NOSONAR disable CERT, MSC03-J This line is not hardcoded, it is fallback value.
	public static final String PAC_PROCESSING_REALTIME_FILE_PATH_DEFAULT = "/NFS_DATA/transfer/incoming/deposit/PACDateAutomation/";

	/**
	 * Prefix of each file which will get stored inside "pac.amazon.s3.temp.fileprocessing.realtime.path".
	 */
	public static final String PAC_PROCESSING_REALTIME_FILE_NAME_PREFIX = "pac.amazon.s3.temp.fileprocessing.realtime.file.name.prefix";
	public static final String PAC_PROCESSING_REALTIME_FILE_NAME_PREFIX_DEFAULT = "realtime_";

	/**
	 * Enables or disables PAC HIVE globally.
	 * <p/>
	 * If this property is {@code false} AERA JSON files will not be processed and all PAC HIVE functionality will be
	 * disabled unless some parts of it are enabled by more fine-grained properties.
	 * <p/>
	 * It can be overridden by {@link Jnjgtb2bpacConstants#PAC_AERA_DISABLED_FOR_BASE_STORES} or
	 * {@link Jnjgtb2bpacConstants#PAC_AERA_ENABLED_FOR_ORDERS}.
	 */
	public static final String PAC_AERA_ENABLED = "pac.aera.enabled";
	public static final boolean PAC_AERA_ENABLED_DEFAULT = false;

	/**
	 * Disables PAC HIVE for specific base stores.
	 * <p/>
	 * It can be used when {@link Jnjgtb2bpacConstants#PAC_AERA_ENABLED} is set to {@code true} to disable PAC HIVE for
	 * specific counties.
	 * <p/>
	 * A list of comma-separated Hybris BaseStore uid can be stored in this property.
	 * <p/>
	 * It can be overridden by {@link Jnjgtb2bpacConstants#PAC_AERA_ENABLED_FOR_ORDERS}.
	 */
	public static final String PAC_AERA_DISABLED_FOR_BASE_STORES = "pac.aera.disabled.for.base.stores";
	public static final String PAC_AERA_DISABLED_FOR_BASE_STORES_DEFAULT = StringUtils.EMPTY;
	public static final String PAC_AERA_DISABLED_FOR_BASE_STORES_SEPARATOR = ",";

	/**
	 * Enables PAC HIVE for specific orders even if {@link Jnjgtb2bpacConstants#PAC_AERA_ENABLED} is {@code false} or
	 * PAC HIVE is disabled by {@link Jnjgtb2bpacConstants#PAC_AERA_DISABLED_FOR_BASE_STORES}.
	 * <p/>
	 * It allows to enable displaying of already existing PAC HIVE data om UI. This property will not enable processing
	 * of AERA JSON files if {@link Jnjgtb2bpacConstants#PAC_AERA_ENABLED} is {@code false}.
	 * <p/>
	 * A list of comma-separated Hybris {@link de.hybris.platform.core.model.order.OrderModel#CODE} or
	 * {@link de.hybris.platform.core.model.order.OrderModel#SAPORDERNUMBER} can be stored in this property.
	 */
	public static final String PAC_AERA_ENABLED_FOR_ORDERS = "pac.aera.enabled.for.orders";
	public static final String PAC_AERA_ENABLED_FOR_ORDERS_DEFAULT = StringUtils.EMPTY;
	public static final String PAC_AERA_ENABLED_FOR_ORDERS_SEPARATOR = ",";

	/**
	 * Controls which product sectors will be affected by PAC HIVE. See
	 * {@link com.jnj.core.model.JnJProductModel#SECTOR}.
	 */
	public static final String PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR = "pac.aera.enabled.for.product.sectors";
	public static final String PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR_DEFAULT = Jnjb2bCoreConstants.MDD_SECTOR;
	public static final String PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR_SEPARATOR = ",";

	private Jnjgtb2bpacConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

}
