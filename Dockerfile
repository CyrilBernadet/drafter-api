FROM adoptopenjdk:11-jre-hotspot
EXPOSE 3200
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]