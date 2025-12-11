/**
 * 
 */
package com.jnj.b2b.loginaddon.controllers.pages;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author nsinha7
 *
 */
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants.Logging;
import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.b2b.storefront.controllers.pages.AbstractPageController;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSerialResponseData;
import com.jnj.core.dto.JnjGTSerializationForm;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.serialization.JnjGTSerialFacade;
import com.jnj.services.CMSSiteService;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;


@Controller("JnjGTSerializationPageController")
@RequestMapping(value ="/serialization")
public class JnjGTSerializationPageController extends AbstractPageController 
{

	
	@Resource(name="GTSerialFacade")
	protected JnjGTSerialFacade jnjGTSerialFacade;
	
	public JnjGTSerialFacade getJnjGTVerifySerialFacade() {
		return jnjGTSerialFacade;
	}
	protected static final String SERIAL_HOME_PAGE = "serialHomePage";

	@Resource(name = "GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected BaseStoreService baseStoreService;
	
	@Autowired
	protected SessionService sessionService;
	
	/** The Enumerator DOWNLOAD_TYPE. **/
	protected enum DOWNLOAD_TYPE
	{
		/** The PDF. **/
		PDF,
		/** The EXCEL. **/
		EXCEL, NONE;
	}
	
	@Autowired
	protected CMSSiteService cMSSiteService;
	@Autowired
	protected MediaService mediaService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	
	protected static final Logger LOG = Logger.getLogger(JnjGTSerializationPageController.class);
	protected static final String UPDATED_PRIVACY_POLICY = "updatedLegalPolicy";
	protected static final String SERIALIZATION_RESULT_REPORT_PDF_VIEW = "JnJGTSerializationResultsPdfView";
	protected static final String SERIALIZATION_RESULT_EXCEL_VIEW = "JnJGTSerializationResultsExcelView";
	private static final String SERIALIZATION_RESULTS_NAME = "SerializationResults"; 
	protected static final String CART_CHECKOUT_PAGE = "cartCheckoutPage";
	
	/**
	 * This method gets the home page
	 *
	 * @param model
	 * @return view
	 * @throws CMSItemNotFoundException
	 */
	@GetMapping
	public String getSerialHome(final Model model,
			@RequestParam(value = "firstTimeLogin", defaultValue = "false", required = false) final boolean firstTimeLogin,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getHome()";
		boolean redirectToTarget = true;
				
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		sessionService.removeAttribute("responseData");
		// resetPassword start
		if (!isResetPasswordComplete()) {
			final String siteUrl = JnjWebUtil.getServerUrl(request);
			String userId = userService.getCurrentUser().getUid();
			String passwordResetUrl ="";
			try {
				passwordResetUrl = jnjGTCustomerFacade.resetPasswordUrlFirstTimeLogin(userId, siteUrl);
				logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "isResetPasswordComplete :: "+passwordResetUrl);
			} catch (UnsupportedEncodingException e) {
				//e.printStackTrace();
			}
			//System.out.println("test point : "+REDIRECT_PREFIX + "/register/resetPasswordForNewUser"+passwordResetUrl);
			return REDIRECT_PREFIX + "/register/resetPasswordForNewUser"+passwordResetUrl;  ///passwordExpiredEmail
		}
		//reset password end
		if (!isRegistrationComplete())
		{
			//return REDIRECT_PREFIX + "/register/activateUser"; // commented to redirect to profile page instead of old activate page
			return REDIRECT_PREFIX + "/my-account/personalInformation";
		}
		/**
		 * Check if firstTimeLogin is false, only then will we check for privacy policy update. This is because the first
		 * time login will come as true only when the updated privacy policy has been accepted
		 *
		 **/
		if (!firstTimeLogin)
		{
			/** Checking the Privacy Policy Versions **/
			final boolean updatedPrivacyPolicy = jnjGTCustomerFacade.checkPrivacyPolicyVersions();
			model.addAttribute(UPDATED_PRIVACY_POLICY, Boolean.valueOf(updatedPrivacyPolicy));
			logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "Privacy policy updated :: "
					+ updatedPrivacyPolicy);
			if (!updatedPrivacyPolicy)
			{
				redirectToTarget = false;

			}
		}

