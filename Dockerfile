FROM gradle:6.8.3 AS builder
WORKDIR /src
COPY . .
RUN  ./gradlew build -x test

FROM amazoncorretto:19.0.2-alpine AS service
WORKDIR /app
COPY --from=builder /src/build/libs/demo-1.0.0.jar .
COPY /src/main/resources/application.properties .
# Public details
WORKDIR /app
EXPOSE 8080
CMD ["java","-jar demo-1.0.0.jar"]
