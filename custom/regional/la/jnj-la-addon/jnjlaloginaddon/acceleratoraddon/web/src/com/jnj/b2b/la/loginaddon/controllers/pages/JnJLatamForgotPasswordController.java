package com.jnj.b2b.la.loginaddon.controllers.pages;

import com.jnj.b2b.la.jnjlaawstokenservice.data.JwtTokenBean;
import com.jnj.b2b.la.loginaddon.controllers.JnjlaloginaddonControllerConstants;
import com.jnj.b2b.loginaddon.constants.LoginaddonConstants;
import com.jnj.b2b.loginaddon.controllers.pages.JnjGTForgotPasswordController;
import com.jnj.b2b.loginaddon.recaptcha.JnjWebUtil;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.company.impl.JnjLatamB2BCommerceUserFacadeImpl;
import com.jnj.jwt.service.JwtService;
import com.jnj.jwt.service.exception.JwtServiceException;
import com.jnj.restservice.exception.RestServiceException;
import com.jnj.restservice.logic.Response;
import com.jnj.restservice.util.RestServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.apache.commons.lang3.BooleanUtils;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.exceptions.BusinessException;
import net.minidev.json.JSONObject;


/**
 * @author rselvar5
 *
 */
@Controller

public class JnJLatamForgotPasswordController extends JnjGTForgotPasswordController
{
	public static final String EMAIL_VALIDATION_RESULT = "Email validation result :: ";
	private static final String RESET_INPUT = "resetInput";
	protected static final String MULTIMODE = "multimode";
	protected static final int JNJ_INTERNAL_RESET_PASSWORD_ERROR_CODE = 999;
	protected static final int JNJ_INTERNAL_RESET_PASSWORD_SUCCESS_CODE = 998;
	protected static final String RESET_EMAIL_TOKEN_VERIFY_URL="/emailTokenVerification";
	protected static final String PASWD_UPDATE_FAILED= "Password update failed!";
	protected static final String PWRD_EXPIRY_ERROR_MSG= "password.expiry.errormessage.invalidtoken";
	protected static final String EXCEP_IN_PASS_VERF_PROCESS= "Exception in password verification process :: ";

	protected ConfigurationService configurationService;

	protected RestServiceUtil restServiceUtil;
	protected JwtService jwtService;

	@Autowired
	private JnjLatamB2BCommerceUserFacadeImpl jnjLatamB2BCommerceUserFacadeImpl;

	@Override
	public String getForgotPasswordPopup(final Model model,@RequestParam(value = "passwordExipred", required = false) final String passwordExipred) {
		final String METHOD_NAME = "getForgotPasswordPopup()";
		
		JnjGTCoreUtil.logInfoMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnJLatamForgotPasswordController.class);

        if(passwordExipred!=null && passwordExipred.equals("true")){
            model.addAttribute(PASSWORD_EXPIRED, passwordExipred);
        }

