/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.laudo.impl;
import org.apache.commons.io.FileUtils;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import com.jnj.core.services.JnjConfigService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Config;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.mapper.laudo.JnjLaudoMapper;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.core.services.laudo.JnjLaudoService;
import com.jnj.core.services.media.JnjLatamMediaService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.core.util.JnjZipFileUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjLaudoData;
import com.jnj.facades.data.JnjLaudoFileStatusData;
import com.jnj.facades.impl.DefaultMessageFacade;
import com.jnj.facades.laudo.JnjLaudoFacade;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Laudo;
import com.jnj.la.core.services.customer.impl.JnjLatamCustomerServiceImpl;
import com.jnj.utils.CommonUtil;


/**
 * The impl class for operations related to Laudo.
 *
 * @author plahiri1
 * @version 1.0
 */
public class JnjLaudoFacadeImpl implements JnjLaudoFacade
{

	@Autowired
	private Converter<JnjLaudoModel, JnjLaudoData> jnjLaudoDataConverter;
	@Autowired
	private JnjLaudoService jnjLaudoService;
    private JnjConfigService jnjConfigService;
    
	@Autowired
	protected DefaultMessageFacade messageFacade;
	protected CMSSiteService cmsSiteService;
	
	@Autowired
	private MediaService mediaService;

	@Autowired
	private JnjLatamMediaService jnjLatamMediaService;

	@Autowired
	private JnjLaudoMapper jnjLaudoMapper;

	@Autowired
	protected JnjLatamCustomerServiceImpl jnjLatamCustomerService;

	private static final String METHOD_GET_LAUDO_DETAILS_WITH_MEDIA = "getLaudoDetailsWithMedia()";
	private static final String METHOD_CONVERT_PAGE_DATA = "Facade - convertPageData()";
	private static final String METHOD_GET_LAUDO_DETAILS = "Facade - getLaudoDetails()";
	private static final String FIELD_LOTE_NUMBER = "Lote Number";
	private static final String FIELD_JNJ_ID = "J&J ID";
	private static final String FIELD_EXPIRATION_DATE = "Expiration Date";
	private static final String FIELD_PDF_FILE_NAME = "PDF File Name";
	/** The file result. */
	private String exceptionCode = null;

