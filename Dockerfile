# Stage 1: Build the application
FROM maven:3.8.4-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Si NO usas TLS/SSL, NO necesitas el certificado (elimina este RUN):
# RUN apk add --no-cache curl && \
#     curl -sS "https://truststore.pki.rds.amazonaws.com/us-east-1/us-east-1-bundle.pem" -o /etc/ssl/certs/rds-ca.pem
# Variables de entorno (opcional)
#ENV MONGODB_PASSWORD=moreto2804

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
## Etapa de construcción
#FROM maven:3.9.1-eclipse-temurin-17-alpine AS build
#WORKDIR /build
#
#COPY pom.xml .
#RUN mvn dependency:go-offline -B
#
#COPY src ./src
#RUN mvn clean package -DskipTests -B
#
## Etapa de ejecución
#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app
#
## Copiar el JAR generado desde la etapa de construcción
#COPY --from=build /build/target/*.jar app.jar
#
## Exponer el puerto 8080
#EXPOSE 8080
#
## Comando para iniciar la aplicación
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]