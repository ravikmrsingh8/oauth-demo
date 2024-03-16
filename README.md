# OAuth2.0 in Action with Spring Boot 
## OAuth2.0
OAuth 2.0 is a <b>delegation protocol</b>, a means of letting someone who controls a resource allow a software application to access that resource on their behalf <b>without impersonating</b> them. 

The application requests authorization from the owner of the resource and receives tokens that it can use to access the resource. This all happens without the application needing to impersonate the person who controls the resource

```
RFC 6749 https://tools.ietf.org/html/rfc6749

The OAuth 2.0 authorization framework enables a third-party application to obtain limited access to an HTTP service, either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP service, or by allowing the third-party application to obtain access on its own behalf.
```

### The OAuth Dance
OAuth is a complex security protocol with different components sending pieces of information to each other in a precise balance akin to a technological dance :blush:.
Fundamentally, there are two major steps are involve in in an OAuth transaction:
1. issuing a token 
2. using the token

The token represents the access that's been delegated to the client. OAuth transaction consists of following sequences

1. The Resource ownwr indicates to the client that they would like the client to act on their behalf (for example, "Go load my photos from that service so I can print them").
2. The client requests authorization from the Resource owner at the Authorization Server.
3. The Resource Owner grants authorization to the client.
4. The client receives a Token from the Authorization server.
5. The client presents the Token to the Protected Resource
