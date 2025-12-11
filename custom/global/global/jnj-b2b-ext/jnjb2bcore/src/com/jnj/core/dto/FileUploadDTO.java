/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;


/**
 * This is the data class for storing all attributes obtained from the form for Sell Out Reports.
 * 
 * @author Accenture
 * @version 1.0
 */
public class FileUploadDTO
{

	protected String name = null;
	protected MultipartFile file = null;
	protected Date date;
	protected String hour;
	protected String renameFileTo;
	protected String fileExtension;
	protected String filePathInShareFolder;
	protected String fileNameInShareFolder;
	protected String fileDirInShareFolder;
	protected String remoteFilePath;

	public String getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(final String fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	public String getName()
	{
		return name;
	}

	public String setName(final String name)
	{
		return this.name = name;
	}

	public MultipartFile getFile()
	{
		return file;
	}

	public void setFile(final MultipartFile file)
	{
		this.file = file;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(final Date date)
	{
		this.date = date;
	}

	public String getHour()
	{
		return hour;
	}

	public void setHour(final String hour)
	{
		this.hour = hour;
	}

	/**
	 * @return the renameFileTo
	 */
	public String getRenameFileTo()
	{
		return renameFileTo;
	}

	public String getFilePathInShareFolder()
	{
		return filePathInShareFolder;
	}

	public void setFilePathInShareFolder(final String filePathInShareFolder)
	{
		this.filePathInShareFolder = filePathInShareFolder;
	}

	/**
	 * @param renameFileTo
	 *           the renameFileTo to set
	 */
	public void setRenameFileTo(final String renameFileTo)
	{
		this.renameFileTo = renameFileTo;
	}

	public String getFileNameInShareFolder()
	{
		return fileNameInShareFolder;
	}

	public void setFileNameInShareFolder(final String fileNameInShareFolder)
	{
		this.fileNameInShareFolder = fileNameInShareFolder;
	}

	public String getFileDirInShareFolder()
	{
		return fileDirInShareFolder;
	}

	public void setFileDirInShareFolder(final String fileDirInShareFolder)
	{
		this.fileDirInShareFolder = fileDirInShareFolder;
	}

	public String getRemoteFilePath()
	{
		return remoteFilePath;
	}

	public void setRemoteFilePath(final String remoteFilePath)
	{
		this.remoteFilePath = remoteFilePath;
	}

}
