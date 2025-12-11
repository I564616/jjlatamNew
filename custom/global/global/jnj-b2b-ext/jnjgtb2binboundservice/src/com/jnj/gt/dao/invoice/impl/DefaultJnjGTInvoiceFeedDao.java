/**
 *
 */
package com.jnj.gt.dao.invoice.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.gt.dao.invoice.JnjGTInvoiceFeedDao;
import com.jnj.gt.model.JnjGTIntInvoiceEntryLotModel;
import com.jnj.gt.model.JnjGTIntInvoiceEntryModel;
import com.jnj.gt.model.JnjGTIntInvoicePriceModel;


/**
 * @author abhishek.b.arora
 * 
 */
public class DefaultJnjGTInvoiceFeedDao implements JnjGTInvoiceFeedDao
{

	@Autowired
	FlexibleSearchService flexibleSearchService;
	private static final String AND_ITEM_CATEGORY_EQUAL = " AND {itemCategory} IN (?itemCategory)";


	@Override
	public List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(final String invoiceDocNum)
	{

		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntInvoiceEntryModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntInvoiceEntryModel.INVOICENUM).append("} = ?code");

		queryParams.put("code", invoiceDocNum);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTIntInvoiceEntryModel> result = flexibleSearchService
				.<JnjGTIntInvoiceEntryModel> search(flexibleSearchQuery).getResult();
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.invoice.JnjGTInvoiceDao#getInvoiceEntry(java.lang.String)
	 */
	@Override
	public List<JnjGTIntInvoiceEntryModel> getInvoiceEntry(final String invoiceDocNum, final List<String> itemCategory)
	{
		StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntInvoiceEntryModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntInvoiceEntryModel.INVOICENUM).append("} = ?code");
		if (CollectionUtils.isNotEmpty(itemCategory))
		{
			queryParams.put("itemCategory", itemCategory);
			searchQuery = searchQuery.append(AND_ITEM_CATEGORY_EQUAL);
		}

		queryParams.put("code", invoiceDocNum);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTIntInvoiceEntryModel> result = flexibleSearchService
				.<JnjGTIntInvoiceEntryModel> search(flexibleSearchQuery).getResult();
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.dao.invoice.JnjGTInvoiceDao#getInvoicePrice(java.lang.String)
	 */
	@Override
	public List<JnjGTIntInvoicePriceModel> getInvoicePrice(final String invoiceDocNum)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntInvoicePriceModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntInvoicePriceModel.INVOICENUM).append("} = ?code");

		queryParams.put("code", invoiceDocNum);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTIntInvoicePriceModel> result = flexibleSearchService
				.<JnjGTIntInvoicePriceModel> search(flexibleSearchQuery).getResult();
		return result;
	}

	@Override
	public List<JnjGTIntInvoiceEntryLotModel> getInvoiceEntryLot(final String invoiceDocNum, final String invoiceEntryNum)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();

		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntInvoiceEntryLotModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntInvoiceEntryLotModel.INVOICENUM).append("} = ?code and {")
				.append(JnjGTIntInvoiceEntryLotModel.LINEITEMNUM).append("} = ?entryCode");

		queryParams.put("code", invoiceDocNum);
		queryParams.put("entryCode", invoiceEntryNum);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjGTIntInvoiceEntryLotModel> result = flexibleSearchService.<JnjGTIntInvoiceEntryLotModel> search(
				flexibleSearchQuery).getResult();
		return result;
	}

}
