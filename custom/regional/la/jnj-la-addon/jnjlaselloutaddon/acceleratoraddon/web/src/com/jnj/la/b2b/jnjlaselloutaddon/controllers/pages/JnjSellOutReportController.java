/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.b2b.jnjlaselloutaddon.controllers.pages;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jnj.b2b.jnjselloutaddon.form.JnjSellOutReportsForm;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.core.annotations.AuthorizedUserGroup;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.FileUploadDTO;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnjFileUploadToSharedFolderUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.account.JnjSellOutReportsFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.la.b2b.jnjlaselloutaddon.controllers.JnjlaselloutaddonControllerConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjSellOutReportData;
import com.jnj.storefront.forms.FileUploadForm;

/**
 * This is the controller class that fetches the data for Sell Out reports and
 * displays it on front end.
 *
 * @author CTS
 * @version 1.0
 */
@RequestMapping(value = "/my-account/sellout")
public class JnjSellOutReportController extends AbstractSearchPageController {

	private static final String SELLOUT_GROUP = "selloutGroup";

    @Autowired
	private SessionService sessionService;

	@Autowired
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Autowired
	private JnjSellOutReportsFacade jnjSellOutReportsFacade;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetDefaultB2BUnitUtil;

	@Autowired
	private JnjFileUploadToSharedFolderUtil fileUploadUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private JnjGTCustomerFacade jnjLatamCustomerFacadeImpl;

	@Autowired
	private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	private String pageSize = "0";
	private String sortflag = Jnjlab2bcoreConstants.SellOutReports.SORT_ORDER_DESC;
	private final int updatedPageSize = 0;
	private String uploadStatus = "";
	private boolean sizeError = false;

	private static final String MAXIMUM_FILE_SIZE = "10485760";
	private static final int MAX_PAGE_LIMIT = 100;
	private static final String FALSE = "false";
	private static final String CURRENT_PAGE_SIZE = "currentPageSize";
	private static final String SORT_CODE = "sortCode";
	private static final String PAGE = "page";
	private static final String SCROLL_POS = "scrollPos";
	private static final String IS_NEW_REQUEST_FALSE = "?isNewRequest=false";
	private static final String REDIRECTING_TO_GET_METHOD = "Redirecting to get method";
	private static final String SELLOUT = "SelloutReport";
	private static final Class THIS_CLASS = JnjSellOutReportController.class;

	private enum DOWNLOAD_TYPE {
		PDF, EXCEL;
	}

