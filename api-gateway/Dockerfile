FROM ghcr.io/graalvm/graalvm-ce:ol8-java17
COPY ./target/*.jar api-gateway.jar
CMD ["java","-jar","api-gateway.jar"]