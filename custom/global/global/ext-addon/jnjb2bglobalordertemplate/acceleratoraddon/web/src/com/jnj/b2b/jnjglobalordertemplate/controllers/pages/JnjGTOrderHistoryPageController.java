/*package com.jnj.na.storefront.controllers.pages;*/
package com.jnj.b2b.jnjglobalordertemplate.controllers.pages;

import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.activation.MimetypesFileTypeMap;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants.Logging;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants.Order;
import com.jnj.b2b.jnjglobalordertemplate.constants.Jnjb2bglobalordertemplateConstants.Order.Invoice;
import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTDisputeItemInquiryForm;
import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTDisputeOrderInquiryForm;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTAccountSelectionData;
import com.jnj.core.dto.JnjGTDisputeItemInquiryDto;
import com.jnj.core.dto.JnjGTDisputeOrderInquiryDto;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTSearchSortForm;
import com.jnj.core.enums.JnjGTPageType;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.enums.SelectedAccountType;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.order.util.JnjOrderUtil;
/*import com.jnj.ca.core.constants.JnjcaCoreConstants;
import com.jnj.ca.facades.JnjCAInvoiceFacade;*/
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.customer.JnjCustomerFacade;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTInvoiceEntryData;
import com.jnj.facades.data.JnjGTInvoiceOrderData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTSurgeonData;
import com.jnj.facades.invoice.JnjGTInvoiceFacade;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.services.CMSSiteService;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

/**
 * The Controller class responsible for handling requests for:
 * <ul>
 * <li>Order History page, search and sorting.</li>
 * <li>Order Detail Page.</li>
 * <li>Dispute Inquiry</li>
 * <li>Track Shipment</li>
 * <li>Proof of Delivery</li>
 * <li>Update Surgeon Details</li>
 * <li>Update Purchase Order Number.</li>
 *
 * @author akash.rawat. t.e.sharma
 */
@Controller("JnjGTOrderHistoryPageController")
@Scope("tenant")
@RequestMapping(value = "/order-history")

public class JnjGTOrderHistoryPageController extends AbstractSearchPageController
{
	@Resource(name = "GTOrderFacade")
	protected JnjGTOrderFacade orderFacade;

	@Resource(name = "sessionService")
	protected SessionService sessionService;

	@Resource(name = "simpleBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "GTCartFacade")
	protected JnjGTCartFacade jnJGTCartFacade;

	@Resource(name = "jnjGTInvoiceFacade")
	protected JnjGTInvoiceFacade jnjGTInvoiceFacade;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;
	@Autowired
	CMSSiteService cMSSiteService;

	@Autowired
	MediaService mediaService;
	
	
	/*@Autowired
	private JnjCAInvoiceFacade jnjInvioceFacade;*/
	
	@Autowired 
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Resource(name = "jnjCustomerFacade")
	protected JnjCustomerFacade jnjCustomerFacade;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	@Resource(name = "GTB2BUnitFacade")
	protected JnjGTB2BUnitFacade jnjGTB2BUnitFacade;
	@Autowired
	protected JnjGTOrderFacade jnjGTOrderFacade;
	
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	@Resource(name="GTCartFacade")
	JnjGTCartFacade jnjGTCartFacade;
	
	@Resource(name = "commerceCategoryService")
	protected CommerceCategoryService commerceCategoryService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected JnjOrderUtil orderUtil;
	
	protected static final String CONFIRMATION_PAGE = "confirmationPage";
	
	public JnjGTOrderFacade getOrderFacade() {
		return orderFacade;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder() {
		return resourceBreadcrumbBuilder;
	}

	public JnjGTCartFacade getJnJGTCartFacade() {
		return jnJGTCartFacade;
	}

	public JnjGTInvoiceFacade getJnjGTInvoiceFacade() {
		return jnjGTInvoiceFacade;
	}

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public JnjCustomerFacade getJnjCustomerFacade() {
		return jnjCustomerFacade;
	}

	public JnjGTCustomerFacade getJnjGTCustomerFacade() {
		return jnjGTCustomerFacade;
	}

	public JnjGTB2BUnitFacade getJnjGTB2BUnitFacade() {
		return jnjGTB2BUnitFacade;
	}
	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}
	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService() {
	    return catalogVersionService;
	}
	
	public JnjGTOrderHistoryPageController() throws NoSuchAlgorithmException
	{
		random = SecureRandom.getInstance("SHA1PRNG");
	}
	

	private final SecureRandom random;
	protected static final Logger LOGGER = Logger.getLogger(JnjGTOrderHistoryPageController.class);
	

	protected static final String LINES = "lines";

	/**
	 * Constant enum to store document type for download.
	 */
	public enum DOWNLOAD_TYPE
	{
		PDF, EXCEL, NONE;
	}

	/**
	 * Constant enum to store Pop up types available on Order History page.
	 */
	protected enum POP_UP_TYPE
	{
		SELECT_ACCOUNTS, UPDATE_PO, UPDATE_SURGEON, DISPUTE_ORDER, DISPUTE_ITEM, SURGEON_INFORMATION;
	}
	
	protected enum PAGE_TYPE
	{
		 DISPUTE_ORDER, DISPUTE_ITEM,DISPUTE_INVOICE_ITEM;
	}

	/**
	 * View Id for the Order History PDF Download.
	 */
	protected static final String ORDER_HISTORY_PDF_VIEW = "JnjGTOrderHistoryPdfView";

	/**
	 * View Id for the Order History Excel Download.
	 */
	protected static final String ORDER_HISTORY_EXCEL_VIEW = "jnjGTOrderHistoryExcelView";

	/**
	 * View Id for the Order List PDF Download.
	 */
//	protected static final String ORDER_DETAIL_PDF = "jnjNAOrderDetailPdfView";
	protected static final String ORDER_DETAIL_PDF = "jnjGTOrderDetailPdfView";

	/**
	 * View Id for the Invoice Detail PDF Download.
	 */
	protected static final String INVOICE_DETAIL_PDF = "jnjGTInvoiceDetailPdfView";

	/**
	 * View Id for the Order List Excel Download.
	 */
	protected static final String SHIPPING_DETAIL_EXCEL = "JnjGTShippingDetailExcelView";
//	protected static final String ORDER_DETAIL_EXCEL = "jnjNAOrderDetailExcelView";
	protected static final String ORDER_DETAIL_EXCEL = "jnjGTOrderDetailExcelView";

	/**
	 * View Id for the Invoice Detail EXCEL Download.
	 */
	protected static final String INVOICE_DETAIL_EXCEL = "jnjGTInvoiceDetailExcelView";

	/**
	 * Constant for Order code pattern,
	 */
	protected static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "/order/{orderCode:.*}";

	/**
	 * Constant value for Order Invoice Detail Page request mapping.
	 */
	protected static final String INVOICE_DETAIL_PATH = "/order/invoiceDetail/{orderCode:.*}";

	protected static final String DISPUTE_ORDER_PATH = "/order/disputeOrder";
	protected static final String DISPUTE_ITEM_PATH = "/order/disputeItem";
	protected static final String INVOICE_DISPUTE_ITEM_PATH = "/order/invoice/disputeItem";
	/**
	 * Constant value for Order Invoice Detail Download request mapping.
	 */
	protected static final String INVOICE_DETAIL_DOWNLOAD_PATH = "/orderInvoiceDetailDownload";

	/**
	 * Constant value for Surgeon Information Pop up request mapping.
	 */
	protected static final String SURGEON_INFORMATION_PATH = "/order/surgeonInformation/{orderCode:.*}";

	/**
	 * Constant value for Order Detail CMS Page Id.
	 */
	protected static final String ORDER_DETAIL_CMS_PAGE = "jnjOrderDetailPage";

	/**
	 * Constant value for Order History CMS Page Id.
	 */
	protected static final String ORDER_HISTORY_CMS_PAGE = "jnjOrderHistoryPage";

	/**
	 * Constant value for Invoice Detail CMS Page Id.
	 */
	protected static final String INVOICE_DETAIL_CMS_PAGE = "jnjInvoiceDetailPage";
	protected static final String DISPUTE_ORDER_CMS_PAGE = "jnjDisputeOrderPage";
	/**
	 * Redirect Prefix vallue constant.
	 */
	protected static final String REDIRECT_CART = REDIRECT_PREFIX + "/cart";

	protected static final String REDIRECT_HOME = REDIRECT_PREFIX + "/home";
	protected static final String REDIRECT_ORDER_HISTORY = REDIRECT_PREFIX + "/order-history";
	protected static final String ORDER_CHANNEL_PREFIX = "order.channel.";

	protected static final String CMOD_PATH = "/order/{orderCode}/{sapOrderNumber}/{callCmod}/{pageName}";

	protected static final String CMOD_VALUE = "callCmod";

	protected static final String ACCOUNT_SEARCH_TERM = "accountSearchTerm";
	protected static final String ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY = "account.selection.resultsPerPage";
	protected static final String ACCOUNT_PAGINATION_DATA = "accountPaginationData";
	protected static final String CURRENT_PAGE = "currentPage";
	
	protected static final String CART_MOD_DATA = "cartModData";
	protected static final String ADD_STATUS = "addStatus";
	protected static final String ERROR_MSG_TYPE = "errorMsg";

private static final String INVOICED = "Orderhistory.status.invoiced";
	private static final String OPEN = "Orderhistory.status.Open";
	private static final String PENDING = "Orderhistory.status.Pending";
	private static final String BACKORDERED = "Orderhistory.status.Backordered";
	private static final String SHIPPED = "Orderhistory.status.Shipped";
	private static final String CONFIRMED = "Orderhistory.status.Confirmed";
	private static final String ITEMACCEPTED =  "Orderhistory.status.itemaccepted";
	private static final String CANCELLED = "Orderhistory.status.Cancelled";
	private static final String COMPLETED = "Orderhistory.status.Completed";
	private static final String INPICKING = "Orderhistory.status.inpicking";
	private static final String ONHOLD =  "Orderhistory.status.Onhold";
	private static final String CREATED = "Orderhistory.status.created";
	private static final String BEINGPROCESSED =  "Orderhistory.status.beingProcessed";

	//private static final String PACKING_LIST_PDF_VIEW = "orderDetailPage";

	//private static final String DELIVERY_PROOF_PDF_VIEW = "orderDetailPage";

	/**
	 * Size of a byte buffer to read/write file
	 */
	protected static final int BUFFER_SIZE = Integer.valueOf(Config.getParameter(Invoice.PROOF_OF_DELIVERY_DOC_BUFFER)).intValue();

	@Autowired
	protected I18NService i18nService;
	@Autowired
	protected MessageSource messageSource;
	
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
	
	
	/* 
	 * Method to reset saved orderhistorysearchform on clicking order history
	 * 
	 */
		
		@RequestMapping(value = "/reviseOrderHistory")
		public String reviseCart(JnjGTOrderHistoryForm form,final Model model,final HttpServletRequest request) throws CMSItemNotFoundException
		{	
			try{
			form = new JnjGTOrderHistoryForm();
			form.setResetSelection(Boolean.TRUE);
			sessionService.setAttribute("orderHistoryForm", form);
			//return getOrderHistory(form,model,request);
			}catch(Exception ex){
				System.out.println("revise cart function");
				ex.printStackTrace();
			}
			return getOrderHistory(form,model,request);
		}
	
	
	@RequestMapping(method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String getOrderHistory(JnjGTOrderHistoryForm form, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
			boolean resultLimitExceeded = false;
		//OS-345
		if(form!=null && form.getLineStatus()!=null )
		{
			
		//	form.setLineStatus("Invoiced");
			switch (form.getLineStatus())
			{
				case ("INVOICED"):
					form.setLineStatus(messageSource.getMessage(INVOICED, null, i18nService.getCurrentLocale()));
					
					break;

				case ("OPEN"):
					form.setLineStatus(messageSource.getMessage(OPEN, null, i18nService.getCurrentLocale()));
					
					break;

				case ("PENDING"):
					form.setLineStatus(messageSource.getMessage(PENDING, null, i18nService.getCurrentLocale()));
					
					break;

				case ("BACKORDERED"):
					form.setLineStatus(messageSource.getMessage(BACKORDERED, null, i18nService.getCurrentLocale()));
					
					break;

				case ("SHIPPED"):
					form.setLineStatus(messageSource.getMessage(SHIPPED, null, i18nService.getCurrentLocale()));
					
					break;

				case ("CONFIRMED"):
					form.setLineStatus(messageSource.getMessage(CONFIRMED, null, i18nService.getCurrentLocale()));
					
					break;

				case ("ITEM_ACCEPTED"):
					form.setLineStatus(messageSource.getMessage(ITEMACCEPTED, null, i18nService.getCurrentLocale()));
					
					break;
					
				case ("CANCELLED"):
					form.setLineStatus(messageSource.getMessage(CANCELLED, null, i18nService.getCurrentLocale()));
					
					break;
					
				case ("COMPLETED"):
					form.setLineStatus(messageSource.getMessage(COMPLETED, null, i18nService.getCurrentLocale()));
					
					break;
					
				case ("IN_PICKING"):
					form.setLineStatus(messageSource.getMessage(INPICKING, null, i18nService.getCurrentLocale()));
					
					break;
					
				case ("ON_HOLD"):
					form.setLineStatus(messageSource.getMessage(ONHOLD, null, i18nService.getCurrentLocale()));
					
					break;
					
				case ("CREATED"):
					form.setLineStatus(messageSource.getMessage(CREATED, null, i18nService.getCurrentLocale()));
					
					break;
					
				case ("BEING_PROCESSED"):
					form.setLineStatus(messageSource.getMessage(BEINGPROCESSED, null, i18nService.getCurrentLocale()));
					
					break;
			}
			
			
	}
		final int finalPageSize = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();

		int totalResults = Integer.parseInt(Config
				.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PAGESIZE));;

		if (DOWNLOAD_TYPE.PDF.toString().equals(form.getDownloadType())
				|| DOWNLOAD_TYPE.EXCEL.toString().equals(form.getDownloadType()))
		{
			if (StringUtils.isNotEmpty(form.getTotalNumberOfResults()))
			{
				totalResults = Integer.parseInt(form.getTotalNumberOfResults());

				final int resultLimit = Integer.parseInt(Config
						.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PDF_AND_EXCEL_DOWNLOAD_LINES_THRESHOLD));
				if (resultLimit > 0)
				{
					if (totalResults >= resultLimit)
					{
						totalResults = resultLimit;
						resultLimitExceeded = true;
					}
				}
			}
			
		}
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return getOrderHistoryDetails(form,model,request,resultLimitExceeded);
		}
		
