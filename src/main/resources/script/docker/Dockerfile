#FROM findepi/graalvm:java17-native
FROM openjdk:17.0.2-oraclelinux8

LABEL maintainer="Meng Wei Jin"

ARG DIR=/opt/app

RUN mkdir -p ${DIR}

WORKDIR ${DIR}

ENV SERVER_PORT=8080 LANG=C.UTF-8 LC_ALL=C.UTF-8
ENV JAVA_OPTS="-Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError"

EXPOSE ${SERVER_PORT}

#ADD ./target/app-admin.jar ./app.jar

ENTRYPOINT java -Dserver.port=${SERVER_PORT} ${JAVA_OPTS} -jar app.jar