/**
 * 
 */
package com.jnj.core.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.jnj.core.services.cart.impl.DefaultJnjCartService;



/**
 * The JnjCartServiceImplTest class is used to test all methods of JnjCartServiceImpl class.
 * 
 * @author sumit.y.kumar
 * 
 */
@UnitTest
public class JnjCartServiceImplTest
{
	private DefaultJnjCartService defaultjnjCartService;

	@Mock
	ModelService modelServiceMock;

	@Mock
	CartService cartServiceMock;



	@Mock
	ProductService productServiceMock;

	@Mock
	CartModel cartModelMock;

	@Mock
	CartEntryModel cartEntryModelMock;


	@Mock
	AbstractOrderEntryModel abstEntryModelMock;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultjnjCartService = new DefaultJnjCartService();
		ReflectionTestUtils.setField(defaultjnjCartService, "cartService", cartServiceMock);
		ReflectionTestUtils.setField(defaultjnjCartService, "productService", productServiceMock);
		ReflectionTestUtils.setField(defaultjnjCartService, "modelService", modelServiceMock);

	}

}
