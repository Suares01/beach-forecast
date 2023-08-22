FROM --platform=linux/amd64 eclipse-temurin:17-jdk-focal

FROM --platform=linux/amd64 maven

ARG DATASOURCE_URL
ARG DATASOURCE_USERNAME
ARG DATASOURCE_PASSWORD
ARG CACHE_PASSWORD
ARG CACHE_HOST
ARG CACHE_PORT
ARG JWT_SECRET

ENV DATASOURCE_URL=${DATASOURCE_URL}
ENV DATASOURCE_USERNAME=${DATASOURCE_USERNAME}
ENV DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}
ENV CACHE_PASSWORD=${CACHE_PASSWORD}
ENV CACHE_HOST=${CACHE_HOST}
ENV CACHE_PORT=${CACHE_PORT}
ENV JWT_SECRET=${JWT_SECRET}

COPY . /app

WORKDIR /app

RUN mvn clean package

CMD ["java", "-jar", "target/beach-forecast-api-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080