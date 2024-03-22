# Service Discovery using Eureka in Spring Microservices



## What is service discovery?
Service Discovery is how microservices discover each other over a network. 
It allows services to find and communicate with each other without hard-coding the hostname and port. The only ‘fixed point’ in such an architecture is the service registry, with which each service has to register.


![image info](/images/eureka-service-discovery/service-registration.jpg)

Eureka provides service discovery in a microservices architecture. This involves two steps on a high level:
- <b>Eureka server (service registry)</b>: It is a server that stores the addresses of all the registered microservices. Services register themselves on the Eureka server and details like name, host, and port are stored there.

- <b>Eureka Client</b>: It's a microservice registered on the central server and it updates and retrieves addresses to/from the central Eureka server. Details of other registered microservices become available for the registered service.


Lets setup microservices along with service discovery and an api gateway.
below image shows overview of system. Http request are sent to the API Gateway. API Gateway is itself an Eureka client and registered to the Eureka 
Server. Multiple Microservice instances are registered to the Eureka server.

API gateway can act as Load Balancer as well and load balances to the available instances in round robin function. 

![image info](/images/eureka-service-discovery/eureka.png)

## Eureka Server Setup
### 1. Add Required Dependencies
Head over to [Spring initializer](https://start.spring.io/) and create project with below dependencies
1. <b>Eureka Server</b> <code>SPRING CLOUD DISCOVERY</code>
spring-cloud-netflix Eureka Server.

2. <b>Eureka Discovery Client</b> <code>SPRING CLOUD DISCOVERY</code>
A REST based service for locating services for the purpose of load balancing and failover of middle-tier servers.

This will add below dependencies in <code>pom.xml</code> along with some spring cloud dependencies

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
### 2. Add <code>@EnableEurekaServer</code> on main Application class
```java
@EnableEurekaServer
@SpringBootApplication
public class ServiceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDiscoveryApplication.class, args);
	}

}
```

### 3. Add configuration in application.properties file
```
server.port = 8761

# Eureka Client Configs
eureka.client.register-with-eureka = false
eureka.client.fetch-registry = false

logging.level.com.netflix.eureka=OFF
logging.level.com.netflix.discovery=OFF
```

We have disabled properties register-with-eureka and fetch-registry in our standalone application but in production, generally, there is a cluster of eureka servers and they register among themselves as clients. But for learning purposes, we are running the server on the localhost and we don’t need a cluster. So we need to switch off the client's behavior so that it doesn't try to register itself. 

At this point if we start  Application we can see eureka server running at port 8761 with microservice instances registered.

![image info](/images/eureka-service-discovery/eureka-server-runnig-alone.png)


## Eureka Client Setup

We will create 2 Microservices which will be Eureka clients, <code>product-service</code> and <code>user-service</code> example shown is for <code>product-service</code>, similar steps are required for <code>user-service</code> as well

### 1. Add required dependencies

To include the Eureka Client in your project, use the starter with a group ID of org.springframework.cloud and an artifact ID of spring-cloud-starter-netflix-eureka-client. Add spring cloud dependencies as well in dependency management

<code>pom.xml</code>
```xml
<properties>
	<java.version>21</java.version>
	<spring-cloud.version>2023.0.0</spring-cloud.version>
</properties>
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
</dependencies>
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

### 2. Add @EnableEurekaClient on main application class
```java
@EnableDiscoveryClient
@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
```

### 3. Application properties
```
spring.application.name=product-service
server.port = 9002
# Eureka Client
eureka.client.serviceUrl.defaultZone = http://localhost:8761/eureka
```

serviceUrl.defaultZone is the address where eureka server is running.
Now when we start these two microservices we can see them registered in eureka service registry.



## API gateway Setup
### 1. Add Dependencies
Add <b>Reactive Gateway</b> <code> SPRING CLOUD ROUTING </code>,  <b>Eureka Discovery Client</b> <code>SPRING CLOUD DISCOVERY</code> and  <b> Spring Boot Actuator </b> <code> OPS </code> as dependencies from Spring Initializer

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-gateway</artifactId>
	</dependency>
	<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-eureka-client -->
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
</dependencies>
```

### 2. Application properties file
Define application name, server port and gateway routes in application.properties file
```
spring.application.name=api-gateway


server.port = 9000

#Gateway Routes properties
spring.cloud.gateway.routes[0].id = user-service
spring.cloud.gateway.routes[0].uri = lb://user-service
spring.cloud.gateway.routes[0].predicates[0] = Path=/users
spring.cloud.gateway.routes[0].predicates[1] = Method=GET
spring.cloud.gateway.routes[0].filters[0] = RemoveRequestHeader=Cookie


#Gateway Routes properties
spring.cloud.gateway.routes[1].id = product-service
spring.cloud.gateway.routes[1].uri = lb://product-service
spring.cloud.gateway.routes[1].predicates[0] = Path=/products
spring.cloud.gateway.routes[1].predicates[1] = Method=GET
spring.cloud.gateway.routes[1].filters[0] = RemoveRequestHeader=Cookie

# Eureka Server
eureka.client.service-url.default-zone = http://localhost:8761/eureka

#Actutaor endpoint
management.endpoint.gateway.enabled=true
management.endpoints.web.exposure.include=*

``` 

Now Start API Gateway as well which will register itself with the Eureka server

## Running APP 
We have created Protected APIs using OAuth2 so first start the keycloak server. and then import these postman collections to checkout the request format

[PostMan Collection Link](/postman-collections/)
Get a token using either by standard auth flow or by password grant and use that token in Authorization Header and make Http GET request to the products or users end point

![image info](/images/eureka-service-discovery/api-request.png)


