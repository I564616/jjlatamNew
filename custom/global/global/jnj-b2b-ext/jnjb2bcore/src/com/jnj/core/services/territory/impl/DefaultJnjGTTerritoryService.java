/**
 *
 */
package com.jnj.core.services.territory.impl;


import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.territory.JnjGTTerritoryDao;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivProdMappingModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author komal.sehgal
 *
 */
public class DefaultJnjGTTerritoryService implements JnjGTTerritoryService
{


	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTTerritoryService.class);

	@Autowired
	protected JnjGTTerritoryDao jnjGTTerritoryDao;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected ModelService modelService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;


	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	
	public JnjGTTerritoryDao getJnjGTTerritoryDao() {
		return jnjGTTerritoryDao;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	/*
	 * (non-Javadoc) This methdo is used for getting TerriToryDivison Modle Bu Uid and Source ID.
	 * 
	 * @param : Uid and SourceId
	 */
	@Override
	public JnjGTTerritoryDivisonModel getTerritoryDivisonByUid(final String id, final String sourceId)
	{
		return jnjGTTerritoryDao.getTerritoryDivisonByUid(id, sourceId);
	}

	/*
	 * (non-Javadoc) This method is used for getting TerritoryDivisonCust Model by Example.
	 * 
	 * @see com.jnj.core.services.territory.JnjGTTerritoryService#getTerritoryDivCustRelByExample()
	 */
	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRel(final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel)
	{
		List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelList = null;
		try
		{
			jnjGTTerritoryDivCustRelList = flexibleSearchService.getModelsByExample(jnjGTTerritoryDivCustRelModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("JnjGTTerritoryDivCustRelModel was not found with the loaded attributes");
		}
		return jnjGTTerritoryDivCustRelList;

	}


	/* This method is used for saving JnjGTTerritoryDivisonModel */

	@Override
	public boolean saveTerritoryDivisonModel(final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel)
	{
		boolean savedModel = false;
		try
		{
			modelService.save(jnjGTTerritoryDivisonModel);
			modelService.refresh(jnjGTTerritoryDivisonModel);
			savedModel = true;
		}
		catch (final ModelSavingException exp)
		{
			throw exp;
		}
		return savedModel;
	}

	/* This method is used for saving JnjGTTerritoryDivCustRelModel */

	@Override
	public boolean saveTerritoryDivisonCustModel(final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel)
	{
		boolean savedModel = false;
		try
		{
			modelService.save(jnjGTTerritoryDivCustRelModel);
			modelService.refresh(jnjGTTerritoryDivCustRelModel);
			savedModel = true;
		}
		catch (final ModelSavingException exp)
		{
			LOGGER.error("JnjGTTerritoryDivCustRelModel was not saved with the loaded attributes");
			throw exp;
		}
		return savedModel;
	}

	/* This method is used for getting all the TerritoryDivCustRelModel */
	

	/* This method is used for removing the TerritoryDivModel */
	@Override
	public boolean removeTerritoryDivisonModel(final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel)
	{
		boolean removedModel = false;
		try
		{
			modelService.remove(jnjGTTerritoryDivisonModel);
			removedModel = true;
		}
		catch (final ModelRemovalException exception)
		{
			LOGGER.error("Model was not removed");
			throw exception;
		}
		return removedModel;
	}

	/* This method is used for removing TerritoryDivCustRelModel */
	@Override
	public boolean removeTerritoryDivisonCustRelModel(final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList)
	{
		boolean removedModel = false;
		try
		{
			modelService.removeAll(jnjGTTerritoryDivCustRelModelList);
			removedModel = true;
		}
		catch (final ModelRemovalException exception)
		{
			LOGGER.error("Model was not removed");
			throw exception;
		}
		return removedModel;
	}


	/* This method is used for saving JnjGTTerritoryDivProdMappingModel */
	@Override
	public boolean saveTerritoryDivisonProdModel(final JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMappingModel)
	{
		boolean savedModel = false;
		try
		{
			modelService.save(jnjGTTerritoryDivProdMappingModel);
			savedModel = true;
		}
		catch (final ModelSavingException exp)
		{
			LOGGER.error("Model was not saved with the loaded attributes");
			throw exp;
		}
		return savedModel;
	}

	/* This method is used for saving JnjGTTerritoryDivProdMappingModel Model By Example */
	@Override
	public JnjGTTerritoryDivProdMappingModel getJnjGTTerritoryDivProdMappingModel(
			final JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMappingModel)
	{

		JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMapping = null;
		try
		{
			jnjGTTerritoryDivProdMapping = flexibleSearchService.getModelByExample(jnjGTTerritoryDivProdMappingModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return jnjGTTerritoryDivProdMapping;
	}




	/**
	 * This method is used for saving JnjGTTerritoryDivProdMappingModel
	 *
	 * @param jnjGTTerritoryDivProdMappingModel
	 * @return
	 */
	@Override
	public boolean removeJnjGTTerritoryDivProdMappingModel(final JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMappingModel)
	{

		boolean removedModel = false;
		try
		{
			modelService.remove(jnjGTTerritoryDivProdMappingModel);
			removedModel = true;

		}
		catch (final ModelRemovalException exception)
		{
			LOGGER.error("Model was not removed");
			throw exception;
		}
		return removedModel;

	}

	//Invalidating all the TerritoryDivisonModelwhich doesnt fall in valid dates
	@Override
	public void removeInvaildTerritoryDivisonModel()
	{
		final List<JnjGTTerritoryDivisonModel> territoryDivisonModelList = getAllJnjGTTerritoryDivModel(Jnjb2bCoreConstants.SalesAlignment.MDD);
		for (final JnjGTTerritoryDivisonModel territoryDivisonModel : territoryDivisonModelList)
		{
			if (territoryDivisonModel.getEndDate() != null && territoryDivisonModel.getEffectiveDate() != null)
			{
				if (JnjGTCoreUtil.compareDates(territoryDivisonModel.getEffectiveDate(), territoryDivisonModel.getEndDate()))
				{
					//validating TerritoryDivisonModel
					territoryDivisonModel.setInvalidated(Boolean.FALSE);
					saveTerritoryDivisonModel(territoryDivisonModel);
					//Validating all the Cust Model.
					final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
					jnjGTTerritoryDivCustRelModel.setSource(territoryDivisonModel);
					final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
					//Entering the relationships of the User Or B2BUnit
					for (final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRel : jnjGTTerritoryDivCustRelModelList)
					{
						Date endDate = jnjGTTerritoryDivCustRel.getEndDate();
						Date effectiveDate = jnjGTTerritoryDivCustRel.getEffectiveDate();
						if (endDate == null)
						{
							final Calendar calendar = Calendar.getInstance();
							calendar.add(Calendar.DAY_OF_MONTH, 1);
							endDate = calendar.getTime();
						}
						if (effectiveDate == null)
						{
							final Calendar calendar = Calendar.getInstance();
							calendar.add(Calendar.DAY_OF_MONTH, -1);
							effectiveDate = calendar.getTime();
						}
						if (JnjGTCoreUtil.compareDates(effectiveDate, endDate))
						{
							//Validating the  TerritoryDivisonCustModel
							jnjGTTerritoryDivCustRel.setInvalidated(Boolean.FALSE);
							saveTerritoryDivisonCustModel(jnjGTTerritoryDivCustRel);
							//Adding the  Territory Model  From the User Or B2BUnit
							final PrincipalModel pricipalModel = jnjGTTerritoryDivCustRel.getTarget();
							final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
							groups.addAll(pricipalModel.getGroups());
							groups.add(jnjGTTerritoryDivCustRel.getSource());
							pricipalModel.setGroups(groups);
							jnjGTB2BUnitService.saveItemModel(pricipalModel);
						}
					}
				}
				else
				{
					//Invalidating the  TerritoryDivisonModel
					territoryDivisonModel.setInvalidated(Boolean.TRUE);
					saveTerritoryDivisonModel(territoryDivisonModel);
					//Getting the  TerritoryDivisonCustModel
					final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
					jnjGTTerritoryDivCustRelModel.setSource(territoryDivisonModel);
					final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
					for (final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRel : jnjGTTerritoryDivCustRelModelList)
					{
						//Invalidating the  TerritoryDivisonCustModel
						jnjGTTerritoryDivCustRel.setInvalidated(Boolean.TRUE);
						saveTerritoryDivisonCustModel(jnjGTTerritoryDivCustRel);
						//Removing the Invalidated  Territory Model  From the User Or B2BUnit
						final PrincipalModel pricipalModel = jnjGTTerritoryDivCustRel.getTarget();
						Set<PrincipalGroupModel> groups = null;
						groups = new HashSet<PrincipalGroupModel>();
						groups.addAll(pricipalModel.getGroups());
						groups.remove(jnjGTTerritoryDivCustRel.getSource());
						pricipalModel.setGroups(groups);
						jnjGTB2BUnitService.saveItemModel(pricipalModel);
					}

				}
			}

		}
	}

	//Invalidating all the TerritoryDivisonCustModel which doesnt fall in valid dates

	@Override
	public void removeInvaildTerritoryDivisonCustModel()
	{
		final List<JnjGTTerritoryDivCustRelModel> territoryDiviCustModelList = getAllJnjGTTerritoryDivCustRelModel();
		for (final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel : territoryDiviCustModelList)
		{

			Date endDate = jnjGTTerritoryDivCustRelModel.getEndDate();
			Date effectiveDate = jnjGTTerritoryDivCustRelModel.getEffectiveDate();
			if (endDate == null)
			{
				final Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endDate = calendar.getTime();
			}
			if (effectiveDate == null)
			{
				final Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				effectiveDate = calendar.getTime();
			}
			if (JnjGTCoreUtil.compareDates(effectiveDate, endDate))
			{
				jnjGTTerritoryDivCustRelModel.setInvalidated(Boolean.FALSE);
			}
			else
			{
				//Setting the flag as true for invalid Model
				jnjGTTerritoryDivCustRelModel.setInvalidated(Boolean.TRUE);
				//Removing the
				final PrincipalModel pricipalModel = jnjGTTerritoryDivCustRelModel.getTarget();
				final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
				groups.addAll(pricipalModel.getGroups());
				groups.remove(jnjGTTerritoryDivCustRelModel.getSource());
				pricipalModel.setGroups(groups);
				jnjGTB2BUnitService.saveItemModel(pricipalModel);
			}

		}
	}

	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryDivCustRelWithNullTarget(final PK territoryDivPk, final String wwid,
			final String ucn)
	{
		return jnjGTTerritoryDao.getTerritoryDivCustRelWithNullTarget(territoryDivPk, wwid, ucn);
	}


	@Override
	public void alignCustomerWithTerritory()
	{
		final List<JnjGTTerritoryDivCustRelModel> custRelList = jnjGTTerritoryDao.getTerritoryDivCustRelWithNullTarget();
		for (final JnjGTTerritoryDivCustRelModel cutRel : custRelList)
		{
			final String wwid = cutRel.getWwid();
			if (wwid != null)
			{
				final JnJB2bCustomerModel customer = new JnJB2bCustomerModel();
				customer.setWwid(wwid);
				final List<JnJB2bCustomerModel> JnJB2bCustomerModelList = jnjGTCustomerService
						.getJnJB2bCustomerModels(customer);
				if (CollectionUtils.isNotEmpty(JnJB2bCustomerModelList))
				{
					for (final JnJB2bCustomerModel jnjgtb2bCustomer : JnJB2bCustomerModelList)
					{
						alignCustomer(jnjgtb2bCustomer, cutRel);
					}
				}
			}
		}
	}

	/**
	 *
	 */
	protected void alignCustomer(final JnJB2bCustomerModel jnjgtb2bCustomer, final JnjGTTerritoryDivCustRelModel cutRel)
	{
		cutRel.setTarget(jnjgtb2bCustomer);
		modelService.save(cutRel);
		final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
		groups.addAll(jnjgtb2bCustomer.getGroups());
		groups.add(cutRel.getSource());
		jnjgtb2bCustomer.setGroups(groups);
		modelService.save(jnjgtb2bCustomer);
	}

	@Override
	public void alignCustomerWithTerritoryWithWwid(final String wwid, final JnJB2bCustomerModel customer)
	{
		final List<JnjGTTerritoryDivCustRelModel> custRelList = jnjGTTerritoryDao.getTerritoryDivCustRelWithWwidAndNullTarget(wwid);
		for (final JnjGTTerritoryDivCustRelModel cutRel : custRelList)
		{
			alignCustomer(customer, cutRel);
		}
	}


	@Override
	public JnjGTTerritoryDivisonModel getTerritoryDivisonModel(final JnjGTTerritoryDivisonModel jnjGTTerritoryDiv)
	{
		JnjGTTerritoryDivisonModel jnjGTTerritoryDivModel = null;
		try
		{
			jnjGTTerritoryDivModel = flexibleSearchService.getModelByExample(jnjGTTerritoryDiv);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("JnjGTTerritoryDivCustRelModel was not found with the loaded attributes");
		}
		return jnjGTTerritoryDivModel;

	}

	@Override
	public List<JnjGTTerritoryDivCustRelModel> getTerritoryNCustomerRels(final Set<String> territoryPKSet)
	{
		return jnjGTTerritoryDao.getTerritoryNCustomerRels(territoryPKSet);
	}
	
	@Override
	public List<JnjGTTerritoryDivCustRelModel> getAllJnjGTTerritoryDivCustRelModel()
	{
		return jnjGTTerritoryDao.getAllJnjGTTerritoryDivCustRelModel();
	}


	/* This method is used for getting all the TerritoryDivModel */
	@Override
	public List<JnjGTTerritoryDivisonModel> getAllJnjGTTerritoryDivModel(final String sourceSystemId)
	{
		return jnjGTTerritoryDao.getAllJnjGTTerritoryDivison(sourceSystemId);
	}

}
