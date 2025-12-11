package com.jnj.facades.services;

import com.jnj.core.dto.JnjConsignmentIssueDTO;
import com.jnj.core.dto.JnjFormDTO;
import com.jnj.core.model.JnJB2bCustomerModel;


public interface JnjServicesFacade
{

	void sendEmailToUser(final JnjFormDTO dto);

	String fetchRecipientEmails(String formName, JnJB2bCustomerModel customer, String company, String bid);

	/**
	 * The method is used for sending an email on requesting for an Indirect Payer.
	 *
	 * @param dto
	 */
	void sendEmailToUserForIndirectPayer(JnjFormDTO dto);

	public JnjConsignmentIssueDTO setInfoForConsignmentForm(final JnjConsignmentIssueDTO jnjConsignmentIssueDTO);
}
