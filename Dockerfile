FROM java:8-jdk-alpine

RUN echo 'http://dl-cdn.alpinelinux.org/alpine/v3.6/main' >> /etc/apk/repositories
RUN echo 'http://dl-cdn.alpinelinux.org/alpine/v3.6/community' >> /etc/apk/repositories
RUN apk update
RUN apk add mongodb=3.4.4-r0

RUN mongo --version

COPY ./target/xyinc-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch xyinc-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","xyinc-0.0.1-SNAPSHOT.jar"]
