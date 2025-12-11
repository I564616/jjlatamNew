/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.impex.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.type.TypeService;

import com.jnj.core.model.JnjSyncOrderImpExImportCronJobModel;
import com.jnj.core.model.JnjSyncOrderImpExImportJobModel;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSyncOrderImportCronJobInitDefaultsInterceptor implements InitDefaultsInterceptor
{

	@Override
	public void onInitDefaults(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
	{
		if (model instanceof JnjSyncOrderImpExImportCronJobModel)
		{
			final JnjSyncOrderImpExImportCronJobModel cronJobModel = (JnjSyncOrderImpExImportCronJobModel) model;
			final ComposedTypeModel comptypemodel = typeService.getComposedTypeForClass(model.getClass());
			final String jobcode = typeService.getAttributeDescriptor(comptypemodel, "job").getAttributeType().getCode();
			if (model.getClass().equals(JnjSyncOrderImpExImportCronJobModel.class)
					|| jobcode.equals("JnjSyncOrderImpExImportCronJob"))
			{
				JnjSyncOrderImpExImportJobModel job;

				try
				{
					job = (JnjSyncOrderImpExImportJobModel) cronJobService.getJob("Jnj-Sync-Order-ImpEx-Import");
				}
				catch (final UnknownIdentifierException _ex)
				{
					job = (JnjSyncOrderImpExImportJobModel) interceptorcontext.getModelService().create("JnjSyncOrderImpExImportJob");
					job.setCode("Jnj-Sync-Order-ImpEx-Import");
					interceptorcontext.getModelService().save(job);
				}
				cronJobModel.setJob(job);

			}
		}

	}

	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	public void setTypeService(final TypeService typeservice)
	{
		typeService = typeservice;
	}

	private CronJobService cronJobService;
	private TypeService typeService;
}
