# Client registrartion to the Authorization Server
Prerequisite
1. [Setup keycloak server](/keycloak-setup/README.md) 
2. [Setup a new realm ](/keycloak-setup/README.md#create-a-realm)
3. [Add user](/keycloak-setup/README.md#create-a-user)


### Realm and Client used in example code
1. Created a new realm with name oauth-demo
2. Below steps outlines the client registration process in Keycloak

    ![image info](/images/standard-authorization-flow/client-registration.png)

    ![image info](/images/standard-authorization-flow/standard-flow-with-client-secret.png)

    ![image info](/images/standard-authorization-flow/configure-redirect-uri.png)

3. Get Client secret in credentials tab
    ![image info](/images/standard-authorization-flow/client-secret.png)

4. once a realm is created then we can use the well-known endpoints by this url [well-known endpoints](http://localhost:8080/realms/oauth-demo/.well-known/openid-configuration) (http://localhost:8080/realms/oauth-demo/.well-known/openid-configuration)