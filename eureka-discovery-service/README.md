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

At this point if we start  Application we can see eureka server running at port 8761 with no microservice instances registered.

![image info](/images/eureka-service-discovery/eureka-server-runnig-alone.png)


## Eureka Client Setup

We will create 2 Microservices which will be Eureka clients, <code>product-service</code> and <code>user-service</code>

 Example shown here is for <code>product-service</code>, similar steps are required for <code>user-service</code> as well

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

### 2. Add @EnableDiscoveryClient on main application class
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

### 2. Enable @EnableDiscoveryClient config on main class
```java
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}

```

### 3. Application properties file
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
We have created protected APIs using OAuth2, First start the keycloak server and then import these postman collections to checkout the request format

[PostMan Collection Link](/postman-collections/)
Get a token using either by standard auth flow or by password grant and use that token in Authorization Header and make Http GET request to the products or users end point

![image info](/images/eureka-service-discovery/api-request.png)


## Some Important Notes on Eureka

1. An application registered with Eureka is known as Eureka instance. Every Eureka instance is also a Eureka Client as it can fetch the details of other Eureka instances also.

2. If we are using the Eureka server in standalone mode, i.e. there’s only one Eureka server then we need to set <code>eureka.client.fetch-registry</code> and <code>eureka.client.register-with-eureka</code> to false so that it doesn’t try to register itself with itself as the Eureka server also has a built-in Eureka client.

3. A service is registered with the Eureka server when the <code>eureka.client.register-with-eureka</code> is set to true (by default, true) and it becomes an Eureka instance. Then this instance keeps sending heartbeats to the Eureka server. If Eureka server doesn’t receive a heartbeat from any instance within a particular time limit (by default, 30 secs) then it will consider that instance as DOWN and will de-register it from the service registry.

4. A Eureka instance is also a Eureka client as it fetches the registry from Eureka server containing the details of other instances. In order to enable it, <code>eureka.client.fetch-registry</code> is set to true (by default, true). As soon as a service registers itself with the server, it fetches the registry and catches it. It keeps on checking the registry at regular interval (by default, 30 secs) and if there is any change in the registry, it fetches the update only and the unchanged part is still used from the cache.

5. Each Eureka instance uses a service endpoint, <code>DiscoveryClient</code> in order to get a list of all the <code>ServiceInstance</code> instances of the services registered on the registry. 

6. We can also see all the registered instances at http://localhost:8761/eureka/apps