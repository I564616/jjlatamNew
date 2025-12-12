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

import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customergroups.CustomerGroupFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoDatas;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.data.UserGroupDataList;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressValidationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserGroupListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.PK.PKException;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import com.jnj.gt.core.constants.YcommercewebservicesConstants;
import com.jnj.gt.core.populator.HttpRequestCustomerDataPopulator;
import com.jnj.gt.core.populator.options.PaymentInfoOption;
import com.jnj.gt.core.user.data.AddressDataList;
import com.jnj.gt.core.validation.data.AddressValidationData;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.enums.ParameterIn;




@Controller
@RequestMapping(value = "/{baseSiteId}/users")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Tag(name = "Users")
public class UsersController extends BaseCommerceController
{
	private static final Logger LOG = Logger.getLogger(UsersController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "customerGroupFacade")
	private CustomerGroupFacade customerGroupFacade;
	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;
	@Resource(name = "httpRequestCustomerDataPopulator")
	private HttpRequestCustomerDataPopulator httpRequestCustomerDataPopulator;
	@Resource(name = "HttpRequestUserSignUpDTOPopulator")
	private Populator<HttpServletRequest, UserSignUpWsDTO> httpRequestUserSignUpDTOPopulator;
	@Resource(name = "addressDataErrorsPopulator")
	private Populator<AddressVerificationResult<AddressVerificationDecision>, Errors> addressDataErrorsPopulator;
	@Resource(name = "validationErrorConverter")
	private Converter<Object, List<ErrorWsDTO>> validationErrorConverter;
	@Resource(name = "putUserDTOValidator")
	private Validator putUserDTOValidator;
	@Resource(name = "userSignUpDTOValidator")
	private Validator userSignUpDTOValidator;
	@Resource(name = "guestConvertingDTOValidator")
	private Validator guestConvertingDTOValidator;
	@Resource(name = "passwordStrengthValidator")
	private Validator passwordStrengthValidator;


	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@SuppressWarnings("squid:S1160")
	@Operation(hidden = true, summary = " Registers a customer", description = "Registers a customer. There are two options for registering a customer. The first option requires "
			+ "the following parameters: login, password, firstName, lastName, titleCode. The second option converts a guest to a customer. In this case, the required parameters are: guid, password.")
	@ApiBaseSiteIdParam
	public UserWsDTO registerUser(
			@Parameter(description  = "Customer's login. Customer login is case insensitive.") @RequestParam(required = false) final String login,
			@Parameter(description  = "Customer's password.") @RequestParam final String password,
			@Parameter(description  = "Customer's title code. For a list of codes, see /{baseSiteId}/titles resource") @RequestParam(required = false) final String titleCode,
			@Parameter(description  = "Customer's first name.") @RequestParam(required = false) final String firstName,
			@Parameter(description  = "Customer's last name.") @RequestParam(required = false) final String lastName,
			@Parameter(description  = "Guest order's guid.") @RequestParam(required = false) final String guid,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
			throws DuplicateUidException, RequestParameterException, WebserviceValidationException, UnsupportedEncodingException //NOSONAR
	{
		final UserSignUpWsDTO user = new UserSignUpWsDTO();
		httpRequestUserSignUpDTOPopulator.populate(httpRequest, user);
		CustomerData customer = null;
		String userId = login;
		if (guid != null)
		{
			validate(user, "user", guestConvertingDTOValidator);
			convertToCustomer(password, guid);
			customer = customerFacade.getCurrentCustomer();
			userId = customer.getUid();
		}
		else
		{
			validate(user, "user", userSignUpDTOValidator);
			registerNewUser(login, password, titleCode, firstName, lastName);
			customer = customerFacade.getUserForUID(userId);
		}
		httpResponse.setHeader(YcommercewebservicesConstants.LOCATION, getAbsoluteLocationURL(httpRequest, userId)); //NOSONAR
		return getDataMapper().map(customer, UserWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@SuppressWarnings("squid:S1160")
	@Operation(summary = " Registers a customer", description = "Registers a customer. There are two options for registering a customer. The first option requires the following "
			+ "parameters: login, password, firstName, lastName, titleCode. The second option converts a guest to a customer. In this case, the required parameters are: guid, password.")
	@ApiBaseSiteIdParam
	public UserWsDTO registerUser(@Parameter(description  = "User's object.", required = true) @RequestBody final UserSignUpWsDTO user,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
			throws DuplicateUidException, UnknownIdentifierException, //NOSONAR
			IllegalArgumentException, WebserviceValidationException, UnsupportedEncodingException //NOSONAR
	{
		validate(user, "user", userSignUpDTOValidator);
		final RegisterData registration = getDataMapper().map(user, RegisterData.class,
				"login,password,titleCode,firstName,lastName");
		customerFacade.register(registration);
		final String userId = user.getUid();
		httpResponse.setHeader(YcommercewebservicesConstants.LOCATION, getAbsoluteLocationURL(httpRequest, userId)); //NOSONAR
		return getDataMapper().map(customerFacade.getUserForUID(userId), UserWsDTO.class, fields);
	}

	protected String getAbsoluteLocationURL(final HttpServletRequest httpRequest, final String uid)
			throws UnsupportedEncodingException
	{
		final String requestURL = httpRequest.getRequestURL().toString();
		final StringBuilder absoluteURLSb = new StringBuilder(requestURL);
		if (!requestURL.endsWith(YcommercewebservicesConstants.SLASH))
		{
			absoluteURLSb.append(YcommercewebservicesConstants.SLASH);
		}
		absoluteURLSb.append(UriUtils.encodePathSegment(uid, StandardCharsets.UTF_8.name()));
		return absoluteURLSb.toString();
	}

	protected void registerNewUser(final String login, final String password, final String titleCode, final String firstName,
			final String lastName) throws RequestParameterException, DuplicateUidException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("registerUser: login=" + sanitize(login));
		}

		if (!EmailValidator.getInstance().isValid(login))
		{
			throw new RequestParameterException("Login [" + sanitize(login) + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, "login");
		}

		final RegisterData registration = new RegisterData();
		registration.setFirstName(firstName);
		registration.setLastName(lastName);
		registration.setLogin(login);
		registration.setPassword(password);
		registration.setTitleCode(titleCode);
		customerFacade.register(registration);
	}


	protected void convertToCustomer(final String password, final String guid)
			throws RequestParameterException, DuplicateUidException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("registerUser: guid=" + sanitize(guid));
		}

		try
		{
			customerFacade.changeGuestToCustomer(password, guid);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new RequestParameterException("Order with guid " + sanitize(guid) + " not found in current BaseStore",
					RequestParameterException.UNKNOWN_IDENTIFIER, "guid", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			// Occurs when order does not belong to guest user.
			// For security reasons it's better to treat it as "unknown identifier" error
			throw new RequestParameterException("Order with guid " + sanitize(guid) + " not found in current BaseStore",
					RequestParameterException.UNKNOWN_IDENTIFIER, "guid", ex);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get customer profile", description = "Returns customer profile.")
	@ApiBaseSiteIdAndUserIdParam
	public UserWsDTO getUser(
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		return getDataMapper().map(customerData, UserWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@Operation(hidden = true, summary = "Updates customer profile", description = "Updates customer profile. Attributes not provided in the request body will be defined again (set to null or default).")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string"), in = ParameterIn.PATH),
		@Parameter(name = "userId", description = "User identifier.", required = true, schema = @Schema(type = "string"), in = ParameterIn.PATH),
		@Parameter(name = "language", description = "Customer's language.", required = false, schema = @Schema(type = "string"), in = ParameterIn.QUERY),
		@Parameter(name = "currency", description = "Customer's currency.", required = false, schema = @Schema(type = "string"), in = ParameterIn.QUERY) })
	public void putUser(@Parameter(description = "Customer's first name.") @RequestParam final String firstName,
			@Parameter(description = "Customer's last name.") @RequestParam final String lastName,
			@Parameter(description  = "Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = true) @RequestParam(required = true) final String titleCode,
			final HttpServletRequest request) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("putCustomer: userId=" + customer.getUid());
		}
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setTitleCode(titleCode);
		customer.setLanguage(null);
		customer.setCurrency(null);
		httpRequestCustomerDataPopulator.populate(request, customer);

		customerFacade.updateFullProfile(customer);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Updates customer profile", description = "Updates customer profile. Attributes not provided in the request body will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	public void putUser(@Parameter(description  = "User's object", required = true) @RequestBody final UserWsDTO user)
			throws DuplicateUidException
	{
		validate(user, "user", putUserDTOValidator);

		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("putCustomer: userId=" + customer.getUid());
		}

		getDataMapper().map(user, customer, "firstName,lastName,titleCode,currency(isocode),language(isocode)", true);
		customerFacade.updateFullProfile(customer);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	@Operation(hidden = true, summary = "Updates customer profile", description = "Updates customer profile. Only attributes provided in the request body will be changed.")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
		@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
		@Parameter(name = "firstName", description = "Customer's first name", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "lastName", description = "Customer's last name", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "titleCode", description = "Customer's title code. Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "language", description = "Customer's language", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "currency", description = "Customer's currency", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	public void updateUser(final HttpServletRequest request) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateUser: userId=" + customer.getUid());
		}
		httpRequestCustomerDataPopulator.populate(request, customer);
		customerFacade.updateFullProfile(customer);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Updates customer profile", description = "Updates customer profile. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	public void updateUser(@Parameter(description  = "User's object.", required = true) @RequestBody final UserWsDTO user)
			throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateUser: userId=" + customer.getUid());
		}

		getDataMapper().map(user, customer, "firstName,lastName,titleCode,currency(isocode),language(isocode)", false);
		customerFacade.updateFullProfile(customer);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Delete customer profile", description = "Removes customer profile.")
	@ApiBaseSiteIdAndUserIdParam
	public void deactivateUser()
	{
		final CustomerData customer = customerFacade.closeAccount();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deactivateUser: userId=" + customer.getUid());
		}
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/login", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Changes customer's login name.", description = "Changes a customer's login name. Requires the customer's current password.")
	@ApiBaseSiteIdAndUserIdParam
	public void changeLogin(
			@Parameter(description  = "Customer's new login name. Customer login is case insensitive.", required = true) @RequestParam final String newLogin,
			@Parameter(description  = "Customer's current password.", required = true) @RequestParam final String password)
			throws DuplicateUidException, PasswordMismatchException, RequestParameterException //NOSONAR
	{
		if (!EmailValidator.getInstance().isValid(newLogin))
		{
			throw new RequestParameterException("Login [" + newLogin + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, "newLogin");
		}
		customerFacade.changeUid(newLogin, password);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/password", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@Operation(summary = "Changes customer's password", description = "Changes customer's password.")
	@ApiBaseSiteIdAndUserIdParam
	public void changePassword(@Parameter(description = "User identifier.") @PathVariable final String userId,
			@Parameter(description = "Old password. Required only for ROLE_CUSTOMERGROUP") @RequestParam(required = false) final String old,
			@Parameter(description  = "New password.", required = true) @RequestParam(value = "new") final String newPassword)
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final UserSignUpWsDTO customer = new UserSignUpWsDTO();
		customer.setPassword(newPassword);
		validate(customer, "password", passwordStrengthValidator);
		if (containsRole(auth, "ROLE_TRUSTED_CLIENT") || containsRole(auth, "ROLE_CUSTOMERMANAGERGROUP"))
		{
			userService.setPassword(userId, newPassword);
		}
		else
		{
			if (StringUtils.isEmpty(old))
			{
				throw new RequestParameterException("Request parameter 'old' is missing.", RequestParameterException.MISSING, "old");
			}
			customerFacade.changePassword(old, newPassword);
		}
	}

