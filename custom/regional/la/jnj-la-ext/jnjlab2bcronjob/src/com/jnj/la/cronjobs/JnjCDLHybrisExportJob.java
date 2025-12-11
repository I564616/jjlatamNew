/**
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 **/

package com.jnj.la.cronjobs;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.daos.TypeDao;
import de.hybris.platform.util.CSVWriter;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.jnj.exceptions.BusinessException;

import com.jnj.la.core.dao.customer.JnjCDLExportDao;
import com.jnj.la.core.util.JnJLaCronjobUtil;
import com.jnj.la.core.model.JnjCDLExportJobModel;


public class JnjCDLHybrisExportJob extends AbstractJobPerformable<JnjCDLExportJobModel>
{
	private static final Logger LOG = Logger.getLogger(JnjCDLHybrisExportJob.class);
	private static final String YES = "Y";
	private static final String PK ="pk";
	private static final String COMMA =",";
	private static final String COMMASPACE =", ";
	private static final String UNDERSCORE = "_";
    private static final String MESSAGE = "IO Exception in ";
	
	private String srcDirectory = StringUtils.EMPTY;
	private String destDirectory = StringUtils.EMPTY;
	private String hostName = StringUtils.EMPTY;
	private String userName = StringUtils.EMPTY;
	private String password = StringUtils.EMPTY;

	private TypeDao typeDao;
	private JnjCDLExportDao jnjCDLExportDao;
	private CommonI18NService commonI18NService;
	private static ConfigurationService configurationService;

	private static final Class<JnjCDLHybrisExportJob> CURRENTCLASS = JnjCDLHybrisExportJob.class;

