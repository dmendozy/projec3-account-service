FROM openjdk:8
ADD target/account-service.jar account-service.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","account-service.jar"]