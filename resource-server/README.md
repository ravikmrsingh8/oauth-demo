# Resource Server

## JWT Validation
This lesson discusses JSON web token validation.

We'll cover the following
1. How tokens are signed
2. Symmetric Signatures
3. Asymmetric Signatures
4. How is token validated
5. Claims validations

Here is the basic flow of JWT authentication:
1. The client sends a request to the server with user credentials.
2. The server generates a signed JWT for the client if the credentials are valid.
3. The server sends the token back to the client which is stored in the browser.
4. For every subsequent request, the client sends the token back to the server.
5. The server validates the token, and if it is valid then grants access to the client.

## How tokens are signed?
There are two mechanisms to sign a token:

### Symmetric Signatures
When a JWT is signed using a secret key, then it is called a symmetric signature. This type of signature is done when there is only one server that signs and validates the token. The same secret key is used to generate and validate the token. The token is signed using HMAC.

HMAC stands for Hashing for Message Authentication Code. It’s a message authentication code obtained by running a cryptographic hash function (like MD5, SHA1, and SHA256) over the data (to be authenticated) and a shared secret key



### Asymmetric Signatures
This signature is suitable for distributed scenarios. Suppose there are multiple applications that can validate a given JWT. If we use a secret key to sign a JWT, then these applications will need that key to validate the token.

It is not possible to share the secret key amongst all the applications, as it may get leaked. To solve this issue, asymmetric signing is done. Asymmetric signing uses a private-public key pair for signing. There is one server that has the private key. This server generates the tokens, signs them using the private key, and shares it with the client.

Now the client can send this token to any application and they can validate it using the public key. This signature is done using RSA. It is asymmetric encryption and a digital signature algorithm.



## How is token validated?
### Signature Validation
Let us now look at how a server validates a JWT. We already know that a JWT has three parts: a header, a payload, and a signature.

When a server receives a token, it fetches the header and payload from that token. It then uses the secret key or the public key (in the case of asymmetric signing) to generate the signature from the header and payload.

If the generated signature matches the signature provided in the JWT, then it is considered to be valid.



### Claims validations
It is not sufficient to just validate the signature of the token. There are a few other security properties that need to be validated as discussed below:

1. Check if the token is still valid. This can be validated through exp claim.
2. Validate that the token is actually meant for mentioned resource server through the aud claim.
3. Check the scope (or Authority) provided to complete a given action at resource server
4. Check if the token can be used at this time using the nbf claim. NBF stands for not before which means that this token should not be used before a particular time.




## Spring Resource Server Configs

Get jwk-set-uri from well known configuration end point. this config is needed so that OAuth resource server can read the Public Key of Authorization server and validate the token signature.
```
spring.application.name=resource-server
server.port=8082

spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/oauth-demo/protocol/openid-connect/certs
```

SecurityFilterChain Bean can be configured to check the Authorities(Scopes) required to complete or Allow and REST Endpoint
```java
@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/users/status").hasAuthority("SCOPE_readuser"));
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/users/token").hasRole("developer"));
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        // This application is an OAuth2 resource server and accepts jwt tokens
        http.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));

        return http.build();
    }
}

```