/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.jnj.b2b.jnjglobalprofile.forms;

import java.util.List;

import com.jnj.facades.data.SecretQuestionData;






/**
 * Form object for updating the password.
 */
public class JnjGTSecretQuestionForm
{

	private List<SecretQuestionData> questionList;
	private String errorMsgForQuestion;

	private String errorMsgForAnswer;

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

	/**
	 * @return the errorMsgForQuestion
	 */
	public String getErrorMsgForQuestion()
	{
		return errorMsgForQuestion;
	}

	/**
	 * @param errorMsgForQuestion
	 *           the errorMsgForQuestion to set
	 */
	public void setErrorMsgForQuestion(final String errorMsgForQuestion)
	{
		this.errorMsgForQuestion = errorMsgForQuestion;
	}

	/**
	 * @return the errorMsgForAnswer
	 */
	public String getErrorMsgForAnswer()
	{
		return errorMsgForAnswer;
	}

	/**
	 * @param errorMsgForAnswer
	 *           the errorMsgForAnswer to set
	 */
	public void setErrorMsgForAnswer(final String errorMsgForAnswer)
	{
		this.errorMsgForAnswer = errorMsgForAnswer;
	}


}