		JnjGTCoreUtil.logInfoMessage(Logging.FORGOT_PASSWORD_NAME, METHOD_NAME, Logging.END_OF_METHOD, JnJLatamForgotPasswordController.class);
		return getLatamView(JnjlaloginaddonControllerConstants.Views.Pages.Password.PasswordResetChangePage);
	}

	/**
	 * This method is responsible is generate the token with AWS REST API --> Pass email and get the token
	 * 	Build URL and send mail to customer
	 * @param email
	 * @param request
	 * @return String
	 */
	@PostMapping("/emailTokenNotification")
	@ResponseBody
	public String submitEmailTokenNotification(
			@RequestParam("email") final String email, final HttpServletRequest request)
	{
		//To generate the token with AWS REST API --> Pass email and get the token
		// Build URL and send mail to customer.
		LOG.info("Entering emailtoken");
		if(StringUtils.isBlank(email)){
			return getRestServiceUtil().getMultimodeJsonResponse(false,JNJ_INTERNAL_RESET_PASSWORD_ERROR_CODE,"email is not valid");
		}
		final String convertedEmail = StringUtils.lowerCase(email);
		if (!jnjGTCustomerFacade.validateUid(email)){
			LOG.info("Entering validateUid");
			return getRestServiceUtil().getMultimodeJsonResponse(false,JNJ_INTERNAL_RESET_PASSWORD_ERROR_CODE,"email is not valid");
		}
		LOG.info("Entering after validateUid");
		Response<String> response = null;
		try{
			response = getRestServiceUtil().getPasswordResetToken(convertedEmail);
		}catch(final RestServiceException e){
			LOG.error(e);
			return getRestServiceUtil().getMultimodeJsonResponse(false,e.getErrorCode().getCode(),e.getMessage());
		}
		final String awsToken =response.getToken();

		LOG.info("Token :" + awsToken + " for the given id:" + convertedEmail);
		if(StringUtils.isNotBlank(awsToken)){
			//generate JWT and send in the email
			final String jwtToken =getJwtService().createJWT(convertedEmail, awsToken);
			sendPasswordResetEmail(convertedEmail,jwtToken,request);

			return getRestServiceUtil().getMultimodeJsonResponse(true,response.getResponseCode(),null);
		}
		return getRestServiceUtil().getMultimodeJsonResponse(false,response.getResponseCode(),response.getResponseMessage());
	}

	@GetMapping("/emailTokenVerification")
	@ResponseBody
	public ModelAndView submitEmailTokenVerification(@RequestParam("token") final String token,
													 final Model model, final RedirectAttributes redirectAttributes )
	{
		//Verify the token with AWS and allow to reset Password [true /false]
		boolean validation = false;
		String errorMessage=null;
		String awsToken=null;
		String email=null;

		int errorCode ;
		if(StringUtils.isNotBlank(token)){
			try{
				final JwtTokenBean tokenBean =getJwtService().validateJwt(token);
				awsToken= tokenBean.getToken();
				email = tokenBean.getEmailId();

				final Response<String> response = getRestServiceUtil().verifyPasswordToken(email, awsToken, false);
				validation=response.isValidToken();
				if(!validation){
					errorMessage = response.getResponseMessage();
					errorCode = response.getResponseCode();
					sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_MESSAGE, errorMessage);
					sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_CODE, errorCode);
				}
			}catch(final JwtServiceException e){
				LOG.error(e);
				sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_MESSAGE, e.getMessage());
				sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_CODE,e.getErrorCode().getCode());
			}catch(final RestServiceException e){
				LOG.error(e);
				sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_MESSAGE, e.getMessage());
				sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_ERROR_CODE,e.getErrorCode().getCode());
			}
		}
		if(validation){
			sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN, awsToken);
			LOG.info("token is ::::::::::::::::::::::"+awsToken);
		}
		redirectAttributes.addAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_RESET_FLAG, true);
		sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN_VERIFICATION, true);
		sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_EMAIL_TOKEN_VERIFIED, validation);
		sessionService.setAttribute(LoginaddonConstants.Login.PASSWORD_RESET_EMAIL, email);
		return new ModelAndView("redirect:/login");
	}
	public String getLatamView(final String view){
		return JnjlaloginaddonControllerConstants.ADDON_PREFIX + view;

	}
	/**
	 * This method is responsible for sending the password reset email entered by the user
	 * @param userId
	 * @param token
	 * @param request
	 * @return void
	 */
	private  void sendPasswordResetEmail(final String userId, final String token,final HttpServletRequest request)
	{
		final String methodName = "sentTemporaryEmail()";
		final String siteUrl = JnjWebUtil.getServerUrl(request);
		final String logoUrl = JnjWebUtil.getUnsecureServerUrl(request);
		final StringBuilder resetPasswordURL= new StringBuilder(siteUrl);
		try{
			resetPasswordURL.append(request.getServletPath().substring(0, request.getServletPath().lastIndexOf("/")))
					.append(RESET_EMAIL_TOKEN_VERIFY_URL);
			jnjLatamB2BCommerceUserFacadeImpl.sendPasswordResetEmail(logoUrl, userId, siteUrl, resetPasswordURL.toString(), token);
		}/*catch (final DuplicateUidException e){
			//JnjGTCoreUtil.logErrorMessage("Duplicate Uid found sending temporary password email:", methodName, "EXCEPTION" + e, e, JnJLatamForgotPasswordController.class);
		}*/catch (final Exception e){
			JnjGTCoreUtil.logErrorMessage("Unsupported encoding sending temporary password email:", methodName, "EXCEPTION" + e, e, JnjGTForgotPasswordController.class);
		}
	}

	/**
	 * This method is responsible for validating the email entered by the user It also blocks the entry if the user is
	 * registered as an attacker (Once invalid attempt count is 3 or more).
	 * @param email
	 * the email entered by the user on the font end
	 * @return validationStatusString
	 */
	@Override
	public Map<String, String> validateEmailRequest(@RequestParam("email") String email)
	{
		final String methodName = "validateEmailRequest()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, methodName, Logging.BEGIN_OF_METHOD);
		email = StringUtils.lowerCase(email);
		
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, "User email received :: " + email);
		final Map<String, String> responseMap = new HashMap<>();
		
		/** Validating if the user exists **/
		if (jnjGTCustomerFacade.validateUid(email))
		{
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, EMAIL_VALIDATION_RESULT + true);

			if (jnjGTCustomerFacade.isLoginDisabled(email))
			{
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, "User is login disabled!");
				responseMap.put(STATUS, DISABLED);
				logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, methodName, Logging.END_OF_METHOD);
				/** Returning status as DISABLED **/
				return responseMap;
			}
			if (BooleanUtils.isTrue(Boolean.valueOf(getConfigurationService().getConfiguration()
					.getBoolean(LoginaddonConstants.Login.MULTI_MODE_PASSWORD_RESET_ROUTE, false))))
			{

				responseMap.put(STATUS, MULTIMODE);
				final Map<String, List<String>> channels = getPasswordResetChannels(email);
				final JSONObject jOBJ = new JSONObject(channels);
				responseMap.put(RESET_INPUT, jOBJ.toJSONString());
				return responseMap;

			}
			getSecurityQuestions(email, responseMap);
		}
		else
		{
			/** Setting Invalid status as email was not valid **/
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, EMAIL_VALIDATION_RESULT + false);
			responseMap.put(STATUS, INVALID);
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, methodName, Logging.END_OF_METHOD);
		return responseMap;
	}
	/**
	 * This method returns the valid password reset channels for the given email
	 * @param email
	 * @return
	 */
	public Map<String, List<String>> getPasswordResetChannels(final String email){
		final Map<String, List<String>> resetMap = new HashMap<>();

		final List<String> emailList = new ArrayList<String>();
		emailList.add(email);

		final List<String> textList = new ArrayList<String>();
		final List<String> phoneList = new ArrayList<String>();
		resetMap.put("email", emailList);
		resetMap.put("text", textList);
		resetMap.put("phone", phoneList);
		return resetMap;
	}

	/**
	 * This method will populate the security questions for the given email
	 * @param email
	 * @param responseMap
	 * @return
	 */
	public Map<String,String> getSecurityQuestions(final String email, final Map<String,String> responseMap){
		final String methodName = "getSecurityQuestions()";

		final StringBuilder questionsDelimitedString = new StringBuilder();
		/** Fetching all the secret questions **/
		final List<String> questions = fetchAllSecretQuestions(email);
		if (!questions.isEmpty())
		{
			for (final String question : questions)
			{
				questionsDelimitedString.append(question);
				questionsDelimitedString.append(Jnjb2bCoreConstants.UNDERSCORE_SYMBOL);
			}
			/** Setting status valid as the user email is successfully validated **/
			responseMap.put(STATUS, VALID);
			/** Setting string of all secret questions **/
			responseMap.put(QUESTIONS, String.valueOf(questionsDelimitedString));
			LOG.info(EMAIL_VALIDATION_RESULT + true);
		}
		else
		{
			/** Setting Invalid status as question were not found **/
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, "No Questions found! Returning invalid");
			responseMap.put(STATUS, INVALID);
		}
		return responseMap;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	public RestServiceUtil getRestServiceUtil() {
		return restServiceUtil;
	}

	public void setRestServiceUtil(final RestServiceUtil restServiceUtil) {
		this.restServiceUtil = restServiceUtil;
	}

	public JwtService getJwtService() {
		return jwtService;
	}

	public void setJwtService(final JwtService jwtService) {
		this.jwtService = jwtService;
	}
	private String updatePasswordForUser(final String password, final String email, final String methodName, String error)
	{
		try
		{
			jnjGTCustomerFacade.updateUserPassword(password, email);
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, "Password has been updated successfully");
		}
		catch (final TokenInvalidatedException tokenInvalidatedException)
		{
			logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, PASWD_UPDATE_FAILED);
			error = jnjCommonFacadeUtil.getMessageFromImpex(PWRD_EXPIRY_ERROR_MSG);
			LOG.error("Password update error :: ", tokenInvalidatedException);
		}
		return error;
	}
	protected String tokenValidation(final boolean multimode, final String email, final String token){
		String validationResponse = null;
		if(multimode){
			if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(token)){
				final Response<String> response = getRestServiceUtil().verifyPasswordToken(email, token, true);
				final boolean validation=response.isValidToken();
				if(!validation){
					final String errorMessage = response.getResponseMessage();
					final int  errorCode = response.getResponseCode();
					validationResponse = getRestServiceUtil().getMultimodeJsonResponse(false,errorCode,errorMessage);
				}
			}
		}
		return validationResponse;
	}

	/**
	 * This method is responsible for updating the password for the user
	 * 
	 * @param password
	 * @param email
	 * @return updateStatus
	 */
	@PostMapping("/latamRevisionVerification")
	@ResponseBody
	public String updateCustomerPassword(@RequestParam("password") final String password,
										 @RequestParam("email") final String email, @RequestParam("token") final String token)
	{
		final String methodName = "updateCustomerPassword()";
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, methodName, Logging.BEGIN_OF_METHOD);
		boolean multimode = false;
		final String TRUE = String.valueOf(Boolean.TRUE);
		final String EXCEPTION = "Exception";
		String error = "";
		logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName,
				"Email received :: " + email + " :: Password received :: " + password + " :: Token received :: " + token);
		if (BooleanUtils
				.isTrue(Boolean.valueOf(getConfigurationService().getConfiguration()
						.getBoolean(LoginaddonConstants.Login.MULTI_MODE_PASSWORD_RESET_ROUTE, false))))		
		{
			multimode = true;
			if (StringUtils.isBlank(token))
			{
				return getRestServiceUtil().getMultimodeJsonResponse(false, JNJ_INTERNAL_RESET_PASSWORD_ERROR_CODE,
						"token not found");
			}
		}

		try
		{

			error = jnjGTCustomerFacade.verifyPassword(password, email);
			if (TRUE.equalsIgnoreCase(error.trim()))
			{
				final String validation = tokenValidation(multimode, email, token);
				if (validation != null)
				{
					return validation;
				}
				error = updatePasswordForUser(password, email, methodName, error);
			}
			else
			{
				logDebugMessage(Logging.FORGOT_PASSWORD_NAME, methodName, PASWD_UPDATE_FAILED);
				error = jnjCommonFacadeUtil.getMessageFromImpex("profile.changePassword.errormessage.oldPasswords");
			}
		}
		catch (PasswordMismatchException | BusinessException exception)
		{
			error = EXCEPTION;
			LOG.error(EXCEP_IN_PASS_VERF_PROCESS, exception);
		}
		if (multimode)
		{
			if (TRUE.equalsIgnoreCase(error.trim()))
			{
				error = getRestServiceUtil().getMultimodeJsonResponse(true, JNJ_INTERNAL_RESET_PASSWORD_SUCCESS_CODE, null);
			}
			else
			{
				error = getRestServiceUtil().getMultimodeJsonResponse(false, JNJ_INTERNAL_RESET_PASSWORD_ERROR_CODE, error);
			}
		}
		logMethodStartOrEnd(Logging.FORGOT_PASSWORD_NAME, methodName, Logging.END_OF_METHOD);
		return error;
	}
}
