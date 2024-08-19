FROM openjdk:21-rc-jdk

WORKDIR /app

COPY target/keycloak-web-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

# Run:
#   'docker build -t ivangorbunovv/keycloak-web-image .'