		public String getOrderHistoryDetails(JnjGTOrderHistoryForm form, final Model model,final HttpServletRequest request, boolean resultLimitExceeded) throws CMSItemNotFoundException
		{
		/** On re-set selection, set new form **/
		form = (form.isResetSelection()) ? new JnjGTOrderHistoryForm() : form;
		// fix jjepic-757
				if (request.getMethod().equalsIgnoreCase("GET"))
				{
					final JnjGTOrderHistoryForm sessionForm = sessionService.getAttribute("orderHistoryForm");
					if (null != sessionForm)
					{
						form = sessionForm;
					}

				}
		
		//Changes for Depuy Specific Order History enahancement AAOL-4796		
		JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel)userService.getCurrentUser();
		String division  = null;
		if(currentUser.getDivison()!=null){
			division = currentUser.getDivison();
			
			if((division.equalsIgnoreCase(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK)))
					||(division.equalsIgnoreCase(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_SPINE)))||
					(division.equalsIgnoreCase(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_CODMAN)))){
				model.addAttribute("isDepuyUser", true);				
			}
			else{
				model.addAttribute("isDepuyUser", false);
			}
		}
//	    final int finalPageSize = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
		final int finalPageSize =Integer.parseInt(Config
					.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PAGESIZE));
		final PageableData pageableData = createPageableData(0, finalPageSize, form.getSortCode(), ShowMode.Page);
		final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
		if (null != form.getAccounts() && form.getAccounts().length > 0)
		{
			String selectedAccountsString = "";
			for (final String account : form.getAccounts())
			{
				selectedAccountsString += "," + account;
			}
			if (selectedAccountsString.indexOf(",") == 0)
			{
				model.addAttribute("accountsSelectedList", selectedAccountsString.split(",", 2)[1]);
			}
			else
			{
				model.addAttribute("accountsSelectedList", selectedAccountsString);
			}
		}
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
		//Set counter for ascending or descending sort
		setAscDescCounter(model, form);

		if (DOWNLOAD_TYPE.NONE.toString().equals(form.getDownloadType()))
		{
			final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData, form);
			populateModel(model, searchPageData, ShowMode.Page);
			populateDropDownOptions(model, currentSite, form.getSelectedAccountType());
			//change to fix the demo comment where the line status was not getting reset to default value
			if(null!= form.getLineStatus())
			{
			convertLineStatus(form); 
			}
			model.addAttribute("orderHistoryForm", form);
			if (request.getMethod().equalsIgnoreCase("POST"))
			{
				sessionService.setAttribute("orderHistoryForm", form);
			}
			/** Setting the start date **/
			if (null != form.getStartDate())
			{
				model.addAttribute("startDate", form.getStartDate());
			}
			/** Setting the end date **/
			if (null != form.getEndDate())
			{
				model.addAttribute("endDate", form.getEndDate());
			}

		
			/*model.addAttribute("showChangeAccount", sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT));
			*/
			LOGGER.debug("*********************************************");
			
			final String totalAccountsSelected = Integer.toString(form.getAccounts().length);
			model.addAttribute("totalAccountsSelected", totalAccountsSelected);
	
			
			model.addAttribute("selectedaccounts", StringUtils.join(form.getAccounts(), Jnjb2bglobalordertemplateConstants.SYMBOl_COMMA));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, Collections.singletonList(new Breadcrumb(null, jnjCommonFacadeUtil
					.getMessageFromImpex("orderHistoryPage.heading"), null)));

			model.addAttribute("metaRobots", "no-index,no-follow");
			
			model.addAttribute("isMddSite", Boolean.valueOf(Jnjb2bglobalordertemplateConstants.MDD.equals(currentSite)));

			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
			storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
           /**return changed**/
			/***added by Shamili*****/
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderHistoryPage);
		}
		else
		{
		    /* Excel image adding Started here */
		    final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		    final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
			
			
			final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData, form);
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("resultLimitExceeded", resultLimitExceeded);
			
			//Send site logo
			model.addAttribute( "siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
			 /* Excel image adding end here */
			
			 /* Pdf image adding Started here */
			List<ContentCatalogModel> catologLst = getCmsSiteService().getCurrentSite().getContentCatalogs();
			 if (catologLst != null && catologLst.size() > 0) {
				 MediaModel mediaModel1 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
						 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE);
				 MediaModel mediaModel2 = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), 
						 Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2);
                	        if (mediaModel1 != null) {
                	               model.addAttribute("jnjConnectLogoURL", mediaService.getStreamFromMedia(mediaModel1));
                	        }
                	        if (mediaModel2 != null) {
                	           model.addAttribute("jnjConnectLogoURL2", mediaService.getStreamFromMedia(mediaModel2));
                	        }
			}
			 /* Pdf image adding Started here */
			return (DOWNLOAD_TYPE.PDF.toString().equals(form.getDownloadType())) ? ORDER_HISTORY_PDF_VIEW : ORDER_HISTORY_EXCEL_VIEW;
		}
	}
	
	@PostMapping(ORDER_CODE_PATH_VARIABLE_PATTERN)
	public String orderDetailPost(@PathVariable("orderCode") final String orderCode, final JnjGTSearchSortForm form,
			final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		return orderDetail(orderCode, form, model, request, true);
	}

	@GetMapping(ORDER_CODE_PATH_VARIABLE_PATTERN)
	public String orderDetail(@PathVariable("orderCode") final String orderCode, final JnjGTSearchSortForm form,
			final Model model, final HttpServletRequest request, final boolean ignoreRestriction) throws CMSItemNotFoundException
	{
		final JnjGTOrderData orderData = (JnjGTOrderData) orderFacade.getOrderDetailsForCode(orderCode, ignoreRestriction);
		try
		{
			
			sessionService.setAttribute("orderDetailPageSize", Integer.valueOf(form.getPageSize()));
			sessionService.setAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE, JnjGTPageType.ORDER_DETAIL);
			// jjepic - 773  packinglist expiry
			/*final JnjGTOrderData orderData = (JnjGTOrderData) orderFacade.getOrderDetailsForCode(orderCode, ignoreRestriction);*/
			//final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
			sessionService.removeAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE);

			final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
			
		
			// jjepic - 773 - packinglist expiry
			if (null != orderData.getActualDeliveryDate()){
		    int packingListExpiryDate = Days.daysBetween(new DateTime(orderData.getActualDeliveryDate()), new DateTime(new Date())).getDays();
		    model.addAttribute("packingListExpiryDate", packingListExpiryDate);
			}
			
			model.addAttribute("orderData", orderData);
			model.addAttribute("searchSortForm", form);
			model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
			model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_URL, JnjWebUtil.getSiteUrl(request));
			setOrderDetailBreadCrumb(model);
		}
		catch (final UnknownIdentifierException exception)
		{
			LOGGER.warn("Attempted to load a order that does not exist or is not visible", exception);
			return REDIRECT_HOME;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//return getViewForPage(model);
		
		if(null !=orderData && "KE".equals(orderData.getOrderType()))
		{
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderDetailConsignmentChargePage);
		}
		//changes for AAOL-4339
		else if(null != orderData && "KA".equals(orderData.getOrderType()))
		{
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderDetailConsignmentReturnPage);
		}
		else if(null != orderData && "KB".equals(orderData.getOrderType()))
		{
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderDetailConsignmentFillupPage);
		}
		//end of AAOL-4339
		else
		{
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderDetailPage);
		} 
	}

	@RequestMapping(value = "/updateShippingList")
	public String updateShippingList(final Model model, @RequestParam("shippingDetails") final String shippingDetails, @RequestParam("orderCode") String orderCode)
	{
		final JnjGTOrderData orderData = (JnjGTOrderData) orderFacade.getOrderDetailsForCode(orderCode, true);
		model.addAttribute("orderData",orderData);
		return SHIPPING_DETAIL_EXCEL;
		/*System.out.println("Controller"+shippingDetails);*/
	}
	@GetMapping("/refineOrderDetail")
	public String refineSearchResult(@RequestParam("orderCode") final String orderCode, final JnjGTSearchSortForm form,
			final Model model) throws CMSItemNotFoundException
	{
		boolean resultLimitExceeded = false;
		final String downloadType = form.getDownloadType();
		final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
		
		//Changes to remove the limit off 100 results.
//       	final int finalPageSize = (form.isShowMore()) ? form.getPageSize() * form.getShowMoreCounter() : form.getPageSize();
		final int finalPageSize =Integer.parseInt(Config
				.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_HISTORY_PAGESIZE));
         /* code changes for defect 56 */
		int totalResults = finalPageSize;
       		if (DOWNLOAD_TYPE.PDF.toString().equals(downloadType) || DOWNLOAD_TYPE.EXCEL.toString().equals(downloadType))
		{
			if (StringUtils.isNotEmpty(form.getTotalNumberOfResults()))
			{
				totalResults = Integer.parseInt(form.getTotalNumberOfResults());
				final int resultLimit = Integer.parseInt(Config
						.getParameter(Jnjb2bglobalordertemplateConstants.Order.ORDER_DETAIL_PDF_AND_EXCEL_DOWNLOAD_LINES_THRESHOLD));
				if (resultLimit > 0)
				{
					if (totalResults >= resultLimit)
					{
						totalResults = resultLimit;
						resultLimitExceeded = true;
					}
				}
			}
		}
		sessionService.removeAttribute("orderDetailPageSize");
		model.addAttribute("noOfLines", Integer.valueOf(orderFacade.getOrderDetailsForCode(orderCode).getEntries().size()));
		sessionService.setAttribute("orderDetailPageSize", Integer.valueOf(totalResults));
		sessionService.setAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE, JnjGTPageType.ORDER_DETAIL);
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		sessionService.removeAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE);

		model.addAttribute("orderData", orderData);
		model.addAttribute("searchSortForm", form);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}

		if (DOWNLOAD_TYPE.NONE.toString().equals(downloadType))
		{
			//sessionService.setAttribute("orderDetailPageSize", Integer.valueOf(finalPageSize));
			storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
			setOrderDetailBreadCrumb(model);
			//return getViewForPage(model);
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.OrderDetailPage);
		}
		else
		{
			final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
			final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
			//Fix for JJEPIC-614 PDF/Excel download all the line >10 products in the order.
			final int pageSizeForDownload = MAX_PAGE_LIMIT;
			//sessionService.setAttribute("orderDetailPageSize", Integer.valueOf(orderFacade.getOrderDetailsForCode(orderCode).getEntries().size()));
			model.addAttribute("orderDataForPDFExcel", orderData);
			model.addAttribute("resultLimitExceeded", resultLimitExceeded);
			model.addAttribute(
					"siteLogoPath",
					mediaDirBase + File.separator + "sys_master" + File.separator
							+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
							
			
			
			
			
			
			
			return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? ORDER_DETAIL_PDF : ORDER_DETAIL_EXCEL;
		}
		
	}

	/**
	 * The isCmodRgaCall method is used to get the byte array for the cmod & rga doc. from the CMOD interface.
	 *
	 * @param model
	 *           the model
	 * @param request
	 *           the request
	 * @param httpServletResponse
	 *           the http servlet response
	 * @param order
	 *           code the order code
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	@GetMapping(CMOD_PATH)
	public String isCmodRgaCall(@PathVariable("orderCode") final String orderCode,
			@PathVariable("sapOrderNumber") final String sapOrderNumber, @PathVariable("callCmod") final String callCmodRga,
			@PathVariable("pageName") final String pageName, final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request, final HttpServletResponse httpServletResponse)
			throws CMSItemNotFoundException
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "mapCmodRgaRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Map<String, byte[]> byteArrayInMap = null;
		byte[] byteArray = null;
		String errorMessage = null;
		try
		{
			// if the call is for cmod doc.
			if (callCmodRga.equals(CMOD_VALUE))
			{
				byteArrayInMap = orderFacade.isCmodRgaCall(sapOrderNumber, true);
			}
			else
			{
				byteArrayInMap = orderFacade.isCmodRgaCall(sapOrderNumber, false);
			}
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "isCmodRgaCall()" + Logging.HYPHEN + "BusinessException Occured:"
					+ Logging.HYPHEN + businessException.getMessage());
			errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("order.details.cmodrga.error.message");
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "isCmodRgaCall()" + Logging.HYPHEN
					+ "IllegalArgumentException Occured:" + Logging.HYPHEN + illegalArgumentException.getMessage());
			errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("order.details.cmodrga.error.message");
		}
		catch (final IntegrationException integrationException)
		{
			LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "isCmodRgaCall()" + Logging.HYPHEN
					+ "IntegrationException Occured:" + Logging.HYPHEN + integrationException.getMessage());
			errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("order.details.cmodrga.error.message");
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "isCmodRgaCall()" + Logging.HYPHEN
					+ "Exception Occured other then business, illegal argument and integration exception: " + Logging.HYPHEN
					+ throwable.getMessage());
			errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("order.details.cmodrga.error.message");
		}

		if (null != byteArrayInMap && StringUtils.isEmpty(errorMessage))
		{
			for (final Map.Entry<String, byte[]> entry : byteArrayInMap.entrySet())
			{
				byteArray = entry.getValue();
				httpServletResponse.setContentLength(Long.valueOf(byteArray.length).intValue());
				final InputStream inputStream = new ByteArrayInputStream(byteArray);
				// if the file type is xml then set the content type and header for the xml file

				httpServletResponse.setContentType("application/pdf");
				httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + entry.getKey()
						+ Jnjb2bglobalordertemplateConstants.PDF_EXTENSION);
				try
				{
					FileCopyUtils.copy(inputStream, httpServletResponse.getOutputStream());
					inputStream.close();
					httpServletResponse.getOutputStream().close();
				}
				catch (final IOException exception)
				{
					LOGGER.error(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "isCmodRgaCall()" + Logging.HYPHEN
							+ "Input output exception occured: " + Logging.HYPHEN + exception.getMessage());
					errorMessage = jnjCommonFacadeUtil.getMessageFromImpex("order.details.cmodrga.error.message");
				}
			}
		}

		if (StringUtils.isNotEmpty(errorMessage))
		{
			model.addAttribute("errorOccured", errorMessage);
		}
		else
		{
			model.addAttribute("errorOccured", "");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "mapCmodRgaRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (StringUtils.equals(pageName, CONFIRMATION_PAGE))
		{
			redirectModel.addFlashAttribute("cmodDownloadError", errorMessage);
			redirectModel.addFlashAttribute("hideOnloadSpinner", Boolean.TRUE);
			return REDIRECT_PREFIX + "/checkout/single/orderConfirmation/" + orderCode;
		}
		else
		{
		return orderDetail(orderCode, new JnjGTSearchSortForm(), model, request,true);
		}
	}

	
	
	@GetMapping(INVOICE_DETAIL_PATH)
	public String invoiceDetails(@PathVariable("orderCode") final String orderCode, final Model model,
			final HttpServletRequest request, final boolean ignoreRestriction) throws CMSItemNotFoundException
	{

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		try
		{
		sessionService.setAttribute("orderDetailPageSize", Integer.valueOf(10));
		sessionService.setAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE, JnjGTPageType.INVOICE_DETAIL);
		// jjepic - 773 - packing list expiry
		 JnjGTOrderData orderData = null;
		try{
			orderData = (JnjGTOrderData) orderFacade.getOrderDetailsForCode(orderCode,ignoreRestriction);
	      //final OrderData orderData =  orderFacade.getOrderDetailsForCode(orderCode);
		
		}catch(Exception e){
			
			LOGGER.warn("Attempted to load a order that does not exist or is not visible", e);
			return REDIRECT_HOME;
		}
		if (null == orderData)
		{
			throw new UnknownIdentifierException(orderCode + " does not exists or it is not valid order for current user");
		}
		sessionService.removeAttribute(Jnjb2bglobalordertemplateConstants.CURRENT_PAGE_TYPE);
		sessionService.removeAttribute("orderDetailPageSize");
		
		// jjepic - 773 - packing list expiry
		if (null != orderData.getActualDeliveryDate()){
	    int packingListExpiryDate = Days.daysBetween(new DateTime(orderData.getActualDeliveryDate()), new DateTime(new Date())).getDays();
	    model.addAttribute("packingListExpiryDate", packingListExpiryDate);
		}
	    model.addAttribute("orderData", orderData);
		final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
		final List<JnjGTInvoiceOrderData> orderInvoices = jnjGTInvoiceFacade.getInvoiceDetailsByCode(orderCode);
		model.addAttribute("orderInvoices", orderInvoices);
        model.addAttribute("errorInvoiceMessage", Boolean.FALSE);
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
				.add(new Breadcrumb("/order-history", jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null));
		breadcrumbs.add(new Breadcrumb("/order-history/order/" + orderCode, jnjCommonFacadeUtil
				.getMessageFromImpex("orderDetailPage.heading"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("invoiceDetailPage.heading"), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

		model.addAttribute("proofOfDelTestInd", Config.getParameter(Invoice.PROOF_OF_DELIVERY_TEST_IND));
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_URL, JnjWebUtil.getSiteUrl(request));
		storeCmsPageInModel(model, getContentPageForLabelOrId(INVOICE_DETAIL_CMS_PAGE));
		//return getViewForPage(model);
		if(null !=orderData && "KE".equals(orderData.getOrderType()))
		{
			return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Invoice.InvoiceDetailChargePage);
		}
		else
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Invoice.InvoiceDetailPage);
	}
	catch (final UnknownIdentifierException exception)
	{
		LOGGER.warn("Attempted to load a order that does not exist or is not visible", exception);
		return REDIRECT_ORDER_HISTORY;
	}
}
	@PostMapping(INVOICE_DETAIL_PATH)
	public String invoiceDetailsPOST(@PathVariable("orderCode") final String orderCode, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		
		return invoiceDetails(orderCode, model, request, true);
	}

	@GetMapping("/order/surgeonInformation")
	public String getSurgeonInformationPopup(final String orderCode, final Model model)
	{
		model.addAttribute("surgeonInfo", orderFacade.getSurgeonInformation(orderCode));
		model.addAttribute("popUpType", POP_UP_TYPE.SURGEON_INFORMATION);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//return ControllerConstants.Views.Fragments.Order.OrderHistoryPopup;
		return "";
	}

	@PostMapping(INVOICE_DETAIL_DOWNLOAD_PATH)
	public String invoiceDetailsDownload(@RequestParam("invoiceNumber") final String invoiceNumber,
			@RequestParam("downloadType") final String downloadType, final Model model) throws CMSItemNotFoundException
	{
		final JnjGTInvoiceOrderData invoice = jnjGTInvoiceFacade.getInvoiceDetailsByInvoiceNumber(invoiceNumber);
		model.addAttribute("orderInvoice", invoice);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		/*sessionService.removeAttribute(Jnjb2bCoreConstants.CURRENT_PAGE_TYPE);
		sessionService.removeAttribute("orderDetailPageSize");*/
		final OrderData orderData = orderFacade.getOrderDetailsForCode(invoice.getOrderNumber());
		model.addAttribute("orderData", orderData);
		model.addAttribute("siteLogoPath", orderFacade.getImagePathForOrderandInvoiceDownloads("epicEmailLogoImageOne"));
		
		final String currentSite = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME);
		model.addAttribute(Jnjb2bglobalordertemplateConstants.SITE_NAME, currentSite);
		return (DOWNLOAD_TYPE.PDF.toString().equals(downloadType)) ? INVOICE_DETAIL_PDF : INVOICE_DETAIL_EXCEL;
	}

	@PostMapping("/addToCart")
	public String addToCart(@RequestParam("prodIds") final String prodIds, final Model model) throws CMSItemNotFoundException
	{
		boolean forceNewEntry = orderUtil.isForceNewEntryEnabled();
		
		final Map<String, String> selectedProducts = new HashMap<String, String>();
		String[] productCodeAndQty = null;
		if (StringUtils.isNotEmpty(prodIds))
		{
			// Create product code array
			final String[] products = prodIds.replaceAll("\n", "").split(LoginaddonConstants.SYMBOl_COMMA);
		
		for (final String selectedProd : products)
		{
			 
			productCodeAndQty = selectedProd.split(":", 2);
			final String qty = "0".equals(productCodeAndQty[1]) ? "0" : productCodeAndQty[1];
			selectedProducts.put(productCodeAndQty[0], qty);
		}
		//	final List<ProductData> addedProducts = new ArrayList<ProductData>(selectedProducts.size());
		}
		final String currentSite = sessionService.getAttribute(LoginaddonConstants.SITE_NAME);
		JnjCartModificationData jnjCartModificationData = null;
		//Hot fix branch changes by NA team
		sessionService.setAttribute("addtoCartFromOrderHistoryPage", true);
		//Changes to allow regions to enable/disable forceNew Entry
		if(forceNewEntry){
			jnjCartModificationData = jnjGTCartFacade.addToCartGT(selectedProducts, true, false,false,0);
		}else{
			jnjCartModificationData = jnjGTCartFacade.addToCartGT(selectedProducts, true, false);
		}
		
		model.addAttribute(ADD_STATUS, selectedProducts);
		if (!CollectionUtils.isEmpty(jnjCartModificationData.getCartModifications()))
		{
			
			final CartModificationData cartModificationData = jnjCartModificationData.getCartModifications().get(0);
			if (null != cartModificationData.getEntry())
			{
				
				model.addAttribute("entry", cartModificationData.getEntry());
				model.addAttribute("product", cartModificationData.getEntry().getProduct());
				model.addAttribute(LINES, String.valueOf(jnjCartModificationData.getTotalUnitCount()));
				model.addAttribute(CART_MOD_DATA, jnjCartModificationData);
			}
			if (cartModificationData.getQuantityAdded() == 0L)
			{
				model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModificationData.getStatusCode());
			}
		} 
