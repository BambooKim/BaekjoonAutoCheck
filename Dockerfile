
FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/baekjoon-0.0.1-SNAPSHOT.jar
ARG db_url
ARG db_user
ARG db_password
ARG jwt_secret
ARG jwt_token_validity_in_seconds

ENV DB_URL=${db_url}
ENV DB_USER=${db_user}
ENV DB_PASSWORD=${db_password}
ENV JWT_SECRET=${jwt_secret}
ENV JWT_TOKEN_VALIDITY_IN_SECONDS=${jwt_token_validity_in_seconds}


COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","-Dspring.datasource.url=jdbc:mysql://${DB_URL}/baekjoon","-Dspring.datasource.username=${DB_USER}","-Dspring.datasource.password=${DB_PASSWORD}","-Djwt-secret=${JWT_SECRET}","-Djwt-token-validity-in-seconds=${JWT_TOKEN_VALIDITY_IN_SECONDS}","/app.jar"]