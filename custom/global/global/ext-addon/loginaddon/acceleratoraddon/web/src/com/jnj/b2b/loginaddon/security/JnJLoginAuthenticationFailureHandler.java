/**
 *
 */
package com.jnj.b2b.loginaddon.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;

import com.jnj.b2b.storefront.security.LoginAuthenticationFailureHandler;


/**
 * @author rbalasu4
 * 
 */
public class JnJLoginAuthenticationFailureHandler extends LoginAuthenticationFailureHandler
{

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{

		super.onAuthenticationFailure(request, response, exception);


		/*
		 * this.logAuditData("login", "User Login Event. unsucessful login. Bad credentials.",
		 * JnjGTCoreUtil.getIpAddress(request),
		 * 
		 * true, false, new Date(), userUid);
		 */

		final int userFailedLogins = getBruteForceAttackCounter().getUserFailedLogins(request.getParameter("j_username"));

		/** Store the Failed Attempts in the session **/

		request.getSession().setAttribute("JNJ_USER_FAILED_LOGINS", Integer.valueOf(userFailedLogins));

		/* LOG.debug("getDefaultLoginPage()", "User Failed logins :: " + userFailedLogins ); */




	}
}
