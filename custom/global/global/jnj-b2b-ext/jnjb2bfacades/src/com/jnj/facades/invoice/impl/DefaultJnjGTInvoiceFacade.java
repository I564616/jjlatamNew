/**
 * 
 */
package com.jnj.facades.invoice.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.data.JnjGTInvoiceOrderData;
import com.jnj.facades.invoice.JnjGTInvoiceFacade;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.services.JnjGTInvoiceService;


/**
 * @author t.e.sharma
 * 
 */
public class DefaultJnjGTInvoiceFacade implements JnjGTInvoiceFacade
{
	@Autowired
	JnjGTInvoiceService jnjGTInvoiceService;

	private Converter<JnjGTInvoiceModel, JnjGTInvoiceOrderData> jnjGTInvoiceOrderDataConverter;

	@Override
	public List<JnjGTInvoiceOrderData> getInvoiceDetailsByCode(final String orderCode)
	{
		final List<JnjGTInvoiceModel> orderInvoices = jnjGTInvoiceService.getInvoiceDetailsByCode(orderCode);
		final List<JnjGTInvoiceOrderData> invoiceData = new ArrayList<>();
		for (final JnjGTInvoiceModel invoiceModel : orderInvoices)
		{
			invoiceData.add(getJnjGTInvoiceOrderDataConverter().convert(invoiceModel, new JnjGTInvoiceOrderData()));
		}
		return invoiceData;
	}

	@Override
	public JnjGTInvoiceOrderData getInvoiceDetailsByInvoiceNumber(final String invoiceNumber)
	{
		final JnjGTInvoiceModel invoiceModel = jnjGTInvoiceService.getInvoiceByInvoiceNum(invoiceNumber);
		final JnjGTInvoiceOrderData invoiceData = getJnjGTInvoiceOrderDataConverter().convert(invoiceModel,
				new JnjGTInvoiceOrderData());
		return invoiceData;
	}

	/**
	 * @return the jnjGTInvoiceOrderDataConverter
	 */
	public Converter<JnjGTInvoiceModel, JnjGTInvoiceOrderData> getJnjGTInvoiceOrderDataConverter()
	{
		return jnjGTInvoiceOrderDataConverter;
	}

	/**
	 * @param jnjGTInvoiceOrderDataConverter
	 *           the jnjGTInvoiceOrderDataConverter to set
	 */
	public void setJnjGTInvoiceOrderDataConverter(
			final Converter<JnjGTInvoiceModel, JnjGTInvoiceOrderData> jnjGTInvoiceOrderDataConverter)
	{
		this.jnjGTInvoiceOrderDataConverter = jnjGTInvoiceOrderDataConverter;
	}

	/**
	 * @return the jnjGTInvoiceDataService
	 */
	public JnjGTInvoiceService getJnjGTInvoiceDataService()
	{
		return jnjGTInvoiceService;
	}


	/**
	 * @param jnjGTInvoiceDataService
	 *           the jnjGTInvoiceDataService to set
	 */
	public void setJnjGTInvoiceDataService(final JnjGTInvoiceService jnjGTInvoiceDataService)
	{
		this.jnjGTInvoiceService = jnjGTInvoiceDataService;
	}

}
