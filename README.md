# Tool Room #

## Introduction ##
I am not proud of my Java Bootcamp exam application. I decided to rebuild and improve a few things. 
This time I focused only on the backend. I've given up online viewing (zero HTML, JSP and so on).
The basic functions have remained the same. 
I created a REST API using all the advantages of Spring Boot. 

## Differences from the exam application ##
* simple authorization
* validation
* unit tests for all complex packages: users, rentals and tools and one (of five) simple: category
* logs
* spring doc
* jar package instead of war

## Architecture ##
Backend - Java 17, Spring Boot 2.7.5, My SQL database by default placed locally.
Frontend - no frontend ;)

## Configuration ##

[application.yml](https://github.com/pawsko/ToolRoom/blob/master/src/main/resources/application.yml) 

```
spring:
	secutity:
		user:
			username: admin
			password: admin
	datasource:
		url: jdbc:mysql://localhost:3306/tools
		username: root
		password: coderslab
```

API:

http://localhost:8080 - homepage

Swagger:

http://localhost:8080/swagger-ui/index.html

First of all you need create some tools, create users (like colleagues, family members, or neighbours) from Utilities menu. After that you can rent particular tool.
Postman is a very usefull tool to test API calls. All of available. CRUD without "D" (there is no delete ability), due to the traceability of history.
<img alt="Homepage" src="https://github.com/pawsko/ToolRoom/blob/master/media/homepage.png">
<img src="https://github.com/pawsko/ToolRoom/blob/master/media/Swagger.png">
<img src="https://github.com/pawsko/ToolRoom/blob/master/media/GETbyID.png">
<img src="https://github.com/pawsko/ToolRoom/blob/master/media/PUTbyID.png">
<img src="https://github.com/pawsko/ToolRoom/blob/master/media/GET.png">
<img src="https://github.com/pawsko/ToolRoom/blob/master/media/POST.png">
