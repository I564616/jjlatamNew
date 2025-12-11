/**
 * 
 */
package com.jnj.gt.mapper.shipment;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.model.JnjGTShippingLineDetailsModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.ShipmentInbound;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.mapper.order.JnjGTOrderSyncDataLoadMapper;
import com.jnj.gt.model.JnjGTIntShipTrckHdrModel;
import com.jnj.gt.model.JnjGTIntShipTrckLineModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.order.JnjGTOrderFeedService;
import com.jnj.gt.service.shipment.JnjGTShipmentFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * The JnjGTShipmentDataLoadMapper class contains all those methods which map the shipment related intermediate tables
 * data with Hybris Shipment model(JnjGTShippingDetails).
 * 
 * @author sumit.y.kumar
 * 
 */
public class JnjGTShipmentDataLoadMapper extends JnjAbstractMapper
{
	static final Logger LOGGER = Logger.getLogger(JnjGTShipmentDataLoadMapper.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTShipmentFeedService jnjGTShipmentFeedService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private JnjGTOrderFeedService jnjGTOrderFeedService;

	@Autowired
	private JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapper;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	/**
	 * {!@inheritDoc}
	 * 
	 */
	@Override
	public void processIntermediateRecords()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Call the private method for sold to records.
		mapShipmentDataWithOrder();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	/**
	 * Map shipment data with order.
	 * 
	 * @return true, if successful
	 */
	public boolean mapShipmentDataWithOrder()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		boolean recordStatus = false;
		String errorMessage = null;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntShipTrckHdrModel> jnjGTIntShipTrckHdrModels = (List<JnjGTIntShipTrckHdrModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntShipTrckHdrModel._TYPECODE, RecordStatus.PENDING);
		if (!jnjGTIntShipTrckHdrModels.isEmpty())
		{
			for (final JnjGTIntShipTrckHdrModel jnjGTIntShipTrckHdrModel : jnjGTIntShipTrckHdrModels)
			{
				OrderModel orderModel = null;
				String sourceSysId = null;
				boolean isModelExisted = false;
				try
				{
					errorMessage = null;
					recordStatus = false;
					if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getSourceSysId())
							&& StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCorrelationId())
							&& StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getSapOrderNum()))
					{
						// Call To fetch the source system id
						sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntShipTrckHdrModel.getSourceSysId());
						final BaseSiteModel baseSiteModel = jnjGTOrderFeedService.getBaseSiteModelUsingSourceSysId(sourceSysId);
						orderModel = jnjGTOrderFeedService.getOrderModelUsingSapOrdNoAndBaseSite(
								jnjGTIntShipTrckHdrModel.getSapOrderNum(), baseSiteModel);
						if (null != orderModel)
						{
							final List<JnjGTIntShipTrckLineModel> jnjGTIntShipTrckLineModels = jnjGTShipmentFeedService
									.getJnjGTIntShipTrckLineModel(jnjGTIntShipTrckHdrModel.getCorrelationId(), null,
											jnjGTIntShipTrckHdrModel.getSourceSysId());
							if (CollectionUtils.isNotEmpty(orderModel.getShippingDetails()))
							{
								for (final JnjGTShippingDetailsModel jnjGTShippingDetailsModel : orderModel.getShippingDetails())
								{
									if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCorrelationId())
											&& StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getSourceSysId())
											&& jnjGTShippingDetailsModel.getCorrelationId().equals(
													jnjGTIntShipTrckHdrModel.getCorrelationId())
											&& jnjGTShippingDetailsModel.getSourceSysId().equals(jnjGTIntShipTrckHdrModel.getSourceSysId()))
									{
										// map the line level information by getting value from the intermediate and set it in hybris model.
										jnjGTShippingDetailsModel
												.setShippingLineDetails(mapIntLineModelWithHybrisLineModel(jnjGTIntShipTrckLineModels));
										// map the header information
										mapIntHdrModelWithHybrisShippingDetailModel(jnjGTShippingDetailsModel, jnjGTIntShipTrckHdrModel);
										isModelExisted = true;
										break;
									}
								}
								if (!isModelExisted)
								{
									final JnjGTShippingDetailsModel jnjGTShippingDetailsModel = modelService
											.create(JnjGTShippingDetailsModel.class);
									// map the line level information by getting value from the intermediate and set it in hybris model.
									jnjGTShippingDetailsModel
											.setShippingLineDetails(mapIntLineModelWithHybrisLineModel(jnjGTIntShipTrckLineModels));
									// map the header information
									mapIntHdrModelWithHybrisShippingDetailModel(jnjGTShippingDetailsModel, jnjGTIntShipTrckHdrModel);
									final Set<JnjGTShippingDetailsModel> jnjGTShippingDetailsModels = new HashSet<JnjGTShippingDetailsModel>(
											orderModel.getShippingDetails());
									jnjGTShippingDetailsModels.add(jnjGTShippingDetailsModel);
									orderModel.setShippingDetails(jnjGTShippingDetailsModels);
								}
							}
							else
							{
								final JnjGTShippingDetailsModel jnjGTShippingDetailsModel = modelService
										.create(JnjGTShippingDetailsModel.class);
								// map the line level information by getting value from the intermediate and set it in hybris model.
								jnjGTShippingDetailsModel
										.setShippingLineDetails(mapIntLineModelWithHybrisLineModel(jnjGTIntShipTrckLineModels));
								// map the header information
								mapIntHdrModelWithHybrisShippingDetailModel(jnjGTShippingDetailsModel, jnjGTIntShipTrckHdrModel);
								final Set<JnjGTShippingDetailsModel> jnjGTShippingDetailsModels = new HashSet<JnjGTShippingDetailsModel>();
								jnjGTShippingDetailsModels.add(jnjGTShippingDetailsModel);
								orderModel.setShippingDetails(jnjGTShippingDetailsModels);
							}
							getShipmentActualDate(orderModel, jnjGTIntShipTrckLineModels, jnjGTIntShipTrckHdrModel);
							modelService.save(orderModel);
							recordStatus = true;
						}
						else
						{
							errorMessage = "NO Order Exists in Hybris corresponding to the Order Number : "
									+ jnjGTIntShipTrckHdrModel.getSapOrderNum() + " | Corelation ID: "
									+ jnjGTIntShipTrckHdrModel.getCorrelationId() + " | Base Site UID : " + baseSiteModel.getUid()
									+ " | Source Sys ID: " + jnjGTIntShipTrckHdrModel.getSourceSysId();
						}

					}
					else if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getSourceSysId())
							&& StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getDeliveryNum()))
					{
						// Call To fetch the source system id
						sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntShipTrckHdrModel.getSourceSysId());
						final List<JnjGTIntShipTrckLineModel> jnjGTIntShipTrckLineModels = jnjGTShipmentFeedService
								.getJnjGTIntShipTrckLineModel(null, jnjGTIntShipTrckHdrModel.getDeliveryNum(),
										jnjGTIntShipTrckHdrModel.getSourceSysId());

						if (CollectionUtils.isNotEmpty(jnjGTIntShipTrckLineModels))
						{
							final BaseSiteModel baseSiteModel = jnjGTOrderFeedService.getBaseSiteModelUsingSourceSysId(sourceSysId);
							final String sapOrderNumber = jnjGTIntShipTrckLineModels.get(0).getSapOrderNum();
							orderModel = jnjGTOrderFeedService.getOrderModelUsingSapOrdNoAndBaseSite(sapOrderNumber, baseSiteModel);
							if (null != orderModel)
							{
								if (CollectionUtils.isNotEmpty(orderModel.getShippingDetails()))
								{
									for (final JnjGTShippingDetailsModel jnjGTShippingDetailsModel : orderModel.getShippingDetails())
									{
										if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getDeliveryNum())
												&& StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getSourceSysId())
												&& jnjGTShippingDetailsModel.getDeliveryNum().equals(
														jnjGTIntShipTrckHdrModel.getDeliveryNum())
												&& jnjGTShippingDetailsModel.getSourceSysId().equals(
														jnjGTIntShipTrckHdrModel.getSourceSysId()))
										{
											// map the line level information by getting value from the intermediate and set it in hybris model.
											jnjGTShippingDetailsModel
													.setShippingLineDetails(mapIntLineModelWithHybrisLineModel(jnjGTIntShipTrckLineModels));
											// map the header information
											mapIntHdrModelWithHybrisShippingDetailModel(jnjGTShippingDetailsModel, jnjGTIntShipTrckHdrModel);
											isModelExisted = true;
											break;
										}
									}
									if (!isModelExisted)
									{
										final JnjGTShippingDetailsModel jnjGTShippingDetailsModel = modelService
												.create(JnjGTShippingDetailsModel.class);
										// map the line level information by getting value from the intermediate and set it in hybris model.
										jnjGTShippingDetailsModel
												.setShippingLineDetails(mapIntLineModelWithHybrisLineModel(jnjGTIntShipTrckLineModels));
										// map the header information
										mapIntHdrModelWithHybrisShippingDetailModel(jnjGTShippingDetailsModel, jnjGTIntShipTrckHdrModel);
										final Set<JnjGTShippingDetailsModel> jnjGTShippingDetailsModels = new HashSet<JnjGTShippingDetailsModel>(
												orderModel.getShippingDetails());
										jnjGTShippingDetailsModels.add(jnjGTShippingDetailsModel);
										orderModel.setShippingDetails(jnjGTShippingDetailsModels);
									}
								}
								else
								{
									final JnjGTShippingDetailsModel jnjGTShippingDetailsModel = modelService
											.create(JnjGTShippingDetailsModel.class);
									// map the line level information by getting value from the intermediate and set it in hybris model.
									jnjGTShippingDetailsModel
											.setShippingLineDetails(mapIntLineModelWithHybrisLineModel(jnjGTIntShipTrckLineModels));
									// map the header information
									mapIntHdrModelWithHybrisShippingDetailModel(jnjGTShippingDetailsModel, jnjGTIntShipTrckHdrModel);
									final Set<JnjGTShippingDetailsModel> jnjGTShippingDetailsModels = new HashSet<JnjGTShippingDetailsModel>();
									jnjGTShippingDetailsModels.add(jnjGTShippingDetailsModel);
									orderModel.setShippingDetails(jnjGTShippingDetailsModels);
								}

								getShipmentActualDate(orderModel, jnjGTIntShipTrckLineModels, jnjGTIntShipTrckHdrModel);
								modelService.save(orderModel);
								recordStatus = true;
							}
							else
							{
								errorMessage = "NO Order Exists in Hybris corresponding to the Order Number : " + sapOrderNumber
										+ " | Delivery Number: " + jnjGTIntShipTrckHdrModel.getDeliveryNum() + " | Base Site UID : "
										+ baseSiteModel.getUid();
							}
						}
						else
						{
							errorMessage = "No Shipping Tracking Lines present corresponding to the Delivery Number: "
									+ jnjGTIntShipTrckHdrModel.getDeliveryNum();
						}
					}
					else
					{
						errorMessage = "Shipping Tracking Header could NOT be processed either of the required value is null or invalid || Correlation Id:  "
								+ jnjGTIntShipTrckHdrModel.getCorrelationId()
								+ " | Delivery Number: "
								+ jnjGTIntShipTrckHdrModel.getDeliveryNum()
								+ " | SAP Order Number: "
								+ jnjGTIntShipTrckHdrModel.getSapOrderNum()
								+ " | SOURCE SYS ID: "
								+ jnjGTIntShipTrckHdrModel.getSourceSysId();
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
								+ "The Record with Correlation Id: " + jnjGTIntShipTrckHdrModel.getCorrelationId()
								+ "  and delivery number: " + jnjGTIntShipTrckHdrModel.getDeliveryNum()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntShipTrckHdrModel, RecordStatus.SUCCESS, false, null);

					if (JnjGTSourceSysId.MDD.toString().equals(sourceSysId))
					{
						updateOrderAndOrderEntryStatus(orderModel);
					}
				}
				else
				{
					String recordId = null;
					if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCorrelationId()))
					{
						recordId = jnjGTIntShipTrckHdrModel.getCorrelationId();
					}
					else if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getDeliveryNum()))
					{
						recordId = jnjGTIntShipTrckHdrModel.getDeliveryNum();
					}
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntShipTrckHdrModel, null, true, errorMessage,
							Logging.SHIPMENT_TRACKING_FEED, recordId);
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
								+ "The Record with Number: " + recordId + " was not processed successfully. Error Message:  "
								+ errorMessage);
					}
				}
			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
						+ "No JnjGTIntShipTrckHdrModel Records with status 'PENDING' exists in Hybris. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapShipmentDataWithOrder()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return recordStatus;
	}


	private void getShipmentActualDate(final OrderModel orderModel,
			final List<JnjGTIntShipTrckLineModel> jnjGTIntShipTrckLineModel, final JnjGTIntShipTrckHdrModel jnjGTIntShipTrckHdrModel)
	{
		final DateFormat format = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final List<AbstractOrderEntryModel> entries = orderModel.getEntries();
		final List<AbstractOrderEntryModel> updatedEntries = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel entryModel : entries)
		{
			final List<JnjDeliveryScheduleModel> schlines = entryModel.getDeliverySchedules();

			for (final JnjGTIntShipTrckLineModel stLineModel : jnjGTIntShipTrckLineModel)
			{
				if (stLineModel.getDeliveryLineNum().equals(entryModel.getSapOrderlineNumber()))
				{
					for (final JnjDeliveryScheduleModel schModel : schlines)
					{
						try
						{
							System.out.println("Before Changing" + schModel.getShipDate());
							schModel.setShipDate(format.parse(jnjGTIntShipTrckHdrModel.getActualShipDate()));

							System.out.println("After Changing" + schModel.getShipDate());
						}
						catch (final ParseException e)
						{
							// YTODO Anerated catch block
							e.printStackTrace();
						}
						modelService.save(schModel);
					}
				}

			}
			updatedEntries.add(entryModel);

		}
		orderModel.setEntries(updatedEntries);
		modelService.saveAll(orderModel);
	}

	/**
	 * Map intermediate ship track line model with hybris shipping line details model.
	 * 
	 * @param jnjGTIntShipTrckLineModels
	 *           the jnj na int ship trck line models
	 * @return the sets the
	 */
	private Set<JnjGTShippingLineDetailsModel> mapIntLineModelWithHybrisLineModel(
			final List<JnjGTIntShipTrckLineModel> jnjGTIntShipTrckLineModels)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapIntLineModelWithHybrisLineModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Set<JnjGTShippingLineDetailsModel> jnjGTShippingLineDetailsModels = new HashSet<JnjGTShippingLineDetailsModel>();
		for (final JnjGTIntShipTrckLineModel jnjGTIntShipTrckLineModel : jnjGTIntShipTrckLineModels)
		{
			if (null != jnjGTIntShipTrckLineModel)
			{
				final JnjGTShippingLineDetailsModel jnjGTShippingLineDetailsModel = modelService
						.create(JnjGTShippingLineDetailsModel.class);
				jnjGTShippingLineDetailsModel.setSourceSysId(jnjGTIntShipTrckLineModel.getSourceSysId());
				if (StringUtils.isNotEmpty(jnjGTIntShipTrckLineModel.getDeliveryLineNum()))
				{
					jnjGTShippingLineDetailsModel.setDeliveryLineNum(jnjGTIntShipTrckLineModel.getDeliveryLineNum());
				}
				if (StringUtils.isNotEmpty(jnjGTIntShipTrckLineModel.getDeliveryQty()))
				{
					jnjGTShippingLineDetailsModel.setDeliveryQty(jnjGTIntShipTrckLineModel.getDeliveryQty());
				}
				if (StringUtils.isNotEmpty(jnjGTIntShipTrckLineModel.getTrackingNum()))
				{
					jnjGTShippingLineDetailsModel.setTrackingNum(jnjGTIntShipTrckLineModel.getTrackingNum());
				}
				if (StringUtils.isNotEmpty(jnjGTIntShipTrckLineModel.getSapOrderLineNum()))
				{
					jnjGTShippingLineDetailsModel.setSapOrderLineNum(jnjGTIntShipTrckLineModel.getSapOrderLineNum());
				}
				modelService.save(jnjGTShippingLineDetailsModel);
				jnjGTShippingLineDetailsModels.add(jnjGTShippingLineDetailsModel);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapIntLineModelWithHybrisLineModel()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTShippingLineDetailsModels;
	}

	private void mapIntHdrModelWithHybrisShippingDetailModel(final JnjGTShippingDetailsModel jnjGTShippingDetailsModel,
			final JnjGTIntShipTrckHdrModel jnjGTIntShipTrckHdrModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapIntHdrModelWithHybrisShippingDetailModel()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		jnjGTShippingDetailsModel.setSourceSysId(jnjGTIntShipTrckHdrModel.getSourceSysId());
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCorrelationId()))
		{
			jnjGTShippingDetailsModel.setCorrelationId(jnjGTIntShipTrckHdrModel.getCorrelationId());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getDeliveryNum()))
		{
			jnjGTShippingDetailsModel.setDeliveryNum(jnjGTIntShipTrckHdrModel.getDeliveryNum());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getActualDeliveryTime()))
		{
			jnjGTShippingDetailsModel.setActualDeliveryTime(jnjGTIntShipTrckHdrModel.getActualDeliveryTime());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getActualShipTime()))
		{
			jnjGTShippingDetailsModel.setActualShipTime(jnjGTIntShipTrckHdrModel.getActualShipTime());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getBolNum()))
		{
			jnjGTShippingDetailsModel.setBolNum(jnjGTIntShipTrckHdrModel.getBolNum());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCarrierScacCde()))
		{
			jnjGTShippingDetailsModel.setCarrierScacCde(jnjGTIntShipTrckHdrModel.getCarrierScacCde());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getCarrierScacName()))
		{
			jnjGTShippingDetailsModel.setCarrierScacName(jnjGTIntShipTrckHdrModel.getCarrierScacName());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getContainerId()))
		{
			jnjGTShippingDetailsModel.setContainerId(jnjGTIntShipTrckHdrModel.getContainerId());
		}
		if (StringUtils.isNotEmpty(jnjGTIntShipTrckHdrModel.getTrackingNum()))
		{
			jnjGTShippingDetailsModel.setTrackingNum(jnjGTIntShipTrckHdrModel.getTrackingNum());
		}

		final String dateFormat = Config.getParameter(ShipmentInbound.SHIP_TRACK_DATE_FORMAT_KEY);
		final DateFormat formatter = new SimpleDateFormat(dateFormat);

		if (null != jnjGTIntShipTrckHdrModel.getActualDeliveryDate())
		{
			Date actualDeliveryDate = null;
			try
			{
				actualDeliveryDate = formatter.parse(jnjGTIntShipTrckHdrModel.getActualDeliveryDate());
				jnjGTShippingDetailsModel.setActualDeliveryDate(actualDeliveryDate);
			}
			catch (final ParseException exception)
			{
				LOGGER.error("Parsing Exception while processing " + jnjGTIntShipTrckHdrModel.ACTUALDELIVERYDATE
						+ exception.getMessage());
				jnjGTShippingDetailsModel.setActualDeliveryDate(null);
			}
		}

		if (null != jnjGTIntShipTrckHdrModel.getActualShipDate())
		{
			Date actualShipDate = null;
			try
			{
				actualShipDate = formatter.parse(jnjGTIntShipTrckHdrModel.getActualShipDate());
			}
			catch (final ParseException exception)
			{
				LOGGER.error("Parsing Exception while processing " + jnjGTIntShipTrckHdrModel.ACTUALSHIPDATE + exception.getMessage());
			}
			jnjGTShippingDetailsModel.setActualShipDate(actualShipDate);
		}
		modelService.save(jnjGTShippingDetailsModel);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SHIPMENT_TRACKING_FEED + Logging.HYPHEN + "mapIntHdrModelWithHybrisShippingDetailModel()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Updates and saves status of the Order and its associated entries from <code>jnjGTOrderSyncDataLoadMapper</code>.
	 * 
	 * @param order
	 */
	private void updateOrderAndOrderEntryStatus(final OrderModel order)
	{
		final OrderStatus orderStatus = order.getStatus();
		if (CollectionUtils.isNotEmpty(order.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				try
				{
					jnjGTOrderSyncDataLoadMapper.populateMddOrderEntryStatus(order, entry);
					modelService.save(entry);
				}
				catch (final Exception exception)
				{
					LOGGER.error("ERROR WHILE UPDATING ORDER ENTRY STATUS FOR ORDER | SAP ORDER NUMBER: " + order.getSapOrderNumber()
							+ " | LINE NUMBER: " + entry.getSapOrderlineNumber() + " | EXCEPTION: ", exception);
					continue;
				}
			}
		}

		try
		{
			jnjGTOrderSyncDataLoadMapper.populateMddOrderStatus(order);
			final OrderStatus currentOrderStatus = order.getStatus();

			/***
			 * Set Email Notification & Email Preference flag if new order status corresponds to PARTIALLY_SHIPPED or
			 * SHIPPED.
			 ***/
			if ((orderStatus == null || !orderStatus.equals(currentOrderStatus))
					&& (currentOrderStatus != null && OrderStatus.PARTIALLY_SHIPPED.equals(currentOrderStatus) || OrderStatus.SHIPPED
							.equals(currentOrderStatus)))
			{
				order.setSendOrderShipmentEmail(true);
				order.setShipmentEmailPreference(true);
			}
			modelService.save(order);
		}
		catch (final Exception exception)
		{
			LOGGER.error("ERROR WHILE UPDATING ORDER STATUS FOR ORDER: " + order.getSapOrderNumber() + " | EXCEPTION: ", exception);
		}

	}


	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}
}
