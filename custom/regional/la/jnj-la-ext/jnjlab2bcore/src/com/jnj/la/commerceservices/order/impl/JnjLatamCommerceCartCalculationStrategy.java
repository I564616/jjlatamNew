/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.commerceservices.order.impl;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 *
 */
public class JnjLatamCommerceCartCalculationStrategy extends DefaultCommerceCartCalculationStrategy
{

	private CalculationService calculationService;
	private PromotionsService promotionsService;
	private TimeService timeService;
	private BaseSiteService baseSiteService;
	private List<CommerceCartCalculationMethodHook> commerceCartCalculationMethodHooks;
	private ConfigurationService configurationService;
	private ExternalTaxesService externalTaxesService;
	private boolean calculateExternalTaxes = false;

	@Override
	@Deprecated
	public boolean calculateCart(final CartModel cartModel)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.calculateCart(parameter);
	}

	@Override
	public boolean calculateCart(final CommerceCartParameter parameter)
	{
		final String methodName = "calculateCart()";
		final CartModel cartModel = parameter.getCart();

		validateParameterNotNull(cartModel, "Cart model cannot be null");

		final CalculationService calcService = getCalculationService();
		boolean recalculated = false;
		if (calcService.requiresCalculation(cartModel))
		{
			try
			{
				parameter.setRecalculate(false);
				beforeCalculate(parameter);
				calcService.calculate(cartModel);
				getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
						AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
			}
			catch (final CalculationException calculationException)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.CART_CALCULATE, methodName,
						"Cart model " + cartModel.getCode() + " was not calculated due to: " + ExceptionUtils.getStackTrace(calculationException),
						JnjLatamCommerceCartCalculationStrategy.class);
			}
			catch (final Exception exception)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.CART_CALCULATE, methodName,
						"Exception: Cart model was not calculated due to: " + exception.getMessage(), exception,
						JnjLatamCommerceCartCalculationStrategy.class);
			}
			finally
			{
				afterCalculate(parameter);
			}
			recalculated = true;
		}
		if (calculateExternalTaxes)
		{
			getExternalTaxesService().calculateExternalTaxes(cartModel);
		}
		return recalculated;
	}

	@Override
	@Deprecated
	public boolean recalculateCart(final CartModel cartModel)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.recalculateCart(parameter);
	}

	@Override
	public boolean recalculateCart(final CommerceCartParameter parameter)
	{
		final String methodName = "recalculateCart()";
		JnjGTCoreUtil.logDebugMessage(Logging.CART_RECALCULATE, methodName,
				"Inside JnjLatamCommerceCartCalculationStrategy recalculateCart() method",
				JnjLatamCommerceCartCalculationStrategy.class);
		final CartModel cartModel = parameter.getCart();
		try
		{
			parameter.setRecalculate(true);
			beforeCalculate(parameter);
			getCalculationService().recalculate(cartModel);
			getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
					AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
		}
		catch (final CalculationException calculationException)
		{
			JnjGTCoreUtil.logDebugMessage(Logging.CART_RECALCULATE, methodName,
					"Cart model " + cartModel.getCode() + " was not calculated due to: " + ExceptionUtils.getStackTrace(calculationException),
					JnjLatamCommerceCartCalculationStrategy.class);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.CART_RECALCULATE, methodName,
					"Exception: Cart model was not recalculated due to: " + exception.getMessage(), exception,
					JnjLatamCommerceCartCalculationStrategy.class);
		}
		finally
		{
			afterCalculate(parameter);

		}
		return true;
	}

	@Override
	protected void beforeCalculate(final CommerceCartParameter parameter)
	{
		if (getCommerceCartCalculationMethodHooks() != null && (parameter.isEnableHooks() && getConfigurationService()
				.getConfiguration().getBoolean(CommerceServicesConstants.CARTCALCULATIONHOOK_ENABLED, true)))
		{
			for (final CommerceCartCalculationMethodHook commerceCartCalculationMethodHook : getCommerceCartCalculationMethodHooks())
			{
				commerceCartCalculationMethodHook.beforeCalculate(parameter);
			}
		}
	}

	@Override
	protected void afterCalculate(final CommerceCartParameter parameter)
	{
		if (getCommerceCartCalculationMethodHooks() != null && (parameter.isEnableHooks() && getConfigurationService()
				.getConfiguration().getBoolean(CommerceServicesConstants.CARTCALCULATIONHOOK_ENABLED, true)))
		{
			for (final CommerceCartCalculationMethodHook commerceCartCalculationMethodHook : getCommerceCartCalculationMethodHooks())
			{
				commerceCartCalculationMethodHook.afterCalculate(parameter);
			}
		}
	}

	@Override
	protected Collection<PromotionGroupModel> getPromotionGroups()
	{
		final Collection<PromotionGroupModel> promotionGroupModels = new ArrayList<>();
		if (getBaseSiteService().getCurrentBaseSite() != null
				&& getBaseSiteService().getCurrentBaseSite().getDefaultPromotionGroup() != null)
		{
			promotionGroupModels.add(getBaseSiteService().getCurrentBaseSite().getDefaultPromotionGroup());
		}
		return promotionGroupModels;
	}

	@Override
	protected CalculationService getCalculationService()
	{
		return calculationService;
	}

	@Override
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}


	@Override
	protected PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	@Override
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	@Override
	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Override
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	@Override
	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Override
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Override
	protected List<CommerceCartCalculationMethodHook> getCommerceCartCalculationMethodHooks()
	{
		return new ArrayList<>(commerceCartCalculationMethodHooks);
	}

	@Override
	public void setCommerceCartCalculationMethodHooks(
			final List<CommerceCartCalculationMethodHook> commerceCartCalculationMethodHooks)
	{
		this.commerceCartCalculationMethodHooks = new ArrayList<>(commerceCartCalculationMethodHooks);
	}

	@Override
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Override
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	@Override
	public ExternalTaxesService getExternalTaxesService()
	{
		return externalTaxesService;
	}


	@Override
	public void setExternalTaxesService(final ExternalTaxesService externalTaxesService)
	{
		this.externalTaxesService = externalTaxesService;
	}


	@Override
	public boolean isCalculateExternalTaxes()
	{
		return calculateExternalTaxes;
	}


	@Override
	public void setCalculateExternalTaxes(final boolean calculateExternalTaxes)
	{
		this.calculateExternalTaxes = calculateExternalTaxes;
	}

}
