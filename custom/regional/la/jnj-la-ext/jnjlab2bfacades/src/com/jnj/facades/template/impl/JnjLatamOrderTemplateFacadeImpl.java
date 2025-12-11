/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.template.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.dto.JnjGTTemplateDetailsData;
import com.jnj.core.enums.ShareStatus;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjOrderTemplateModel;
import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.jnj.facades.data.JnjTemplateEntryData;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.cart.JnjLACartService;


/**
 *
 */
public class JnjLatamOrderTemplateFacadeImpl extends DefaultJnjGTOrderTemplateFacade
{

	@Autowired
	private JnjLACartService jnjLaCartService;

	@Autowired
	private JnJLaProductService jnjLaProductService;


	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private CategoryService categoryService;


	@Resource(name = "jnjLaOrderTemplateEntryConverter")
	protected Converter<JnjTemplateEntryModel, JnjTemplateEntryData> jnjLaOrderTemplateEntryConverter;

	protected static final String RESTRICTED_PRODUCT = "restricted";

	protected static final String INACTIVE_PRODUCT = "inactive";

	@Override
	public String updateProductAndQuantityForTemplate(final JnjGTTemplateDetailsData jnjGTTemplateDetailsForm)
	{
		final String METHOD_NAME = "updateProductAndQuantityForTemplate";
		ProductModel product = null;

		try
		{
			final String catalogId = jnjGTTemplateDetailsForm.getProductCode();
			product = jnjLaProductService.getProductForCatalogId(catalogId);
		}
		catch (final UnknownIdentifierException ex)
		{
			JnjGTCoreUtil.logErrorMessage("JnjLatamOrderTemplateFacadeImplException", METHOD_NAME, "Message.", ex,
					JnjLatamOrderTemplateFacadeImpl.class);
			return INVALID_PRODUCT;
		}
		catch (final Exception ex)
		{
			JnjGTCoreUtil.logErrorMessage("JnjLatamOrderTemplateFacadeImplException", METHOD_NAME, "Message.", ex,
					JnjLatamOrderTemplateFacadeImpl.class);
			return INVALID_PRODUCT;
		}

		if (null != product)
		{

			final JnJLaProductModel productModelToValidate = (JnJLaProductModel) product;
			boolean isProductRestricted = false;
			String productDetails = null;
			if (productModelToValidate != null)
			{
				isProductRestricted = jnjLaCartService.isProductAllignedToRestrictedCategory(productModelToValidate);
				if (isProductRestricted)
				{
					return RESTRICTED_PRODUCT;
				}
				else
				{
					productDetails = productModelToValidate.getCatalogId() + " $$$ " + productModelToValidate.getName() + " $$$ "
							+ productModelToValidate.getNumeratorDUOM() + " $$$ "
							+ productModelToValidate.getBaseUnitOfMeasure().getName() + " $$$ "
							+ productModelToValidate.getUnit().getName();
				}
			}

			return productDetails;
		}
		else
		{
			return INVALID_PRODUCT;
		}
	}

	@Override
	public boolean editExistingTemplate(final String tempProdQtys, final String templateName, final boolean shared,
			final String templateCode)
	{
		final String METHOD_NAME = "editExistingTemplate";
		boolean confirmTemplateEdited = false;
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
				Jnjb2bCoreConstants.ONLINE);

		try
		{
			JnjOrderTemplateModel jnjOrderTemplateModel = new JnjOrderTemplateModel();
			jnjOrderTemplateModel.setCode(templateCode);
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			final B2BUnitModel currentB2BUnit = currentUser.getCurrentB2BUnit();

			final UserModel user = userService.getCurrentUser();
			//Getting the Template Model For required Code.
			jnjOrderTemplateModel = jnjGTOrderTemplateService.getJnjGTOrderTemplateModel(jnjOrderTemplateModel);
			final List<JnjTemplateEntryModel> templateEntryModelList = jnjOrderTemplateModel.getEntryList();
			final List<JnjTemplateEntryModel> tempEntryModelList = new ArrayList<>();

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
			for (final String productCode : products)
			{
				final JnjTemplateEntryModel jnjTemplateEntryModel = new JnjTemplateEntryModel();
				final String[] product_Qq = productCode.split(COLON);

				JnJLaProductModel productModel = null;

				if (product_Qq != null && product_Qq[0] != null)
				{

					try
					{

						productModel = (JnJLaProductModel) jnjLaProductService.getProductForCatalogId(catalogVersionModel,
								product_Qq[0].trim());
					}

					catch (final BusinessException e)
					{
						JnjGTCoreUtil.logErrorMessage("BusinessException", "createNewTemplate", "Message.", e,
								JnjLatamOrderTemplateFacadeImpl.class);
					}
				}



				if (null != productModel)
				{
					productModel.setCatalogId(product_Qq[0]);
					jnjTemplateEntryModel.setProduct(productModel);
					final Long qnt = Long.valueOf(product_Qq[1]);
					jnjTemplateEntryModel.setQty(qnt);

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
			JnjGTCoreUtil.logErrorMessage("JnjLatamOrderTemplateFacadeImplException", METHOD_NAME, "Message.", e,
					JnjLatamOrderTemplateFacadeImpl.class);
			confirmTemplateEdited = false;
		}
		return confirmTemplateEdited;
	}

	@Override
	public boolean createNewTemplate(final String tempProds, final String templateName, final boolean shared)
	{
		boolean confirmTemplateCreated = false;
		try
		{
			final AbstractOrderModel cart = new AbstractOrderModel();

			final List<AbstractOrderEntryModel> cartEntryList = new ArrayList();
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			final B2BUnitModel currentB2BUnit = currentUser.getCurrentB2BUnit();
			final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
			final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();


			final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
					Jnjb2bCoreConstants.ONLINE);


			final UserModel user = userService.getCurrentUser();
			final String[] products = tempProds.split(SYMBOl_COMMA);
			for (final String product : products)
			{
				final AbstractOrderEntryModel cartEntry = new AbstractOrderEntryModel();
				final String[] product_Qq = product.split(COLON);
				JnJLaProductModel productModel = null;
				if (product_Qq != null && product_Qq[0] != null)
				{
					try
					{

						productModel = (JnJLaProductModel) jnjLaProductService.getProductForCatalogId(catalogVersionModel,
								product_Qq[0].trim());
					}

					catch (final BusinessException e)
					{
						JnjGTCoreUtil.logErrorMessage("BusinessException", "createNewTemplate", "Message.", e,
								JnjLatamOrderTemplateFacadeImpl.class);
					}
				}
				if (null != productModel)
				{
					productModel.setCatalogId(product_Qq[0]);
					cartEntry.setProduct(productModel);
					final Long qnt = Long.valueOf(product_Qq[1]);
					cartEntry.setQuantity(qnt);
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
			JnjGTCoreUtil.logErrorMessage("Create new Template", "createNewTemplate()", "Exception", e,
					JnjLatamOrderTemplateFacadeImpl.class);
		}
		return confirmTemplateCreated;
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
		templateData.setOrderEntry(Converters.convertAll(entryModels, jnjLaOrderTemplateEntryConverter));
		return templateData.getOrderEntry();
	}
}
