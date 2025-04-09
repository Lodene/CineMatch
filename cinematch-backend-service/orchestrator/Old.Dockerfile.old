FROM maven as build
RUN apt-get update \
  && apt-get install -y ca-certificates curl git openssh-client --no-install-recommends \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app
ADD . /app
RUN mvn install:install-file -Dfile=/app/common-module-1.0.0-orchestrator.jar -DgroupId=com.projet_asi_ii -DartifactId=common-module -Dversion=1.0.0 -Dpackaging=jar
RUN mvn verify --fail-never
RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008", "-Dspring.profiles.active=localdocker", "-jar", "/app/app.jar"]