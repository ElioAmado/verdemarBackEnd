FROM maven:3.9.9-ibm-semeru-21-noble

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/app.jar"]
