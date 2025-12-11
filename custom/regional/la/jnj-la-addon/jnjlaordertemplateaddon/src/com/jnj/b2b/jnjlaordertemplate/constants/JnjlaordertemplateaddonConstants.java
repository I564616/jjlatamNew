/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.jnjlaordertemplate.constants;

/**
 * Global class for all Jnjlaordertemplateaddon constants. You can add global constants for your extension into this class.
 */
public final class JnjlaordertemplateaddonConstants extends GeneratedJnjlaordertemplateaddonConstants
{
	public static final String EXTENSIONNAME = "jnjlaordertemplateaddon";

	private JnjlaordertemplateaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public interface Order {

		public interface OrderChannel {


			public static final String ORDET_CHANNEL_DFUE = "DFUE";
			public static final String ORDET_CHANNEL_E001 = "E001";
			public static final String ORDET_CHANNEL_E003 = "E003";
			public static final String ORDET_CHANNEL_E004 = "E004";
			public static final String ORDET_CHANNEL_E006 = "E006";
			public static final String ORDET_CHANNEL_E008 = "E008";
			public static final String ORDET_CHANNEL_M002 = "M002";
			public static final String ORDET_CHANNEL_M004 = "M004";

		}

		public interface OrderType
		{
			public static final String ORDET_TYPE_9SE1 = "9SE1";
			public static final String ORDET_TYPE_ZCT = "ZCT";
			public static final String ORDET_TYPE_ZEX = "ZEX";
			public static final String ORDET_TYPE_ZFR = "ZFR";
			public static final String ORDET_TYPE_ZINS = "ZINS";
			public static final String ORDET_TYPE_ZKA = "ZKA";
			public static final String ORDET_TYPE_ZKB = "ZKB";
			public static final String ORDET_TYPE_ZKE = "ZKE";
			public static final String ORDET_TYPE_ZKEI = "ZKEI";
			public static final String ORDET_TYPE_ZKR = "ZKR";
			public static final String ORDET_TYPE_ZLZ = "ZLZ";
			public static final String ORDET_TYPE_ZMIS = "ZMIS";
			public static final String ORDET_TYPE_ZOCR = "ZOCR";
			public static final String ORDET_TYPE_ZODR = "ZODR";
			public static final String ORDET_TYPE_ZOR = "ZOR";
			public static final String ORDET_TYPE_ZOR1 = "ZOR1";
			public static final String ORDET_TYPE_ZORD = "ZORD";
			public static final String ORDET_TYPE_ZQT = "ZQT";
			public static final String ORDET_TYPE_ZRCT = "ZRCT";
			public static final String ORDET_TYPE_ZREC = "ZREC";
			public static final String ORDET_TYPE_ZRED = "ZRED";
			public static final String ORDET_TYPE_ZRER = "ZRER";
			public static final String ORDET_TYPE_ZRO = "ZRO";
			public static final String ORDET_TYPE_ZSER = "ZSER";
			public static final String ORDET_TYPE_ZSM = "ZSM";
			public static final String ORDET_TYPE_ZSER2 = "ZSER2";
			public static final String ORDET_TYPE_ZKGW = "ZKGW";
			public static final String ORDET_TYPE_ZOS  = "ZOS";
			public static final String ORDET_TYPE_ZSIN = "ZSIN";
			public static final String ORDET_TYPE_ZIND = "ZIND";
			public static final String EXCEPTION_SOLD_TO = "ZOR";
			public static final String ORDET_TYPE_9SES = "_9SES";
			public static final String ORDET_TYPE_ZSES = "ZSES";			
		}

		public interface SearchOption
		{
			public static final String ORDER_NUMBER_PT = "Número de Pedido";
			public static final String PRODUCT_NUMBER_PT = "Código de Produto";
			public static final String PO_NUMBER_PT = "OC Cliente";
			public static final String INVOICE_NUMBER_PT = "Número de Fatura";

			public static final String ORDER_NUMBER_ES = "Número de Pedido";
			public static final String PRODUCT_NUMBER_ES = "Código de Producto";
			public static final String PO_NUMBER_ES = "OC Cliente";
			public static final String INVOICE_NUMBER_ES = "Número de Factura";
		}

	}

	// implement here constants used by this extension
}
