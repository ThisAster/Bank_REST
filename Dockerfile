# Используем базовый образ с JDK 17
FROM maven:3.9.8-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /bank-card-management

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /bank-card-management

# Копируем JAR файл в контейнер
COPY --from=build /bank-card-management/target/bank-card-management-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]