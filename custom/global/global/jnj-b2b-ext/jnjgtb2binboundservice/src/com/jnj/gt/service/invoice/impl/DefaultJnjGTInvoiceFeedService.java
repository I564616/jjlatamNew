/**
 *
 */
package com.jnj.gt.service.invoice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.gt.dao.invoice.JnjGTInvoiceFeedDao;
import com.jnj.gt.model.JnjGTIntInvoiceEntryLotModel;
import com.jnj.gt.model.JnjGTIntInvoiceEntryModel;
import com.jnj.gt.model.JnjGTIntInvoicePriceModel;
import com.jnj.gt.service.invoice.JnjGTInvoiceFeedService;


/**
 * @author abhishek.b.arora
 * 
 */
public class DefaultJnjGTInvoiceFeedService implements JnjGTInvoiceFeedService
{
	@Autowired
	JnjGTInvoiceFeedDao jnjGTInvoiceDao;

	@Override
	public List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(final String invoiceDocNum)
	{
		return jnjGTInvoiceDao.getInvoiceEntry(invoiceDocNum);
	}

	@Override
	public List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(final String invoiceDocNum, final List<String> itemCategory)
	{
		return jnjGTInvoiceDao.getInvoiceEntry(invoiceDocNum, itemCategory);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.invoice.JnjGTInvoiceService#getInvoiceEntryLot(java.lang.String)
	 */
	@Override
	public List<JnjGTIntInvoiceEntryLotModel> getInvoiceEntryLot(final String invoiceDocNum, final String invoiceEntryNum)
	{
		// YTODO Auto-generated method stub
		return jnjGTInvoiceDao.getInvoiceEntryLot(invoiceDocNum, invoiceEntryNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.invoice.JnjGTInvoiceService#getInvoicePrice(java.lang.String)
	 */
	@Override
	public List<JnjGTIntInvoicePriceModel> getInvoicePrice(final String invoiceDocNum)
	{
		// YTODO Auto-generated method stub
		return jnjGTInvoiceDao.getInvoicePrice(invoiceDocNum);
	}
}
