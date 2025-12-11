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
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */


public class JnjRegisterData implements java.io.Serializable
{
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.titleCode</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String titleCode;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.state</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String state;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.city</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String city;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretQ4</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretQ4;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretAns1</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretAns1;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretQ3</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretQ3;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretQ2</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretQ2;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretQ1</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretQ1;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretAns4</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretAns4;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretAns5</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretAns5;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretAns2</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretAns2;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretQ6</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretQ6;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretAns3</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretAns3;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.zipCode</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String zipCode;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretQ5</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretQ5;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.login</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String login;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.checkPwd</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String checkPwd;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.firstName</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String firstName;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.chkEmail</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String chkEmail;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.lastName</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String lastName;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.secretAns6</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String secretAns6;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.emailNotification</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String emailNotification;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.country</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String country;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.phoneNo</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String phoneNo;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.pwd</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String pwd;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.email</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String email;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.address</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String address;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.custName</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String custName;
	/**
	 * <i>Generated property</i> for <code>JnJRegisterData.mobileNo</code> property defined at extension
	 * <code>JnJB2bAcceleratorstorefront</code>.
	 */
	private String mobileNo;

	private List<SecretQuestionData> questionList;


	/**
	 * @return the questionList
	 */
	public List<SecretQuestionData> getQuestionList()
	{
		return questionList;
	}


	/**
	 * @param questionList
	 *           the questionList to set
	 */
	public void setQuestionList(final List<SecretQuestionData> questionList)
	{
		this.questionList = questionList;
	}


	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}


	public String getTitleCode()
	{
		return titleCode;
	}


	public void setState(final String state)
	{
		this.state = state;
	}


	public String getState()
	{
		return state;
	}


	public void setCity(final String city)
	{
		this.city = city;
	}


	public String getCity()
	{
		return city;
	}


	public void setSecretQ4(final String secretQ4)
	{
		this.secretQ4 = secretQ4;
	}


	public String getSecretQ4()
	{
		return secretQ4;
	}


	public void setSecretAns1(final String secretAns1)
	{
		this.secretAns1 = secretAns1;
	}


	public String getSecretAns1()
	{
		return secretAns1;
	}


	public void setSecretQ3(final String secretQ3)
	{
		this.secretQ3 = secretQ3;
	}


	public String getSecretQ3()
	{
		return secretQ3;
	}


	public void setSecretQ2(final String secretQ2)
	{
		this.secretQ2 = secretQ2;
	}


	public String getSecretQ2()
	{
		return secretQ2;
	}


	public void setSecretQ1(final String secretQ1)
	{
		this.secretQ1 = secretQ1;
	}


	public String getSecretQ1()
	{
		return secretQ1;
	}


	public void setSecretAns4(final String secretAns4)
	{
		this.secretAns4 = secretAns4;
	}


	public String getSecretAns4()
	{
		return secretAns4;
	}


	public void setSecretAns5(final String secretAns5)
	{
		this.secretAns5 = secretAns5;
	}


	public String getSecretAns5()
	{
		return secretAns5;
	}


	public void setSecretAns2(final String secretAns2)
	{
		this.secretAns2 = secretAns2;
	}


	public String getSecretAns2()
	{
		return secretAns2;
	}


	public void setSecretQ6(final String secretQ6)
	{
		this.secretQ6 = secretQ6;
	}


	public String getSecretQ6()
	{
		return secretQ6;
	}


	public void setSecretAns3(final String secretAns3)
	{
		this.secretAns3 = secretAns3;
	}


	public String getSecretAns3()
	{
		return secretAns3;
	}


	public void setZipCode(final String zipCode)
	{
		this.zipCode = zipCode;
	}


	public String getZipCode()
	{
		return zipCode;
	}


	public void setSecretQ5(final String secretQ5)
	{
		this.secretQ5 = secretQ5;
	}


	public String getSecretQ5()
	{
		return secretQ5;
	}


	public void setLogin(final String login)
	{
		this.login = login;
	}


	public String getLogin()
	{
		return login;
	}


	public void setCheckPwd(final String checkPwd)
	{
		this.checkPwd = checkPwd;
	}


	public String getCheckPwd()
	{
		return checkPwd;
	}


	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}


	public String getFirstName()
	{
		return firstName;
	}


	public void setChkEmail(final String chkEmail)
	{
		this.chkEmail = chkEmail;
	}


	public String getChkEmail()
	{
		return chkEmail;
	}


	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}


	public String getLastName()
	{
		return lastName;
	}


	public void setSecretAns6(final String secretAns6)
	{
		this.secretAns6 = secretAns6;
	}


	public String getSecretAns6()
	{
		return secretAns6;
	}

	public void setEmailNotification(final String emailNotification)
	{
		this.emailNotification = emailNotification;
	}

	public String getEmailNotification()
	{
		return emailNotification;
	}

	public void setCountry(final String country)
	{
		this.country = country;
	}

	public String getCountry()
	{
		return country;
	}

	public void setPhoneNo(final String phoneNo)
	{
		this.phoneNo = phoneNo;
	}


	public String getPhoneNo()
	{
		return phoneNo;
	}


	public void setPwd(final String pwd)
	{
		this.pwd = pwd;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getEmail()
	{
		return email;
	}

	public void setAddress(final String address)
	{
		this.address = address;
	}

	public String getAddress()
	{
		return address;
	}

	public void setCustName(final String custName)
	{
		this.custName = custName;
	}


	public String getCustName()
	{
		return custName;
	}

	public void setMobileNo(final String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public String getMobileNo()
	{
		return mobileNo;

	}

}