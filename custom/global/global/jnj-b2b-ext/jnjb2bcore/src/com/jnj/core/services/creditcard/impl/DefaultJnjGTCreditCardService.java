/**
 * 
 */
package com.jnj.core.services.creditcard.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.services.creditcard.JnjGTCreditCardService;


/**
 * @author himanshi.batra
 * 
 */
public class DefaultJnjGTCreditCardService implements JnjGTCreditCardService
{

	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreditCardService.class);

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	/**
	 * Save creditcard.
	 * 
	 * @param JnjGTCreditCardModel
	 *           the jnj na CreditCard model
	 * @return true, if successful
	 */
	@Override
	public boolean saveCreditCard(final JnjGTCreditCardModel JnjGTCreditCardModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveCreditCard" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean saved = false;
		try
		{
			modelService.saveAll(JnjGTCreditCardModel);
			saved = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			throw modelSavingException;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveCreditCard" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return saved;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * Gets the creditcard model by example.
	 * 
	 * @param creditcard
	 *           the creditcard
	 * @return the creditcard model by example
	 */
	@Override
	public JnjGTCreditCardModel getJnjGTCreditCardModel(final JnjGTCreditCardModel creditcard)
	{
		JnjGTCreditCardModel JnjGTCreditCardModel = null;
		try
		{
			JnjGTCreditCardModel = flexibleSearchService.getModelByExample(creditcard);

		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.info("CreditCardModel Model Not Found" + exp.getMessage() + "Therefore Creating A New Credit Card Model");

		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("get(JnjGTCreditCardModelByExample()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
			+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnjGTCreditCardModel;
	}
}
