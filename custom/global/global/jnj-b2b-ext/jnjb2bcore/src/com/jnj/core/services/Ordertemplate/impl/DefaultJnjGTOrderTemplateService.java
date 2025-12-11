/**
 *
 */
package com.jnj.core.services.Ordertemplate.impl;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.services.template.impl.DefaultJnjTemplateService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTTemplateDetailsData;
import com.jnj.core.dao.Ordertemplate.JnjGTOrderTemplateDao;
import com.jnj.core.enums.ShareStatus;
import com.jnj.core.services.Ordertemplate.JnjGTOrderTemplateService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;


/**
 * @author sakshi.kashiva
 * 
 */
public class DefaultJnjGTOrderTemplateService extends DefaultJnjTemplateService implements JnjGTOrderTemplateService
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderTemplateService.class);

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Resource(name = "cartService")
	protected CartService cartService;

	@Resource(name = "jnjB2BUnitService")
	JnjGTB2BUnitService jnjGTB2BUnitService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	@Resource(name = "GTOrderTemplateDao")
	JnjGTOrderTemplateDao jnjGTOrderTemplateDao;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected KeyGenerator guidKeyGenerator;
	
	@Autowired
	protected PersistentKeyGenerator orderTemplateNumberseriesGenerator;

	@Autowired
	protected B2BOrderService b2bOrderService;

	public ModelService getModelService() {
		return modelService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public CartService getCartService() {
		return cartService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	public JnjGTOrderTemplateDao getJnjGTOrderTemplateDao() {
		return jnjGTOrderTemplateDao;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public KeyGenerator getGuidKeyGenerator() {
		return guidKeyGenerator;
	}

	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}

	/**
	 * Save Order Template.
	 * 
	 * @param JnjOrderTemplateModel
	 *           the jnj na order template model
	 * @return true, if successful
	 */
	@Override
	public boolean saveTemplate(final JnjOrderTemplateModel JnjOrderTemplateModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveTemplate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		boolean saved = false;
		try
		{
			modelService.save(JnjOrderTemplateModel);
			saved = true;
		}
		catch (final ModelSavingException modelSavingException)
		{

			LOGGER.error("saveTemplate()" + Logging.HYPHEN + "Model not found Exception" + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
			throw modelSavingException;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveTemplate()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return saved;
	}

	/**
	 * Gets the order template model by example.
	 * 
	 * @param order
	 *           the order
	 * @return the order model by example
	 */
	@Override
	public JnjOrderTemplateModel getJnjGTOrderTemplateModel(final JnjOrderTemplateModel JnjGTOrderTemplate)
	{

		JnjOrderTemplateModel JnjOrderTemplateModel = null;
		try
		{
			JnjOrderTemplateModel = flexibleSearchService.getModelByExample(JnjGTOrderTemplate);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("getJnjGTOrderTemplateModelByExample()" + Logging.HYPHEN + "Error Found-Model is not loaded"
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTOrderTemplateModelByExample()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnjOrderTemplateModel;

	}

	/**
	 * Gets the order template Entry model by example.
	 * 
	 * @param orderEntry
	 *           the order Entry
	 * @return the order Entry model by example
	 */
	@Override
	public JnjTemplateEntryModel getJnjGTOrderTemplateEntryModel(final JnjTemplateEntryModel orderEntry)
	{

		JnjTemplateEntryModel JnjTemplateEntryModel = null;
		try
		{
			JnjTemplateEntryModel = flexibleSearchService.getModelByExample(orderEntry);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("getJnjGTOrderTemplateEntryModelByExample()" + Logging.HYPHEN + "Error Found-Model is not loaded"
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTOrderTemplateEntryModelByExample()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnjTemplateEntryModel;

	}

	/**
	 * Save Order Template Entry.
	 * 
	 * @param JnjTemplateEntryModel
	 *           the jnj na order template Entry model
	 * @return true, if successful
	 */
	@Override
	public boolean saveTemplateEntry(final JnjTemplateEntryModel JnjTemplateEntryModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveTemplateEntry()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		boolean saved = false;
		try
		{
			modelService.save(JnjTemplateEntryModel);
			saved = true;
		}
		catch (final ModelSavingException modelSavingException)
		{

			LOGGER.error("saveTemplateEntry()" + Logging.HYPHEN + "Model not found Exception" + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
			throw modelSavingException;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveTemplateEntry()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return saved;
	}


	@Override
	public boolean createTemplateFromSessionCart(final String templateName, final boolean shared)
	{
		final CartModel cart = cartService.getSessionCart();
		final JnjOrderTemplateModel JnjOrderTemplateModel = createOrderTemplate(cart, templateName, shared);
		return saveTemplate(JnjOrderTemplateModel);
	}


	@Override
	public boolean createTempateFromOrder(final String orderId, final String templateName, final boolean shared)
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderId);
		final JnjOrderTemplateModel JnjOrderTemplateModel = createOrderTemplate(order, templateName, shared);
		return saveTemplate(JnjOrderTemplateModel);
	}
	
	@Override
	public boolean createNewTempateFromCartData(final AbstractOrderModel cart, final String templateName, final boolean shared)
	{
		
		final JnjOrderTemplateModel JnjOrderTemplateModel = createOrderTemplate(cart, templateName, shared);
		return saveTemplate(JnjOrderTemplateModel);
	}

	/**
	 * Creates the order template.
	 * 
	 * @param cart
	 *           the cart
	 * @return true, if successful
	 */
	protected JnjOrderTemplateModel createOrderTemplate(final AbstractOrderModel cart, final String templateName,
			final boolean shared)
	{
		final List<AbstractOrderEntryModel> cartEntryList = cart.getEntries();
		//final String code = guidKeyGenerator.generate().toString();
		String code= new SimpleDateFormat("yyyyMMdd").format(new Date()) +orderTemplateNumberseriesGenerator.generate().toString();
		final List<JnjTemplateEntryModel> templateEntryModelList = new ArrayList<JnjTemplateEntryModel>();
		JnjOrderTemplateModel JnjOrderTemplateModel = modelService.create(JnjOrderTemplateModel.class);
		JnjOrderTemplateModel.setName(templateName);
		JnjOrderTemplateModel.setCode(code);
		JnjOrderTemplateModel.setTemplateNumber(code);
		JnjOrderTemplateModel.setAuthor(jnjGTCustomerService.getCurrentUser());
		if (shared)
		{
			JnjOrderTemplateModel.setVisibleTo(cart.getUnit());
			JnjOrderTemplateModel.setShareStatus(ShareStatus.SHARED);
		}
		else
		{
			JnjOrderTemplateModel.setVisibleTo(cart.getUser());
			JnjOrderTemplateModel.setShareStatus(ShareStatus.PRIVATE);
		}
		try
		{

			//Trying to fetch existing one
			final JnjOrderTemplateModel savedTemplateModel = flexibleSearchService.getModelByExample(JnjOrderTemplateModel);
			//If exist set the latest with old one.
			JnjOrderTemplateModel = savedTemplateModel;
		}
		catch (final ModelNotFoundException mnfe)
		{
			LOGGER.info("Template not found for code:" + code + "Creating new Template");
		}
		for (final AbstractOrderEntryModel cartEntry : cartEntryList)
		{
			//Please use the
			final JnjTemplateEntryModel templateEntryModel = modelService.create(JnjTemplateEntryModel.class);
			templateEntryModel.setProduct(getProduct(cartEntry.getProduct()));
			templateEntryModel.setQty(cartEntry.getQuantity());
			templateEntryModel.setUnitPrice(cartEntry.getBasePrice());
			templateEntryModel.setTotalPrice(cartEntry.getTotalPrice());
			templateEntryModel.setOrderTemplate(JnjOrderTemplateModel);
			templateEntryModel.setReferencedVariant(cartEntry.getReferencedVariant());
			templateEntryModelList.add(templateEntryModel);
		}
		JnjOrderTemplateModel.setEntryList(templateEntryModelList);
		JnjOrderTemplateModel.setUnit(jnjGTB2BUnitService.getCurrentB2BUnit());
		return JnjOrderTemplateModel;
	}


	public JnJProductModel getProduct(final ProductModel productModel)
	{
		JnJProductModel JnJProductModel = null;
		if (productModel instanceof JnjGTVariantProductModel)
		{
			final JnjGTVariantProductModel variantProduct = (JnjGTVariantProductModel) productModel;
			final JnJProductModel modProduct = (JnJProductModel) variantProduct.getBaseProduct();
			JnJProductModel = modProduct.getMaterialBaseProduct();

			// if base product is null
			if (JnJProductModel == null)
			{
				JnJProductModel = modProduct;
			}
		}
		else if (productModel instanceof JnJProductModel)
		{
			JnJProductModel = (JnJProductModel) productModel;
		}
		return JnJProductModel;
	}


	@Override
	public List<JnjOrderTemplateModel> searchOrderTemplate(final String searchByCriteria, final String searchParameter)
	{
		return null;

	}

	@Override
	public SearchPageData<JnjOrderTemplateModel> getPagedOrderTemplatesForGT(final PageableData pageableData,
			final String searchByCriteria, final String searchParameter, final String sortByCriteria, final String unit)
	{
		return jnjGTOrderTemplateDao.getPagedOrderTemplates(pageableData, searchByCriteria, searchParameter, sortByCriteria, unit);
	}

	@Override
	public List<JnjTemplateEntryModel> getSelectedTemplate(final JnjGTPageableData jnjGTPageableData, final String unit)
	{
		final JnjOrderTemplateModel templateModel = jnjGTOrderTemplateDao.getOrderTemplateDetails(
				jnjGTPageableData.getSearchText(), unit);

		final List<JnjTemplateEntryModel> templateEntryDatas = new ArrayList<JnjTemplateEntryModel>();

		/** Checking if the user has access to the requested order template **/
		if (null == templateModel)
		{
			/** Return null to signify lack of access to requested template **/
			return null;
		}

		JnjTemplateEntryModel entryData = null;
		final int totalCountToDisplay = jnjGTPageableData.getCurrentPage() * jnjGTPageableData.getPageSize();
		for (final JnjTemplateEntryModel orderTemplateEntry : templateModel.getEntryList())
		{
			if (orderTemplateEntry instanceof JnjTemplateEntryModel)
			{
				entryData = orderTemplateEntry;
				templateEntryDatas.add(entryData);
				if (templateEntryDatas.size() == totalCountToDisplay && !(jnjGTPageableData.getStatus().equalsIgnoreCase("All")))
				{
					break;
				}
			}
		}
		return templateEntryDatas;
	}

	/**
	 * This method gives the List of Templates will include all personal and shared templates for the Current Account
	 * 
	 * @return List<JnjOrderTemplateModel>
	 */
	@Override
	public Map<String, String> getOrderTemplatesForHome()
	{
		return jnjGTOrderTemplateDao.getOrderTemplatesForHome();
	}

	/**
	 * This method will be used to delete the template as the Global code does not find the current B2b unit, instead it
	 * looks for the default B2b unit.
	 */
	@Override
	public boolean deleteTemplate(final String templateCode)
	{
		boolean confirmDelete = false;
		try
		{
			JnjOrderTemplateModel templateModel = modelService.create(JnjOrderTemplateModel.class);
			templateModel.setCode(templateCode);
			templateModel.setUnit(jnjGTB2BUnitService.getCurrentB2BUnit());
			templateModel = flexibleSearchService.getModelByExample(templateModel);
			modelService.removeAll(templateModel.getEntryList());
			modelService.remove(templateModel);
			confirmDelete = true;
		}
		catch (final ModelRemovalException e)
		{
			confirmDelete = false;
		}

		return confirmDelete;
	}

	/**
	 * This method is used to update the data of the Order Template.
	 */
	@Override
	public boolean updateTemplate(final JnjGTTemplateDetailsData templateDetailsData)
	{
		JnjOrderTemplateModel templateModel = modelService.create(JnjOrderTemplateModel.class);
		templateModel.setCode(templateDetailsData.getTemplateNumber());
		templateModel = flexibleSearchService.getModelByExample(templateModel);
		final ShareStatus status = templateDetailsData.isShareWithAccountUsers() ? ShareStatus.SHARED : ShareStatus.PRIVATE;
		final PrincipalModel principalModel = templateDetailsData.isShareWithAccountUsers() ? jnjGTB2BUnitService
				.getCurrentB2BUnit() : jnjGTCustomerService.getCurrentUser();
		templateModel.setVisibleTo(principalModel);
		templateModel.setShareStatus(status);
		templateModel.setName(templateDetailsData.getTemplateName());
		return saveTemplate(templateModel);
	}

	@Override
	public List<JnjOrderTemplateModel> getJnjGTOrderTemplateModelsByNumber(final JnjOrderTemplateModel JnjGTOrderTemplate)
	{

		List<JnjOrderTemplateModel> JnjGTOrderTemplateModelList = null;
		try
		{
			JnjGTOrderTemplateModelList = flexibleSearchService.getModelsByExample(JnjGTOrderTemplate);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("getJnjGTOrderTemplateModelByExample()" + Logging.HYPHEN + "Error Found-Model is not loaded"
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTOrderTemplateModelByExample()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnjGTOrderTemplateModelList;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.Ordertemplate.JnjGTOrderTemplateService#getJnjGTOrderTemplateEntryModelList(com.jnj.na.model
	 * .JnjGTOrderTemplateEntryModel)
	 */
	@Override
	public List<JnjTemplateEntryModel> getJnjGTOrderTemplateEntryModelList(final JnjTemplateEntryModel orderEntry)
	{
		List<JnjTemplateEntryModel> JnjGTOrderTemplateEntryModelList = null;
		try
		{
			JnjGTOrderTemplateEntryModelList = flexibleSearchService.getModelsByExample(orderEntry);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("getJnjGTOrderTemplateModelByExample()" + Logging.HYPHEN + "Error Found-Model is not loaded"
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnjGTOrderTemplateEntryModelList;
	}
 

}
