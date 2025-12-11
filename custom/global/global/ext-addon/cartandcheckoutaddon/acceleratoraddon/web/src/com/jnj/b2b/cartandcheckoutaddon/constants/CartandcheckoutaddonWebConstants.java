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
 */
package com.jnj.b2b.cartandcheckoutaddon.constants;

/**
 * Global class for all Cartandcheckoutaddon web constants. You can add global constants for your extension into this
 * class.
 */
public final class CartandcheckoutaddonWebConstants
{
	//Dummy field to avoid pmd error - delete when you add the first real constant!
	public static final String deleteThisDummyField = "DELETE ME";

	private CartandcheckoutaddonWebConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public interface CheckOut
	{
		public static final String ORDER_CONFIRMATION_DOWNLOAD = "downloadData()";
	}

	public interface JnjGTExcelPdfViewLabels
	{
   	interface ContractList
   	{
   		String CONTRACT_MYCONTRACTS = "contract.mycontracts";
   		String CONTRACT_CONTRACTNUMBER = "contract.number.header";
   		String CONTRACT_TENDERNUMBER = "contract.tendernumber.header";
   		String CONTRACT_INDIRECTCUSTOMER = "contract.indirectcustomer.number";
   		String CONTRACT_INDIRECTCUSTOMER_NAME = "contract.indirectcustomer.name";
   		String CONTRACT_EXPIRATIONDATE = "contract.expiration.date";
   		String CONTRACT_CONTRACTTYPE = "contract.type";
   		
   		String CONTRACT_STATUS = "contract.status";
   		String CONTRACT_CREATION_DATE = "contract.creation.date";
   		String CONTRACT_LAST_TIME_UPDATED = "contract.last.updated";
   		String CONTRACT_PRODUCT_NAME = "contract.product.name";
   		String CONTRACT_UNIT_PRICE = "contract.unit.price";
   		
   		String CONTRACT_BAL = "contract.balance";
   		String CONTRACT_CONSUMED = "contract.consumed";
   		String CONTRACT_QTY = "contract.quantity";
   		String CONTRACT_UNIT_MEASURE = "contract.unit.measurement";
   		
   		String CONTRACT_TOTAL_CONTRACT_VALUE = "contract.total.value";
   		String CONTRACT_CONSUMED_VALUE = "contract.consumed.value";
   		String CONTRACT_BALANCE = "contract.balance";
   		String CONTRACT_ZCQ = "ZCQ";
   		String CONTRACT_ZCV = "ZCV";
   		public static final String CONTRACT_LIST_FILENAME = "MyContracts";
   		public static final String CONTRACT_LIST_TITLE = "MyContract List";
   		public static final String CONTRACT_DETAIL_FILENAME = "contract.detail.name";
   		public static final String CONTRACT_DETAIL_TITLE = "contract.details.header";
   		
   		 
   	}
	}
	// implement here constants used by this extension
}
