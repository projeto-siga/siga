FROM eclipse-temurin:8u352-b08-jre
COPY target/sigaarq.war /sigaarq.war
#RUN adduser -D -H -u 5001 -G users siga-arq
#USER siga-arq:users
EXPOSE 8080
CMD exec java ${JAVA_OPTS} -jar /sigaarq.war
