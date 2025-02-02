FROM eclipse-temurin:21-jdk-jammy@sha256:7b9c017c1c7272e8768a59422a7c37a9c870c9eae9926f715d4278bc5c3c3b9d AS compile
WORKDIR /project
COPY . .
RUN ./mvnw clean package -DskipTests
RUN ls -la

FROM eclipse-temurin:21-jdk-jammy@sha256:7b9c017c1c7272e8768a59422a7c37a9c870c9eae9926f715d4278bc5c3c3b9d AS builder
ARG JAR_FILE=target/*.jar
COPY --from=compile /project/${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:21-jdk-jammy@sha256:7b9c017c1c7272e8768a59422a7c37a9c870c9eae9926f715d4278bc5c3c3b9d AS final
LABEL authors="huygia12"
WORKDIR /app
RUN addgroup --system javauser && \
    adduser --system --shell /bin/false --ingroup javauser javauser

COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
RUN mkdir -p certs/
COPY certs/ certs/
RUN chown -R javauser:javauser /app
USER javauser

EXPOSE 8200
ENTRYPOINT ["java", "--enable-preview", "org.springframework.boot.loader.launch.JarLauncher"]