	protected boolean containsRole(final Authentication auth, final String role)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get customer's addresses", description = "Returns customer's addresses.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "List of customer's addresses")
	public AddressListWsDTO getAddresses(
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<AddressData> addressList = getUserFacade().getAddressBook();
		final AddressDataList addressDataList = new AddressDataList();
		addressDataList.setAddresses(addressList);
		return getDataMapper().map(addressDataList, AddressListWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@Operation(hidden = true, summary = "Creates a new address.", description = "Creates a new address.")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "firstName", description = "Customer's first name", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "lastName", description = "Customer's last name", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "titleCode", description = "Customer's title code. Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "country.isocode", description = "Country isocode. This parameter is required and have influence on how rest of parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line1", description = "First part of address. If this parameter is required depends on country (usually it is required).", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line2", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "town", description = "Town name. If this parameter is required depends on country (usually it is required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "postalCode", description = "Postal code. Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "region.isocode", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	public AddressWsDTO createAddress(final HttpServletRequest request,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException //NOSONAR
	{
		final AddressData addressData = super.createAddressInternal(request);
		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@Operation(summary = "Creates a new address.", description = "Creates a new address.")
	@ApiBaseSiteIdAndUserIdParam
	public AddressWsDTO createAddress(
			@Parameter(description  = "Address object.", required = true) @RequestBody final AddressWsDTO address,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException //NOSONAR
	{
		validate(address, "address", getAddressDTOValidator());
		final AddressData addressData = getDataMapper().map(address, AddressData.class,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress,phone");
		addressData.setShippingAddress(true);
		addressData.setVisibleInAddressBook(true);

		getUserFacade().addAddress(addressData);
		if (addressData.isDefaultAddress())
		{
			getUserFacade().setDefaultAddress(addressData);
		}

		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get info about address", description = "Returns detailed information about address with a given id.")
	@ApiBaseSiteIdAndUserIdParam
	public AddressWsDTO getAddress(@Parameter(description  = "Address identifier.", required = true) @PathVariable final String addressId,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getAddress: id=" + sanitize(addressId));
		}
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user", //NOSONAR
					RequestParameterException.INVALID, "addressId");
		}

		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@Operation(hidden = true, summary = "Updates the address", description = "Updates the address. Attributes not provided in the request will be defined again (set to null or default).")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "firstName", description = "Customer's first name", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "lastName", description = "Customer's last name", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "titleCode", description = "Customer's title code. Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "country.isocode", description = "Country isocode. This parameter is required and have influence on how rest of parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line1", description = "First part of address. If this parameter is required depends on country (usually it is required).", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line2", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "town", description = "Town name. If this parameter is required depends on country (usually it is required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "postalCode", description = "Postal code. Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "region.isocode", description = "Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "defaultAddress", description = "Parameter specifies if address should be default for customer.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	public void putAddress(@Parameter(description  = "Address identifier.", required = true) @PathVariable final String addressId,
			final HttpServletRequest request) throws WebserviceValidationException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("editAddress: id=" + sanitize(addressId));
		}
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, "addressId");
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFirstName(null);
		addressData.setLastName(null);
		addressData.setCountry(null);
		addressData.setLine1(null);
		addressData.setLine2(null);
		addressData.setPostalCode(null);
		addressData.setRegion(null);
		addressData.setTitle(null);
		addressData.setTown(null);
		addressData.setDefaultAddress(false);
		addressData.setFormattedAddress(null);

		getHttpRequestAddressDataPopulator().populate(request, addressData);

		final Errors errors = new BeanPropertyBindingResult(addressData, "addressData");
		getAddressValidator().validate(addressData, errors);

		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
		getUserFacade().editAddress(addressData);

		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			getUserFacade().setDefaultAddress(addressData);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Updates the address", description = "Updates the address. Attributes not provided in the request will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	public void putAddress(@Parameter(description  = "Address identifier.", required = true) @PathVariable final String addressId,
			@Parameter(description  = "Address object.", required = true) @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException //NOSONAR
	{
		validate(address, "address", getAddressDTOValidator());
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, "addressId");
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);
		getDataMapper().map(address, addressData,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress", true);

		getUserFacade().editAddress(addressData);

		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			getUserFacade().setDefaultAddress(addressData);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.PATCH)
	@Operation(hidden = true, summary = "Updates the address", description = "Updates the address. Only attributes provided in the request body will be changed.")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "firstName", description = "Customer's first name", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "lastName", description = "Customer's last name", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "titleCode", description = "Customer's title code. Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "country.isocode", description = "Country isocode. This parameter is required and have influence on how rest of parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line1", description = "First part of address. If this parameter is required depends on country (usually it is required).", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line2", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "town", description = "Town name. If this parameter is required depends on country (usually it is required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "postalCode", description = "Postal code. Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "region.isocode", description = "Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "defaultAddress", description = "Parameter specifies if address should be default for customer.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	@ResponseStatus(HttpStatus.OK)
	public void patchAddress(@Parameter(description  = "Address identifier.", required = true) @PathVariable final String addressId,
			final HttpServletRequest request) throws WebserviceValidationException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("editAddress: id=" + sanitize(addressId));
		}
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, "addressId");
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);
		final Errors errors = new BeanPropertyBindingResult(addressData, "addressData");

		getHttpRequestAddressDataPopulator().populate(request, addressData);
		getAddressValidator().validate(addressData, errors);

		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}

		if (addressData.getId().equals(getUserFacade().getDefaultAddress().getId()))
		{
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
		}
		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			getUserFacade().setDefaultAddress(addressData);
		}
		getUserFacade().editAddress(addressData);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Updates the address", description = "Updates the address. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void patchAddress(@Parameter(description  = "Address identifier.", required = true) @PathVariable final String addressId,
			@Parameter(description  = "Address object", required = true) @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException //NOSONAR
	{
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, "addressId");
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);

		getDataMapper().map(address, addressData,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress", false);
		validate(addressData, "address", getAddressValidator());

		if (addressData.getId().equals(getUserFacade().getDefaultAddress().getId()))
		{
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
		}
		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			getUserFacade().setDefaultAddress(addressData);
		}
		getUserFacade().editAddress(addressData);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete customer's address", description = "Removes customer's address.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void deleteAddress(@Parameter(description  = "Address identifier.", required = true) @PathVariable final String addressId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deleteAddress: id=" + sanitize(addressId));
		}
		final AddressData address = getUserFacade().getAddressForCode(addressId);
		if (address == null)
		{
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, "addressId");
		}
		getUserFacade().removeAddress(address);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/verification", method = RequestMethod.POST)
	@ResponseBody
	@Operation(hidden = true, summary = "Verifies the address", description = "Verifies the address.")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "country.isocode", description = "Country isocode. This parameter is required and have influence on how rest of parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line1", description = "First part of address. If this parameter is required depends on country (usually it is required).", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "line2", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "town", description = "Town name. If this parameter is required depends on country (usually it is required)", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "postalCode", description = "Postal code. Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "region.isocode", description = "Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	public AddressValidationWsDTO verifyAddress(final HttpServletRequest request,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final AddressData addressData = new AddressData();
		final Errors errors = new BeanPropertyBindingResult(addressData, "addressData");
		AddressValidationData validationData = new AddressValidationData();

		getHttpRequestAddressDataPopulator().populate(request, addressData);
		if (isAddressValid(addressData, errors, validationData))
		{
			validationData = verifyAddresByService(addressData, errors, validationData);
		}
		return getDataMapper().map(validationData, AddressValidationWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/addresses/verification", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Verifies address", description = "Verifies address.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public AddressValidationWsDTO verifyAddress(
			@Parameter(description  = "Address object.", required = true) @RequestBody final AddressWsDTO address,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		// validation is a bit different here
		final AddressData addressData = getDataMapper().map(address, AddressData.class,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode)");
		final Errors errors = new BeanPropertyBindingResult(addressData, "addressData");
		AddressValidationData validationData = new AddressValidationData();

		if (isAddressValid(addressData, errors, validationData))
		{
			validationData = verifyAddresByService(addressData, errors, validationData);
		}
		return getDataMapper().map(validationData, AddressValidationWsDTO.class, fields);
	}

	/**
	 * Checks if address is valid by a validators
	 *
	 * @formparam addressData
	 * @formparam errors
	 * @formparam validationData
	 * @return true - adress is valid , false - address is invalid
	 */
	protected boolean isAddressValid(final AddressData addressData, final Errors errors,
			final AddressValidationData validationData)
	{
		getAddressValidator().validate(addressData, errors);

		if (errors.hasErrors())
		{
			validationData.setDecision(AddressVerificationDecision.REJECT.toString());
			validationData.setErrors(createResponseErrors(errors));
			return false;
		}
		return true;
	}

	/**
	 * Verifies address by commerce service
	 *
	 * @formparam addressData
	 * @formparam errors
	 * @formparam validationData
	 * @return object with verification errors and suggested addresses list
	 */
	protected AddressValidationData verifyAddresByService(final AddressData addressData, final Errors errors,
			final AddressValidationData validationData)
	{
		final AddressVerificationResult<AddressVerificationDecision> verificationDecision = addressVerificationFacade
				.verifyAddressData(addressData);
		if (verificationDecision.getErrors() != null && !verificationDecision.getErrors().isEmpty())
		{
			populateErrors(errors, verificationDecision);
			validationData.setErrors(createResponseErrors(errors));
		}

		validationData.setDecision(verificationDecision.getDecision().toString());

		if (verificationDecision.getSuggestedAddresses() != null && !verificationDecision.getSuggestedAddresses().isEmpty())
		{
			final AddressDataList addressDataList = new AddressDataList();
			addressDataList.setAddresses(verificationDecision.getSuggestedAddresses());
			validationData.setSuggestedAddressesList(addressDataList);
		}

		return validationData;
	}

	protected ErrorListWsDTO createResponseErrors(final Errors errors)
	{
		final List<ErrorWsDTO> webserviceErrorDto = new ArrayList<>();
		validationErrorConverter.convert(errors, webserviceErrorDto);
		final ErrorListWsDTO webserviceErrorList = new ErrorListWsDTO();
		webserviceErrorList.setErrors(webserviceErrorDto);
		return webserviceErrorList;
	}

	/**
	 * Populates Errors object
	 *
	 * @param errors
	 * @param addressVerificationResult
	 */
	protected void populateErrors(final Errors errors,
			final AddressVerificationResult<AddressVerificationDecision> addressVerificationResult)
	{
		addressDataErrorsPopulator.populate(addressVerificationResult, errors);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get customer's credit card payment details list.", description = "Return customer's credit card payment details list.")
	@ApiBaseSiteIdAndUserIdParam
	public PaymentDetailsListWsDTO getPaymentInfos(
			@Parameter(description  = "Type of payment details.", required = true) @RequestParam(required = false, defaultValue = "false") final boolean saved,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPaymentInfos");
		}

		final CCPaymentInfoDatas paymentInfoDataList = new CCPaymentInfoDatas();
		paymentInfoDataList.setPaymentInfos(getUserFacade().getCCPaymentInfos(saved));

		return getDataMapper().map(paymentInfoDataList, PaymentDetailsListWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(summary = "Get customer's credit card payment details.", description = "Returns a customer's credit card payment details for the specified paymentDetailsId.")
	@ApiBaseSiteIdAndUserIdParam
	public PaymentDetailsWsDTO getPaymentDetails(
			@Parameter(description  = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return getDataMapper().map(getPaymentInfo(paymentDetailsId), PaymentDetailsWsDTO.class, fields);
	}

	public CCPaymentInfoData getPaymentInfo(final String paymentDetailsId)
	{
		LOG.debug("getPaymentInfo : id = " + sanitize(paymentDetailsId));
		try
		{
			final CCPaymentInfoData paymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentDetailsId);
			if (paymentInfoData == null)
			{
				throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.",
						RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId");
			}
			return paymentInfoData;
		}
		catch (final PKException e)
		{
			throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.",
					RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId", e);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete customer's credit card payment details.", description = "Removes a customer's credit card payment details based on a specified paymentDetailsId.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void deletePaymentInfo(
			@Parameter(description  = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deletePaymentInfo: id = " + sanitize(paymentDetailsId));
		}
		getPaymentInfo(paymentDetailsId);
		getUserFacade().removeCCPaymentInfo(paymentDetailsId);
	}



	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.PATCH)
	@Operation(hidden = true, summary = "Updates existing customer's credit card payment details. ", description = "Updates an existing customer's credit card payment "
			+ "details based on the specified paymentDetailsId. Only those attributes provided in the request will be updated.")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "accountHolderName", description = "Name on card.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "cardType", description = "Card type. Call GET /{baseSiteId}/cardtypes beforehand to see what card types are supported.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "expiryMonth", description = "Month of expiry date.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "expiryYear", description = "Year of expiry date.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "issueNumber", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "startMonth", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "startYear", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "subscriptionId", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "saved", description = "Parameter defines if the payment details should be saved for the customer and than could be reused", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "defaultPaymentInfo", description = "Parameter defines if the payment details should be used as default for customer.", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.firstName", description = "Customer's first name.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.lastName", description = "Customer's last name.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.titleCode", description = "Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.country.isocode", description = "Country isocode. This parameter havs influence on how rest of address parameters are validated (e.g. if parameters are required: line1,line2,town,postalCode,region.isocode)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.line1", description = "If this parameter is required depends on country (usually it is required).", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.line2", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "bbillingAddress.town", description = "If this parameter is required depends on country (usually it is required)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.postalCode", description = "Postal code. If this parameter is required depends on country (usually it is required)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddressregion.isocode", description = "Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentInfo(
			@Parameter(description  = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			final HttpServletRequest request) throws RequestParameterException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentInfo: id = " + sanitize(paymentDetailsId));
		}

		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);

		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		final Collection<PaymentInfoOption> options = new ArrayList<PaymentInfoOption>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		getHttpRequestPaymentInfoPopulator().populate(request, paymentInfoData, options);
		validate(paymentInfoData, "paymentDetails", getCcPaymentInfoValidator());

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Updates existing customer's credit card payment details.", description = "Updates an existing customer's credit card payment details based "
			+ "on the specified paymentDetailsId. Only those attributes provided in the request will be updated.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentInfo(
			@Parameter(description  = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@Parameter(description  = "Payment details object", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
			throws RequestParameterException //NOSONAR
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();

		getDataMapper().map(paymentDetails, paymentInfoData,
				"accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,defaultPaymentInfo,saved,"
						+ "billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress)",
				false);
		validate(paymentInfoData, "paymentDetails", getCcPaymentInfoValidator());

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}

	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.PUT)
	@Operation(hidden = true, summary = "Updates existing customer's credit card payment details. ", description = "Updates existing customer's credit card payment "
			+ "info based on the payment info ID. Attributes not given in request will be defined again (set to null or default).")
	@Parameters(
	{ @Parameter(name = "baseSiteId", description = "Base site identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "userId", description = "User identifier", required = true, schema = @Schema(type = "string") , in = ParameterIn.PATH),
			@Parameter(name = "accountHolderName", description = "Name on card.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "cardType", description = "Card type. Call GET /{baseSiteId}/cardtypes beforehand to see what card types are supported.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "expiryMonth", description = "Month of expiry date.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "expiryYear", description = "Year of expiry date.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "issueNumber", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "startMonth", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "startYear", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "subscriptionId", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "saved", description = "Parameter defines if the payment details should be saved for the customer and than could be reused", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "defaultPaymentInfo", description = "Parameter defines if the payment details should be used as default for customer.", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.firstName", description = "Customer's first name.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.lastName", description = "Customer's last name.", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.titleCode", description = "Customer's title code. For a list of codes, see /{baseSiteId}/titles resource", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.country.isocode", description = "Country isocode. This parameter havs influence on how rest of address parameters are validated (e.g. if parameters are required: line1,line2,town,postalCode,region.isocode)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.line1", description = "If this parameter is required depends on country (usually it is required).", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.line2", description = "Second part of address. If this parameter is required depends on country (usually it is not required)", schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "bbillingAddress.town", description = "If this parameter is required depends on country (usually it is required)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddress.postalCode", description = "Postal code. If this parameter is required depends on country (usually it is required)", required = true, schema = @Schema(type = "string") , in = ParameterIn.QUERY),
			@Parameter(name = "billingAddressregion.isocode", description = "Isocode for region. If this parameter is required depends on country.", required = false, schema = @Schema(type = "string") , in = ParameterIn.QUERY) })
	@ResponseStatus(HttpStatus.OK)
	public void putPaymentInfo(@PathVariable final String paymentDetailsId, final HttpServletRequest request)
			throws RequestParameterException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentInfo: id = " + sanitize(paymentDetailsId));
		}

		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);

		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		paymentInfoData.setAccountHolderName(null);
		paymentInfoData.setCardNumber(null);
		paymentInfoData.setCardType(null);
		paymentInfoData.setExpiryMonth(null);
		paymentInfoData.setExpiryYear(null);
		paymentInfoData.setDefaultPaymentInfo(false);
		paymentInfoData.setSaved(false);

		paymentInfoData.setIssueNumber(null);
		paymentInfoData.setStartMonth(null);
		paymentInfoData.setStartYear(null);
		paymentInfoData.setSubscriptionId(null);

		final AddressData address = paymentInfoData.getBillingAddress();
		address.setFirstName(null);
		address.setLastName(null);
		address.setCountry(null);
		address.setLine1(null);
		address.setLine2(null);
		address.setPostalCode(null);
		address.setRegion(null);
		address.setTitle(null);
		address.setTown(null);

		final Collection<PaymentInfoOption> options = new ArrayList<PaymentInfoOption>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		getHttpRequestPaymentInfoPopulator().populate(request, paymentInfoData, options);
		validate(paymentInfoData, "paymentDetails", getCcPaymentInfoValidator());

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Updates existing customer's credit card payment info.", description = "Updates existing customer's credit card payment info based on the "
			+ "payment info ID. Attributes not given in request will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void putPaymentInfo(
			@Parameter(description  = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@Parameter(description  = "Payment details object.", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
			throws RequestParameterException //NOSONAR
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();

		validate(paymentDetails, "paymentDetails", getPaymentDetailsDTOValidator());
		getDataMapper().map(paymentDetails, paymentInfoData,
				"accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,defaultPaymentInfo,saved,billingAddress"
						+ "(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress)",
				true);

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}/customergroups", method = RequestMethod.GET)
	@Operation(summary = "Get all customer groups of a customer.", description = "Returns all customer groups of a customer.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserGroupListWsDTO getAllCustomerGroupsForCustomer(
			@Parameter(description  = "User identifier.", required = true) @PathVariable final String userId,
			@Parameter(description  = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC, DEFAULT, FULL"})) @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserGroupDataList userGroupDataList = new UserGroupDataList();
		userGroupDataList.setUserGroups(customerGroupFacade.getCustomerGroupsForUser(userId));
		return getDataMapper().map(userGroupDataList, UserGroupListWsDTO.class, fields);
	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String[] statusesStrings = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		for (final String status : statusesStrings)
		{
			statusesEnum.add(OrderStatus.valueOf(status));
		}
		return statusesEnum;
	}

	protected OrderHistoriesData createOrderHistoriesData(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();

		orderHistoriesData.setOrders(result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());

		return orderHistoriesData;
	}
}
