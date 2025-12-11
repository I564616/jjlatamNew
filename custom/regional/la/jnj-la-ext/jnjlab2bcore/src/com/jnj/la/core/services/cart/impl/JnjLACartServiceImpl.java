/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.cart.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.enums.ContractDocumentTypeEnum;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.cart.impl.DefaultJnjGTCartService;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.daos.impl.JnjLaProductDaoImpl;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.JnjLoadTranslationService;
import com.jnj.la.core.services.cart.JnjLACartService;
import com.jnj.la.core.services.contract.JnjContractService;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.services.CMSSiteService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Arrays;

public class JnjLACartServiceImpl extends DefaultJnjGTCartService implements JnjLACartService
{

	private static final Logger LOGGER = Logger.getLogger(JnjLACartServiceImpl.class);

	private final long forceInStockMaxQuantity = 999999L;
	protected static final String BASKET_PAGE_MESSAGE_QTY_UPDATE = "basket.page.message.update";
	protected static final String BASKET_PAGE_MESSAGE_QTY_ADJUSTED = "basket.page.message.qtyAdjusted";
	protected static final String BASKET_PAGE_MESSAGE_MIN_QTY_ADDED = "basket.page.message.minQtyAdded";

	@Resource(name = "jnjB2BUnitService")
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private JnjCustomerEligibilityService customerEligibilityService;

	@Autowired
	private JnjLoadTranslationService jnjLoadTranslationService;

	@Autowired
	protected JnJLaProductService jnjLaProductService;

	@Autowired
	protected B2BCartService b2bCartService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private B2BOrderService b2bOrderService;

	private JnjOrderService jnjOrderService;

	@Autowired
	private JnjLAOrderService jnjLAOrderService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjContractService jnjContractService;

	@Autowired
	CMSSiteService cMSSiteService;

	@Autowired
	private ConfigurationService configurationService;


	@Override
	public CommerceCartModification addToCart(final CartModel cartModel, final ProductModel productModel, final long quantityToAdd,
			final UnitModel unit, final boolean forceNewEntry, final boolean calculateFlag, final String catalogId,
			final JnjLaOrderEntryData entryData) throws CommerceCartModificationException
	{
		final String methodName = "addToCart";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.AddToCart.ADD_TO_CART, methodName, Logging.BEGIN_OF_METHOD,
				JnjLACartServiceImpl.class);

		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
		ServicesUtil.validateParameterNotNull(productModel, "Product model cannot be null");

		if (productModel.getVariantType() != null)
		{
			JnjGTCoreUtil.logInfoMessage("Add to Cart", methodName, "Product variant type should be null.",
					JnjLACartServiceImpl.class);
			throw new CommerceCartModificationException("Choose a variant instead of the base product");
		}

		// For contracts orders, the product code is used as material entered instead of the catalogId
		String materialEntered;
		if (StringUtils.isEmpty(cartModel.getContractNumber()))
		{
			materialEntered = catalogId;
		}
		else
		{
			materialEntered = productModel.getCode();
		}

