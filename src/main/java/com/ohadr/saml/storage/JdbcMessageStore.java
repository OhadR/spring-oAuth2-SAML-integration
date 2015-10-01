package com.ohadr.saml.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.xml.XMLObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.saml.parser.SAMLObject;
import org.springframework.security.saml.storage.SAMLMessageStorage;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Implementation of token services that stores messages in a database.
 * 
 * @author Ohad Redlich
 */
@Component
public class JdbcMessageStore implements SAMLMessageStorage, InitializingBean 
{
	private static final Log LOG = LogFactory.getLog(JdbcMessageStore.class);

	private static final int XML_MESSAGE_COL_IDX = 2;

	private static final String DEFAULT_REFRESH_TOKEN_INSERT_STATEMENT = "insert into saml_message_store (message_id, message) values (?, ?)";

	private static final String DEFAULT_REFRESH_TOKEN_SELECT_STATEMENT = "select message_id, message from saml_message_store where message_id = ?";

	private static final String DEFAULT_REFRESH_TOKEN_DELETE_STATEMENT = "delete from saml_message_store where message_id = ?";

	@Autowired
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	public JdbcMessageStore() 
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		Assert.notNull(dataSource, "DataSource required");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public void storeMessage(String messageId, XMLObject message)
	{
		SAMLObject<XMLObject> object = new SAMLObject<XMLObject>(message);

		jdbcTemplate.update(DEFAULT_REFRESH_TOKEN_INSERT_STATEMENT,
				new Object[] { messageId, new SqlLobValue( SerializationUtils.serialize(object)) },
				new int[] { Types.VARCHAR, Types.BLOB });
	}

//	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) 
//	{
//		jdbcTemplate.update(insertRefreshTokenSql,
//				new Object[] { refreshToken.getValue(), new SqlLobValue(SerializationUtils.serialize(refreshToken)),
//						new SqlLobValue(SerializationUtils.serialize(authentication)) }, new int[] { Types.VARCHAR,
//						Types.BLOB, Types.BLOB });
//	}


	
	@Override
	public XMLObject retrieveMessage(String messageID)
	{
        SAMLObject<XMLObject> objectFromDB = null;

		try 
		{
			objectFromDB = jdbcTemplate.queryForObject(DEFAULT_REFRESH_TOKEN_SELECT_STATEMENT,
					new RowMapper<SAMLObject<XMLObject>>()
					{
						@Override
						public SAMLObject<XMLObject> mapRow(ResultSet rs, int rowNum) throws SQLException 
						{
							return SerializationUtils.deserialize(rs.getBytes( XML_MESSAGE_COL_IDX ));
						}
					}, messageID);
		}
		catch (EmptyResultDataAccessException e)
		{
			LOG.info("Failed to find saml-request for message " + messageID);
		}

        if (objectFromDB == null) {
            return null;
        } else {
            return objectFromDB.getObject();
        }
	}

	
//	public OAuth2RefreshToken readRefreshToken(String token) 
//	{
//		OAuth2RefreshToken refreshToken = null;
//
//		try {
//			refreshToken = jdbcTemplate.queryForObject(selectRefreshTokenSql,
//					new RowMapper<OAuth2RefreshToken>() {
//						@Override
//						public DefaultOAuth2RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
//							return SerializationUtils.deserialize(rs.getBytes(2));
//						}
//					}, token);
//		}
//		catch (EmptyResultDataAccessException e) {
//			if (LOG.isInfoEnabled()) {
//				LOG.info("Failed to find refresh token for token " + token);
//			}
//		}
//
//		return refreshToken;
//	}


	public void removeMessage(String messageID) 
	{
		jdbcTemplate.update(DEFAULT_REFRESH_TOKEN_DELETE_STATEMENT, messageID);
	}


}
