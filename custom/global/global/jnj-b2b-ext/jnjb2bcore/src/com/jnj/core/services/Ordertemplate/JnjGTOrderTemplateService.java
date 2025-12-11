/**
 *
 */
package com.jnj.core.services.Ordertemplate;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.services.template.JnjTemplateService;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTTemplateDetailsData;


/**
 * @author sakshi.kashiva
 * 
 */
public interface JnjGTOrderTemplateService extends JnjTemplateService
{
	/**
	 * Save Order Template.
	 * 
	 * @param JnjOrderTemplateModel
	 *           the jnj na order template model
	 * @return true, if successful
	 */
	public boolean saveTemplate(final JnjOrderTemplateModel JnjOrderTemplateModel);

	/**
	 * Gets the order template model by example.
	 * 
	 * @param order
	 *           the order
	 * @return the order model by example
	 */
	public JnjOrderTemplateModel getJnjGTOrderTemplateModel(JnjOrderTemplateModel order);

	/**
	 * Gets the order template Entry model by example.
	 * 
	 * @param orderEntry
	 *           the order Entry
	 * @return the order Entry model by example
	 */
	public JnjTemplateEntryModel getJnjGTOrderTemplateEntryModel(JnjTemplateEntryModel orderEntry);

	/**
	 * Save Order Template Entry.
	 * 
	 * @param JnjTemplateEntryModel
	 *           the jnj na order template Entry model
	 * @return true, if successful
	 */
	public boolean saveTemplateEntry(final JnjTemplateEntryModel JnjTemplateEntryModel);

	/**
	 * Creates the tempate from session cart.
	 * 
	 * @param templateName
	 *           the template name
	 * @param shared
	 *           the shared
	 * @return true, if successful
	 */
	public boolean createTemplateFromSessionCart(final String templateName, final boolean shared);

	/**
	 * @param pageableData
	 * @param searchByCriteria
	 * @param searchParameter
	 * @param sortByCriteria
	 * @return
	 */
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplatesForGT(PageableData pageableData, String searchByCriteria,
			String searchParameter, String sortByCriteria, String unit);

	/**
	 * @param jnjGTPageableData
	 * @param unit
	 * @return List<JnjTemplateEntryModel>
	 */
	List<JnjTemplateEntryModel> getSelectedTemplate(final JnjGTPageableData jnjGTPageableData, String unit);

	/**
	 * This method is used to update the data of the Order Template.
	 * 
	 * @param templateDetailsData
	 * @return
	 */
	public boolean updateTemplate(JnjGTTemplateDetailsData templateDetailsData);

	/**
	 * This method gives the List of Templates will include all personal and shared templates for the Current Account
	 * 
	 * @return List<JnjOrderTemplateModel>
	 */
	public Map<String, String> getOrderTemplatesForHome();

	/**
	 * Gets the jnj na order template models by number.
	 * 
	 * @param JnjGTOrderTemplate
	 *           the jnj na order template
	 * @return the jnj na order template models by number
	 */
	List<JnjOrderTemplateModel> getJnjGTOrderTemplateModelsByNumber(JnjOrderTemplateModel JnjGTOrderTemplate);


	/**
	 * Gets the jnj na order template entry model list.
	 * 
	 * @param orderEntry
	 *           the order entry
	 * @return the jnj na order template entry model list
	 */
	public List<JnjTemplateEntryModel> getJnjGTOrderTemplateEntryModelList(JnjTemplateEntryModel orderEntry);

	/**
	 * Creates the tempate from order.
	 * 
	 * @param orderId
	 *           the order id
	 * @param templateName
	 *           the template name
	 * @param shared
	 *           the shared
	 * @return true, if successful
	 */
	public boolean createTempateFromOrder(String orderId, String templateName, final boolean shared);
	
	public boolean createNewTempateFromCartData(final AbstractOrderModel cart, final String templateName, final boolean shared);
}
