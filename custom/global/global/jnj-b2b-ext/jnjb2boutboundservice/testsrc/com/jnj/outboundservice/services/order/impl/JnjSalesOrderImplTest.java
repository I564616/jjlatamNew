/**
 * 
 */
package com.jnj.outboundservice.services.order.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationResponse;


/**
 * @author sumit.y.kumar
 * 
 */
public class JnjSalesOrderImplTest
{
	private DefaultJnjSalesOrder defaultjnjOrderFacade;

	@Mock
	private SalesOrderSimulationRequest salesOrderSimulationRequest;

	@Mock
	private SalesOrderSimulationResponse salesOrderSimulationResponse;

	/*
	 * @Mock private ProductModel mockProduct;
	 * 
	 * @Mock private JnJProductModel mockJnjProduct;
	 * 
	 * @Mock private CatalogVersionModel mockCatalogVersion;
	 * 
	 * @Before public void setUp() { MockitoAnnotations.initMocks(this);
	 * 
	 * productService = new JnJProductServiceImpl(); productService.setModelService(mockModelService);
	 * productService.setProductDao(mockProductDao); productService.setSessionService(mockSessionService); }
	 * 
	 * @Test public void testSaveProduct() { //given final CatalogModel mockCatalog = Mockito.mock(CatalogModel.class);
	 * Mockito.when(mockCatalog.getId()).thenReturn(null);
	 * Mockito.when(mockCatalogVersion.getCatalog()).thenReturn(mockCatalog);
	 * Mockito.when(mockModelService.create(JnJProductModel.class)).thenReturn(mockJnjProduct);
	 * 
	 * //mockProduct = Mockito.mock(JnJProductModel.class); final JnJProductModel product = productService.createModel();
	 * productService.setModelService(mockModelService);
	 * 
	 * final CatalogModel catalog = Mockito.mock(CatalogModel.class); catalog.setId("powertoolsProductCatalog"); final
	 * CatalogModel catalogModel = Mockito.mock(CatalogModel.class); final CatalogVersionModel catver =
	 * catalogModel.getActiveCatalogVersion(); product.setCatalogVersion(catver); product.setCode("test_07601");
	 * 
	 * //given doNothing().when(mockModelService).save(mockJnjProduct);
	 * 
	 * 
	 * //when final boolean saved = productService.saveProduct(product);
	 * 
	 * //then Assert.assertTrue(saved); }
	 */

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultjnjOrderFacade = new DefaultJnjSalesOrder();



	}

	@Test
	public void testSaveProduct()
	{
		//given
		final SalesOrderSimulationRequest mockCatalog = Mockito.mock(SalesOrderSimulationRequest.class);


		Mockito.when(defaultjnjOrderFacade.getWebServiceTemplate().marshalSendAndReceive(mockCatalog)).thenReturn(
				salesOrderSimulationResponse);



		//given
		//doNothing().when(jnjOrderFacadeImpl).getWebServiceTemplate().marshalSendAndReceive(mockCatalog);


		//when
		//final boolean saved = productService.saveProduct(product);

		//then
		//Assert.assertTrue(saved);
	}
}
