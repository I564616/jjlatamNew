/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.jnj.gt.core.v2.controller;

import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherWsDTO;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Controller
@RequestMapping(value = "/{baseSiteId}/vouchers")
@Tag(name = "Vouchers")
public class VouchersController extends BaseController
{
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;

	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get a voucher based on code", description = "Returns details of a single voucher that is specified by its voucher identification code.")
	@ApiBaseSiteIdParam
	public VoucherWsDTO getVoucherByCode(
			@Parameter(description = "Voucher identifier (code)", required = true) @PathVariable final String code,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = "BASIC") final String fields)
			throws VoucherOperationException
	{
		return getDataMapper().map(voucherFacade.getVoucher(code), VoucherWsDTO.class, fields);
	}
}
