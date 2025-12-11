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


import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import com.jnj.gt.core.v2.helper.StoresHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Tag(name = "Stores")
public class StoresController extends BaseController
{
	private static final String DEFAULT_SEARCH_RADIUS_METRES = "100000.0";
	private static final String DEFAULT_ACCURACY = "0.0";
	@Resource(name = "storesHelper")
	private StoresHelper storesHelper;


	@RequestMapping(value = "/{baseSiteId}/stores", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get a list of store locations", description = "Lists all store locations that are near the location specified in a query or based on latitude and longitude.")
	@ApiBaseSiteIdParam
	public StoreFinderSearchPageWsDTO locationSearch(
			@Parameter(description = "Location in natural language i.e. city or country.") @RequestParam(required = false) final String query, //NOSONAR
			@Parameter(description = "Coordinate that specifies the north-south position of a point on the Earth's surface.") @RequestParam(required = false) final Double latitude,
			@Parameter(description = "Coordinate that specifies the east-west position of a point on the Earth's surface.") @RequestParam(required = false) final Double longitude,
			@Parameter(description = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@Parameter(description = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@Parameter(description = "Sorting method applied to the return results.") @RequestParam(required = false, defaultValue = "asc") final String sort,
			@Parameter(description = "Radius in meters. Max value: 40075000.0 (Earth's perimeter).") @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_RADIUS_METRES) final double radius,
			@Parameter(description = "Accuracy in meters.") @RequestParam(required = false, defaultValue = DEFAULT_ACCURACY) final double accuracy,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response) throws RequestParameterException //NOSONAR
	{
		final StoreFinderSearchPageWsDTO result = storesHelper.locationSearch(query, latitude, longitude, currentPage, pageSize,
				sort, radius, accuracy, addPaginationField(fields));

		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());

		return result;
	}


	@RequestMapping(value = "/{baseSiteId}/stores", method = RequestMethod.HEAD)
	@Operation(summary = "Get a header with the number of store locations", description = "In the response header, the \"x-total-count\" indicates the number of "
			+ "all store locations that are near the location specified in a query, or based on latitude and longitude.")
	@ApiBaseSiteIdParam
	public void countLocationSearch(
			@Parameter(description = "Location in natural language i.e. city or country.") @RequestParam(required = false) final String query,
			@Parameter(description = "Coordinate that specifies the north-south position of a point on the Earth's surface.") @RequestParam(required = false) final Double latitude,
			@Parameter(description = "Coordinate that specifies the east-west position of a point on the Earth's surface.") @RequestParam(required = false) final Double longitude,
			@Parameter(description = "Radius in meters. Max value: 40075000.0 (Earth's perimeter).") @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_RADIUS_METRES) final double radius,
			@Parameter(description = "Accuracy in meters.") @RequestParam(required = false, defaultValue = DEFAULT_ACCURACY) final double accuracy,
			final HttpServletResponse response) throws RequestParameterException //NOSONAR
	{
		final StoreFinderSearchPageData<PointOfServiceData> result = storesHelper.locationSearch(query, latitude, longitude, 0, 1,
				"asc", radius, accuracy);

		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());
	}


	@RequestMapping(value = "/{baseSiteId}/stores/{storeId}", method = RequestMethod.GET)
	@Operation(summary = "Get a store location", description = "Returns store location based on its unique name.")
	@ApiBaseSiteIdParam
	@ResponseBody
	public PointOfServiceWsDTO locationDetails(
			@Parameter(description = "Store identifier (currently store name)", required = true) @PathVariable final String storeId,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return storesHelper.locationDetails(storeId, fields);
	}
}
