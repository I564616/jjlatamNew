/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.data;

public class JnjGTClassificationData
{

	private String classificationClass;
	private String featureName;
	private String featureValue;
	private String language;

	/**
	 * @return the classificationClass
	 */
	public String getClassificationClass()
	{
		return classificationClass;
	}

	/**
	 * @param classificationClass
	 *           the classificationClass to set
	 */
	public void setClassificationClass(final String classificationClass)
	{
		this.classificationClass = classificationClass;
	}

	/**
	 * @return the featureName
	 */
	public String getFeatureName()
	{
		return featureName;
	}

	/**
	 * @param featureName
	 *           the featureName to set
	 */
	public void setFeatureName(final String featureName)
	{
		this.featureName = featureName;
	}

	/**
	 * @return the featureValue
	 */
	public String getFeatureValue()
	{
		return featureValue;
	}

	/**
	 * @param featureValue
	 *           the featureValue to set
	 */
	public void setFeatureValue(final String featureValue)
	{
		this.featureValue = featureValue;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}
}
