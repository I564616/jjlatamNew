/**
 *
 */
package com.jnj.facades.template.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.dto.JnjGTOrderTemplateForm;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTTemplateDetailsData;
import com.jnj.core.enums.ShareStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.Ordertemplate.JnjGTOrderTemplateService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.operations.JnjGTOperationsService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.facades.template.JnjGTOrderTemplateFacade;
import com.jnj.services.MessageService;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author neeraj.m.kumar
 * 
 */
public class DefaultJnjGTOrderTemplateFacade extends DefaultJnjTemplateFacade implements JnjGTOrderTemplateFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderTemplateFacade.class);

	protected static final String CART_MODIFICATAION_DATA = "cartModificationData";
	protected static final String INVALID_PRODUCT = "invalid";
	protected static final String VALID_PRODUCT = "valid";
	protected static final String QTY_UPDATE = "quantityUpdated";
	protected static final String SYMBOl_COMMA = ",";
	public static final String COLON = ":";
	@Autowired
	protected JnjGTOrderTemplateService jnjGTOrderTemplateService;

	@Autowired
	protected MessageService messageService;
	
	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	
	@Autowired
	protected JnjConfigService jnjConfigService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	@Resource(name = "jnjGTOrderTemplateConverter")
	protected Converter<JnjOrderTemplateModel, JnjGTOrderTemplateData> jnjGTOrderTemplateConverter;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected ModelService modelService;

	@Resource(name = "jnjGTOrderTemplateEntryConverter")
	protected Converter<JnjTemplateEntryModel, JnjTemplateEntryData> jnjGTOrderTemplateEntryConverter;

	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

    @Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;
	
	@Autowired
	protected JnJProductService jnjProductService;
    
	@Autowired
	protected JnjGTOperationsService jnjGTOperationsService;
	
	@Autowired
	protected UserService userService;

	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;

	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;
	
	@Override
	public boolean createTemplateFromSessionCart(final String templateName, final boolean shared)
	{
		return jnjGTOrderTemplateService.createTemplateFromSessionCart(templateName, shared);

	}

	@Override
	public boolean createTemplateFromOrder(final String orderId, final String templateName, final boolean shared)
	{
		return jnjGTOrderTemplateService.createTempateFromOrder(orderId, templateName, shared);

	}


	@Override
	public Map<String, String> getDropDownList(final String configId)
	{
		return jnjConfigService.getDropdownValuesInMap(configId);
	}

	@Override
	public List<String> getDropDownListForGroupBy(final String id)
	{
		return Arrays.asList(JnJCommonUtil.getValue(id).split(Jnjb2bCoreConstants.CONST_COMMA));
	}

	@Override
	public SearchPageData<JnjGTOrderTemplateData> getPagedTemplateForStatuses(final PageableData pageableData,
			final JnjGTOrderTemplateForm form)
	{

		final String jnjGTB2bUnitPK = jnjGTCustomerService.getCurrentUser().getCurrentB2BUnit().getPk().toString();


		final String searchByCriteria = form.getSearchby();
		final String searchParameter = form.getSearchText();
		final String sortByCriteria = form.getSortby();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.GET_PAGED_TEMPLATES
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final SearchPageData<JnjOrderTemplateModel> orderTemplateResults = jnjGTOrderTemplateService.getPagedOrderTemplatesForGT(
				pageableData, searchByCriteria, searchParameter, sortByCriteria, jnjGTB2bUnitPK);
		final SearchPageData<JnjGTOrderTemplateData> result = new SearchPageData<JnjGTOrderTemplateData>();
		if (null != orderTemplateResults)
		{
			final Collection<JnjOrderTemplateModel> orderTemplateList = orderTemplateResults.getResults();
			final List<JnjGTOrderTemplateData> orderTemplateDataList = templateConverter(orderTemplateList);
			form.setTotalTemplates(Long.valueOf(orderTemplateResults.getPagination().getTotalNumberOfResults()));
			result.setResults(orderTemplateDataList);
		}
		else
		{
			result.setResults(new ArrayList<JnjGTOrderTemplateData>());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.GET_PAGED_TEMPLATES
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	protected List<JnjGTOrderTemplateData> templateConverter(final Collection<JnjOrderTemplateModel> orderTemplateList)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.TEMPLATE_CONVERTER
					+ "-" + Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjGTOrderTemplateData> orderTemplateDataList = new ArrayList<JnjGTOrderTemplateData>();
		for (final JnjOrderTemplateModel orderTemplate : orderTemplateList)
		{
			if (orderTemplate instanceof JnjOrderTemplateModel && orderTemplate instanceof JnjOrderTemplateModel)
			{
				JnjGTOrderTemplateData templateData = new JnjGTOrderTemplateData();
				templateData = jnjGTOrderTemplateConverter.convert((JnjOrderTemplateModel) orderTemplate);
				orderTemplateDataList.add(templateData);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.MY_TEMPLATE_FACADE + " - " + Jnjb2bCoreConstants.Logging.TEMPLATE_CONVERTER
					+ "-" + Logging.END_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		return orderTemplateDataList;
	}

	@Override
	public List<JnjTemplateEntryData> getSelectedOrderTemplateDetail(final JnjGTPageableData jnjGTPageableData)
	{

		final String jnjGTB2bUnitPK = jnjGTCustomerService.getCurrentUser().getCurrentB2BUnit().getPk().toString();

		final List<JnjTemplateEntryModel> entryModels = jnjGTOrderTemplateService.getSelectedTemplate(jnjGTPageableData,
				jnjGTB2bUnitPK);

		/** entryModels will be returned as null only if the user does not have access to the requested template **/
		if (null == entryModels)
		{
			/** Return null to signify lack of access to requested template **/
			return null;
		}

		final JnjGTOrderTemplateData templateData = new JnjGTOrderTemplateData();
		templateData.setOrderEntry(Converters.convertAll(entryModels, jnjGTOrderTemplateEntryConverter));
		return templateData.getOrderEntry();
	}

	/**
	 * This method gives the List of Templates will include all personal and shared templates for the Current Account
	 * 
	 * @return List<JnjOrderTemplateModel>
	 */
	@Override
	public Map<String, String> getOrderTemplatesForHome()
	{
		return jnjGTOrderTemplateService.getOrderTemplatesForHome();

	}

	/**
	 * This method is used For addTocart From Template Via Home
	 * 
	 * @param responseMap
	 * @param templateCode
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> addToCartFromHomeTemplate(final Map<String, Object> responseMap, final String templateCode)
	{
		JnjOrderTemplateModel orderTemplateModel = new JnjOrderTemplateModel();
		orderTemplateModel.setCode(templateCode);
		//Getting the Template Model For required Code.
		orderTemplateModel = jnjGTOrderTemplateService.getJnjGTOrderTemplateModel(orderTemplateModel);
		//Getting the Template Entry Model List  For required Template.
		final List<JnjTemplateEntryModel> templateEntryModelList = orderTemplateModel.getEntryList();

		//Getting the Template Entry Model List  For required Template.
		for (final JnjTemplateEntryModel templateEntry : templateEntryModelList)
		{
			List<String> productCodes = new ArrayList<String>();
			if (templateEntry instanceof JnjTemplateEntryModel)
			{

				final JnjTemplateEntryModel JnjTemplateEntryModel = (JnjTemplateEntryModel) templateEntry;

				//Getting the Valid Product To Be added
				
				final JnjGTVariantProductModel referencedVariant = JnjTemplateEntryModel.getReferencedVariant();
				productCodes = jnJGTProductService.getEligibleUrlAndCodeForOrderHistoryAndTemplate(
						(JnJProductModel) JnjTemplateEntryModel.getProduct(),
						referencedVariant);
				final String productTobeAdded = productCodes.get(0);
				try
				{
					final JnjCartModificationData cartModificationData = jnjGTCartFacade.addToCart(productTobeAdded,
							String.valueOf(templateEntry.getQty()));

					if (!cartModificationData.getCartModifications().get(0).isError())
					{
						responseMap.put(CART_MODIFICATAION_DATA, cartModificationData);
						responseMap.put(productTobeAdded, Jnjb2bCoreConstants.HomePage.PRODUCT_ADDTOCART_SUCCESS);
					}
					else
					{
						responseMap.put(productTobeAdded, cartModificationData.getCartModifications().get(0).getStatusCode());
					}
				}
				catch (final CommerceCartModificationException exeption)
				{
					LOGGER.error("Not able to add products" + " Excption trace" + exeption);
					LOGGER.error("will be logged into the database");
					responseMap.put(
							productTobeAdded,
							productTobeAdded + Jnjb2bCoreConstants.UserSearch.SPACE
									+ jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_NOT_ADDED_ERROR));
					jnjGTOperationsService.logAuditData("User", productTobeAdded + Jnjb2bCoreConstants.UserSearch.SPACE + jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_NOT_ADDED_ERROR), "", false, false, new Date(), userService.getCurrentUser().getName());
				}
				catch (final NumberFormatException exeption)
				{
					LOGGER.error("Not able to add products as the entered value for the quantity is not valid");
					LOGGER.error("will be logged into the database");
					responseMap.put(
							productTobeAdded,
							productTobeAdded
									+ Jnjb2bCoreConstants.UserSearch.SPACE
									+ jnjCommonFacadeUtil
											.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_INVALID_ERROR_QUNATITY));
					jnjGTOperationsService.logAuditData("User", productTobeAdded + Jnjb2bCoreConstants.UserSearch.SPACE + jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_INVALID_ERROR_QUNATITY) , "", false, false, new Date(), userService.getCurrentUser().getName());
				}
			}
		}
		return responseMap;
	}

	/**
	 * This method is used to update the data of the Order Template.
	 */
	@Override
	public boolean updateTemplate(final JnjGTTemplateDetailsData templateDetailsData)
	{
		return jnjGTOrderTemplateService.updateTemplate(templateDetailsData);
	}

	/**
	 * This method will be used to delete the template as the Global code does not find the current B2b unit, instead it
	 * looks for the default B2b unit.
	 */
	@Override
	public boolean deleteTemplate(final String templateCode)
	{
		return jnjGTOrderTemplateService.deleteTemplate(templateCode);
	}

	/**
	 * This method is used to find a method based on the template code provided.
	 */
	@Override
	public JnjGTOrderTemplateData getTemplateForCode(final String templateCode)
	{
		JnjGTOrderTemplateData templateData = new JnjGTOrderTemplateData();
		JnjOrderTemplateModel orderTemplateModel = new JnjOrderTemplateModel();
		orderTemplateModel.setCode(templateCode);
		//Getting the Template Model For required Code.
		orderTemplateModel = jnjGTOrderTemplateService.getJnjGTOrderTemplateModel(orderTemplateModel);
		templateData = jnjGTOrderTemplateConverter.convert(orderTemplateModel);
		return templateData;
	}
	
	public String updateProductAndQuantityForTemplate(JnjGTTemplateDetailsData jnjGTTemplateDetailsForm)
	{
			ProductModel product = jnJGTProductService.getProductByCodeOrEAN(jnjGTTemplateDetailsForm.getProductCode());
			
			
			if (null != product)
			{
				
				ProductModel productModelToValidate = product;
				LOGGER.debug("JnjGTProductData :::::::::::::: "+ product.getCode() + "::::::::::::::::      " + product.getUnit().getCode());
				
				final Date startTime = new Date();
				if (productModelToValidate != null)
				{
					// From session get the current site i.e. MDD/CONS
					final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

					// If site is MDD, variant needs to be searched upon GTIN and Base Material Number
					if (productModelToValidate instanceof JnjGTVariantProductModel)
					{
						productModelToValidate = ((JnjGTVariantProductModel) productModelToValidate).getBaseProduct();
					}
					/*final Boolean salesRep = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);

					// If product is not saleable return the corresponding error message
					final String status = jnJGTProductService.isProductSaleable((JnJProductModel) productModelToValidate, currentSite);
					
					// If user is a sales rep with place order privilege, and If product is not of compatible division return the corresponding error message
					if (Jnjb2bCoreConstants.MDD.equals(currentSite) && BooleanUtils.isTrue(salesRep)
							&& !jnJGTProductService.isProductDivisionSameAsUserDivision((JnJProductModel) productModelToValidate))
					{
						jnjGTOperationsService.logAuditData("User", productModelToValidate.getCode() + Jnjb2bCoreConstants.UserSearch.SPACE + jnjCommonFacadeUtil.getMessageFromImpex(Jnjb2bCoreConstants.HomePage.PRODUCT_NOT_ADDED_ERROR), "", false, false, new Date(), userService.getCurrentUser().getName());
						return messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null,	productModelToValidate.getCode());
										
					}*/
				
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("----->JnjGTCartFacadeImpl - validateProduct(" + productModelToValidate.getCode()
							+ ") Time taken for Validating ProductModel: " + (new Date().getTime() - startTime.getTime()));
				}
				
				/* product add*/
			//	jnjEMEATemplateDetailsForm.setProductCode(((JnjGTVariantProductModel)productModelToValidate).getBaseProduct().getCode());
				/*String isUpdated = addTemplateProduct(jnjGTTemplateDetailsForm);
				
				if("quantityUpdated".equals(isUpdated))
				{
					return isUpdated;
				}*/
				final JnjGTVariantProductModel delVariant = jnJGTProductService.getDeliveryGTIN(productModelToValidate);
				
				
				String productDetails = productModelToValidate.getCode()+" $$$ "+productModelToValidate.getName()+" $$$ "+delVariant.getNumerator().toString()+" $$$ "+delVariant.getLwrPackagingLvlUom().getName()+" $$$ "+delVariant.getUnit().getName();
				/*productModelToValidate.getVariants().iterator()*/
				return productDetails;
				
			}
			
		
				
		}else
		{
			
			return INVALID_PRODUCT;
		}
			return "";
	}
	
	
	
	public String addTemplateProduct(JnjGTTemplateDetailsData jnjGTTemplateDetailsForm)
	{
		
		
		final String templateCode = jnjGTTemplateDetailsForm.getTemplateNumber();
		final String refVariant = jnjGTTemplateDetailsForm.getProductCode();
		ProductModel product = jnJGTProductService.getProductByCodeOrEAN(refVariant);
		JnjGTVariantProductModel variantProduct = null;
		JnjGTVariantProductModel variantProductEAN = null;
		boolean isQtyUpdated = false;
		long qty =Long.parseLong(jnjGTTemplateDetailsForm.getQuantity()) ;
		try
		{
			JnjOrderTemplateModel templateModel = modelService.create(JnjOrderTemplateModel.class);
			templateModel.setCode(templateCode);
			templateModel.setUnit(jnjGTB2BUnitService.getCurrentB2BUnit());
			templateModel = flexibleSearchService.getModelByExample(templateModel);
			final List<JnjTemplateEntryModel> templateEntrylist = new ArrayList<JnjTemplateEntryModel>();
			//JnjTemplateEntryModel refVariantModel=null;
			final JnjTemplateEntryModel templateEntryModel = modelService.create(JnjTemplateEntryModel.class);
			final List<JnjTemplateEntryModel> templateEntryModelList = new ArrayList<JnjTemplateEntryModel>();
		
		if (product instanceof JnjGTVariantProductModel)
		{
			String[] baseProduct = product.getCode().split("\\|");
			if(baseProduct[1].equals("1"))
			{
				ProductModel productEAN = jnJGTProductService.getProductByCodeOrEAN(baseProduct[0]);
				LOGGER.debug("productEANproductEAN ::  " + productEAN.getCode() + "cc " + productEAN.getUnit());
				
					templateEntryModel.setProduct(getProduct(productEAN));
					if (productEAN instanceof JnjGTVariantProductModel)
					{
						variantProductEAN = (JnjGTVariantProductModel) productEAN;
					}

					templateEntryModel.setReferencedVariant(variantProductEAN);
						//final JnjGTVariantProductModel variantProduct = (JnjGTVariantProductModel) productModel;
					final JnJProductModel modProduct = (JnJProductModel) variantProductEAN.getBaseProduct();
					qty = setQuantityEAN(variantProductEAN,modProduct,Long.parseLong(jnjGTTemplateDetailsForm.getQuantity()), false);
					LOGGER.debug("derivedQuantity ::  " + qty);
					LOGGER.debug("variantProductEAN  " + variantProductEAN.getCode());
		}
			else {
			 variantProduct = (JnjGTVariantProductModel) product;
				
				templateEntryModel.setProduct(getProduct(product));
				templateEntryModel.setReferencedVariant(variantProduct);
				LOGGER.debug("refVariant :" + refVariant + "llllllll  "+variantProduct.getCode());
			}
		}
		
	
		
		//getPagedOrderTemplates();
		boolean confirmDelete = false;
		
		templateEntryModel.setQty(qty);
		LOGGER.debug("templateEntryModel.setQty :: " + templateEntryModel.getQty());
			
			for (final JnjTemplateEntryModel entryModel : templateModel.getEntryList())
			{
				isQtyUpdated = false;
				LOGGER.debug("entryModelentryModelentryModel " + entryModel.getProduct().getCode());
				
				if(entryModel.getReferencedVariant() != null)
				{
					LOGGER.debug("entryModel.getReferencedVariant().getCode().equalsIgnoreCase(refVariant) " + entryModel.getQty() + entryModel.getReferencedVariant().getCode());
					String[] baseProduct = product.getCode().split("\\|");
					long deliveryEnteredQty = Long.parseLong(jnjGTTemplateDetailsForm.getQuantity());
					if(baseProduct[1].equals("1"))
					{
						ProductModel productEAN = jnJGTProductService.getProductByCodeOrEAN(entryModel.getProduct().getCode());
						LOGGER.debug("productEANproductEAN ::  " + productEAN.getCode() + "cc " + productEAN.getUnit());
						if(entryModel.getProduct().getCode().equals(baseProduct[0]))
						{
							templateEntryModel.setProduct(getProduct(productEAN));
							if (productEAN instanceof JnjGTVariantProductModel)
							{
								variantProductEAN = (JnjGTVariantProductModel) productEAN;
							}

							templateEntryModel.setReferencedVariant(variantProductEAN);
							final JnJProductModel modProduct = (JnJProductModel) variantProductEAN.getBaseProduct();
							
							qty = setQuantityEAN(variantProductEAN,modProduct,Long.parseLong(jnjGTTemplateDetailsForm.getQuantity()), false);
							LOGGER.debug("variantProductEAN  " + variantProductEAN.getCode() + "qty :: " + qty);
							qty = qty + entryModel.getQty();
							LOGGER.debug("entryModel.getQty() :: " + entryModel.getQty());
							LOGGER.debug("qtyqtyqtyqty ::  " + qty);
							templateEntryModel.setQty(qty);
							//	final JnjGTVariantProductModel variantProduct = (JnjGTVariantProductModel) productModel;
							templateEntrylist.remove(entryModel);
							isQtyUpdated = true;
						}
					}
					else
					{
						if(entryModel.getReferencedVariant().getCode().equals(product.getCode()))
						{
							//entryModel.setQty(Long.parseLong(jnjEMEATemplateDetailsForm.getQuantity()));
							LOGGER.debug("entryModel.getQty() :: " + entryModel.getQty());
							qty = qty + entryModel.getQty();
							LOGGER.debug("qtyqtyqtyqty ::  " + qty);
							templateEntryModel.setQty(qty);
							templateEntrylist.remove(entryModel);
							isQtyUpdated = true;
						}
					}
				}
				else
				{
					
					String[] baseProduct = product.getCode().split("\\|");
					if(entryModel.getProduct().getCode().equals(baseProduct[0]))
					{
						qty = qty + entryModel.getQty();
						LOGGER.debug("entryModel.getQty() :: " + entryModel.getQty());
						LOGGER.debug("qtyqtyqtyqty ::  " + qty);
						templateEntryModel.setQty(qty);
						//entryModel.setQty(Long.parseLong(jnjEMEATemplateDetailsForm.getQuantity()));
						templateEntrylist.remove(entryModel);
						isQtyUpdated = true;
					}
				}
				if(! isQtyUpdated)
				{
					templateEntrylist.add(entryModel);
				}
				
			}
			templateEntrylist.add(templateEntryModel);
			
			for (final JnjTemplateEntryModel entryModel : templateModel.getEntryList())
			{
				LOGGER.debug("11111111entryModelentryModelentryModel " + entryModel.getProduct().getCode());
				LOGGER.debug("111111111entryModel.getReferencedVariant().getCode().equalsIgnoreCase(refVariant) " + entryModel.getQty());
				
				//templateEntrylist.add(entryModel);
				
			}
			
			templateModel.setEntryList(templateEntrylist);

			modelService.save(templateModel);
			//modelService.remove(refVariantModel);
			//modelService.remove(templateModel);
			
		}
		catch (final ModelRemovalException e)
		{
			//confirmDelete = false;
		}
		
		if(isQtyUpdated)
		{
			return QTY_UPDATE;
		}
		
		return ""; 
		
	}
	
	
	public long setQuantityEAN(final JnjGTVariantProductModel variantProduct, final JnJProductModel modProduct, final long quantityToAdd, final boolean isUpdate)
	{
		
		final JnjGTVariantProductModel delVariant = jnJGTProductService.getDeliveryGTIN(modProduct);
		long derivedQuantity = quantityToAdd;
		if(! isUpdate){
				if (isQtyAdjustmentRequired(variantProduct, delVariant))
				{
					if (quantityToAdd == 0)
					{
						derivedQuantity = 1;
						//addToCartMsgKey = BASKET_PAGE_MESSAGE_MIN_QTY_ADDED;
						derivedQuantity = getRoundedQuantity(delVariant, variantProduct, derivedQuantity);
					}
					else
					{
						//derivedQuantity = getRoundedQuantity(delVariant, variantProduct, quantityToAdd);
			
						LOGGER.debug("Start : Rounding of QTY");
						final long deliveryVariantLevelQty = delVariant.getNumerator() != null ? delVariant.getNumerator().longValue() : 1;
						final long enteredVariantLevelQty = variantProduct.getNumerator() != null ? variantProduct.getNumerator()
								.longValue() : 1;
						derivedQuantity = (((quantityToAdd * enteredVariantLevelQty) / deliveryVariantLevelQty) + ((quantityToAdd
								% deliveryVariantLevelQty > 0) ? 1 : 0));
						long i=0;
						
						for(long j=quantityToAdd;j>0;j=j-deliveryVariantLevelQty)
						{
							i++;
						}
						derivedQuantity = i;
						LOGGER.debug("actual QTYYYY :: " + i);
						if (quantityToAdd != derivedQuantity)
						{
							//addToCartMsgKey = BASKET_PAGE_MESSAGE_QTY_ADJUSTED;
						}
					}
				}
				else
				{
					if (quantityToAdd == 0)
					{
						derivedQuantity = 1;
					//	addToCartMsgKey = BASKET_PAGE_MESSAGE_MIN_QTY_ADDED;
					}
					final long deliveryVariantLevelQty = delVariant.getNumerator() != null ? delVariant.getNumerator().longValue() : 1;
					final long enteredVariantLevelQty = variantProduct.getNumerator() != null ? variantProduct.getNumerator()
							.longValue() : 1;
						
							long i=0;
							
							for(long j=quantityToAdd;j>0;j=j-deliveryVariantLevelQty)
							{
								i++;
							}
							derivedQuantity = i;
					/*		
					derivedQuantity = (((quantityToAdd * enteredVariantLevelQty) / deliveryVariantLevelQty) + ((quantityToAdd
							% deliveryVariantLevelQty > 0) ? 1 : 0));*/
					LOGGER.debug("deliveryVariantLevelQty : " + deliveryVariantLevelQty + "enteredVariantLevelQty : " + enteredVariantLevelQty + " derivedQuantity : " + derivedQuantity);
					LOGGER.debug("actual QTYYYY :: " + i);
				}
				
				// Add the entered variant
				if((delVariant.getMinOrderQuantity() != null ) && (delVariant.getMinOrderQuantity()>derivedQuantity))//Rajendar added the code for MOQ value 
				{
					derivedQuantity = delVariant.getMinOrderQuantity();
				}
				
				LOGGER.debug("derivedQuantity1111 : " + derivedQuantity);
				return derivedQuantity;
		}
		else
		{
			LOGGER.debug("elsederivedQuantityderivedQuantity");
		}
		
		return derivedQuantity;
	}
	
	protected boolean isQtyAdjustmentRequired(final JnjGTVariantProductModel variantProduct,
		final JnjGTVariantProductModel delVariant)
{
	final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
	
	LOGGER.debug("variantProduct.getPackagingLvlCode() :: " + variantProduct.getPackagingLvlCode() + "delVariant.getPackagingLvlCode() :: " + delVariant.getPackagingLvlCode() + "variantProduct.getCode() :: " + variantProduct.getCode()
			+ "delVariant.getCode() :: " +delVariant.getCode());
	return Jnjb2bCoreConstants.MDD.equals(currentSiteName)
			&& null != variantProduct.getPackagingLvlCode()
			&& null != delVariant.getPackagingLvlCode()
			&& (!(variantProduct.getCode().equals(delVariant.getCode()) || variantProduct.getPackagingLvlCode().intValue() > delVariant
					.getPackagingLvlCode().intValue()));
}
	
	protected long getRoundedQuantity(final JnjGTVariantProductModel delVariant, final JnjGTVariantProductModel variantProduct,
		final long quantityToAdd)
{
	LOGGER.debug("Start : Rounding of QTY");
	final long deliveryVariantLevelQty = delVariant.getNumerator() != null ? delVariant.getNumerator().longValue() : 1;
	final long enteredVariantLevelQty = variantProduct.getNumerator() != null ? variantProduct.getNumerator().longValue() : 1;

	final long roundedQty = (((quantityToAdd * enteredVariantLevelQty) / deliveryVariantLevelQty) + ((quantityToAdd
			% deliveryVariantLevelQty > 0) ? 1 : 0))
			* (deliveryVariantLevelQty / enteredVariantLevelQty);
	LOGGER.debug("Start : Rounding of QTY");
	return roundedQty;
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
	
	public boolean deleteTemplateProduct(final String templateCode, final String refVariant)
	{
		boolean confirmDelete = false;
		try
		{

			JnjOrderTemplateModel templateModel = modelService.create(JnjOrderTemplateModel.class);
			templateModel.setCode(templateCode);
			templateModel.setUnit(jnjGTB2BUnitService.getCurrentB2BUnit());
			templateModel = flexibleSearchService.getModelByExample(templateModel);
			final List<JnjTemplateEntryModel> templateEntrylist = new ArrayList<JnjTemplateEntryModel>();
			//JnjTemplateEntryModel refVariantModel=null;
			for (final JnjTemplateEntryModel entryModel : templateModel.getEntryList())
			{
				LOGGER.debug("Inside for loop " + entryModel.getReferencedVariant().getCode());
				LOGGER.debug("entryModel.getReferencedVariant().getCode().equalsIgnoreCase(refVariant) "
						+ entryModel.getReferencedVariant().getCode().equalsIgnoreCase(refVariant));
				if (!(entryModel.getReferencedVariant().getCode().equalsIgnoreCase(refVariant)))
				{
					
					templateEntrylist.add(entryModel);
					
				}else{
					modelService.remove(entryModel);
				}
			}
			templateModel.setEntryList(templateEntrylist);

			modelService.save(templateModel);
			//modelService.remove(refVariantModel);
			//modelService.remove(templateModel);
			confirmDelete = true;
		}
		catch (final ModelRemovalException e)
		{
			confirmDelete = false;
		}
		
		return confirmDelete;
	}

	public boolean createNewTemplate(final String tempProds,final String templateName,
			final boolean shared) {
		boolean confirmTemplateCreated = false;
		try
		{
		AbstractOrderModel cart = new AbstractOrderModel();
		
		List <AbstractOrderEntryModel> cartEntryList = new ArrayList();
	    JnjGTVariantProductModel delVariant = new JnjGTVariantProductModel();
	    final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final B2BUnitModel currentB2BUnit =  currentUser.getCurrentB2BUnit();
		
		final UserModel user = userService.getCurrentUser();
		final String[] products = tempProds.split(SYMBOl_COMMA);
		for (final String product : products)
		{
			AbstractOrderEntryModel cartEntry = new AbstractOrderEntryModel();
			final String[] product_Qq = product.split(COLON);
			
			final JnJProductModel productModel = jnjProductService.getProductCodeOrEAN(product_Qq[0]);
			
			productModel.setCatalogId(product_Qq[0]);
			if (null != productModel)
			{
				
				
					if (productModel instanceof JnJProductModel)
					{
						 delVariant = jnJGTProductService.getDeliveryGTIN(productModel);
					}

					cartEntry.setProduct(productModel);
					Long qnt = Long.valueOf(product_Qq[1]);
					cartEntry.setQuantity(qnt);
					cartEntry.setReferencedVariant(delVariant);
					
					cartEntryList.add(cartEntry);
		}
	}
		cart.setEntries(cartEntryList);
		cart.setUnit(currentB2BUnit);
		cart.setUser(user);
		jnjGTOrderTemplateService.createNewTempateFromCartData(cart, templateName, shared);
		confirmTemplateCreated = true;
}
		catch (final ModelRemovalException e)
		{
			confirmTemplateCreated = false;
		}
		return confirmTemplateCreated;
	}
	
	
	public boolean editExistingTemplate(final String tempProdQtys, final String templateName, final boolean shared, final String templateCode)
	{
		boolean confirmTemplateEdited = false;
		try
		{
		JnjOrderTemplateModel jnjOrderTemplateModel = new JnjOrderTemplateModel();
		jnjOrderTemplateModel.setCode(templateCode);
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final B2BUnitModel currentB2BUnit =  currentUser.getCurrentB2BUnit();
	
		final UserModel user = userService.getCurrentUser();
		//Getting the Template Model For required Code.
		jnjOrderTemplateModel = jnjGTOrderTemplateService.getJnjGTOrderTemplateModel(jnjOrderTemplateModel);
		 JnjGTVariantProductModel delVariant = new JnjGTVariantProductModel();
		final List<JnjTemplateEntryModel> templateEntryModelList = jnjOrderTemplateModel.getEntryList();
		 List<JnjTemplateEntryModel> tempEntryModelList = new ArrayList<JnjTemplateEntryModel>();
		 
		 if (shared)
			{
				jnjOrderTemplateModel.setVisibleTo(currentB2BUnit);
				jnjOrderTemplateModel.setShareStatus(ShareStatus.SHARED);
			}
			else
			{
				jnjOrderTemplateModel.setVisibleTo(user);
				jnjOrderTemplateModel.setShareStatus(ShareStatus.PRIVATE);
			}
		 
		modelService.removeAll(templateEntryModelList);
		
		final String[] products = tempProdQtys.split(SYMBOl_COMMA);
		for (final String product : products)
		{
			JnjTemplateEntryModel jnjTemplateEntryModel = new JnjTemplateEntryModel();
			final String[] product_Qq = product.split(COLON);
			
			final JnJProductModel productModel = jnjProductService.getProductCodeOrEAN(product_Qq[0]);
			
			productModel.setCatalogId(product_Qq[0]);
			if (null != productModel)
			{
				
				
					if (productModel instanceof JnJProductModel)
					{
						 delVariant = jnJGTProductService.getDeliveryGTIN(productModel);
					}

					jnjTemplateEntryModel.setProduct(productModel);
					Long qnt = Long.valueOf(product_Qq[1]);
					jnjTemplateEntryModel.setQty(qnt);
					jnjTemplateEntryModel.setReferencedVariant(delVariant);
					
					tempEntryModelList.add(jnjTemplateEntryModel);
		  }
	     }
		
		jnjOrderTemplateModel.setEntryList(tempEntryModelList);
		jnjOrderTemplateModel.setName(templateName);
		modelService.save(jnjOrderTemplateModel);
		confirmTemplateEdited = true;
		}
		catch (final ModelRemovalException e)
		{
			confirmTemplateEdited = false;
		}
		return confirmTemplateEdited;
}
	}