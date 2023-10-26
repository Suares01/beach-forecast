FROM --platform=linux/amd64 eclipse-temurin:20-jdk-jammy as base

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve

COPY src ./src

FROM base as build

RUN ./mvnw clean package -D skipTests

FROM --platform=linux/amd64 eclipse-temurin:20-jre-jammy as production

ENV CACHE_PASSWORD=${CACHE_PASSWORD}
ENV CACHE_HOST=${CACHE_HOST}
ENV CACHE_PORT=${CACHE_PORT}
ENV API_BASE_URL=${API_BASE_URL}
ENV PORT=${PORT}

EXPOSE ${PORT}

COPY --from=build /app/target/beach-forecast-api-gateway-*.jar /beach-forecast-api-gateway.jar

ENTRYPOINT ["java", "-jar", "beach-forecast-api-gateway.jar"]
