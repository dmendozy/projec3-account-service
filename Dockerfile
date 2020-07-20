FROM openjdk:8
ADD target/account-service.jar account-service.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","account-service.jar"]