FROM maven:3.9.2-eclipse-temurin-17
WORKDIR /app
COPY target/verdemar-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]
