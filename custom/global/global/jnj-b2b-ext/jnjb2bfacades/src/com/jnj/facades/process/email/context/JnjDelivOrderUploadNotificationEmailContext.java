/**
 *
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.email.JnjGTEmailGenerationService;
import com.jnj.core.enums.JnjOrderTypesEnum;
//import com.jnj.na.model.JnjNADeliveredOrderNotificationProcessModel;



/**
 * @author shriya.tiwari
 *
 */
public class JnjDelivOrderUploadNotificationEmailContext extends CustomerEmailContext
{
	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjDelivOrderUploadNotificationEmailContext.class);


	public static final String EMAIL_ATTACHMENT_PATH = "attachmentPath";
	public static final String EMAIL_ATTACHMENT_FILE_NAME = "attachmentFileName";
	public static final String USE_DIRECT_PATH = "useDirectPath";


	/**
	 * JnJ Order Number.
	 */
	private String jnjOrderNumber;


	/**
	 * @return the jnjOrderNumber
	 */
	public String getJnjOrderNumber()
	{
		return jnjOrderNumber;
	}

	/**
	 * @param jnjOrderNumber
	 *           the jnjOrderNumber to set
	 */
	public void setJnjOrderNumber(final String jnjOrderNumber)
	{
		this.jnjOrderNumber = jnjOrderNumber;
	}



	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{

		final String METHOD_INIT = "Context - init()";

		LOGGER.info(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + System.currentTimeMillis() + "JnjDelivOrderUploadNotificationEmailContext");



		//final JnjNADeliveredOrderNotificationProcessModel delivOrderUploadNotificationProcessModel = (JnjNADeliveredOrderNotificationProcessModel) businessProcessModel;

		/*final OrderModel order = delivOrderUploadNotificationProcessModel.getOrder();

		if (JnjOrderTypesEnum.ZDEL.equals(order.getOrderType()) && null != order.getAttachedDoc())
		{

			final MediaModel mediaModel = order.getAttachedDoc();
			final File file = new File(JnJCommonUtil.getValue("delivered.order.attachment.path") + File.separator
					+ mediaModel.getRealFileName());
			final InputStream is = mediaService.getStreamFromMedia(mediaModel);
			OutputStream os;

			try
			{
				os = new FileOutputStream(file);
				final byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1)
				{
					os.write(buffer, 0, bytesRead);
				}
				is.close();
				os.flush();
				os.close();

			}
			catch (final FileNotFoundException exception)
			{
				LOGGER.error("Error while trying to find the file for the deliver order upload email", exception);
			}
			catch (final IOException ioe)
			{
				LOGGER.error("Error while trying to perform I/O operations on the file for the deliver order upload email", ioe);
			}

			put("orderNumber", order.getOrderNumber());


			put(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME, file.getName());
			put(Jnjb2bCoreConstants.USE_DIRECT_PATH, TRUE);
			put(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH, JnJCommonUtil.getValue("delivered.order.attachment.path"));


			put(Jnjb2bCoreConstants.DELETE_FILE, Boolean.TRUE);

			*//** Setting the From Email address **//*
			put(FROM_EMAIL, Config.getParameter("user.from.supervisor.approval.email"));

			put(FROM_DISPLAY_NAME, "CustomerSupport");
			put(DISPLAY_NAME, "CustomerSupport");
			put(EMAIL, Config.getParameter("delivered.order.file.upload"));
			try
			{
				modelService.remove(order.getAttachedDoc());

			}
			catch (final ModelRemovalException exp)
			{
				LOGGER.error("Not able to delete attached doc from Order: " + order.getOrderNumber());
			}
			order.setAttachedDoc(null);

			modelService.save(order);
		}*/
	}
}