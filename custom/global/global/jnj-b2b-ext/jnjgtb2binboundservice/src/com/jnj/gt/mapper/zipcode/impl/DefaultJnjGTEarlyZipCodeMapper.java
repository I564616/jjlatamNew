/**
 * 
 */
package com.jnj.gt.mapper.zipcode.impl;

import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.model.JnjGTEarlyZipCodesModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.zipcode.JnjGTEarlyZipCodeMapper;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.common.impl.DefaultJnjGTStgSerivce;
import com.jnj.gt.service.zipcode.JnjGTEarlyZipCodeService;


/**
 * The JnjGTEarlyZipCodeMapperImpl class contains the definition of all the method of the JnjGTEarlyZipCodeMapper
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTEarlyZipCodeMapper implements JnjGTEarlyZipCodeMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTEarlyZipCodeMapper.class);
	private static final String EFFECTIVE_STRING_IN_TXT = "Effective:";
	private static final String IMPEX_FILE_PATH = "/jnjgtb2binboundservice/import/feed/earlyZipCodeImpexCronJob.impex";
	private static final String REMOVE_DATA_IMPEX_FILE_PATH = "/jnjgtb2binboundservice/import/feed/removeEarlyZipCodeImpexCronJob.impex";

	@Autowired
	private JnjGTEarlyZipCodeService jnjGTEarlyZipCodeService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private SetupImpexService setupImpexService;

	@Autowired
	private DefaultJnjGTStgSerivce jnjGTStgSerivce;

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws SystemException
	 * @throws IOException
	 *            , FileNotFoundException
	 */
	@Override
	public void fetchAndSaveDataInHybris() throws SystemException, IOException, FileNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchAndSaveDataInHybris()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		boolean isEffectiveStringOccured = false;
		// To Make all is updated flag of JnjGTEarlyZipCodes model with false value.
		jnjGTStgSerivce.loadFeedData(IMPEX_FILE_PATH, Logging.EARLY_ZIP_CODE_FEED);
		final File file = jnjGTEarlyZipCodeService.fetchFileFromExternalUrl(true);
		final BufferedReader bufferReader = new BufferedReader(new FileReader(file));
		String dataRow = bufferReader.readLine();
		// check Not null and not empty on the data row.
		while (null != dataRow)
		{
			// Check for the effective string after that start reading the content of the file.
			if (dataRow.contains(EFFECTIVE_STRING_IN_TXT))
			{
				isEffectiveStringOccured = true;
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchAndSaveDataInHybris()" + Logging.HYPHEN
							+ "Effective String is existed in the file" + isEffectiveStringOccured);
				}
				// To skip the empty line which is existed below the effective string.
				dataRow = bufferReader.readLine();
				// To get the first record
				dataRow = bufferReader.readLine();
			}

			// If file contains the effective string then only read the content value.
			if (isEffectiveStringOccured && !dataRow.isEmpty())
			{
				final String areaName = dataRow.substring(0, 30).trim();
				final String state = dataRow.substring(30, 37).trim();
				final String postalCode = dataRow.substring(37, 47).trim();
				final String time = dataRow.substring(47, dataRow.length()).trim();
				if (StringUtils.isNotEmpty(postalCode) && StringUtils.isNotEmpty(areaName) && StringUtils.isNotEmpty(state)
						&& StringUtils.isNotEmpty(time))
				{
					final CountryModel countryModel = commonI18NService.getCountry(Config
							.getParameter(Jnjgtb2binboundserviceConstants.COUNTRY_ISO_CODE));
					final RegionModel regionModel = commonI18NService.getRegion(countryModel, state);
					JnjGTEarlyZipCodesModel jnjGTEarlyZipCodesModel = jnjGTEarlyZipCodeService.getModelByStateAndPostalCode(areaName,
							postalCode, regionModel);
					// If the model is not existed then create the new model else update the existed model.
					if (null == jnjGTEarlyZipCodesModel)
					{
						jnjGTEarlyZipCodesModel = modelService.create(JnjGTEarlyZipCodesModel.class);
						jnjGTEarlyZipCodesModel.setAreaName(areaName);
						jnjGTEarlyZipCodesModel.setZipCode(postalCode);
						jnjGTEarlyZipCodesModel.setStateCode(regionModel);
						jnjGTEarlyZipCodesModel.setCourier(Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.FEDEX));
					}
					jnjGTEarlyZipCodesModel.setTime(time);
					jnjGTEarlyZipCodesModel.setIsUpdated(Boolean.TRUE);
					modelService.save(jnjGTEarlyZipCodesModel);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateWriteDashboard(Logging.EARLY_ZIP_CODE_FEED, postalCode,
							"Some of the value is empty or null");
				}
			}
			dataRow = bufferReader.readLine();
		}
		bufferReader.close();
		// Move the file in archive folder.
		final boolean uploadStatus = JnJXMLFilePicker.zipAndMoveFile(file,
				Jnjgtb2binboundserviceConstants.EarlyZipCode.EARLY_ZIP_CODE_FOLDER,
				Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.ARCHIVE_FOLDER), true);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchAndSaveDataInHybris()" + Logging.HYPHEN
					+ "After moving the file in sftp location" + uploadStatus);
		}

		// Call For Excel file and read its content so that we can save the same in hybris data base.
		final File excelFile = jnjGTEarlyZipCodeService.fetchFileFromExternalUrl(false);
		final FileInputStream fileInputStream = new FileInputStream(excelFile);
		final HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
		final HSSFSheet worksheet = workbook.getSheet("Sheet1");
		final int rowNum = worksheet.getLastRowNum() + 1;
		for (int i = 1; i < rowNum; i++)
		{
			// Get the value from the worksheet.
			final HSSFRow row = worksheet.getRow(i);
			final HSSFCell cellA1 = row.getCell(0);
			final String a1Val = cellA1.getStringCellValue();
			final HSSFCell cellB1 = row.getCell(1);
			final String b1Val = cellB1.getStringCellValue();
			final HSSFCell cellC1 = row.getCell(2);
			final String c1Val = cellC1.getStringCellValue();
			if (StringUtils.isNotEmpty(a1Val) && StringUtils.isNotEmpty(b1Val) && StringUtils.isNotEmpty(c1Val))
			{
				final CountryModel countryModel = commonI18NService.getCountry(Config
						.getParameter(Jnjgtb2binboundserviceConstants.COUNTRY_ISO_CODE));
				final RegionModel regionModel = commonI18NService.getRegion(countryModel, a1Val);
				JnjGTEarlyZipCodesModel jnjGTEarlyZipCodesModel = jnjGTEarlyZipCodeService.getModelByStateAndPostalCode(null, b1Val,
						regionModel);
				if (null == jnjGTEarlyZipCodesModel)
				{
					jnjGTEarlyZipCodesModel = modelService.create(JnjGTEarlyZipCodesModel.class);
					jnjGTEarlyZipCodesModel.setZipCode(b1Val);
					jnjGTEarlyZipCodesModel.setStateCode(regionModel);
					jnjGTEarlyZipCodesModel.setCourier(Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.UPS));
				}
				jnjGTEarlyZipCodesModel.setTime(c1Val);
				jnjGTEarlyZipCodesModel.setIsUpdated(Boolean.TRUE);
				modelService.save(jnjGTEarlyZipCodesModel);
			}
			else
			{
				//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
				jnjGTFeedService.updateWriteDashboard(Logging.EARLY_ZIP_CODE_FEED, b1Val, "Some of the value is empty or null");
			}
		}
		fileInputStream.close();
		// To remove all those records for which is updated flag is false i.e the are updated.
		jnjGTStgSerivce.loadFeedData(REMOVE_DATA_IMPEX_FILE_PATH, Logging.EARLY_ZIP_CODE_FEED);

		final boolean uploadStatusForExcel = JnJXMLFilePicker.zipAndMoveFile(excelFile,
				Jnjgtb2binboundserviceConstants.EarlyZipCode.EARLY_ZIP_CODE_FOLDER,
				Config.getParameter(Jnjgtb2binboundserviceConstants.EarlyZipCode.ARCHIVE_FOLDER), true);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchAndSaveDataInHybris()" + Logging.HYPHEN
					+ "After moving the file in sftp location" + uploadStatusForExcel);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EARLY_ZIP_CODE_FEED + Logging.HYPHEN + "fetchAndSaveDataInHybris()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}
}
