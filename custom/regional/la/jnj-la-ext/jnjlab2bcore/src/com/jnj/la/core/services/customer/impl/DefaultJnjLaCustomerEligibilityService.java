/**
 *
 */
package com.jnj.la.core.services.customer.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCustomerEligiblityDTO;
import com.jnj.core.services.customerEligibility.impl.DefaultJnjCustomerEligibilityService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.customer.JnjLaCustomerEligiblityDao;
import com.jnj.la.core.services.customer.JnjLaCustomerEligibilityService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Set;

import static com.jnj.la.core.util.JnjLaCommonUtil.getIdByCountry;


/**
 * Customer Eligibility Interface Data Load - Service Layer class, responsible for all method calls from
 * <code>JnjCustomerEligiblityDataLoadMapper</code>
 * 
 * @author mapnda3
 * 
 */
public class DefaultJnjLaCustomerEligibilityService extends DefaultJnjCustomerEligibilityService implements
		JnjLaCustomerEligibilityService
{

	protected JnjLaCustomerEligiblityDao customerLaEligiblityDao;



	/**
	 * @return the customerLaEligiblityDao
	 */
	public JnjLaCustomerEligiblityDao getCustomerLaEligiblityDao()
	{
		return customerLaEligiblityDao;
	}



	/**
	 * @param customerLaEligiblityDao
	 *           the customerLaEligiblityDao to set
	 */
	public void setCustomerLaEligiblityDao(final JnjLaCustomerEligiblityDao customerLaEligiblityDao)
	{
		this.customerLaEligiblityDao = customerLaEligiblityDao;
	}



	@Override
	public Set<JnjCustomerEligiblityDTO> getCustomerEligibilityRecords(final String customerEligibilityQuery)
	{
		return getCustomerLaEligiblityDao().getCustomerEligibilityRecords(customerEligibilityQuery);
	}

	@Override
	public CategoryModel getCategoryByCode(final String categoryCode, final String unitCountryCode)
	{
		CategoryModel category = null;
		try
		{
			final String catalogId = getIdByCountry(unitCountryCode).toLowerCase().concat(Jnjlab2bcoreConstants.PRODUCT_CATALOG);

			final CatalogModel catalogModel = getCatalogService().getCatalogForId(catalogId);
			final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogModel.getId(),
					Jnjb2bCoreConstants.ONLINE);

			category = getCategoryService().getCategoryForCode(catalogVersion, categoryCode);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage("La Customer Eligibility", "getCategoryByCode()", e.getMessage(), e,
					DefaultJnjLaCustomerEligibilityService.class);
			return null;
		}
		return category;
	}
}
