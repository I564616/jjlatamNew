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
package com.jnj.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorservices.config.HostConfigService;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorservices.storefront.data.MetaElementData;
import de.hybris.platform.acceleratorservices.storefront.util.PageTitleResolver;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.AbstractController;
import com.jnj.b2b.storefront.controllers.ThirdPartyConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.View;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * Base controller for all page controllers. Provides common functionality for all page controllers.
 */
public abstract class AbstractPageController extends AbstractController
{
	private static final Logger LOG = Logger.getLogger(AbstractPageController.class);
	
	public static final String PAGE_ROOT = "pages/";
	public static final String ROOT = "/";
	public static final String REDIRECT_PREFIX = "redirect:";

	public static final String CMS_PAGE_MODEL = "cmsPage";
	public static final String CMS_PAGE_TITLE = "pageTitle";

	protected static final String PRODUCT_CATALOG = "productCatalog";
	protected static final String ONLINE = "Online";

	/*@Resource(name = "jnjGTCustomerFacade")
	private JnjGTCustomerFacade customerFacades;*/

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "pageTitleResolver")
	private PageTitleResolver pageTitleResolver;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "hostConfigService")
	private HostConfigService hostConfigService;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;
	
	@Resource(name="commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Autowired
	protected JnJCommonUtil jnJCommonUtil;
	
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	@Resource(name="jnjGTB2BCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjGTB2BCommerceUserService;

	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	protected CatalogVersionService catalogVersionService;
	
	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	protected HostConfigService getHostConfigService()
	{
		return hostConfigService;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@ModelAttribute("showContractLink")
	public String showContractLink()
	{
		String showContractLink = StringUtils.EMPTY;
		if(sessionService.getAttribute("sessionShowContractLink") != null)
		{
			showContractLink = sessionService.getAttribute("sessionShowContractLink");
		}
		return showContractLink;
	}
	@ModelAttribute("languages")
	public Collection<LanguageData> getLanguages()
	{
		return storeSessionFacade.getAllLanguages();
	}

	@ModelAttribute("currencies")
	public Collection<CurrencyData> getCurrencies()
	{
		return storeSessionFacade.getAllCurrencies();
	}

	@ModelAttribute("currentLanguage")
	public LanguageData getCurrentLanguage()
	{
		return storeSessionFacade.getCurrentLanguage();
	}

	@ModelAttribute("currentCurrency")
	public CurrencyData getCurrentCurrency()
	{
		return storeSessionFacade.getCurrentCurrency();
	}
	@ModelAttribute("pdfPopupGenrationFlg")
	public boolean getPDFPopupGenration()
	{
		return isPDFGeneration();
	}
	@ModelAttribute("siteName")
	public String getSiteName()
	{
		final CMSSiteModel site = cmsSiteService.getCurrentSite();
		return site != null ? site.getJnjWebSiteType().toString() : "";
	}

	 @ModelAttribute("user")
	public CustomerData getUser()
	{
		//return customerFacades.getCurrentGTCustomer();
		 return customerFacade.getCurrentCustomer();
	} 

	@ModelAttribute("googleUserAccountNumber")
	protected String getGoolgeUserAccountNumber() {

		final String accountNumber = jnJCommonUtil.getCurrentUserAccountNumber();
		LOG.debug("google change Account number:" + accountNumber);
		return accountNumber;
	}
	
	@ModelAttribute("googleUserSector")
	protected String getGoogleUserSector()
	{
		final String userSector = jnjGTB2BCommerceUserService.getCurrentUserSector();
		if(LOG.isDebugEnabled()) 
		{
			LOG.debug("google  User Sector:" + userSector);
		}
		return userSector;
	}
	
	@ModelAttribute("googleAnalyticsTrackingId")
	public String getGoogleAnalyticsTrackingId(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(ThirdPartyConstants.Google.ANALYTICS_TRACKING_ID, request.getServerName());
	}
	/* Google reCaptcha*/
	@ModelAttribute("googleCaptchaLanguageIso")
	public String getGoogleCaptchaLanguage(final HttpServletRequest request)
	{
		String langIso = Jnjb2bCoreConstants.RECAPTCHA_LANGUAGE + "." + commerceCommonI18NService.getCurrentLanguage().getIsocode();
		return getHostConfigService().getProperty(langIso, request.getServerName());
	}
	
	@ModelAttribute("googleCaptchaKey")
	public String getGoogleCaptchaKey(final HttpServletRequest request)
	{
		String langIso = Jnjb2bCoreConstants.RECAPTCHA_PUBLIC_KEY + "." + jnJCommonUtil.getLocale();
		return getHostConfigService().getProperty(langIso, request.getServerName());
	}


	@ModelAttribute("jirafeApiUrl")
	public String getJirafeApiUrl(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(ThirdPartyConstants.Jirafe.API_URL, request.getServerName());
	}

	@ModelAttribute("jirafeApiToken")
	public String getJirafeApitoken(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(ThirdPartyConstants.Jirafe.API_TOKEN, request.getServerName());
	}

	@ModelAttribute("jirafeApplicationId")
	public String getJirafeApplicationId(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(ThirdPartyConstants.Jirafe.APPLICATION_ID, request.getServerName());
	}

	@ModelAttribute("jirafeSiteId")
	public String getJirafeSiteId(final HttpServletRequest request)
	{
		String propertyValue = getHostConfigService().getProperty(
				ThirdPartyConstants.Jirafe.SITE_ID + "." + storeSessionFacade.getCurrentCurrency().getIsocode().toLowerCase(),
				request.getServerName());
		if ("".equals(propertyValue))
		{
			propertyValue = getHostConfigService().getProperty(ThirdPartyConstants.Jirafe.SITE_ID, request.getServerName());
		}
		return propertyValue;
	}

	@ModelAttribute("jirafeVersion")
	public String getJirafeVersion(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(ThirdPartyConstants.Jirafe.VERSION, request.getServerName());
	}

	@ModelAttribute("jirafeDataUrl")
	public String getJirafeDataUrl(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(ThirdPartyConstants.Jirafe.DATA_URL, request.getServerName());
	}

	protected void storeCmsPageInModel(final Model model, final AbstractPageModel cmsPage)
	{
		if (model != null && cmsPage != null)
		{
			model.addAttribute(CMS_PAGE_MODEL, cmsPage);
			if (cmsPage instanceof ContentPageModel)
			{
				storeContentPageTitleInModel(model, getPageTitleResolver().resolveContentPageTitle(cmsPage.getTitle()));
			}
		}
	}

	protected void storeContentPageTitleInModel(final Model model, final String title)
	{
		model.addAttribute(CMS_PAGE_TITLE, title);
	}

	protected String getViewForPage(final Model model)
	{
		if (model.containsAttribute(CMS_PAGE_MODEL))
		{
			final AbstractPageModel page = (AbstractPageModel) model.asMap().get(CMS_PAGE_MODEL);
			if (page != null)
			{
				return getViewForPage(page);
			}
		}
		return null;
	}

	protected String getViewForPage(final AbstractPageModel page)
	{
		if (page != null)
		{
			final PageTemplateModel masterTemplate = page.getMasterTemplate();
			if (masterTemplate != null)
			{
				final String targetPage = cmsPageService.getFrontendTemplateName(masterTemplate);
				if (targetPage != null && !targetPage.isEmpty())
				{
					return PAGE_ROOT + targetPage;
				}
			}
		}
		return null;
	}

	/**
	 * Checks request URL against properly resolved URL and returns null if url is proper or redirection string if not.
	 * 
	 * @param request
	 *           - request that contains current URL
	 * @param response
	 *           - response to write "301 Moved Permanently" status to if redirected
	 * @param resolvedUrlPath
	 *           - properly resolved URL
	 * @return null if url is properly resolved or redirection string if not
	 * @throws UnsupportedEncodingException
	 */
	protected String checkRequestUrl(final HttpServletRequest request, final HttpServletResponse response,
			final String resolvedUrlPath) throws UnsupportedEncodingException
	{
		try
		{
			final String resolvedUrl = response.encodeURL(request.getContextPath() + resolvedUrlPath);
			final String requestURI = URIUtil.decode(request.getRequestURI(), "utf-8");
			final String decoded = URIUtil.decode(resolvedUrl, "utf-8");
			if (StringUtils.isNotEmpty(requestURI) && requestURI.endsWith(decoded))
			{
				return null;
			}
			else
			{
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);
				final String queryString = request.getQueryString();
				if (queryString != null && !queryString.isEmpty())
				{
					return "redirect:" + resolvedUrlPath + "?" + queryString;
				}
				return "redirect:" + resolvedUrlPath;
			}
		}
		catch (final URIException e)
		{
			throw new UnsupportedEncodingException();
		}
	}

	protected ContentPageModel getContentPageForLabelOrId(final String labelOrId) throws CMSItemNotFoundException
	{
		String key = labelOrId;
		if (StringUtils.isEmpty(labelOrId))
		{
			// Fallback to site home page
			final ContentPageModel homePage = cmsPageService.getHomepage();
			if (homePage != null)
			{
				key = cmsPageService.getLabelOrId(homePage);
			}
			else
			{
				// Fallback to site start page label
				final CMSSiteModel site = cmsSiteService.getCurrentSite();
				if (site != null)
				{
					key = cmsSiteService.getStartPageLabelOrId(site);
				}
			}
		}

		// Actually resolve the label or id - running cms restrictions
		return cmsPageService.getPageForLabelOrId(key);
	}

	protected PageTitleResolver getPageTitleResolver()
	{
		return pageTitleResolver;
	}

	protected void storeContinueUrl(final HttpServletRequest request)
	{
		final StringBuilder url = new StringBuilder();
		url.append(request.getServletPath());
		final String queryString = request.getQueryString();
		if (queryString != null && !queryString.isEmpty())
		{
			url.append('?').append(queryString);
		}
		getSessionService().setAttribute(WebConstants.CONTINUE_URL, url.toString());
	}

	protected void setUpMetaData(final Model model, final String metaKeywords, final String metaDescription)
	{
		final List<MetaElementData> metadata = new LinkedList<MetaElementData>();
		metadata.add(createMetaElement("keywords", metaKeywords));
		metadata.add(createMetaElement("description", metaDescription));
		model.addAttribute("metatags", metadata);
	}

	protected MetaElementData createMetaElement(final String name, final String content)
	{
		final MetaElementData element = new MetaElementData();
		element.setName(name);
		element.setContent(content);
		return element;
	}

	protected void setUpMetaDataForContentPage(final Model model, final ContentPageModel contentPage)
	{
		setUpMetaData(model, contentPage.getKeywords(), contentPage.getDescription());
	}

	protected RequestContextData getRequestContextData(final HttpServletRequest request)
	{
		return getBean(request, "requestContextData", RequestContextData.class);
	}
	private boolean isPDFGeneration() {
		
		// getting language from the url to access the footer link override pdf or not
		final String currentLanguage = getCurrentLanguage().getIsocode(); 
		if(Config.getParameter(Jnjb2bCoreConstants.BYEPASS_POPUP_TO_PDF_IN_LANGUAGE_LIST).contains(currentLanguage)){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}
	
	@ModelAttribute("countryCode")
	protected String getCountry(){
				String country= null;
				try{
					
					final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
					//country =  cmsSiteModel.getUid();
					
				List<CountryModel> countryModels=(List<CountryModel>) cmsSiteModel.getStores().get(0).getDeliveryCountries();
				
				country=countryModels.get(0).getIsocode();
				sessionService.getCurrentSession().setAttribute("countryCode", country);
				}catch(Exception e){
					e.printStackTrace();
				}		
				return country;
			}
	protected boolean validateCaptchaResponse(final String captchaResponse)
	{
		boolean isCaptchaResponseValid = Boolean.FALSE;
		if(StringUtils.isNotBlank(captchaResponse))
		{
			isCaptchaResponseValid = jnJCommonUtil.checkCaptcha(captchaResponse);
		}
		return isCaptchaResponseValid;
	}

	protected CatalogVersionModel getCurrentCatalog() {
		final String catalogId = sessionService.getCurrentSession().getAttribute(PRODUCT_CATALOG);
		return catalogVersionService.getCatalogVersion(catalogId, ONLINE);
	}

	@ModelAttribute("genesysDataURL")
	protected String getGenesysDataURL() {
		return configurationService.getConfiguration().getString("jnj.genesys.dataURL");
	}
	@ModelAttribute("genesysDeploymentKey")
	protected String getGenesysDeploymentKey() {
		return configurationService.getConfiguration().getString("jnj.genesys.deploymentKey");
	}
	@ModelAttribute("genesysOrgGuid")
	protected String getGenesysOrgGuid() {
		return configurationService.getConfiguration().getString("jnj.genesys.orgGuid");
	}
		
	@ModelAttribute("placeOrderResComUserGrpFlag")
	protected boolean getPlaceOrderResComUserGrpFlag() {
		return jnjCommonFacadeUtil.getPermissionsFlagsForCommercialUserGroup(Jnjb2bCoreConstants.GROUP_PLACE_ORDER_RES_COMM_USER);
	}
	@ModelAttribute("pharmaCommercialUserGroupFlag")
	protected boolean getPharmaCommercialUserGroupFlag() {
		return jnjCommonFacadeUtil.getPermissionsFlagsForCommercialUserGroup(Jnjb2bCoreConstants.GROUP_PHARMA_COMMERCIAL_USER);
	}
	@ModelAttribute("mddCommercialUserGroupFlag")
	protected boolean getMddCommercialUserGroupFlag() {
		return jnjCommonFacadeUtil.getPermissionsFlagsForCommercialUserGroup(Jnjb2bCoreConstants.GROUP_MDD_COMMERCIAL_USER);
	}
	
	@ModelAttribute("cloudmersiveAPIURL")
	protected String getCloudmersiveAPIURL() {
		return configurationService.getConfiguration().getString(Jnjb2bCoreConstants.CLOUDMERSIVE_API_URL);
	}
	
	@ModelAttribute("cloudmersiveAPIKey")
	protected String getCloudmersiveAPIKey() {
		return configurationService.getConfiguration().getString(Jnjb2bCoreConstants.CLOUDMERSIVE_API_KEY);
	}		
	
}
