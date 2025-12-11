package com.jnj.b2b.storefront.security;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;
import de.hybris.platform.spring.security.CoreUserDetails;

public class JnjAuthenticationProvider extends CoreAuthenticationProvider {

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{
		
		System.out.println("i am in JnjAuthenticationProvider");
		if (Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized())
		{
			final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
			UserDetails userDetails = null;
			try
			{
				userDetails = retrieveUser(username);
			}
			catch (final UsernameNotFoundException notFound)
			{
				throw new BadCredentialsException(
						messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"), notFound);
			}

			getPreAuthenticationChecks().check(userDetails);

			final User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());

			additionalAuthenticationChecks(userDetails, (AbstractAuthenticationToken) authentication);

			// finally, set user in session
			JaloSession.getCurrentSession().setUser(user);
			final Authentication successAuthentication = createSuccessAuthentication(authentication, userDetails);
			JaloSession.getCurrentSession().setAttribute("AUTH", successAuthentication);
			return successAuthentication;
		}
		else
		{
			return createSuccessAuthentication(//
					authentication, //
					new CoreUserDetails("systemNotInitialized", "systemNotInitialized", true, false, true, true,
							Collections.EMPTY_LIST, null));
		}
	}



}
