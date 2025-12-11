/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.controllers.pages;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.jnj.b2b.cartandcheckoutaddon.controllers.CartandcheckoutaddonControllerConstants;
import com.jnj.b2b.cartandcheckoutaddon.controllers.pages.JnjGTContractController;
import com.jnj.b2b.cartandcheckoutaddon.forms.ContractForm;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjContractPriceData;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.b2b.cartandcheckoutaddon.controllers.JnjlacartandcheckoutaddonControllerConstants;
import com.jnj.services.MessageService;


/**
 * @author mpanda3
 *
 */
public class JnJLaContractController extends JnjGTContractController
{
	public static final Class<JnJLaContractController> THIS_CLASS = JnJLaContractController.class;
	@Autowired
	protected JnjLatamCartFacade jnJLatamCartFacade;

	@Autowired
	private CommonI18NService commonI18NService;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Autowired
	private MessageService messageService;

	@Autowired
	private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

	/** The Constant CONTRACT__DETAILS_CMS_PAGE. */
	private static final String CONTRACT__DETAILS_CMS_PAGE = "contractDetail";

	/** The Constant CONTRACT_CMS_PAGE. */
	private static final String CONTRACT_CMS_PAGE = "contract";

	protected static final String CART_MODIFICATION_DATA = "cartModificationData";

	protected static final String RESULT_PDF = "JnjLatamContractPdfView";
	protected static final String RESULT_EXCEL = "JnjLatamContractExcelView";

	//for contract detail page download
	protected static final String RESULT_DETAIL_PDF = "JnjLatamContractDetailPdfView";
	protected static final String RESULT_DETAIL_EXCEL = "JnjLatamContractDetailExcelView";

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	/** The user service. */
	@Autowired
	private UserService userService;