	/** The file status. */
	private boolean fileStatus = true;
	
	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}
	public void setJnjConfigService(final JnjConfigService jnjConfigService) {
		this.jnjConfigService = jnjConfigService;
	}
	
	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	public void setCmsSiteService(CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

	@Override
	public SearchPageData<JnjLaudoData> getLaudoDetailsWithMedia(final JnjPageableData jnjPageableData) throws BusinessException
	{

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_DETAILS_WITH_MEDIA, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		final SearchPageData<JnjLaudoModel> searchPageData = jnjLaudoService.getPagedLaudoDetailsWithMedia(jnjPageableData);
		final SearchPageData<JnjLaudoData> pagedDataForLaudo = convertPageData(searchPageData, jnjLaudoDataConverter);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_DETAILS_WITH_MEDIA, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);



		return pagedDataForLaudo;

	}
	
	@Override
	public List<JnjLaudoModel> laudoDetailsExpiredDelete(final String countryCode) throws BusinessException	{
		 return jnjLaudoService.laudoDetailsExpiredDocument(countryCode);
	}

	@Override
	public SearchPageData<JnjLaudoData> convertPageData(final SearchPageData<JnjLaudoModel> source,
			final Converter<JnjLaudoModel, JnjLaudoData> converter)
	{

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CONVERT_PAGE_DATA, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		final SearchPageData<JnjLaudoData> result = new SearchPageData<JnjLaudoData>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CONVERT_PAGE_DATA, Logging.END_OF_METHOD,
					JnjLaudoFacadeImpl.class);
		}
		return result;
	}

	@Override
	public Map<File, String> getLaudoFile(final String laudoFileName) throws BusinessException, IOException
	{

		final String METHOD_GET_LAUDO_FILE = "getLaudoFile()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		File laudoFile = null;
		String laudoFileType = null;
		boolean readLock = false;
		boolean readUnLock = false;
		final Map<File, String> laudoFileMap = new HashMap<File, String>();
		if (StringUtils.isNotEmpty(laudoFileName))
		{
			//Getting Laudo Entry Based on FileName
			final JnjLaudoModel jnjLaudoModel = jnjLaudoService.getLaudoByFileName(laudoFileName);

			if (null == jnjLaudoModel)
			{
				throw new BusinessException("No Laudo Entry Found for file with fileName: " + laudoFileName,
						Jnjlab2bcoreConstants.Laudo.LAUDO_EXCEPTION_CODE_EMSG045, Severity.BUSINESS_EXCEPTION);
			}

			if (null == jnjLaudoModel.getFileMedia())
			{
				throw new BusinessException("No Media found for Laudo Entry with fileName: " + laudoFileName,
						Jnjlab2bcoreConstants.Laudo.LAUDO_EXCEPTION_CODE_EMSG045, Severity.BUSINESS_EXCEPTION);
			}

			//Locking the Laudo Entry with 'Read Lock'
			readLock = jnjLaudoService.toggleLock(jnjLaudoModel, Jnjlab2bcoreConstants.Laudo.LAUDO_READ_LOCK,
					Jnjlab2bcoreConstants.Laudo.LAUDO_LOCK_ENTRY);

			if (!readLock)
			{
				throw new BusinessException("Unable to lock Laudo Entry with fileName: " + laudoFileName,
						Jnjlab2bcoreConstants.Laudo.LAUDO_EXCEPTION_CODE_EMSG045, Severity.BUSINESS_EXCEPTION);
			}

			//Getting the Physical Location of the Laudo File
			final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
			final String laudoMediaPath = mediaDirBase + File.separator
					+ Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_ROOT_FOLDER_KEY)
					+ jnjLaudoModel.getFileMedia().getLocation();

			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FILE,
					"Getting Laudo from File Location: " + laudoMediaPath, JnjLaudoFacadeImpl.class);
			laudoFile = new File(FilenameUtils.getName(laudoMediaPath));

			FileUtils.copyInputStreamToFile(mediaService.getStreamFromMedia(jnjLaudoModel.getFileMedia()), laudoFile);

			laudoFileType = jnjLaudoModel.getFileMedia().getMime();
			//UnLocking the Laudo Entry with 'Read Lock'
			readUnLock = jnjLaudoService.toggleLock(jnjLaudoModel, Jnjlab2bcoreConstants.Laudo.LAUDO_READ_LOCK,
					Jnjlab2bcoreConstants.Laudo.LAUDO_UNLOCK_ENTRY);

			if (!readUnLock)
			{
				throw new BusinessException("Unable to lock Laudo Entry with fileName: " + laudoFileName,
						Jnjlab2bcoreConstants.Laudo.LAUDO_EXCEPTION_CODE_EMSG045, Severity.BUSINESS_EXCEPTION);
			}
		}
		laudoFileMap.put(laudoFile, laudoFileType);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return laudoFileMap;

	}

	@Override
	public SearchPageData<JnjLaudoData> getLaudoDetails(final JnjPageableData jnjPageableData) throws BusinessException
	{

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_DETAILS, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		final SearchPageData<JnjLaudoModel> searchPageData = jnjLaudoService.getPagedLaudoDetails(jnjPageableData);
		final SearchPageData<JnjLaudoData> pagedDataForLaudo = convertPageData(searchPageData, jnjLaudoDataConverter);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_DETAILS, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		return pagedDataForLaudo;
	}

	@Override
	public List<JnjLaudoFileStatusData> deleteLaudos(final List<String> fileNameList) throws BusinessException
	{
		final String METHOD_DELETE_LAUDOS = "deleteLaudos()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_LAUDOS, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		final List<String> lineErrors = new ArrayList<String>();
		exceptionCode = null;
		fileStatus = true;
		final List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList = new ArrayList<JnjLaudoFileStatusData>();
		JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();

		if (null != fileNameList && !fileNameList.isEmpty())
		{
			fileLoop: for (final String fileName : fileNameList)
			{
				JnjLaudoModel jnjLaudoModel = null;
				try
				{
					jnjLaudoModel = jnjLaudoService.getLaudoByFileName(fileName.toLowerCase());
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_DELETE_LAUDOS,
							"BusinessException occured while getting Laudo Entry with File with name: " + fileName + " Excpetion: ",
							businessException, JnjLaudoFacadeImpl.class);
					exceptionCode = Laudo.LAUDO_EXCEPTION_DELETE_LINE_ERRORS;
					lineErrors.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
					fileStatus = false;

					//Setting Up File Status Messages.
					jnjLaudoFileStatusData = setLaudoFileStatusData(fileName, fileStatus, exceptionCode, lineErrors);
					jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
					continue fileLoop;
				}
				if (null != jnjLaudoModel)
				{
					//Getting Both 'Online' and 'Staged' versions of ContentCatalaog
					Set<CatalogVersionModel> catalogVersions = null;
					try
					{
						catalogVersions = jnjLaudoMapper.getCurrentContentCatalogVersions();
					}
					catch (final BusinessException businessException)
					{
						JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_DELETE_LAUDOS,
								"Business exception Occured while retrieveing Content Catalog Version.", businessException,
								JnjLaudoFacadeImpl.class);

					}

					if (null != catalogVersions && !catalogVersions.isEmpty())
					{
						catalogVersionLoop: for (final CatalogVersionModel catalogVersionModel : catalogVersions)
						{
							//Getting Laudo Media Based on FileName and Catalog Version
							MediaModel mediaModel;
							try
							{
								mediaModel = mediaService.getMedia(catalogVersionModel, fileName.toLowerCase());
							}
							catch (final UnknownIdentifierException unknownIdentifierException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_DELETE_LAUDOS,
										"UnknownIdentifierException occured while Fetching Media Associated with File with name: "
												+ fileName + " Excpetion: ",
										unknownIdentifierException, JnjLaudoFacadeImpl.class);
								continue catalogVersionLoop;
							}
							catch (final AmbiguousIdentifierException ambiguousIdentifierException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_DELETE_LAUDOS,
										"AmbiguousIdentifierException occured while Fetching Media Associated with File with name: "
												+ fileName + " Excpetion: ",
										ambiguousIdentifierException, JnjLaudoFacadeImpl.class);

								exceptionCode = Laudo.LAUDO_EXCEPTION_DELETE_LINE_ERRORS;
								lineErrors.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
								fileStatus = false;

								//Setting Up File Status Messages.
								jnjLaudoFileStatusData = setLaudoFileStatusData(fileName, fileStatus, exceptionCode, lineErrors);
								jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
								continue catalogVersionLoop;
							}
							if (null != mediaModel)
							{
								try
								{
									jnjLatamMediaService.removeMedia(mediaModel); //Removing Media Models for the Laudo
								}
								catch (final BusinessException businessException)
								{
									JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_DELETE_LAUDOS,
											"BusinessException occured while Deleteing Media Associated with File with name: " + fileName
													+ " Excpetion: ",
											businessException, JnjLaudoFacadeImpl.class);

									exceptionCode = Laudo.LAUDO_EXCEPTION_DELETE_LINE_ERRORS;
									lineErrors.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
									fileStatus = false;

									//Setting Up File Status Messages.
									jnjLaudoFileStatusData = setLaudoFileStatusData(fileName, fileStatus, exceptionCode, lineErrors);
									jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
									continue fileLoop;
								}
							}
						}
					}
					//Remove Laudo Entry
					try
					{
						jnjLaudoService.removeLaudoEntry(jnjLaudoModel);
					}
					catch (final BusinessException businessException)
					{
						JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_DELETE_LAUDOS,
								"BusinessException occured while Deleteing Laudo Entry with File with name: " + fileName + " Excpetion: ",
								businessException, JnjLaudoFacadeImpl.class);

						exceptionCode = Laudo.LAUDO_EXCEPTION_DELETE_LINE_ERRORS;
						lineErrors.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
						fileStatus = false;

						//Setting Up File Status Messages.
						jnjLaudoFileStatusData = setLaudoFileStatusData(fileName, fileStatus, exceptionCode, lineErrors);
						jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
						jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
						continue fileLoop;
					}
				}
				else
				{
					exceptionCode = Laudo.LAUDO_EXCEPTION_DELETE_LINE_ERRORS;
					lineErrors.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
					fileStatus = false;
				}
				//Setting Up File Status Messages.
				jnjLaudoFileStatusData = setLaudoFileStatusData(fileName, fileStatus, exceptionCode, lineErrors);
				jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_LAUDOS, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		return jnjLaudoFileStatusDataList;
	}

	private JnjLaudoFileStatusData setLaudoFileStatusData(final String fileName, final boolean fileStatus,
			final String exceptionCode, final List<String> lineErrors)
	{
		final String METHOD_SET_FILE_STATUS_MESSAGE = "setFileStatusMessages()";
		final JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
		final StringBuilder statusMessagePart1 = new StringBuilder();
		final StringBuilder statusMessagePart2 = new StringBuilder();
		final StringBuilder finalStatusMessage = new StringBuilder();
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SET_FILE_STATUS_MESSAGE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		try
		{
			/* Building StatusMessagePart1 */
			if (fileStatus && (StringUtils.isEmpty(exceptionCode)
					|| StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_FILE_SUCCESS_FLAG)))
			{
				/* Complete Success Case */
				statusMessagePart1.append(messageFacade.getMessageTextForCode(Laudo.STATUS_SUCCESS_KEY));
			}
			else if (fileStatus && !StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_FILE_SUCCESS_FLAG))
			{
				/* Partial Success Case */
				statusMessagePart1.append(messageFacade.getMessageTextForCode(Laudo.STATUS_PARTIAL_SUCCESS_KEY));
			}
			else if (!fileStatus)
			{
				/* Failure Case */
				statusMessagePart1.append(messageFacade.getMessageTextForCode(Laudo.STATUS_FAILURE_KEY));
			}


			/* Building StatusMessagePart2 */
			if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_GENERAL_EXCEPTION_CODE))
			{
				statusMessagePart2.append(Logging.HYPHEN);
				statusMessagePart2.append(messageFacade.getMessageTextForCode(Laudo.LAUDO_GENERAL_EXCEPTION_MESSAGE_CODE));
			}
			else if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_EXCEPTION_CODE_EMSG007))
			{
				statusMessagePart2.append(Logging.HYPHEN);
				statusMessagePart2.append(messageFacade.getMessageTextForCode(Laudo.LAUDO_EXCEPTION_EMSG007_MESSAGE_CODE));
			}
			else if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_EXCEPTION_CODE_EMSG008))
			{
				statusMessagePart2.append(Logging.HYPHEN);
				statusMessagePart2.append(messageFacade.getMessageTextForCode(Laudo.LAUDO_EXCEPTION_EMSG008_MESSAGE_CODE));
			}
			else if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_EXCEPTION_CODE_EMSG011))
			{
				statusMessagePart2.append(Logging.HYPHEN);
				statusMessagePart2.append(messageFacade.getMessageTextForCode(Laudo.LAUDO_EXCEPTION_EMSG0011_MESSAGE_CODE));
			}
			else if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS))
			{
				/* Adding CSV Line Error Messages */
				if (null != lineErrors && !lineErrors.isEmpty())
				{
					for (final String csvLineErrorMessage : lineErrors)
					{
						statusMessagePart2.append(Logging.HYPHEN);
						statusMessagePart2.append(csvLineErrorMessage);
						statusMessagePart2.append(Jnjlab2bcoreConstants.BREAK_LINE);
					}
				}
			}
			else if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_EXCEPTION_CODE_EMSG045))
			{
				statusMessagePart2.append(Logging.HYPHEN);
				statusMessagePart2.append(messageFacade.getMessageTextForCode(Laudo.LAUDO_EXCEPTION_EMSG0045_MESSAGE_CODE));
			}
			else if (StringUtils.equalsIgnoreCase(exceptionCode, Laudo.LAUDO_EXCEPTION_CODE_EMSG046))
			{
				statusMessagePart2.append(Logging.HYPHEN);
				statusMessagePart2.append(messageFacade.getMessageTextForCode(Laudo.LAUDO_EXCEPTION_EMSG0046_MESSAGE_CODE));
			}

			/* Building finalStatusMessage */
			finalStatusMessage.append(statusMessagePart1);
			finalStatusMessage.append(Jnjlab2bcoreConstants.BREAK_LINE);
			finalStatusMessage.append(statusMessagePart2);

			/* Populating jnjLaudoFileStatusData */
			jnjLaudoFileStatusData.setFileName(fileName);
			jnjLaudoFileStatusData.setProcessed(Boolean.valueOf(fileStatus));
			jnjLaudoFileStatusData.setStatusMessage(finalStatusMessage.toString());
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_SET_FILE_STATUS_MESSAGE,
					"Exception Occred While getting Localised Message.", businessException, JnjLaudoFacadeImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SET_FILE_STATUS_MESSAGE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return jnjLaudoFileStatusData;
	}

	@Override
	public File convertMultipartFile(final MultipartFile multipartFile) throws BusinessException
	{
		final String METHOD_CONVERT_MULTIPART_FILE = "convertMultipartFile()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CONVERT_MULTIPART_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		final String tempFilePathBase = getLaudoTempFilePath();
		final String tempFilePath = tempFilePathBase + multipartFile.getOriginalFilename();

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CONVERT_MULTIPART_FILE,
				"Path For temporary Media Storage is: " + tempFilePath, JnjLaudoFacadeImpl.class);

		final File file = new File(tempFilePathBase, FilenameUtils.getName(multipartFile.getOriginalFilename()));
		{
			try
			{
				if (file.createNewFile() && file.exists())
				{
					multipartFile.transferTo(file);//Transferring multiPartFile to Java File Object.
				}
			}
			catch (final IllegalStateException illegalStateException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LAUDO,
						METHOD_CONVERT_MULTIPART_FILE, "IllegalStateException while converting MultipartFile: "
								+ multipartFile.getOriginalFilename() + " to Java IO File. Cascading to BusinessException",
						illegalStateException, JnjLaudoFacadeImpl.class);
				throw new BusinessException("IllegalStateException while converting MultipartFile: "
						+ multipartFile.getOriginalFilename() + " to Java IO File.");
			}
			catch (final IOException iOException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LAUDO,
						METHOD_CONVERT_MULTIPART_FILE, "IOException while converting MultipartFile: "
								+ multipartFile.getOriginalFilename() + " to Java IO File. Cascading to BusinessException",
						iOException, JnjLaudoFacadeImpl.class);
				throw new BusinessException("IllegalStateException while converting MultipartFile: "
						+ multipartFile.getOriginalFilename() + " to Java IO File.");
			}
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_CONVERT_MULTIPART_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return file;
	}

	private String getLaudoTempFilePath() throws BusinessException
	{
		final String METHOD_GET_LAUDO_TEMP_FILE_PATH = "getLaudoTempFilePath()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_TEMP_FILE_PATH, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		final CountryModel countryModel = jnjLatamCustomerService.getCountryOfUser();
		String countryIso = null;

		if (null != countryModel)
		{
			countryIso = countryModel.getIsocode();
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_TEMP_FILE_PATH,
					"No country Fetched for the Logged In User. Initiating BusinessException", JnjLaudoFacadeImpl.class);
			fileStatus = false;
			throw new BusinessException("No country Fetched for the Logged In User.");
		}

		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final String laudoMediaFolderPath = mediaDirBase + File.separator
				+ Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_ROOT_FOLDER_KEY)
				+ Config.getParameter(Jnjlab2bcoreConstants.Laudo.LAUDO_TEMP_MEDIA_FOLDER_NAME);
		final String tempFilePathBase = laudoMediaFolderPath + File.separator + countryIso + File.separator;
		final File laudoTempFolder = new File(FilenameUtils.getFullPath(tempFilePathBase), FilenameUtils.getName(tempFilePathBase));
		laudoTempFolder.mkdirs();// Make folders If Does Not Exists.
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_TEMP_FILE_PATH,
				"Path For temporary Media Storage is: " + tempFilePathBase, JnjLaudoFacadeImpl.class);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_TEMP_FILE_PATH, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return tempFilePathBase;
	}

	@Override
	public JnjLaudoFileStatusData processSingleLaudoEntry(final JnjLaudoData jnjLaudoData)
	{
		final String METHOD_PROCESS_SINGLE_LAUDO_ENTRY = "processSingleLaudoEntry()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_SINGLE_LAUDO_ENTRY, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
		if (null != jnjLaudoData)
		{
			fileStatus = true;
			exceptionCode = null;
			try
			{ // Map LAUDO field values to LAUDO Model
				jnjLaudoMapper.mapLaudoToHybris(jnjLaudoData);
				if (jnjLaudoData.getPdfFile().exists())
				{
					jnjLaudoMapper.addOrUpdatePdfToLaudo(jnjLaudoData.getPdfFile());// Add PDF File to previously created LAUDO Entry.
				}
				else
				{
					JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO,
							METHOD_PROCESS_SINGLE_LAUDO_ENTRY, "File does not Exists for Laudo Entry with Product Code: "
									+ jnjLaudoData.getProductCode() + " and Laudo No.: " + jnjLaudoData.getLaudoNumber(),
							JnjLaudoFacadeImpl.class);
					fileStatus = false;
					exceptionCode = Laudo.LAUDO_GENERAL_EXCEPTION_CODE;
				}
			}
			catch (final BusinessException businessException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_SINGLE_LAUDO_ENTRY,
						"Business Exception Occured while adding Single Laudo Entry with Product Code: " + jnjLaudoData.getProductCode()
								+ " ,Laudo No.: " + jnjLaudoData.getLaudoNumber() + " and FileName: "
								+ jnjLaudoData.getPdfFile().getName(),
						businessException, JnjLaudoFacadeImpl.class);
				fileStatus = false;
				exceptionCode = businessException.getExceptionCode();
			}
			catch (final IOException exception)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_SINGLE_LAUDO_ENTRY,
						"IO Exception Occured while adding Single Laudo Entry with Product Code: " + jnjLaudoData.getProductCode()
								+ " ,Laudo No.: " + jnjLaudoData.getLaudoNumber() + " and FileName: "
								+ jnjLaudoData.getPdfFile().getName(),
						exception, JnjLaudoFacadeImpl.class);
				fileStatus = false;
			}
			//Setting jnjLaudoFileStatusData.
			jnjLaudoFileStatusData = setLaudoFileStatusData(jnjLaudoData.getPdfFile().getName(), fileStatus, exceptionCode, null);
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_SINGLE_LAUDO_ENTRY,
					"JnjLaudoData received as Null.", JnjLaudoFacadeImpl.class);
			fileStatus = false;
			exceptionCode = Laudo.LAUDO_GENERAL_EXCEPTION_CODE;

		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_SINGLE_LAUDO_ENTRY, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return jnjLaudoFileStatusData;
	}

	@Override
	public void deleteLaudoTempFile(final File file)
	{
		final String METHOD_DELETE_TEMP_LAUDO_FILE = "deleteLaudoTempFile()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_TEMP_LAUDO_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		if (file.exists())
		{
			//Deleting Individual File
			if (file.delete())
			{
				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_TEMP_LAUDO_FILE,
						"File with FileName: " + file.getName() + " deleted sucessfully", JnjLaudoFacadeImpl.class);
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_TEMP_LAUDO_FILE,
						"File with FileName: " + file.getName() + " was not deleted sucessfully", JnjLaudoFacadeImpl.class);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_DELETE_TEMP_LAUDO_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);
	}

	@Override
	public List<JnjLaudoFileStatusData> processUploadedFiles(final CommonsMultipartFile[] files)
	{
		final String METHOD_PRCOESS_LAUDO_UPLOADED_FILE = "processUploadedFiles()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_UPLOADED_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		final List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList = new ArrayList<JnjLaudoFileStatusData>();
		for (final MultipartFile multipartFile : files)
		{
			final String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

			//Case when the Uploaded file is a Zip File
			if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_ZIP))
			{
				processLaudoZipFile(jnjLaudoFileStatusDataList, multipartFile);
			}
			//Case when the Uploaded file is a CSV or PDF File
			if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_CSV)
					|| StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_PDF))
			{
				processMultipartFile(jnjLaudoFileStatusDataList, multipartFile, fileExtension);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_UPLOADED_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return jnjLaudoFileStatusDataList;
	}

	private void processLaudoZipFile(final List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList,
			final MultipartFile multipartFile)

	{
		final String METHOD_PRCOESS_LAUDO_ZIP_FILE = "processLaudoZipFile()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_ZIP_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_ZIP_FILE,
				"Begin Processing of ZIP File with FileName = " + multipartFile.getOriginalFilename(), JnjLaudoFacadeImpl.class);

		fileStatus = true;
		exceptionCode = null;
		List<File> unzippedFileList = null;
		File lastFile = null;
		try
		{
			unzippedFileList = unzipUpoadedFiles(multipartFile);
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_ZIP_FILE,
					"Error Occured while unzipping the File [" + multipartFile.getOriginalFilename() + "]. ", businessException,
					JnjLaudoFacadeImpl.class);

			jnjLaudoFileStatusDataList.add(
					setLaudoFileStatusData(multipartFile.getOriginalFilename(), false, businessException.getExceptionCode(), null));
		}
		if (null != unzippedFileList && !unzippedFileList.isEmpty())
		{
			int listSize = unzippedFileList.size();
			for (final File file : unzippedFileList)
			{
				//Finding the last File in the List
				if (--listSize == 0)
				{
					lastFile = file.getParentFile();
				}

				//Case when the Uploaded file is a CSV File
				if (StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(file.getName()), Laudo.LAUDO_FILE_FORMAT_CSV))
				{
					processFile(jnjLaudoFileStatusDataList, METHOD_PRCOESS_LAUDO_ZIP_FILE, file, Laudo.LAUDO_FILE_FORMAT_CSV);
				}
				//Case when the Uploaded file is a PDF File
				else if (StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(file.getName()), Laudo.LAUDO_FILE_FORMAT_PDF))
				{
					processFile(jnjLaudoFileStatusDataList, METHOD_PRCOESS_LAUDO_ZIP_FILE, file, Laudo.LAUDO_FILE_FORMAT_PDF);
				}
			}
			deleteLaudoTempFile(lastFile); //Deleting Temp Zip Folder
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_ZIP_FILE,
					"No files inside the the Zip File [" + multipartFile.getOriginalFilename() + "]. ", JnjLaudoFacadeImpl.class);

			jnjLaudoFileStatusDataList
					.add(setLaudoFileStatusData(multipartFile.getOriginalFilename(), false, Laudo.LAUDO_GENERAL_EXCEPTION_CODE, null));
		}

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_ZIP_FILE,
				"End of Processing of ZIP File with FileName = " + multipartFile.getOriginalFilename(), JnjLaudoFacadeImpl.class);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_ZIP_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

	}

	private void processMultipartFile(final List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList,
			final MultipartFile multipartFile, final String fileExtension)
	{
		final String METHOD_PROCESS_MULTIPART_FILE = "processMultipartFile()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_MULTIPART_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
		try
		{
			final File file = convertMultipartFile(multipartFile);
			if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_CSV))
			{
				jnjLaudoFileStatusData = processLaudoCsvFile(file);
				deleteLaudoTempFile(file);
			}
			else if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_PDF))
			{
				deleteLaudoTempFile(file);
				jnjLaudoFileStatusData = processLaudoPdfFile(file);
			}
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_MULTIPART_FILE, "BusinessException occured. ",
					businessException, JnjLaudoFacadeImpl.class);

			exceptionCode = businessException.getExceptionCode();
			jnjLaudoFileStatusData = setLaudoFileStatusData(multipartFile.getOriginalFilename(), false, exceptionCode, null);
		}
		finally
		{
			jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PROCESS_MULTIPART_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);
	}

	private List<File> unzipUpoadedFiles(final MultipartFile multipartFile) throws BusinessException
	{
		final String METHOD_UNZIP_UPLOADED_FILES = "unzipUpoadedFiles()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_UNZIP_UPLOADED_FILES, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		final JnjObjectComparator jnjObjectComparator;
		final String tempLaudoBasePath = getLaudoTempFilePath();
		final String tempLaudoPath = tempLaudoBasePath + multipartFile.getName();

		final File unzippedFolder = new File(tempLaudoBasePath, FilenameUtils.getName(multipartFile.getName()));
		unzippedFolder.mkdir();
		try
		{
			JnjZipFileUtil.extractZip(multipartFile.getInputStream(), unzippedFolder);
		}
		catch (final IOException iOException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_UNZIP_UPLOADED_FILES,
					"IOException while Unzipping the file. Cascading to BusinessException. ", iOException, JnjLaudoFacadeImpl.class);

			throw new BusinessException("IOException while Unzipping the file.", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
					Severity.BUSINESS_EXCEPTION);

		}
		final List<File> unzippedFileList = new ArrayList<File>();
		final File[] files = new File(tempLaudoBasePath, FilenameUtils.getName(multipartFile.getName())).listFiles();

		// Iterating over the File List
		for (final File file : files)
		{
			if (file.isFile())
			{
				unzippedFileList.add(file);
			}
		}
		jnjObjectComparator = new JnjObjectComparator(File.class, "getName", true, true);
		Collections.sort(unzippedFileList, jnjObjectComparator);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_UNZIP_UPLOADED_FILES, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return unzippedFileList;
	}

	private void processFile(final List<JnjLaudoFileStatusData> jnjLaudoFileStatusDataList, final String callingMethod,
			final File file, final String fileExtension)
	{
		JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
		try
		{
			if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_CSV))
			{
				jnjLaudoFileStatusData = processLaudoCsvFile(file);
				deleteLaudoTempFile(file);
			}
			else if (StringUtils.equalsIgnoreCase(fileExtension, Laudo.LAUDO_FILE_FORMAT_PDF))
			{
				jnjLaudoFileStatusData = processLaudoPdfFile(file);
				deleteLaudoTempFile(file);
			}
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, "processFile()", "BusinessException occured. ",
					businessException, JnjLaudoFacadeImpl.class);
			deleteLaudoTempFile(file);
			exceptionCode = businessException.getExceptionCode();
			jnjLaudoFileStatusData = setLaudoFileStatusData(file.getName(), false, exceptionCode, null);
		}
		finally
		{
			jnjLaudoFileStatusDataList.add(jnjLaudoFileStatusData);
		}
	}

	private JnjLaudoFileStatusData processLaudoPdfFile(final File file) throws BusinessException
	{
		final String METHOD_PRCOESS_LAUDO_PDF_FILE = "processLaudoPdfFile()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE,
				"Begin Processing of PDF File with FileName = " + file.getName(), JnjLaudoFacadeImpl.class);

		JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
		if (file.exists())
		{
			fileStatus = true;
			exceptionCode = null;
			try
			{
				jnjLaudoMapper.addOrUpdatePdfToLaudo(file);//Calling the mapper to save the PDF in the Laudo Model
			}
			catch (final IllegalStateException ilegalStateException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE,
						"IlegalStateException occured. Cascading to BusinessException. ", ilegalStateException,
						JnjLaudoFacadeImpl.class);
				fileStatus = false;
				exceptionCode = Laudo.LAUDO_GENERAL_EXCEPTION_CODE;
			}
			catch (final IOException exception)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE,
						"IOException occured. Cascading to BusinessException. ", exception, JnjLaudoFacadeImpl.class);
				fileStatus = false;
				exceptionCode = Laudo.LAUDO_GENERAL_EXCEPTION_CODE;
			}
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE,
					"File with name: " + file.getName() + " does not exists in physical location.", JnjLaudoFacadeImpl.class);
			fileStatus = false;
			exceptionCode = Laudo.LAUDO_GENERAL_EXCEPTION_CODE;
		}

		//Setting jnjLaudoFileStatusData.
		jnjLaudoFileStatusData = setLaudoFileStatusData(file.getName(), fileStatus, exceptionCode, null);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE,
				"End of Processing of PDF File with FileName = " + file.getName(), JnjLaudoFacadeImpl.class);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_PDF_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);

		return jnjLaudoFileStatusData;
	}

	private JnjLaudoFileStatusData processLaudoCsvFile(final File file) throws BusinessException
	{
		final List lineErrorsList = new ArrayList<String>();
		final String METHOD_PRCOESS_LAUDO_CSV_FILE = "processLaudoCsvFile()";
		final int CSV_COLUMN_NO = 5;
		final int MAX_LENGHT = 30;
		final String DATE_FORMAT_REGEX = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE, Logging.BEGIN_OF_METHOD,
				JnjLaudoFacadeImpl.class);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
				"Begin Processing of CSV File with FileName = " + file.getName(), JnjLaudoFacadeImpl.class);

		fileStatus = true;
		exceptionCode = null;
		JnjLaudoFileStatusData jnjLaudoFileStatusData = new JnjLaudoFileStatusData();
		if (file.exists())
		{
			int rowReadCount = 0;
			int rowPrcossedCount = 0;
			final char fieldSeparator = ';';
			CSVReader cSVReader = null;
			try
			{
				cSVReader = new CSVReader(file, CSVConstants.HYBRIS_ENCODING); //Creating a CSV Reader to parse CSV file to Laudo Data
			}
			catch (final UnsupportedEncodingException unsupportedEncodingException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
						"UnsupportedEncodingException occured. Cascading to Business Exception.", unsupportedEncodingException,
						JnjLaudoFacadeImpl.class);
				throw new BusinessException("UnsupportedEncodingException occured while Processing CSV File",
						Laudo.LAUDO_EXCEPTION_CODE_EMSG011, Severity.BUSINESS_EXCEPTION);
			}
			catch (final IOException iOException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE, "IOException occured. ",
						iOException, JnjLaudoFacadeImpl.class);

				throw new BusinessException("IOException occured while Processing CSV File", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
						Severity.BUSINESS_EXCEPTION);
			}
			cSVReader.setFieldSeparator(new char[]
			{ fieldSeparator });//Setting the Field Separator to CSV Default Separator','
			Map<Integer, String> row = null;

			//Fetching the CSV Field Positions from the Properties File.
			final int productCodePos = Config.getInt(Jnjlab2bcoreConstants.Laudo.LAUDO_PRODUCT_POS_KEY, 0);
			final int laudoNumberPos = Config.getInt(Jnjlab2bcoreConstants.Laudo.LAUDO_LOT_NUMBER_POS_KEY, 1);
			final int expirationDatePos = Config.getInt(Jnjlab2bcoreConstants.Laudo.LAUDO_EXPIRATION_DATE_POS_KEY, 2);
			final int fileNamePos = Config.getInt(Jnjlab2bcoreConstants.Laudo.LAUDO_FILE_NAME_POS_KEY, 3);
			final int expiredDocumentPos = Config.getInt(Jnjlab2bcoreConstants.Laudo.LAUDO_EXPIRED_DOCUMENT_DELETE, 4);
			while (cSVReader.readNextLine()) //Iterating over CSV Rows
			{
				rowReadCount++;
				row = cSVReader.getLine();
				if (CSV_COLUMN_NO == row.size())
				{
					final JnjLaudoData jnjLaudoData = new JnjLaudoData();

					// Parsing JnJ Product ID
					final String productCode = row.get(Integer.valueOf(productCodePos)).trim();
					if (StringUtils.isNotEmpty(productCode))//Empty validation
					{
						if (productCode.length() <= MAX_LENGHT)//Size Validation
						{
							jnjLaudoData.setProductCode(productCode);
						}
						else
						{
							/* Row Error:: JnJ ID Size Issue */

							JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
									"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);
							JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
									"J&J ID on Line: [ " + cSVReader.getCurrentLineNumber() + " ] does not meet the required length.",
									JnjLaudoFacadeImpl.class);

							/* Adding Error Message to the RowErrorList */
							exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
							lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_1)
									+ FIELD_JNJ_ID + messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_2)
									+ cSVReader.getCurrentLineNumber()
									+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_3));
							continue;
						}
					}
					else
					{
						/* Row Error::JnJ ID Empty */

						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);
						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"Cannot Retrieve J&J ID for Line: [" + cSVReader.getCurrentLineNumber() + " ]", JnjLaudoFacadeImpl.class);
						/* Adding Error Message to the RowErrorList */
						exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
						lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_1)
								+ FIELD_JNJ_ID + messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_2)
								+ cSVReader.getCurrentLineNumber()
								+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_3));
						continue;
					}

					/* Parsing Lote Number */
					final String batchCountries = jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.BATCH_COUNTRIES);
			        final CountryModel countryModel = cmsSiteService.getCurrentSite().getDefaultCountry();
			        final String countryCode = countryModel != null ? countryModel.getIsocode() : Jnjlab2bcoreConstants.Invoice.SearchOption.NONE;
			        final String laudoNumber = row.get(Integer.valueOf(laudoNumberPos)).trim();
					if (StringUtils.isNotEmpty(laudoNumber))//Empty validation
					{
						if (laudoNumber.length() <= MAX_LENGHT)//Size Validation
						{
							jnjLaudoData.setLaudoNumber(laudoNumber);
						}
						else
						{ /* Row Error::Lote Number Length Issue */
							JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
									"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);
							JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
									"Lote Number on Line: [ " + cSVReader.getCurrentLineNumber() + " ] does not meet the required length.",
									JnjLaudoFacadeImpl.class);
							/* Adding Error Message to the RowErrorList */
							exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
							lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_1)
									+ FIELD_LOTE_NUMBER
									+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_2)
									+ cSVReader.getCurrentLineNumber()
									+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_LENGHT_ERROR_MESSAGE_CODE_3));
							continue;
						}
					} else if(StringUtils.isNotEmpty(batchCountries) && batchCountries.contains(countryCode)) { 
					    /* Row Error::Empty Lote Number */
						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);
						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"Cannot Retrieve Lote Number for Row: " + cSVReader.getCurrentLineNumber(), JnjLaudoFacadeImpl.class);
						/* Adding Error Message to the RowErrorList */
						exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
						lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_1)
								+ FIELD_LOTE_NUMBER + messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_2)
								+ cSVReader.getCurrentLineNumber()
								+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_3));
						continue;
					} else {
					  jnjLaudoData.setLaudoNumber(Jnjlab2bcoreConstants.Invoice.SearchOption.NONE);
					}

					/* Parsing Expiration Date */
					final String dateString = row.get(Integer.valueOf(expirationDatePos)).trim();
					if (StringUtils.isNotEmpty(dateString))//Empty validation
					{
						if (dateString.matches(DATE_FORMAT_REGEX))//Date Format Validation
						{
							final Date expirationDate = CommonUtil.parseStringToDate(row.get(Integer.valueOf(expirationDatePos)),
									Jnjlab2bcoreConstants.Laudo.LAUDO_EN_DATE_FORMAT);
							if (null != expirationDate)
							{
								jnjLaudoData.setExpirationDate(expirationDate);
							}
							else
							{
								JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
										"Parsing Excpetion Occured while parsing Date at row: " + cSVReader.getCurrentLineNumber(),
										JnjLaudoFacadeImpl.class);
								continue;
							}
						}
						else
						{
							/* Row Error:: Expiration Date Invalid Format */

							JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
									"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);

							JnjGTCoreUtil.logDebugMessage(
									Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE, "Date information for Line: [ "
											+ cSVReader.getCurrentLineNumber() + " ] does not meet the required format.",
									JnjLaudoFacadeImpl.class);
							/* Adding Error Message to the RowErrorList */
							exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
							lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_DATE_ERROR_MESSAGE_CODE_1)
									+ cSVReader.getCurrentLineNumber()
									+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_DATE_ERROR_MESSAGE_CODE_2));
							continue;
						}
					}
					else
					{ /* Row Error:: Expiration Date Null */

						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);

						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"Cannot Retrieve Expiration Date for Row: " + cSVReader.getCurrentLineNumber(), JnjLaudoFacadeImpl.class);

						/* Adding Error Message to the RowErrorList */
						exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
						lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_1)
								+ FIELD_EXPIRATION_DATE
								+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_2)
								+ cSVReader.getCurrentLineNumber()
								+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_3));
						continue;
					}

					/* Parsing File Name */
					final String pdfFileName = row.get(Integer.valueOf(fileNamePos)).trim();
					if (StringUtils.isNotEmpty(pdfFileName))//Empty validation
					{
						jnjLaudoData.setPdfFileName(pdfFileName.toLowerCase());
					}
					else
					{
						/* Row Error:: PDF File Name Null */

						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"Cannot Retrieve File Name for Row: " + cSVReader.getCurrentLineNumber(), JnjLaudoFacadeImpl.class);

						JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"CSV Row: " + cSVReader.getCurrentLineNumber() + " not Valid.", JnjLaudoFacadeImpl.class);
						/* Adding Error Message to the RowErrorList */
						exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
						lineErrorsList.add(
								messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_1) + FIELD_PDF_FILE_NAME
										+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_2)
										+ cSVReader.getCurrentLineNumber()
										+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_ROW_EMPTY_ERROR_MESSAGE_CODE_3));
						continue;
					}

                    final String  expireDeletion = row.get(Integer.valueOf(expiredDocumentPos)).trim();
					jnjLaudoData.setBatchRequired("true".equalsIgnoreCase(expireDeletion));
					
					/* Calling the MAPPER to Map the Data Class to the Models. */
					try
					{
						jnjLaudoMapper.mapLaudoToHybris(jnjLaudoData);
						rowPrcossedCount++;
					}
					catch (final BusinessException businessException)
					{
						JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
								"BusinessException occured.", businessException, JnjLaudoFacadeImpl.class);
						exceptionCode = businessException.getExceptionCode();
						continue;
					}
				}
				else
				{
					JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
							"Invalid Row Structure at Row: " + cSVReader.getCurrentLineNumber(), JnjLaudoFacadeImpl.class);
					exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
					lineErrorsList.add(messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_COLUMN_EXCEED_MESSAGE_CODE_1)
							+ cSVReader.getCurrentLineNumber()
							+ messageFacade.getMessageTextForCode(Laudo.LAUDO_CSV_COLUMN_EXCEED_MESSAGE_CODE_2));
					exceptionCode = Laudo.LAUDO_EXCEPTION_CSV_LINE_ERRORS;
					continue;
				}
			}

			/* Closing the CSV Reader */
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
					"Closing CSV Reader for file with File Name: " + file.getName(), JnjLaudoFacadeImpl.class);

			try
			{
				cSVReader.close();
			}
			catch (final IOException iOException)
			{
				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
						"IOException occured while Closing the CSV reader. ", iOException, JnjLaudoFacadeImpl.class);

				throw new BusinessException("IOException occured while Processing CSV File", Laudo.LAUDO_GENERAL_EXCEPTION_CODE,
						Severity.BUSINESS_EXCEPTION);
			}

			/* CSV File Status Checks */
			if (rowPrcossedCount == 0)
			{
				/* If no Row is processed Successfully. */

				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
						"The CSV file with File Name: " + file.getName() + " is Invalid and No row is processed.",
						JnjLaudoFacadeImpl.class);
				fileStatus = false;
			}
			else if (rowReadCount != rowPrcossedCount)
			{
				/* Some Rows processed Successfully. */
				JnjGTCoreUtil
						.logDebugMessage(
								Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE, "The CSV file with File Name: "
										+ file.getName() + " has some invalid rows and the file is processed partially.",
								JnjLaudoFacadeImpl.class);

				fileStatus = true;
			}
			else
			{
				/* All Rows processed Successfully. */
				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
						"The CSV file with File Name: " + file.getName() + " has valid rows and the file is processed successfully.",
						JnjLaudoFacadeImpl.class);
				exceptionCode = Laudo.LAUDO_FILE_SUCCESS_FLAG;
				fileStatus = true;
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
					"File with name: " + file.getName() + " does not exists in physical location.", JnjLaudoFacadeImpl.class);
			JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
					"The CSV file with File Name: " + file.getName() + " is not processed.", JnjLaudoFacadeImpl.class);
			fileStatus = false;
			exceptionCode = Laudo.LAUDO_GENERAL_EXCEPTION_CODE;

		}
		//Setting jnjLaudoFileStatusData.
		jnjLaudoFileStatusData = setLaudoFileStatusData(file.getName(), fileStatus, exceptionCode, lineErrorsList);


		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE,
				"End Processing of CSV File with FileName = " + file.getName(), JnjLaudoFacadeImpl.class);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_PRCOESS_LAUDO_CSV_FILE, Logging.END_OF_METHOD,
				JnjLaudoFacadeImpl.class);


		return jnjLaudoFileStatusData;
	}
}
