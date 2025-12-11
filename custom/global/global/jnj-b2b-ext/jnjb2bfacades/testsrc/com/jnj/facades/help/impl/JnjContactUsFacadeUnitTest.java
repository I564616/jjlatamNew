package com.jnj.facades.help.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.jnj.core.dto.JnjContactUsDTO;
import com.jnj.core.services.impl.DefaultJnjContactUsService;


/**
 * Test class for Facade layer of Contact us flow
 * 
 * @author sanchit.a.kumar
 * 
 */
@UnitTest
public class JnjContactUsFacadeUnitTest
{

	private DefaultJnjContactUsFacade defaultjnjContactUsFacade;
	@Mock
	private JnjContactUsDTO mockJnjContactUsDTO;
	@Mock
	private DefaultJnjContactUsService mockJnjContactUsServiceImpl;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		defaultjnjContactUsFacade = new DefaultJnjContactUsFacade();

		Mockito.when(Boolean.valueOf(mockJnjContactUsServiceImpl.sendMessage(mockJnjContactUsDTO))).thenReturn(Boolean.TRUE);
		ReflectionTestUtils.setField(defaultjnjContactUsFacade, "defaultjnjContactUsService", mockJnjContactUsServiceImpl);
	}

	/**
	 * Test method for
	 * {@link com.jnj.facades.help.impl.JnjContactUsFacadeImpl#sendMessage(com.jnj.core.dto.JnjContactUsDTO)}.
	 */
	@Test
	public void testSendMessage()
	{
		Assert.assertTrue(defaultjnjContactUsFacade.sendMessage(mockJnjContactUsDTO));
	}
}
