/**
 * 
 */
package com.jnj.mediaconversion.imagemagick;


import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;

import java.io.File;
import java.io.IOException;

import de.hybris.platform.mediaconversion.conversion.DefaultMediaConversionService;
import de.hybris.platform.mediaconversion.imagemagick.ImageMagickMediaConversionStrategy;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Amit.Mohan
 * 
 */
public class JnjImageMagickMediaConversionStrategy extends ImageMagickMediaConversionStrategy
{

	@Autowired
	private DefaultMediaConversionService mediaConversionService;

	public DefaultMediaConversionService getMediaConversionService() {
		return mediaConversionService;
	}

	public void convertMedia(final File input, final File target, final ConversionMediaFormatModel format) throws IOException
	{
		super.convert(mediaConversionService, input, target, format);

	}
}
