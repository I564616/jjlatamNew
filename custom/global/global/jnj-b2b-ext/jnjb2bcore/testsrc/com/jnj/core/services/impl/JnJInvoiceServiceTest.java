/**
 * 
 */

package com.jnj.core.services.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jnj.core.model.JnJInvoiceOrderModel;




/**
 * @author ila.sharma
 * 
 */

@UnitTest
public class JnJInvoiceServiceTest
{
	private DefaultJnjInvoiceService invoiceService;

	@Mock
	private ModelService mockModelService;

	@Mock
	private JnJInvoiceOrderModel mockJnjOrder;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		invoiceService = new DefaultJnjInvoiceService();
		invoiceService.setModelService(mockModelService);
	}

	@Test
	public void testSaveInvoice()
	{
		final JnJInvoiceOrderModel mockOrder = Mockito.mock(JnJInvoiceOrderModel.class);
		Mockito.when(mockOrder.getInvDocNo()).thenReturn(null);
		Mockito.when(mockModelService.create(JnJInvoiceOrderModel.class)).thenReturn(mockJnjOrder);

		doNothing().when(mockModelService).save(mockJnjOrder);
	}

	@Test
	public void testExceptionForSaveInvoice()
	{
		final JnJInvoiceOrderModel mockOrder = Mockito.mock(JnJInvoiceOrderModel.class);
		Mockito.when(mockOrder.getInvDocNo()).thenReturn(null);
		Mockito.when(mockModelService.create(JnJInvoiceOrderModel.class)).thenReturn(mockJnjOrder);

		doThrow(new ModelSavingException("Invoice not saved")).when(mockModelService).save(mockJnjOrder);
	}
}
