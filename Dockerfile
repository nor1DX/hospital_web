FROM gradle:9.4.1-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle

COPY src src

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

ENV POSTGRES_HOST=postgres
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=hospital
ENV POSTGRES_USER=hospital
ENV POSTGRES_PASSWORD=hospital

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
