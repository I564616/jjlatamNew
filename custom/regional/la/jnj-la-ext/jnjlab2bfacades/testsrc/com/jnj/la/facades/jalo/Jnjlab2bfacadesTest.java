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
package com.jnj.la.facades.jalo;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.order.impl.JnjLatamInvoiceDocMapperImpl;
import com.jnj.outboundservice.invoice.ElectronicBillingResponse;


/**
 * JUnit Tests for the Jnjlab2bfacades extension
 */
public class Jnjlab2bfacadesTest extends HybrisJUnit4TransactionalTest
{
	/** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(Jnjlab2bfacadesTest.class.getName());

	@Before
	public void setUp()
	{
		// implement here code executed before each test
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 *
	 * @throws IntegrationException
	 * @throws BusinessException
	 */
	@Test
	public void testJnjlab2bfacades() throws BusinessException, IntegrationException
	{
		//final boolean testTrue = true;


		final JnjLatamInvoiceDocMapperImpl obj = new JnjLatamInvoiceDocMapperImpl();

		final ElectronicBillingResponse resp = obj.getInvoiceDocMapper("PDF", "0090091554");


		System.out.println("in the latam facadessssssssss" + resp);
		assertTrue("response is coming", !(resp.equals(null)));
	}
}
