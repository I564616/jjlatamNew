package com.jnj.core.services.surgeon;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.Collection;

import com.jnj.core.model.JnjGTSurgeonModel;


// TODO: Auto-generated Javadoc
/**
 * The Interface JnjGTSurgeonService.
 *
 * @author sakshi.kashiva
 */
public interface JnjGTSurgeonService
{
	/**
	 * Save surgeon.
	 *
	 * @param JnjGTSurgeonModel
	 *           the jnj na surgeon model
	 * @return true, if successful
	 */
	public boolean saveSurgeon(final JnjGTSurgeonModel JnjGTSurgeonModel);


	/**
	 * Gets the surgeon model by example.
	 *
	 * @param surgeon
	 *           the surgeon
	 * @return the surgeon model by example
	 */
	public JnjGTSurgeonModel getJnjGTSurgeonModelByExample(JnjGTSurgeonModel surgeon);

	/**
	 * Retrieves all surgeon records present in the system.
	 *
	 * @return Collection<JnjGTSurgeonModel>
	 */
	public Collection<JnjGTSurgeonModel> getAllSurgeonRecords();

	/**
	 * Gets the surgeon records.
	 *
	 * @param pageableData
	 *           the pageable data
	 * @param searchPattern
	 *           the search pattern
	 * @return the surgeon records
	 */
	public SearchPageData<JnjGTSurgeonModel> getSurgeonRecords(final PageableData pageableData, final String searchPattern);
}
