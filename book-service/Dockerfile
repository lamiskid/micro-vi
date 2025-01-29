FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/book-service.jar  book-service.jar
ENTRYPOINT ["java", "-jar", "book-service.jar"]
