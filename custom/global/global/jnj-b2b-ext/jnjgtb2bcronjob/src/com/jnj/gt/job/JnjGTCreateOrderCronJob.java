/**
 *
 */
package com.jnj.gt.job;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.text.ParseException;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.constants.Jnjgtb2bcronjobConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.core.data.JnjGTCreateConsOrdResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjGTIntermediateMasterModel;
import com.jnj.gt.outbound.mapper.JnjGTCreateConsOrdMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateDeliveredOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateOrderMapper;
import com.jnj.gt.service.common.JnjGTFeedService;


/**
 * The JnjGTCreateOrderCronJob class is used to processed all those orders which is not created in SAP and it will
 * execute on daily basis.
 * 
 * @author sumit.y.kumar
 * 
 */
public class JnjGTCreateOrderCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTCreateOrderCronJob.class);

	@Autowired
	protected JnjOrderService jnjOrderService;

	@Autowired
	protected JnjGTCreateOrderMapper jnjGTCreateOrderMapper;

	@Resource(name = "GTCreateDeliveredOrderMapper")
	protected JnjGTCreateDeliveredOrderMapper jnjGTCreateDeliveredOrderMapper;

	@Autowired
	protected JnjGTCreateConsOrdMapper jnjGTCreateConsOrdMapper;

	@Autowired
	protected JnjGTFeedService jnjGTFeedService;

	/**
	 * This method is responsible for calling the JnjGTCreateMapperImpl, JnjGTCreateDeliveredMapperImpl class for
	 * retrieving those orders for which SAP order Number doesn't exist in the hybris database.
	 * 
	 * @param cronJobModel
	 * @return PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean isExceptionOccured = false;
		String errorMessage = null;
		final List<OrderModel> orderModelList = jnjOrderService.getSubmitOrderDataList();
		try
		{
			if (null != orderModelList && !orderModelList.isEmpty())
			{
				// Iterating the order model and send it to the SAP Submit Order Service to
				// create the order model at the SAP end.
				for (final OrderModel orderModel : orderModelList)
				{
					isExceptionOccured = false;
					final JnjGTIntermediateMasterModel jnjGTIntMasterModel = orderModel.getOperationArchModel();
					if (null != jnjGTIntMasterModel && jnjGTIntMasterModel.getRecordStatus() == RecordStatus.PENDING)
					{
						if (jnjGTIntMasterModel.getWriteAttempts().intValue() < Jnjb2bCoreConstants.MAX_WRITE_ATTEMPTS.intValue())
						{
							try
							{
								errorMessage = null;
								final JnjGTSapWsData wsData = new JnjGTSapWsData();
								wsData.setConnectionTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_CRONJOB_CONNECTION_TIME_OUT);
								wsData.setReadTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.WEBSERVICE_ORDER_CRONJOB_READ_TIME_OUT);
								// Fetch order type value from the order model.
								final String orderType = orderModel.getOrderType().getCode();
								// if its value is house or trade or affiliate then enter inside if block and invokes the consumer out bound service.
								if (JnjOrderTypesEnum.ZHOR.getCode().equals(orderType)
										|| JnjOrderTypesEnum.ZTOR.getCode().equals(orderType)
										|| JnjOrderTypesEnum.ZIO2.getCode().equals(orderType))
								{
									final JnjGTCreateConsOrdResponseData jnjGTCreateConsOrdResponseData = jnjGTCreateConsOrdMapper
											.mapCreateConsOrdRequestResponse(orderModel, wsData);
									if (null != jnjGTCreateConsOrdResponseData && !jnjGTCreateConsOrdResponseData.isSapResponseStatus()
											&& StringUtils.isNotEmpty(jnjGTCreateConsOrdResponseData.getErrorMessage()))
									{
										errorMessage = jnjGTCreateConsOrdResponseData.getErrorMessage();
										isExceptionOccured = true;
									}
								}
								// else if its value is delivered then enter inside if block and invokes the create delivered  order service.
								else if (JnjOrderTypesEnum.ZDEL.getCode().equals(orderType)
										|| JnjOrderTypesEnum.ZKB.getCode().equals(orderType))
								{
									jnjGTCreateDeliveredOrderMapper.mapCreateDelOrderRequestResponse(orderModel, wsData);
								}// else invokes create order out bound service.
								else if (JnjOrderTypesEnum.ZOR.getCode().equals(orderType)
										|| JnjOrderTypesEnum.ZEX.getCode().equals(orderType)
										|| JnjOrderTypesEnum.ZNC.getCode().equals(orderType))
								{
									jnjGTCreateOrderMapper.mapCreateOrderRequestResponse(orderModel, wsData);
								}
							}
							catch (final ParseException exception)
							{
								LOGGER.error(
										Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
												+ "Parse Exception Occured " + exception.getMessage() + "orderModel code is "
												+ orderModel.getCode(), exception);
								isExceptionOccured = true;
								errorMessage = exception.getMessage();
							}
							catch (final NumberFormatException exception)
							{
								LOGGER.error(
										Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
												+ "Number Format Exception Occured " + exception.getMessage() + "orderModel code is "
												+ orderModel.getCode(), exception);
								isExceptionOccured = true;
								errorMessage = exception.getMessage();
							}
							catch (final ModelNotFoundException | BusinessException exception)
							{
								LOGGER.error(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
										+ "Model Not Found Exception or business exception Occured " + exception.getMessage()
										+ "orderModel code is " + orderModel.getCode(), exception);
								isExceptionOccured = true;
								if (StringUtils.isNotEmpty(exception.getMessage()))
								{
									errorMessage = exception.getMessage();
								}
								else
								{
									errorMessage = "No customer master record exists for customer";
								}
							}

							if (isExceptionOccured)
							{
								//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
								jnjGTFeedService.updateIntermediateRecord(jnjGTIntMasterModel, null, true, errorMessage,
										Logging.CREATE_ORDER_CRONJOB, orderModel.getCode());
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
											+ "The Record with Order Number: " + orderModel.getCode()
											+ " was not processed successfully and error message is: " + errorMessage);
								}
							}
							else
							{
								//Record Successfully saved to Hybris. Changing the status to 'Success'.
								if (LOGGER.isDebugEnabled())
								{
									LOGGER.debug(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
											+ "The Record with Order Number: " + orderModel.getCode()
											+ " processed successfully. Changing the Record status to 'SUCCESS'");
								}
								jnjGTFeedService.updateIntermediateRecord(jnjGTIntMasterModel, RecordStatus.SUCCESS, false, null);
							}
						}
					}
					else
					{
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug("CronJob runs successfully run for this order but no updation in status because JnjGTIntermediateMasterModel Model not present for given Order number");
						}
					}
				}
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("CronJob runs successfully");
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.END_OF_METHOD
						+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final SystemException systemException)
		{
			LOGGER.error(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + "System Exception occured "
					+ systemException.getMessage(), systemException);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		catch (final IntegrationException integrationException)
		{
			LOGGER.error(Logging.CREATE_ORDER_CRONJOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN
					+ "Integration Exception occured " + integrationException.getMessage(), integrationException);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}
}
