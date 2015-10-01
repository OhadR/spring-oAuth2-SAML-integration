package com.ohadr.saml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * this class is autowired to the SamlProvider, so it tries to get the user's details from the token using this 
 * class, o/w it returns null.
 * @author Ohad
 *
 */
@Component
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService
{

	@Override
	public Object loadUserBySAML(SAMLCredential credential)
			throws UsernameNotFoundException
	{
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String email = credential.getNameID().getValue();
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authorities.add(authority);
		
		UserDetails userDetails = new User(
				email, "password", true, true, true, true, authorities);

		return userDetails;
	}

}
