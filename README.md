# OAuth2.0 in Action with Spring Boot 
# OAuth2.0
OAuth 2.0 is a <b>delegation protocol</b>, a means of letting someone who controls a resource allow a software application to access that resource on their behalf <b>without impersonating</b> them. 

The application requests authorization from the owner of the resource and receives tokens that it can use to access the resource. This all happens without the application needing to impersonate the person who controls the resource

 RFC 6749 https://tools.ietf.org/html/rfc6749
> <i> The OAuth 2.0 authorization framework enables a third-party application to obtain limited access to an HTTP service, either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP service, or by allowing the third-party application to obtain access on its own behalf.</i>


## The OAuth Dance
OAuth is a complex security protocol with different components sending pieces of information to each other in a precise balance akin to a technological dance :blush:.
Fundamentally, there are two major steps are involve in in an OAuth transaction:
1. issuing a token 
2. using the token

The token represents the access that's been delegated to the client. OAuth transaction consists of following sequences

1. The Resource owner indicates to the client that they would like the client to act on their behalf (for example, "Go load my photos from that service so I can print them").
2. The client requests authorization from the Resource owner at the Authorization Server.
3. The Resource Owner grants authorization to the client.
4. The client receives a Token from the Authorization server.
5. The client presents the Token to the Protected Resource


## Entities in OAuth

### Scopes 
Scope is used by OAuth2 to restrict access to a resource. When requesting an access token from an authorization server, a client application will include a scope request parameter specifying a list of scopes or an amount of access to user Resources the generated access token should have. In turn, the authorization server uses the “scope” response parameter to inform the client of the scope of the access token issued.

The OAuth 2 documentation says that <i>"Scope is a mechanism in OAuth 2.0 to limit an application’s access to a user’s account. An application can request one or more scopes, this information is then presented to the user in the consent screen, and the access token issued to the application will be limited to the scopes granted.

The value of the scope parameter is expressed as a list of space-delimited, case-sensitive strings. The strings are defined by the authorization server. If the value contains multiple space-delimited strings, their order does not matter, and each string adds an additional access range to the requested scope."</i>



## Hands On
1. We will create an Outh2Client App which will display the list of products.
This client will send the request to API Gateway. API Gateway will Consult Eureka to the find the resource server instances and then route the request to the resource server in round robin fashion.
    
    Spring MVC App (OAuth2 Client use standard code flow for Authorization) wil look like below   

    ![image info](/images/client-app/home-page.png)

2. A React SPA(OAuth2 Client and Use PKCE for Authorization) which will display the List of Users