//			return getView(CartandcheckoutaddonControllerConstants.Views.Fragments.Cart.AddToCartPopup);
	 
	
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Cart.AddToCartPopup);
		
	}

	@PostMapping("/selectAccount")
	public String getSelectAccountPopup(final Model model, final String[] selectedAccounts, final JnjGTOrderHistoryForm form,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showMore", defaultValue = "false") final String showMore,
			@RequestParam(value = "showMoreCounter", defaultValue = "1") final String showMoreCounter,
			@RequestParam(value = "searchTerm", defaultValue = "", required = false) final String searchTerm)
	{

		final int pageSizeFromConfig = Config.getInt(ACCOUNT_SELECTION_RESULTS_PER_PAGE_KEY, 50);

		final int finalPageSize = (Boolean.valueOf(showMore)) ? pageSizeFromConfig * Integer.valueOf(showMoreCounter)
				: pageSizeFromConfig;
		final JnjGTPageableData pageableData = createPageableData(page, finalPageSize, JnJB2BUnitModel.UID, showMode);

		if (StringUtils.isNotEmpty(searchTerm))
		{
			pageableData.setSearchBy(searchTerm);
			model.addAttribute(ACCOUNT_SEARCH_TERM, searchTerm); // setting search term
		}
		/** Calling facade layer **/
		final JnjGTAccountSelectionData accountSelectionData = jnjGTCustomerFacade.getAccountsMap(true, true, false, pageableData);

		model.addAttribute("popUpType", POP_UP_TYPE.SELECT_ACCOUNTS);
		model.addAttribute("accounts", accountSelectionData.getAccountsMap());
		model.addAttribute("currentAccountId", orderFacade.getCurrentB2bUnitId());
		model.addAttribute("selectedAccounts", selectedAccounts);
		model.addAttribute("orderHistoryForm", form);

		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute(ACCOUNT_PAGINATION_DATA, accountSelectionData.getPaginationData()); // setting pagination data
		model.addAttribute(CURRENT_PAGE, showMoreCounter);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Template.OrderHistoryPopups);
	}

	@GetMapping("/updatePurchaseOrder")
	public String getPurchaseOrderPopup(final Model model)
	{
		model.addAttribute("popUpType", POP_UP_TYPE.UPDATE_PO);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//return ControllerConstants.Views.Fragments.Order.OrderHistoryPopup;
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Order.OrderHistoryPopup);
	}

	@PostMapping("/updatePurchaseOrderRequest")
	@ResponseBody
	public boolean updatePurchaseOrderNumber(final String orderNumber, final String purchaseOrderNumber)
	{
		
		try
		{
			return orderFacade.submitPOOrderChangeRequest(orderNumber, purchaseOrderNumber);
		}
		catch (SystemException | IntegrationException exception)
		{
			LOGGER.error(Logging.ORDER_HISTORY_UPDATE_PO + Logging.HYPHEN + "updatePurchaseOrderNumber()" + Logging.HYPHEN
					+ "Integration | System Exception occured " + exception.getMessage(), exception);
			return false;
		}
	}

	@PostMapping("/surgeonData")
	public String getSurgeonData(final Model model,
			@RequestParam(value = "loadMoreCounter", defaultValue = "1") final int loadMoreCounter,
			@RequestParam(value = "searchPattern", required = false) final String searchPattern)
	{
		model.addAttribute("pagedSurgeonData", orderFacade.getSurgeonData(searchPattern, loadMoreCounter));
		model.addAttribute("loadMoreCounter", Integer.valueOf(loadMoreCounter + 1));
		model.addAttribute("popUpType", POP_UP_TYPE.UPDATE_SURGEON);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		//return ControllerConstants.Views.Fragments.Order.OrderHistoryPopup;
		return "";
	}

	@PostMapping("/updateSurgeonGTRequest")
	@ResponseBody
	public String updateSurgeonRequest(final String selectedSurgeonId, final String orderNumber,final String selectSurgeonName,
			final String hospitalId) throws CMSItemNotFoundException
	{
		try
		{
			return orderFacade.submitSurgeonOrderChangeRequest(orderNumber, selectedSurgeonId,selectSurgeonName, hospitalId);
		}
		catch (SystemException | IntegrationException exception)
		{
			LOGGER.error(Logging.ORDER_HISTORY_UPDATE_PO + Logging.HYPHEN + "updateSurgeonRequest()" + Logging.HYPHEN
					+ "Integration | System Exception occured " + exception.getMessage(), exception);
			return StringUtils.EMPTY;
		}
	}

	
