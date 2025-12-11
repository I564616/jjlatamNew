package com.jnj.b2b.storefront.security;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.jnj.b2b.storefront.filters.StorefrontFilter;
import com.jnj.b2b.storefront.security.impl.WebHttpSessionRequestCache;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

public class RequestUtility{
	private Set<String> includeUrls;
	private WebHttpSessionRequestCache httpSessionRequestCache;
	private SessionService sessionService;
	private static final Logger LOGGER = Logger.getLogger(RequestUtility.class);
	
	public static final String REDIRECT_REFERER = "redirectReferer";
	public static final String ORIGINAL_REFERER = "originalReferer";

	@Autowired(required = true)
	private CMSSiteService cmsSiteService;
	
	@Autowired(required = true)
	private BaseSiteService baseSiteService;
	
	public boolean matchURLPattern(String url){
		boolean result = false;
		for (String urlPattern : includeUrls) {
			try{
			 Pattern compliedPattern = Pattern.compile(urlPattern);
			 Matcher matcher = compliedPattern.matcher(url);
			 result = matcher.matches();
			}
			catch(Exception e){
				LOGGER.debug("Exception occured while matching - urlPattern"+urlPattern+"-and -request url->"+url);
			}
			 if(result){
				 LOGGER.debug("Pattern matches for URL****************************  saving - urlPattern"+urlPattern+"-and -request url->"+url);
				 break;
			 }
		}
		return result;
	}
	
	public void saveRedirectUrl(final HttpServletRequest request, final HttpServletResponse response){
		sessionService.setAttribute(REDIRECT_REFERER, request.getRequestURL().toString());
		//httpSessionRequestCache.saveRequest(request, response);
	}
   

	public String getSavedRedirectUrl(final HttpServletRequest request, final HttpServletResponse response){
		String targetUrl = null;
		//SavedRequest savedRequest = httpSessionRequestCache.getRequest(request,response);
		
	    //	if(null!=savedRequest){
		//	targetUrl = savedRequest.getRedirectUrl();
		//}
		
		if(StringUtils.isBlank(targetUrl)){
			targetUrl = (String) sessionService.getAttribute(REDIRECT_REFERER);
			sessionService.removeAttribute("redirectUrl");
		}else if(targetUrl.contains("login")){
			targetUrl = "/home";
		}
		
		if(StringUtils.isBlank(targetUrl)){
			targetUrl = "/home";
		}
	
		String siteId = getSiteId(request, response);
		BaseSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		
		//The below should be uncommented after global page hosted in webserver
		
		/*if(StringUtils.isNotBlank(siteId) && !siteId.equalsIgnoreCase(cmsSiteModel.getUid())){
			targetUrl = "/login";
			sessionService.removeAttribute("redirectUrl");
		}*/
		
		return targetUrl;
	}
	
	
	public String getSiteId(final HttpServletRequest request, final HttpServletResponse response){
		BaseSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		String queryString = request.getQueryString();
		String siteId = null;
		if (StringUtils.isBlank(queryString) && null != cmsSiteModel) {

			String toSplitURl = StringUtils.removeEnd(request.getRequestURL().toString(), "/")
					+ (StringUtils.isBlank(queryString) ? "" : "?"
							+ queryString);
			String[] finalSplt = null;
			String[] spltByPath = toSplitURl
					.split(request.getContextPath());
			if (StringUtils.isNotBlank(spltByPath[1])) {
				finalSplt = spltByPath[1].split("/");
			}
			if (StringUtils.isNotBlank(finalSplt[1])) {
				siteId = finalSplt[1];
			}
		}
		LOGGER.debug("site id from url --> "+siteId);
		return siteId;
	}
	
	public boolean matchSites(final HttpServletRequest request, final HttpServletResponse response){
		
		String siteIso = getSiteId(request, response);
		BaseSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		
		/*if(StringUtils.isNotBlank(siteIso) && !siteIso.equalsIgnoreCase(cmsSiteModel.getDefaultCountry().getIsocode())){
			return true;
		}*/
		
		return false;
	}
	
	public Set<String> getIncludeUrls() {
		return includeUrls;
	}
	public void setIncludeUrls(Set<String> includeUrls) {
		this.includeUrls = includeUrls;
	}
	public WebHttpSessionRequestCache getHttpSessionRequestCache() {
		return httpSessionRequestCache;
	}
	public void setHttpSessionRequestCache(
			WebHttpSessionRequestCache httpSessionRequestCache) {
		this.httpSessionRequestCache = httpSessionRequestCache;
	}
	public SessionService getSessionService() {
		return sessionService;
	}
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
	
	
}
