/**
 * 
 */
package com.jnj.gt.mapper.creditcard;

import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntCreditCardModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.core.services.creditcard.JnjGTCreditCardService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * The Class jnjGTCreditCardDataLoadMapper which by fetching values from intermediate model provides us the Hybris
 * model.
 * 
 * @author himanshi.batra
 */
public class JnjGTCreditCardDataLoadMapper extends JnjAbstractMapper
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjGTCreditCardDataLoadMapper.class);

	/** The model service. */
	@Autowired
	private ModelService modelService;

	@Autowired
	private JnjGTCreditCardService jnjGTCreditCardService;

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerSevice;

	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjConfigService jnjConfigService;

	//Methad to invoke the main logic
	@Override
	public void processIntermediateRecords()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapCreditCardData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		//invoking method
		mapCreditCardData();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapCreditCardData()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Map surgeon data. Mapping the intermediate records in hybris model
	 * 
	 * @return the surgeon model
	 */
	public boolean mapCreditCardData()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapCreditCardData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		boolean recordStatus = false;

		String errorMessage = null;
		final List<JnjGTIntCreditCardModel> jnjcreditcardIntModelList = (List<JnjGTIntCreditCardModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntCreditCardModel._TYPECODE, RecordStatus.PENDING);

		if (CollectionUtils.isNotEmpty(jnjcreditcardIntModelList))
		{
			for (final JnjGTIntCreditCardModel jnjcreditcardIntModel : jnjcreditcardIntModelList)
			{
				recordStatus = false;
				try
				{

					/* Checking if the User corresponding to the creditCard exist or not */
					JnJB2bCustomerModel customer = null;
					if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getUserId()))
					{
						customer = new JnJB2bCustomerModel();
						customer.setGatewayUserId(jnjcreditcardIntModel.getUserId());
						customer = jnjGTCustomerSevice.getJnJB2bCustomerModel(customer);
					}
					/*
					 * JnJB2BUnitModel b2bUnit = null; if
					 * (StringUtils.isNotEmpty(jnjcreditcardIntModel.getShipToAccountNumer())) { Checking if the B2BuNIT
					 * corresponding to the creditCard exist or not b2bUnit = new JnJB2BUnitModel(); b2bUnit =
					 * jnjGTB2BUnitService.getB2BUnitModelForUid(jnjcreditcardIntModel.getShipToAccountNumer(),
					 * JnjGTInboundUtil.fetchValidSourceSysId(jnjcreditcardIntModel.getSourceId())); if (b2bUnit != null) {
					 * LOGGER.info("B2BUnit" + jnjcreditcardIntModel.getShipToAccountNumer() +
					 * "Not Found for Credit Card User" + jnjcreditcardIntModel.getUserId()); } }
					 */

					/* If exist then only process the record */
					if (customer != null)
					{
						/* Checking that credit card exist for incoming Sequence Number */
						JnjGTCreditCardModel creditCard = new JnjGTCreditCardModel();
						creditCard.setCcOwner(customer.getUid());
						creditCard.setCode(jnjcreditcardIntModel.getCardSequenceNumber());
						creditCard.setCardSource(jnjcreditcardIntModel.getCardSource());
						creditCard = jnjGTCreditCardService.getJnjGTCreditCardModel(creditCard);
						/* Creating a new model when credit card with the incoming sequence number exist or not */
						if (creditCard == null)
						{
							creditCard = modelService.create(JnjGTCreditCardModel.class);
							creditCard.setCcOwner(customer.getUid());
							//Mapping the user value
							creditCard.setUser(customer);

						}
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardSequenceNumber()))
						{

							creditCard.setCode(jnjcreditcardIntModel.getCardSequenceNumber());
							//As Suggested By Sandeep
							creditCard.setNumber(jnjcreditcardIntModel.getCardLasr4Digit());
						}

						//Mapping the Sourceid value 
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getSourceId()))
						{
							creditCard.setSourceId(JnjGTInboundUtil.fetchValidSourceSysId(jnjcreditcardIntModel.getSourceId()));
						}

						//Mapping the Card Tokenize value 
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardNumber()))
						{
							creditCard.setSubscriptionId(jnjcreditcardIntModel.getCardNumber());
						}
						//Mapping the Buisness sector value
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getBuisnessSector()))
						{
							creditCard.setBuisnessSector(jnjcreditcardIntModel.getBuisnessSector());
						}

						//Mapping the Cardsource value
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardSource()))
						{
							creditCard.setCardSource(jnjcreditcardIntModel.getCardSource());
						}

						//Mapping the Card value
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardLasr4Digit()))
						{
							creditCard.setCardLasr4Digit(jnjcreditcardIntModel.getCardLasr4Digit());
						}
						//Mapping the Cardexpiry for month
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardExpiryMonth()))
						{
							creditCard.setValidToMonth(jnjcreditcardIntModel.getCardExpiryMonth());
						}
						//Mapping the Cardexpiry for year
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardExpiryYear()))
						{
							creditCard.setValidToYear(jnjcreditcardIntModel.getCardExpiryYear());
						}
						//Mapping the Card type value
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardType()))
						{
							final String cardType = jnjConfigService.getConfigValueById(jnjcreditcardIntModel.getCardType());
							creditCard.setType(CreditCardType.valueOf(cardType));
						}

						//Mapping the account value
						//creditCard.setAccount(b2bUnit);

						//Mapping the indicator value
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getIndicator()))
						{
							creditCard.setIndicator(jnjcreditcardIntModel.getIndicator());
						}
						//Mapping the Card date value
						if (StringUtils.isNotEmpty(jnjcreditcardIntModel.getCardAddedDate()))
						{
							creditCard.setCardAddedDate(jnjcreditcardIntModel.getCardAddedDate());
						}
						//Saving the CreditCard Model
						recordStatus = jnjGTCreditCardService.saveCreditCard(creditCard);
						LOGGER.info("Credit Card Created For Customer" + jnjcreditcardIntModel.getUserId());
					}
					else
					{
						LOGGER.error("Credit Card Model Not Created Because Customer Not Found With User Id "
								+ jnjcreditcardIntModel.getUserId() + "In Hybris");
						recordStatus = false;
					}

				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.CREDIT_CARD_FEED + Logging.HYPHEN + "mapCreditCardData()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelLoadingException exception)
				{
					LOGGER.error(Logging.ORDER_TEMPLATE_FEED + Logging.HYPHEN + "mapOrderTemplateEntryToHybris()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CREDIT_CARD_FEED + Logging.HYPHEN + "mapCreditCardData()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.CREDIT_CARD_FEED + Logging.HYPHEN + "mapCreditCardData()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Exception exception)
				{
					LOGGER.error(Logging.CREDIT_CARD_FEED + Logging.HYPHEN + "mapCreditCardData()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CREDIT_CARD_FEED + Logging.HYPHEN + "mapCreditCardData()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjcreditcardIntModel.getUserId()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjcreditcardIntModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjcreditcardIntModel, null, true, errorMessage);
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CREDIT_CARD_FEED + Logging.HYPHEN + "mapCreditCardData()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjcreditcardIntModel.getUserId()
								+ " was not processed successfully.");
					}
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
