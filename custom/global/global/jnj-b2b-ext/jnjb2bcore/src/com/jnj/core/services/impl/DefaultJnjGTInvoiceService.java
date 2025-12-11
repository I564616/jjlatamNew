/**
 * 
 */
package com.jnj.core.services.impl;

import java.util.List;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.invoice.JnjGTInvoiceDao;
import com.jnj.core.model.JnjGTInvoiceEntryLotModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTInvoicePriceModel;
import com.jnj.core.services.JnjGTInvoiceService;


/**
 * @author t.e.sharma
 * 
 */
public class DefaultJnjGTInvoiceService implements JnjGTInvoiceService
{

	@Resource(name = "invoiceDao")
	JnjGTInvoiceDao jnjGTInvoiceDao;

	/**
	 * @return the jnjGTInvoiceDao
	 */
	public JnjGTInvoiceDao getJnjGTInvoiceDao() {
		return jnjGTInvoiceDao;
	}


	/**
	 * @param jnjGTInvoiceDao the jnjGTInvoiceDao to set
	 */
	public void setJnjGTInvoiceDao(JnjGTInvoiceDao jnjGTInvoiceDao) {
		this.jnjGTInvoiceDao = jnjGTInvoiceDao;
	}

	@Override
	public List<JnjGTInvoiceModel> getInvoiceDetailsByCode(final String orderCode)
	{

		return getJnjGTInvoiceDao().getInvoiceDetailsByCode(orderCode);

	}



	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public JnjGTInvoiceModel getInvoiceByInvoiceNum(final String invDocNo)
	{

		return getJnjGTInvoiceDao().getInvoiceByInvoiceNum(invDocNo);
	}


	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public JnjGTInvoiceEntryModel getInvoiceEntryByInvoiceNumAndLineItemNum(final String invoiceNum, final String lineNum)
	{

		return getJnjGTInvoiceDao().getInvoiceEntryByInvoiceNumAdLineItemNum(invoiceNum, lineNum);
	}


	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public JnjGTInvoiceEntryLotModel getInvoiceEntryLotByInvNumLineItemAndLotNum(final String invoiceDocNum,
			final String invoiceEntryNum, final String lotNum)
	{

		return getJnjGTInvoiceDao().getInvoiceEntryLotByInvNumLineItemAndLotNum(invoiceDocNum, invoiceEntryNum, lotNum);
	}


	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public List<JnjGTInvoicePriceModel> getInvoicePricesByInvoiceNum(final String invoiceDocNum)
	{

		return getJnjGTInvoiceDao().getInvoicePricesByInvoiceNum(invoiceDocNum);
	}


}
