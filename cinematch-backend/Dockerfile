#########-------FIRST BUILDER CONTAINER---------
FROM maven as build

RUN apt-get update \
  && apt-get install -y ca-certificates curl git openssh-client --no-install-recommends \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
RUN mvn verify --fail-never

COPY src ./src

CMD mvn spring-boot:run
