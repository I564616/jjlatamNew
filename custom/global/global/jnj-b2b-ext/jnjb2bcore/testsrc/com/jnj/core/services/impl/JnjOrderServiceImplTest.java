/**
 * 
 */
package com.jnj.core.services.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jnj.core.services.cart.impl.DefaultJnjCartService;


/**
 * The JnjOrderServiceImplTest class is used to test the method of JnjOrderServiceImpl class.
 * 
 * @author sumit.y.kumar
 * 
 */
public class JnjOrderServiceImplTest
{
	private DefaultJnjCartService defaultjnjCartService;
	@Mock
	private ModelService mockModelService;
	@Mock
	private CartModel cartModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultjnjCartService = new DefaultJnjCartService();
		defaultjnjCartService.setModelService(mockModelService);

	}

	/**
	 * The testSaveSimulateOrderData method is used to test the saveSimulateOrderData operation of JnjOrderServiceImpl
	 * class.
	 * 
	 */
	@Test
	public void testSaveSimulateOrderData()
	{
		Mockito.when(mockModelService.create(CartModel.class)).thenReturn(cartModel);

		doNothing().when(mockModelService).save(cartModel);

		final boolean saveSimulateOrderData = defaultjnjCartService.saveCartModel(cartModel, true);
		Assert.assertTrue(saveSimulateOrderData);
	}

	/**
	 * The testExceptionForSaveSimulateOrderData method is used to test the saveSimulateOrderData operation of
	 * JnjOrderServiceImpl class for the exception scenario.
	 * 
	 */
	@Test
	public void testExceptionForSaveSimulateOrderData()
	{
		Mockito.when(mockModelService.create(OrderModel.class)).thenReturn(cartModel);

		doThrow(new ModelSavingException("not saved")).when(mockModelService).save(cartModel);

		final boolean saveSimulateOrderData = defaultjnjCartService.saveCartModel(cartModel, true);
		Assert.assertTrue(saveSimulateOrderData);
	}

}
