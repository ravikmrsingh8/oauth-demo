## Password grant type
The Password grant type is a legacy way to exchange a user's credentials for an access token. Because the client application has to collect the user's password and send it to the authorization server, it is not recommended that this grant be used at all anymore.

This flow provides no mechanism for things like multi-factor authentication or delegated accounts, so is quite limiting in practice.

The latest [OAuth 2.0 Security Best Current Practice](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics) spec actually recommends against using the Password grant entirely, and it is being removed in the OAuth 2.1 update.

### Request Parameters
The access token request will contain the following parameters.

grant_type (required) – The <code>grant_type</code> parameter must be set to “password”.

username (required) – The user’s username.

password (required) – The user’s password.

scope (optional) – The scope requested by the application.

Client Authentication (required if the client was issued a secret) - Base 64 encoded value of <code>{client_id:client_secret}</code> in Authorization Header

![image info](/images/password-grant/password-grant.png)