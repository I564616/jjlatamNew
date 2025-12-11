package com.jnj.facades.order.converters.populator;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaCoreUtil;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The JnjScheduleLinePopulator class is used to populate the data in the Schedule lines of JnjOrderEntryData from the
 * Delivery Schedule lines of the AbstractOrderEntry Model.
 *
 * @author sumit.y.kumar
 *
 */
public class JnjLaScheduleLinePopulator extends JnjGTScheduleLinePopulator
{

	private final static String available = "available";
	private final static String notAvailable = "notAvailable";
	private final static String partialyAvailable = "partialyAvailable";
	private final static String multipleDateAvailable = "multipleDateAvailable";

	protected final static String CONFIRMED_LINE_STATUS_CODES = Config
			.getParameter(Jnjb2bCoreConstants.Order.CONFIRMED_SCHEDULE_LINE_STATUS);

	private final static String functionalityName = "Latam ScheduleLine Populator";

	private static final Class currentClass = JnjLaScheduleLinePopulator.class;
	private static final String PAC_HIVE_ENABLED = "pac.aera.enabled";

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected ConfigurationService configurationService;
	
	@Override
	public void populate(final AbstractOrderEntryModel source, final JnjGTOrderEntryData target)
	{
		final String methodName = "populate()";
		JnjGTCoreUtil.logDebugMessage(functionalityName, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		long confirmedQty = 0;
		boolean pacHiveEnabled = configurationService.getConfiguration().getBoolean(PAC_HIVE_ENABLED);
		boolean isPODAvailable = source.getDeliverySchedules().stream().anyMatch(se -> se.getProofOfDeliveryDate()!=null);
		boolean pachiveFlag = false; 
		if (pacHiveEnabled && CollectionUtils.isEmpty(source.getJnjPacHiveEntries())) {
			pachiveFlag = true;
		}
		boolean schFlag = false;
		if (!pacHiveEnabled && CollectionUtils.isNotEmpty(source.getDeliverySchedules())) {
			schFlag = true;
		}
		if (pachiveFlag || schFlag || isPODAvailable)
		{
			final List<JnjDeliveryScheduleData> jnjDeliveryScheduleDataList = new ArrayList<>();
			final List<JnjDeliveryScheduleModel> jnjDeliveryScheduleModelList = new ArrayList<>();

			jnjDeliveryScheduleModelList.addAll(source.getDeliverySchedules());

			for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : jnjDeliveryScheduleModelList)
			{
				if (null != jnjDeliveryScheduleModel)
				{
					final JnjDeliveryScheduleData scheduleData = populateScheduleEntry(jnjDeliveryScheduleModel);
					jnjDeliveryScheduleDataList.add(scheduleData);
					if (null != scheduleData.getQuantity()){
						confirmedQty = confirmedQty + scheduleData.getQuantity().longValue();
					}
				}
			}

			// If there are free items, change the confirmedQty variable value to consider them
			final Map<String, JnjOutOrderLine> freeGoodsMapFromSession = sessionService
					.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);

			if (freeGoodsMapFromSession != null && !freeGoodsMapFromSession.isEmpty())
			{
				confirmedQty += getFreeItemConfirmedQty(source.getProduct().getCode(), freeGoodsMapFromSession);
			}

			target.setScheduleLines(jnjDeliveryScheduleDataList);
			target.setConfirmedQty(Long.valueOf(confirmedQty));
			if (source.getQuantity().longValue() == confirmedQty && jnjDeliveryScheduleDataList.size() == 1){
				target.setAvailabilityStatus(available);
			}else if (source.getQuantity().longValue() == confirmedQty){
				target.setAvailabilityStatus(multipleDateAvailable);
			}else if (confirmedQty == 0){
				target.setAvailabilityStatus(notAvailable);
			}else{
				target.setAvailabilityStatus(partialyAvailable);
			}
		}
		else
		{
			target.setAvailabilityStatus(notAvailable);
		}
		JnjGTCoreUtil.logDebugMessage(functionalityName, methodName, Logging.END_OF_METHOD, currentClass);
	}

	/**
	 * @param code
	 * @param freeGoodsMapFromSession
	 * @return
	 */
	private long getFreeItemConfirmedQty(final String code, final Map<String, JnjOutOrderLine> freeGoodsMapFromSession)
	{
		long freeGoodsConfirmedQty = 0;

		for (final Map.Entry<String, JnjOutOrderLine> entry : freeGoodsMapFromSession.entrySet())
		{
			if (entry.getKey().equalsIgnoreCase(code))
			{
				freeGoodsConfirmedQty += JnjLaCoreUtil.getLongValueFromDecimalString(entry.getValue().getMaterialQuantity());
				return freeGoodsConfirmedQty;
			}
		}
		return freeGoodsConfirmedQty;
	}

	private JnjDeliveryScheduleData populateScheduleEntry(final JnjDeliveryScheduleModel source)
	{
		final String methodName = "populateScheduleEntry()";
		JnjGTCoreUtil.logDebugMessage(functionalityName, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		SimpleDateFormat commonDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final JnjDeliveryScheduleData target = new JnjDeliveryScheduleData();
		target.setDeliveryDate(source.getDeliveryDate());
		target.setScheduledLineNumber(source.getScheduledLineNumber());
		target.setLineNumber(source.getLineNumber());
		target.setQuantity(source.getQty());
		target.setRoundedQuantity(source.getRoundedQuantity());
		target.setShippingDate(source.getShipDate());

		if (source.getProofOfDeliveryDate() != null) {
			target.setProofOfDeliveryDate(commonDateFormat.format(source.getProofOfDeliveryDate()));
			target.setDeliveryDate(source.getProofOfDeliveryDate());
		}

		if (source.getCarrierExpectedDeliveryDate() != null) {
			target.setCarrierExpectedDeliveryDate(commonDateFormat.format(source.getCarrierExpectedDeliveryDate()));
		}
		//Display availability date from product model instead of Simulate response
		if (null != source.getOwnerEntry() && null != source.getOwnerEntry().getProduct())
		{
			target.setMaterialAvailabilityDate(((JnJProductModel) (source.getOwnerEntry().getProduct())).getBackOrderedDate());
		}
		final String scheduleLineCode = source.getLineStatus();
		String scheduleLineStatus = null;
		if (StringUtils.isNotEmpty(scheduleLineCode)) {
			populateScheduleLineStatus
					(source, target, scheduleLineCode, scheduleLineStatus);
		}

		JnjGTCoreUtil.logDebugMessage(functionalityName, methodName, Logging.END_OF_METHOD, currentClass);

		return target;
	}

	private static void populateScheduleLineStatus(JnjDeliveryScheduleModel source, JnjDeliveryScheduleData target, String scheduleLineCode, String scheduleLineStatus) {
		if (CONFIRMED_LINE_STATUS_CODES.contains(scheduleLineCode))
		{
			scheduleLineStatus = OrderEntryStatus.CONFIRMED.getCode();
		}
		else if (Jnjb2bCoreConstants.Order.SCHEDULE_LINE_STATUS_UC.equals(scheduleLineCode))
		{
			if (source.getOwnerEntry() != null && Jnjb2bCoreConstants.Order.PRODUCT_DIVISION
					.equals(((JnJProductModel) source.getOwnerEntry().getProduct()).getSalesOrgCode()))
			{
				scheduleLineStatus = OrderEntryStatus.ITEM_ACCEPTED.getCode();
			}
			else
			{
				scheduleLineStatus = OrderEntryStatus.BACKORDERED.getCode();
			}
		}
		else if (Jnjb2bCoreConstants.Order.SCHEDULE_LINE_STATUS_CANCELLED.equals(scheduleLineCode))
		{
			scheduleLineStatus = OrderEntryStatus.CANCELLED.getCode();
		}
		target.setLineStatus(scheduleLineStatus);
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}
}
