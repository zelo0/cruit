FROM openjdk:11-jdk

VOLUME /tmp

ARG JAR_FILE=./build/libs/cruit.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar", \
"-Dspring.config.location=/config/application.yml"]