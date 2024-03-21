##  API Gateway
An API gateway simplifies the communication between a client and a service, whether that be between a user’s web browser and a server, or between a frontend application and the backend application that it relies on. The main purpose of integrating the API gateway in microservice communication is, API Gateway acts as a single entry point to access services.

![image info](/images/API-Gateway-in-Microservices.png)


## Spring Cloud API gateway

### Dependencies

#### Reactive Gateway SPRING CLOUD ROUTING
Provides a simple, yet effective way to route to APIs in reactive applications. Provides cross-cutting concerns to those APIs such as security, monitoring/metrics, and resiliency.

#### Spring Boot Actuator OPS
Supports built in (or custom) endpoints that let you monitor and manage your application - such as application health, metrics, sessions, etc.

### Configuration 
application.properties file has routes specified
```
 spring.cloud.gateway.routes[0].id = user-service
 spring.cloud.gateway.routes[0].uri = http://localhost:9001
 spring.cloud.gateway.routes[0].predicates[0] = Path=/users
 spring.cloud.gateway.routes[0].predicates[1] = Method=GET
 spring.cloud.gateway.routes[0].filters[0] = RemoveRequestHeader=Cookie

 spring.cloud.gateway.routes[1].id = product-service
 spring.cloud.gateway.routes[1].uri = http://localhost:9002
 spring.cloud.gateway.routes[1].predicates[0] = Path=/products
 spring.cloud.gateway.routes[1].predicates[1] = Method=GET
 spring.cloud.gateway.routes[1].filters[0] = RemoveRequestHeader=Cookie
```

To enable actuator endpoints for gateway at <code>http://localhost:9000/actuator/gateway/routes</code> use below properties.
```
management.endpoint.gateway.enabled=true
management.endpoints.web.exposure.include = *
```