/*<AAOL-3681>	*/
	@GetMapping(DISPUTE_ORDER_PATH)
	public String disputeDetails(@RequestParam(value = "orderCode") final String orderCode, final Model model,
			final HttpServletRequest request, final boolean ignoreRestriction) throws CMSItemNotFoundException
	{
		
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		final JnjGTDisputeOrderInquiryForm form = new JnjGTDisputeOrderInquiryForm();
		populateDisputeOrderInqForm((JnjGTOrderData) orderData, form);

		model.addAttribute("orderData", orderData);
		model.addAttribute("disputeOrderInquiryForm", form);
		model.addAttribute("pageType", PAGE_TYPE.DISPUTE_ORDER);
		model.addAttribute("countries", jnjCustomerFacade.getCountries());
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
				.add(new Breadcrumb("/order-history", jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("disputeOrderPage.heading"), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(DISPUTE_ORDER_CMS_PAGE));
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Template.DisputeOrderAndItemPage);
	}

	@PostMapping("/submitDisputeOrdInq")
	public String submitDisputeOrderInquiry(final JnjGTDisputeOrderInquiryForm form, final Model model,
			final RedirectAttributes redirectAttributes, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final JnjGTDisputeOrderInquiryDto dto = new JnjGTDisputeOrderInquiryDto();
		if (form.getTaxExemptCertificate() != null && form.getTaxExemptCertificate().getOriginalFilename() != null
				&& !form.getTaxExemptCertificate().getOriginalFilename().isEmpty())
		{
			final MultipartFile taxExemptMultipartFile = form.getTaxExemptCertificate();
			final String filePath = Config.getParameter(Jnjb2bglobalordertemplateConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_FILE_PATH_KEY);
			final File file = new File(filePath + Jnjb2bglobalordertemplateConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_FILE_PREFIX
					+ taxExemptMultipartFile.getOriginalFilename());

			try
			{
				file.createNewFile();
				taxExemptMultipartFile.transferTo(file);
				dto.setCreatedFileName(file.getName());
			}
			catch (IllegalStateException | IOException e)
			{
				LOGGER.error("Could not create new file or transfer content from multipart data.");
				e.printStackTrace();
			}
		}
		populateDisputeOrderDto(dto, form);
		//model.addAttribute("disputeFlag", orderFacade.sendDisputeorderInquiry(dto, form.getOrderCode()));
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		redirectAttributes.addFlashAttribute("disputeFlag", orderFacade.sendDisputeorderInquiry(dto, form.getOrderCode()));
		return REDIRECT_PREFIX + "/order-history/order/" + form.getOrderCode();
		//return orderDetail(form.getOrderCode(), new JnjGTSearchSortForm(), model, request);
	}

	/*<AAOL-3648>	*/
	@GetMapping(DISPUTE_ITEM_PATH)
	public String disputeLine(@RequestParam(value = "orderCode") final String orderCode,
			@RequestParam(value = "productCode") final String productCode,
			@RequestParam(value = "contractNum") final String contractNum,
			@RequestParam(value = "totalPrice") final String totalPrice, final Model model) throws CMSItemNotFoundException
	
	{
		
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		final JnjGTDisputeItemInquiryForm form = new JnjGTDisputeItemInquiryForm();

		form.setTotalDisputedAmount(totalPrice);
		populateDisputeItemInqForm((JnjGTOrderData) orderData, form, productCode, contractNum);

		model.addAttribute("disputeItemInquiryForm", form);
		model.addAttribute("pageType", PAGE_TYPE.DISPUTE_ITEM);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
				.add(new Breadcrumb("/order-history", jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null));
		breadcrumbs.add(new Breadcrumb("/order-history/order/" + orderCode, jnjCommonFacadeUtil
				.getMessageFromImpex("orderDetailPage.heading"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("disputeItemPage.heading"), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(DISPUTE_ORDER_CMS_PAGE));
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Template.DisputeOrderAndItemPage);
	}

	@PostMapping("/submitDisputeItemInq")
	@ResponseBody
	public boolean submitDisputeItemInquiry(final JnjGTDisputeItemInquiryForm form, final Model model)
	{
		final JnjGTDisputeItemInquiryDto dto = new JnjGTDisputeItemInquiryDto();
		populateDisputeItemDto(dto, form);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		return orderFacade.sendDisputeorderInquiry(dto, form.getOrderCode());
	}
	
	/*Implementing DisputeItems from Invoice */
	/*AAOL-4059*/
	@GetMapping(INVOICE_DISPUTE_ITEM_PATH)
	public String disputeLine(@RequestParam(value = "orderCode") final String orderCode,
			@RequestParam(value = "productCode") final String productCode,
			@RequestParam(value = "contractNum") final String contractNum,
			@RequestParam(value = "totalPrice") final String totalPrice,
			@RequestParam(value = "invoiceNum") final String invoiceNum,
			@RequestParam(value = "qty") final String qty,
			final Model model) throws CMSItemNotFoundException
	{
			LOGGER.info("Inside Dispute Line Item");
		
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		final JnjGTDisputeItemInquiryForm form = new JnjGTDisputeItemInquiryForm();
		final JnjGTInvoiceOrderData invoice = jnjGTInvoiceFacade.getInvoiceDetailsByInvoiceNumber(invoiceNum);
		final List<JnjGTInvoiceEntryData> invoiceEntry = invoice.getInvoiceEntries();

		form.setDisputeInvoiceNumber(invoiceNum);

		populateDisputeItemInqForm((JnjGTOrderData) orderData, form, productCode, contractNum);

		model.addAttribute("orderData", orderData);
		model.addAttribute("disputeItemInquiryForm", form);
		model.addAttribute("pageType", PAGE_TYPE.DISPUTE_INVOICE_ITEM);
		model.addAttribute("invoice", invoiceEntry);
		
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
				.add(new Breadcrumb("/order-history", jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null));
		breadcrumbs.add(new Breadcrumb("/order-history/order/" + orderCode, jnjCommonFacadeUtil
				.getMessageFromImpex("orderDetailPage.heading"), null));
		breadcrumbs.add(new Breadcrumb("/order-history/order/invoiceDetail/" + orderCode, jnjCommonFacadeUtil
				.getMessageFromImpex("invoiceDetailPage.heading"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("disputeItemPage.heading"), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(DISPUTE_ORDER_CMS_PAGE));
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Fragments.Template.DisputeOrderAndItemPage);
	}

	
	@PostMapping("/invoiceSubmitDisputeItemInq")
	public String submitDisputeItemInquiry(final JnjGTDisputeItemInquiryForm form, final Model model,
			final RedirectAttributes redirectAttributes, final HttpServletRequest request)
	{
		LOGGER.info("Inside submit Dispute Line Item");
		final JnjGTDisputeItemInquiryDto dto = new JnjGTDisputeItemInquiryDto();
		populateDisputeItemInvoiceDto(dto, form);
		redirectAttributes.addFlashAttribute("disputeFlag", orderFacade.sendDisputeorderInquiry(dto, form.getOrderCode()));
		
		return REDIRECT_PREFIX + "/order-history/order/invoiceDetail/" + form.getOrderCode() + "?&param2=true";

	}
	
	/*AAOL-4059*/
	
	//TODO [AR]: To be completed once Shipment Tracking info/clarification arrives on outbound service.
	@RequestMapping(value = "/trackLineItem")
	public String trackOrderLine(@RequestParam(value = "orderCode") final String orderCode,
			@RequestParam(value = "lineCode") final String lineCode, @RequestParam(value = "trackingId") final String trackingId,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException

	{
		return getOrderHistory(new JnjGTOrderHistoryForm(), model,request);
	}

	//TODO [AR]: To be completed once Shipment Tracking info/clarification arrives on outbound service
	@RequestMapping(value = "/trackOrder")
	public String trackOrder(@RequestParam(value = "orderCode", required = false) final String orderCode,
			@RequestParam(value = "trackingId", required = false) final String trackingId, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException

	{
		return getOrderHistory(new JnjGTOrderHistoryForm(), model,request);
	}

	@GetMapping("/deliveryProof")
	public void getDeliveryProof(@RequestParam(value = "trackingId") final String trackingId,
			@RequestParam(value = "shipDate") final String shipDate, final HttpServletResponse response)
	{
		final File documentFile = orderFacade.getDeliveryProof(trackingId, shipDate);
		if (documentFile != null)
		{
			FileInputStream inputStream = null;
			OutputStream outStream = null;
			try
			{
				inputStream = new FileInputStream(documentFile);

				/*
				 * Set content type, header key & value for the output response. Document file content is written to the
				 * output stream of the response to generate the content on download.
				 */
				final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
				String mimeType = mimeTypesMap.getContentType(documentFile.getName());

				if (mimeType == null)
				{
					mimeType = "application/octet-stream";
				}

				response.setContentType(mimeType);
				response.setContentLength((int) documentFile.length());

				final String headerKey = "Content-Disposition";
				final String headerValue = String.format("attachment; filename=\"%s\"", documentFile.getName());
				response.setHeader(headerKey, headerValue);

				outStream = response.getOutputStream();

				final byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				while ((bytesRead = inputStream.read(buffer)) != -1)
				{
					outStream.write(buffer, 0, bytesRead);
				}
				response.flushBuffer();
			}
			catch (final IOException exception)
			{
				LOGGER.error("Reading/Writing of Proof of Delivery document to the output response stream has caused an exception: "
						+ exception.getMessage());
			}
			finally
			{
				try
				{
					if (inputStream != null)
					{
						inputStream.close();
					}

					if (outStream != null)
					{
						outStream.close();
					}
				}
				catch (final IOException exception)
				{
					LOGGER.error("Closing Input/Output stream has caused an exception: " + exception.getMessage());
				}
			}
		}
		else
		{
			LOGGER.error("Could not find 'Proof of Delivery' document, exiting the request.");
		}
	}

	/**
	 * Populates drop down options of Order History page for:
	 *
	 * <ul>
	 * <li>Sort By</li>
	 * <li>Channel</li>
	 * <li>Line Status</li>
	 * <li>Status</li>
	 * <li>Order Type</li>
	 * <li>Search By</li>
	 * </ul>
	 *
	 * @param model
	 */
	protected void populateDropDownOptions(final Model model, final String currentSite, final SelectedAccountType accountType)
	{
		final List<SelectOption> orderTypes = new ArrayList<>();
		/*** Populate Order types only those listed in requirements and not all. ***/
		populateOrderTypes(orderTypes);

		final List<SelectOption> orderStatus = new ArrayList<>();
		/*** Populate Order status drop down options based on the account selected, i.e. MDD, CONSUMER or BOTH ***/
		if (SelectedAccountType.MDD.equals(accountType))
		{
			orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(), enumerationService
					.getEnumerationName(OrderStatus.CREATED)));
			orderStatus.add(new SelectOption(OrderStatus.PENDING.getCode(), enumerationService
					.getEnumerationName(OrderStatus.PENDING)));
			orderStatus.add(new SelectOption(OrderStatus.SHIPPED.toString(), enumerationService
					.getEnumerationName(OrderStatus.SHIPPED)));
			orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CANCELLED)));
			orderStatus.add(new SelectOption(OrderStatus.PARTIALLY_SHIPPED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.PARTIALLY_SHIPPED)));
			orderStatus.add(new SelectOption(OrderStatus.INCOMPLETE.getCode(), enumerationService
					.getEnumerationName(OrderStatus.INCOMPLETE)));
			orderStatus.add(new SelectOption(OrderStatus.ACCEPTED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.ACCEPTED)));
			orderStatus.add(new SelectOption(OrderStatus.BACKORDERED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.BACKORDERED)));
			orderStatus.add(new SelectOption(OrderStatus.INVOICED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.INVOICED)));
			orderStatus.add(new SelectOption(OrderStatus.RELEASED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.RELEASED)));
		}
		else if (SelectedAccountType.CONSUMER.equals(accountType))
		{
			orderStatus.add(new SelectOption(OrderStatus.BEING_PROCESSED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.BEING_PROCESSED)));
			orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(), enumerationService
					.getEnumerationName(OrderStatus.CREATED)));
			orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CANCELLED)));
			orderStatus.add(new SelectOption(OrderStatus.CREDIT_HOLD.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CREDIT_HOLD)));
			orderStatus.add(new SelectOption(OrderStatus.IN_PICKING.getCode(), enumerationService
					.getEnumerationName(OrderStatus.IN_PICKING)));
			orderStatus.add(new SelectOption(OrderStatus.COMPLETED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.COMPLETED)));
		}
		else
		{
			orderStatus.add(new SelectOption(OrderStatus.PENDING.getCode(), enumerationService
					.getEnumerationName(OrderStatus.PENDING)));
			orderStatus.add(new SelectOption(OrderStatus.SHIPPED.toString(), enumerationService
					.getEnumerationName(OrderStatus.SHIPPED)));
			orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CANCELLED)));
			orderStatus.add(new SelectOption(OrderStatus.PARTIALLY_SHIPPED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.PARTIALLY_SHIPPED)));
			orderStatus.add(new SelectOption(OrderStatus.INCOMPLETE.getCode(), enumerationService
					.getEnumerationName(OrderStatus.INCOMPLETE)));
			orderStatus.add(new SelectOption(OrderStatus.ACCEPTED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.ACCEPTED)));
			orderStatus.add(new SelectOption(OrderStatus.BACKORDERED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.BACKORDERED)));
			orderStatus.add(new SelectOption(OrderStatus.INVOICED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.INVOICED)));
			orderStatus.add(new SelectOption(OrderStatus.IN_PROCESS.getCode(), enumerationService
					.getEnumerationName(OrderStatus.IN_PROCESS)));
			orderStatus.add(new SelectOption(OrderStatus.DELIVERED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.DELIVERED)));
			orderStatus.add(new SelectOption(OrderStatus.BEING_PROCESSED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.BEING_PROCESSED)));
			orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(), enumerationService
					.getEnumerationName(OrderStatus.CREATED)));
			orderStatus.add(new SelectOption(OrderStatus.CREDIT_HOLD.getCode(), enumerationService
					.getEnumerationName(OrderStatus.CREDIT_HOLD)));
			orderStatus.add(new SelectOption(OrderStatus.IN_PICKING.getCode(), enumerationService
					.getEnumerationName(OrderStatus.IN_PICKING)));
			orderStatus.add(new SelectOption(OrderStatus.COMPLETED.getCode(), enumerationService
					.getEnumerationName(OrderStatus.COMPLETED)));
			
		}

		final List<SelectOption> lineStatus = new ArrayList<>();
		//populateLineStatus(lineStatus); //fix for jjepic 544

		/*Added the below changes for ECPH-2207 Consumer*/
		if (SelectedAccountType.CONSUMER.equals(accountType))
		{
			lineStatus.add(new SelectOption(OrderEntryStatus.BEING_PROCESSED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.BEING_PROCESSED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.CANCELLED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.CANCELLED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.COMPLETED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.COMPLETED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.CREATED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.CREATED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.IN_PICKING.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.IN_PICKING))));
			lineStatus.add(new SelectOption(OrderEntryStatus.ON_HOLD.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.ON_HOLD))));
		
		} else{
			lineStatus.add(new SelectOption(OrderEntryStatus.PENDING.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.PENDING))));
			lineStatus.add(new SelectOption(OrderEntryStatus.CANCELLED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.CANCELLED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.BACKORDERED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.BACKORDERED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.SHIPPED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.SHIPPED))));
			lineStatus.add(new SelectOption(OrderEntryStatus.INVOICED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.INVOICED))));
	        lineStatus.add(new SelectOption(OrderEntryStatus.CONFIRMED.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.CONFIRMED))));
	        lineStatus.add(new SelectOption(OrderEntryStatus.ITEM_ACCEPTED.getCode(), enumerationService
	        				.getEnumerationName((OrderEntryStatus.ITEM_ACCEPTED))));
	         /*For AAOL-3608 Start*/
	        lineStatus.add(new SelectOption(OrderEntryStatus.OPEN.getCode(), enumerationService
					.getEnumerationName((OrderEntryStatus.OPEN))));
	        /*For AAOL-3608 End*/
		}
		/*** Populate Sort By options. ***/
		final List<String> sortOptions = new ArrayList<>();
		sortOptions.add(Order.SortOption.DEFAULT_SORT_CODE);
		sortOptions.add(Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST);
		sortOptions.add(Order.SortOption.ORDER_NUMBER_DECREASING);
		sortOptions.add(Order.SortOption.ORDER_NUMBER_INCREASING);
		sortOptions.add(Order.SortOption.ORDER_TOTAL_DECREASING);
		sortOptions.add(Order.SortOption.ORDER_TOTAL_INCREASING);

		/*** Populate Search By options. ***/
		List<String> searchOptions = new ArrayList<>();
		populateSearchOptions(searchOptions, currentSite);

		/*** Populate Order Channel options based on the code, and corresponding label from message inventory. ***/
		final List<SelectOption> channelOptions = new ArrayList<>();
		populateChannelOptions(channelOptions);
		
		//Changes for Adding Surgeon drop-down AAOL-4796
		/*final List<SelectOption> surgeonOptions = new ArrayList<>();
		populateSurgeonOption(surgeonOptions);*/
		
		//Changes for Adding Franchise drop-down AAOL-4732
		final List<SelectOption> franchiseOptions = new ArrayList<>();
		populateFranchiseOptions(franchiseOptions);
		
		model.addAttribute("orderTypes", orderTypes);
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("lineStatus", lineStatus);
		model.addAttribute("channels", channelOptions);
		model.addAttribute("sortOptions", sortOptions);
		model.addAttribute("searchOptions", searchOptions);
		model.addAttribute("franchiseOptions", franchiseOptions);
