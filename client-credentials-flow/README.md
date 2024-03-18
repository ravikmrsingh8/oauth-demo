## OAuth2 client credentials flow

The Client Credentials flow is used when an application needs to access its own resources, rather than a user's resources. In this flow, the application sends its client ID and client secret to the authorization server, and in return receives an access token that can be used to access protected resources.

![image info](/images/client_credentials-flow/client_credentials_grant.png)

### Basic authentication
The first and most common mechanism for authenticating a client in the Client Credentials flow is Basic Authentication. In this mechanism, the client sends its client ID and client secret as part of the Authorization header in an HTTP request. The The Authorization header contains a Base64-encoded string of <code>{client-ID}:{client-secret}</code>.

### Body Authentication
In the Body Authentication mechanism, the client sends its client ID and client secret as parameters in the body of the HTTP request. The <code>Content-Type</code> header in this case is set to <code>application/x-www-form-urlencoded</code>

<hr />

## Client Credentials OAuth flow with Basic Authentication Hands-on
Configure Client registration to support client_credentials grant 

![image info](/images/client_credentials-flow/client_config.png)

Trigger a POST Request to fetch Access Token on token Endpoint 

Set Authorization Header
![image info](/images/client_credentials-flow/basic_authorization_header.png)

Triggers POST Request with grant_type and scope param in body
![image info](/images/client_credentials-flow/access_token.png)
