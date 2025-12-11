/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.data;

public class JnjGTProductImageResponse
{

	protected String code;
	protected String qualifier;
	protected String url;
	protected String imageFileFormat;
	protected String imageType;
	protected String upcCode;
	protected String imageSize;
	protected String fileName;
	protected String imageLastUpdDate;
	protected String mediaCode;


	public String getUpcCode()
	{
		return upcCode;
	}

	public void setUpcCode(final String upcCode)
	{
		this.upcCode = upcCode;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getQualifier()
	{
		return qualifier;
	}

	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getImageFileFormat()
	{
		return imageFileFormat;
	}

	public void setImageFileFormat(final String imageFileFormat)
	{
		this.imageFileFormat = imageFileFormat;
	}

	public String getImageType()
	{
		return imageType;
	}

	public void setImageType(final String imageType)
	{
		this.imageType = imageType;
	}

	public String getImageSize()
	{
		return imageSize;
	}

	public void setImageSize(final String imageSize)
	{
		this.imageSize = imageSize;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	public String getImageLastUpdDate()
	{
		return imageLastUpdDate;
	}

	public void setImageLastUpdDate(final String imageLastUpdDate)
	{
		this.imageLastUpdDate = imageLastUpdDate;
	}

	public String getMediaCode()
	{
		return mediaCode;
	}

	public void setMediaCode(final String mediaCode)
	{
		this.mediaCode = mediaCode;
	}

}
