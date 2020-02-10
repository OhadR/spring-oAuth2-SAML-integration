# spring-oAuth2-SAML-integration [![Build Status](https://travis-ci.org/OhadR/spring-oAuth2-SAML-integration.svg?branch=master)](https://travis-ci.org/OhadR/spring-oAuth2-SAML-integration)   

How to integrate Spring-oAuth2 with Spring-SAML

Full article is here:
https://www.codeproject.com/Articles/598581/How-to-integrate-Spring-oAuth-with-Spring-SAML


# Introduction 
This document describes how to integrate the Spring-Security-oAuth2 project with Spring-Security-SAML.

I assume the reader is familiar with both oAuth and its components, and SAML and its components.

## 04-2018: Spring Versions Updated

On 04-2018, we have updated Spring versions.

* Spring:                4.2.4.RELEASE
* Spring Security:       4.0.3.RELEASE
* Spring Security oAuth: 2.0.9.RELEASE
* Spring Security SAML:  1.0.1.RELEASE

## 02-2020: Spring Versions Updated

On 02-2020, Spring versions were updated.

* Spring:                5.2.3.RELEASE
* Spring Security:       5.2.2.RELEASE
* Spring Security oAuth: 2.0.18.RELEASE
* Spring Security SAML:  1.0.3.RELEASE

If you wish to use the older version, (3.1.X, oAuth 1.0.5), you can find it tagged. 

# Motivation

Suppose you want your system to support oAuth2.  I would recommend using the Spring-Security-oAuth project. When you use Spring, you enjoy the many benefits of this open-source package: it is widely used, there is responsive support (in the forum), it is open source, and much more. This package allows the developer to write an oAuth-client, an oAuth resource server, or an oAuth authorization server.

Let us discuss SAML.  If you want to implement your own SAML SP (Service Provider), I recommend using Spring-Security-SAML, for the same reasons I recommended Spring-security-oAuth, above.

Now, consider an application that authenticates its users with oAuth, meaning the application is an "oAuth resource server", and its clients implement the oAuth protocol, meaning they are "oAuth clients".  I was asked to enable this application to connect SAML IdPs (identity providers) and authenticate users in front of them. This means the application must support not only oAuth, but SAML as well. Note, however, that if the application supports SAML, changes would have to be made in all clients, not only in the application itself. Currently the clients are "oAuth clients", (i.e., they fulfill the oAuth protocol). If the application supports SAML as well, the clients will also have to support it on their side. In SAML, the redirects are implemented differently, and the requests are different. So, the question is, how can we make this application support SAML without changing all clients?

The solution is to create an application ("the bridge") that will be a bridge between oAuth and SAML. When a non-authorized client tries to access the protected resource, it is redirected to the authorization server (this is how oAuth works). But here is the trick: from the client’s point of view– and from the application itself – this bridge functions as a valid "oAuth authorization server". Therefore, there is no need to change anything, not in the client code and not in the application code. On the other hand, instead of opening a popup dialog with username and password, this server functions as an SP and redirects the user to authenticate in front of a pre-configured IdP. 

# How to build? #

	mvn clean install.

# How to run? #

The easiest way is to use tomcat-maven-plugin, by 
    
	...\>mvn tomcat7:run


For this application purposes, MySQL is the Repository layer. Thus, validate the connection to the MySQL server (the properties in the default.properties), 
and make sure the following table created using the script:

<pre>
CREATE table `ohads`.saml_message_store(
    message_id VARCHAR(50) NOT NULL ,
    message BLOB NOT NULL
);
</pre>

note that 'ohads' is database-name (should appear in default.properties accordingly)

# Questions?

Feel free to open issues here if you have any unclear matter or any other question.