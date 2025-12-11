/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;


/**
 * DTO class for Header and Line Item level object for Invoices.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnJInvoiceDTO
{
	private List<JnJInvoiceHeaderDataDTO> jnJInvoiceHeaderDataDTO;
	private List<JnJInvoiceLineItemDataDTO> jnJInvoiceLineItemDataDTO;

	public List<JnJInvoiceHeaderDataDTO> getJnJInvoiceHeaderDataDTO()
	{
		return jnJInvoiceHeaderDataDTO;
	}

	public void setJnJInvoiceHeaderDataDTO(final List<JnJInvoiceHeaderDataDTO> jnJInvoiceHeaderDataDTO)
	{
		this.jnJInvoiceHeaderDataDTO = jnJInvoiceHeaderDataDTO;
	}

	public List<JnJInvoiceLineItemDataDTO> getJnJInvoiceLineItemDataDTO()
	{
		return jnJInvoiceLineItemDataDTO;
	}

	public void setJnJInvoiceLineItemDataDTO(final List<JnJInvoiceLineItemDataDTO> jnJInvoiceLineItemDataDTO)
	{
		this.jnJInvoiceLineItemDataDTO = jnJInvoiceLineItemDataDTO;
	}

}
