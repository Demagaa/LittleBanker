# LittleBanker

This REST API service helps user to manage the customers, accounts and transactions of simplified bank model.

Technologies: Java 17, Spring MVC, JDBC, Maven, Docker

Output: .war artifact built with Maven.

Launch:

Artifact can be deployed remotely on any native server that supports Tomcat by copying the WAR file

Interaction with the service is implemented via API calls. 
Postman API collection can be found in root directory (LittleBanker.postman_collection.json) file. 

Program needs MySQL database to be set up:

Database is dockerized, just run compose command in db-docker folder, this will configure DB container with some sample data:

`docker compose up`

Alternatively you can run .sql script located in db-docker/init folder and configure DB on your local server. 
Connectivity setting can be adjusted application.properties file (localhost:3308 and root account used by default).

Project status: improving stage
