package com.pac.aera.job.service;

import com.gt.pac.aera.PacHiveException;
import com.jnj.core.model.JnJProductModel;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.store.BaseStoreModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;

import static com.gt.pac.aera.constants.Jnjgtb2bpacConstants.*;

/**
 * {@inheritDoc}
 */
public class DefaultJnjGTPacHiveConfigurationService implements JnjGTPacHiveConfigurationService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultJnjGTPacHiveConfigurationService.class);

	protected ConfigurationService configurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPacHiveEnabledGlobally()
	{
		final boolean result = this.configurationService.getConfiguration().getBoolean(
				PAC_AERA_ENABLED,
				PAC_AERA_ENABLED_DEFAULT
		);

		LOG.debug("[PAC HIVE] PAC HIVE is {} globally because '" + PAC_AERA_ENABLED + "' is '{}'.",
		          result ? "enabled" : "disabled", result
		);

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPacHiveDisabledGlobally()
	{
		return !this.isPacHiveEnabledGlobally();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPacHiveEnabledForOrderEntry(@Nonnull final AbstractOrderEntryModel orderEntryModel)
	throws PacHiveException
	{
		Validate.notNull(orderEntryModel, "Parameter 'orderEntryModel' can not be null.");

		if (this.isPacHiveDisabledForOrder(orderEntryModel.getOrder()))
		{
			LOG.debug(
					"[PAC HIVE] PAC HIVE is disabled for order entry with PK '{}' because it is disabled for the " +
					"order.", orderEntryModel.getPk()
			);
			return false;
		}

		final String pacHiveEnabledForProductSectors = this.configurationService.getConfiguration().getString(
				PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR, PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR_DEFAULT
		);

		final ProductModel product = orderEntryModel.getProduct();
		if (!(product instanceof JnJProductModel))
		{
			LOG.error(
					"[PAC HIVE] Can not determine if PAC HIVE is disabled for order entry with PK '{}' because its " +
					"product is not instance of JnJProductModel. PAC HIVE will be considered as disabled.",
					orderEntryModel.getPk()
			);
			return false;
		}

		final JnJProductModel jnJProductModel = (JnJProductModel) product;

		final String orderEntryProductSector = jnJProductModel.getSector();

		final String[] sectors = pacHiveEnabledForProductSectors.split(PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR_SEPARATOR);
		final boolean result = Arrays.stream(sectors)
		                             .map(String::trim)
		                             .filter(StringUtils::isNotBlank)
		                             .anyMatch(it -> StringUtils.equalsIgnoreCase(it, orderEntryProductSector));

		LOG.debug(
				"[PAC HIVE] PAC HIVE is {} for order entry with PK '{}' because '" +
				PAC_AERA_ENABLED_FOR_PRODUCT_SECTOR + "' is '{}' and order entry's product sector is: " +
				"'{}'.",
				result ? "enabled" : "disabled",
				orderEntryModel.getPk(),
				pacHiveEnabledForProductSectors,
				orderEntryProductSector
		);

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPacHiveDisabledForOrderEntry(@Nonnull final AbstractOrderEntryModel orderEntryModel)
	throws PacHiveException
	{
		return !this.isPacHiveEnabledForOrderEntry(orderEntryModel);
	}

	protected boolean isPacHiveEnabledForOrder(@Nonnull final AbstractOrderModel orderModel) throws PacHiveException
	{
		if (this.isPacHiveEnabledForCurrentBaseStore(orderModel))
		{
			LOG.debug(
					"[PAC HIVE] PAC HIVE is enabled for order with PK '{}' sapOrderNumber: '{}' because it is enabled" +
					" for the order's base store.",
					orderModel.getPk(),
					orderModel.getSapOrderNumber()
			);
			return true;
		}

		Validate.notNull(orderModel, "Parameter 'orderModel' can not be null.");

		final String pacHiveEnabledForOrderCodes = this.configurationService.getConfiguration().getString(
				PAC_AERA_ENABLED_FOR_ORDERS, PAC_AERA_ENABLED_FOR_ORDERS_DEFAULT
		);

		final String orderCode = orderModel.getCode();
		final String sapOrderNumber = orderModel.getSapOrderNumber();

		final String[] orderCodes = pacHiveEnabledForOrderCodes.split(PAC_AERA_ENABLED_FOR_ORDERS_SEPARATOR);
		final boolean result = Arrays.stream(orderCodes)
		                             .map(String::trim)
		                             .filter(StringUtils::isNotBlank)
		                             .anyMatch(it -> StringUtils.equalsIgnoreCase(it, orderCode) ||
		                                             StringUtils.equalsIgnoreCase(it, sapOrderNumber)
		                             );

		LOG.debug(
				"[PAC HIVE] PAC HIVE is {} for order with PK '{}' sapOrderNumber: '{}' because '" +
				PAC_AERA_ENABLED_FOR_ORDERS + "' is '{}' and order code is '{}' sapOrderNumber: '{}'.",
				result ? "enabled" : "disabled",
				orderModel.getPk(),
				orderModel.getSapOrderNumber(),
				pacHiveEnabledForOrderCodes,
				orderCode,
				sapOrderNumber
		);

		return result;
	}

	protected boolean isPacHiveDisabledForOrder(@Nonnull final AbstractOrderModel orderModel) throws PacHiveException
	{
		return !this.isPacHiveEnabledForOrder(orderModel);
	}

	protected boolean isPacHiveEnabledForCurrentBaseStore(@Nonnull final AbstractOrderModel orderModel)
	throws PacHiveException
	{
		return !this.isPacHiveDisabledForCurrentBaseStore(orderModel);
	}

	protected boolean isPacHiveDisabledForCurrentBaseStore(@Nonnull final AbstractOrderModel orderModel)
	throws PacHiveException
	{
		Validate.notNull(orderModel, "Parameter 'orderModel' can not be null.");

		if (this.isPacHiveDisabledGlobally())
		{
			LOG.debug(
					"[PAC HIVE] PAC HIVE is disabled for order's (PK '{}' sapOrderNumber: '{}') base store '{}' " +
					"because it is disabled globally.",
					orderModel.getPk(),
					orderModel.getSapOrderNumber(),
					orderModel.getStore() == null ? null : orderModel.getStore().getUid()
			);

			return true;
		}

		BaseStoreModel currentBaseStore = orderModel.getStore();
		if (null == currentBaseStore)
		{
			Set<BaseStoreModel> baseStoresInB2bUnit = orderModel.getUnit().getCountry().getBaseStores();
			currentBaseStore = baseStoresInB2bUnit.iterator().next();
		}
		
		if (null == currentBaseStore)
		{
			throw new PacHiveException(
					"Can not check if PAC HIVE is enabled for current base store because it is null. Probably the " +
					"method is being called from a wrong place which does not have current base store in context."
			);
		}

		final String baseStoresWithPacHiveDisabled = this.configurationService.getConfiguration().getString(
				PAC_AERA_DISABLED_FOR_BASE_STORES, PAC_AERA_DISABLED_FOR_BASE_STORES_DEFAULT
		);

		final String currentBaseStoreUid = currentBaseStore.getUid();

		final boolean result = Arrays.stream(
				baseStoresWithPacHiveDisabled.split(PAC_AERA_DISABLED_FOR_BASE_STORES_SEPARATOR)
		).map(
				String::trim
		).filter(
				StringUtils::isNotBlank
		).anyMatch(
				it -> StringUtils.equalsIgnoreCase(it, currentBaseStoreUid)
		);

		LOG.debug(
				"[PAC HIVE] PAC HIVE is {} for order with PK '{}' sapOrderNumber: '{}' because '" +
				PAC_AERA_DISABLED_FOR_BASE_STORES_SEPARATOR + "' is '{}' and order base store is '{}'.",
				result ? "enabled" : "disabled",
				orderModel.getPk(),
				orderModel.getSapOrderNumber(),
				baseStoresWithPacHiveDisabled,
				currentBaseStoreUid
		);

		return result;
	}

	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
