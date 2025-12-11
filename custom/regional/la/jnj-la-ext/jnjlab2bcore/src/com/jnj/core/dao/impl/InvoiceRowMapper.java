package com.jnj.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jnj.core.dto.JnJInvoiceDTO;
import com.jnj.core.dto.JnJInvoiceHeaderDataDTO;
import com.jnj.core.dto.JnJInvoiceLineItemDataDTO;
import com.jnj.la.core.dto.JnJLaInvoiceHeaderDataDTO;


/**
 * This class RSA DB model to required DTO objects for hybris.
 *
 * @author Manoj.K.Panda
 *
 */
public class InvoiceRowMapper implements RowMapper<JnJInvoiceDTO>
{

	@Override
	public JnJInvoiceDTO mapRow(final ResultSet invoiceResults, final int rowNum) throws SQLException
	{

		final JnJInvoiceDTO invoiceDTO = new JnJInvoiceDTO();
		String invoiceDocNumber = null;
		JnJLaInvoiceHeaderDataDTO headerDTO = null;
		final List<JnJInvoiceHeaderDataDTO> headerDTOs = new ArrayList<>();
		JnJInvoiceLineItemDataDTO lineItem = null;
		List<JnJInvoiceLineItemDataDTO> lineItems = null;
		final List<JnJInvoiceLineItemDataDTO> allLineItems = new ArrayList<>();
		do
		{
			if (invoiceDocNumber == null || !invoiceDocNumber.equals(invoiceResults.getString(1)))
			{
				headerDTO = new JnJLaInvoiceHeaderDataDTO();
				invoiceDocNumber = invoiceResults.getString(1);
				lineItems = new ArrayList<>();
				headerDTO.setLineItemList(lineItems);
				headerDTO.setInvoiceDocNumber(invoiceDocNumber);
				populateInvoiceHeaderDTO(headerDTO, invoiceResults);
				headerDTOs.add(headerDTO);
			}

			lineItem = new JnJInvoiceLineItemDataDTO();
			populateInvoiceLineDTO(lineItem, invoiceResults);
			headerDTO.getLineItemList().add(lineItem);
			allLineItems.add(lineItem);
		}
		while (invoiceResults.next());

		invoiceDTO.setJnJInvoiceHeaderDataDTO(headerDTOs);
		invoiceDTO.setJnJInvoiceLineItemDataDTO(allLineItems);
		return invoiceDTO;
	}

	private void populateInvoiceHeaderDTO(final JnJLaInvoiceHeaderDataDTO headerDTO, final ResultSet invoiceResults)
			throws SQLException
	{
		headerDTO.setBillType(invoiceResults.getString(2));
		headerDTO.setNetValue(invoiceResults.getString(3));
		headerDTO.setPayer(invoiceResults.getString(4));
		headerDTO.setSoldTo(invoiceResults.getString(5));
		headerDTO.setPoNumber(invoiceResults.getString(6));
		headerDTO.setModel(invoiceResults.getString(7));
		headerDTO.setSeries(invoiceResults.getString(8));
		headerDTO.setNfNumber(invoiceResults.getString(9));
		headerDTO.setDocNumber(invoiceResults.getString(10));
		headerDTO.setCreationDate(invoiceResults.getString(11));
		headerDTO.setRegion(invoiceResults.getString(12));
		headerDTO.setNfYear(invoiceResults.getString(13));
		headerDTO.setNfMonth(invoiceResults.getString(14));
		headerDTO.setStcd(invoiceResults.getString(15));
		headerDTO.setCdv(invoiceResults.getString(16));
		headerDTO.setBillingDoc(invoiceResults.getString(17));
		headerDTO.setCancelledDocNo(invoiceResults.getString(18));
		headerDTO.setSalesOrder(invoiceResults.getString(25));
		headerDTO.setLastUpdateDate(invoiceResults.getString(26));
	}

	private void populateInvoiceLineDTO(final JnJInvoiceLineItemDataDTO lineItem, final ResultSet invoiceResults)
			throws SQLException
	{
		lineItem.setMaterial(invoiceResults.getString(19));
		lineItem.setItemNumber(invoiceResults.getString(20));
		lineItem.setQty(invoiceResults.getString(21));
		lineItem.setOrderReason(invoiceResults.getString(22));
		lineItem.setSalesOrderItemNo(invoiceResults.getString(23));
		lineItem.setLotNo(invoiceResults.getString(24));
	}

}
