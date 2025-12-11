package com.pac.aera.job.service;

import com.gt.pac.aera.PacHiveException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import jakarta.annotation.Nonnull;

/**
 * Service for working with PAC HIVE configuration.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface JnjGTPacHiveConfigurationService
{
	/**
	 * Checks if PAC HIVE is enabled for the given order entry.
	 *
	 * @param orderEntryModel Order entry model to be checked.
	 * @return {@code true} if PAC HIVE is enabled for the given order code otherwise {@code false}.
	 */
	boolean isPacHiveEnabledForOrderEntry(@Nonnull AbstractOrderEntryModel orderEntryModel) throws PacHiveException;

	/**
	 * Checks if PAC HIVE is disabled for the given order entry.
	 *
	 * @param orderEntryModel Order entry model to be checked.
	 * @return {@code true} if PAC HIVE is disabled for the given order code otherwise {@code false}.
	 */
	boolean isPacHiveDisabledForOrderEntry(@Nonnull AbstractOrderEntryModel orderEntryModel) throws PacHiveException;

	/**
	 * Checks if PAC HIVE is enabled globally.
	 *
	 * @return {@code true} if PAC HIVE is enabled globally otherwise {@code false}.
	 */
	boolean isPacHiveEnabledGlobally();

	/**
	 * Checks if PAC HIVE is disabled globally.
	 *
	 * @return {@code true} if PAC HIVE is disabled globally otherwise {@code false}.
	 */
	boolean isPacHiveDisabledGlobally();
}
