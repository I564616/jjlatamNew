/**
 * 
 */
package com.jnj.core.solr;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *  This is implementation class of SolrQueryPostProcessor, is used to add the fields which should be returned as part of
 * solr response.
 * @author tlevaku
 *
 */
public class JnjSolrFacetSearchListener implements FacetSearchListener{
	
	
	protected static final Logger LOG = Logger.getLogger(JnjSolrFacetSearchListener.class);
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	protected UserService userService;
	
	@Resource(name = "commerceCategoryService")
	protected CommerceCategoryService commerceCategoryService;

	@Override
	public void beforeSearch(FacetSearchContext query) throws FacetSearchException {
		
		String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (currentSite == null)
		{
			sessionService.setAttribute(Jnjb2bCoreConstants.SITE_NAME, Jnjb2bCoreConstants.CONS);
			currentSite = Jnjb2bCoreConstants.CONS;
		}

		// If site is CONS, restriction needs to be applied else no restriction is required
		if (currentSite.equals(Jnjb2bCoreConstants.CONS))
		{
			final String fieldString = JnJCommonUtil.getValue(Jnjgtb2bMDDConstants.FIELDLIST);
			final String[] fieldsToReturn = fieldString.split(Jnjb2bCoreConstants.Register.COMMA_SEPARATOR);
			for (final String string : fieldsToReturn)
			{
				query.getSearchQuery().addField(string);
			}
		}
		
		if (currentSite.equals(Jnjb2bCoreConstants.MDD))
		{
			final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
			final String fieldString = JnJCommonUtil.getValue(Jnjgtb2bMDDConstants.FIELDLIST);
			final String[] fieldsToReturn = fieldString.split(Jnjb2bCoreConstants.Register.COMMA_SEPARATOR);
			for (final String string : fieldsToReturn)
			{
				query.getSearchQuery().addField(string);
			}
			/*Adding to restrict products in solr query based on allowed franchise as part of user of AAOL-5318*/		
			List<CategoryModel> categories = new ArrayList<CategoryModel>();
			if(CollectionUtils.isNotEmpty(currentCustomer.getAllowedFranchise()))
			{
				categories = currentCustomer.getAllowedFranchise();
			}
			else
			{
				categories = getAllFranchise();
			}
			int size =0;
			String string = StringUtils.EMPTY;
			
			for(CategoryModel category:categories)
			{
				if(size<(categories.size()-1))
				{
					string = string+category.getCode()+" OR ";
					size++;
				}
				else
				{
					string = string+category.getCode();
				}
			}
			query.getSearchQuery().addFilterRawQuery("allCategories_string_mv:("+string+")");	
			
		}
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Jnjb2bCoreConstants.Solr.SOLR_FACET_LISTENER + Logging.HYPHEN + query + Logging.HYPHEN + Logging.END_OF_METHOD);
		}
		
		
		
	}

	@Override
	public void afterSearch(FacetSearchContext arg0) throws FacetSearchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSearchError(FacetSearchContext arg0) throws FacetSearchException {
		// TODO Auto-generated method stub
		
	}
	
	
	public List<CategoryModel> getAllFranchise(){
		final CategoryModel category = commerceCategoryService.getCategoryForCode("Categories");
		final List<CategoryModel> allFranchise = new ArrayList<>();

		if(CollectionUtils.isNotEmpty(category.getCategories())){
			LOG.info("getAllFranchise() Fetching all available Franchises from hybris");
			for (final CategoryModel subCategory : category.getCategories()){
				allFranchise.add(subCategory);
			}
		}
		
		return allFranchise;
	}
	
	public CategoryData getCategoryForCode(String code) {
		final CategoryModel category = commerceCategoryService.getCategoryForCode(code);
		CategoryData categoryData = new CategoryData();
		categoryData.setCode(category.getCode());
		categoryData.setName(category.getName());
		return categoryData;
	}

}
