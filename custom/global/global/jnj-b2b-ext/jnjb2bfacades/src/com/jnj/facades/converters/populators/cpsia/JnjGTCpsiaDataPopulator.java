package com.jnj.facades.converters.populators.cpsia;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;
import com.jnj.facades.data.JnjGTCpsiaData;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.model.JnjGTProductCpscDetailModel;


public class JnjGTCpsiaDataPopulator implements Populator<JnjGTProductCpscDetailModel, JnjGTCpsiaData>
{
	private static final String DATE_FORMAT = "MM/dd/yyyy";

	@Resource(name = "productService")
	private JnJGTProductService jnjProductService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	/**
	 * Populates data attributes of JnjGTInvoiceOrderData from the source instance of JnjGTInvoiceModel.
	 */
	@Override
	public void populate(final JnjGTProductCpscDetailModel source, final JnjGTCpsiaData target) throws ConversionException
	{
		target.setProductCode(source.getProductCode());

		// Fetching the product model to set product name
		if (StringUtils.isNotEmpty(source.getProductCode()))
		{
			final JnJProductModel product = jnjProductService.getProductModelByCode(source.getProductCode(), null);
			if (product != null)
			{
				target.setProductName(StringUtils.isEmpty(product.getName()) ? ((JnJProductModel) product).getMdmDescription()
						: product.getName());
				target.setDescription(product.getDescription());
			}
		}

		target.setLotNumber(source.getLotNumber());
		target.setOutercase(source.getOutercase());
		target.setUpc(source.getUpcCode());
		if (null != source.getModifiedDate())
		{
			final DateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			target.setModifiedDate(dateFormatter.format(source.getModifiedDate()));
		}
	}
}
