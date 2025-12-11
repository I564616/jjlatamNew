package com.jnj.facades.policies.impl;

import java.util.Locale;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.policies.JnjGTPoliciesFacade;
import com.jnj.services.CMSSiteService;




import de.hybris.platform.core.model.media.MediaModel;

public class JnjGTPoliciesFacadeImpl implements JnjGTPoliciesFacade
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTPoliciesFacadeImpl.class);
	@Resource(name = "i18nService")
	private I18NService i18nService;
	
	protected I18NService getI18nService()
	{
		return i18nService;
	}
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private CatalogVersionService catalogVersionService;
	
	@Autowired
	private CMSSiteService cmsSiteService;
	
	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}
	
	@ModelAttribute("siteName")
	public String getSiteName()
	{
		final CMSSiteModel site = cmsSiteService.getCurrentSite();
		return site != null ? site.getName() : "";
	}
	
	public String getCookiePolicies() throws CMSItemNotFoundException
	{
		LOGGER.info("In DARM Policies Facade - CookiePolicy");
		String url = StringUtils.EMPTY;
		String mediaCode = StringUtils.EMPTY;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		List<ContentCatalogModel> catologLst = null;
		Locale locale = null;
		try{
		if(cmsSiteModel != null)
		{
			catologLst = cmsSiteModel.getContentCatalogs();
			locale = getI18nService().getCurrentLocale();
			String siteId = StringUtils.EMPTY;
			siteId = cmsSiteModel.getUid();
			mediaCode=siteId+locale+"CookiePolicy";
		}
		if (catologLst != null && catologLst.size() >0 && StringUtils.isNotBlank(mediaCode))
		{
			MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), mediaCode);
			if(mediaModel != null)
			{
				url = mediaModel.getURL();
				return url;
			}
			else
			{
				return url;
			}
		}
		}
		catch(final Exception e)
		{
			LOGGER.error("Exception has occured while getting the media model for CookiePolicy",e);
		}
		return url;
		
	 }
	
	public String getTermsAndConditionsPolicies() throws CMSItemNotFoundException
	{
		LOGGER.info("In DARM Policies Facade - TermsAndCondition");
		String url = StringUtils.EMPTY;
		String mediaCode = StringUtils.EMPTY;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		List<ContentCatalogModel> catologLst = null;
		Locale locale = null;
		try{
		if(cmsSiteModel != null)
		{
			catologLst = cmsSiteModel.getContentCatalogs();
			locale = getI18nService().getCurrentLocale();
			String siteId = StringUtils.EMPTY;
			siteId = cmsSiteModel.getUid();
			mediaCode=siteId+locale+"TermsAndCondition";
		}
		if (catologLst != null && catologLst.size() >0 && StringUtils.isNotBlank(mediaCode))
		{
			MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), mediaCode);
			if(mediaModel != null)
			{
				url = mediaModel.getURL();
				return url;
			}
			else
			{
				return url;
			}
		}
		}
		catch(final Exception e)
		{
			LOGGER.error("Exception has occured while getting the media model for Terms and Conditions",e);
		}
		return url;
	}
	public String getUsefulLinks() throws CMSItemNotFoundException
	{
		LOGGER.info("In DARM Policies Facade - UsefulLinks");
		String url = StringUtils.EMPTY;
		String mediaCode = StringUtils.EMPTY;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		List<ContentCatalogModel> catologLst = null;
		Locale locale = null;
		try{
		if(cmsSiteModel != null)
		{
			catologLst = cmsSiteModel.getContentCatalogs();
			locale = getI18nService().getCurrentLocale();
			String siteId = StringUtils.EMPTY;
			siteId = cmsSiteModel.getUid();
			mediaCode=siteId+locale+"UsefulLinks";
		}
		if (catologLst != null && catologLst.size() >0 && StringUtils.isNotBlank(mediaCode))
		{
			MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), mediaCode);
			if(mediaModel != null)
			{
				url = mediaModel.getURL();
				return url;
			}
			else
			{
				return url;
			}
		}
		}
		catch(final Exception e)
		{
			LOGGER.error("Exception has occured while getting the media model for Useful Links",e);
		}
		return url;
	}
	
	public String getPrivacyPolicies() throws CMSItemNotFoundException
	{
		LOGGER.info("In DARM Policies Facade - PrivacyPolicy");
		String url = StringUtils.EMPTY;
		String mediaCode = StringUtils.EMPTY;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		List<ContentCatalogModel> catologLst = null;
		Locale locale = null;
		try{
		if(cmsSiteModel != null)
		{
			catologLst = cmsSiteModel.getContentCatalogs();
			locale = getI18nService().getCurrentLocale();
			String siteId = StringUtils.EMPTY;
			siteId = cmsSiteModel.getUid();
			mediaCode=siteId+locale+"PrivacyPolicy";
		}
		if (catologLst != null && catologLst.size() >0 && StringUtils.isNotBlank(mediaCode))
		{
			MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), mediaCode);
			if(mediaModel != null)
			{
				url = mediaModel.getURL();
				return url;
			}
			else
			{
				return url;
			}
		}
		}
		catch(final Exception e)
		{
			LOGGER.error("Exception has occured while getting the media model for PrivacyPolicy",e);
		}
		return url;
		
	}
	
	public String getLegalNotices() throws CMSItemNotFoundException
	{
		LOGGER.info("In DARM Policies Facade - LegalNotice");
		String url = StringUtils.EMPTY;
		String mediaCode = StringUtils.EMPTY;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		List<ContentCatalogModel> catologLst = null;
		Locale locale = null;
		try{
		if(cmsSiteModel != null)
		{
			catologLst = cmsSiteModel.getContentCatalogs();
			locale = getI18nService().getCurrentLocale();
			String siteId = StringUtils.EMPTY;
			siteId = cmsSiteModel.getUid();
			mediaCode=siteId+locale+"LegalNotice";
		}
		if (catologLst != null && catologLst.size() >0 && StringUtils.isNotBlank(mediaCode))
		{
			MediaModel mediaModel = mediaService.getMedia(catalogVersionService.getCatalogVersion(catologLst.get(0).getId(), Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE), mediaCode);
			if(mediaModel != null)
			{
				url = mediaModel.getURL();
				return url;
			}
			else
			{
				return url;
			}
		}
		}
		catch(final Exception e)
		{
			LOGGER.error("Exception has occured while getting the media model for LegalNotice",e);
		}
		return url;
		
	}

    public MediaService getMediaService() {
        return mediaService;
    }

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

}
