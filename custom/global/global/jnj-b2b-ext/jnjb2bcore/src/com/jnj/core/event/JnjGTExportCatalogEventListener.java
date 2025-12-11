/**
 *
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTExportCatalogEmailProcessModel;


/**
 * This class represents the event listener for the export catalog email flow.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTExportCatalogEventListener extends AbstractSiteEventListener<JnjGTExportCatalogEvent>
{
	private static final Logger LOG = Logger.getLogger(JnjGTExportCatalogEventListener.class);

	/** Model service **/
	@Autowired
	private ModelService modelService;

	/** Business process services required to create process **/
	@Autowired
	private BusinessProcessService businessProcessService;
	
	

	public ModelService getModelService() {
		return modelService;
	}


	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}


	/**
	 * This method is triggered when an object of the event is caught.
	 *
	 * @param JnjGTExportCatalogEvent
	 */
	@Override
	protected void onSiteEvent(final JnjGTExportCatalogEvent JnjGTExportCatalogEvent)
	{
		final String METHOD_NAME = "onSiteEvent()";

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD);
		/** Creating the process **/
		final JnjGTExportCatalogEmailProcessModel JnjGTExportCatalogEmailProcessModel = (JnjGTExportCatalogEmailProcessModel) businessProcessService
				.createProcess("jnjGTExportCatalogEmailProcess" + "-" + System.currentTimeMillis(), "jnjGTExportCatalogEmailProcess");

		final Map<String, String> exportCatalogData = JnjGTExportCatalogEvent.getExportCatalogData();




		/** Populating the Registration Data Map **/
		if (null != exportCatalogData)
		{
			JnjGTExportCatalogEmailProcessModel.setExportCatalogData(exportCatalogData);

			/** Populating the process model and then starting the process for the PCM request account email **/
			populateProcessModel(JnjGTExportCatalogEvent, JnjGTExportCatalogEmailProcessModel);
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.END_OF_METHOD);
	}


	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTExportCatalogEmailProcessModel
	 */
	private void populateProcessModel(final JnjGTExportCatalogEvent event,
			final JnjGTExportCatalogEmailProcessModel JnjGTExportCatalogEmailProcessModel)
	{
		final String METHOD_NAME = "populateProcessModel()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				"Populating the JnjGTExportCatalogEmailProcessModel");
		JnjGTExportCatalogEmailProcessModel.setSite(event.getSite());
		JnjGTExportCatalogEmailProcessModel.setStore(event.getBaseStore());
		JnjGTExportCatalogEmailProcessModel.setCurrentB2bUnit(event.getCurrentB2bUnit());
		JnjGTExportCatalogEmailProcessModel.setCurrentSite(event.getCurrentSite());
		modelService.save(JnjGTExportCatalogEmailProcessModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				"JnjGTExportCatalogEmailProcessModel saved! Now starting the process - JnjGTExportCatalogEmailProcessModel");
		businessProcessService.startProcess(JnjGTExportCatalogEmailProcessModel);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.END_OF_METHOD);
	}

	/**
	 * This method simply enables this listener to handle the event when spotted. Hence default return is true.
	 *
	 * @param JnjGTExportCatalogEvent
	 * @return true
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTExportCatalogEvent JnjGTExportCatalogEvent)
	{
		final String METHOD_NAME = "shouldHandleEvent()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.EXPORT_CATALOG_EMAIL, METHOD_NAME,
				Jnjb2bCoreConstants.Logging.END_OF_METHOD);
		return true;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	private void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 *
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	private void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
}
