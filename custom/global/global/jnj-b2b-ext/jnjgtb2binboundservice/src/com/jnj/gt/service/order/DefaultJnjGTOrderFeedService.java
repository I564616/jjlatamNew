/**
 * 
 */
package com.jnj.gt.service.order;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Order;
import com.jnj.gt.dao.order.JnjGTOrderFeedDao;
import com.jnj.core.event.JnjGTOrderStatusValidationEvent;
import com.jnj.gt.model.JnjGTIntOrdHdrNoteModel;
import com.jnj.gt.model.JnjGTIntOrdLineHoldLocalModel;
import com.jnj.gt.model.JnjGTIntOrdLinePriceLocalModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderLinePartModel;
import com.jnj.gt.model.JnjGTIntOrderLineTxtModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;
import com.jnj.gt.model.JnjGTIntProductLotMasterModel;
import com.jnj.gt.model.JnjGTTanOrdEntStsMappingModel;


/**
 * The jnjGTOrderFeedServiceImpl class contains all those methods which are dealing with order related intermediate
 * model and it has definition of all the methods which are defined in the jnjGTOrderFeedService interface.
 * 
 * @author
 * 
 */
public class DefaultJnjGTOrderFeedService implements JnjGTOrderFeedService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderFeedService.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private JnjGTOrderFeedDao jnjGTOrderFeedDao;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private BaseStoreService baseStoreService;

	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Override
	public List<JnjGTIntOrdHdrNoteModel> getJnjGTIntOrdHdrNoteModel(final String orderNumber, final String sourceSystemId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrdHdrNoteModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTIntOrdHdrNoteModel jnjGTIntOrdHdrNoteModel = new JnjGTIntOrdHdrNoteModel();
		jnjGTIntOrdHdrNoteModel.setSapOrderNumber(orderNumber);

		List<JnjGTIntOrdHdrNoteModel> jnjGTIntOrdHdrNotes = null;
		try
		{
			jnjGTIntOrdHdrNotes = flexibleSearchService.getModelsByExample(jnjGTIntOrdHdrNoteModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return jnjGTIntOrdHdrNotes;
	}

	@Override
	public List<JnjGTIntOrderLineModel> getJnjGTIntOrderLineModel(final String orderNumber, final String sourceSystemId,
			final List<String> itemCategory, final String highLevelItemNumber)
	{
		return jnjGTOrderFeedDao.getJnjGTIntOrderLineModel(orderNumber, sourceSystemId, itemCategory, highLevelItemNumber);
	}

	@Override
	public List<JnjGTIntOrderSchLineModel> getJnjGTIntOrderSchLineModel(final String orderNumber, final String sourceSystemId,
			final String orderLineNumber)
	{
		return jnjGTOrderFeedDao.getJnjGTIntOrderSchLineModel(orderNumber, sourceSystemId, orderLineNumber);
	}

	@Override
	public List<JnjGTIntOrderLineTxtModel> getJnjGTIntOrderLineTxtModel(final String orderNumber, final String sourceSystemId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderLineTxtModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTIntOrderLineTxtModel jnjGTIntOrderLineTxtModel = new JnjGTIntOrderLineTxtModel();
		jnjGTIntOrderLineTxtModel.setSapOrderNumber(orderNumber);
		jnjGTIntOrderLineTxtModel.setSourceSystemId(sourceSystemId);

		List<JnjGTIntOrderLineTxtModel> jnjGTIntOrderLineTxts = null;
		try
		{
			jnjGTIntOrderLineTxts = flexibleSearchService.getModelsByExample(jnjGTIntOrderLineTxtModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return jnjGTIntOrderLineTxts;
	}

	@Override
	public List<JnjGTIntOrderLinePartModel> getJnjGTIntOrderLinePartModel(final String orderNumber, final String sourceSystemId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderLinePartModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTIntOrderLinePartModel jnjGTIntOrderLinePartModel = new JnjGTIntOrderLinePartModel();
		jnjGTIntOrderLinePartModel.setSapOrderNumber(orderNumber);
		jnjGTIntOrderLinePartModel.setSourceSystemId(sourceSystemId);

		List<JnjGTIntOrderLinePartModel> jnjGTIntOrderLineParts = null;
		try
		{
			jnjGTIntOrderLineParts = flexibleSearchService.getModelsByExample(jnjGTIntOrderLinePartModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return jnjGTIntOrderLineParts;
	}

	@Override
	public List<JnjGTIntOrdLineHoldLocalModel> getJnjGTIntOrdLineHoldLocalModel(final String orderNumber,
			final String sourceSystemId, final String orderLineNumber,final Date recordTimeStamp)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrdLineHoldLocalModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTIntOrdLineHoldLocalModel jnjGTIntOrdLineHoldLocalModel = new JnjGTIntOrdLineHoldLocalModel();
		jnjGTIntOrdLineHoldLocalModel.setSapOrderNumber(orderNumber);
		jnjGTIntOrdLineHoldLocalModel.setSourceSystemId(sourceSystemId);

		if (StringUtils.isNotBlank(orderLineNumber))
		{
			jnjGTIntOrdLineHoldLocalModel.setSapOrderLineNumber(orderLineNumber);
		}

		List<JnjGTIntOrdLineHoldLocalModel> jnjGTIntOrdLineHoldLocals = null;
		try
		{
			jnjGTIntOrdLineHoldLocals = flexibleSearchService.getModelsByExample(jnjGTIntOrdLineHoldLocalModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return jnjGTIntOrdLineHoldLocals;
	}

	@Override
	public List<JnjGTIntOrdLinePriceLocalModel> getJnjGTIntOrdLinePriceLocalModel(final String orderNumber,
			final String sourceSystemId, final String orderLineNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrdLinePriceLocalModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTIntOrdLinePriceLocalModel jnjGTIntOrdLinePriceLocalModel = new JnjGTIntOrdLinePriceLocalModel();
		jnjGTIntOrdLinePriceLocalModel.setSapOrderNumber(orderNumber);
		jnjGTIntOrdLinePriceLocalModel.setSourceSystemId(sourceSystemId);

		if (StringUtils.isNotBlank(orderLineNumber))
		{
			jnjGTIntOrdLinePriceLocalModel.setSapOrderLineNumber(orderLineNumber);
		}

		List<JnjGTIntOrdLinePriceLocalModel> jnjGTIntOrdLinePriceLocals = null;
		try
		{
			jnjGTIntOrdLinePriceLocals = flexibleSearchService.getModelsByExample(jnjGTIntOrdLinePriceLocalModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return jnjGTIntOrdLinePriceLocals;
	}

	@Override
	public Double getPriceJnjGTIntOrdLineHoldLocal(final String sapOrderNumber, final String sapOrderLineNumber,
			final String sourceSystemId, final String[] priceConditionType)
	{
		final List<JnjGTIntOrdLinePriceLocalModel> jnjGTIntOrdLinePriceLocalModels = jnjGTOrderFeedDao
				.getPriceJnjGTIntOrdLineHoldLocal(sapOrderNumber, sapOrderLineNumber, sourceSystemId, priceConditionType);
		double sum = 0;
		if (CollectionUtils.isNotEmpty(jnjGTIntOrdLinePriceLocalModels))
		{
			for (final JnjGTIntOrdLinePriceLocalModel jnjGTIntOrdLinePriceLocalModel : jnjGTIntOrdLinePriceLocalModels)
			{
				sum += Double.parseDouble(jnjGTIntOrdLinePriceLocalModel.getValue());
			}
		}
		return Double.valueOf(sum);
	}

	@Override
	public JnjGTIntProductLotMasterModel getJnjGTIntProductLotMasterModel(final String lotNumber, final String sourceSystemId,
			final String productSkuCode)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrdLineHoldLocalModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTIntProductLotMasterModel jnjGTIntProductLotMasterModel = new JnjGTIntProductLotMasterModel();
		jnjGTIntProductLotMasterModel.setSrcSysId(sourceSystemId);
		jnjGTIntProductLotMasterModel.setLotNumber(lotNumber);
		jnjGTIntProductLotMasterModel.setProductSkuCode(productSkuCode);

		List<JnjGTIntProductLotMasterModel> jnjGTIntProductLotMasterList = null;
		try
		{
			jnjGTIntProductLotMasterList = flexibleSearchService.getModelsByExample(jnjGTIntProductLotMasterModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		if (jnjGTIntProductLotMasterList.size() > 0)
		{
			return jnjGTIntProductLotMasterList.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public BaseSiteModel getBaseSiteModelUsingSourceSysId(final String sourceSysId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getBaseSiteModelUsingSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		BaseSiteModel baseSiteModel = null;
		if (StringUtils.isNotEmpty(sourceSysId) && sourceSysId.equals(JnjGTSourceSysId.MDD.getCode()))
		{
			baseSiteModel = baseSiteService.getBaseSiteForUID(JnJCommonUtil.getValue(Jnjb2bCoreConstants.MDD_SITE_ID_KEY));
		}
		else if (StringUtils.isNotEmpty(sourceSysId))
		{
			baseSiteModel = baseSiteService.getBaseSiteForUID(JnJCommonUtil.getValue(Jnjb2bCoreConstants.CONSUMER_SITE_ID_KEY));
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getBaseSiteModelUsingSourceSysId()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return baseSiteModel;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public OrderModel getOrderModelUsingSapOrdNoAndBaseSite(final String sapOrderNumber, final BaseSiteModel baseSiteModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderModelUsingSapOrdNoAndBaseSite()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		OrderModel orderModel = null;
		try
		{
			if (StringUtils.isNotEmpty(sapOrderNumber) && null != baseSiteModel)
			{
				final OrderModel exampleOrder = new OrderModel();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("getOrderModelUsingSapOrdNoAndBaseSite()" + Logging.HYPHEN + "Sap Order Number " + sapOrderNumber
							+ "Base Site Uid " + baseSiteModel.getUid());
				}
				exampleOrder.setSapOrderNumber(sapOrderNumber);
				exampleOrder.setSite(baseSiteModel);

				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Customer product number.
				orderModel = flexibleSearchService.getModelByExample(exampleOrder);
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			return null;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderModelUsingSapOrdNoAndBaseSite()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderModel;
	}

	@Override
	public void sendOrderStatusFileValidationEmail(final List<String> fileNames, final Map<String, String> fileAndLineCounts)
	{
		final JnjGTOrderStatusValidationEvent event = new JnjGTOrderStatusValidationEvent();

		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getBaseSiteForUID(Jnjb2bCoreConstants.CONS_SITE_UID));
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		/*** Set Anonymous User since cron job is running through Admin. ***/
		event.setCustomer(userService.getAnonymousUser());
		event.setFileNameAndCounts(fileAndLineCounts);
		event.setOrderStatusInboundFileNames(fileNames);
		eventService.publishEvent(event);
	}

	@Override
	public String getCurrencyFromOrdLinePriceLocal(final String sapOrderNumber, final String sourceSysid)
	{
		String currencyIsoCode = jnjGTOrderFeedDao.getCurrencyFromOrdLinePriceLocal(sapOrderNumber, sourceSysid);
		if (StringUtils.isEmpty(currencyIsoCode))
		{
			currencyIsoCode = Config.getParameter(Order.DEFAULT_CURRENCY_KEY);
		}
		return currencyIsoCode;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public OrderEntryStatus getTanOrderLineStatus(final String firstTanOrderLineStatus, final String secondTanOrderLineStatus)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getTanOrderLineStatus()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnjGTTanOrdEntStsMappingModel jnjGTTanOrdEntStsMappingModel = null;
		OrderEntryStatus orderEntryStatus = null;
		try
		{
			if (StringUtils.isNotEmpty(firstTanOrderLineStatus) && StringUtils.isNotEmpty(secondTanOrderLineStatus))
			{
				jnjGTTanOrdEntStsMappingModel = new JnjGTTanOrdEntStsMappingModel();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("getTanOrderLineStatus()" + Logging.HYPHEN + "First Tan Order Line Status " + firstTanOrderLineStatus
							+ "Second Tan Order Line Status " + secondTanOrderLineStatus);
				}
				jnjGTTanOrdEntStsMappingModel.setFirstTanOrderLineStatus(firstTanOrderLineStatus);
				jnjGTTanOrdEntStsMappingModel.setSecondTanOrderLineStatus(secondTanOrderLineStatus);

				// Invoking the Flexible Search Service to get the order entry status mapping model for tan tan scenario.
				jnjGTTanOrdEntStsMappingModel = flexibleSearchService.getModelByExample(jnjGTTanOrdEntStsMappingModel);
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			return null;
		}
		if (null != jnjGTTanOrdEntStsMappingModel)
		{
			orderEntryStatus = jnjGTTanOrdEntStsMappingModel.getFinalStatus();
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getTanOrderLineStatus()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return orderEntryStatus;
	}
}
