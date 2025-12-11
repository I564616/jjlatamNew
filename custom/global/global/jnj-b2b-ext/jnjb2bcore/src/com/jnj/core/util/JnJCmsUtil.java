/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.util;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.BusinessCenter;
import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.services.CMSSiteService;
import com.jnj.utils.CommonUtil;


/**
 * CMS UTIL.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnJCmsUtil
{

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(JnJCmsUtil.class);


	protected static final String METHOD_GET_SESSION_CONTENT_CATALOG_VERSIONS = "getSessionContentCatalogVersions()";


	/** The catalog version service. */
	@Autowired
	protected CatalogVersionService catalogVersionService;

	/** The cms site serive. */
	@Autowired
	protected CMSSiteService cmsSiteSerive;

	/** The cms page service. */
	@Autowired
	protected CMSPageService cmsPageService;

	/** The model service. */
	@Autowired
	ModelService modelService;
	
	@Autowired
	protected EnumerationService enumerationService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	// this is the regexp used for split-operations on the strings. It defines
	// the space between words.
	/** The Constant WORD_DELIMITER_REGEXP. */
	//protected static final String WORD_DELIMITER_REGEXP = "[ ]";

	// this is used to combine an array to a string
	/** The Constant BLANK_DELIMITER. */
	//protected static final String BLANK_DELIMITER = " ";

	/** The Constant DOTS_STRING. */
	//	protected static final String DOTS_STRING = " <b>...</b> ";

	// number of words shown before/after the found search term
	/** The Constant NUMBER_OF_WORDS_BEFORE. */
	//protected static final int NUMBER_OF_WORDS_BEFORE = 20;

	/** The Constant NUMBER_OF_WORDS_AFTER. */
	//protected static final int NUMBER_OF_WORDS_AFTER = 20;

	

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public CMSSiteService getCmsSiteSerive() {
		return cmsSiteSerive;
	}

	public CMSPageService getCmsPageService() {
		return cmsPageService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}

	/**
	 * Gets the content for page.
	 * 
	 * @param contentPage
	 *           the content page
	 * @return the content for page
	 */
	public String getContentForPage(final ContentPage contentPage)
	{

		final ContentPageModel page = modelService.toModelLayer(contentPage);
		return getContentForPage(page);
	}

	/**
	 * Traverses from the Page down to a single component and reads the content from the components.
	 * 
	 * @param page
	 *           the page
	 * @return the content for page
	 */
	public String getContentForPage(final ContentPageModel page)
	{

		StringBuilder strBuilder = new StringBuilder();
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{
					strBuilder.append(Utilities.filterOutHTMLTags((((JnjNewsBannerComponentModel) component)).getContent())).append(
							Jnjb2bCoreConstants.SPACE);
					break;
				}
			}
		}


		if (StringUtils.equals(strBuilder.toString().trim(), "null"))
		{
			strBuilder = new StringBuilder();
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Content for page [" + page.getUid() + "] equals [" + strBuilder.toString() + "]");
		}

		return strBuilder.toString();
	}

	/**
	 * To get thumbnail url.
	 * 
	 * @param page
	 *           the page
	 * @return String
	 */
	public String getNewsThumbnailUrl(final ContentPageModel page)
	{

		String thumnailUrl = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					final MediaModel media = ((JnjNewsBannerComponentModel) component).getThumbnail();
					if (null != media)
					{
						thumnailUrl = media.getURL();
						break;
					}

				}
			}
		}
		return thumnailUrl;


	}

	/**
	 * To get news url.
	 * 
	 * @param page
	 *           the page
	 * @return String
	 */
	public String getNewsUrlLink(final ContentPageModel page)
	{

		String urlLink = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					urlLink = ((JnjNewsBannerComponentModel) component).getUrlLink();


				}
			}
		}
		return urlLink;


	}

	/**
	 * Gets the head lines.
	 * 
	 * @param page
	 *           the page
	 * @return the head lines
	 */
	public String getHeadLines(final ContentPageModel page)
	{

		String headLine = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					headLine = ((JnjNewsBannerComponentModel) component).getHeadline();


				}
			}
		}
		return headLine;


	}

	/**
	 * Gets the news publication year.
	 * 
	 * @param page
	 *           the page
	 * @return the news publication year
	 */
	public String getNewsPublicationYear(final ContentPageModel page)
	{

		String publishYear = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					final Date date = ((JnjNewsBannerComponentModel) component).getNewsPublishDate();

					if (null != date)
					{

						final Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						final int year = cal.get(Calendar.YEAR);
						publishYear = String.valueOf(year);
						break;

					}
				}
			}
		}
		return publishYear;


	}

	/**
	 * To get publication date.
	 * 
	 * @param page
	 *           the page
	 * @return String
	 */
	public String getNewsPublicationDate(final ContentPageModel page)
	{

		String publishDate = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					final Date date = ((JnjNewsBannerComponentModel) component).getNewsPublishDate();

					if (null != date)
					{
						publishDate = CommonUtil.formatDateToString(date, jnjCommonUtil.getDateFormat());
						break;

					}
				}
			}
		}
		return publishDate;


	}

	/**
	 * Gets the news publication date in long.
	 * 
	 * @param page
	 *           the page
	 * @return the news publication date in long
	 */
	public String getNewsPublicationDateInLong(final ContentPageModel page)
	{

		String longDate = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					final Date date = ((JnjNewsBannerComponentModel) component).getNewsPublishDate();

					if (null != date)
					{
						longDate = String.valueOf(date.getTime());
						break;

					}
				}
			}
		}
		return longDate;

	}

	/**
	 * To get business center.
	 * 
	 * @param page
	 *           the page
	 * @return String
	 */
	public String getNewsBusinessCenter(final ContentPageModel page)
	{

		String bCenter = null;
		for (final ContentSlotData slotData : cmsPageService.getContentSlotsForPage(page))
		{
			final Collection<AbstractCMSComponentModel> components = slotData.getCMSComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof JnjNewsBannerComponentModel)
				{

					final BusinessCenter businessCenter = ((JnjNewsBannerComponentModel) component).getBusinessCenter();

					if (null != businessCenter)
					{
						bCenter = enumerationService.getEnumerationName(businessCenter);
						break;

					}
				}
			}
		}
		return bCenter;


	}

	/**
	 * Returns the first number of words of the originalContentString. The number of words is determined by
	 * numberOfWordsBefore + numberOfWordsAfter
	 * 
	 * @param originalContentString
	 *           the original content string
	 * @return the first part of content
	 */
	/*
	 * protected String getFirstPartOfContent(final String originalContentString) { final int numberOfWordsBefore =
	 * NUMBER_OF_WORDS_BEFORE; final int numberOfWordsAfter = NUMBER_OF_WORDS_AFTER; final int totalNumberOfWords =
	 * numberOfWordsBefore + numberOfWordsAfter;
	 * 
	 * final String[] arrWordsOriginal = originalContentString.split(WORD_DELIMITER_REGEXP); if (arrWordsOriginal.length
	 * > totalNumberOfWords) { final String[] arrWordsCropped = new String[totalNumberOfWords];
	 * System.arraycopy(arrWordsOriginal, 0, arrWordsCropped, 0, totalNumberOfWords);
	 * 
	 * final String combinedContentStringBefore = arrayToString(arrWordsCropped, " ");
	 * 
	 * String croppedString = combinedContentStringBefore; croppedString = croppedString + DOTS_STRING; return
	 * croppedString; } else { // the original text has less than totalNumberOfWords return originalContentString; } }
	 */

	/**
	 * Returns some content around the searchterm. If the content does not contain the searchterm, null is returned.
	 * 
	 * @param contentString
	 *           the content string
	 * @param searchTerm
	 *           the search term
	 * @return the small content around searchterm
	 */
	/*
	 * protected String getSmallContentAroundSearchterm(final String contentString, final String searchTerm) { final int
	 * firstOccurenceOfSearchterm = contentString.toUpperCase().indexOf(searchTerm.toUpperCase());
	 * 
	 * final int numberOfWordsBefore = NUMBER_OF_WORDS_BEFORE; final int numberOfWordsAfter = NUMBER_OF_WORDS_AFTER; if
	 * (firstOccurenceOfSearchterm < 0) { // searchterm was not found in the content return null; } else { final int
	 * indexBeforeSearchterm = firstOccurenceOfSearchterm; final int startIndex =
	 * getStartIndexOfWordsBefore(contentString, indexBeforeSearchterm, numberOfWordsBefore); final int
	 * indexAfterSearchterm = firstOccurenceOfSearchterm + searchTerm.length(); final int endIndex =
	 * getEndIndexOfWordsAfter(contentString, indexAfterSearchterm, numberOfWordsAfter);
	 * 
	 * final String croppedString = contentString.substring(startIndex, endIndex);
	 * 
	 * return croppedString; } }
	 */
	/**
	 * Returns the index where the first word of the words specified in numberOfWordsBefore begins.
	 * 
	 * @param contentString
	 *           the content string
	 * @param indexBeforeSearchterm
	 *           The index where the found searchterm begins
	 * @param numberOfWordsBefore
	 *           how many words should be taken before the searchterm?
	 * @return the start index of words before
	 */
	/*
	 * protected int getStartIndexOfWordsBefore(final String contentString, final int indexBeforeSearchterm, int
	 * numberOfWordsBefore) { final String subStringBefore = contentString.substring(0, indexBeforeSearchterm); final
	 * String[] arrWordsBefore = subStringBefore.split(WORD_DELIMITER_REGEXP); if (arrWordsBefore.length <
	 * numberOfWordsBefore) { numberOfWordsBefore = arrWordsBefore.length; } final String[] arrWordsCropped = new
	 * String[numberOfWordsBefore]; System.arraycopy(arrWordsBefore, arrWordsBefore.length - numberOfWordsBefore,
	 * arrWordsCropped, 0, arrWordsCropped.length);
	 * 
	 * final String combinedContentStringBefore = arrayToString(arrWordsCropped, " "); final int lengthOfStringBefore =
	 * combinedContentStringBefore.length(); final int startIndex = indexBeforeSearchterm - lengthOfStringBefore; return
	 * startIndex; }
	 *//**
	 * Returns the index where the last word of the words specified in numberOfWordsBefore ends.
	 * 
	 * @param contentString
	 *           the content string
	 * @param indexAfterSearchterm
	 *           The index where the found searchterm ends
	 * @param numberOfWordsAfter
	 *           how many words should be taken after the searchterm?
	 * @return the end index of words after
	 */
	/*
	 * protected int getEndIndexOfWordsAfter(final String contentString, final int indexAfterSearchterm, int
	 * numberOfWordsAfter) { final String subStringAfter = contentString.substring(indexAfterSearchterm,
	 * contentString.length()); final String[] arrWordsAfter = subStringAfter.split(WORD_DELIMITER_REGEXP);
	 * 
	 * if (arrWordsAfter.length < numberOfWordsAfter) { numberOfWordsAfter = arrWordsAfter.length; }
	 * 
	 * final String[] arrWordsCropped = new String[numberOfWordsAfter]; final int startIndexArrWordsAfter =
	 * arrWordsAfter.length - numberOfWordsAfter; System.arraycopy(arrWordsAfter, startIndexArrWordsAfter,
	 * arrWordsCropped, 0, arrWordsCropped.length);
	 * 
	 * final String combinedContentStringAfter = arrayToString(arrWordsCropped, BLANK_DELIMITER); final int
	 * lengthOfStringAfter = combinedContentStringAfter.length(); final int endIndex = indexAfterSearchterm +
	 * lengthOfStringAfter; return endIndex; }
	 */

	/**
	 * Array to string.
	 * 
	 * @param stringArray
	 *           the string array
	 * @param separator
	 *           the separator
	 * @return the string
	 */
	public static String arrayToString(final String[] stringArray, final String separator)
	{
		final StringBuffer result = new StringBuffer();
		if (stringArray.length > 0)
		{
			result.append(stringArray[0]);
			for (int i = 1; i < stringArray.length; i++)
			{
				result.append(separator);
				result.append(stringArray[i]);
			}
		}
		return result.toString();
	}

	/**
	 * Currently the searchterm is made bold, but we could also wrap a span-element around it and define in the css how
	 * it looks.
	 * 
	 * @param contentString
	 *           the content string
	 * @param searchTerm
	 *           the search term
	 * @return the string
	 */
	/*
	 * protected String markSearchterm(String contentString, final String searchTerm) { // ?i = case-insensitivie-search
	 * final String regex = "((?i)" + searchTerm + ")"; final String replacement = "<em>$1</em>"; contentString =
	 * contentString.replaceAll(regex, replacement); return contentString; }
	 */

	/**
	 * Adds the dots at end.
	 * 
	 * @param originalContentString
	 *           the original content string
	 * @param croppedContentString
	 *           the cropped content string
	 * @param contentStringToDot
	 *           the content string to dot
	 * @return the string
	 */
	/*
	 * protected String addDotsAtEnd(final String originalContentString, final String croppedContentString, final String
	 * contentStringToDot) { String dottedContentString = contentStringToDot; final int startIndexOfCroppedString =
	 * originalContentString.indexOf(croppedContentString); final int endIndexOfCroppedString = startIndexOfCroppedString
	 * + croppedContentString.length();
	 * 
	 * final String dotsString = DOTS_STRING; if (startIndexOfCroppedString > 0) { // there is text before the cropped
	 * string, add dots dottedContentString = dotsString + dottedContentString; } else if (endIndexOfCroppedString <
	 * originalContentString.length()) { // there is text after the cropped string, add dots dottedContentString =
	 * dottedContentString + dotsString; } return dottedContentString; }
	 */

	/**
	 * Trim content. This method is used to trim the content according to the characters specified, keeping the words
	 * intact.
	 * 
	 * @param originalContentString
	 *           the original content string
	 * @param delimiter
	 *           the delimiter
	 * @param noOfChar
	 *           the no of char
	 * @return the string
	 */
	public String trimContent(final String originalContentString, final char delimiter, final int noOfChar)
	{
		String finalContentString = null;
		final int lenght = originalContentString.length();
		//Checking if the desired length is less than the actual length
		if (noOfChar < lenght)
		{
			if (originalContentString.charAt(noOfChar) != delimiter)
			{
				final String subString = originalContentString.substring(0, noOfChar);
				finalContentString = originalContentString.substring(0, subString.lastIndexOf(delimiter));
			}
			else
			{
				finalContentString = originalContentString.substring(0, noOfChar);
			}

		}
		//If the desired length is equal or greater than the actual length, returning the same string.
		else if (noOfChar >= lenght)
		{
			finalContentString = originalContentString.trim();
		}

		return finalContentString;
	}



	/**
	 * Gets the session content catalog versions.
	 * 
	 * @return the session content catalog versions
	 */
	public Collection<CatalogVersionModel> getSessionContentCatalogVersions()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.JNJ_CMS_UTIL + Logging.HYPHEN + METHOD_GET_SESSION_CONTENT_CATALOG_VERSIONS + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final List<ContentCatalogModel> contentCatalogs = cmsSiteSerive.getCurrentSite().getContentCatalogs();
		final Collection<CatalogVersionModel> sessionCatalogVersions = catalogVersionService.getSessionCatalogVersions();
		final Collection result = new ArrayList();
		for (final CatalogVersionModel sessionCatalogVersion : sessionCatalogVersions)
		{
			if (!(contentCatalogs.contains(sessionCatalogVersion.getCatalog())))
			{
				continue;
			}
			result.add(sessionCatalogVersion);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.JNJ_CMS_UTIL + Logging.HYPHEN + METHOD_GET_SESSION_CONTENT_CATALOG_VERSIONS + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return result;
	}

}
