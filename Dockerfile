FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
WORKDIR hlaeja
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/hlaeja/application.jar"]
