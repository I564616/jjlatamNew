/**
 * 
 */
package com.jnj.gt.service.zipcode.impl;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.model.JnjGTEarlyZipCodesModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.service.zipcode.JnjGTEarlyZipCodeService;


/**
 * The JnjGTEarlyZipCodeServiceImpl class contains the definition of all the method of the JnjGTEarlyZipCodeService
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTEarlyZipCodeService implements JnjGTEarlyZipCodeService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTEarlyZipCodeService.class);

	@Autowired
	FlexibleSearchService flexibleSearchService;

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws SystemException
	 */
	@Override
	public File fetchFileFromExternalUrl(final boolean isCallForTextFile) throws SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchFileFromExternalUrl()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		URL url = null;
		String path = null;
		File file = null;
		try
		{
			final String dir = Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.DIR_PATH);
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Jnjgtb2binboundserviceConstants.DATE_FORMAT_FOR_FILE_NAME);
			final Date date = new Date();
			if (isCallForTextFile)
			{
				url = URI.create(Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.TXT_FILE_URL)).toURL();
				path = dir.concat(Jnjgtb2binboundserviceConstants.EarlyZipCode.TXT_FILE_NAME).concat(simpleDateFormat.format(date))
						.concat(Jnjgtb2binboundserviceConstants.EarlyZipCode.TXT_FILE_EXTENSION);
			}
			else
			{
				url = URI.create(Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.EXCEL_FILE_URL)).toURL();
				path = dir.concat(Jnjgtb2binboundserviceConstants.EarlyZipCode.EXCEL_FILE_NAME).concat(simpleDateFormat.format(date))
						.concat(Jnjgtb2binboundserviceConstants.EarlyZipCode.EXCEL_FILE_EXTENSION);
			}
			// Create File Object.
			file = new File(path);

			FileUtils.copyURLToFile(url, file);
		}
		catch (final MalformedURLException malformedURLException)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchFileFromExternalUrl()" + Logging.HYPHEN
					+ "Mal Formed URL exception occured -" + malformedURLException.getMessage(), malformedURLException);
			throw new SystemException(malformedURLException.getMessage(), MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		catch (final IOException ioException)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchFileFromExternalUrl()" + Logging.HYPHEN
					+ "Mal Formed URL exception occured -" + ioException.getMessage(), ioException);
			throw new SystemException(ioException.getMessage(), MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchFileFromExternalUrl()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return file;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public JnjGTEarlyZipCodesModel getModelByStateAndPostalCode(final String areaName, final String postalCode,
			final RegionModel regionModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "getModelByStateAndPostalCode()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjGTEarlyZipCodesModel jnjGTEarlyZipCodeModel = new JnjGTEarlyZipCodesModel();
		try
		{
			if (StringUtils.isNotEmpty(areaName))
			{
				jnjGTEarlyZipCodeModel.setAreaName(areaName);
				jnjGTEarlyZipCodeModel.setCourier(Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.FEDEX));
			}
			else
			{
				jnjGTEarlyZipCodeModel.setCourier(Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.UPS));
			}
			jnjGTEarlyZipCodeModel.setZipCode(postalCode);
			jnjGTEarlyZipCodeModel.setStateCode(regionModel);
			// Invoking the Flexible Search Service to get the Jnj NA Early Zip code Model on passing the Customer product number.
			jnjGTEarlyZipCodeModel = flexibleSearchService.getModelByExample(jnjGTEarlyZipCodeModel);
		}
		catch (final ModelNotFoundException exception)
		{
			LOGGER.error(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "getModelByStateAndPostalCode()" + Logging.HYPHEN
					+ "Model Not Found Exception Occured for postal code " + postalCode + " and region model is " + regionModel
					+ exception.getMessage());
			jnjGTEarlyZipCodeModel = null;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "getModelByStateAndPostalCode()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTEarlyZipCodeModel;
	}

}
