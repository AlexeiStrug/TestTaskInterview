FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./target/demo-0.0.1-SNAPSHOT.jar /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT ["java","-jar","app.jar"]