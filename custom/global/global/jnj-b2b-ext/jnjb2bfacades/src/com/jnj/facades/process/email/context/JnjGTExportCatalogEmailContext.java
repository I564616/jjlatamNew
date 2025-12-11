/**
 *
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontProcessModel;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnjGTExportCatalogEmailProcessModel;
import com.jnj.core.model.JnJProductModel;


/**
 * This class is used to set the context for the email to send export catalog data to user.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTExportCatalogEmailContext extends AbstractEmailContext<StoreFrontProcessModel>
{
	/**  */
	protected static final String B2B_UNIT = "b2bUnit";
	protected static final Logger LOG = Logger.getLogger(JnjGTExportCatalogEmailContext.class);
	protected static final String TO_EMAIL = "toEmail";
	protected static final String FROM_EMAIL_ADDRESS = "fromEmail";
	protected static final String FIRST_NAME = "firstName";
	public static final String TO_DISPLAY_NAME = "toDisplayName";
	protected Map<String, String> exportCatalogData = new HashMap<String, String>();
	@Autowired
	private Converter<JnJProductModel, JnjGTProductData> productConverter;

	/** The jnj na product service. */
	@Resource(name = "productService")
	protected JnJGTProductService jnjGTProductService;

	/** The session service. */
	@Autowired
	protected SessionService sessionService;

	public Converter<JnJProductModel, JnjGTProductData> getProductConverter()
	{
		return productConverter;
	}

	/**
	 * @param exportCatalogData
	 *           the exportCatalogData to set
	 */
	public void setExportCatalogData(final Map<String, String> exportCatalogData)
	{
		this.exportCatalogData = exportCatalogData;
	}

	@Override
	public void init(final StoreFrontProcessModel storeFrontProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_NAME = "init()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD);
		super.init(storeFrontProcessModel, emailPageModel);
		JnjGTExportCatalogEmailProcessModel jnjGTExportCatalogProcessModel = null;
		if (storeFrontProcessModel instanceof JnjGTExportCatalogEmailProcessModel)
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
					"Process Model is an instance of JnjGTExportCatalogEmailProcessModel");

			/** Type-casting the storeFrontCustomerProcessModel to JnjGTExportCatalogEmailProcessModel **/
			jnjGTExportCatalogProcessModel = (JnjGTExportCatalogEmailProcessModel) storeFrontProcessModel;
			//Setting the current site and the b2bunit in the session to use during the conversion of products.
			sessionService.setAttribute(Jnjb2bCoreConstants.SITE_NAME, jnjGTExportCatalogProcessModel.getCurrentSite());
			sessionService.setAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT, jnjGTExportCatalogProcessModel.getCurrentB2bUnit());
			/** Setting the Amazon Upload data map in the context **/
			logDebugMessage(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
					"Setting the Export Catalog data map in the context...");
			setExportCatalogData(new HashMap<String, String>(jnjGTExportCatalogProcessModel.getExportCatalogData()));
			createExcelFile(jnjGTExportCatalogProcessModel, exportCatalogData);
			logDebugMessage(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME, "Export Catalog data has been set!");

			/** Setting the To Email address **/
			put(EMAIL, getExportCatalogData().get(TO_EMAIL));
			put(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH, Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_PATH_KEY);
			put(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME,
					getExportCatalogData().get(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME));
			put(Jnjb2bCoreConstants.DELETE_FILE, getExportCatalogData().get(Jnjb2bCoreConstants.DELETE_FILE));
			LOG.info("Attachment Deletion flag:" + getExportCatalogData().get(Jnjb2bCoreConstants.DELETE_FILE));
			/** Setting the From Email address **/
			put(FROM_EMAIL, JnJCommonUtil.getValue(Jnjb2bCoreConstants.EXPORT_EMAIL_FROM));
			/** Setting the User display name **/
			put(FROM_DISPLAY_NAME, JnJCommonUtil.getValue(Jnjb2bCoreConstants.EXPORT_EMAIL_FROM));
			put(DISPLAY_NAME, getExportCatalogData().get(FIRST_NAME));
			try
			{
				put(B2B_UNIT, Long.valueOf(jnjGTExportCatalogProcessModel.getCurrentB2bUnit().getUid()));
			}
			catch (final NumberFormatException exception)
			{
				LOG.error(
						"An error occured while trying to convert the uid of b2b unit to Long from String, to remove leading zeros",
						exception);
			}
			logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
					Jnjb2bCoreConstants.Logging.END_OF_METHOD);

		}

		/*logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				JnjPCMCoreConstants.Logging.END_OF_METHOD);*/

	}

	/**
	 * This method is used to create the excel file which will be send as the attachment to the user.
	 * 
	 * @param jnjGTExportCatalogEmailProcessModel
	 * @param exportCatalogData
	 */
	protected void createExcelFile(final JnjGTExportCatalogEmailProcessModel jnjGTExportCatalogEmailProcessModel,
			final Map<String, String> exportCatalogData)
	{
		final DateFormat df1 = new SimpleDateFormat("ddMMMyy_HHmmss");
		df1.setTimeZone(TimeZone.getTimeZone("EST"));
		final String currentSite = jnjGTExportCatalogEmailProcessModel.getCurrentSite();
		List<JnjGTProductData> catalogProductsData = null;
		String fileNameStr = null;
		if (!currentSite.equals(Jnjb2bCoreConstants.MDD))
		{
			final List<JnJProductModel> catalogproducts = jnjGTProductService.getProductsForCategory(currentSite,
					jnjGTExportCatalogEmailProcessModel.getCurrentB2bUnit().getPk().toString());

			LOG.info("Total number of products models found for " + currentSite + " are " + catalogproducts.size());
			fileNameStr = "Consumer_Product_Catalog";
			catalogProductsData = Converters.convertAll(catalogproducts, getProductConverter());
			LOG.info("Total number of products data found after conversion, for " + currentSite + " are "
					+ catalogProductsData.size());
		}
		final String filepath = Config.getParameter(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_PATH_KEY);
		FileOutputStream fileOut = null;
		try
		{
			String fileName = null;
			if (currentSite.equals(Jnjb2bCoreConstants.MDD))
			{
				final File[] files = (new File(filepath)).listFiles();
				Arrays.sort(files, new Comparator<File>()
				{
					@Override
					public int compare(final File f1, final File f2)
					{
						return Long.valueOf(f2.lastModified()).compareTo(Long.valueOf(f1.lastModified()));
					}
				});
				for (int index = 0; index < files.length; index++)
				{
					if (files[index].getName().contains(Jnjb2bCoreConstants.MDD))
					{
						fileName = files[index].getName();
						break;
					}
				}
				exportCatalogData.put(Jnjb2bCoreConstants.DELETE_FILE, Boolean.FALSE.toString());
			}
			else
			{
				try
				{
					fileName = fileNameStr + "-" + jnjGTExportCatalogEmailProcessModel.getExportCatalogData().get(TO_EMAIL) + "-"
							+ jnjGTExportCatalogEmailProcessModel.getCurrentB2bUnit().getUid() + "-" + df1.format(new Date()) + ".xls";
					fileOut = new FileOutputStream(filepath + File.separator + fileName);
					jnjGTProductService.createCONSExportFile(catalogProductsData, jnjGTExportCatalogEmailProcessModel
							.getCurrentB2bUnit().getName(), fileName);
					catalogProductsData = null;
				}
				catch (final FileNotFoundException expection)
				{
					LOG.error("There was an error while trying to find the file", expection);
				}
				finally
				{
					try
					{
						fileOut.flush();
						fileOut.close();
					}
					catch (final Exception expection)
					{
						LOG.error("There was an error while trying to close the file output stream", expection);
					}
				}

			}

			exportCatalogData.put(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME, fileName);

		}
		catch (final Exception expection)
		{
			LOG.error("There was an error while trying to find the file", expection);
		}

	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Jnjb2bCoreConstants.Logging.HYPHEN + methodName + Jnjb2bCoreConstants.Logging.HYPHEN
					+ entryOrExit + Jnjb2bCoreConstants.Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Jnjb2bCoreConstants.Logging.HYPHEN + methodName + Jnjb2bCoreConstants.Logging.HYPHEN
					+ message);
		}
	}

	@Override
	protected BaseSiteModel getSite(final StoreFrontProcessModel businessProcessModel)
	{
		return businessProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final StoreFrontProcessModel businessProcessModel)
	{
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(final StoreFrontProcessModel businessProcessModel)
	{
		return null;
	}

	/**
	 * @return the exportCatalogData
	 */
	public Map<String, String> getExportCatalogData()
	{
		return exportCatalogData;
	}

}
