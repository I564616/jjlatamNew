/**
 *
 */
package com.jnj.la.core.services;

import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnjIntLoadTranslationModel;
import com.jnj.la.core.model.LoadTranslationModel;

import java.util.List;


/**
 * The interface class for Load Translation
 *
 * @author sanchit.a.kumar
 *
 */
public interface JnjLoadTranslationService
{
	boolean saveloadTranslationData(final LoadTranslationModel loadTranslationModel) throws BusinessException;

	/**
	 * The getLoadTranslationModel method retrieves the Load Translation Model object by using customer product number.
	 *
	 * @param customerProductNumber
	 *           the customer product number
	 * @param b2bUnit
	 *           b2b Unit
	 * @return the load translation model by customer number
	 */
	public LoadTranslationModel getLoadTranslationModel(final String customerProductNumber, final JnJB2BUnitModel b2bUnit);

	/**
	 * The getLoadTranslationModel method retrieves the Load Translation Model object by using jnj product model.
	 *
	 * @param jnjProductModel
	 *           the jnj product model
	 * @param b2bUnit
	 *           B2B Unit
	 * @return the load translation model by customer number
	 */
	public LoadTranslationModel getLoadTranslationModelByProductNumber(final JnJProductModel jnjProductModel, final JnJB2BUnitModel b2bUnit);

	/**
	 * Gets the Customer Delivery Uom mapping by using Jnj Product Model.
	 *
	 * @param jnjProductModel
	 *           the jnj product model
	 * @param b2bUnit
	 *           B2B Unit
	 * @return the cust del uom mapping
	 */
	JnjUomDTO getCustDelUomMapping(final JnJProductModel jnjProductModel, final JnJB2BUnitModel b2bUnit);

	/**
	 * Gets the Customer Delivery Uom mapping by using Jnj Product Model.
	 *
	 * @param jnjProductModel
	 *           the jnj product model
	 * @return the cust del uom mapping
	 */
	JnjUomDTO getCustDelUomMapping(final JnJProductModel jnjProductModel);

	/**
	 * Gets the jnj int load translation models.
	 *
	 * @param recordStatus
	 *           the record status
	 * @param customMaterialNumber
	 *           the custom material number
	 * @param idocNumber
	 *           the idoc number
	 * @param jnjProductCode
	 *           the jnj product code
	 * @return the jnj int load translation models
	 */
	public List<com.jnj.la.core.model.JnjIntLoadTranslationModel> getJnjIntLoadTranslationModels(final RecordStatus recordStatus,
			final String customMaterialNumber, final String idocNumber, final String jnjProductCode, final String fileName);

	/**
	 * Save int load translation model.
	 *
	 * @param jnjIntLoadTranslationModel
	 *           the jnj int load translation model
	 * @return true, if successful
	 */
	public boolean saveIntLoadTranslationModel(final JnjIntLoadTranslationModel jnjIntLoadTranslationModel);

	/**
	 * Gets the jnj int load translation models with entries.
	 *
	 * @param recordStatus
	 *           the record status
	 */
	public void getJnjIntLoadTranslatiomModelWithEntries(final RecordStatus recordStatus);

	/**
	 * Gets the jnj int invoice order models by using record status .
	 *
	 * @param recordStatus
	 *           the record status
	 */
	public List<JnjIntLoadTranslationModel> getJnjIntLoadTranslatiomModelForRemove(final RecordStatus recordStatus);

	/**
	 * Gets the Customer Delivery Uom mapping by using Jnj Product Model.
	 *
	 * @param jnjProductModel
	 *           the jnj product model
	 * @param soldToNumber
	 *           String
	 * @return the cust del uom mapping
	 */
	public JnjUomDTO getCustDelUomEDIMappingForEAN(final JnJProductModel jnjProductModel, final String soldToNumber);

	/**
	 * @param jnjProductModel
	 * @param jnJB2bUnitModel
	 * @return the load translation model by customer number
	 */
	LoadTranslationModel getLoadTranslationModelEDI(JnJProductModel jnjProductModel, JnJB2BUnitModel jnJB2bUnitModel);

	/**
	 * @param customerProductNumber
	 * @param soldToNumber
	 * @return the load translation model by customer number
	 */
	LoadTranslationModel getLoadTranslationModelFile(String customerProductNumber, String soldToNumber);

	/**
	 * @param jnjProductModel
	 * @param validSoldToNumber
	 * @return the load translation model by customer number
	 */
	LoadTranslationModel getLoadTranslationModelByCatalogIdUnit(JnJProductModel jnjProductModel, String validSoldToNumber);

	/**
	 * @param jnjProductModel
	 * @param loadTranslationModel
	 * @return the load translation model by customer number
	 */
	JnjUomDTO getCustDelUomMappingForAlbert(JnJProductModel jnjProductModel, LoadTranslationModel loadTranslationModel);

	/**
	 * @param jnjProductModel
	 * @param loadTranslationModel
	 * @return the load translation model by customer number
	 */
	JnjUomDTO getCustDelUomMappingForAlianca(JnJProductModel jnjProductModel, LoadTranslationModel loadTranslationModel);

	/**
	 * @param jnjProductModel
	 * @param receivedUOMNumerator
	 * @return the load translation model by customer number
	 */
	JnjUomDTO getCustDelUomMappingForAliancaMM(JnJProductModel jnjProductModel, Integer receivedUOMNumerator);

	/**
	 * @param jnjProductModel
	 * @param receivedUOMNumerator
	 * @return JnjUom DTO
	 */
	JnjUomDTO getCustDelUomMappingForSaoLuizMM(JnJProductModel jnjProductModel, Integer receivedUOMNumerator);

	/**
	 * @param jnjProductModel
	 * @param loadTranslationModel
	 * @return JnjUom DTO
	 */
	JnjUomDTO getCustDelUomMappingForSaoLuiz(JnJProductModel jnjProductModel, LoadTranslationModel loadTranslationModel);

	/**
	 * @param jnjProductModel
	 * @param receivedUOMNumerator
	 * @param loadTranslationModel
	 */
	void getCustDelUomMappingByRoundigForSaoLuiz(JnJProductModel jnjProductModel, LoadTranslationModel loadTranslationModel,
			JnjUomDTO jnjUomDTO, Integer receivedUOMNumerator);


}