	@Override
	public PerformResult perform(final JnjCDLExportJobModel cronjob)
	{
		final String methodName = "perform";
		LOG.debug("Starting JnjCDLHybrisExportJob Job");
		try
		{
			srcDirectory = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CDL.CDL_SRC_FILE_PATH);
			destDirectory = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CDL.CDL_DEST_FILE_PATH);
			hostName = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CDL.CDL_FILE_HOSTNAME);
			userName = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CDL.CDL_FILE_USERNAME);
			password = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CDL.CDL_FILE_PASS);

			final StringBuilder exportObjects = new StringBuilder(
					configurationService.getConfiguration().getString("jnj.cdl.export.job.items"));
			tryExportService(exportObjects.toString(), cronjob);

		}
		catch (final BusinessException be) {
			LOG.error("Exception when running "+CURRENTCLASS+"-"+methodName, be);
			return cronJobAborted();
		}
		catch (final Exception e)
		{
			LOG.error("Exception when running "+CURRENTCLASS+"-"+methodName, e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		} finally {
			modelService.save(cronjob);
		}
		LOG.debug("Completed JnjCDLHybrisExportJob Job");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	public void tryExportService(final String types, final JnjCDLExportJobModel cronjob) throws BusinessException {
		final String[] typeList = types.split(COMMA);
		final List<String> fileList = new ArrayList<>();
        final String methodName = "tryExportService";
		
		final Date cronjobLastSuccessfulRunDate = getDateForCronJob(cronjob);
		try {
			final String compressvalue = configurationService.getConfiguration().getString("cdl.mbox.file.compress");
		for (final String type : typeList)
		{
			
			breakRecordInMultipleList(cronjob, cronjobLastSuccessfulRunDate, fileList, type);
			}

			if (YES.equalsIgnoreCase(compressvalue))
			{
				zipFiles(fileList);
			}

			//sending the data to mbox
			LOG.info(" Files generated successfully and sending to MBOX location");
			JnJLaCronjobUtil.writeFileToMBOX(hostName, userName, password, Jnjlab2bcoreConstants.CDL.FTP_PORT_NUMBER, srcDirectory,
				destDirectory, true);
		}
		catch (final IOException e)
		{
			LOG.error(MESSAGE+CURRENTCLASS+"-"+methodName, e);
		}

	}


	private void breakRecordInMultipleList(final JnjCDLExportJobModel cronjob, final Date cronjobLastSuccessfulRunDate,
			final List<String> fileList, final String type)
			throws IOException, BusinessException
	{
		List<Object> cdlExportForType = null;
		if (BooleanUtils.isTrue(cronjob.getIsFetchFromDateRange()))
		{
			cdlExportForType = jnjCDLExportDao.getCDLExportForTypeForDate(type, cronjob.getStartDate(), cronjob.getEndDate());
		}
		else
		{
			cdlExportForType = jnjCDLExportDao.getCDLExportForType(type, cronjobLastSuccessfulRunDate);
		}
		final int maxCSVLoad = (cronjob.getQueryCount() > 0) ? cronjob.getQueryCount() : 2000;
		int fileCount = 1;
		final String timestamp = new SimpleDateFormat("MMddyyyy").format(new Date());
		final List<String> controlFile = new ArrayList<>();
		if (cdlExportForType.size() > maxCSVLoad)
		{
			for (final List<Object> data : Lists.partition(cdlExportForType, maxCSVLoad))
			{
				generateCSV(cronjob, fileList, type, fileCount, data, timestamp, controlFile);
				fileCount++;
			}
		}
		else
		{
			generateCSV(cronjob, fileList, type, fileCount, cdlExportForType, timestamp, controlFile);
		}

		genrateControlFile(fileList, type, timestamp, controlFile);
	}

	private void genrateControlFile(final List<String> fileList, final String type, final String timestamp,
			final List<String> controlFile) throws IOException
	{
		final String methodName = "genrateControlFile";
		
		final StringBuilder filepath = new StringBuilder();
		filepath.append(srcDirectory).append("control_").append(type).append(UNDERSCORE).append(timestamp).append(".csv");
		final CSVWriter writer = new CSVWriter(new FileWriter(filepath.toString()));

		for (final String row : controlFile)
		{

			try
			{
				writer.writeSrcLine(row);
			}
			catch (final IOException e)
			{
				LOG.error(MESSAGE+CURRENTCLASS+"-"+methodName, e);
			}
		}
		writer.close();
		fileList.add(filepath.toString());
	}

	private void generateCSV(final JnjCDLExportJobModel cronjob, final List<String> fileList, final String type,
			final int fileCount, final List<Object> data,
			final String timestamp, final List<String> controlFile) throws IOException, BusinessException
	{
		final StringBuilder filepath = new StringBuilder();
		final List<SelectedAttribute> selectedAttributeList = populateAttributeList(type);
				
		filepath.append(srcDirectory).append(type).append(timestamp).append(UNDERSCORE).append(fileCount).append(".csv");
		CSVWriter writer = null;
		writer = new CSVWriter(new FileWriter(filepath.toString()));
		addCSVHeaderValues(selectedAttributeList, writer);
		if (CollectionUtils.isNotEmpty(data)) {
			addCSVValues(data, selectedAttributeList, writer, cronjob);
		}
		writer.close();
		fileList.add(filepath.toString());
		final StringBuilder row = new StringBuilder();
		row.append(type).append(timestamp).append(UNDERSCORE).append(fileCount).append(".csv");
		row.append(COMMASPACE).append(String.valueOf(data.size()));
		controlFile.add(row.toString());
	}

	public Date getDateForCronJob(final JnjCDLExportJobModel cronjob)
	{
		if (Objects.isNull(cronjob.getDataForLastNoOfDays()) || cronjob.getDataForLastNoOfDays().intValue() == 0)
		{
			return null;

		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -cronjob.getDataForLastNoOfDays().intValue());
		return cal.getTime();
	}

	public List<SelectedAttribute> populateAttributeList(final String type)
	{
		final Collection<AttributeDescriptorModel> attributes = new HashSet<>();
		final ComposedTypeModel typeModel = typeDao.findComposedTypeByCode(type);
		attributes.addAll(typeModel.getInheritedattributedescriptors());
		attributes.addAll(typeModel.getDeclaredattributedescriptors());

		if(null != typeModel.getSubtypes()){
			for (final ComposedTypeModel allSubType : typeModel.getAllSubTypes())
			{
				attributes.addAll(allSubType.getDeclaredattributedescriptors());
			}
		}
		final StringBuilder genericExcludedField = new StringBuilder(
				configurationService.getConfiguration().getString("jnj.cdl.exclude.fields.Generic", StringUtils.EMPTY));
		final StringBuilder typeExcludedField = new StringBuilder(
				configurationService.getConfiguration().getString("jnj.cdl.exclude.fields." + type +"" , StringUtils.EMPTY));


		final StringBuilder fieldsToIgnore = genericExcludedField.append(COMMA).append(typeExcludedField);
		final String[] ignoreList = fieldsToIgnore.toString().split(COMMA);

		final List<SelectedAttribute> attrList = new ArrayList<>();

		for (final AttributeDescriptorModel attribute : attributes)
		{
			if(Arrays.stream(ignoreList).noneMatch(attribute.getQualifier().toLowerCase(Locale.ENGLISH)::contains)){
				attrList.add(new SelectedAttribute(attribute));
			}
		}
		return attrList.stream().sorted((o1, o2) -> o1.getQualifier().compareTo(o2.getQualifier()))
				.toList();
	}

	void addCSVHeaderValues(final List<SelectedAttribute> selectedAttributeList, final CSVWriter writer)
	{
		final String methodName = "addCSVHeaderValues";
		
		final StringBuilder header = new StringBuilder();
		int counter = 0;
		for (final SelectedAttribute selAtt : selectedAttributeList)
		{
			header.append(selAtt.getQualifier());
			counter++;
			appendColumn(selectedAttributeList, header, counter);
		}
		try {
			writer.writeSrcLine(header.toString());
		}
		catch (final IOException e)
		{
			LOG.error(MESSAGE+CURRENTCLASS+"-"+methodName, e);
		}
	}

	void addCSVValues(final List<Object> itemsByType, final List<SelectedAttribute> selectedAttributes, final CSVWriter writer,
			final JnjCDLExportJobModel cronjob) throws BusinessException
	{

		final String methodName = "addCSVValues";
		final StringBuilder rows = new StringBuilder();
		int lineBreakCountr = 0;
		for (final Object item : itemsByType)
		{
			int counter = 1;
			lineBreakCountr++;
			for (final SelectedAttribute selectedAttribute : selectedAttributes)
			{
				counter = appendValuesInRows(selectedAttributes, rows, (ItemModel) item, counter, selectedAttribute);
				clearAbortRequest(cronjob);
			}
			if (itemsByType.size() > lineBreakCountr)
			{
				rows.append("\n");
			}
		}

		try {
			writer.writeSrcLine(rows.toString());
		}
		catch (final IOException e)
		{
			LOG.error(MESSAGE+CURRENTCLASS+"-"+methodName, e);
		}
	}

	static int appendValuesInRows(final List<SelectedAttribute> selectedAttributes, final StringBuilder rows, final ItemModel item,
			int counter, final SelectedAttribute selectedAttribute)
	{
		try {
			createRowsForType(selectedAttributes, rows, item, counter, selectedAttribute);
		}
		catch (final UnsupportedOperationException ue)
		{
			if (LOG.isDebugEnabled()) {
				LOG.debug("Cannot copy value for the given attribute: " + ue);
			}
			appendColumn(selectedAttributes, rows, counter);
		}
		catch (final JaloSystemException je)
		{
			if (LOG.isDebugEnabled()) {
				LOG.debug("Attribute not present in subtype: " + je);
			}
			appendColumn(selectedAttributes, rows, counter);
		}
		catch (final Exception e)
		{
			LOG.debug("exception in column while appending the rows: " + e);
			appendColumn(selectedAttributes, rows, counter);
		} finally {
			counter++;
		}
		return counter;
	}

	private void clearAbortRequest(final JnjCDLExportJobModel cronjob) throws BusinessException
	{
		if(clearAbortRequestedIfNeeded(cronjob)) {
			LOG.info("Job aborted manually !");
			throw new BusinessException("Cronjob aborted manually");
		}
	}

	private static void appendColumn(final List<SelectedAttribute> selectedAttributes, final StringBuilder rows, final int counter)
	{
		if (selectedAttributes.size() != counter) {
			rows.append(COMMA);
		}
	}

	static PerformResult cronJobAborted() {
		return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
	}

	static void createRowsForType(final List<SelectedAttribute> selectedAttributes, final StringBuilder rows, final ItemModel item,
			final int counter, final SelectedAttribute selectedAttribute)
	{
		if(PK.equalsIgnoreCase(selectedAttribute.getQualifier())){
			rows.append(item.getPk());
		}
		else{
			getAttributeValueWhenNotNull(rows, item, selectedAttribute);
		}

		appendColumn(selectedAttributes, rows, counter);
	}


	static void getAttributeValueWhenNotNull(final StringBuilder rows, final ItemModel item,
			final SelectedAttribute selectedAttribute)
	{
		if(null != item.getProperty(selectedAttribute.getQualifier())) {
			if (item.getProperty(selectedAttribute.getQualifier()) instanceof ItemModel) {
				final ItemModel qualifierModel = item.getProperty(selectedAttribute.getQualifier());
				final StringBuilder value = new StringBuilder();
				value.append('"').append(qualifierModel.getPk()).append('"');
				rows.append(value.toString());
			}
			else if (item.getProperty(selectedAttribute.getQualifier()) instanceof Collection) {
				getAttributeValueWhenCollectionType(rows, item, selectedAttribute);
			}
			else {
				final StringBuilder value = new StringBuilder();
				value.append('"').append(item.getProperty(selectedAttribute.getQualifier()).toString().replace("\"", "")).append('"');
				rows.append(value.toString().replace("\n", " "));

			}
		}
	}

	private static void getAttributeValueWhenCollectionType(final StringBuilder rows, final ItemModel item,
			final SelectedAttribute selectedAttribute)
	{
		final List<Object> listValue = new ArrayList<>(item.getProperty(selectedAttribute.getQualifier()));
		final Set<String> temp = new HashSet<>();
		listValue.forEach(listItem->{
			if (listItem instanceof ItemModel listItemModel) {
				temp.add(String.valueOf(listItemModel.getPk()));
			}
			else {
				temp.add(String.valueOf(listItem).replace("\n", " ").replace("\"", ""));
			}
		});

		final String value = temp.stream().collect(Collectors.joining(COMMASPACE));
		rows.append(new StringBuilder().append('"').append(value).append('"').toString());
	}


	public void zipFiles(final List<String> files) throws IOException
	{
		final String methodName = "zipFiles";
		final String timestamp = new SimpleDateFormat("MMddyyyy").format(new Date());
		final String zipFilePath = srcDirectory + "CDLZip" + timestamp + ".zip";

		try(FileOutputStream fos = new FileOutputStream(zipFilePath);
			ZipOutputStream zipOut =  new ZipOutputStream(new BufferedOutputStream(fos));
		) {
			for (final String filePath : files)
			{
				final File input = new File(filePath);
				final ZipEntry ze = new ZipEntry(input.getName());
				LOG.info("Zipping the file: "+input.getName());
				zipOut.putNextEntry(ze);
				final FileInputStream fis = new FileInputStream(input);
				zipOut.write(IOUtils.toByteArray(fis));
				zipOut.closeEntry();
				fis.close();
			}

			for (final String filePath : files)
			{
				final File input = new File(filePath);
				Files.delete(input.toPath());
			}

			LOG.info("Done... Zipped the files...");
		}	
		catch (final IOException e)
		{
			LOG.error(MESSAGE+CURRENTCLASS+"-"+methodName, e);
		}

	}
	
	public TypeDao getTypeDao() {
		return typeDao;
	}

	public void setTypeDao(final TypeDao typeDao)
	{
		this.typeDao = typeDao;
	}

	public JnjCDLExportDao getJnjCDLExportDao()
	{
		return jnjCDLExportDao;
	}

	public void setJnjCDLExportDao(final JnjCDLExportDao jnjCDLExportDao)
	{
		this.jnjCDLExportDao = jnjCDLExportDao;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public static ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public static void setConfigurationService(final ConfigurationService configurationService)
	{
		JnjCDLHybrisExportJob.configurationService = configurationService;
	}
}