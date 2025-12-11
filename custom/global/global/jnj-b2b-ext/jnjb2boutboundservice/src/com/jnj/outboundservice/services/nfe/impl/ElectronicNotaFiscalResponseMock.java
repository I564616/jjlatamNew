/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.outboundservice.services.nfe.impl;

import org.w3._2000._09.xmldsig.KeyInfoType;
import org.w3._2000._09.xmldsig.SignatureType;

import br.inf.portalfiscal.nfe.Avulsa;
import br.inf.portalfiscal.nfe.Cana;
import br.inf.portalfiscal.nfe.InfNFe;
import br.inf.portalfiscal.nfe.TNFe;
import br.inf.portalfiscal.nfe.TNfeProc;
import br.inf.portalfiscal.nfe.TProtNFe;

import com.jnj.itsusmpl00082.sg910_btb_in0501_electronicnotafiscal_hybris_source_v1_webservices.receiveelectronicnotafiscalws.ElectronicNotaFiscalResponse;


/**
 * The Class ElectronicNotaFiscalResponseMock acts as a mock class for ElectronicNotaFiscalResponse.
 * 
 * @author Accenture
 * @version 1.0
 */
public class ElectronicNotaFiscalResponseMock
{

	/**
	 * Gets the mock response until the WS are up.
	 * 
	 * @return the response
	 */
	public ElectronicNotaFiscalResponse getResponse()
	{
		final ElectronicNotaFiscalResponse electronicNotaFiscalResponse = new ElectronicNotaFiscalResponse();
		/*
		 * final JAXBContext jAXBContext = JAXBContext.newInstance(ElectronicNotaFiscalResponse.class); final Unmarshaller
		 * unmarshaller = jAXBContext.createUnmarshaller(); final File file = new
		 * File("C:/Users/bhanu.pratap.jain/Desktop/QueryNFe/receiveEletronicNFeResponse.xml");
		 * electronicNotaFiscalResponse = (ElectronicNotaFiscalResponse) unmarshaller.unmarshal(file);
		 */



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


		return electronicNotaFiscalResponse;
	}
}
