FROM java:8-jdk-alpine

COPY ./target/xyinc-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch xyinc-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","xyinc-0.0.1-SNAPSHOT.jar"]
