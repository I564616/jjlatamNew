/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facede.nfe.mapper;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultI18NService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.nfe.mapper.JnjNfeMapper;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ElectronicNotaFiscalResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ReceiveElectronicNotaFiscalFromHybrisWrapper;
import com.jnj.outboundservice.services.nfe.JnjNfeService;


/**
 * Junit for JnjNfeMapper.
 * 
 * @author Accenture
 * @version 1.0
 */
@UnitTest
public class JnjNfeMapperUnitTest
{

	@Mock
	private DefaultI18NService defaultI18NServiceMock;

	/** The jnj nfe service. */
	@Mock
	private JnjNfeService jnjNfeServiceMock;

	@Mock
	private CommonI18NService commonI18NService;

	private JnjNfeMapper jnjNfeMapper;
	private JnJInvoiceOrderModel jnJInvoiceOrderModel;
	private ElectronicNotaFiscalResponse electronicNotaFiscalResponse;
	private ReceiveElectronicNotaFiscalFromHybrisWrapper receiveElectronicNotaFiscalFromHybrisWrapper;



	@Before
	public void setUp() throws IntegrationException
	{
		MockitoAnnotations.initMocks(this);

		defaultI18NServiceMock = Mockito.mock(DefaultI18NService.class);
		jnjNfeMapper = new JnjNfeMapper();
		jnJInvoiceOrderModel = new JnJInvoiceOrderModel();
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

		receiveElectronicNotaFiscalFromHybrisWrapper = new ReceiveElectronicNotaFiscalFromHybrisWrapper();

		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("br");

		ReflectionTestUtils.setField(jnjNfeMapper, "jnjNfeService", jnjNfeServiceMock);
		ReflectionTestUtils.setField(jnjNfeMapper, "commonI18NService", commonI18NService);

		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);
		given(jnjNfeServiceMock.getNfeWrapper(receiveElectronicNotaFiscalFromHybrisWrapper)).willReturn(
				electronicNotaFiscalResponse);

	}

	/**
	 * Test method for
	 * {@link com.jnj.facede.nfe.mapper.JnjNfeMapper#mapNfeRequestData(com.jnj.core.model.JnJInvoiceOrderModel, com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.NFERequestData)}
	 * .
	 * 
	 * @throws IntegrationException
	 * @throws BusinessException
	 */
	@Test
	public void testMapNfeRequestData() throws BusinessException, IntegrationException
	{
		final ElectronicNotaFiscalResponse response = jnjNfeMapper.mapNfeRequestData(jnJInvoiceOrderModel,
				receiveElectronicNotaFiscalFromHybrisWrapper);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getNfeProc());
	}


}
