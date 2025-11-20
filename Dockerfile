#Build stage

#Base image with Maven and JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
#Set working directory
WORKDIR /app
#Copy project files
COPY pom.xml .
COPY src ./src
#Build the application, skipping tests
RUN mvn -DskipTests clean package

#Run stage
#Base image with JRE 21
FROM eclipse-temurin:21-jre
#Set working directory
WORKDIR /app
#Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar
#Expose application port
EXPOSE 8080
#Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
