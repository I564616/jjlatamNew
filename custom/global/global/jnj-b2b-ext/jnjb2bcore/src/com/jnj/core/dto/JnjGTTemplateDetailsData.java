/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * This Class is used to save the edited template as send by the user.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTTemplateDetailsData
{
	private String templateNumber;
	private boolean shareWithAccountUsers;
	private String templateName;
	private String productCode;
	private String quantity;

	/**
	 * @return the templateNumber
	 */
	public String getTemplateNumber()
	{
		return templateNumber;
	}

	/**
	 * @param templateNumber
	 *           the templateNumber to set
	 */
	public void setTemplateNumber(final String templateNumber)
	{
		this.templateNumber = templateNumber;
	}

	/**
	 * @return the shareWithAccountUsers
	 */
	public boolean isShareWithAccountUsers()
	{
		return shareWithAccountUsers;
	}

	/**
	 * @param shareWithAccountUsers
	 *           the shareWithAccountUsers to set
	 */
	public void setShareWithAccountUsers(final boolean shareWithAccountUsers)
	{
		this.shareWithAccountUsers = shareWithAccountUsers;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName()
	{
		return templateName;
	}

	/**
	 * @param templateName
	 *           the templateName to set
	 */
	public void setTemplateName(final String templateName)
	{
		this.templateName = templateName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
