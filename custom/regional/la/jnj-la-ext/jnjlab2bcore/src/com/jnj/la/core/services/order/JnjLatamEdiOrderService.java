/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.order;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.jnj.core.dataload.mapper.PurchaseOrder;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;


public interface JnjLatamEdiOrderService
{
	String createOrderFromAlbertFile(InputStream inputStream, List<File> fileListForSAP, List<String> errorDetailsList,
			PurchaseOrder purchaseOrder, List<JnjUploadOrderSHAModel> passedSHAModels);

	String createOrderFromAliancaFile(InputStream inputStream, List<File> fileListForSAP, List<String> errorDetailsList,
			PurchaseOrder purchaseOrder, List<JnjUploadOrderSHAModel> passedSHAModels);


	String createOrderFromSaoLuiz(InputStream inputStream, List<File> fileListForSAP, List<String> errorDetailsList,
			PurchaseOrder purchaseOrder, List<JnjUploadOrderSHAModel> passedSHAModels);

	String createOrderFromEanLayOut(InputStream inputStream, List<File> fileListForSAP, List<String> errorDetails,
			PurchaseOrder purchaseOrder, List<PurchaseOrder> puchaseOrderList, List<JnjUploadOrderSHAModel> passedSHAModels);

	boolean sftpCallAndMoveFileToZipFolder(final List<File> fileListForSAP, final List<String> errorDetails);

	JnjUploadOrderSHAModel createUploadOrderSHADetails(String fileHash, String fileName, JnJB2bCustomerModel currentUser);

	void updateUploadOrderSHADetails(JnjUploadOrderSHAModel jnjUploadOrderSHAModel, String fileName, RecordStatus success);

}