		/** Setting up the data for the CMS page to render **/
		setupModel(model, SERIAL_HOME_PAGE);
		/** fetching the page corresponding to the EPIC or PCM site to render **/
	  	return getSerialHomePage(model, firstTimeLogin, redirectToTarget,request);
 
	}
	
	
	
	/**
	 * This method is used to display the EPIC home page.
	 *
	 * @param model
	 * @param firstTimeLogin
	 * @return EPIC home page
	 */
	protected String getSerialHomePage(final Model model, final boolean firstTimeLogin, boolean redirectToTarget,final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final String METHOD_NAME = "getEPICHomePage()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		final Object firstTimeVisitAfterLogin = sessionService.getAttribute(LoginaddonConstants.Login.FIRST_TIME_LOGIN);
		
		model.addAttribute("currencySymbol", baseStoreService.getCurrentBaseStore().getDefaultCurrency().getSymbol());
		
		setSurveyFlag(model);
		
		model.addAttribute("homePageForm", new JnjGTSerializationForm());
		List<Integer> years = getYearRange();
		model.addAttribute("expiryYearRange", years);
		model.addAttribute("isSerialUser", true);
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);
		model.addAttribute("serialVerified", false);
		/*JnjGTSerialResponseData responseDataSerial = sessionService.getAttribute("serialResponseData");
		if(responseDataSerial!=null){
			model.addAttribute("responseData",responseDataSerial);
		}*/

		return getTargetView(redirectToTarget);
	}
	
	

	/**
	 * This method acts as controller to get response in serialization
	 * @param model
	 * @param jnjGTSerializationForm
	 * @return String
	 */
	@PostMapping
	public String verifySerial(final Model model, @ModelAttribute final JnjGTSerializationForm jnjGTSerializationForm){
		
		JnjGTSerialResponseData responseData = new JnjGTSerialResponseData();
		boolean downLoadPdf = false;
		if (null != jnjGTSerializationForm)	{
			responseData = jnjGTSerialFacade.verifySerial(jnjGTSerializationForm); 
		}
		
		sessionService.setAttribute("responseData", responseData);
		try {
			storeCmsPageInModel(model, getContentPageForLabelOrId(SERIAL_HOME_PAGE));
		} catch (CMSItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("verifySerialForm",jnjGTSerializationForm);
		model.addAttribute("responseData",responseData);
		//Change for AAOL-6197
		if(responseData.getStatus().equalsIgnoreCase(Jnjb2bCoreConstants.Serialization.KNOWN)){
			downLoadPdf = true;
		}
		model.addAttribute("serialVerified", true);
		model.addAttribute("isSerialUser", true);
		model.addAttribute("downLoadPdf", downLoadPdf);
		List<Integer> years = getYearRange();
		model.addAttribute("expiryYearRange", years);
		return getView(LoginaddonControllerConstants.Views.Pages.Home.SerialzationPage);
	}
	
	/**
	 * This method generates excel or pdf view
	 * @param model
	 * @param jnjGTSerializationForm
	 * @return String
	 */
	@PostMapping("/serializationResult")
	public String downloadSerializationSearchResults(final Model model, @ModelAttribute final JnjGTSerializationForm jnjGTSerializationForm ){
		final String METHOD_NAME = "downloadSerializationSearchResults()";
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
	    final CatalogVersionModel currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
		 /* Excel image adding Started here */
		model.addAttribute("siteLogoPath", mediaDirBase + File.separator + "sys_master" + File.separator
			+ mediaService.getMedia(currentCatalog, "epicEmailLogoImageOne").getLocation());
		 /* Excel image adding end here */
		/* PDF image adding Started here */
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
		
		 /* PDF image adding end here */
//		 model.addAttribute(SERIALIZATION_FORM_NAME, jnjGTSerializationForm);
		 JnjGTSerialResponseData responseData = new JnjGTSerialResponseData();
			if(null!=sessionService.getAttribute("responseData"));{
				responseData = sessionService.getAttribute("responseData");
			}
		 model.addAttribute(SERIALIZATION_RESULTS_NAME, responseData);
			/** Returning the view for PDF or excel **/
			return (String.valueOf(DOWNLOAD_TYPE.PDF).equalsIgnoreCase(jnjGTSerializationForm.getDownloadType())) ? SERIALIZATION_RESULT_REPORT_PDF_VIEW : SERIALIZATION_RESULT_EXCEL_VIEW;
		
	}
	
		
	/**
	 * This methods gets current year with list that includes +/- 10 years range
	 * @return List
	 */
	private List<Integer> getYearRange() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		for(int i=10;i>0;i--){
			years.add((year-i));
		}
		for(int i=0;i<=10;i++){
			years.add((year+i));
		}
		return years;
	} 
	
	
	/**
	 * Gets the registration status.
	 *
	 * @return the registration status
	 */
	protected boolean isResetPasswordComplete(){
		final String METHOD_NAME = "isResetPasswordComplete()";
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTCustomerFacade.isResetPasswordComplete();
	}
	
	/**
	 * Gets the registration status.
	 *
	 * @return the registration status
	 */
	protected boolean isRegistrationComplete()
	{
		final String METHOD_NAME = "isRegistrationComplete()";
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(LoginaddonConstants.Logging.LOGIN, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return jnjGTCustomerFacade.isRegistrationComplete();
	}
	
	/**
	 * Utility method used for logging entry into / exit from any method
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
	
	/**
	 * @return
	 */
	protected String getTargetView(final boolean redirectToTarget)
	{
		String targetUrl = LoginaddonControllerConstants.Views.Pages.Home.SerialzationPage;
 
		if (redirectToTarget)
		{
//			final String tmpTargetUrl = sessionService.getAttribute(LoginaddonConstants.JNJ_NA_TARGET_URL);
		    	final String tmpTargetUrl = sessionService.getAttribute(LoginaddonConstants.JNJ_GT_TARGET_URL);
			if (StringUtils.isNotEmpty(tmpTargetUrl))
			{
//				sessionService.removeAttribute(LoginaddonConstants.JNJ_NA_TARGET_URL);
			    	sessionService.removeAttribute(LoginaddonConstants.JNJ_GT_TARGET_URL);
				targetUrl = REDIRECT_PREFIX + tmpTargetUrl;
			}
		}
		LOG.info("targetUrl ----------"+targetUrl);
		return getView(targetUrl);
	}
	
	/**
	 * This method sets the CMS page association in the model.
	 *
	 * @param model
	 * @throws CMSItemNotFoundException
	 */
	protected void setupModel(final Model model, final String pageLabelOrId) throws CMSItemNotFoundException
	{

		final String METHOD_NAME = "setupModel()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(pageLabelOrId);
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
		logDebugMessage(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, "CMS Page loaded to the model");
		logMethodStartOrEnd(LoginaddonConstants.Logging.HOME_PAGE, METHOD_NAME, Logging.END_OF_METHOD);
	}
	/**
	 * This method sets the survey flag in the model
	 *
	 * @param model
	 */
	protected void setSurveyFlag(final Model model)
	{

		final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonConstants.Login.SHOW_CHANGE_ACCOUNT);
		LOG.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			LOG.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		final String METHOD_NAME = "setSurveyFlag()";
		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting the survey flag **/
		model.addAttribute(LoginaddonConstants.Login.SURVEY, Boolean.valueOf(jnjGTCustomerFacade.checkSurvey()));
		logDebugMessage(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, "Survey flag has been set.");

		logMethodStartOrEnd(LoginaddonConstants.Logging.SURVEY, METHOD_NAME, Logging.END_OF_METHOD);
	}
	
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
	
}