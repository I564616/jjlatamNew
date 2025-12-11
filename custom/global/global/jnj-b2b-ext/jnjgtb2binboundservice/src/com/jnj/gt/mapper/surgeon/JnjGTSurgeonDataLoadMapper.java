/**
 * 
 */
package com.jnj.gt.mapper.surgeon;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntSurgeonModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.core.services.surgeon.JnjGTSurgeonService;


/**
 * The Class JnjGTSurgeonDataLoadMapper which by fetching values from intermediate model provides us the Hybris model.
 * 
 * @author sakshi.kashiva
 */
public class JnjGTSurgeonDataLoadMapper extends JnjAbstractMapper
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjGTSurgeonDataLoadMapper.class);

	/** The model service. */
	@Autowired
	private ModelService modelService;

	/** The jnj na surgeon service. */
	@Autowired
	private JnjGTSurgeonService jnjGTSurgeonService;


	/** The jnj na feed service. */
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	//Methad to invoke the main logic
	@Override
	public void processIntermediateRecords()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapSurgeonData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		//invoking method
		mapSurgeonData();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapSurgeonData()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Map surgeon data. Mapping the intermediate records in hybris model
	 * 
	 * @return the surgeon model
	 */
	public boolean mapSurgeonData()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapSurgeonData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		//Fetching the records from intermediate table with pending status
		final Collection<JnjGTIntSurgeonModel> jnjSurgIntModelList = (Collection<JnjGTIntSurgeonModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntSurgeonModel._TYPECODE, RecordStatus.PENDING);
		JnjGTSurgeonModel jnjGTSurgeonModel = null;
		String errorMessage = null;
		boolean recordStatus = false;
		if (CollectionUtils.isNotEmpty(jnjSurgIntModelList))
		{
			for (final JnjGTIntSurgeonModel jnjSurgIntModel : jnjSurgIntModelList)
			{
				try
				{
					final JnjGTSurgeonModel surgeon = new JnjGTSurgeonModel();
					surgeon.setSurgeonId(jnjSurgIntModel.getSurgeonId());
					//Fetching the surgeon model
					jnjGTSurgeonModel = jnjGTSurgeonService.getJnjGTSurgeonModelByExample(surgeon);
					//Validating if the model is created 
					if (null == jnjGTSurgeonModel)
					{
						jnjGTSurgeonModel = modelService.create(JnjGTSurgeonModel.class);
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getSurgeonId()))
					{
						jnjGTSurgeonModel.setSurgeonId(jnjSurgIntModel.getSurgeonId().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getCity()))
					{
						jnjGTSurgeonModel.setCity(jnjSurgIntModel.getCity().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getState()))
					{
						jnjGTSurgeonModel.setState(jnjSurgIntModel.getState().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getBusinessSector()))
					{
						jnjGTSurgeonModel.setBusinessSector(jnjSurgIntModel.getBusinessSector().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getCountry()))
					{
						jnjGTSurgeonModel.setCountry(jnjSurgIntModel.getCountry().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getFirstName()))
					{
						jnjGTSurgeonModel.setFirstName(jnjSurgIntModel.getFirstName().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getLastName()))
					{
						jnjGTSurgeonModel.setLastName(jnjSurgIntModel.getLastName().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getHospitalId()))
					{
						jnjGTSurgeonModel.setHospitalId(jnjSurgIntModel.getHospitalId().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getMiddleName()))
					{
						jnjGTSurgeonModel.setMiddleName(jnjSurgIntModel.getMiddleName().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getPostalCode()))
					{
						jnjGTSurgeonModel.setPostalCode(jnjSurgIntModel.getPostalCode().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getRecNumber()))
					{
						jnjGTSurgeonModel.setRecNumber(jnjSurgIntModel.getRecNumber().trim());
					}
					if (StringUtils.isNotEmpty(jnjSurgIntModel.getRecType()))
					{
						jnjGTSurgeonModel.setRecType(jnjSurgIntModel.getRecType().trim());
					}
					//Saving the Surgeon Model
					recordStatus = jnjGTSurgeonService.saveSurgeon(jnjGTSurgeonModel);
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.SURGEON_FEED + Logging.HYPHEN + "mapSurgeonData()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SURGEON_FEED + Logging.HYPHEN + "mapSurgeonData()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.SURGEON_FEED + Logging.HYPHEN + "mapSurgeonData()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.SURGEON_FEED + Logging.HYPHEN + "mapSurgeonData()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SURGEON_FEED + Logging.HYPHEN + "mapSurgeonData()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjSurgIntModel.getSurgeonId()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjSurgIntModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjSurgIntModel, null, true, errorMessage);
					LOGGER.info(Logging.SURGEON_FEED + Logging.HYPHEN + "mapSurgeonData()" + Logging.HYPHEN
							+ "The Record with Customer Number: " + jnjSurgIntModel.getSurgeonId() + " was not processed successfully.");
				}
			}
		}
		return recordStatus;
	}

	@Override
	public void processIntermediateRecords(String facadeBeanId) {
		// TODO Auto-generated method stub
		
	}
}
