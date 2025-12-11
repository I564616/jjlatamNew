/**
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 **/
package com.jnj.la.core.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.springframework.jdbc.core.JdbcTemplate;
import com.jnj.core.connector.JNJRSADBConnector;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import com.jnj.la.core.dto.product.JnjRSAProductDTO;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.dto.JnjTranslationDTO;
import com.jnj.core.dao.impl.InvoiceRowMapper;
import com.jnj.la.core.daos.impl.DefaultJnjLaInvoiceDao;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.dao.order.impl.OrderRowMapper;
import com.jnj.core.dto.JnJInvoiceDTO;
import java.lang.Object;

public class JnJLaCronjobUtil {

	private static final Logger LOGGER = Logger.getLogger(JnJLaCronjobUtil.class);
	private static final String CONNECTION_TIMEOUT = "com.microsoft.sqlserver.jdbc.SQLServerException: Connection timed out";
	private static final String CONNECTION_RESET = "com.microsoft.sqlserver.jdbc.SQLServerException: Connection reset";	
	private static final String SFTP_EXCEPTION = "SFTP exception occured in ";
	private static final String DEST_DIRECTORY_TO_SFTP_CHANNEL = "destDirectory has been successfully set to sftpChannel!!!!!!!!!!!!!!";
	private static final String JSCH_EXCEPTION = "Jsch exception occured in ";
	private static final String SFTP_CHANNEL_CONNECTED = "sftpChannel has been successfully Connected!!!!!!!!!!!!!!";
	private static final String SESSION_CONNECTED = "session has been successfully Connected!!!!!!!!!!!!!!";
	private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
	private static final String SESSION_DISCONNECTED = "Session has been successfully Disconnected!!!!!!!!!!!!!!";	

	private static final Class<JnJLaCronjobUtil> CURRENTCLASS = JnJLaCronjobUtil.class;

	private JNJRSADBConnector rsaDBConnector;

	public JNJRSADBConnector getRsaDBConnector() {
		return rsaDBConnector;
	}

	public void setRsaDBConnector(final JNJRSADBConnector rsaDBConnector) {
		this.rsaDBConnector = rsaDBConnector;
	}

