# businesscard

Tools needed:
1. Java 1.8+
2. Maven 3.x

Please follow the following to run the application:
1. Clone the project
2. Run command mvn spring-boot:run
3. Use tools like soapui, postman or browser to query

# urls
1. To query by id use this: http://localhost:8080/{icd}/{enterpriseNumber}

    Sample: http://localhost:8080/business-card/9956/0452435714

2. To query by name use this: http://localhost:8080/business-card/{name}

    Sample: http://localhost:8080/business-card/ACTUAL%20SYSTEMS

3. To search for names use this: http://localhost:8080/business-card/search/{name} 

    Sample: http://localhost:8080/business-card/search/B
