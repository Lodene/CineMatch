cd cinematch-backend-service\common-module
mvn clean install
cd ..\..
podman compose up --build  -d