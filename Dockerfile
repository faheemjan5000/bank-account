FROM openjdk:17
EXPOSE 8080
ADD target/bank-app.jar bank-app.jar
ENTRYPOINT ["java","-jar","/bank-app.jar"]