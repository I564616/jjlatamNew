/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalresources.form;

import java.util.List;

import com.jnj.b2b.jnjglobalresources.form.JnjGTUserSearchForm;
import com.jnj.b2b.storefront.forms.B2BCustomerForm;

import de.hybris.platform.commercefacades.storesession.data.LanguageData;
/**
 * YTODO <<Replace this line with the class description>>
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTB2BCustomerForm extends B2BCustomerForm
{

	private String supervisorName;
	private String supervisorEmail;
	private String supervisorPhone;
	private String phone;
	private String accounts;
	private boolean mdd;
	private boolean consumer;
	private boolean pharma;
	private String wwid;
	private String division;
	private String role;
	private String phoneNumberPrefix;
	private String supervisorPhonePrefix;
	private String groups;
	private JnjGTUserSearchForm searchForm;
	private JnjGTAddressForm contactAddress;
	private String status;
	private String passwordChangeDate;
	private String passwordExpiryDate;
	private String csrNotes;
	private boolean noCharge;
	private String existingStatus;
	private String orgName;
	private String department;
	private String radio1;
	private String franchiseDivisions;
	private String territories;
	private List<String> territorDivsion;
	private String allowedFranchise;
	private String language;
	private boolean consignmentEntryOrder; //AAOL-3112
	private boolean financialEnable; //AAOL-2422
	public boolean isConsignmentEntryOrder() {
		return consignmentEntryOrder;
	}

	public void setConsignmentEntryOrder(boolean consignmentEntryOrder) {
		this.consignmentEntryOrder = consignmentEntryOrder;
	}

	/**
	 * @return the franchiseDivisions
	 */
	public String getFranchiseDivisions()
	{
		return franchiseDivisions;
	}

	/**
	 * @return the territorDivsion
	 */
	public List<String> getTerritorDivsion()
	{
		return territorDivsion;
	}

	/**
	 * @param territorDivsion
	 *           the territorDivsion to set
	 */
	public void setTerritorDivsion(final List<String> territorDivsion)
	{
		this.territorDivsion = territorDivsion;
	}

	/**
	 * @param franchiseDivisions
	 *           the franchiseDivisions to set
	 */
	public void setFranchiseDivisions(final String franchiseDivisions)
	{
		this.franchiseDivisions = franchiseDivisions;
	}

	/**
	 * @return the territories
	 */
	public String getTerritories()
	{
		return territories;
	}

	/**
	 * @param territories
	 *           the territories to set
	 */
	public void setTerritories(final String territories)
	{
		this.territories = territories;
	}

	/**
	 * @return the radio1
	 */
	public String getRadio1()
	{
		return radio1;
	}

	/**
	 * @param radio1
	 *           the radio1 to set
	 */
	public void setRadio1(final String radio1)
	{
		this.radio1 = radio1;
	}


	/**
	 * @return the orgName
	 */
	public String getOrgName()
	{
		return orgName;
	}

	/**
	 * @param orgName
	 *           the orgName to set
	 */
	public void setOrgName(final String orgName)
	{
		this.orgName = orgName;
	}

	/**
	 * @return the department
	 */
	public String getDepartment()
	{
		return department;
	}

	/**
	 * @param department
	 *           the department to set
	 */
	public void setDepartment(final String department)
	{
		this.department = department;
	}

	/**
	 * @return the existingStatus
	 */
	public String getExistingStatus()
	{
		return existingStatus;
	}

	/**
	 * @param existingStatus
	 *           the existingStatus to set
	 */
	public void setExistingStatus(final String existingStatus)
	{
		this.existingStatus = existingStatus;
	}

	/**
	 * @return the noCharge
	 */
	public boolean isNoCharge()
	{
		return noCharge;
	}

	/**
	 * @param noCharge
	 *           the noCharge to set
	 */
	public void setNoCharge(final boolean noCharge)
	{
		this.noCharge = noCharge;
	}

	/**
	 * @return the csrNotes
	 */
	public String getCsrNotes()
	{
		return csrNotes;
	}

	/**
	 * @param csrNotes
	 *           the csrNotes to set
	 */
	public void setCsrNotes(final String csrNotes)
	{
		this.csrNotes = csrNotes;
	}

	/**
	 * @return the passwordChangeDate
	 */
	public String getPasswordChangeDate()
	{
		return passwordChangeDate;
	}

	/**
	 * @param passwordChangeDate
	 *           the passwordChangeDate to set
	 */
	public void setPasswordChangeDate(final String passwordChangeDate)
	{
		this.passwordChangeDate = passwordChangeDate;
	}

	/**
	 * @return the passwordExpiryDate
	 */
	public String getPasswordExpiryDate()
	{
		return passwordExpiryDate;
	}

	/**
	 * @param passwordExpiryDate
	 *           the passwordExpiryDate to set
	 */
	public void setPasswordExpiryDate(final String passwordExpiryDate)
	{
		this.passwordExpiryDate = passwordExpiryDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the contactAddress
	 */
	public JnjGTAddressForm getContactAddress()
	{
		return contactAddress;
	}

	/**
	 * @param contactAddress
	 *           the contactAddress to set
	 */
	public void setContactAddress(final JnjGTAddressForm contactAddress)
	{
		this.contactAddress = contactAddress;
	}

	/**
	 * @return the groups
	 */
	public String getGroups()
	{
		return groups;
	}

	/**
	 * @return the searchForm
	 */
	public JnjGTUserSearchForm getSearchForm()
	{
		return searchForm;
	}

	/**
	 * @param searchForm
	 *           the searchForm to set
	 */
	public void setSearchForm(final JnjGTUserSearchForm searchForm)
	{
		this.searchForm = searchForm;
	}

	/**
	 * @param groups
	 *           the groups to set
	 */
	public void setGroups(final String groups)
	{
		this.groups = groups;
	}

	/**
	 * @return the supervisorName
	 */
	public String getSupervisorName()
	{
		return supervisorName;
	}

	/**
	 * @return the phoneNumberPrefix
	 */
	public String getPhoneNumberPrefix()
	{
		return phoneNumberPrefix;
	}

	/**
	 * @param phoneNumberPrefix
	 *           the phoneNumberPrefix to set
	 */
	public void setPhoneNumberPrefix(final String phoneNumberPrefix)
	{
		this.phoneNumberPrefix = phoneNumberPrefix;
	}

	/**
	 * @return the supervisorPhonePrefix
	 */
	public String getSupervisorPhonePrefix()
	{
		return supervisorPhonePrefix;
	}

	/**
	 * @param supervisorPhonePrefix
	 *           the supervisorPhonePrefix to set
	 */
	public void setSupervisorPhonePrefix(final String supervisorPhonePrefix)
	{
		this.supervisorPhonePrefix = supervisorPhonePrefix;
	}

	/**
	 * @param supervisorName
	 *           the supervisorName to set
	 */
	public void setSupervisorName(final String supervisorName)
	{
		this.supervisorName = supervisorName;
	}

	/**
	 * @return the supervisorEmail
	 */
	public String getSupervisorEmail()
	{
		return supervisorEmail;
	}

	/**
	 * @param supervisorEmail
	 *           the supervisorEmail to set
	 */
	public void setSupervisorEmail(final String supervisorEmail)
	{
		this.supervisorEmail = supervisorEmail;
	}

	/**
	 * @return the supervisorPhone
	 */
	public String getSupervisorPhone()
	{
		return supervisorPhone;
	}

	/**
	 * @param supervisorPhone
	 *           the supervisorPhone to set
	 */
	public void setSupervisorPhone(final String supervisorPhone)
	{
		this.supervisorPhone = supervisorPhone;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the accounts
	 */
	public String getAccounts()
	{
		return accounts;
	}

	/**
	 * @param accounts
	 *           the accounts to set
	 */
	public void setAccounts(final String accounts)
	{
		this.accounts = accounts;
	}

	/**
	 * @return the mdd
	 */
	public boolean isMdd()
	{
		return mdd;
	}

	/**
	 * @param mdd
	 *           the mdd to set
	 */
	public void setMdd(final boolean mdd)
	{
		this.mdd = mdd;
	}

	/**
	 * @return the consumer
	 */
	public boolean isConsumer()
	{
		return consumer;
	}

	/**
	 * @param consumer
	 *           the consumer to set
	 */
	public void setConsumer(final boolean consumer)
	{
		this.consumer = consumer;
	}

	/**
	 * @return the wwid
	 */
	public String getWwid()
	{
		return wwid;
	}

	/**
	 * @param wwid
	 *           the wwid to set
	 */
	public void setWwid(final String wwid)
	{
		this.wwid = wwid;
	}

	/**
	 * @return the division
	 */
	public String getDivision()
	{
		return division;
	}

	/**
	 * @param division
	 *           the division to set
	 */
	public void setDivision(final String division)
	{
		this.division = division;
	}

	/**
	 * @return the role
	 */
	public String getRole()
	{
		return role;
	}

	/**
	 * @param role
	 *           the role to set
	 */
	public void setRole(final String role)
	{
		this.role = role;
	}

	public boolean isPharma() {
		return pharma;
	}

	public void setPharma(boolean pharma) {
		this.pharma = pharma;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isFinancialEnable() {
		return financialEnable;
	}

	public void setFinancialEnable(boolean financialEnable) {
		this.financialEnable = financialEnable;
	}

	public String getAllowedFranchise() {
		return allowedFranchise;
	}

	public void setAllowedFranchise(String allowedFranchise) {
		this.allowedFranchise = allowedFranchise;
	}
}