	public boolean getResultsFromDB(final String query, final Map<String, List<?>> dtoMap, final Class dtoClass) {
		final String methodName = "getresultsFromDB()";
		boolean reconnectDB = false;
		try {

			Date timeBeforeConnection = new Date();
			final JdbcTemplate jdbcTemplate = rsaDBConnector.getJdbcTemplate();
			Date timeAfterConnection = new Date();
			final long timeDifference = timeAfterConnection.getTime() - timeBeforeConnection.getTime();

			JnjGTCoreUtil.logInfoMessage("Time in getting connection", methodName,
					"Time difference in milli seconds " + timeDifference, JnJLaCronjobUtil.class);

			if (jdbcTemplate != null) {
				if (dtoClass.getSimpleName().equals(JnjRSAProductDTO.class.getSimpleName())) {

					final List<JnjRSAProductDTO> products = jdbcTemplate.query(query,
							new BeanPropertyRowMapper<JnjRSAProductDTO>(dtoClass));
					dtoMap.put(JnjRSAProductDTO.class.getSimpleName(), products);
					LOGGER.info("Product results from DB: " + products.size());
				} else if (dtoClass.getSimpleName().equals(JnjTranslationDTO.class.getSimpleName())) {
					final List<JnjTranslationDTO> translationRecords = jdbcTemplate.query(query,
							new BeanPropertyRowMapper<JnjTranslationDTO>(dtoClass));
					dtoMap.put(JnjTranslationDTO.class.getSimpleName(), translationRecords);
					LOGGER.info("Transaltion results from DB: " + translationRecords);
				} else if (dtoClass.getSimpleName().equals(JnJInvoiceDTO.class.getSimpleName())) {
					final JnJInvoiceDTO invoiceRecords = jdbcTemplate.queryForObject(query, new InvoiceRowMapper());
					final List<JnJInvoiceDTO> listOfInvoices = new ArrayList<>();
					listOfInvoices.add(invoiceRecords);
					dtoMap.put(JnJInvoiceDTO.class.getSimpleName(), listOfInvoices);
					LOGGER.info("Invoice results from DB: " + listOfInvoices);
				} else if (dtoClass.getSimpleName().equals(JnjDropshipmentDTO.class.getSimpleName())) {
					final List<JnjDropshipmentDTO> listOfDropshipment = jdbcTemplate.query(query,
							new BeanPropertyRowMapper<JnjDropshipmentDTO>(dtoClass));
					dtoMap.put(JnjDropshipmentDTO.class.getSimpleName(), listOfDropshipment);
					LOGGER.info("Dropshipment results from DB: " + listOfDropshipment);
				} else if (dtoClass.getSimpleName().equals(JnjOrderDTO.class.getSimpleName())) {
					final List<JnjOrderDTO> listOfOrders = jdbcTemplate.queryForObject(query, new OrderRowMapper());
					dtoMap.put(JnjOrderDTO.class.getSimpleName(), listOfOrders);
					LOGGER.info("Order results from DB: " + listOfOrders);
				} else {
					LOGGER.info("DTO class not found ############" + dtoClass.getSimpleName());
				}
			} else {
				LOGGER.info("Error while getting DB coneection: " + jdbcTemplate);
				reconnectDB = true;
			}
		} catch (final Exception e) {
			if (e.getMessage().contains(CONNECTION_TIMEOUT) || e.getMessage().contains(CONNECTION_RESET)) {
				JnjGTCoreUtil.logErrorMessage("Get results from RSA DB",methodName,
						"Can not connect to RSA due to SQLServerException: " + e.getMessage(), e, JnJLaCronjobUtil.class);
				reconnectDB = true;
			} else {

				JnjGTCoreUtil.logErrorMessage("Get results from RSA DB",methodName,
						"Can not connect to RSA view to perform query. Message:  " + e.getMessage(), e, JnJLaCronjobUtil.class);
				reconnectDB = false;
			}
		}
		return reconnectDB;
	}
	
	public static boolean writeFileToMBOX(final String HOSTNAME, final String USER_NAME, final String PASSWORD, final int PORT_NUMBER,
			final String srcDirectory, final String destDirectory, final boolean isCDLExport) throws IOException
	{
		boolean status=true;
		final String methodName = "writeFileToMBOX()";
		Session session = null;
		ChannelSftp sftpChannel = null;
		try
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(USER_NAME, HOSTNAME, PORT_NUMBER);
			session.setPassword(PASSWORD);		
			session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
			session.connect();
			LOGGER.debug(SESSION_CONNECTED);
			sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			LOGGER.debug(SFTP_CHANNEL_CONNECTED);
			LOGGER.debug(DEST_DIRECTORY_TO_SFTP_CHANNEL);
		}
		catch (final JSchException jschException)
		{
			LOGGER.error(JSCH_EXCEPTION+CURRENTCLASS+"-"+methodName+jschException.getMessage(), jschException);
		}
		try
		{
			if(sftpChannel!=null){
				File[] fileNameList = null;
				if(isCDLExport) {
					final File directory = new File(srcDirectory);
					fileNameList = directory.listFiles();
				}
				for(final File fileName:fileNameList) {
					sftpChannel.put(fileName.getAbsolutePath(),destDirectory);
					LOGGER.info("file-"+fileName+" from "+srcDirectory+" to "+destDirectory +" absolute path"+fileName.getAbsolutePath());
					Files.delete(fileName.toPath());
				}
			}
		}
		catch (final SftpException sftpException)
		{
			LOGGER.error(SFTP_EXCEPTION +CURRENTCLASS+"-"+methodName+ sftpException.getMessage(), sftpException);
			status=false;
		}
		finally {
			if(sftpChannel!=null){
				sftpChannel.disconnect();
			}
			if(session!=null){
				session.disconnect();
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(SESSION_DISCONNECTED);
			}
		}
		return status;
	}

}
