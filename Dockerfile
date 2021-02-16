FROM ubuntu:20.10
COPY target/*-runner /app
RUN chmod 750 /app
EXPOSE 8080
CMD [ "/app", "Dquarkus.http.host=0.0.0.0" ]