//		model.addAttribute("surgeonOptions", surgeonOptions);
	}
	
	/**
	 * This method is used to populate Surgeon drop down AAOL-4796
	 * @param surgeonOption
	 */
	/*protected void populateSurgeonOption(List<SelectOption> surgeonOption) {
		
		List<JnjGTSurgeonData> surgeonSearchData = orderFacade.getSurgeonData();
		
		for(JnjGTSurgeonData surgeonData: surgeonSearchData){
			if(surgeonData.getDisplayName()!=null && surgeonData.getCode()!=null){
				surgeonOption.add(new SelectOption(surgeonData.getCode(),surgeonData.getDisplayName()+" "+ surgeonData.getCode()));
			}
			
		}
		
	}*/

	/**
	 * This method is used to populate Franchise Description dropdown
	 * @param franchiseOptions
	 */
	//Changed for AAOL-5219
	protected void populateFranchiseOptions(List<SelectOption> franchiseOptions) {
		
		boolean isAdmin = false;
		if(sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE)!=null && 
				sessionService.getAttribute(LoginaddonConstants.Login.USER_TYPE).equals(JnjGTUserTypes.PORTAL_ADMIN)) {
			isAdmin = true;
		}
		
		if(isAdmin){
			final CategoryModel category = commerceCategoryService.getCategoryForCode("Categories");
			for (final CategoryModel subCategory : category.getCategories()){
				franchiseOptions.add(new SelectOption(subCategory.getName(), subCategory.getName()));
			}
		}
		//Filter applied for Non-Admin Users	
		else{
			final List<CategoryModel> allowedfranchise = jnjGTCustomerFacade.getUserFranchise();
			for (final CategoryModel categories :  allowedfranchise){
				franchiseOptions.add(new SelectOption(categories.getName(),categories.getName()));
			}
		}
	}
			

	protected List<SelectOption> populateOrderStatus(final List<SelectOption> orderStatus)
	{
		
		
		//JJEPIC790
	/*	orderStatus.add(new SelectOption(OrderStatus.PENDING.getCode(), enumerationService
				.getEnumerationName(OrderStatus.PENDING)));
		orderStatus.add(new SelectOption(OrderStatus.SHIPPED.toString(), enumerationService
				.getEnumerationName(OrderStatus.SHIPPED)));*/
		orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.CANCELLED)));
		/*orderStatus.add(new SelectOption(OrderStatus.PARTIALLY_SHIPPED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.PARTIALLY_SHIPPED)));
		orderStatus.add(new SelectOption(OrderStatus.INCOMPLETE.getCode(), enumerationService
				.getEnumerationName(OrderStatus.INCOMPLETE)));
		orderStatus.add(new SelectOption(OrderStatus.ACCEPTED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.ACCEPTED)));
		orderStatus.add(new SelectOption(OrderStatus.BACKORDERED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.BACKORDERED)));
		orderStatus.add(new SelectOption(OrderStatus.INVOICED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.INVOICED)));
		orderStatus.add(new SelectOption(OrderStatus.IN_PROCESS.getCode(), enumerationService
				.getEnumerationName(OrderStatus.IN_PROCESS)));
		orderStatus.add(new SelectOption(OrderStatus.DELIVERED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.DELIVERED)));*/
		orderStatus.add(new SelectOption(OrderStatus.BEING_PROCESSED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.BEING_PROCESSED)));
		orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(), enumerationService
				.getEnumerationName(OrderStatus.CREATED)));
		orderStatus.add(new SelectOption(OrderStatus.CREDIT_HOLD.getCode(), enumerationService
				.getEnumerationName(OrderStatus.CREDIT_HOLD)));
		orderStatus.add(new SelectOption(OrderStatus.IN_PICKING.getCode(), enumerationService
				.getEnumerationName(OrderStatus.IN_PICKING)));
		orderStatus.add(new SelectOption(OrderStatus.COMPLETED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.COMPLETED)));
		
		return orderStatus;
	}
	
	protected List<String> populateSearchOptions(final List<String> searchOptions, final String currentSite){
		searchOptions.add(Order.SearchOption.ORDER_NUMBER);
		searchOptions.add(Order.SearchOption.PRODUCT_NUMBER);
		searchOptions.add(Order.SearchOption.PO_NUMBER);
		if (Jnjb2bglobalordertemplateConstants.MDD.equals(currentSite))
		{
			searchOptions.add(Order.SearchOption.DEALER_PO_NUMBER);
		}
		searchOptions.add(Order.SearchOption.INVOICE_NUMBER);
		//Added by Pradeep chandupatla for search the Orders using Credit and Debit Memo for GTR-1797
		if (Jnjb2bglobalordertemplateConstants.MDD.equals(currentSite))
		{
			/*searchOptions.add(Order.SearchOption.CREDIT_MEMO);
			searchOptions.add(Order.SearchOption.DEBIT_MEMO);*/
		}
		return searchOptions;
	}
	
	/*protected List<SelectOption> populateChannelOptions(final List<SelectOption> channelOptions){
		populateChannelOptions(channelOptions);

		model.addAttribute("orderTypes", orderTypes);
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("lineStatus", lineStatus);
		model.addAttribute("channels", channelOptions);
		model.addAttribute("sortOptions", sortOptions);
		model.addAttribute("searchOptions", searchOptions);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
	}
	*/
	/*protected List<SelectOption> populateOrderStatus(final List<SelectOption> orderStatus)
	{
		orderStatus.add(new SelectOption(OrderStatus.BEING_PROCESSED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.BEING_PROCESSED)));
		orderStatus.add(new SelectOption(OrderStatus.CREATED.toString(), enumerationService
				.getEnumerationName(OrderStatus.CREATED)));
		orderStatus.add(new SelectOption(OrderStatus.CANCELLED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.CANCELLED)));
		orderStatus.add(new SelectOption(OrderStatus.CREDIT_HOLD.getCode(), enumerationService
				.getEnumerationName(OrderStatus.CREDIT_HOLD)));
		orderStatus.add(new SelectOption(OrderStatus.IN_PICKING.getCode(), enumerationService
				.getEnumerationName(OrderStatus.IN_PICKING)));
		orderStatus.add(new SelectOption(OrderStatus.COMPLETED.getCode(), enumerationService
				.getEnumerationName(OrderStatus.COMPLETED)));
		return orderStatus;
	}*/
	
	protected List<SelectOption> populateChannelOptions(final List<SelectOption> channelOptions)
	{
		channelOptions.add(new SelectOption(Order.ChannelOption.PHONE, Order.ChannelOption.PHONE));
		channelOptions.add(new SelectOption(Order.ChannelOption.FAX, Order.ChannelOption.FAX));
		channelOptions.add(new SelectOption(Order.ChannelOption.EMAIL, Order.ChannelOption.EMAIL));
		channelOptions.add(new SelectOption(Order.ChannelOption.WEB, Order.ChannelOption.WEB));
		channelOptions.add(new SelectOption(Order.ChannelOption.EDI, Order.ChannelOption.EDI));
		channelOptions.add(new SelectOption(Order.ChannelOption.VMI, Order.ChannelOption.VMI));
		channelOptions.add(new SelectOption(Order.ChannelOption.OTHER, Order.ChannelOption.OTHER));
		return channelOptions;
	}

	protected List<SelectOption> populateLineStatus(List<SelectOption> lineStatus){	
	/*** Populate Line Level Status options. ***/
	
		//LineaddedJune
		
		
	/*lineStatus.add(new SelectOption(OrderEntryStatus.PENDING.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.PENDING))));
	lineStatus.add(new SelectOption(OrderEntryStatus.CANCELLED.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.CANCELLED))));
	lineStatus.add(new SelectOption(OrderEntryStatus.BACKORDERED.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.BACKORDERED))));
	lineStatus.add(new SelectOption(OrderEntryStatus.OPEN.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.OPEN))));
	lineStatus.add(new SelectOption(OrderEntryStatus.SHIPPED.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.SHIPPED))));
	lineStatus.add(new SelectOption(OrderEntryStatus.INVOICED.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.INVOICED))));
            lineStatus.add(new SelectOption(OrderEntryStatus.CONFIRMED.getCode(), enumerationService
			.getEnumerationName((OrderEntryStatus.CONFIRMED))));*/
		
          
		
     return lineStatus;
	}
	
	
	
	
	protected List<SelectOption> populateOrderTypes(List<SelectOption> orderTypes){		
	
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZOR.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZOR)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZDEL.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZDEL)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZLZ.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZLZ)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZNC.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZNC)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZKB.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZKB)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZEX.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZEX)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZRE.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZRE)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZQT.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZQT)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.KA.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.KA)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.KB.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.KB)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.KE.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.KE)));
	//Added by Pradeep Chandupatla new order types Credit Memo and debit memo
	/*orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZCR.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZCR)));
	orderTypes.add(new SelectOption(JnjOrderTypesEnum.ZDR.getCode(), enumerationService
			.getEnumerationName(JnjOrderTypesEnum.ZDR)));*/

	/*commented by tharun to hide credit and debit memos*/
	
	return orderTypes;
	}

	/**
	 * Data class used to hold drop down select options and respective values. Holds the code identifier as well as the
	 * display name.
	 */
	public static class SelectOption
	{
		protected final String code;
		protected final String name;

		public SelectOption(final String code, final String name)
		{
			this.code = code;
			this.name = name;
		}

		public String getCode()
		{
			return code;
		}

		public String getName()
		{
			return name;
		}
	}

	/**
	 * Populates <code>JnjGTDisputeOrderInquiryForm</code> with the fields to be defaulted on the Dispute Order page.
	 *
	 * @param data
	 * @param form
	 */
	protected void populateDisputeOrderInqForm(final JnjGTOrderData data, final JnjGTDisputeOrderInquiryForm form)
	{
		form.setOrderCode(data.getCode());
		form.setAccountNumber(data.getAccountNumber());
		form.setPurchaseOrderNumber(data.getPurchaseOrderNumber());
		form.setShipToAddress(data.getDeliveryAddress());
	}


	/**
	 * Populates <code>JnjGTDisputeItemInquiryForm</code> with the fields to be defaulted on the Dispute Item page.
	 *
	 * @param data
	 * @param form
	 * @param productCode
	 */
	protected void populateDisputeItemInqForm(final JnjGTOrderData data, final JnjGTDisputeItemInquiryForm form,
			final String productCode, final String contractNum)
	{
		form.setOrderCode(data.getCode());
		form.setAccountNumber(data.getAccountNumber());
		form.setPurchaseOrderNumber(data.getPurchaseOrderNumber());
		form.setOverShippedProductCode(productCode);
		form.setShortShippedProductCode(productCode);
		form.setContractNumber(contractNum);
	}

	/**
	 * Populates <code>JnjGTDisputeOrderInquiryDto</code> with the fields to be sent for submission of dispute order
	 * inquiry.
	 *
	 * @param data
	 * @param form
	 */
	protected void populateDisputeOrderDto(final JnjGTDisputeOrderInquiryDto dto, final JnjGTDisputeOrderInquiryForm form)
	{
		dto.setCertificateAttached((form.getTaxExemptCertificate() != null
				&& form.getTaxExemptCertificate().getOriginalFilename() != null && !form.getTaxExemptCertificate()
				.getOriginalFilename().isEmpty()) ? true : false);

		dto.setAccountNumber(form.getAccountNumber());
		dto.setPurchaseOrderNumber(form.getPurchaseOrderNumber());

		dto.setShipToAddress(form.getShipToAddress());
		dto.setCorrectAddress(form.getCorrectAddress());

		dto.setCorrectPurchaseOrderNumber(form.getCorrectPurchaseOrderNumber());
		dto.setNewPONumber(form.getNewPONumber());

		final Map<String, List<String>> productAndLotMap = new HashMap<>();
		final Map<String, String> productAndQtyMap = new HashMap<>();

		/*** Set product-lot info and product-qty based Key value pairs by splitting the input values received in form. ***/
		if (form.getUnorderedProductsInfo() != null && form.getUnorderedProductsInfo().length > 0)
		{
			String[] productLotQty = null;
			String[] lotAndQty = null;
			String productCode = "";
			for (final String unorderedProdInfo : form.getUnorderedProductsInfo())
			{
				productLotQty = unorderedProdInfo.split(":", 2);
				productCode = productLotQty[0];

				if (productLotQty[1] != null)
				{
					final List<String> lotInfo = new ArrayList<>();

					lotAndQty = productLotQty[1].split("~", 2);
					lotInfo.addAll(Arrays.asList(lotAndQty[0].split("^")));

					if (!lotInfo.isEmpty())
					{
						productAndLotMap.put(productCode, lotInfo);
					}

					productAndQtyMap.put(productCode, lotAndQty[1]);
				}
			}
		}

		dto.setInCorrectAddressDispute(form.isInCorrectAddressDispute());
		dto.setProductsAndlotInfo(productAndLotMap);
		dto.setProductsAndQuantity(productAndQtyMap);

		dto.setKeepProductsShipped(form.isKeepProductsShipped());
		dto.setDisputedFees(form.getDisputedFees() != null ? Arrays.asList(form.getDisputedFees()) : null);
	}

	public void setAscDescCounter(final Model model, final JnjGTOrderHistoryForm form)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		LOGGER.info("\n\nform sort code "+form.getSortCode());
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_NUMBER_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn1", Jnjb2bCoreConstants.Order.SortOption.ORDER_NUMBER_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_NUMBER_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn1", Jnjb2bCoreConstants.Order.SortOption.ORDER_NUMBER_INCREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.PO_NUMBER_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn2", Jnjb2bCoreConstants.Order.SortOption.PO_NUMBER_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.PO_NUMBER_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn2", Jnjb2bCoreConstants.Order.SortOption.PO_NUMBER_INCREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_TYPE_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn3", Jnjb2bCoreConstants.Order.SortOption.ORDER_TYPE_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_TYPE_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn3", Jnjb2bCoreConstants.Order.SortOption.ORDER_TYPE_INCREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.SHIP_TO_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn4", Jnjb2bCoreConstants.Order.SortOption.SHIP_TO_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.SHIP_TO_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn4", Jnjb2bCoreConstants.Order.SortOption.SHIP_TO_INCREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.DEFAULT_SORT_CODE))
		{
			model.addAttribute("ascDescCounter_hcolumn5", Jnjb2bCoreConstants.Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_DATE_OLDEST_TO_NEWEST))
		{
			model.addAttribute("ascDescCounter_hcolumn5", Jnjb2bCoreConstants.Order.SortOption.DEFAULT_SORT_CODE);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.CHANNEL_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn6", Jnjb2bCoreConstants.Order.SortOption.CHANNEL_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.CHANNEL_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn6", Jnjb2bCoreConstants.Order.SortOption.CHANNEL_INCREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.STATUS_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn7", Jnjb2bCoreConstants.Order.SortOption.STATUS_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.STATUS_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn7", Jnjb2bCoreConstants.Order.SortOption.STATUS_INCREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_TOTAL_INCREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn8", Jnjb2bCoreConstants.Order.SortOption.ORDER_TOTAL_DECREASING);
		}
		if (form.getSortCode() != null && form.getSortCode().equalsIgnoreCase(Jnjb2bCoreConstants.Order.SortOption.ORDER_TOTAL_DECREASING))
		{
			model.addAttribute("ascDescCounter_hcolumn8", Jnjb2bCoreConstants.Order.SortOption.ORDER_TOTAL_INCREASING);
		}
	}

	/**
	 * Populates <code>JnjGTDisputeItemInquiryDto</code> with the fields to be sent for submission of dispute item
	 * inquiry.
	 *
	 * @param dto
	 * @param form
	 */
	
	protected void populateDisputeItemDto(final JnjGTDisputeItemInquiryDto dto, final JnjGTDisputeItemInquiryForm form)
	{
		dto.setAccountNumber(form.getAccountNumber());
		dto.setPurchaseOrderNumber(form.getPurchaseOrderNumber());
		if (form.getLotNumbers() != null)
		{
			dto.setLotNumbers(Arrays.asList(form.getLotNumbers().split(",")));
		}

		dto.setTotalDisputedAmount(form.getTotalDisputedAmount());
		dto.setExpectedPrice(form.getExpectedPrice());
		dto.setContractNumber(form.getContractNumber());

		dto.setShortShippedProductCode(form.getShortShippedProductCode());
		dto.setShortShippedOrderedQuantity(form.getShortShippedOrderedQuantity());
		dto.setShortShippedReceivedQuantity(form.getShortShippedReceivedQuantity());
		dto.setReplacementRequired(form.isReplacementRequired());

		dto.setOverShippedProductCode(form.getOverShippedProductCode());
		dto.setOverShippedOrderedQuantity(form.getOverShippedOrderedQuantity());
		dto.setOverShippedReceivedQuantity(form.getOverShippedReceivedQuantity());

		dto.setKeepProductsShipped(form.isKeepProductsShipped());
		dto.setNewPurchaseOrderNumber(form.getNewPurchaseOrderNumber());
	}
	
	/*AAOL-4059*/	
	protected void populateDisputeItemInvoiceDto(final JnjGTDisputeItemInquiryDto dto, final JnjGTDisputeItemInquiryForm form)
	{
		LOGGER.info("########Inside populateDisputeItemInvoiceDto");

		final List<String> productCodes = new ArrayList<String>();
		final List<String> contractNumbers = new ArrayList<String>();
		final List<String> quantity = new ArrayList<String>();

		/***
		 * Set product-lot info and product-qty based Key value pairs by splitting the input values received in form. for
		 * Pricing
		 ***/
		if (form.getUnorderedPricingProductsInfo() != null && form.getUnorderedPricingProductsInfo().length > 0)
		{
			LOGGER.info("########Inside populateDisputeItemInvoiceDto inside loop 1st");
			final Map<String, List<String>> productAndQtyMap = new HashMap<>();
			final Map<String, String> productAndDispPriceMap = new HashMap<>();
			final Map<String, String> productAndCorrectPriceMap = new HashMap<>();
			final Map<String, String> productAndContractNumberMap = new HashMap<>();
			final Map<String, String> productAndInvoiceNumberMap = new HashMap<>();
			String[] productLotQty = null;
			String[] lotAndQty = null;
			String productCode = "";
			int indexOfProduct = 0;
		
			for (final String unorderedProdInfo : form.getUnorderedPricingProductsInfo() ) 
			{
				
				productLotQty = unorderedProdInfo.split(":", 5);
				productCode = String.valueOf(indexOfProduct) + Jnjb2bCoreConstants.Solr.COLON + productLotQty[0];
				indexOfProduct++;
				if (productLotQty[1] != null)
				{
					final List<String> lotInfo = new ArrayList<>();

					lotAndQty = productLotQty[1].split("~", 5);
					lotInfo.addAll(Arrays.asList(lotAndQty[0].split(";")));

					if (!lotInfo.isEmpty())
					{
						productAndQtyMap.put(productCode, lotInfo);
					}
				
						productCodes.add(productLotQty[0]);
			
					
					LOGGER.info("########Inside product added in 1st ");
					quantity.add(lotAndQty[0]);
					contractNumbers.add(lotAndQty[3]);

					productAndDispPriceMap.put(productCode, lotAndQty[1]);
					productAndCorrectPriceMap.put(productCode, lotAndQty[2]);
					productAndContractNumberMap.put(productCode, lotAndQty[3]);
					productAndInvoiceNumberMap.put(productCode, lotAndQty[4]);

				}
			}

			dto.setProductsAndQuantityPrice(productAndQtyMap);
			dto.setProductsAndDisputedPrice(productAndDispPriceMap);
			dto.setProductsAndCorrectPrice(productAndCorrectPriceMap);
			dto.setProductsAndContractNumber(productAndContractNumberMap);
			dto.setProductsAndInvoiceNumber(productAndInvoiceNumberMap);
			dto.setDisputeType("Pricing");
		}






		/***
		 * Set product-lot info and product-qty based Key value pairs by splitting the input values received in form. for
		 * Short shipped
		 ***/
		if (form.getUnorderedShortShippedProductsInfo() != null && form.getUnorderedShortShippedProductsInfo().length > 0)
		{
			LOGGER.info("########Inside populateDisputeItemInvoiceDto inside loop2nd");
			final Map<String, List<String>> productAndQtyReceivedMap = new HashMap<>();
			final Map<String, String> productAndQtyOrderedMap = new HashMap<>();
			final Map<String, String> productAndReplacementMap = new HashMap<>();
			final Map<String, String> productAndInvoiceNumberShortMap = new HashMap<>();
			String[] productLotQty = null;
			String[] lotAndQty = null;
			String productCode = "";
			int indexOfProduct = 0;
			for (final String unorderedProdInfo : form.getUnorderedShortShippedProductsInfo())
			{
				productLotQty = unorderedProdInfo.split(":", 4);
				productCode = String.valueOf(indexOfProduct) + Jnjb2bCoreConstants.Solr.COLON + productLotQty[0];
				indexOfProduct++;
				if (productLotQty[1] != null)
				{
					final List<String> lotInfo = new ArrayList<>();

					lotAndQty = productLotQty[1].split("~", 4);
					lotInfo.addAll(Arrays.asList(lotAndQty[0].split(";")));

					if (!lotInfo.isEmpty())
					{
						productAndQtyReceivedMap.put(productCode, lotInfo);
					}
					productCodes.add(productLotQty[0]);
					LOGGER.info("########Inside product added in 2nd ");
					quantity.add(lotAndQty[1]);
					productAndQtyOrderedMap.put(productCode, lotAndQty[1]);
					productAndReplacementMap.put(productCode, lotAndQty[2]);
					productAndInvoiceNumberShortMap.put(productCode, lotAndQty[3]);

				}
			}


			dto.setProductsAndQuantityRecieved(productAndQtyReceivedMap);
			dto.setProductsAndQuantityOrdered(productAndQtyOrderedMap);
			dto.setProductsAndReplacement(productAndReplacementMap);
			dto.setProductsAndInvoiceNumberShort(productAndInvoiceNumberShortMap);
			dto.setDisputeType("Shortshipping");
		}
		/***
		 * Set product-lot info and product-qty based Key value pairs by splitting the input values received in form. for
		 * Over shipped
		 ***/
		if (form.getUnorderedOverShippedProductsInfo() != null && form.getUnorderedOverShippedProductsInfo().length > 0)
		{
			LOGGER.info("########Inside populateDisputeItemInvoiceDto inside loop 3rd");
			final Map<String, List<String>> productAndQtyReceivedOverMap = new HashMap<>();
			final Map<String, String> productAndQtyOrderedOverMap = new HashMap<>();
			final Map<String, String> productAndLotNumbersMap = new HashMap<>();
			final Map<String, String> productAndInvoiceNumberOverMap = new HashMap<>();

			String[] productLotQty = null;
			String[] lotAndQty = null;
			String productCode = "";
			int indexOfProduct = 0;
			for (final String unorderedProdInfo : form.getUnorderedOverShippedProductsInfo())
			{
				productLotQty = unorderedProdInfo.split(":", 4);
				productCode = String.valueOf(indexOfProduct) + Jnjb2bCoreConstants.Solr.COLON + productLotQty[0];
				indexOfProduct++;
				if (productLotQty[1] != null)
				{
					final List<String> lotInfo = new ArrayList<>();

					lotAndQty = productLotQty[1].split("~", 4);
					lotInfo.addAll(Arrays.asList(lotAndQty[0].split(";")));

					if (!lotInfo.isEmpty())
					{
						productAndQtyReceivedOverMap.put(productCode, lotInfo);
					}
					productCodes.add(productLotQty[0]);
					LOGGER.info("########Inside product added in 3rd ");
					quantity.add(lotAndQty[1]);
					productAndQtyOrderedOverMap.put(productCode, lotAndQty[1]);
					productAndLotNumbersMap.put(productCode, lotAndQty[2]);
					productAndInvoiceNumberOverMap.put(productCode, lotAndQty[3]);

				}
			}

			dto.setProductsAndQuantityReceivedOver(productAndQtyReceivedOverMap);
			dto.setProductsAndQuantityOrderedOver(productAndQtyOrderedOverMap);
			dto.setProductsAndLotNumbers(productAndLotNumbersMap);
			dto.setProductsAndInvoiceNumberOver(productAndInvoiceNumberOverMap);
			dto.setDisputeType("Overshipping");
		}
		final String randomNum = String.valueOf(Math.abs(getRandom().nextInt()));

		dto.setDisputeItemNumber(randomNum);
		dto.setOrderCode(form.getOrderCode());
		dto.setAccountNumber(form.getAccountNumber());
		dto.setDisputeInvoiceNumber(form.getDisputeInvoiceNumber());
		dto.setPurchaseOrderNumber(form.getPurchaseOrderNumber());
		dto.setProductCode(productCodes);
		dto.setContactNumber(contractNumbers);
		dto.setQuantity(quantity);

		//dto.setDisputeItemFlag(true);

	
	}
	
	/*AAOL-4059*/
	
	protected SecureRandom getRandom()
	{
		return random;
	}

	@Override
	protected JnjGTPageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final JnjGTPageableData pageableData = new JnjGTPageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	protected void setOrderDetailBreadCrumb(final Model model)
	{
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bglobalordertemplateConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs
				.add(new Breadcrumb("/order-history", jnjCommonFacadeUtil.getMessageFromImpex("orderHistoryPage.heading"), null));
		breadcrumbs.add(new Breadcrumb(null, jnjCommonFacadeUtil.getMessageFromImpex("orderDetailPage.heading"), null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
	}

	public String getView(final String view){
        return Jnjb2bglobalordertemplateControllerConstants.ADDON_PREFIX + view;
 }

	
	// Newly added Method
	
	/**
	 * Gets the nfe file.
	 *
	 * @param fileType
	 *           the file type
	 * @param salesOrder
	 *           the sales order
	 * @param orderCode
	 *           the order code
	 * @param model
	 *           the model
	 * @param httpServletResponse
	 *           the http servlet response
	 * @param poNumber
	 *           the po number
	 * @param invoiceId
	 *           the invoice id
	 * @throws BusinessException
	 *            the business exception
	 */
	/*@RequestMapping(value = "/invoiceDocFile")
	public String getInvoiceDocFile(@RequestParam(value = "downloadType") final String fileType,
			@RequestParam(value = "salesOrder") final String salesOrder, @RequestParam(value = "orderCode") final String orderCode,
			final Model model, final HttpServletResponse httpServletResponse,final RedirectAttributes redirectModel, @RequestParam(value = "poNumber") final String poNumber,
			@RequestParam(value = "invoiceNumber")  String invoiceId) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(JnjcaCoreConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocMapper()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		String errorMessage = null;
		File invoiceDocFile = null;
		//invoiceId = "90000542";
		try
		{
			invoiceDocFile = jnjInvioceFacade.getInvoiceDocFile(fileType, invoiceId);
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(JnjcaCoreConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()" + Logging.HYPHEN
					+ "BusinessException Occured:" + Logging.HYPHEN + businessException.getMessage());
			model.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorMessage","invoice.error.notfound");
			return REDIRECT_ORDER_HISTORY+"/order/invoiceDetail/"+orderCode ;
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			LOGGER.error(JnjcaCoreConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()" + Logging.HYPHEN
					+ "IllegalArgumentException Occured:" + Logging.HYPHEN + illegalArgumentException.getMessage());
			model.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorMessage","invoice.error.notfound");
			return REDIRECT_ORDER_HISTORY+"/order/invoiceDetail/"+orderCode ;
		}
		catch (final IntegrationException integrationException)
		{
			LOGGER.error(JnjcaCoreConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()" + Logging.HYPHEN
					+ "IntegrationException Occured:" + Logging.HYPHEN + integrationException.getMessage());
			model.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorMessage","invoice.availability.message");
			return REDIRECT_ORDER_HISTORY+"/order/invoiceDetail/"+orderCode ;
			
		}
		catch (final Throwable throwable)
		{
			LOGGER.error(JnjcaCoreConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()" + Logging.HYPHEN
					+ "Exception Occured other then business, illegal argument and integration exception: " + Logging.HYPHEN
					+ throwable.getMessage());
			model.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorMessage","invoice.availability.message");
			return REDIRECT_ORDER_HISTORY+"/order/invoiceDetail/"+orderCode ;
		}
		try
		{
			if (null != invoiceDocFile && invoiceDocFile.exists())
			{
				final FileInputStream fileInputStream = new FileInputStream(invoiceDocFile);
				// if the file type is xml then set the content type and header for the xml file
				if (StringUtils.isNotEmpty(fileType) && WebConstants.InvoiceDetails.FILE_TYPE.contains(fileType))
				{
					httpServletResponse.setContentType(fileType);
					httpServletResponse.setHeader(WebConstants.InvoiceDetails.HEADER_PARAM,
							WebConstants.InvoiceDetails.HEADER_PARAM_VALUE + invoiceId + WebConstants.InvoiceDetails.FILE_TYPE);
				}
				//else the file type is pdf so set the content type and header for it
				else
				{
					httpServletResponse.setContentType(fileType);
					httpServletResponse.setHeader(WebConstants.InvoiceDetails.HEADER_PARAM,
							WebConstants.InvoiceDetails.HEADER_PARAM_VALUE + invoiceId + WebConstants.InvoiceDetails.PDF_FILE_TYPE);
				}
				httpServletResponse.setContentLength(new Long(invoiceDocFile.length()).intValue());
				FileCopyUtils.copy(fileInputStream, httpServletResponse.getOutputStream());
				fileInputStream.close();
				//httpServletResponse.getOutputStream().close();
				invoiceDocFile.delete();
			}
			
		}
		catch (final IOException exception)
		{
			model.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorInvoiceMessage", Boolean.TRUE);
			redirectModel.addAttribute("errorMessage","invoice.availability.message");
			return REDIRECT_ORDER_HISTORY+"/order/invoiceDetail/"+orderCode ;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(JnjcaCoreConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocMapper()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return null;
		
	}*/
	
	
	/**
	 * Utility method used for logging custom messages in debug mode.
	 *
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param message
	 *           the message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

	/**
	 * Populate inovice details in session.
	 *
	 * @param salesOrder
	 *           the sales order
	 * @param poNumber
	 *           the po number
	 * @param orderCode
	 *           the order code
	 * @param errorMessage
	 *           the error message
	 */
	protected void populateInoviceDetailsInSession(final String salesOrder, final String poNumber, final String orderCode,
			final String errorMessage)
	{
		sessionService.setAttribute("salesOrder", salesOrder);
		sessionService.setAttribute("poNum", poNumber);
		sessionService.setAttribute("orderCode", orderCode);
		sessionService.setAttribute("invoiceDownloaderrorMessage", errorMessage);
	}

	protected void convertLineStatus(JnjGTOrderHistoryForm form)
	{
		switch (form.getLineStatus())
		{
			case ("Invoiced"):
				form.setLineStatus("INVOICED");
				
				break;

			case ("Open"):
				form.setLineStatus("OPEN");
				
				break;

			case ("Pending"):
				form.setLineStatus("PENDING");
				
				break;

			case ("Backordered"):
				form.setLineStatus("BACKORDERED");
				
				break;

			case ("Shipped"):
				form.setLineStatus("SHIPPED");
				
				break;

			case ("Confirmed"):
				form.setLineStatus("CONFIRMED");
				
				break;

			case ("Item Accepted"):
				form.setLineStatus("ITEM_ACCEPTED");
				
				break;
				
			case ("Cancelled"):
				form.setLineStatus("CANCELLED");
				
				break;
				
			case ("Completed"):
				form.setLineStatus("COMPLETED");
				
				break;
				
			case ("In Picking"):
				form.setLineStatus("IN_PICKING");
				
				break;
				
			case ("On Hold"):
				form.setLineStatus("ON_HOLD");
				
				break;
				
			case ("Created"):
				form.setLineStatus("CREATED");
				
				break;
				
			case ("Being Processed"):
				form.setLineStatus("BEING_PROCESSED");
				
				break;
		}
	}

	/**
	 * @param orderFacade the orderFacade to set
	 */
	public void setOrderFacade(JnjGTOrderFacade orderFacade) {
		this.orderFacade = orderFacade;
	}

	/**
	 * @param jnjCustomerFacade the jnjCustomerFacade to set
	 */
	public void setJnjCustomerFacade(JnjCustomerFacade jnjCustomerFacade) {
		this.jnjCustomerFacade = jnjCustomerFacade;
	}
	
	
	/*AAOL_3641*/
	@RequestMapping(value = "/orderValidation")
	@ResponseBody
	public Boolean orderValidation(@RequestParam(value = "orderCode") final String orderCode,
			 final Model model) throws CMSItemNotFoundException

	{
		try{
			OrderData order = orderFacade.getOrderDetailsForCode(orderCode);
			if (order != null && order.getCode() != null) {
				return true;
			} else {
				return false;
		}
		
		}catch(UnknownIdentifierException e){
			return false;
		}
		
		
	}
	
	/**
	 * This Method is used to fetch Order Surgery Info from Order-History page
	 * @param model
	 * @param orderNumber
	 * @return
	 */
	@GetMapping("/surgeryInfoOrderData")
	public String getOrderSurgeryInfo(final Model model, final String orderNumber)
	{
		final JnjGTOrderData orderData = (JnjGTOrderData) orderFacade.getOrderDetailsForCode(orderNumber);
		
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjb2bCoreConstants.Logging.SHOW_CHANGE_ACCOUNT);
		LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		model.addAttribute("orderData",orderData);
		return getView(Jnjb2bglobalordertemplateControllerConstants.Views.Pages.Order.SurgeryInfoOrderPopUp);
		//return "";
				
	}

}

