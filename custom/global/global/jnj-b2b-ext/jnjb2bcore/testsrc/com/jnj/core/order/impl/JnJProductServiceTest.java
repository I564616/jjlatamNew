/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.jnj.core.order.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.impl.DefaultJnJProductService;


@UnitTest
public class JnJProductServiceTest
{
	private DefaultJnJProductService productService;
	@Mock
	private SessionService mockSessionService;
	@Mock
	private ProductDao mockProductDao;
	@Mock
	private ModelService mockModelService;

	@Mock
	private JnJProductModel mockJnjProduct;
	@Mock
	private CatalogVersionModel mockCatalogVersion;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		productService = new DefaultJnJProductService();
		productService.setModelService(mockModelService);
		productService.setProductDao(mockProductDao);
		productService.setSessionService(mockSessionService);
	}

	@Test
	public void testSaveProduct()
	{
		//given
		final CatalogModel mockCatalog = Mockito.mock(CatalogModel.class);
		Mockito.when(mockCatalog.getId()).thenReturn(null);
		Mockito.when(mockCatalogVersion.getCatalog()).thenReturn(mockCatalog);
		Mockito.when(mockModelService.create(JnJProductModel.class)).thenReturn(mockJnjProduct);

		//mockProduct = Mockito.mock(JnJProductModel.class);
		final JnJProductModel product = productService.createModel();
		productService.setModelService(mockModelService);

		final CatalogModel catalog = Mockito.mock(CatalogModel.class);
		catalog.setId("powertoolsProductCatalog");
		final CatalogModel catalogModel = Mockito.mock(CatalogModel.class);
		final CatalogVersionModel catver = catalogModel.getActiveCatalogVersion();
		product.setCatalogVersion(catver);
		product.setCode("test_07601");

		//given
		doNothing().when(mockModelService).save(mockJnjProduct);


		//when
		final boolean saved = productService.saveProduct(product);

		//then
		Assert.assertTrue(saved);
	}


	@Test
	public void testExceptionForSaveProduct()
	{
		//given
		final CatalogModel mockCatalog = Mockito.mock(CatalogModel.class);
		Mockito.when(mockCatalog.getId()).thenReturn(null);
		Mockito.when(mockCatalogVersion.getCatalog()).thenReturn(mockCatalog);
		Mockito.when(mockModelService.create(JnJProductModel.class)).thenReturn(mockJnjProduct);



		//mockProduct = Mockito.mock(JnJProductModel.class);
		final JnJProductModel product = productService.createModel();
		productService.setModelService(mockModelService);

		final CatalogModel catalog = Mockito.mock(CatalogModel.class);
		catalog.setId("powertoolsProductCatalog");
		final CatalogModel catalogModel = Mockito.mock(CatalogModel.class);
		final CatalogVersionModel catver = catalogModel.getActiveCatalogVersion();
		product.setCatalogVersion(catver);
		//product.setCode("test_07601");		

		doThrow(new ModelSavingException("Product not saved")).when(mockModelService).save(mockJnjProduct);


		final boolean saved = productService.saveProduct(product);
		Assert.assertFalse(saved);

		//when


	}
}
