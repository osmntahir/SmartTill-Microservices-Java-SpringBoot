# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Copy the JAR file to the container
ADD target/user-management-service-0.0.1-SNAPSHOT.jar user-management-service-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT [ "java","-jar","user-management-service-0.0.1-SNAPSHOT.jar" ]