	@Override
	public String searchContracts(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form)
			throws CMSItemNotFoundException
	{
		final String methodName = "searchContracts()";

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
		try
		{
			final int finalPageSize = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
			final PageableData pageableData = createPageableData(page, finalPageSize, sortCode, showMode);
			final SearchPageData<JnjContractData> searchPageData = jnjGTContractFacade.getPagedContracts(pageableData,
					form.getSearchByCriteria(), form.getSearchParameter(), form.getSortByCriteria(), form.getSelectCriteria(),
					form.getFilterCriteria2());
			String language = "en";
			if (null != commonI18NService.getCurrentLanguage())
			{
				language = commonI18NService.getCurrentLanguage().getIsocode().toLowerCase();
			}
			if (searchPageData.getResults().size() > 0)
			{
				model.addAttribute("contractList", searchPageData.getResults());
				model.addAttribute("sessionlanguage", language);
			}
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("searchPageData", searchPageData);
			final Object showChangeAccountLink = sessionService
					.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
			if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
			{
				model.addAttribute("showChangeAccountLink", Boolean.TRUE);
			}
			preparePage(model);
		}
		catch (final CMSItemNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.CONTRACTS, methodName, Logging.END_OF_METHOD, exception, THIS_CLASS);
		}
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, methodName, Logging.END_OF_METHOD, THIS_CLASS);
		return getView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Account.Contract);
	}

	@Override
	protected void preparePage(final Model model) throws CMSItemNotFoundException
	{

		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTRACT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTRACT_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.contracts"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		if (!model.containsAttribute("contractForm"))
		{
			model.addAttribute("contractForm", new ContractForm());
		}
	}



	@Override
	protected String addToCartFromContract(final String[] selectedProductCatalogIds, final Model model, final ContractForm form)
			throws CMSItemNotFoundException
	{
		final String methodName = "addToCartFromContract()";
		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
		for (final String product : selectedProductCatalogIds)
		{
			JnjGTCoreUtil.logInfoMessage(Logging.CONTRACTS, methodName, "Latam :::::::::: product value : " + product, THIS_CLASS);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
		Map<String, Object> responseMap = new HashMap<String, Object>();

		jnJLatamCartFacade.saveCartWithContractNo(form.geteCCContractNum());

		responseMap = addToCartFromContract(responseMap, selectedProductCatalogIds, form.getIndirectCustomer());


		if (!responseMap.isEmpty() && responseMap.containsKey(CART_MODIFICATION_DATA))
		{
			final JnjCartModificationData cartData = (JnjCartModificationData) responseMap.get(CART_MODIFICATION_DATA);
			model.addAttribute(PRODUCT, cartData.getCartModifications().get(0).getEntry().getProduct());
			model.addAttribute(CART_DATA, cartData);
			responseMap.remove(CART_MODIFICATION_DATA);
		}
		model.addAttribute(ADD_STATUS, responseMap);
		final CartData cartData = jnjGTCartFacade.getSessionCart();

		model.addAttribute(LINES, String.valueOf(cartData.getTotalUnitCount()));
		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, methodName, Logging.END_OF_METHOD, THIS_CLASS);
		return getView(LoginaddonControllerConstants.Views.Fragments.Cart.AddToCartHomePopup);
	}

	private Map<String, Object> addToCartFromContract(final Map<String, Object> responseMap,
			final String[] selectedProductCatalogIds, final String indirectCustomer)
	{
		final long qty = Jnjb2bCoreConstants.DEFAULT_ADD_TO_CART_QTY;
		final List<String> selectedProductList = Arrays.asList(selectedProductCatalogIds);
		for (final String productTobeAdded : selectedProductList)
		{
			JnjCartModificationData cartModificationData;
			try
			{
				cartModificationData = jnJLatamCartFacade.addToCartFromContract(productTobeAdded, String.valueOf(qty));

				if (!cartModificationData.getCartModifications().get(0).isError())
				{
					responseMap.put(CART_MODIFICATION_DATA, cartModificationData);
					responseMap.put(productTobeAdded, Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
					jnJLatamCartFacade.updateIndirectCustomer(indirectCustomer, productTobeAdded);
				}
				else
				{
					responseMap.put(productTobeAdded, cartModificationData.getCartModifications().get(0).getStatusCode());
				}

			}
			catch (final CommerceCartModificationException e)
			{
				JnjGTCoreUtil.logErrorMessage("JnJLaContractController", "addToCartFromContract()", e.getMessage(), e, THIS_CLASS);
			}
		}
		return responseMap;
	}

	@Override
	public String getContractDetails(@PathVariable("contractNumber") final String contractNumber,
			@RequestParam(value = "entryCount", defaultValue = "0") final int entryCount, final Model model)
	{
		final String methodName = "getContractDetails()";

		jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);

		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);
		try
		{
			final JnjContractData contractData = jnjGTContractFacade.getContractDetailsById(contractNumber, entryCount);

			final CurrencyModel currencyModel = commonI18NService.getCurrentCurrency();
			final JnjContractPriceData cntrctPriceData = new JnjContractPriceData();
			cntrctPriceData.setTotalAmount(createPrice(currencyModel, contractData.getTotalAmount()));
			cntrctPriceData.setBalanceAmount(createPrice(currencyModel, contractData.getBalanceAmount()));
			cntrctPriceData.setConsumedAmount(createPrice(currencyModel, contractData.getConsumedAmount()));
			final List<JnjContractEntryData> contractEntryList = contractData.getContractEntryList();
			final Map<String, JnjContractPriceData> cntrctEntryMap = new HashMap<String, JnjContractPriceData>();


			for (final JnjContractEntryData cntrctEntrData : contractEntryList)
			{
				final JnjContractPriceData cntractPriceData = new JnjContractPriceData();
				cntractPriceData.setBalanceAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractBalanceQty())));
				cntractPriceData.setTotalAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractQty())));
				cntractPriceData.setConsumedAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getConsumedQty())));

				cntrctEntryMap.put(cntrctEntrData.getProductCode(), cntractPriceData);

			}

			storeCmsPageInModel(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
			String language = "en";
			if (null != commonI18NService.getCurrentLanguage())
			{
				language = commonI18NService.getCurrentLanguage().getIsocode().toLowerCase();
			}
			model.addAttribute("sessionlanguage", language);
			model.addAttribute("contractData", contractData);
			//
			model.addAttribute("cntrctPriceData", cntrctPriceData);
			model.addAttribute("cntrctEntryMap", cntrctEntryMap);
			model.addAttribute("contractEntryList", contractEntryList);

			sessionService.setAttribute("contractEntryList", contractEntryList);
			setContractDetailBreadCrumb(model);
			model.addAttribute("metaRobots", "no-index,no-follow");
			model.addAttribute("canCheckout", Boolean.valueOf(jnjGetCurrentDefaultB2BUnitUtil.validateCheckoutUser()));
		}
		catch (final CMSItemNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.CONTRACTS, methodName, exception.getMessage(), exception, THIS_CLASS);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, methodName, Logging.END_OF_METHOD, THIS_CLASS);
		return getView(JnjlacartandcheckoutaddonControllerConstants.Views.Pages.Account.ContractDetail);
	}

	@Override
	public String getView(final String view)
	{
		return JnjlacartandcheckoutaddonControllerConstants.ADDON_PREFIX + view;
	}

	@Override
	public ContractForm isNonContractProduct(final ContractForm form, final Model model,
			@RequestParam(value = "selectedProducts", required = false) final String[] selectedProductCatalogIds,
			@RequestParam(value = "eCCContractNum", required = false) final String contractNum)
			throws CommerceCartModificationException, CMSItemNotFoundException
	{
		final String methodName = "LATAM - isNonContractProduct()";
		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, methodName, Logging.BEGIN_OF_METHOD, THIS_CLASS);

		final JnjContractFormData formData = jnJLatamCartFacade.validateIsNonContract(selectedProductCatalogIds, contractNum);
		if(formData != null && formData.getIsNonContractProductInCart() != null)
		{
			form.setNonContractProductInCart(formData.getIsNonContractProductInCart());// generally bypass this condition to show popup because  on cart product / selected product is mixed
			form.setNonContractProductInSelectedList(formData.getIsNonContractProductInSelectedList());
			form.setMultiContractCount(formData.getMultiContractCount());
			form.setMultiProductCount(formData.getMultiProductCount());
			if (jnjGTCartFacade.getSessionCart().getTotalItems() == 0)
			{
				form.setNonContractProductInCart(false);
			}

			JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, methodName, Logging.END_OF_METHOD, THIS_CLASS);
		}
		
		return form;
	}

	@Override
	public String downloadContractsList(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType)
	{
		final String METHOD_NAME = "downloadContractsList()";
		boolean noResultFlag = false;
		int noResult = 0;

		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLaContractController.class);
		try
		{
			if (StringUtils.isNotEmpty(form.getLoadNoOfRecords()))
			{
				noResult = Integer.parseInt(form.getLoadNoOfRecords());
			}
			else
			{
				noResult = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
			}

			final PageableData pageableData = createPageableData(page, noResult, sortCode, showMode);
			final SearchPageData<JnjContractData> searchPageData = jnjGTContractFacade.getPagedContracts(pageableData,
					form.getSearchByCriteria().trim(), form.getSearchParameter(), form.getSortByCriteria(), form.getSelectCriteria(),
					form.getFilterCriteria2());
			model.addAttribute("totalNoOfRecords", String.valueOf(searchPageData.getPagination().getTotalNumberOfResults()));
			model.addAttribute("contractList", searchPageData.getResults());

			if (searchPageData.getResults().size() > 0)
			{
				model.addAttribute("contractList", searchPageData.getResults());
			}
			else
			{
				noResultFlag = true;
				model.addAttribute("searchCriteria", form.getSearchByCriteria());
				model.addAttribute("searchParameter", form.getSearchParameter());
				model.addAttribute("noResultFlag", Boolean.valueOf(noResultFlag));
			}
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("contractForm", form);
			preparePage(model);
		}
		catch (final CMSItemNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.CONTRACTS, METHOD_NAME, exception.getMessage(), exception,
					JnJLaContractController.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.CONTRACTS, METHOD_NAME, Logging.END_OF_METHOD, JnJLaContractController.class);

		final List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
		if (catologLst != null && catologLst.size() > 0)
		{
			final MediaModel mediaModel1 = mediaService.getMedia(
					catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
					Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
			final MediaModel mediaModel2 = mediaService.getMedia(
					catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
					Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
			if (mediaModel1 != null)
			{
				model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
			}
			if (mediaModel2 != null)
			{
				model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
			}
		}

		/* site log */
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
				.getActiveCatalogVersion();
		//Send site logo
		model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne")));

		model.addAttribute("scrollPos", scrollPos);
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_PDF
				: (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType)) ? RESULT_EXCEL
						: getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.ContractPopup);
	}

	@Override
	public String downloadDetailData(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "scrollPos", required = false, defaultValue = "") final String scrollPos,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model, final ContractForm form,
			@RequestParam(value = "downloadType", defaultValue = "None") final String downloadType,
			@RequestParam(value = "eCCContractNum", required = true) final String eCCContractNum,
			@RequestParam(value = "entryCount", defaultValue = "30") final int entryCount)
	{
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, Jnjb2bCoreConstants.Logging.CONTRACTS_DETAIL_LIST,
				Logging.BEGIN_OF_METHOD, JnJLaContractController.class);

		try
		{
			final JnjContractData contractData = jnjGTContractFacade.getContractDetailsById(eCCContractNum, entryCount);
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			//JCC - 13 change start
			final CurrencyModel currencyModel = currentUser.getSessionCurrency();
			final JnjContractPriceData cntrctPriceData = new JnjContractPriceData();
			cntrctPriceData.setTotalAmount(createPrice(currencyModel, contractData.getTotalAmount()));
			cntrctPriceData.setBalanceAmount(createPrice(currencyModel, contractData.getBalanceAmount()));
			cntrctPriceData.setConsumedAmount(createPrice(currencyModel, contractData.getConsumedAmount()));
			final List<JnjContractEntryData> contractEntryList = contractData.getContractEntryList();
			final Map<String, JnjContractPriceData> cntrctEntryMap = new HashMap<String, JnjContractPriceData>();

			for (final JnjContractEntryData cntrctEntrData : contractEntryList)
			{

				final JnjContractPriceData cntractPriceData = new JnjContractPriceData();
				cntractPriceData.setBalanceAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractBalanceQty())));
				cntractPriceData.setTotalAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getContractQty())));
				cntractPriceData.setConsumedAmount(createPrice(currencyModel, Double.valueOf(cntrctEntrData.getConsumedQty())));

				cntrctEntryMap.put(cntrctEntrData.getProductCode(), cntractPriceData);
			}
			//JCC - 13 change end

			storeCmsPageInModel(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONTRACT__DETAILS_CMS_PAGE));
			String language = null;
			if (null != currentUser.getSessionLanguage())
			{
				language = currentUser.getSessionLanguage().getIsocode();
			}
			model.addAttribute("sessionlanguage", language);
			model.addAttribute("contractData", contractData);
			//
			model.addAttribute("cntrctPriceData", cntrctPriceData);
			model.addAttribute("cntrctEntryMap", cntrctEntryMap);
			model.addAttribute("contractEntryList", contractEntryList);
			setContractDetailBreadCrumb(model);
			model.addAttribute("metaRobots", "no-index,no-follow");
			model.addAttribute("canCheckout", Boolean.valueOf(jnjGetCurrentDefaultB2BUnitUtil.validateCheckoutUser()));

			final List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
			if (catologLst != null && catologLst.size() > 0)
			{
				final MediaModel mediaModel1 = mediaService.getMedia(
						catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
						Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
				final MediaModel mediaModel2 = mediaService.getMedia(
						catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.ONLINE),
						Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
				if (mediaModel1 != null)
				{
					model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
				}
				if (mediaModel2 != null)
				{
					model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
				}
			}

			/* site log */
			final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
			final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
			//Send site logo
			model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne")));

		}
		catch (final CMSItemNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, Jnjb2bCoreConstants.Logging.CONTRACTS_DETAIL_LIST,
					"Contract Detail page not found in current content", exception, JnJLaContractController.class);
		}

		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CONTRACTS, Jnjb2bCoreConstants.Logging.CONTRACTS_DETAIL_LIST,
				Logging.END_OF_METHOD, JnJLaContractController.class);

		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? RESULT_DETAIL_PDF
				: (DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType)) ? RESULT_DETAIL_EXCEL
						: getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.ContractPopup);

	}

	@Override
	public boolean removeNonContractProduct(final ContractForm form, final Model model,
											@RequestParam(value = "contractNum", required = false) String contractNum)
			throws CommerceCartModificationException, CMSItemNotFoundException
	{

		return jnJLatamCartFacade.removeNonContractProduct(contractNum);

	}
}
