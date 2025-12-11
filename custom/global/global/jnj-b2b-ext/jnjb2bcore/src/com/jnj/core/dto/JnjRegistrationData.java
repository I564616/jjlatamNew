/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;

import com.jnj.facades.data.SecretQuestionData;


/**
 * TODO:<Ujjwal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjRegistrationData
{
	private JnjSectorTypeData jnjSectorTypeData;
	private JnjUserInfoData jnjUserInfoData;
	private JnjCompanyInfoData companyInfoData;
	private List<String> emailPreferences;
/*	5506*/
	private List<String> smsPreferences;
	private String backorderEmailType;
	//AAOL-3716
	private String shippedOrderEmailType;
	
	//AAOl-4100
	private String invoiceEmailPrefType;
	private String deliveryNoteEmailPrefType;
	
	
	
	
	public String getInvoiceEmailPrefType() {
		return invoiceEmailPrefType;
	}

	public void setInvoiceEmailPrefType(String invoiceEmailPrefType) {
		this.invoiceEmailPrefType = invoiceEmailPrefType;
	}

	public String getDeliveryNoteEmailPrefType() {
		return deliveryNoteEmailPrefType;
	}

	public void setDeliveryNoteEmailPrefType(String deliveryNoteEmailPrefType) {
		this.deliveryNoteEmailPrefType = deliveryNoteEmailPrefType;
	}

	public String getShippedOrderEmailType() {
		return shippedOrderEmailType;
	}

	public void setShippedOrderEmailType(String shippedOrderEmailType) {
		this.shippedOrderEmailType = shippedOrderEmailType;
	}

	private String password;
	private List<SecretQuestionData> secretQuestionsAnswers;
	
	/**
	 * @return the jnjSectorTypeData
	 */
	public JnjSectorTypeData getJnjSectorTypeData()
	{
		return jnjSectorTypeData;
	}

	/**
	 * @param jnjSectorTypeData
	 *           the jnjSectorTypeData to set
	 */
	public void setJnjSectorTypeData(final JnjSectorTypeData jnjSectorTypeData)
	{
		this.jnjSectorTypeData = jnjSectorTypeData;
	}

	/**
	 * @return the jnjUserInfoData
	 */
	public JnjUserInfoData getJnjUserInfoData()
	{
		return jnjUserInfoData;
	}

	/**
	 * @param jnjUserInfoData
	 *           the jnjUserInfoData to set
	 */
	public void setJnjUserInfoData(final JnjUserInfoData jnjUserInfoData)
	{
		this.jnjUserInfoData = jnjUserInfoData;
	}

	/**
	 * @return the companyInfoData
	 */
	public JnjCompanyInfoData getCompanyInfoData()
	{
		return companyInfoData;
	}

	/**
	 * @param companyInfoData
	 *           the companyInfoData to set
	 */
	public void setCompanyInfoData(final JnjCompanyInfoData companyInfoData)
	{
		this.companyInfoData = companyInfoData;
	}

	/**
	 * @return the emailPreferences
	 */
	public List<String> getEmailPreferences()
	{
		return emailPreferences;
	}

	/**
	 * @param emailPreferences
	 *           the emailPreferences to set
	 */
	public void setEmailPreferences(final List<String> emailPreferences)
	{
		this.emailPreferences = emailPreferences;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the secretQuestionsAnswers
	 */
	public List<SecretQuestionData> getSecretQuestionsAnswers()
	{
		return secretQuestionsAnswers;
	}

	/**
	 * @param list
	 *           the secretQuestionsAnswers to set
	 */
	public void setSecretQuestionsAnswers(final List<SecretQuestionData> list)
	{
		this.secretQuestionsAnswers = list;
	}

	/**
	 * @return the backorderEmailType
	 */
	public String getBackorderEmailType() {
		return backorderEmailType;
	}

	/**
	 * @param backorderEmailType the backorderEmailType to set
	 */
	public void setBackorderEmailType(String backorderEmailType) {
		this.backorderEmailType = backorderEmailType;
	}

	public List<String> getSmsPreferences() {
		return smsPreferences;
	}

	public void setSmsPreferences(List<String> smsPreferences) {
		this.smsPreferences = smsPreferences;
	}



}
