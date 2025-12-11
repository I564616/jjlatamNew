/**
 *
 */
package com.jnj.gt.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnJProductModel;



/**
 * Cronjob responsible for creating the excel file for MDD product catalog.
 * 
 */
public class JnjGTMDDExportCatalogJob extends AbstractJobPerformable<CronJobModel>
{


	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOG = Logger.getLogger(JnjGTOrderShipEmailNotificationJob.class);

	/**
	 * The private Instance of <code>JnjGTOrderFeedService</code>.
	 */
	@Resource(name = "productService")
	JnJGTProductService jnjGTProductService;

	@Autowired
	private Converter<JnJProductModel, JnjGTProductData> productConverter;

	public Converter<JnJProductModel, JnjGTProductData> getProductConverter()
	{
		return productConverter;
	}

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		final DateFormat df1 = new SimpleDateFormat("ddMMMyy_HHmmss");
		df1.setTimeZone(TimeZone.getTimeZone("EST"));
		final String filepath = Config.getParameter(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_PATH_KEY);
		final List<JnJProductModel> catalogproducts = jnjGTProductService.getProductsForCategory(Jnjb2bCoreConstants.MDD, null);

		LOG.info("Total number of products models found for MDD  are " + catalogproducts.size());
		final String fileName = "MDD_Product_Catalog" + "-" + df1.format(new Date()) + ".xls";
		jnjGTProductService.createMDDExportFile(catalogproducts, fileName);
		jnjGTProductService.deleteOldMDDExportFile(filepath);
		return result;
	}
}
