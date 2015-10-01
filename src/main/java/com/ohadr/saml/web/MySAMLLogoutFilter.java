package com.ohadr.saml.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


public class MySAMLLogoutFilter extends SAMLLogoutFilter
{

	public MySAMLLogoutFilter(LogoutSuccessHandler logoutSuccessHandler,
			LogoutHandler[] localHandler, LogoutHandler[] globalHandlers)
	{
		super(logoutSuccessHandler, localHandler, globalHandlers);
	}


	@Override
	public void processLogout(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		if (requiresLogout(request, response)) 
		{
			String redirectURL = request.getParameter( com.ohadr.saml.web.LogoutSuccessHandler.REDIRECT_URI_PARAM_NAME );
			if (redirectURL != null)
			{
				Cookie c = new Cookie(com.ohadr.saml.web.LogoutSuccessHandler.REDIRECT_URI_PARAM_NAME, redirectURL);
				response.addCookie(c);
			}
		}
		
		super.processLogout(request, response, chain);
	}


}
