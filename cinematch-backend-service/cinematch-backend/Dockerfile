FROM maven as build
RUN apt-get update \
  && apt-get install -y ca-certificates curl git openssh-client --no-install-recommends \
  && rm -rf /var/lib/apt/lists/*


WORKDIR /app
ADD pom.xml /app
RUN mvn verify --fail-never

ADD . /app
RUN mvn package -DskipTests


FROM openjdk:21-jdk-slim
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-Dspring.profiles.active=localdocker", "-jar", "/app/app.jar"]