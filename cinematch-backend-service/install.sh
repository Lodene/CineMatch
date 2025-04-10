#!/bin/bash

cd ./common-module
mvn clean install
cd ..
docker compose up --build -d
