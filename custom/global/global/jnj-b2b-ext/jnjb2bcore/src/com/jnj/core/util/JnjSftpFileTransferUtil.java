/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.util;

import com.sshtools.client.PasswordAuthenticator;
import com.sshtools.client.SshClient;
import com.sshtools.client.SshClientContext;
import com.sshtools.common.logger.Log;
import com.sshtools.common.ssh.SshConnection;
import com.sshtools.common.ssh.SshException;
import com.sshtools.common.util.Utils;
import com.sshtools.synergy.nio.ConnectRequestFuture;
import com.sshtools.synergy.nio.SshEngine;
import com.sshtools.synergy.ssh.Connection;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.FileUploadDTO;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSftpFileTransferUtil
{


	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjSftpFileTransferUtil.class);

	/* Connection Detail For the Empheno Document */
	/** The hostname empheno. */
	private final String hostnameEmpheno = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_HOSTNAME_EMPHENO);

	/** The username empheno. */
	private final String usernameEmpheno = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_USERNAME_EMPHENO);

	/** The password empheno. */
	private final String passwordEmpheno = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_EMPHENO);

	/** The port empheno. */
	private final int portEmpheno = Integer.parseInt((Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PORT_EMPHENO)));

	/* Connection Detail For the Submit EDI Document */
	/** The hostname submit ed i. */
	private final String hostnameSubmitEdI = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_HOSTNAME_SUBMIT_EdI);

	/** The username submit ed i. */
	private final String usernameSubmitEdI = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_USERNAME_SUBMIT_EdI);

	/** The password submit ed i. */
	private final String passwordSubmitEdI = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SUBMIT_EdI);

	/** The port submit ed i. */
	private final int portSubmitEdI = Integer.parseInt((Config
			.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PORT_SUBMIT_EdI)));

	/* Connection Detail For the Sellout Medical Document */
	/** The hostname sellout mdd. */
	private final String hostnameSelloutMDD = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_HOSTNAME_SELLOUT_MDD);

	/** The username sellout mdd. */
	private final String usernameSelloutMDD = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_USERNAME_SELLOUT_MDD);

	/** The password sellout mdd. */
	private final String passwordSelloutMDD = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SELLOUT_MDD);

	/** The port sellout mdd. */
	private final int portSelloutMDD = Integer.parseInt((Config
			.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PORT_SELLOUT_MDD)));


	/* Connection Detail For the Sellout Pharma Document */
	/** The hostname sellout pharma. */
	private final String hostnameSelloutPharma = Config
			.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_HOSTNAME_SELLOUT_PHARMA);

	/** The username sellout pharma. */
	private final String usernameSelloutPharma = Config
			.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_USERNAME_SELLOUT_PHARMA);

	/** The password sellout pharma. */
	private final String passwordSelloutPharma = Config
			.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SELLOUT_PHARMA);

	/** The port sellout pharma. */
	private final int portSelloutPharma = Integer.parseInt((Config
			.getParameter(Jnjb2bCoreConstants.DocumenTransfer.SFTP_PORT_SELLOUT_PHARMA)));

	//NAGS
	private static final String CONNECTIONS_DETAILS_HOST_NAME="SFTP: Connection Details  HostName:-->";
	private static final String USER_NAME=" UserName:-->";
	private static final String PORT=" Port :-->";
	private static final String SECRET_KEY=" PasswordKey:-->";
	private static final String STRICT_HOST_KEY_CHECKING="StrictHostKeyChecking";

	/**
	 * Upload file to sftp.
	 * 
	 * @param fileUploadDTO
	 *           the file upload dto
	 * @param localFilePath
	 *           the local file path
	 * @param remoteFilePath
	 *           the remote file path
	 * @param sftpConnectionType
	 *           the sftp connection type
	 * @return true, if successful
	 */
	public boolean uploadFileToSftp(final FileUploadDTO fileUploadDTO, String localFilePath, String remoteFilePath,
			final String sftpConnectionType)
	{


		boolean sftpflag = false;
		String finallocalFilePath = null;
		if (StringUtils.isNotEmpty(localFilePath))
		{
			localFilePath = localFilePath.replace('\\', '/');
		}
		if (StringUtils.isNotEmpty(remoteFilePath))
		{
			remoteFilePath = remoteFilePath.replace('\\', '/');
		}

		LOG.debug("SFTP: localFilePath=" + localFilePath + " and  remoteFilePath " + remoteFilePath);
		if (null != fileUploadDTO.getRenameFileTo())
		{
			finallocalFilePath = localFilePath + fileUploadDTO.getRenameFileTo();
			LOG.debug("SFTP:File Has To Transfer from " + finallocalFilePath + " to " + remoteFilePath);
		}
		else
		{
			final String fileName = StringUtils.isNotEmpty(fileUploadDTO.getName()) ? fileUploadDTO.getName() : fileUploadDTO
					.getFile().getName();
			finallocalFilePath = localFilePath + fileName;
			LOG.debug("SFTP: File Has To Transfer from " + finallocalFilePath + " to " + remoteFilePath);
		}

		final File file = new File(finallocalFilePath);
		if (!file.exists())
		{
			LOG.error("SFTP: Error. Local file not found in the given location" + finallocalFilePath);
			sftpflag = false;

		}

		else
		{
			Session session = null;
			Channel channel = null;
			ChannelSftp channelSftp = null;
			try
			{
				final JSch jsch = new JSch();
				if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_EMPHENO))
				{
					session = jsch.getSession(usernameEmpheno, hostnameEmpheno, portEmpheno);
					session.setPassword(passwordEmpheno);
					LOG.debug("SFTP: Connection Details  HostName:-->" + hostnameEmpheno + " UserName::-->" + usernameEmpheno
							+ " Port ::-->" + portEmpheno + " PasswordKey:-->"
							+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_EMPHENO);
				}
				else if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SUBMITEDI))
				{
					session = jsch.getSession(usernameSubmitEdI, hostnameSubmitEdI, portSubmitEdI);
					session.setPassword(passwordSubmitEdI);
					LOG.debug("SFTP: Connection Details  HostName:--> " + hostnameSubmitEdI + " UserName:-->" + usernameSubmitEdI
							+ " Port:-->" + portSubmitEdI + " Password Key:-->"
							+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SUBMIT_EdI);
				}
				else if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SELLOUT_MDD))
				{
					session = jsch.getSession(usernameSelloutMDD, hostnameSelloutMDD, portSelloutMDD);
					session.setPassword(passwordSelloutMDD);
					LOG.debug("SFTP: Connection Details  HostName:-->" + hostnameSelloutMDD + " UserName:-->" + usernameSelloutMDD
							+ " Port :-->" + portSelloutMDD + " Password:-->"
							+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SELLOUT_MDD);
				}
				else if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SELLOUT_PHARMA))
				{
					session = jsch.getSession(usernameSelloutPharma, hostnameSelloutPharma, portSelloutPharma);
					session.setPassword(passwordSelloutPharma);
					LOG.debug("SFTP: Connection Details  HostName:-->" + hostnameSelloutPharma + " UserName:-->"
							+ usernameSelloutPharma + " Port :-->" + portSelloutPharma + " PasswordKey:-->"
							+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SELLOUT_PHARMA);
				}

				final java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();
				channel = session.openChannel("sftp");
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				channelSftp.cd(remoteFilePath);
				channelSftp.put(new FileInputStream(file), file.getName());
				channelSftp.ls(remoteFilePath);
				sftpflag = true;
			}

			catch (final Exception exception)
			{
				// As we are  using Jsch API ,  we are using general exception class.
				LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + " - " + Jnjb2bCoreConstants.Logging.SFTP_UTIL_METHOD + "-"
						+ JnJCommonUtil.getCurrentDateTime() + exception);
				LOG.error(exception);
				sftpflag = false;
			}
		}

		return sftpflag;
	}

	/**
	 * Upload empenho docs to sftp, Files will be renamed to given SAP order number. In case of error in SFTP channel
	 * files will be moved to defined Error Folder.
	 * 
	 * @param filePaths
	 *           the list of file path that need to be uploaded to SFTP Location
	 * @param sapOrderNumber
	 *           String required file name for SFTP location
	 * @param hybrisOrderNumber
	 *           String orderNumber generated by Hybris system
	 * 
	 * @return true, if successful
	 */
	public boolean uploadEmpenhoDocsToSftp(final List<String> filePaths, final String sapOrderNumber,
			final String hybrisOrderNumber)
	{
		LOG.info("Uploading files to SFTP Starts, toatal number of files:" + filePaths.size());
		int count = 0;
		for (final String existingFilePath : filePaths)
		{
			final String existingFileName = existingFilePath.substring(existingFilePath.lastIndexOf(File.separator) + 1);
			final FileUploadDTO fileUploadDTO = new FileUploadDTO();
			//New names will be named with sap Order number + _1 _2 _3 ... so on 
			final String newNameForEmpenhoDoc = JnjFileUploadToSharedFolderUtil.changeFileName(existingFileName,
					getNameForSMTP(++count, sapOrderNumber, hybrisOrderNumber));
			final String newPath = existingFilePath.replace(existingFileName, newNameForEmpenhoDoc);
			JnjFileUploadToSharedFolderUtil.renameFile(existingFilePath, newPath);

			fileUploadDTO.setFilePathInShareFolder(newPath);
			fileUploadDTO.setRemoteFilePath(Config.getParameter(Jnjb2bCoreConstants.UploadFile.SFTP_DESTINATION_EMPHENO_FOLDER));
			final boolean sftupUploadStauts = uploadFileToSftp(fileUploadDTO,
					Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_EMPHENO);
			LOG.info("Final status for File Upload To SFTP for file no " + count + " is: " + sftupUploadStauts);
			//Move file to error folder in case issue in SMTP
			if (!sftupUploadStauts)
			{
				LOG.info("SFTP for file no " + count + " is not successful so moving it to error Folder");
				final String errorInSeningFolderPath = Config.getParameter(
						Jnjb2bCoreConstants.UploadFile.FEED_FILEPATH_EMPENHO_DOC_ERROR).concat(File.separator)
						+ newNameForEmpenhoDoc;
				JnjFileUploadToSharedFolderUtil.renameFile(fileUploadDTO.getFilePathInShareFolder(), errorInSeningFolderPath);
			}
		}
		return true;
	}

	/**
	 * Gets the name for smtp name will have 5 diffrent components 1) Field 1: Fixed prefix IN0500 (length=6). 2) Field
	 * 2: SAP Order Number (length=10). 3) Field 3: Hybris Order Number (length=8). 4) Field 4: Sequential number
	 * (length=3). As the user is allowed to upload multiples files in a same order, this field can vary from 001 to 999
	 * allowing the user to upload up to 999 files per order. Two uploaded files must not have the same name. 5) Field 5:
	 * Date when the order was created on hybris in format MMDDYYYY (length=8). Example:
	 * IN0500_1234567890_12345678_123_12092013
	 * 
	 * @param seqNum
	 *           the seq num
	 * @param sapOrderNum
	 *           the sap order num
	 * @param hybrisOrderNum
	 *           the hybris order num
	 * @return the name for smtp
	 */
	private String getNameForSMTP(final int seqNum, final String sapOrderNum, final String hybrisOrderNum)
	{
		final StringBuffer name = new StringBuffer(JnJCommonUtil.getValue(Jnjb2bCoreConstants.EMPENHO_DOCS_PREFIX_KEY));
		name.append(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL).append(sapOrderNum);
		name.append(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL).append(hybrisOrderNum);
		name.append(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL).append(String.format("%03d", seqNum));
		return name.toString();
	}

	/**
	 * Upload file to sftp.
	 * 
	 * @param fileUploadDTO
	 *           the file upload dto
	 * @param sftpConnectionType
	 *           the sftp connection type
	 * @return true, if successful
	 */
	public boolean uploadFileToSftp(final FileUploadDTO fileUploadDTO, final String sftpConnectionType)
	{
		final String methodName = "uploadFileToSftp()";
		boolean sftpflag = false;
		final String finallocalFilePath = null;
		String remoteFilePath = fileUploadDTO.getRemoteFilePath();
		if (StringUtils.isNotEmpty(remoteFilePath))
		{
			remoteFilePath = remoteFilePath.replace('\\', '/');
		}
		LOG.debug("SFTP: localFilePath=" + fileUploadDTO.getFilePathInShareFolder() + " and  remoteFilePath " + remoteFilePath);
		final File file = new File(fileUploadDTO.getFilePathInShareFolder());
		if (!file.exists())
		{
			LOG.error("SFTP: Error. Local file not found in the given location" + finallocalFilePath);
			sftpflag = false;
		}
		else
		{
			//Using Maverick-Synergy-client API for SSH/SFTP File Transfer

			try (SshClient ssh = createSshClient(sftpConnectionType)) {
				JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SFTP_UTIL, "-" , "SSH client created now placing the file to" + remoteFilePath ,
						JnjSftpFileTransferUtil.class);
				ssh.putFile(file,remoteFilePath);
				JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SFTP_UTIL, "File successfully uploaded to {}" + remoteFilePath, methodName ,
						JnjSftpFileTransferUtil.class);
				sftpflag = true;

			}
			catch (final IOException | SshException exception)
			{
				JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SFTP_UTIL , " - " , Jnjb2bCoreConstants.Logging.SFTP_UTIL_METHOD + "-"
						+ JnJCommonUtil.getCurrentDateTime() + Arrays.toString(exception.getStackTrace()), JnjSftpFileTransferUtil.class );
				JnjGTCoreUtil.logErrorMessage(Jnjb2bCoreConstants.Logging.SFTP_UTIL  , " - Exception occurred while transferring the files to SFTP server " ,exception.getMessage(), exception , JnjSftpFileTransferUtil.class);
				sftpflag = false;
			}
			}
		return sftpflag;
	}

	private SshClient createSshClient(String sftpConnectionType) throws IOException, SshException {
		Log.enableConsole(Log.Level.TRACE);

		if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_EMPHENO)) {
			int port = portEmpheno;
			if (Utils.hasPort(hostnameEmpheno)) {
				port = Utils.getPort(hostnameEmpheno);
			}
			return SshClient.SshClientBuilder.create()
					.withHostname(hostnameEmpheno)
					.withPort(port)
					.withUsername(usernameEmpheno)
					.withPassword(passwordEmpheno)
					.build();
		} else if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SUBMITEDI)) {
			int port = portSubmitEdI;
			if (Utils.hasPort(hostnameSubmitEdI)) {
				port = Utils.getPort(hostnameSubmitEdI);
			}
			return SshClient.SshClientBuilder.create()
					.withHostname(hostnameSubmitEdI)
					.withPort(port)
					.withUsername(usernameSubmitEdI)
					.withPassword(passwordSubmitEdI)
					.build();
		} else if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SELLOUT_MDD)) {
			int port = portSelloutMDD;
			if (Utils.hasPort(hostnameSelloutMDD)) {
				port = Utils.getPort(hostnameSelloutMDD);
			}
			return SshClient.SshClientBuilder.create()
					.withHostname(hostnameSelloutMDD)
					.withPort(port)
					.withUsername(usernameSelloutMDD)
					.withPassword(passwordSelloutMDD)
					.build();
		} else if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SELLOUT_PHARMA)) {
			int port = portSelloutPharma;
			if (Utils.hasPort(hostnameSelloutPharma)) {
				port = Utils.getPort(hostnameSelloutPharma);
			}
			return SshClient.SshClientBuilder.create()
					.withHostname(hostnameSelloutPharma)
					.withPort(port)
					.withUsername(usernameSelloutPharma)
					.withPassword(passwordSelloutPharma)
					.build();
		} else {
			throw new IllegalArgumentException("Invalid SFTP Connection Type");
		}
	}

	/**
	 * The uploadMultipleFileToSftp method uploads the multiple file in the sftp folder.
	 * 
	 * @param fileList
	 *           the file list
	 * @param remoteFilePath
	 *           the remote file path
	 * @param sftpConnectionType
	 *           the sftp connection type
	 * @return true, if successful
	 */
	public boolean uploadMultipleFileToSftp(final List<File> fileList, String remoteFilePath, final String sftpConnectionType)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean sftpFlag = false;

		// check for the remoteFilePath if its not empty then enter in the if block.
		if (StringUtils.isNotEmpty(remoteFilePath))
		{
			// replace the backward slash with the forward slash.
			remoteFilePath = remoteFilePath.replace('\\', '/');
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
					+ " SFTP: remoteFilePath " + remoteFilePath);
		}
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		try
		{
			final JSch jsch = new JSch();
			if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SUBMITEDI))
			{
				session = jsch.getSession(usernameSubmitEdI, hostnameSubmitEdI, portSubmitEdI);
				session.setPassword(passwordSubmitEdI);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("SFTP: Connection Details  HostName:--> " + hostnameSubmitEdI + " UserName:-->" + usernameSubmitEdI
							+ " Port:-->" + portSubmitEdI + " Password Key:-->" + passwordSubmitEdI);
				}
			}

			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
						+ " Connection Established" + remoteFilePath);
			}
			channelSftp = (ChannelSftp) channel;
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
						+ "Starting Change Directory " + remoteFilePath);
			}
			channelSftp.cd(remoteFilePath);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
						+ " Directory Changed to Remote File Path" + remoteFilePath);
			}
			sftpFlag = true;
		}
		catch (final JSchException jschException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
					+ "Jsch exception occured " + jschException.getMessage(), jschException);
			sftpFlag = false;
		}
		catch (final SftpException sftpException)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
					+ "SFTP exception occured " + sftpException.getMessage(), sftpException);
			sftpFlag = false;
		}
		// enters in the if block if the sftpFlag is true.
		if (sftpFlag)
		{
			for (final File file : fileList)
			{
				// check for the file is existed
				if (!file.exists())
				{
					LOG.error("SFTP: Error. Local file not found in the given location" + file);
					sftpFlag = false;
				}
				else
				{
					FileInputStream fileInputStream = null;
					try
					{
						fileInputStream = new FileInputStream(file);
						if (LOG.isDebugEnabled())
						{
							LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
									+ Logging.HYPHEN + " File Input Stream " + fileInputStream);
						}
						channelSftp.put(fileInputStream, file.getName());
						if (LOG.isDebugEnabled())
						{
							LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
									+ Logging.HYPHEN + " After putting the content successfully in sftp folder");
						}
						channelSftp.ls(remoteFilePath);
						if (LOG.isDebugEnabled())
						{
							LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
									+ Logging.HYPHEN + " After getting the list of files from the sftp folder");
						}
						sftpFlag = true;
					}
					catch (final FileNotFoundException fileNotFoundException)
					{
						LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
								+ Logging.HYPHEN + "File Not Found exception occured " + fileNotFoundException.getMessage(),
								fileNotFoundException);
						sftpFlag = false;
					}
					catch (final SftpException sftpException)
					{
						LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
								+ Logging.HYPHEN + "SFTP exception occured " + sftpException.getMessage(), sftpException);
						sftpFlag = false;
					}
					catch (final Throwable throwable)
					{
						LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
								+ Logging.HYPHEN + "Throwable exception occured " + throwable.getMessage(), throwable);
						sftpFlag = false;
					}
					//Closing the input stream in final block
					finally
					{
						if (null != fileInputStream)
						{
							try
							{
								fileInputStream.close();
							}
							catch (final IOException ioException)
							{
								LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()"
										+ Logging.HYPHEN + "Input Output exception occured " + ioException);
								sftpFlag = false;
							}
						}
					}
				}
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Logging.SFTP_UTIL + Logging.HYPHEN + "uploadMultipleFileToSftp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return sftpFlag;
	}

	/**
	 * Upload File To Sftp
	 * @param sftpConnectionType
	 * @param sftpflag
	 * @param remoteFilePath
	 * @param file
	 * @return
	 */
	public boolean uploadFileToSftp(final String sftpConnectionType, boolean sftpflag,
									final String remoteFilePath, final File file) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		FileInputStream fileInputStream=null;  //VB
		try
		{
			final JSch jsch = new JSch();
			if (sftpConnectionType.equals(Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_EMPHENO))
			{
				session = jsch.getSession(usernameEmpheno, hostnameEmpheno, portEmpheno);
				session.setPassword(passwordEmpheno);
				LOG.debug(CONNECTIONS_DETAILS_HOST_NAME + hostnameEmpheno + " UserName::-->" + usernameEmpheno
						+ " Port ::-->" + portEmpheno + SECRET_KEY
						+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_EMPHENO);
			}
			else if (Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_SUBMITEDI.equals(sftpConnectionType))
			{
				/** The port submit ed i. */
				session = jsch.getSession(usernameSubmitEdI, hostnameSubmitEdI, portSubmitEdI);
				session.setPassword(passwordSubmitEdI);
				LOG.debug(CONNECTIONS_DETAILS_HOST_NAME + hostnameSubmitEdI + USER_NAME + usernameSubmitEdI
						+ PORT + portSubmitEdI + SECRET_KEY
						+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SUBMIT_EDI);
			}
			else if (Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_SELLOUT_MDD.equals(sftpConnectionType))
			{
				/** The port sellout mdd. */
				session = jsch.getSession(usernameSelloutMDD, hostnameSelloutMDD, portSelloutMDD);
				session.setPassword(passwordSelloutMDD);
				LOG.debug(CONNECTIONS_DETAILS_HOST_NAME + hostnameSelloutMDD + USER_NAME + usernameSelloutMDD
						+ PORT + portSelloutMDD + " Password:-->"
						+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SELLOUT_MDD);
			}
			else if (Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_SELLOUT_PHARMA.equals(sftpConnectionType))
			{
				/** The port sellout pharma. */
				session = jsch.getSession(usernameSelloutPharma, hostnameSelloutPharma, portSelloutPharma);
				session.setPassword(passwordSelloutPharma);
				LOG.debug(CONNECTIONS_DETAILS_HOST_NAME + hostnameSelloutPharma + USER_NAME
						+ usernameSelloutPharma + PORT + portSelloutPharma + SECRET_KEY
						+ Jnjb2bCoreConstants.DocumenTransfer.SFTP_PASSWORD_SELLOUT_PHARMA);
			}
			else if (Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_NAGS_FILE_MBOX.equals(sftpConnectionType)) {
				final String hostNameNagsMBox = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.NAGS_MBOX_SFTP_HOSTNAME);

				final String userNameNagsMBox = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.NAGS_MBOX_SFTP_USERNAME);

				final String passwordNagsMBox = Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.NAGS_MBOX_SFTP_PASSWORD);

				final int portNagsMBox = Integer.parseInt(Config.getParameter(Jnjb2bCoreConstants.DocumenTransfer.NAGS_MBOX_SFTP_PORT));

				session = jsch.getSession(userNameNagsMBox, hostNameNagsMBox, portNagsMBox);
				session.setPassword(passwordNagsMBox);
				session.setConfig(
						"PreferredAuthentications",
						"publickey,keyboard-interactive,password");
				LOG.debug(CONNECTIONS_DETAILS_HOST_NAME + hostNameNagsMBox + USER_NAME
						+ userNameNagsMBox + PORT + portNagsMBox + SECRET_KEY
						+ passwordNagsMBox);
			}

			final java.util.Properties config = new java.util.Properties();
			config.put(STRICT_HOST_KEY_CHECKING, "no");
			if(session!=null) {
				session.setConfig(config);
				session.connect();

				channel = session.openChannel("sftp");
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				channelSftp.cd(remoteFilePath);

				StringBuilder fileName = null;

				if (Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION_TYPE_NAGS_FILE_MBOX.equals(sftpConnectionType)) {
					fileName = new StringBuilder(channelSftp.pwd()).append(Jnjb2bCoreConstants.DocumenTransfer.DELIMITER).append(file.getName());
				} else {
					fileName = new StringBuilder(file.getName());
				}
				fileInputStream = new FileInputStream(file);
				channelSftp.put(fileInputStream,  fileName.toString());
				channelSftp.ls(channelSftp.pwd());
				sftpflag = true;
			}

		}
		catch (final Exception exception)
		{
			// As we are  using Jsch API ,  we are using general exception class.
			LOG.error(Jnjb2bCoreConstants.Logging.SFTP_UTIL + " - " + Jnjb2bCoreConstants.Logging.SFTP_UTIL_METHOD + "-"
					+ JnJCommonUtil.getCurrentDateTime() , exception);
		}
		finally{
			IOUtils.closeQuietly(fileInputStream);

		}
		return sftpflag;
	}
}
