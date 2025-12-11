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

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercewebservicescommons.dto.order.CardTypeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.TitleListWsDTO;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import com.jnj.gt.core.order.data.CardTypeDataList;
import com.jnj.gt.core.storesession.data.CurrencyDataList;
import com.jnj.gt.core.storesession.data.LanguageDataList;
import com.jnj.gt.core.user.data.CountryDataList;
import com.jnj.gt.core.user.data.TitleDataList;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Misc Controller
 */
@Controller
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Tag(name = "Miscs")
public class MiscsController extends BaseController
{
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	@RequestMapping(value = "/{baseSiteId}/languages", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getLanguages',#fields)")
	@ResponseBody
	@Operation(summary = "Get a list of available languages.", description = "Lists all available languages (all languages used for a particular store). If the list "
			+ "of languages for a base store is empty, a list of all languages available in the system will be returned.")
	@ApiBaseSiteIdParam
	public LanguageListWsDTO getLanguages(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final LanguageDataList dataList = new LanguageDataList();
		dataList.setLanguages(storeSessionFacade.getAllLanguages());
		return getDataMapper().map(dataList, LanguageListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/currencies", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCurrencies',#fields)")
	@ResponseBody
	@Operation(summary = "Get a list of available currencies.", description = "Lists all available currencies (all usable currencies for the current store). If the list "
			+ "of currencies for a base store is empty, a list of all currencies available in the system is returned.")
	@ApiBaseSiteIdParam
	public CurrencyListWsDTO getCurrencies(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CurrencyDataList dataList = new CurrencyDataList();
		dataList.setCurrencies(storeSessionFacade.getAllCurrencies());
		return getDataMapper().map(dataList, CurrencyListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/deliverycountries", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getDeliveryCountries',#fields)")
	@ResponseBody
	@Operation(summary = "Get a list of supported countries.", description = "Lists all supported delivery countries for the current store. The list is sorted alphabetically.")
	@ApiBaseSiteIdParam
	public CountryListWsDTO getDeliveryCountries(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CountryDataList dataList = new CountryDataList();
		dataList.setCountries(checkoutFacade.getDeliveryCountries());
		return getDataMapper().map(dataList, CountryListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/titles", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getTitles',#fields)")
	@ResponseBody
	@Operation(summary = "Get a list of all localized titles.", description = "Lists all localized titles.")
	@ApiBaseSiteIdParam
	public TitleListWsDTO getTitles(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final TitleDataList dataList = new TitleDataList();
		dataList.setTitles(userFacade.getTitles());
		return getDataMapper().map(dataList, TitleListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/cardtypes", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCardTypes',#fields)")
	@ResponseBody
	@Operation(summary = "Get a list of supported payment card types.", description = "Lists supported payment card types.")
	@ApiBaseSiteIdParam
	public CardTypeListWsDTO getCardTypes(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CardTypeDataList dataList = new CardTypeDataList();
		dataList.setCardTypes(checkoutFacade.getSupportedCardTypes());
		return getDataMapper().map(dataList, CardTypeListWsDTO.class, fields);
	}
}
