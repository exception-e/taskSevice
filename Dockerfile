FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 5433
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
LABEL authors="Olga"
