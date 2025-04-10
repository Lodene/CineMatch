#!/bin/bash

cd cinematch-backend-service/common-module
mvn clean install
cd ../..
docker compose up --build -d