	/**
	 *
	 * This method fetches the data for Sell Out Reports to be dislayed on the
	 * front end.
	 *
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@AuthorizedUserGroup(value = SELLOUT_GROUP)
	@GetMapping
	public String getSellOutReportsData(@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String receivedSortCode,
			@RequestParam(value = "isNewRequest", defaultValue = "true") final String isNewRequest,
			@RequestParam(value = PAGE, defaultValue = "0") final int receivedPage,
			@RequestParam(value = SCROLL_POS, required = false, defaultValue = "") final String scrollPos,
			final Model model) throws CMSItemNotFoundException {
		final String methodName = "getSellOutReportsData()";
		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, THIS_CLASS);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("text.account.sellout"), null));
		model.addAttribute("breadcrumbs", breadcrumbs);

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());

		String sortCode = null;
		int page = 0;
		int currentPageSize = Integer.parseInt(Config.getParameter("sellout.report.number.count"));
		SearchPageData<JnjSellOutReportData> searchPageData = new SearchPageData<JnjSellOutReportData>();

		if (isNewRequest.equalsIgnoreCase(FALSE)) {
			if (sessionService.getAttribute(CURRENT_PAGE_SIZE) != null
					&& !sessionService.getAttribute(CURRENT_PAGE_SIZE).toString().isEmpty()) {
				currentPageSize = Integer.parseInt(sessionService.getAttribute(CURRENT_PAGE_SIZE).toString());
			}
			if (sessionService.getAttribute(SORT_CODE) != null
					&& !sessionService.getAttribute(SORT_CODE).toString().isEmpty()) {
				sortCode = sessionService.getAttribute(SORT_CODE).toString();
				sortflag = sortCode;
			}
			if (sessionService.getAttribute(PAGE) != null && !sessionService.getAttribute(PAGE).toString().isEmpty()) {
				page = Integer.parseInt(sessionService.getAttribute(PAGE).toString());
			}
		} else {
			sessionService.removeAttribute(CURRENT_PAGE_SIZE);
			sessionService.removeAttribute(SORT_CODE);
			sessionService.removeAttribute(PAGE);
		}

		final PageableData pageableData = createPageableData(page, currentPageSize, sortCode, showMode);

		try {
			searchPageData = jnjSellOutReportsFacade.getSellOutReportData(sortflag, pageableData);
		}

		catch (final Exception exp) {
			JnjGTCoreUtil.logErrorMessage(SELLOUT, methodName, exp.getMessage(), exp, THIS_CLASS);
		}

		final JnjSellOutReportsForm jnjSellOutReportsForm = new JnjSellOutReportsForm();
		jnjSellOutReportsForm.setPageSize(currentPageSize > 0 ? String.valueOf(currentPageSize) : StringUtils.EMPTY);

		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.DATA_LIST, searchPageData);
		model.addAttribute(Jnjb2bCoreConstants.SellOutReports.SORT_FLAG, sortflag);
		model.addAttribute("defaultPageSize", String.valueOf(currentPageSize));
		model.addAttribute("uploadStatus", uploadStatus);
		model.addAttribute("sizeError", Boolean.valueOf(sizeError));
		model.addAttribute("jnjSellOutReportsForm", jnjSellOutReportsForm);

		sortflag = Jnjb2bCoreConstants.SellOutReports.SORT_ORDER_DESC;
		uploadStatus = "";
		sizeError = false;

		if (sessionService.getAttribute(SCROLL_POS) != null
				&& !StringUtils.isEmpty(sessionService.getAttribute(SCROLL_POS).toString())) {
			model.addAttribute(SCROLL_POS, sessionService.getAttribute(SCROLL_POS).toString());
			sessionService.removeAttribute(SCROLL_POS);
		}

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, THIS_CLASS);
		return JnjlaselloutaddonControllerConstants.ADDON_PREFIX
				+ JnjlaselloutaddonControllerConstants.Views.Pages.Account.SellOutReports;
	}

	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException {
		return getContentPageForLabelOrId(Jnjlab2bcoreConstants.SellOutReports.SELL_OUT_REPORTS);
	}

	@AuthorizedUserGroup(value = SELLOUT_GROUP)
	@PostMapping("/selloutUpload")
	public String uploadSellOutReport(@ModelAttribute(value = "uploadForm") final FileUploadForm form,
			@RequestParam(value = SCROLL_POS, required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = CURRENT_PAGE_SIZE, defaultValue = "5") final int pageSize, final BindingResult result,
			final Model model) {
		final String methodName = "uploadSellOutReport()";
		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, THIS_CLASS);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		final MultipartFile file = form.getFile();

		if (Integer.parseInt((MAXIMUM_FILE_SIZE)) < file.getSize()) {
			sizeError = true;
			return REDIRECT_PREFIX + Jnjb2bCoreConstants.SellOutReports.REDIRECT_TO_MAIN + IS_NEW_REQUEST_FALSE;
		}

		final String filePath;
		String localFilePath;
		final String remoteFilePath;
		final FileUploadDTO fileUploadDTO = new FileUploadDTO();
		final JnjSellOutReportData sellOutReportData = new JnjSellOutReportData();
		fileUploadDTO.setFile((CommonsMultipartFile) file);
		sellOutReportData.setFile((CommonsMultipartFile) file);
		sellOutReportData.setCompany(form.getCompany());
		final JnJB2BUnitModel jnjB2BUnitModel = jnjGetDefaultB2BUnitUtil.getDefaultB2BUnit();
		final String customerId = jnjB2BUnitModel.getUid();
		final CustomerData user = jnjLatamCustomerFacadeImpl.getCurrentCustomer();
		sellOutReportData.setCustomer(customerId);
		final Date date = new Date();
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHMMSS");
		String newFileName;

		if (form.getCompany().equals(Jnjb2bCoreConstants.SellOutReports.MEDICAL)) {
			localFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
					+ Config.getParameter(Jnjb2bCoreConstants.SellOutReports.SHARED_FOLDER_LOCATION_SELLOUT_MEDICAL);
			remoteFilePath = Config.getParameter(Jnjb2bCoreConstants.SellOutReports.MEDICAL_REMOTE_PATH);
			newFileName = customerId + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + file.getOriginalFilename();
		} else {
			localFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
					+ Config.getParameter(Jnjb2bCoreConstants.SellOutReports.SHARED_FOLDER_LOCATION_SELLOUT_PHARMA);
			remoteFilePath = Config.getParameter(Jnjb2bCoreConstants.SellOutReports.PHARMA_REMOTE_PATH);
			newFileName = customerId + "$" + formatter.format(date) + "$" + user.getUid() + "$"
					+ file.getOriginalFilename();
		}

		sellOutReportData.setDocName(file.getOriginalFilename());
		fileUploadDTO.setRenameFileTo(newFileName);

		JnjGTCoreUtil.logInfoMessage(SELLOUT, methodName, "Saving file on shared folder.", THIS_CLASS);
		boolean uploadStatusFlag;

		if (fileUploadUtil.uploadFileToSharedFolder(fileUploadDTO, localFilePath)) {
			JnjGTCoreUtil.logInfoMessage(SELLOUT, methodName,
					"File: " + localFilePath + " transfered successfully to SFTP folder.", THIS_CLASS);
			localFilePath = localFilePath + Jnjb2bCoreConstants.SellOutReports.SLASH;
			filePath = localFilePath + Jnjb2bCoreConstants.SellOutReports.SLASH + form.getFile().getOriginalFilename();

			final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();
			if (form.getCompany().equals(Jnjb2bCoreConstants.SellOutReports.MEDICAL)) {
				uploadStatusFlag = jnjSftpFileTransferUtil.uploadFileToSftp(fileUploadDTO, localFilePath,
						remoteFilePath, Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SELLOUT_MDD);

			} else {
				uploadStatusFlag = jnjSftpFileTransferUtil.uploadFileToSftp(fileUploadDTO, localFilePath,
						remoteFilePath, Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SELLOUT_PHARMA);
			}

			JnjGTCoreUtil.logInfoMessage(SELLOUT, methodName,
					"If saved in shared folder, then call facade method to save on SFTP server", THIS_CLASS);

			if (uploadStatusFlag) {
				uploadStatus = "true";
				try {
					jnjSellOutReportsFacade.updateUploadHistory(sellOutReportData);
				} catch (final Exception exp) {
					JnjGTCoreUtil.logErrorMessage(SELLOUT, methodName, exp.getMessage(), exp, THIS_CLASS);
				}
			} else {
				uploadStatus = FALSE;
			}

			new File(filePath).delete();

		} else {
			JnjGTCoreUtil.logErrorMessage(SELLOUT, methodName,
					"File: " + localFilePath + " could not be transfered to SFTP folder.", THIS_CLASS);
			uploadStatus = FALSE;
		}
		model.addAttribute(SCROLL_POS, scrollPos);

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, THIS_CLASS);
		return REDIRECT_PREFIX + Jnjb2bCoreConstants.SellOutReports.REDIRECT_TO_MAIN + IS_NEW_REQUEST_FALSE;
	}

	@PostMapping("/loadMore")
	public String loadMore(@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form,
			@RequestParam(value = SCROLL_POS, required = false, defaultValue = "") final String scrollPos,
			final Model model) throws CMSItemNotFoundException {
		final String methodName = "loadMore()";
		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, THIS_CLASS);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		// Setting the sort field as per users choice
		pageSize = form.getPageSize();
		sessionService.setAttribute(CURRENT_PAGE_SIZE, pageSize);
		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, REDIRECTING_TO_GET_METHOD, THIS_CLASS);
		sessionService.setAttribute(SCROLL_POS, scrollPos);

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, THIS_CLASS);
		return REDIRECT_PREFIX + Jnjb2bCoreConstants.SellOutReports.REDIRECT_TO_MAIN + IS_NEW_REQUEST_FALSE;
	}

	@PostMapping("/showMore")
	public String sellOutReportsFormShowN(
			@ModelAttribute("jnjSellOutReportsForm") final JnjSellOutReportsForm jnjSellOutReportsForm,
			final Model model) throws CMSItemNotFoundException {
		final String methodName = "sellOutReportsFormShowN()";
		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, THIS_CLASS);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		pageSize = jnjSellOutReportsForm.getPageSize();
		sessionService.setAttribute(CURRENT_PAGE_SIZE, pageSize);

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, REDIRECTING_TO_GET_METHOD, THIS_CLASS);

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, THIS_CLASS);
		return REDIRECT_PREFIX + Jnjb2bCoreConstants.SellOutReports.REDIRECT_TO_MAIN + IS_NEW_REQUEST_FALSE;
	}

	@PostMapping("/sort")
	public String sortSellOutReportsData(@ModelAttribute("sellOutReportsForm") final JnjSellOutReportsForm form,
			final Model model) throws CMSItemNotFoundException {
		final String methodName = "sortSellOutReportsData()";
		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, THIS_CLASS);

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		// Setting the sort field as per users choice
		sortflag = form.getSortType();
		sessionService.setAttribute(SORT_CODE, sortflag);

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, REDIRECTING_TO_GET_METHOD, THIS_CLASS);

		JnjGTCoreUtil.logDebugMessage(SELLOUT, methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, THIS_CLASS);
		return REDIRECT_PREFIX + Jnjb2bCoreConstants.SellOutReports.REDIRECT_TO_MAIN + IS_NEW_REQUEST_FALSE;
	}

	@Override
	protected JnjGTPageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode) {
		final JnjGTPageableData pageableData = new JnjGTPageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode) {
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		} else {
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

}
