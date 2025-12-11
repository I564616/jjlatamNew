package com.jnj.la.core.services.impl;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.dao.JnJCrossReferenceDao;
import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.operationarchitecture.JnjInterfaceOperationArchUtility;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.daos.JnjLaOrderDao;
import com.jnj.la.core.dto.JnjLaUomDTO;
import com.jnj.la.core.model.JnjIntLoadTranslationModel;
import com.jnj.la.core.model.LoadTranslationModel;
import com.jnj.la.core.services.JnjLoadTranslationService;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * This is the service class for the load translation. This class will be saving the model in the database
 *
 * @author sanchit.a.kumar
 *
 */
public class JnjLoadTranslationServiceImpl implements JnjLoadTranslationService
{
    private static final Logger LOGGER = Logger.getLogger(JnjLoadTranslationServiceImpl.class);

    private static final String TRANSLATION_MODEL_FOUND = "Load Translation model with catalog id : %s is present for given b2bUnit : %s and for given customer material number : %s";
    private static final String TRANSLATION_MODEL_NOT_FOUND = "Load Translation model not found for given : %s and for given customer material number : %s";

    @Autowired
	protected ModelService modelService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;

	@Autowired
	protected JnJCrossReferenceDao jnJCrossReferenceDao;

	@Autowired
	protected JnjLaOrderDao jnjOrderDao;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	/**
	 * The setter for the model service
	 *
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * This method is saving the model in the database
	 *
	 * @throws BusinessException
	 */
	@Override
	public boolean saveloadTranslationData(final LoadTranslationModel loadTranslationModel) throws BusinessException
	{
		boolean status;
		try
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Going to save in database");
			}
			modelService.save(loadTranslationModel);
			status = true;
		}
		catch (final Exception e)
		{
			status = false;
			LOGGER.error("Error in saving record in database - " + e.getMessage());
			LOGGER.error(e);
			throw new BusinessException("Error in saving record in database - " + e.getMessage(), MessageCode.BUSINESS_EXCEPTION,
					Severity.BUSINESS_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled() && status)
		{
			LOGGER.debug("Record saved in database");
		}
		return status;
	}

	@Override
    public LoadTranslationModel getLoadTranslationModel(final String customerProductNumber, final JnJB2BUnitModel b2bUnit) {
        final String methodName = "getLoadTranslationModel ()";

        JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), JnjLoadTranslationServiceImpl.class);

        if (StringUtils.isNotEmpty(customerProductNumber)) {
            try {
                final LoadTranslationModel loadTranslationModel = findLoadTranslationModelByB2BUnitAndProduct(customerProductNumber, b2bUnit);

                if (null != loadTranslationModel) {
                    JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, String.format(TRANSLATION_MODEL_FOUND, loadTranslationModel.getCatalogId(), b2bUnit.getUid(), customerProductNumber.toUpperCase()), JnjLoadTranslationServiceImpl.class);
                } else {
                    JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, String.format(TRANSLATION_MODEL_NOT_FOUND, b2bUnit.getUid(), customerProductNumber.toUpperCase()), JnjLoadTranslationServiceImpl.class);
                }

                return loadTranslationModel;
            } catch (final ModelLoadingException | ModelNotFoundException modelLoadingException) {
                JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, modelLoadingException.getMessage(), modelLoadingException, JnjLoadTranslationServiceImpl.class);
            }
        }

        JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), JnjLoadTranslationServiceImpl.class);

        return null;
    }

    private LoadTranslationModel findLoadTranslationModelByB2BUnitAndProduct(final String customerProductNumber, final JnJB2BUnitModel b2bUnit) {
        final LoadTranslationModel loadTranslationModel = new LoadTranslationModel();
        loadTranslationModel.setB2bUnit(b2bUnit);
        loadTranslationModel.setCustMaterialNum(customerProductNumber.toUpperCase());
        return flexibleSearchService.getModelByExample(loadTranslationModel);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public LoadTranslationModel getLoadTranslationModelByProductNumber(final JnJProductModel jnjProductModel, final JnJB2BUnitModel b2bUnit)
	{
		final String METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER = "getLoadTranslationModelByProductNumber()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		LoadTranslationModel loadTranslationModel = null;
		List<LoadTranslationModel> loadTranslationModelList;
		try
		{
			if (null != jnjProductModel)
			{
				loadTranslationModel = new LoadTranslationModel();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(
							Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
									+ Logging.HYPHEN + "Jnj Product Number " + jnjProductModel.getCode());
				}
				loadTranslationModel.setB2bUnit(b2bUnit);
				loadTranslationModel.setCatalogId(jnjProductModel.getCatalogId().toUpperCase());
				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Jnj product number.
				loadTranslationModelList = flexibleSearchService.getModelsByExample(loadTranslationModel);
				if (null != loadTranslationModelList && !loadTranslationModelList.isEmpty())
				{
					loadTranslationModel = loadTranslationModelList.get(0);
					LOGGER.info("Load Translation model with Customer material number  : " + loadTranslationModel.getCustMaterialNum()
							+ " is present for given jnjB2bUnitModel :" + b2bUnit.getUid() + " and for given catalog id  : "
							+ jnjProductModel.getCatalogId());

				}
				else
				{
					loadTranslationModel = null;
					LOGGER.info("Load Translation model not found for given : " + b2bUnit.getUid()
							+ " and for given catalog Id : " + jnjProductModel.getCatalogId());
				}
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.info(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
					+ Logging.HYPHEN + "Model Not Found exception occurred " + modelNotFoundException.getMessage());
			LOGGER.info(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
					+ Logging.HYPHEN + modelNotFoundException);
			loadTranslationModel = null;
		}
		catch (final ModelLoadingException modelLoadingException)
		{
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
					+ Logging.HYPHEN + "Model Loading exception occurred " + modelLoadingException.getMessage());
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
					+ Logging.HYPHEN + modelLoadingException);
			loadTranslationModel = null;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_NUMBER
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return loadTranslationModel;
	}

	@Override
	public JnjUomDTO getCustDelUomMapping(final JnJProductModel jnjProductModel)
	{
		return getCustDelUomMapping(jnjProductModel, jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnjUomDTO getCustDelUomMapping(final JnJProductModel jnjProductModel, final JnJB2BUnitModel b2bUnit)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMapping()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;

		final int numSUOM = StringUtils.isEmpty(jnjProductModel.getNumeratorSUOM()) ? 1
				: Integer.parseInt(jnjProductModel.getNumeratorSUOM());
		// Get the Load Translation Model on the basis of Jnj Product Model.
		LoadTranslationModel loadTranslationModel = null;
		try
		{
			loadTranslationModel = getLoadTranslationModelByProductNumber(jnjProductModel, b2bUnit);
			if (null != loadTranslationModel && null != loadTranslationModel.getCustDevUom()
					&& CollectionUtils.isNotEmpty(jnjProductModel.getUomDetails()))
			{
				for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
				{
					// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
					if (jnjUomConvModel.getUOM().equals(loadTranslationModel.getCustDevUom()))
					{
						custDelUOM = new JnjLaUomDTO();
						custDelUOM.setBaseUnitCount(
								jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
						custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
						custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
						final Integer salesNumerator = Integer.valueOf(jnjProductModel.getNumeratorSUOM());
						if (null != salesNumerator)
						{
							custDelUOM.setSalesUnitsCount(
									(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue())
											/ salesNumerator);
						}
					}
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("getCustDelUomMapping()" + Logging.HYPHEN + "Load translation object not found for "
					+ jnjProductModel.getCode() + Logging.HYPHEN + modelNotFound);
		}

		//Customer specific Delivery UOM not found - Fetching directly from Product Model
		if (null == custDelUOM && null != jnjProductModel.getDeliveryUnitOfMeasure())
		{
			custDelUOM = new JnjLaUomDTO();
			custDelUOM.setSalesUnitsCount(1);
			custDelUOM.setUnitDimension(jnjProductModel.getDeliveryUnitOfMeasure().getName());
			custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
			if (StringUtils.isNotEmpty(jnjProductModel.getNumeratorDUOM()))
			{
				custDelUOM.setSalesUnitsCount(Integer.parseInt(jnjProductModel.getNumeratorDUOM()) / numSUOM);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMapping()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnjUomDTO getCustDelUomEDIMappingForEAN(final JnJProductModel jnjProductModel, final String soldToNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomEDIMappingForEAN()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;
		// Get the Load Translation Model on the basis of Jnj Product Model.
		LoadTranslationModel loadTranslationModel = null;
		Integer salesUomConversionFactor = null;
		try
		{
			loadTranslationModel = getLoadTranslationModelByCatalogIdUnit(jnjProductModel, soldToNumber);

			if (CollectionUtils.isNotEmpty(jnjProductModel.getUomDetails()))
			{
				/* This for block is used to capture sales uom conversion factor. */
				for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
				{
					if (jnjUomConvModel.getUOM().equals(jnjProductModel.getUnit()))
					{
						salesUomConversionFactor = Integer
								.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
						LOGGER.info("salesUomConversionFactor for given product " + jnjProductModel.getCatalogId()
								+ " and for given unit " + jnjProductModel.getUnit().getCode() + " is : " + salesUomConversionFactor);
						break;
					}
				}
				/* If rounding profile available for this sold to translation */
				if (null != loadTranslationModel && null != loadTranslationModel.getCustDevUom())
				{
					LOGGER.info("loadTranslationModel has rounding profile define");
					for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
					{
						// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
						if (jnjUomConvModel.getUOM().equals(loadTranslationModel.getCustDevUom()) && null != salesUomConversionFactor)
						{
							custDelUOM = new JnjLaUomDTO();
							custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
							custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
							final Integer roundingProfileConversionFactor = Integer
									.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
							custDelUOM.setSalesUnitsCount(
									(salesUomConversionFactor.intValue()) / roundingProfileConversionFactor.intValue());
							LOGGER.info("roundingProfileConversionFactor for given product " + jnjProductModel.getCatalogId()
									+ " and for given unit " + loadTranslationModel.getCustDevUom().getCode() + " is : "
									+ roundingProfileConversionFactor);
							break;
						}
					}
				}
				/* if rounding profle is not available then we will fo for delivery numerator */
				else
				{
					LOGGER.info("loadTranslationModel does not have rounding profile define");
					for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
					{
						// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
						if (jnjUomConvModel.getUOM().equals(jnjProductModel.getDeliveryUnitOfMeasure())
								&& null != salesUomConversionFactor)
						{
							custDelUOM = new JnjLaUomDTO();
							custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
							custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
							final Integer deliveryUomConversionFactor = Integer
									.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
							custDelUOM
									.setSalesUnitsCount((salesUomConversionFactor.intValue()) / deliveryUomConversionFactor.intValue());
							LOGGER.info("deliveryUomConversionFactor for given product " + jnjProductModel.getCatalogId()
									+ " and for given unit " + jnjProductModel.getDeliveryUnitOfMeasure().getCode() + " is : "
									+ deliveryUomConversionFactor);
							break;
						}
					}
				}
			}
		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("getCustDelUomEDIMappingForEAN()" + Logging.HYPHEN + "Load translation object not found for "
					+ jnjProductModel.getCode() + Logging.HYPHEN + modelNotFound);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomEDIMappingForEAN()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadTranslationModel getLoadTranslationModelFile(final String customerProductNumber, final String soldToNumber)
	{
		final String METHOD_GET_LOAD_TRANSLATION_MODEL_FILE = "getLoadTranslationModelFile()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_FILE + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		LoadTranslationModel loadTranslationModel = null;
		if (StringUtils.isNotEmpty(customerProductNumber))
		{
			try
			{
				loadTranslationModel = new LoadTranslationModel();
				// Getting the JnJB2BUnitModel object by invoking the getDefaultB2BUnit method of JnjGetCurrentDefaultB2BUnitUtil.
				LOGGER.info("sold To number : " + soldToNumber);
				final JnJB2BUnitModel jnjB2bUnitModel = jnjOrderDao.fetchAllSoldToNumberForFile(soldToNumber);
				loadTranslationModel.setB2bUnit(jnjB2bUnitModel);
				loadTranslationModel.setCustMaterialNum(customerProductNumber.toUpperCase());
				LOGGER.info("querying Load translation model for given b2b unit id : " + soldToNumber
						+ ", and customer material number " + customerProductNumber.toUpperCase());
				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Customer product number.
				loadTranslationModel = flexibleSearchService.getModelByExample(loadTranslationModel);
				if (null != loadTranslationModel)
				{
					LOGGER.info("Load translation model found for  given b2b unit id : " + soldToNumber
							+ ", and customer material number " + customerProductNumber.toUpperCase());
				}
				else
				{
					LOGGER.info("Load translation model NOT found for  given b2b unit id : " + soldToNumber
							+ ", and customer material number " + customerProductNumber.toUpperCase());
				}
			}
			catch (final ModelNotFoundException modelNotFoundException)
			{
				LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_FILE
						+ Logging.HYPHEN + "Model Not Found exception occurred " + modelNotFoundException.getMessage());
				LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_FILE
						+ Logging.HYPHEN + modelNotFoundException);
				loadTranslationModel = null;
			}
			catch (final ModelLoadingException modelLoadingException)
			{
				LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_FILE
						+ Logging.HYPHEN + "Model Loading exception occurred " + modelLoadingException.getMessage());
				LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_FILE
						+ Logging.HYPHEN + modelLoadingException);
				loadTranslationModel = null;
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_FILE + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return loadTranslationModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadTranslationModel getLoadTranslationModelEDI(final JnJProductModel jnjProductModel,
			final JnJB2BUnitModel jnJB2bUnitModel)
	{
		final String METHOD_GET_LOAD_TRANSLATION_MODEL_EDI = "getLoadTranslationModelEDI()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		LoadTranslationModel loadTranslationModel = null;
		try
		{
			if (null != jnjProductModel)
			{
				loadTranslationModel = new LoadTranslationModel();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI
							+ Logging.HYPHEN + "Jnj Product Number " + jnjProductModel.getCode());
				}
				loadTranslationModel.setB2bUnit(jnJB2bUnitModel);
				loadTranslationModel.setProductId(jnjProductModel);
				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Jnj product number.
				loadTranslationModel = flexibleSearchService.getModelByExample(loadTranslationModel);
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI + Logging.HYPHEN
					+ "Model Not Found exception occurred " + modelNotFoundException.getMessage());
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI + Logging.HYPHEN
					+ modelNotFoundException);
			loadTranslationModel = null;
		}
		catch (final ModelLoadingException modelLoadingException)
		{
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI + Logging.HYPHEN
					+ "Model Loading exception occurred " + modelLoadingException.getMessage());
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI + Logging.HYPHEN
					+ modelLoadingException);
			loadTranslationModel = null;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_EDI + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return loadTranslationModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadTranslationModel getLoadTranslationModelByCatalogIdUnit(final JnJProductModel jnjProductModel,
			final String validSoldToNumber)
	{
		final String METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT = "getLoadTranslationModelByCatalogIdUnit()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		LoadTranslationModel loadTranslationModel = null;
		try
		{
			if (null != jnjProductModel)
			{
				loadTranslationModel = new LoadTranslationModel();
				// Getting the JnJB2BUnitModel object by invoking the getDefaultB2BUnit method of JnjGetCurrentDefaultB2BUnitUtil.
				final JnJB2BUnitModel jnjB2bUnitModel = jnjOrderDao.fetchAllSoldToNumberForFile(validSoldToNumber);
				LOGGER.info(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
						+ Logging.HYPHEN + "Jnj Product Number " + jnjProductModel.getCode());
				loadTranslationModel.setB2bUnit(jnjB2bUnitModel);
				loadTranslationModel.setCatalogId(jnjProductModel.getCatalogId());
				LOGGER.info(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
						+ Logging.HYPHEN + "Jnj Product Number : " + jnjProductModel.getCode() + " , AND b2b unit is : "
						+ jnjB2bUnitModel.getUid());
				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Jnj product number.
				loadTranslationModel = flexibleSearchService.getModelByExample(loadTranslationModel);
				if (null != loadTranslationModel)
				{
					LOGGER.info("load translation model is  present for given jnjB2bUnitModel :" + jnjB2bUnitModel.getUid()
							+ " and for given paroduct catalog id : " + jnjProductModel.getCatalogId());
				}
				else
				{
					LOGGER.info("load translation model is  not present for given jnjB2bUnitModel :" + jnjB2bUnitModel.getUid()
							+ " and for given paroduct catalog id : " + jnjProductModel.getCatalogId());
				}
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
					+ Logging.HYPHEN + "Model Not Found exception occurred " + modelNotFoundException.getMessage());
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
					+ Logging.HYPHEN + modelNotFoundException);
			loadTranslationModel = null;
		}
		catch (final ModelLoadingException modelLoadingException)
		{
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
					+ Logging.HYPHEN + "Model Loading exception occurred " + modelLoadingException.getMessage());
			LOGGER.warn(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
					+ Logging.HYPHEN + modelLoadingException);
			loadTranslationModel = null;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.LOAD_TRANSLATION_SERVICE + Logging.HYPHEN + METHOD_GET_LOAD_TRANSLATION_MODEL_BY_PRODUCT_UNIT
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return loadTranslationModel;
	}

	@Override
	public JnjUomDTO getCustDelUomMappingForAlianca(final JnJProductModel jnjProductModel,
			final LoadTranslationModel loadTranslationModel)
	{
		final String METHOD_NAME = "getCustDelUomMappingForAlianca ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;


		if (null != loadTranslationModel.getNumerator())
		{
			custDelUOM = new JnjLaUomDTO();
			custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
			custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
			Integer deliveryNumerator = null;
			Integer salesNumerator = null;
			if (null != jnjProductModel.getUomDetails())
			{
				for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
				{
					// Compare the  Unit Model of the Jnj Uom Conversion Model with delivery unit model for quantity check.
					if (jnjUomConvModel.getUOM().equals(jnjProductModel.getDeliveryUnitOfMeasure()))
					{
						deliveryNumerator = Integer
								.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
						LOGGER.info("value of delivery numerator :" + deliveryNumerator + " , for given product : "
								+ jnjProductModel.getCatalogId() + ", and for given delivery unit of measure"
								+ jnjProductModel.getDeliveryUnitOfMeasure());
					}
					if (jnjUomConvModel.getUOM().equals(jnjProductModel.getUnit()))
					{
						salesNumerator = Integer
								.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
						LOGGER.info("value of sales numerator :" + deliveryNumerator + " , for given product : "
								+ jnjProductModel.getCatalogId() + ", and for given sales unit of measure" + jnjProductModel.getUnit());
					}

				}
				if (null != deliveryNumerator && null != salesNumerator)
				{
					custDelUOM.setSalesUnitsCount(
							(loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
									/ deliveryNumerator.intValue());
					custDelUOM.setFinalUnitCount(
							(loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
									/ salesNumerator.intValue());

					LOGGER.info("value of sales unit count  :"
							+ ((double) loadTranslationModel.getNumerator().intValue()
									/ loadTranslationModel.getDenominator().intValue()) / deliveryNumerator.intValue()
							+ " and value of final unit count :" + ((double) loadTranslationModel.getNumerator().intValue()
									/ loadTranslationModel.getDenominator().intValue()) / salesNumerator.intValue());
				}
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}

	@Override
	public JnjUomDTO getCustDelUomMappingForAliancaMM(final JnJProductModel jnjProductModel, final Integer receivedUOMNumerator)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingForAliancaMM()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;
		custDelUOM = new JnjLaUomDTO();
		custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
		custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
		Integer deliveryNumerator = null;
		Integer salesNumerator = null;
		if (null != jnjProductModel.getUomDetails())
		{
			for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
			{
				// Compare the  Unit Model of the Jnj Uom Conversion Model with delivery unit model for quantity check.
				if (jnjUomConvModel.getUOM().equals(jnjProductModel.getDeliveryUnitOfMeasure()))
				{
					deliveryNumerator = Integer
							.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
					LOGGER.info("value of delivery numerator :" + deliveryNumerator + " , for given product : "
							+ jnjProductModel.getCatalogId() + ", and for given delivery unit of measure"
							+ jnjProductModel.getDeliveryUnitOfMeasure());
				}
				if (jnjUomConvModel.getUOM().equals(jnjProductModel.getUnit()))
				{
					salesNumerator = Integer
							.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
					LOGGER.info("value of sales numerator :" + deliveryNumerator + " , for given product : "
							+ jnjProductModel.getCatalogId() + ", and for given sales unit of measure" + jnjProductModel.getUnit());
				}
			}
			if (null != deliveryNumerator && null != salesNumerator)
			{
				custDelUOM.setSalesUnitsCount(receivedUOMNumerator.intValue() / deliveryNumerator.intValue());
				custDelUOM.setFinalUnitCount(receivedUOMNumerator.intValue() / salesNumerator.intValue());
				LOGGER.info("value of sales unit count  : " + (double) receivedUOMNumerator.intValue() / deliveryNumerator.intValue()
						+ " and value of final unit count : " + (double) receivedUOMNumerator.intValue() / salesNumerator.intValue());
			}
		}


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingForAliancaMM()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}


	@Override
	public JnjUomDTO getCustDelUomMappingForSaoLuiz(final JnJProductModel jnjProductModel,
			final LoadTranslationModel loadTranslationModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingForSaoLuiz()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;


		if (null != loadTranslationModel && null != loadTranslationModel.getNumerator())
		{
			custDelUOM = new JnjLaUomDTO();
			custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
			custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
			Integer salesNumerator = null;
			if (null != jnjProductModel.getUomDetails())
			{
				for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
				{

					if (jnjUomConvModel.getUOM().equals(jnjProductModel.getUnit()))
					{
						salesNumerator = Integer
								.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
					}

				}
				if (null != salesNumerator)
				{
					custDelUOM.setFinalUnitCount(
							(loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
									/ salesNumerator.intValue());
				}
			}

		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingForSaoLuiz()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}

	@Override
	public JnjUomDTO getCustDelUomMappingForSaoLuizMM(final JnJProductModel jnjProductModel, final Integer receivedUOMNumerator)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingForSaoLuizMM()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;
		custDelUOM = new JnjLaUomDTO();
		custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
		custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
		Integer salesNumerator = null;
		if (null != jnjProductModel.getUomDetails())
		{
			for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
			{
				if (jnjUomConvModel.getUOM().equals(jnjProductModel.getUnit()))
				{
					salesNumerator = Integer
							.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
				}
			}
			if (null != salesNumerator)
			{
				custDelUOM.setFinalUnitCount(receivedUOMNumerator.intValue() / salesNumerator.intValue());
			}
		}


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingForSaoLuizMM()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}


	@Override
	public JnjUomDTO getCustDelUomMappingForAlbert(final JnJProductModel jnjProductModel,
			final LoadTranslationModel loadTranslationModel)
	{
		final String METHOD_NAME = "getCustDelUomMappingForAlbert ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnjLaUomDTO custDelUOM = null;
		if (null != loadTranslationModel && null != loadTranslationModel.getNumerator())
		{
			custDelUOM = new JnjLaUomDTO();
			custDelUOM.setSalesUnitDimension(jnjProductModel.getUnit().getName());
			custDelUOM.setSalesUnitCode(jnjProductModel.getUnit().getCode());
			Integer deliveryNumerator = null;
			Integer salesNumerator = null;
			for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
			{
				// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
				if (jnjUomConvModel.getUOM().equals(jnjProductModel.getDeliveryUnitOfMeasure()))
				{
					deliveryNumerator = Integer
							.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
					LOGGER.info("value of delivery numerator :" + deliveryNumerator + " for given delivery unit of measure : "
							+ jnjProductModel.getDeliveryUnitOfMeasure().getCode());
				}
				if (jnjUomConvModel.getUOM().equals(jnjProductModel.getUnit()))
				{
					salesNumerator = Integer
							.valueOf(jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue());
					LOGGER.info("value of sales numerator :" + salesNumerator + " for given sales unit of measure : "
							+ jnjProductModel.getUnit().getCode());
				}

			}
			if (null != deliveryNumerator && null != salesNumerator)
			{
				custDelUOM.setSalesUnitsCount(
						(loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
								/ deliveryNumerator.intValue());
				LOGGER.info("value of sales unit count  :"
						+ (loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
								/ deliveryNumerator.intValue());
				custDelUOM.setFinalUnitCount(
						(loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
								/ salesNumerator.intValue());
				LOGGER.info("value of final unit count  :"
						+ ((double) loadTranslationModel.getNumerator().intValue() / loadTranslationModel.getDenominator().intValue())
								/ salesNumerator.intValue());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return custDelUOM;
	}



	@Override
	public void getCustDelUomMappingByRoundigForSaoLuiz(final JnJProductModel jnjProductModel,
			final LoadTranslationModel loadTranslationModel, final JnjUomDTO jnjUomDTO, final Integer receivedUOMNumerator)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingByRoundigForSaoLuiz()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (null != loadTranslationModel && null != loadTranslationModel.getCustDevUom())
		{
			final UnitModel unitModel = loadTranslationModel.getCustDevUom();
			for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
			{
				// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
				if (jnjUomConvModel.getUOM().equals(unitModel))
				{
					((JnjLaUomDTO) jnjUomDTO).setSalesUnitsCount(receivedUOMNumerator.intValue()
							/ (jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue()));
				}
			}
		}
		//Customer specific Delivery UOM not found - Fetching directly from Product Model
		else
		{
			for (final JnjUomConversionModel jnjUomConvModel : jnjProductModel.getUomDetails())
			{
				// Compare the Unit Model of the Jnj Uom Conversion Model with Unit Model of Load Translation Model.
				if (jnjUomConvModel.getUOM().equals(jnjProductModel.getDeliveryUnitOfMeasure()))
				{
					((JnjLaUomDTO) jnjUomDTO).setSalesUnitsCount(receivedUOMNumerator.intValue()
							/ (jnjUomConvModel.getNumerator().intValue() / jnjUomConvModel.getDenominator().intValue()));
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustDelUomMappingByRoundigForSaoLuiz()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnjIntLoadTranslationModel> getJnjIntLoadTranslationModels(final RecordStatus recordStatus,
			final String customMaterialNumber, final String idocNumber, final String jnjProductCode, final String fileName)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjIntLoadTranslationModels()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		List<JnjIntLoadTranslationModel> jnjIntLoadTranslationModelList = null;
		final JnjIntLoadTranslationModel tempJnJIntLoadTranslationModel = new JnjIntLoadTranslationModel();
		if (null != recordStatus)
		{
			tempJnJIntLoadTranslationModel.setRecordStatus(recordStatus);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getJnjIntLoadTranslationModels()" + Logging.HYPHEN + "JnjIntLoadTranslationModel with status: "
						+ recordStatus.toString());
			}
		}
		if (StringUtils.isNotEmpty(customMaterialNumber))
		{
			tempJnJIntLoadTranslationModel.setCustomerNumber(customMaterialNumber);
		}
		if (StringUtils.isNotEmpty(idocNumber))
		{
			tempJnJIntLoadTranslationModel.setIdocNumber(idocNumber);
		}
		if (StringUtils.isNotEmpty(jnjProductCode))
		{
			tempJnJIntLoadTranslationModel.setJnjProductCode(jnjProductCode);
		}
		if (StringUtils.isNotEmpty(fileName))
		{
			tempJnJIntLoadTranslationModel.setFileName(fileName);
		}

		try
		{
			jnjIntLoadTranslationModelList = flexibleSearchService.getModelsByExample(tempJnJIntLoadTranslationModel);
			if (!jnjIntLoadTranslationModelList.isEmpty() && LOGGER.isDebugEnabled())
			{
				LOGGER.debug(
						"getJnjIntLoadTranslationModels()" + Logging.HYPHEN + "JnjIntLoadTranslationModel with customMaterialNumber: "
								+ customMaterialNumber + "and idocNumber: " + idocNumber + "and jnjProductCode: " + jnjProductCode
								+ "and fileName: " + fileName + " found in Hybris. Returning Existing Models");
			}
			else if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(
						"getJnjIntLoadTranslationModels()" + Logging.HYPHEN + "JnjIntLoadTranslationModel with customMaterialNumber: "
								+ customMaterialNumber + "and idocNumber: " + idocNumber + "and jnjProductCode: " + jnjProductCode
								+ "and fileName: " + fileName + " not found in Hybris. Returning Empty List.");
			}
		}
		catch (ModelNotFoundException | IllegalArgumentException exception)
		{
			LOGGER.error(
					"getJnjIntLoadTranslationModels()" + Logging.HYPHEN + "JnjIntLoadTranslationModel with customMaterialNumber: "
							+ customMaterialNumber + "and idocNumber: " + idocNumber + "and jnjProductCode: " + jnjProductCode
							+ "and fileName: " + fileName + " not found in Hybris. Returning Null - " + exception);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjIntLoadTranslationModels()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjIntLoadTranslationModelList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveIntLoadTranslationModel(final JnjIntLoadTranslationModel jnjIntLoadTranslationModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveIntLoadTranslationModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		if (null != jnjIntLoadTranslationModel)
		{
			try
			{
				modelService.save(jnjIntLoadTranslationModel);
				recordStatus = true;
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("saveIntLoadTranslationModel()" + Logging.HYPHEN
							+ "Model Successfully saved having customer material number is "
							+ jnjIntLoadTranslationModel.getCustomerNumber());
				}
			}
			catch (final ModelSavingException exception)
			{
				LOGGER.error("saveIntLoadTranslationModel()" + Logging.HYPHEN
						+ "Model is not saved successfully which has customer material number is "
						+ jnjIntLoadTranslationModel.getCustomerNumber() + "and the error message is " + exception.getMessage());
				throw exception;
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveIntLoadTranslationModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void getJnjIntLoadTranslatiomModelWithEntries(final RecordStatus recordStatus)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjIntLoadTranslatiomModelWithEntries()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (null != recordStatus)
		{
			final List<JnjIntLoadTranslationModel> jnjIntLoadTranslationModelList = getJnjIntLoadTranslatiomModelForRemove(
					recordStatus);
			// Check if the jnjIntInvoiceModelsList list is empty or not
			LOGGER.debug("JnjIntLoadTranslationModel is being deleted for record status " + recordStatus + "from"
					+ jnjIntLoadTranslationModelList);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjIntInvoiceModelsWithEntries()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	/**
	 *
	 * This method is used to get the JnjIntLoadTranslatiomModel for Removal status on the basis of record Status
	 *
	 * @param recordStatus
	 *           RecordStatus
	 * @return jnjIntLoadTranslationModelList List of JnjIntLoadTranslationModel
	 */
	@Override
	public List<JnjIntLoadTranslationModel> getJnjIntLoadTranslatiomModelForRemove(final RecordStatus recordStatus)
	{
		return jnJCrossReferenceDao.getJnjIntLoadTranslatiomModelForRemove(recordStatus);
	}

}
