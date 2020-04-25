FROM jcr.bongladesch.com/docker/ubuntu:18.04
COPY build/*-runner /app
RUN chmod 750 /app
EXPOSE 8080
CMD [ "/app", "Dquarkus.http.host=0.0.0.0" ]
