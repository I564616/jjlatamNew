/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dataload.utility;

import com.sun.xml.ws.util.xml.StAXSource;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.XMLConstants;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjObjectComparator;



// TODO: Auto-generated Javadoc
/**
 * This class is responsible for picking and sorting the XML files for Load.
 * 
 * @author sanchit.a.kumar
 */
/**
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJXMLFilePicker
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnJXMLFilePicker.class);

	/** The Constant ERROR_ON_NULL_HOT_FOLDER_PATH. */
	protected static final String ERROR_ON_NULL_HOT_FOLDER_PATH = "Recieved Null Hot Folder Path; Returning Null Files";

	/** The Constant SYBMOL_DOT. */
	protected static final String SYBMOL_DOT = ".";

	/** The Constant SYBMOL_FORWARDSLASH. */
	protected static final String SYBMOL_FORWARDSLASH = "//";

	/** The Constant FILE_TYPE_ZIP. */
	protected static final String FILE_TYPE_ZIP = ".zip";



	/**
	 * over the file list supplied, fetching the files having the fileNamePrefix in their names, and finally sorting them
	 * based on the date and index in the filename and returning the tree map containing the same.
	 * 
	 * @param files
	 *           the files
	 * @param fileNamePrefix
	 *           the file name prefix
	 * @return sortedFiles
	 */
	public static TreeMap<String, File> pickAndSortFiles(final File[] files, final String fileNamePrefix)
	{
		String fileName;

		// TreeMap for the sorting of the files
		final TreeMap<String, File> sortedFiles = new TreeMap<String, File>();

		// Iterating over the File List
		for (final File file : files)
		{
			if (file.isFile())
			{
				// Getting file name and converting it to lower case
				fileName = file.getName().toLowerCase();

				// Fetching only those files that contain the fileNamePrefix
				if (fileName.startsWith(fileNamePrefix))
				{
					// Inserting the files in the tree map for ssorting
					sortedFiles.put(fileName, file);
				}
			}
		}
		return sortedFiles;
	}



	/**
	 * This method is responsible for iterating over the file list supplied, fetching the files having the fileNamePrefix
	 * in their names, and finally sorting them based on the date and index in the filename and returning the Array List
	 * containing the same.
	 * 
	 * 
	 * the folder name
	 * 
	 * @param folderKey
	 *           the folder key
	 * @return sortedFiles
	 */
	public static List<File> pickAndSortFiles(final String folderKey)
	{
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.BEGIN_OF_METHOD + " pickAndSortFiles()");
		String filePath = null;
		final JnjObjectComparator jnjObjectComparator;
		final String rootFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT);
		final String incomingFolderPath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING);
		final String fileNamePrefixKey = folderKey + Jnjb2bCoreConstants.FEED_FILE_NAME_PREFIX;

		final String folderName = Config.getParameter(folderKey);
		// list for the sorting of the files
		final List<File> lstFiles = new ArrayList<File>();

		if (StringUtils.isNotEmpty(rootFilePath) && StringUtils.isNotEmpty(incomingFolderPath)
				&& StringUtils.isNotEmpty(folderName))
		{
			filePath = rootFilePath + incomingFolderPath + folderName;
			LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - Input Folder: " + filePath);
			//Getting all the files for the filepath
			final File[] files = new File(filePath).listFiles();

			// Iterating over the File List
			for (final File file : files)
			{
				if (file.isFile())
				{
					// Getting file name 
					final String fileName = file.getName().toLowerCase();

					// Fetching only those files that contain the fileNamePrefix 
					if (fileName.startsWith(Config.getParameter(fileNamePrefixKey).toLowerCase()))
					{
						// Inserting the files in the List
						lstFiles.add(file);
					}
				}
			}

			jnjObjectComparator = new JnjObjectComparator(File.class, "getName", true, true);
			Collections.sort(lstFiles, jnjObjectComparator);
		}
		else
		{
			LOGGER.error("Filepath Root or Folder Name Incorrect, Returning Null");
		}
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.END_OF_METHOD + " pickAndSortFiles()");
		return lstFiles;
	}

	/**
	 * This method is responsible for iterating over the file list supplied, fetching the files having the fileNamePrefix
	 * in their names, and finally sorting them based on the date and index in the filename and returning the Array List
	 * containing the same.
	 * 
	 * 
	 * the folder name
	 * 
	 * @param fileNamePrefixKey
	 *           the file name prefix key
	 * @param dir
	 *           file directory
	 * @return sortedFiles
	 */

	public static List<String> getFilesFromDir(final String fileNamePrefixKey, final String dir)
	{
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.BEGIN_OF_METHOD + " pickAndSortFiles()");
		final JnjObjectComparator jnjObjectComparator;
		// list for the sorting of the files
		final List<String> lstFiles = new ArrayList<String>();

		if (StringUtils.isNotEmpty(dir))
		{
			LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - Input Folder: " + dir);
			//Getting all the files for the filepath
			final File[] files = new File(dir).listFiles();

			// Iterating over the File List
			for (final File file : files)
			{
				if (file.isFile())
				{
					// Getting file name 
					final String fileName = file.getName().toLowerCase();

					// Fetching only those files that contain the fileNamePrefix 
					if (fileName.startsWith(fileNamePrefixKey))
					{
						// Inserting the files in the List						
						lstFiles.add(file.getAbsolutePath());
					}
				}
			}

			jnjObjectComparator = new JnjObjectComparator(File.class, "getName", true, true);
			Collections.sort(lstFiles, jnjObjectComparator);
		}
		else
		{
			LOGGER.error("Filepath Root or Folder Name Incorrect, Returning Null");
		}
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.END_OF_METHOD + " pickAndSortFiles()");
		return lstFiles;
	}


	/**
	 * Validate xml file.
	 * 
	 * @param file
	 *           the file
	 * @param folderKey
	 *           the folder key
	 * @param forceValidate
	 *           the force validate
	 * @return true, if successful
	 */
	public static boolean validateXmlFile(final File file, final String folderKey, final boolean forceValidate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ " validateXMLFile()" + Logging.HYPHEN + System.currentTimeMillis());
		}
		boolean isValid = false;
		final String validaionRequired = Config.getParameter(Jnjb2bCoreConstants.XSD_VALIDAION_REQUIRED);
		if (!forceValidate && "Y".equalsIgnoreCase(validaionRequired))
		{
			String xsdFolderPath = null;
			final String rootFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT);
			final String incomingFolderPath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING);
			final String xsdFolderName = Config.getParameter(Jnjb2bCoreConstants.FEEDS_XSD_FOLDER_KEY);
			final String folderName = Config.getParameter(folderKey);
			final String fileNamePrefixKey = folderKey + Jnjb2bCoreConstants.FEED_FILE_NAME_PREFIX;
			if (StringUtils.isNotEmpty(rootFilePath) && StringUtils.isNotEmpty(incomingFolderPath)
					&& StringUtils.isNotEmpty(folderName) && StringUtils.isNotEmpty(xsdFolderName))
			{
				xsdFolderPath = rootFilePath + incomingFolderPath + folderName + xsdFolderName;
				try
				{
					//Creating the File filter to pick only "XSD" files
					final FileFilter fileFilter = new FileFilter()
					{
						@Override
						public boolean accept(final File pathname)
						{
							boolean xsdFileMatch = false;
							final String xsdFileName = Config.getParameter(fileNamePrefixKey);
							if (pathname.exists()
									&& StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(pathname.getName()),
											Jnjb2bCoreConstants.FEEDS_XSD_FILE_EXTENSION) && pathname.getName().startsWith(xsdFileName))
							{
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
											+ "The XSD file with Name: [" + pathname.getName() + "] matches the XSD File Name pattern.");
								}
								xsdFileMatch = true;
							}
							else
							{
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
											+ "The XSD file with Name: [" + pathname.getName()
											+ "] does not matche the XSD File Name pattern.");
								}
							}
							return xsdFileMatch;
						}
					};

					final File[] listFiles = new File(xsdFolderPath).listFiles(fileFilter);//Getting the List of files from the XSD folder
					final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(file));

					final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					final Schema schema = factory.newSchema(listFiles[0]);

					final Validator validator = schema.newValidator();
					validator.validate(new StAXSource(reader, false));
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
								+ "The XML file with Name: " + file.getName() + "is Valid");
					}
					isValid = true;
				}
				catch (SAXException | IOException | XMLStreamException | FactoryConfigurationError | SecurityException
						| IndexOutOfBoundsException exception)
				{
					LOGGER.error(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
							+ "The Xml File with name " + file.getName() + " is Not Valid");
					LOGGER.error(
							Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
									+ "Exception  Occured while Validating the file with name " + file.getName() + ". Exception: "
									+ exception.getMessage(), exception);
					isValid = false;
				}
				catch (final Throwable exception)
				{
					LOGGER.error(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
							+ "Exception  Occured while Validating the file with name in throwable block" + file.getName()
							+ ". Exception: " + exception.getMessage(), exception);
					isValid = false;
				}
			}
			else
			{
				LOGGER.error(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
						+ "Unable to get XSD file to validate the XML with file name: [" + file.getName()
						+ "] Throwing Bussiness Exception.");
				isValid = false;
			}
		}
		else
		{
			isValid = true;
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + " validateXMLFile()" + Logging.HYPHEN
						+ "Force Validating the XML file with Name: " + file.getName());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.FILE_PICKING_UTILITY + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ " validateXMLFile()" + Logging.HYPHEN + System.currentTimeMillis());
		}
		return isValid;
	}

	/**
	 * This methods picks and sorts the files on name inside the Hot Folder.
	 * 
	 * @param hotFolderPath
	 *           the hot folder path
	 * @return the list
	 */
	public static List<File> pickAndSortFilesForHotFolder(final String hotFolderPath)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SYNC_ORDER_RENAME_FILES + " - " + Logging.BEGIN_OF_METHOD + " - " + Logging.PICK_AND_SORT_FILES
					+ " - " + JnJCommonUtil.getCurrentDateTime());
		}
		List<File> lstFiles = null;
		if (StringUtils.isNotEmpty(hotFolderPath))
		{
			final File hotFolder = new File(hotFolderPath);
			final File[] fileArr = hotFolder.listFiles();
			Arrays.sort(fileArr, NameFileComparator.NAME_COMPARATOR);
			lstFiles = new ArrayList<File>();
			for (final File fileObj : fileArr)
			{
				lstFiles.add(fileObj);
			}
		}
		else
		{
			LOGGER.error(Logging.SYNC_ORDER_RENAME_FILES + " - " + ERROR_ON_NULL_HOT_FOLDER_PATH);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SYNC_ORDER_RENAME_FILES + " - " + Logging.END_OF_METHOD + " - " + Logging.PICK_AND_SORT_FILES
					+ " - " + JnJCommonUtil.getCurrentDateTime());
		}
		return lstFiles;
	}

	/**
	 * This Utility Method is used to move file for Feeds from source folder to Destination Folder.
	 * 
	 * @param sourceFile
	 *           the source file
	 * @param folderKey
	 *           the folder key
	 * @param destinationFolderName
	 *           the destination folder name
	 * @return true, if successful
	 */
	public static boolean fileMove(final File sourceFile, final String folderKey, final String destinationFolderName)
	{
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.BEGIN_OF_METHOD + " - " + " fileMove()");
		final boolean fileMoved = false;
		//final String destinationFolderPath = null;
		final String rootFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT);
		final String incomingFolderPath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING);

		final String folderName = Config.getParameter(folderKey);

		if (StringUtils.isNotEmpty(rootFilePath) && StringUtils.isNotEmpty(incomingFolderPath)
				&& StringUtils.isNotEmpty(folderName) && StringUtils.isNotEmpty(destinationFolderName) && sourceFile.exists())
		{
			/*
			 * destinationFolderPath = rootFilePath + incomingFolderPath + folderName + destinationFolderName; final File
			 * destinationFolder = new File(destinationFolderPath); try { FileUtils.moveFileToDirectory(sourceFile,
			 * destinationFolder, true); fileMoved = true;
			 * 
			 * } catch (final IOException iOException) { LOGGER.error(Logging.FILE_PICKING_UTILITY + " - " + " fileMove()"
			 * + " - " + "Error Occured " + iOException); }
			 */
		}
		else
		{
			LOGGER.error(Logging.FILE_PICKING_UTILITY + " - " + " fileMove()" + " - "
					+ "Filepath Root or Folder Name or DestinationFolderName is Null, Returning False");
		}
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.END_OF_METHOD + " - " + " fileMove()");
		return fileMoved;
	}




	/**
	 * This Utility Method is used to move file for Feeds from source folder to Destination Folder after Zipping them.
	 * 
	 * @param sourceFile
	 *           the source file
	 * @param folderKey
	 *           the folder key
	 * @param destinationFolderName
	 *           the destination folder name
	 * @param incomingFolderFlag
	 *           the incoming folder flag
	 * @return true, if successful
	 */
	public static boolean zipAndMoveFile(final File sourceFile, final String folderKey, final String destinationFolderName,
			final boolean incomingFolderFlag)

	{
		LOGGER.debug(Logging.FILE_PICKING_UTILITY + " - " + Logging.BEGIN_OF_METHOD + " - " + " zipAndMoveFile()");
		String folderPath = null;
		final byte[] buffer = new byte[1024];
		int bytesRead;
		boolean fileZippedAndMoved = false;
		boolean fileDeleted = false;
		String destinationFolderPath = null;
		final String rootFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT);
		final String fileName = StringUtils.substringBefore(sourceFile.getName(), SYBMOL_DOT);
		if (incomingFolderFlag)
		{
			folderPath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING);
		}
		else
		{
			folderPath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING);
		}

		final String folderName = Config.getParameter(folderKey);
		if (StringUtils.isNotEmpty(rootFilePath) && StringUtils.isNotEmpty(folderPath) && StringUtils.isNotEmpty(folderName)
				&& StringUtils.isNotEmpty(destinationFolderName) && null != sourceFile && sourceFile.exists())
		{
			destinationFolderPath = rootFilePath + folderPath + folderName + destinationFolderName;
			LOGGER.info(Logging.FILE_PICKING_UTILITY + " - " + " zipAndMoveFile()" + " destination folder path: "+destinationFolderPath);
			
			final File destinationFolder = new File(destinationFolderPath);

			if (!(destinationFolder.exists()))
			{
				destinationFolder.mkdirs();
			}
			try
			{
				final String zippedFilePath = destinationFolderPath + SYBMOL_FORWARDSLASH + fileName + FILE_TYPE_ZIP;
				final FileInputStream fileInputStream = new FileInputStream(sourceFile);
				final FileOutputStream fileOutputStream = new FileOutputStream(zippedFilePath);
				final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
				final ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
				zipOutputStream.putNextEntry(zipEntry);

				while ((bytesRead = fileInputStream.read(buffer)) > 0)
				{
					zipOutputStream.write(buffer, 0, bytesRead);
				}
				zipOutputStream.closeEntry();
				fileInputStream.close();
				zipOutputStream.close();
				fileZippedAndMoved = true;
			}
			catch (final IOException iOException)
			{
				LOGGER.error(Logging.FILE_PICKING_UTILITY + " - " + " zipAndMoveFile()" + " - " + "Exception Occured " + fileName  + iOException);
			}
			catch (final Exception exception)
			{
				LOGGER.error(Logging.FILE_PICKING_UTILITY + " - " + " zipAndMoveFile()" + " - " + "Exception Occured while moving the file " + fileName + exception);
			}
		}
		else
		{
			LOGGER.error(Logging.FILE_PICKING_UTILITY + " - " + " fileMove()" + " - "
					+ "Filepath Root or Folder Name or DestinationFolderName is Null, Returning False");
		}
		if (fileZippedAndMoved)
		{
			LOGGER.info(Logging.FILE_PICKING_UTILITY + " - " + " zipAndMoveFile()" + " - Deleting File "+ fileName + " from Original Location");
			fileDeleted = sourceFile.delete();
		}
		LOGGER.info(Logging.FILE_PICKING_UTILITY + " - " + Logging.END_OF_METHOD + " - " + " zipAndMoveFile()");
		return fileDeleted;
	}
}
