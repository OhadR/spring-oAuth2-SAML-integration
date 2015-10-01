package com.ohadr.saml.storage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml.storage.SAMLMessageStorage;
import org.springframework.security.saml.storage.SAMLMessageStorageFactory;
import org.springframework.stereotype.Component;


/**
 * this class is used by Spring-SAML to get the message-storage. the (Spring's) default impl - HttpSessionStorageFactory - returns HttpSessionStorage.
 * this class does nothing but to return an autowired member, that manages all it dependencies (data-store, cache, etc).
 * @author Ohad
 *
 */
@Component
public class JdbcMessageStorageFactory implements SAMLMessageStorageFactory
{
	@Autowired
	private JdbcMessageStore jdbcSAMLMessageStorage;
	
	@Override
	public SAMLMessageStorage getMessageStorage(HttpServletRequest request)
	{
        return jdbcSAMLMessageStorage;
	}
}



