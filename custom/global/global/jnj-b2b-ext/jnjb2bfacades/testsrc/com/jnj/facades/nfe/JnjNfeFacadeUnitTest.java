/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.nfe;


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
import org.w3._2000._09.xmldsig.KeyInfoType;
import org.w3._2000._09.xmldsig.SignatureType;

import br.inf.portalfiscal.nfe.Avulsa;
import br.inf.portalfiscal.nfe.Cana;
import br.inf.portalfiscal.nfe.InfNFe;
import br.inf.portalfiscal.nfe.TNFe;
import br.inf.portalfiscal.nfe.TNfeProc;
import br.inf.portalfiscal.nfe.TProtNFe;

import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.nfe.impl.DefaultJnjNfeFacade;
import com.jnj.facades.nfe.mapper.JnjNfeMapper;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ElectronicNotaFiscalResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ReceiveElectronicNotaFiscalFromHybrisWrapper;


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



	private DefaultJnjNfeFacade defaultjnjNfeFacade;
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
		defaultjnjNfeFacade = new DefaultJnjNfeFacade();
		final JnJInvoiceOrderModel jnJInvoiceOrderModel = new JnJInvoiceOrderModel();
		jnJInvoiceOrderModel.setInvDocNo("123");
		jnJInvoiceOrderModel.setRegion("region");
		jnJInvoiceOrderModel.setNfYear("nFYEAR");
		jnJInvoiceOrderModel.setNfMonth("nFMONTH");
		jnJInvoiceOrderModel.setStcd("sTCD");
		jnJInvoiceOrderModel.setModel("mODEL");
		jnJInvoiceOrderModel.setSeries("sERIES");
		jnJInvoiceOrderModel.setNfNumber("nFNUMBER");
		jnJInvoiceOrderModel.setDocNumber("docNumer");
		jnJInvoiceOrderModel.setCdv("cDV");


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


		ReflectionTestUtils.setField(defaultjnjNfeFacade, "jnjInvoiceService", jnjInvoiceService);
		ReflectionTestUtils.setField(defaultjnjNfeFacade, "jnjNfeMapper", jnjNfeMapper);

		given(jnjInvoiceService.getInvoicebyCode("123")).willReturn(jnJInvoiceOrderModel);
		given(
				jnjNfeMapper.mapNfeRequestData((JnJInvoiceOrderModel) any(),
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
	public void testGetNfeFile() throws ModelNotFoundException, BusinessException, IllegalArgumentException, IntegrationException,
			JAXBException
	{
		//Registry.activateMasterTenant();
		final String invDocNo = "123";
		final File xmlFile = defaultjnjNfeFacade.getNfeFile(invDocNo);
		Assert.assertNotNull(xmlFile);
	}

}
