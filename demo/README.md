# Read Me First
This project is created for ignitesol coding challenge purpose:
* This project skeleton is created using [spring initializr](https://start.spring.io) on the website for more details.
* This project is built using spring boot framework.
* Few Maven modules version numbers are changed to support JDK versions installed on the machine.
* Docker will be advantage in order to run required pieces of software as images instead of installing them locally, however system was not supporting.
* Server port is mentioned as 8081 due to default port 8080 is unavailable.
* Created account in twitter and also registered in Twitter developer account in order to acquire related api access information
* Twitter access keys and secrete keys will be revoked with in one or two weeks for security concerns.
* Did not modify any default port information for zookeeper and kafka during this project. 

# Required Software
* JDK 1.8 or above
* zookeeper
* kafka
* [Reference website](https://dzone.com/articles/running-apache-kafka-on-windows-os)

# Topics covered in this demo
* REST API
* openApi swagger
* scheduler
* kafka producer
* kafka dynamic consumer
* websockets

# Getting Started
* zookeeper
    
        zkserver
* kafka

        kafka-server-start.bat ..\..\config\server.properties
* Actual project

      mvn spring-boot:run

* [swagger UI](http://localhost:8081/swagger-ui.html)
* [UI to see messages from websocket](http://localhost:8081)

# Useful commands
* for posting messages to kafka topic

        kafka-console-producer.bat --broker-list localhost:9092 --topic [topic]
* for consuming messages from kafka topic from the beginning

      kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic [topic] --from-beginning
* getting available topic list

      kafka-topics.bat --bootstrap-server localhost:9092 --list