		//Checks if the Product is NOT restricted as per ELIGIBILITY or doesn't fall under Default Category.
		final CommerceCartModification modification = checkCustomerAndProductEligibility(cartModel, productModel, quantityToAdd,
				unit, forceNewEntry, calculateFlag, materialEntered, entryData);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.AddToCart.ADD_TO_CART, methodName, Logging.END_OF_METHOD,
				JnjLACartServiceImpl.class);
		return modification;
	}

	private CommerceCartModification checkCustomerAndProductEligibility(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final UnitModel unit, final boolean forceNewEntry, final boolean calculateFlag,
			final String materialEntered, final JnjLaOrderEntryData entryData)
	{
		final String METHOD_NAME = "checkCustomerEligibility";
		JnjGTCoreUtil.logDebugMessage("Add to Cart", METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLACartServiceImpl.class);

		final CommerceCartModification modification = new CommerceCartModification();

		if (isProductAllignedToRestrictedCategory(productModel))
		{
			final OrderEntryModel entry = new OrderEntryModel();
			entry.setProduct(productModel);
			entry.setMaterialEntered(materialEntered);
			modification.setEntry(entry);
			modification.setQuantityAdded(quantityToAdd);
			modification.setQuantity(quantityToAdd);
			modification.setStatusCode(Jnjlab2bcoreConstants.ADDTO_CART_CATEGORY_ERROR);

			JnjGTCoreUtil.logErrorMessage("Add to Cart ", METHOD_NAME,
					"Product's category restricted or search restriction to fetch the product's category enabled.",
					JnjLaProductDaoImpl.class);
		}
		else
		{
			long finalquantityToAdd = Jnjb2bCoreConstants.DEFAULT_ADD_TO_CART_QTY;
			if (quantityToAdd > 1)
			{
				finalquantityToAdd = quantityToAdd;
			}
			/** Adding Information for order split - Start **/

			Map<String, String> sectorSalesOrgMapping = getSessionService()
					.getAttribute(Jnjb2bCoreConstants.Order.B2BUNIT_SALESORG_MAP);
			if (sectorSalesOrgMapping == null || sectorSalesOrgMapping.isEmpty())
			{
				sectorSalesOrgMapping = getJnjSalesOrgCustService().getSectorAndSalesOrgMapping();
				getSessionService().setAttribute(Jnjb2bCoreConstants.Order.B2BUNIT_SALESORG_MAP, sectorSalesOrgMapping);
			}
			final String country = getJnjGetCurrentDefaultB2BUnitUtil().getCurrentCountryForSite().getIsocode();
			String orderType = Jnjb2bCoreConstants.SAP_ORDER_TYPE_ZOR;
			String dispatcherSalesOrg = StringUtils.EMPTY;
			final String productSector = ((JnJProductModel) productModel).getSector() == null ? StringUtils.EMPTY
					: ((JnJProductModel) productModel).getSector().toUpperCase();
			if (null != sectorSalesOrgMapping && null != sectorSalesOrgMapping.get(productSector))
			{
				dispatcherSalesOrg = sectorSalesOrgMapping.get(productSector);
				if (Config.getParameter(Jnjlab2bcoreConstants.COLDCHAIN_RULE_ENABLE + country.toLowerCase())
						.equalsIgnoreCase(Jnjlab2bcoreConstants.Forms.TRUE))
				{
					//Check if product is cold Chain product.
					if (((JnJProductModel) productModel).getColdChainProduct().booleanValue())
					{
						//Sap order type will be ZORD if corresponding salesOrg does not have cold chain storage.
						if (!getJnjSalesOrgCustService().checkColdChainStorage(dispatcherSalesOrg))
						{
							orderType = Jnjb2bCoreConstants.SAP_ORDER_TYPE_ZORD;
						}
					}
				}
			}

			final UnitModel orderableUnit = unit;
			final List<CartEntryModel> entriesForProd = cartService.getEntriesForProduct(cartModel, productModel);
			if (CollectionUtils.isEmpty(entriesForProd) || entriesForProd.get(0).getQuantity().longValue() < forceInStockMaxQuantity)
			{
				final CartEntryModel cartEntryModel = cartService.addNewEntry(cartModel, productModel, finalquantityToAdd,
						orderableUnit, -1, !forceNewEntry);

				final JnjUomDTO customerDelUOM = jnjLoadTranslationService.getCustDelUomMapping((JnJProductModel) productModel);
				if (null != customerDelUOM)
				{
					final int salesUnitCoun = customerDelUOM.getSalesUnitsCount();
					cartEntryModel.setSalesUOM(Integer.valueOf(salesUnitCoun));
				}
				cartEntryModel.setSalesOrg(dispatcherSalesOrg);
				cartEntryModel.setMaterialEntered(materialEntered);
				cartEntryModel.setSapOrderType(orderType);
				if (null != entryData)
				{
					cartEntryModel.setIndirectCustomer(entryData.getIndirectCustomer());
					cartEntryModel.setIndirectCustomerName(entryData.getIndirectCustomerName());
					cartEntryModel.setIndirectPayer(entryData.getIndirectPayer());
					cartEntryModel.setIndirectPayerName(entryData.getIndirectPayerName());
				}
				if(StringUtils.isBlank(cartModel.getExternalOrderRefNumber()))
				{
					cartModel.setUnit(getJnjGetCurrentDefaultB2BUnitUtil().getDefaultB2BUnit());
			    }
				//Must be fixed, commented to resolve compilation issue.
				String orderChannel;
				if(StringUtils.isNotBlank(cartModel.getExternalOrderRefNumber()) && Objects.nonNull(cartModel.getOrderChannel())) {
					orderChannel = cartModel.getOrderChannel().getCode();
				}else {
					orderChannel = getJnjConfigService().getConfigValueById(Jnjlab2bcoreConstants.Order.ORDER_CHANNEL);
				}	
				try
				{
					cartModel.setOrderChannel(jnjLAOrderService.getOrderChannel(orderChannel));
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logWarnMessage("JnjLACartServiceImpl Exception", METHOD_NAME, "Message", businessException,
							JnjLACartServiceImpl.class);
					LOGGER.warn("Add to Cart - Product Code-" + productModel.getCode()
							+ "Cannot Fetch Order Channel from Hybris for channel code [" + orderChannel + "].");
				}
				updatePriceForEntry(cartEntryModel);
				if (calculateFlag)
				{
					calculateCart(cartModel);
				}
				modification.setQuantityAdded(quantityToAdd);
				modification.setQuantity(quantityToAdd);
				modification.setEntry(cartEntryModel);
				modification.setStatusCode("success");
			}
			else
			{
				modification.setStatusCode(Jnjb2bCoreConstants.MAX_QTY_LIMIT_EXCEED);
			}
			getModelService().save(cartModel);
		}

		JnjGTCoreUtil.logDebugMessage("Add to Cart", METHOD_NAME, Logging.END_OF_METHOD, JnjLACartServiceImpl.class);
		return modification;
	}

	/**
	 * Update price for entry with product Price.
	 *
	 * @param cartEntryModel
	 *           the cart entry model
	 */
	@Override
	protected void updatePriceForEntry(final AbstractOrderEntryModel cartEntryModel)
	{
		final ProductData productData = new ProductData();
		getProductPricePopulator().populate(cartEntryModel.getProduct(), productData);
		if (null != productData.getPrice() && null != productData.getPrice().getValue())
		{
			cartEntryModel.setBasePrice(Double.valueOf(productData.getPrice().getValue().doubleValue()));
		}
		else
		{
			cartEntryModel.setBasePrice(DEFAULT_VALUE);
		}
		cartEntryModel.setTotalFees(DEFAULT_VALUE);
		cartEntryModel.setTaxValues(Collections.EMPTY_LIST);
		cartEntryModel.setDiscountValues(Collections.EMPTY_LIST);
		cartEntryModel.setFreightFees(DEFAULT_VALUE);
		cartEntryModel.setGrossPrice(DEFAULT_VALUE);
		cartEntryModel.setInsurance(DEFAULT_VALUE);
		cartEntryModel.setHandlingFee(DEFAULT_VALUE);
		cartEntryModel.setDropshipFee(DEFAULT_VALUE);
		cartEntryModel.setMinimumOrderFee(DEFAULT_VALUE);
		cartEntryModel.setDefaultPrice(DEFAULT_VALUE);
	}

	/**
	 * This method gets the Active Version of the incoming Product to be added in Cart. In case the cart has a Contract,
	 * then if no active version is available we return the same incoming product.
	 *
	 * @param productModel
	 *           the product model
	 * @param contractNumber
	 *           the contract number
	 * @return ProductModel
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	@Override
	public ProductModel getActiveProduct(final JnJProductModel productModel, final String contractNumber)
			throws CommerceCartModificationException
	{
		final String METHOD_NAME = "getActiveProduct()";
		final String productCode = productModel.getCode();
		JnJProductModel activeProduct = null;
		if (StringUtils.isEmpty(contractNumber))
		{
			try
			{
				activeProduct = getJnJGTProductService().getActiveProduct(productModel);
			}
			catch (final BusinessException businessException)
			{
				LOGGER.error("Add to Cart - No active Product Found in Catlaog ID pool for Product Code [" + productCode + " ]. "
						+ businessException);
				throw new CommerceCartModificationException(
						"Add to Cart - No active Product Found in Catlaog ID pool for Product Code [" + productCode + " ]");
			}
			if (null == activeProduct)
			{
				LOGGER.error("Add to Cart - No active Product Found in Catlaog ID pool for Product Code [" + productCode + " ]");
				throw new CommerceCartModificationException(
						"Add to Cart - No active Product Found in Catlaog ID pool for Product Code [" + productCode + " ]");
			}
		}
		/*
		 * In case we do not have a contract added to the cart we don't care about active versions. If found then OK
		 * otherwise use the current product only.
		 */
		else
		{
			try
			{
				activeProduct = getJnJGTProductService().getActiveProduct(productModel);
			}
			catch (final BusinessException businessException)
			{
				JnjGTCoreUtil.logWarnMessage("JnjLACartServiceImpl Exception", METHOD_NAME, "Message", businessException,
						JnjLACartServiceImpl.class);
				LOGGER.info("Add to Cart - No active Product Found in Catlaog ID pool for Product Code [" + productCode
						+ " ]. Using the same product.");
				activeProduct = productModel;
			}
		}
		return activeProduct;
	}

	/**
	 * Checks and returns true if any of super categories of the product is/are restricted as per Customer Eligibility.
	 *
	 * @param product
	 * @return boolean
	 */
	@Override
	public boolean isProductAllignedToRestrictedCategory(final ProductModel product)
	{
		final String METHOD_NAME = "isProductAllignedToRestrictedCategory()";
		JnjGTCoreUtil.logDebugMessage("Add to Cart", METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLACartServiceImpl.class);

		final Collection<CategoryModel> productSuperCategories = product.getSupercategories();
		final Set<CategoryModel> categories = new HashSet<>();
		Collection<CategoryModel> superCategories = null;

		for (final CategoryModel category : productSuperCategories)
		{
			categories.add(category);
			superCategories = getSessionService().executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Collection<CategoryModel> execute()
				{
					return category.getAllSupercategories();
				}
			}, getUserService().getAdminUser());

			if (CollectionUtils.isNotEmpty(superCategories))
			{
				categories.addAll(superCategories);
			}
		}
		final B2BCustomerModel currentCustomer = (B2BCustomerModel) getUserService().getCurrentUser();
		final JnJB2BUnitModel unit = (JnJB2BUnitModel) currentCustomer.getDefaultB2BUnit();
		final Set<String> restrictedCategoryCodes = customerEligibilityService.getRestrictedCategory(unit.getUid());
		restrictedCategoryCodes.add(DEFAULT_CATEGORY_CODE);

		if (CollectionUtils.isEmpty(categories))
		{
			JnjGTCoreUtil.logInfoMessage("Add to Cart", METHOD_NAME,
					Logging.END_OF_METHOD + ". Product does not containsany category. Returning TRUE", JnjLACartServiceImpl.class);
			return true;
		}
		else
		{
			for (final CategoryModel category : categories)
			{
				if (restrictedCategoryCodes.contains(category.getCode()))
				{
					JnjGTCoreUtil.logInfoMessage("Add to Cart", METHOD_NAME,
							Logging.END_OF_METHOD
									+ ". Customer contains restriction to the product category or product contains the detault category. Returning TRUE",
							JnjLACartServiceImpl.class);
					return true;
				}
			}
		}

		JnjGTCoreUtil.logDebugMessage("Add to Cart", METHOD_NAME, Logging.END_OF_METHOD + " Returning FALSE",
				JnjLACartServiceImpl.class);
		return false;
	}

	/**
	 * This method is used for updating/adding the Indirect Customer.
	 *
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param cartEntryNumber
	 *           the cart entry number
	 * @return true, if successful
	 */
	@Override
	public boolean addIndirectCustomer(final String indirectCustomer, final String indirectCustName, final int cartEntryNumber)
	{
		final String METHOD_NAME = "addIndirectCustomer()";
		JnjGTCoreUtil.logDebugMessage("JnjLACartServiceImpl", METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLACartServiceImpl.class);

		boolean saved = false;
		final CartModel cartModel = getCartService().getSessionCart();
		if (indirectCustomer != null)
		{
			cartModel.setIndirectCustomer(indirectCustomer);
		}
		//Setting indirect customer for one selected entry
		if (cartEntryNumber >= 0)
		{
			final AbstractOrderEntryModel abstOrdEntModel = getEntryModelForNumber(cartModel, cartEntryNumber);
			abstOrdEntModel.setIndirectCustomer(indirectCustomer);
			abstOrdEntModel.setIndirectCustomerName(indirectCustName);
			saved = saveAbstOrderEntry(abstOrdEntModel);
		}
		//Setting indirect customer for whole cart
		else if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			// iterating each entry  to set the Indirect Customer (with MDD product only CR CP012)
			for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
			{
				if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.MDD_SECTOR,
						((JnJProductModel) abstOrdEntModel.getProduct()).getSector()))
				{
					abstOrdEntModel.setIndirectCustomer(indirectCustomer);
					abstOrdEntModel.setIndirectCustomerName(indirectCustName);
				}
			}
			saved = saveAbstOrderEntriesModels(cartModel.getEntries());
		}
		saveCartModel(cartModel, saved);
		JnjGTCoreUtil.logDebugMessage("JnjLACartServiceImpl", METHOD_NAME, Logging.END_OF_METHOD, JnjLACartServiceImpl.class);

		return saved;
	}

	@Override
	public boolean addIndirectPayer(final String indirectPayer, final String indirectPayerName, final int cartEntryNumber)
	{
		boolean saved = false;
		final CartModel cartModel = getCartService().getSessionCart();
		if (indirectPayer != null)
		{
			cartModel.setIndirectPayer(indirectPayer);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("After getting Cart Model");
		}

		//Setting indirect customer for one selected entry
		if (cartEntryNumber >= 0)
		{
			final AbstractOrderEntryModel abstOrdEntModel = getEntryModelForNumber(cartModel, cartEntryNumber);
			abstOrdEntModel.setIndirectPayer(indirectPayer);
			abstOrdEntModel.setIndirectPayerName(indirectPayerName);
			saved = saveAbstOrderEntry(abstOrdEntModel);
		}
		//Setting indirect payer for whole cart
		else if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			// iterating each entry  to set the Indirect Payer (with MDD product only CR CP012)
			for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
			{
				if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.MDD_SECTOR,
						((JnJProductModel) abstOrdEntModel.getProduct()).getSector()))
				{
					abstOrdEntModel.setIndirectPayer(indirectPayer);
					abstOrdEntModel.setIndirectPayerName(indirectPayerName);
				}
			}
			saved = saveAbstOrderEntriesModels(cartModel.getEntries());
		}
		saveCartModel(cartModel, saved);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("UpdateIndirectCustomer done");
		}
		return saved;
	}

	@Override
	public void createCartFromOrder(final String orderId, final JnjGTProductData productData)
	{
		final String METHOD_NAME = "createCartFromOrder()";
		final CartModel sessionCart = b2bCartService.getSessionCart();
		final OrderModel order = b2bOrderService.getOrderForCode(orderId);
		final String contractNumber = order.getContractNumber();
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		final List<String> invalidProductCodes = new ArrayList<>();
		final String lastAddedProductCode = null;
		String lastAddedProductQuantity = null;
		for (final AbstractOrderEntryModel entry : entries)
		{
			try
			{
				final ProductModel product = entry.getProduct();
				final String productCode = product.getCode();
				final ProductModel modProduct = jnjLaProductService.getActiveProductWithRestrictionCheck(product.getCode());
				final Boolean bonusItem = isFreeItem(entry);

				if (modProduct != null && !bonusItem)
				{
					LOGGER.info("---------->Add to cart call for variant product code : " + product.getCode() + " and product code :"
							+ modProduct.getCode());

					final CommerceCartModification commerceCartModification = this.addToCart(sessionCart, product,
							entry.getQuantity().longValue(), modProduct.getUnit(), false);

					if (null != commerceCartModification)
					{
						if (entry.getIndirectCustomer() != null && !StringUtils.EMPTY.equals(entry.getIndirectCustomer()))
						{
							addIndirectCustomer(entry.getIndirectCustomer(), entry.getIndirectCustomerName(), productCode);
						}
						if (entry.getIndirectPayer() != null && !StringUtils.EMPTY.equals(entry.getIndirectPayer()))
						{
							addIndirectPayer(entry.getIndirectPayer(), entry.getIndirectPayerName(), productCode);
						}
						lastAddedProductQuantity = Long.toString(commerceCartModification.getQuantityAdded());
					}
				}
				else
				{
					invalidProductCodes.add(productCode);
				}
			}
			catch (final CommerceCartModificationException modificaitonExp)
			{
				JnjGTCoreUtil.logWarnMessage("JnjLACartServiceImpl Exception", METHOD_NAME, "Message", modificaitonExp,
						JnjLACartServiceImpl.class);
				LOGGER.error("Creating cart from Order---  Product Code: " + entry.getProduct().getCode() + " from Order:" + orderId
						+ " can not be added to cart");
			}
		}

		if (productData != null)
		{
			productData.setLastAddedProduct(lastAddedProductCode);
			productData.setLastAddedProductQuantity(lastAddedProductQuantity);
			if (CollectionUtils.isNotEmpty(invalidProductCodes))
			{
				productData.setInvalidProductCodes(StringUtils.join(invalidProductCodes, Jnjb2bCoreConstants.SYMBOl_COMMA));
			}
		}
		if (contractNumber != null && !contractNumber.isEmpty())
		{
			sessionCart.setContractNumber(contractNumber);
			sessionCart.setIsContractCart(Boolean.TRUE);
		}
		else
		{
			sessionCart.setContractNumber(null);
			sessionCart.setIsContractCart(Boolean.FALSE);
		}
		saveCartModel(sessionCart, true);
	}

	private Boolean isFreeItem(final AbstractOrderEntryModel entry)
	{
		Boolean bonusItem = Boolean.FALSE;

		if (entry.getBasePrice() != null && entry.getBasePrice() <= 0)
		{
			bonusItem = Boolean.TRUE;
		}
		return bonusItem;
	}

	@Override
	public boolean saveCartModel(final AbstractOrderModel cartModel, final boolean saveEntires)
	{
		boolean saved = false;
		try
		{
			if (saveEntires)
			{
				saveAbstOrderEntriesModels(cartModel.getEntries());
			}
			getModelService().save(cartModel);
			saved = true;
		}
		catch (final ModelSavingException exception)
		{
			LOGGER.error("saveCartModel()" + Logging.HYPHEN + "Cart Model Not saved" + exception.getMessage(), exception);
		}

		return saved;
	}

	@Override
	public CommerceCartModification addToCart(final CartModel cartModel, final ProductModel productModel, final long quantityToAdd,
			final UnitModel unit, final boolean forceNewEntry) throws CommerceCartModificationException
	{
		final Date startTime = new Date();
		String addToCartMsgKey = "success";
		final CommerceCartModification cartModification = new CommerceCartModification();
		if (productModel instanceof JnJProductModel)
		{

			final JnjGTVariantProductModel variantProduct = null;
			/* setting JnjGTVariantProductModel to null since latam doesn't utilize variant for base product concept */
			final JnJProductModel modProduct = (JnJProductModel) productModel;
			JnJProductModel baseProduct = modProduct.getMaterialBaseProduct();

			if (baseProduct == null)
			{
				baseProduct = modProduct;
			}

			final UnitModel productUnit = baseProduct.getUnit();
			/**
			 *
			 * There is a different logic for add products in he cart depending on the Cart Type For Standard, No Charge,
			 * International, Consumer units will be as proportional to delivery GTIN, but for Delivered and Replenish
			 * order these will be same what user enters.
			 *
			 **/
			if (cartModel.getOrderType().equals(JnjOrderTypesEnum.ZDEL) || cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB))
			{
				addToCartMsgKey = addToCartDeliveredOrder(cartModel, quantityToAdd, addToCartMsgKey, modProduct, baseProduct,
						productUnit, cartModification);
			}
			else
			{
				addToCartMsgKey = addToCartGenric(cartModel, quantityToAdd, addToCartMsgKey, variantProduct, modProduct, baseProduct,
						productUnit, cartModification, false);
			}

			LOGGER.debug("Start : Calculate Cart");
			calculateCart(cartModel);
			LOGGER.debug("End : Calculate Cart");
			cartModification.setStatusCode(addToCartMsgKey);

			final Date endTime = new Date();

			LOGGER.debug("------>Total Time taken to execute JnjGTCartService.addToCart() : "
					+ (endTime.getTime() - startTime.getTime()) + "seconds");
		}
		return cartModification;
	}

	@Override
	protected String addToCartGenric(final CartModel cartModel, final long quantityToAdd, final String addToCartMsgKey,
			final JnjGTVariantProductModel variantProduct, final JnJProductModel modProduct, final JnJProductModel baseProduct,
			final UnitModel productUnit, final CommerceCartModification cartModification, final boolean forceNewEntry)
	{
		final List<CartEntryModel> entriesForProd = cartService.getEntriesForProduct(cartModel, baseProduct);
		final Date startTime = new Date();
		CartEntryModel cartEntryModel = null;
		modProduct.getMinOrderQuantity();

		if (CollectionUtils.isEmpty(entriesForProd))
		{
			long derivedQuantity = quantityToAdd;
			LOGGER.debug("Coming in the other block of addToCartGenric-------------------------------");
			if (quantityToAdd == 0 && null != modProduct.getMinOrderQuantity())
			{
				derivedQuantity = modProduct.getMinOrderQuantity();
				LOGGER.debug("inside non null min qty-------------------------------------" + variantProduct.getMinOrderQuantity());
			}
			if (quantityToAdd == 0 && modProduct.getMinOrderQuantity() == null)
			{
				derivedQuantity = 1;
				LOGGER.debug("inside null min qty-------------------------------------" + variantProduct.getMinOrderQuantity());
			}

			cartEntryModel = cartService.addNewEntry(cartModel, baseProduct, derivedQuantity, productUnit, -1, false);
			saveAbstOrderEntry(cartEntryModel);
			cartModification.setQuantityAdded(derivedQuantity);
		}
		/** If the Base Product(parents parent product) is exists in the cart **/
		else
		{
			LOGGER.debug("entering non-empty part-------------------------------------");
			cartEntryModel = entriesForProd.get(0);
			long finalQtyToAdd;
			if (quantityToAdd == 0 && null != modProduct.getMinOrderQuantity())
			{
				finalQtyToAdd = modProduct.getMinOrderQuantity();
			}
			else if (quantityToAdd == 0 && modProduct.getMinOrderQuantity() == null)
			{
				finalQtyToAdd = 1;
			}
			else
			{
				finalQtyToAdd = quantityToAdd;

				final long deliveryEnteredQty = finalQtyToAdd;
				final long existingQty = cartEntryModel.getQuantity().longValue();

				finalQtyToAdd = existingQty + deliveryEnteredQty;

				cartModification.setQuantityAdded(deliveryEnteredQty);
				cartEntryModel.setQuantity(Long.valueOf(finalQtyToAdd));
				cartEntryModel.setSalesOrg(jnjConfigService.getConfigValueById(Jnjb2bCoreConstants.Order.SALES_ORGANISATION));
				cartEntryModel.setSapOrderType(cartModel.getOrderType().getCode());
				cartEntryModel.setCalculated(Boolean.FALSE);
				cartModification.setProduct(baseProduct);
				cartModification.setEntry(cartEntryModel);

				saveAbstOrderEntry(cartEntryModel);

				final Date endTime = new Date();
				LOGGER.debug("---------->Total Time taken to execute JnjGTCartService.addToCart().addToCartGeneric() : "
						+ (endTime.getTime() - startTime.getTime()) / (1000) + "seconds");
			}
		}
		return addToCartMsgKey;
	}

	@Override
	public boolean calculateCart(final CartModel cartModel)
	{
		final String METHOD_NAME = "calculateCart()";
		boolean calculated = false;
		try
		{
			if (BooleanUtils.isTrue(cartModel.getSapValidated()))
			{
				resetEntriesToDefaultPrice(cartModel);
				cartModel.setSapValidated(Boolean.FALSE);
			}
			else
			{
				calculateEntries(cartModel);
			}
			calculateTotals(cartModel);
			calculated = saveCartModel(cartModel, true);
		}
		catch (final CalculationException calculateExp)
		{

			JnjGTCoreUtil.logWarnMessage("JnjLACartServiceImpl calculation Exception", METHOD_NAME, "Message", calculateExp,
					JnjLACartServiceImpl.class);
			LOGGER.error("cart calculaton not done");
		}
		return calculated;
	}

	public AbstractOrderEntryModel getEntryModelForCode(final CartModel cartModel, final String productCode)
	{
		return getEntryForCode(cartModel, productCode);
	}

	public AbstractOrderEntryModel getEntryForCode(final AbstractOrderModel order, final String productCode)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if ((entries != null) && (!(entries.isEmpty())))
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final JnJLaProductModel productModel = (JnJLaProductModel) entry.getProduct();
				if (productModel != null && productCode.equals(productModel.getCatalogId()))
				{
					return entry;
				}
			}
		}
		return null;
	}

	@Override
	public boolean addIndirectCustomer(final String indirectCustomer, final String indirectCustomerName, final String productCode)
	{
		final String METHOD_NAME = "addIndirectCustomer()";
		JnjGTCoreUtil.logDebugMessage("JnjLACartServiceImpl", METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLACartServiceImpl.class);

		boolean saved = false;
		// get the current cart model object by using Cart Service
		final CartModel cartModel = getCartService().getSessionCart();
		if (indirectCustomer != null)
		{
			cartModel.setIndirectCustomer(indirectCustomer);
		}
		//Setting indirect customer for one selected entry
		AbstractOrderEntryModel abstOrdEntModel = null;
		if (null != productCode && !StringUtils.EMPTY.equals(productCode))
		{
			abstOrdEntModel = getEntryModelForCode(cartModel, productCode);
		}
		if (null != abstOrdEntModel)
		{
			abstOrdEntModel.setIndirectCustomer(indirectCustomer);
			abstOrdEntModel.setIndirectCustomerName(indirectCustomerName);
			saved = saveAbstOrderEntry(abstOrdEntModel);
		}
		saveCartModel(cartModel, saved);

		JnjGTCoreUtil.logDebugMessage("JnjLACartServiceImpl", METHOD_NAME, Logging.END_OF_METHOD, JnjLACartServiceImpl.class);

		return saved;
	}

	@Override
	public boolean addIndirectPayer(final String indirectPayer, final String indirectPayerName, final String productCode)
	{
		boolean saved = false;
		final CartModel cartModel = getCartService().getSessionCart();
		if (indirectPayer != null)
		{
			cartModel.setIndirectPayer(indirectPayer);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("After getting Cart Model");
		}

		//Setting indirect customer for one selected entry
		if (null != productCode && !StringUtils.EMPTY.equals(productCode))
		{
			final AbstractOrderEntryModel abstOrdEntModel = getEntryModelForCode(cartModel, productCode);
			abstOrdEntModel.setIndirectPayer(indirectPayer);
			abstOrdEntModel.setIndirectPayerName(indirectPayerName);
			saved = saveAbstOrderEntry(abstOrdEntModel);
		}
		//Setting indirect payer for whole cart
		else if (!cartModel.getEntries().isEmpty())
		{
			// iterating each entry  to set the Indirect Payer (with MDD product only CR CP012)
			for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
			{
				if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.MDD_SECTOR,
						((JnJProductModel) abstOrdEntModel.getProduct()).getSector()))
				{
					abstOrdEntModel.setIndirectPayer(indirectPayer);
					abstOrdEntModel.setIndirectPayerName(indirectPayerName);
				}
			}
			saved = saveAbstOrderEntriesModels(cartModel.getEntries());
		}
		saveCartModel(cartModel, saved);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("UpdateIndirectCustomer done");
		}
		return saved;
	}

	@Override
	public boolean updateContractIdInCart(final String empty)
	{
		return false;
	}

	/**
	 * Remove products that are not from contractNum from Cart
	 *
	 * @param contractNum
	 * @return boolean
	 */
	public boolean removeNonContractProduct(final String contractNum)
	{
		boolean removeNonContractFlag = false;
		final CartModel cartModel = getCartService().getSessionCart();

		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			final List<AbstractOrderEntryModel> cartEntryModel = cartModel.getEntries();

			for (final AbstractOrderEntryModel abstractOrderEntryModel : cartEntryModel)
			{
				final String productCodeInEntry = abstractOrderEntryModel.getProduct().getCode();
				removeNonContractFlag = !isProductInContract(contractNum, productCodeInEntry);

				if (removeNonContractFlag)
				{
					modelService.remove(abstractOrderEntryModel);
				}
			}
		}
		return removeNonContractFlag;
	}

	/**
	 * This method returns order type for commercial user
	 *
	 * @param jnjContractModel
	 * @param cartModel
	 * @return JnjOrderTypesEnum
	 */
	@Override
	public JnjOrderTypesEnum calculateOrderTypeForCommercialUser(
			final JnjContractModel jnjContractModel, final CartModel cartModel) {
		final String countryIsoCode = cMSSiteService.getCurrentSite().getDefaultCountry().getIsocode();
		final JnJLaB2BUnitModel b2BUnitModel = (JnJLaB2BUnitModel) cartModel.getUnit();
		LOG.debug("Inside calculateOrderTypeForCommercialUser() countryIsoCode: " + countryIsoCode);
		LOG.debug("Inside calculateOrderTypeForCommercialUser() b2BUnitModel UID: " + b2BUnitModel.getUid());
		List<String> salesOrgList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(b2BUnitModel) && StringUtils.isNotBlank(b2BUnitModel.getIndustryCode1())
				&& getConfigurationService().getConfiguration()
						.getString(Jnjlab2bcoreConstants.ELIGIBLE_INDUSTRY_CODES_FOR_COMMERCIAL_USER, StringUtils.EMPTY)
						.contains(b2BUnitModel.getIndustryCode1())) {
			LOG.debug("Inside calculateOrderTypeForCommercialUser() hasIndustryCode: ");
			String cartSalesOrg = findSalesOrgForOrderCreation(b2BUnitModel, cartModel.getEntries());
			LOG.debug("Inside calculateOrderTypeForCommercialUser() cartSalesOrg: " + cartSalesOrg);
			b2BUnitModel.getSalesOrg().forEach(salesOrg -> salesOrgList.add(salesOrg.getSalesOrg()));
			if (Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(countryIsoCode)) {
				LOG.debug("Inside calculateOrderTypeForCommercialUser() BrazilOrder: ");
				return calculateOrderTypeForBR(cartModel, jnjContractModel, cartSalesOrg, salesOrgList);
			} else return getJnjOrderTypesEnum(jnjContractModel, cartSalesOrg);
		}
		return JnjOrderTypesEnum.ZOR;
	}

	private JnjOrderTypesEnum getJnjOrderTypesEnum(final JnjContractModel jnjContractModel, final String cartSalesOrg) {
		LOG.debug("Entered getJnjOrderTypesEnum() method. Called for all countries except Brazil");
		String productSalesOrg = getConfigurationService().getConfiguration()
				.getString(Jnjlab2bcoreConstants.CONTRACT_PRODUCT_SALES_ORG, StringUtils.EMPTY);
		List<String> productSalesOrgList = StringUtils.isNotEmpty(productSalesOrg) ? Arrays
				.asList(productSalesOrg.split(Jnjlab2bcoreConstants.CONST_COMMA)) : null;
				
		if (CollectionUtils.isNotEmpty(productSalesOrgList)) {
			for (String prodSalesOrg : productSalesOrgList) {
				if (StringUtils.isNotBlank(cartSalesOrg) && cartSalesOrg.equalsIgnoreCase(prodSalesOrg)
						&& ContractDocumentTypeEnum.ZCQ.equals(jnjContractModel.getDocumentType())) {
					return JnjOrderTypesEnum.ZOR;
				}
			}
		}
		LOG.debug("Exit getJnjOrderTypesEnum().");
		return JnjOrderTypesEnum.ZOR;
	}

	private JnjOrderTypesEnum calculateOrderTypeForBR(final CartModel cartModel, final JnjContractModel jnjContractModel,
													  final String cartSalesOrg, final List<String> salesOrgList) {
		LOG.debug("Entered calculateOrderTypeForBR() method");
		if (StringUtils.isNotBlank(cartSalesOrg)
				&& Jnjlab2bcoreConstants.BR_PHARMA_SALESORG.equalsIgnoreCase(cartSalesOrg)) {
			return calculateOrderTypeForPharmaSector(cartModel, jnjContractModel);
		} else if (salesOrgList.contains(Jnjlab2bcoreConstants.BR_MDD_SALESORG) || (StringUtils.isNotBlank(cartSalesOrg)
				&& Jnjlab2bcoreConstants.BR_MDD_SALESORG.equalsIgnoreCase(cartSalesOrg))) {
			if (ContractDocumentTypeEnum.ZCQ.equals(jnjContractModel.getDocumentType())) {
				return JnjOrderTypesEnum.ZOR;
			} else if (ContractDocumentTypeEnum.ZCI.equals(jnjContractModel.getDocumentType())) {
				return JnjOrderTypesEnum.ZINS;
			}
		}
		LOG.debug("Exit calculateOrderTypeForBR() method");
		return JnjOrderTypesEnum.ZOR;
	}

	private JnjOrderTypesEnum calculateOrderTypeForPharmaSector(final CartModel cartModel,
																final JnjContractModel jnjContractModel) {
		LOG.debug("Entered calculateOrderTypeForPharmaSector() method");
		LOG.info("Entered calculateOrderTypeForPharmaSector() contract type: " + jnjContractModel.getDocumentType());
		boolean isSameSoldToAndShipTo = isShipToSameAsSoldTo(cartModel);
		LOG.info("Inside calculateOrderTypeForPharmaSector() isSameSoldToAndShipTo? " + isSameSoldToAndShipTo);
		if(ContractDocumentTypeEnum.ZCQ.equals(jnjContractModel.getDocumentType())) {
			LOG.info("Inside calculateOrderTypeForPharmaSector() ZCQ contract ");
			return isSameSoldToAndShipTo ? JnjOrderTypesEnum.ZOR: JnjOrderTypesEnum.ZORD;
		} else if (ContractDocumentTypeEnum.ZCI.equals(jnjContractModel.getDocumentType())) {
			LOG.info("Inside calculateOrderTypeForPharmaSector() ZCI contract ");
			return isSameSoldToAndShipTo ? JnjOrderTypesEnum.ZINS: JnjOrderTypesEnum.ZIND;
		}
		LOG.info("Exit calculateOrderTypeForPharmaSector() method. OrderType will be ZOR (StandardOrder)");
		return JnjOrderTypesEnum.ZOR;
	}

	private boolean isShipToSameAsSoldTo(final CartModel cartModel) {
		final B2BUnitModel unit = cartModel.getUnit();
		String shipToId = cartModel.getDeliveryAddress() != null ? cartModel.getDeliveryAddress().getJnJAddressId() : "";
		LOG.info("Inside isShipToSameAsSoldTo(): Cart No: "+cartModel.getCode());
		LOG.info("Inside isShipToSameAsSoldTo(): Contract No: "+cartModel.getContractNumber());
		LOG.info("Inside isShipToSameAsSoldTo(): Sold-to is: "+unit.getUid());
		LOG.info("Inside isShipToSameAsSoldTo(): Ship-to is: "+shipToId);
		LOG.info("Inside isShipToSameAsSoldTo(): Is Ship-to same as Sold-to? "+shipToId.equals(unit.getUid()));
		return shipToId.equals(unit.getUid());
	}

	private String findSalesOrgForOrderCreation(final JnJLaB2BUnitModel jnjB2BUnit,
												final List<AbstractOrderEntryModel> orderEntriesList) {
		LOG.debug("Entered findSalesOrgForOrderCreation() method");
		String productSector = null;
		String salesOrgValue = Strings.EMPTY;
		if (CollectionUtils.isNotEmpty(orderEntriesList)) {
			final ProductModel product = orderEntriesList.get(0).getProduct();
			if (product instanceof JnJLaProductModel jnjLaProductModel) {
				productSector = jnjLaProductModel.getSector();
				LOG.debug("Inside findSalesOrgForOrderCreation() productSector: " + productSector);
			}
		}

		final List<JnJSalesOrgCustomerModel> salesOrgList = jnjB2BUnit.getSalesOrg();
		for (final JnJSalesOrgCustomerModel salesOrg : salesOrgList) {
			if (StringUtils.isNotBlank(salesOrg.getSector()) && StringUtils.isNotBlank(productSector)
					&& salesOrg.getSector().equalsIgnoreCase(productSector)) {
				LOG.debug("Inside  findSalesOrgForOrderCreation() found matching sector from salesOrg: " + salesOrg.getSector());
				salesOrgValue = salesOrg.getSalesOrg();
				break;
			}
		}
		LOG.debug("Exit findSalesOrgForOrderCreation(). B2bUnit SalesOrg: " + salesOrgValue);
		return salesOrgValue;
	}

	@Override
	protected boolean updateDeliveryAddress(final AddressModel addressModel) {
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setDeliveryAddress(addressModel);
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) cartModel.getUser();
		boolean contractCartFlag = cartModel.getIsContractCart() != null
				&& BooleanUtils.isTrue(cartModel.getIsContractCart()) && cartModel.getContractNumber() != null;
		if (BooleanUtils.isTrue(currentUser.getCommercialUserFlag()) && contractCartFlag) {
			final JnjContractModel jnjContractModel = getJnjContractService().getContractDetailsById(cartModel.getContractNumber());
			if (!ObjectUtils.isEmpty(jnjContractModel)) {
				cartModel.setOrderType(calculateOrderTypeForCommercialUser(jnjContractModel, cartModel));
			}
		}
		return saveCartModel(cartModel, false);
	}

	@Override
	public AddressModel changeShippingAddress(final String shippingAddressId) {
		final AddressModel addressModel = jnjAddressService.getAddressByPK(shippingAddressId);
		updateDeliveryAddress(addressModel);
		return addressModel;
	}

	/**
	 * Check is a product is in a Contract
	 *
	 * @param contractNum
	 * @param productCode
	 * @return
	 */
	private boolean isProductInContract(final String contractNum, final String productCode)
	{
		final JnjContractModel contract = getValidContract(contractNum);
		boolean isProductInContract = false;

		if (contract != null)
		{
			final List<com.jnj.core.model.JnjContractEntryModel> contractEntryList = contract.getJnjContractEntries();
			if (CollectionUtils.isNotEmpty(contractEntryList))
			{
				for (final JnjContractEntryModel contractEntryModel : contractEntryList)
				{
					if (contractEntryModel.getProduct().getCode().equals(productCode))
					{
						isProductInContract = true;
					}
				}
			}
		}
		return isProductInContract;
	}

	/**
	 * Retrieve contract by ECCContractNum
	 *
	 * @param contractNum
	 * @return
	 */
	private JnjContractModel getValidContract(final String contractNum)
	{
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();

		if (currentB2BUnit != null)
		{
			final Set<JnjContractModel> contractList = currentB2BUnit.getJnjContracts();
			if (CollectionUtils.isNotEmpty(contractList))
			{
				for (final JnjContractModel jnjContractModel : contractList)
				{
					if (jnjContractModel.getECCContractNum().equals(contractNum))
					{
						return jnjContractModel;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean updateComplementaryInfo(final String complementaryInfo)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setComplementaryInfo(complementaryInfo.trim());
		return saveCartModel(cartModel, false);
	}
	
	@Override
	public void setCustomerFreightType(final String customerFreightType)
	{
		final CartModel cartModel = cartService.getSessionCart();
		final JnJLaB2BUnitModel jnJB2bUnitModel = (JnJLaB2BUnitModel) cartModel.getUnit();
		
		String isoCode = null;
		if (null != jnJB2bUnitModel.getCountry())
		{
			isoCode = jnJB2bUnitModel.getCountry().getIsocode();
			if (isoCode.equals(Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL)) {
				cartModel.setCustomerFreightType(customerFreightType);
				saveCartModel(cartModel, false);
				
			}
		}
		
	}

	public JnjContractService getJnjContractService() {
		return jnjContractService;
	}

	public void setJnjContractService(final JnjContractService jnjContractService) {
		this.jnjContractService = jnjContractService;
	}

	public JnjOrderService getJnjOrderService()
	{
		return jnjOrderService;
	}

	public void setJnjOrderService(final JnjOrderService jnjOrderService)
	{
		this.jnjOrderService = jnjOrderService;
	}

	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public B2BCartService getB2bCartService()
	{
		return b2bCartService;
	}

	@Override
	public B2BOrderService getB2bOrderService()
	{
		return b2bOrderService;
	}

	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Override
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Override
	public JnjConfigService getJnjConfigService()
	{
		return jnjConfigService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
