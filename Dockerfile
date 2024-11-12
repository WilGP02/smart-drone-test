FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/droneapp-1.0-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","droneapp-1.0-SNAPSHOT.jar"]