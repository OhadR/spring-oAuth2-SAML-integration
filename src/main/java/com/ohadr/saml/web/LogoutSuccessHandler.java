package com.ohadr.saml.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * this is the success handler that is called after the saml-response from the IdP is processed (by *SAMLLogoutProcessingFilter)
 * it retrieves the redirect-uri from the cookie, that was stored upon the logout request (in MySAMLLogoutFilter)  
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{

	static final String REDIRECT_URI_PARAM_NAME = "REDIRECT_URI";

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
	{
		String redirectURL = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) 
		{
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals( REDIRECT_URI_PARAM_NAME )) 
				{
					redirectURL = cookies[i].getValue();
					break;
				}
			}
		}

		if (redirectURL != null)
		{
			getRedirectStrategy().sendRedirect(request, response, redirectURL);
		}
		else
		{
			super.onLogoutSuccess(request, response, authentication);
		}
	}
}
