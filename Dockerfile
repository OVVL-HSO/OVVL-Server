FROM java:8
EXPOSE 8080
ARG mongoConnection
ARG jwtSecret
ARG supportMail
ARG supportMailPW
ARG supportMailReceiver
ENV MONGODB_CONNECTION=$mongoConnection
ENV OVVL_JWT_SECRET=$jwtSecret
ENV SUPPORT_MAIL_SENDER=$supportMail
ENV SUPPORT_MAIL_SENDER_PW=$supportMailPW
ENV SUPPORT_MAIL_RECEIVER=$supportMailReceiver
COPY build/libs/tam-server.jar tam-server.jar
ENTRYPOINT ["java", "-jar", "tam-server.jar"]
