package com.ohadr.saml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.saml.SAMLEntryPoint;


/**
 * upon trying to reach the /oauth/authorize, if we are not authenticated we are redirected
 * to the SAML (becoz it is declared as the entry point in the XML). so we get here, and 
 * we need to know if the metadata-idp is already loaded for the relevant request 
 */
public class MySAMLEntryPoint extends SAMLEntryPoint
{
	private static Logger log = Logger.getLogger(MySAMLEntryPoint.class);

	private final Map<String, String> subdomains = new HashMap<String, String>();
	
    @Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException 
    {
    	
    	//get the redirect uri before sending the browser to the SAML IdP. becoz we want to
    	//know the subdomain of the referrer, and know if we need to load a new metadata.
    	String redirectUri = request.getParameter( /*AuthorizationRequest.REDIRECT_URI*/"redirect_uri" );
    	
    	String subdomain = StringUtils.substringBetween(redirectUri, "//", ".");
    	
    	//call API to get issuer from subdomain:
    	String issuer = getIssuerNameBySubdomain(subdomain);
    	

    	//TODO: change to debug:
    	log.info("redirectUri: " + redirectUri + "subdomain: " + subdomain);

    	//TODO:
    	//NOTE: we should get the IDP somehow. let's assume we got the IDP in the request:
    	String idpId = subdomain;

    	
    	
    	// add an attribute with the value of the IDP
    	request.setAttribute(org.springframework.security.saml.SAMLConstants.PEER_ENTITY_ID, idpId);
    	
    	super.commence(request, response, e);

    }
    
	private String getIssuerNameBySubdomain(String subdomain)
	{
		// TODO call API
		return "com.ohadr.saml";
	}

	public void resetSubdomains()
	{
		subdomains.clear();
	}

}
