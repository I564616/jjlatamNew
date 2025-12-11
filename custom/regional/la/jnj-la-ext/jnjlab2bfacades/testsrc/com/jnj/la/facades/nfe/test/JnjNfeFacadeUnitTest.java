/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.facades.nfe.test;


import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.io.File;

import jakarta.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.nfe.impl.DefaultJnjNfeFacade;
import com.jnj.la.facades.nfe.mapper.JnjNfeMapper;
import com.jnj.outboundservice.nfe.Avulsa;
import com.jnj.outboundservice.nfe.Cana;
import com.jnj.outboundservice.nfe.ElectronicNotaFiscalResponse;
import com.jnj.outboundservice.nfe.InfNFe;
import com.jnj.outboundservice.nfe.KeyInfoType;
import com.jnj.outboundservice.nfe.ReceiveElectronicNotaFiscalFromHybrisWrapper;
import com.jnj.outboundservice.nfe.SignatureType;
import com.jnj.outboundservice.nfe.TNFe;
import com.jnj.outboundservice.nfe.TNfeProc;
import com.jnj.outboundservice.nfe.TProtNFe;




/**
 * Integration Test for class JnjNfeFacadeImpl
 *
 * @author Accenture
 * @version 1.0
 */
@UnitTest
public class JnjNfeFacadeUnitTest
{

	@Mock
	private JnjNfeMapper jnjNfeMapper;

	@Mock
	private JnjInvoiceService jnjInvoiceService;



	private DefaultJnjNfeFacade jnjNfeFacadeImpl;
	private ElectronicNotaFiscalResponse electronicNotaFiscalResponse;

	/**
	 *
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		jnjNfeFacadeImpl = new DefaultJnjNfeFacade();
		final JnJInvoiceOrderModel jnJInvoiceOrderModel = new JnJInvoiceOrderModel();
		jnJInvoiceOrderModel.setInvDocNo("0090016922");


		//jnJInvoiceOrderModel.setRegion("region");
		jnJInvoiceOrderModel.setNfYear("15");
		//jnJInvoiceOrderModel.setNfMonth("nFMONTH");
		//jnJInvoiceOrderModel.setStcd("sTCD");
		//jnJInvoiceOrderModel.setModel("mODEL");
		//jnJInvoiceOrderModel.setSeries("sERIES");
		//jnJInvoiceOrderModel.setNfNumber("nFNUMBER");
		//jnJInvoiceOrderModel.setDocNumber("docNumer");
		//jnJInvoiceOrderModel.setCdv("cDV");
		jnJInvoiceOrderModel.setBillType("ZF9");
		jnJInvoiceOrderModel.setBillingDoc("X");
		jnJInvoiceOrderModel.setNetValue(628.00);
		jnJInvoiceOrderModel.setPoNumber("00125554");



		electronicNotaFiscalResponse = new ElectronicNotaFiscalResponse();
		final InfNFe infNFe = new InfNFe();
		infNFe.setAvulsa(new Avulsa());
		infNFe.setCana(new Cana());

		final SignatureType signatureType = new SignatureType();
		signatureType.setId("testSig");
		signatureType.setKeyInfo(new KeyInfoType());

		final TNFe tNFe = new TNFe();
		tNFe.setInfNFe(infNFe);
		tNFe.setSignature(signatureType);

		final TNfeProc tNfeProc = new TNfeProc();
		tNfeProc.setNFe(tNFe);
		tNfeProc.setVersao("Versao");
		tNfeProc.setProtNFe(new TProtNFe());

		electronicNotaFiscalResponse.setNfeProc(tNfeProc);


		ReflectionTestUtils.setField(jnjNfeFacadeImpl, "jnjInvoiceService", jnjInvoiceService);
		ReflectionTestUtils.setField(jnjNfeFacadeImpl, "jnjNfeMapper", jnjNfeMapper);

		given(jnjInvoiceService.getInvoicebyCode("0090016922")).willReturn(jnJInvoiceOrderModel);
		given(jnjNfeMapper.mapNfeRequestData((JnJInvoiceOrderModel) any(),
				(ReceiveElectronicNotaFiscalFromHybrisWrapper) any())).willReturn(electronicNotaFiscalResponse);


	}

	/**
	 * Test method for {@link com.jnj.facades.nfe.impl.JnjNfeFacadeImpl#getNfeFile(java.lang.String)}.
	 *
	 * @throws JAXBException
	 * @throws IntegrationException
	 * @throws IllegalArgumentException
	 * @throws BusinessException
	 * @throws ModelNotFoundException
	 */
	@Test
	public void testGetNfeFile()
			throws ModelNotFoundException, BusinessException, IllegalArgumentException, IntegrationException, JAXBException
	{
		//Registry.activateMasterTenant();
		final String invDocNo = "0090016922";
		final File xmlFile = jnjNfeFacadeImpl.getNfeFile(invDocNo);
		System.out.println("Xml File Value" + xmlFile);
		Assert.assertNotNull(xmlFile);
	}

}
