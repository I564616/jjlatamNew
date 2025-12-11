package com.jnj.core.services;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.core.model.JnjOrderTemplateModel;


/**
 * @author komal.sehgal
 * 
 */
public interface JnjTemplateService
{

	/**
	 * Creates the tempate from session cart.
	 * 
	 * @return true, if successful
	 */
	public boolean createTempateFromSessionCart();

	/**
	 * Creates the tempate from order.
	 * 
	 * @param orderId
	 *           the order id
	 * @return true, if successful
	 */
	public boolean createTempateFromOrder(final String orderId);

	/**
	 * Save template.
	 * 
	 * @param jnjOrderTemplateModel
	 *           the jnj order template model
	 * @return true, if successful
	 */
	public boolean saveTemplate(final JnjOrderTemplateModel jnjOrderTemplateModel);

	/**
	 * @param searchByCriteria
	 * @param searchParameter
	 * @return
	 */
	public List<JnjOrderTemplateModel> searchOrderTemplate(String searchByCriteria, String searchParameter);

	/**
	 * @param templateCode
	 * @return
	 */
	public boolean deleteTemplate(String templateCode);

	/**
	 * @param templateCode
	 * @return
	 */
	public JnjOrderTemplateModel getTemplateForCode(String templateCode);

	/**
	 * @param templateCode
	 */
	public boolean addTemplateToCart(String templateCode);

	/**
	 * @param pageableData
	 * @param searchByCriteria
	 * @param searchParameter
	 * @param sortByCriteria
	 * @return
	 */
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplates(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria);

	/**
	 * @return
	 */
	public List<JnjOrderTemplateModel> getRecenlyUsedTemplates();

}
