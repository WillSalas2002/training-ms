FROM openjdk:17-alpine

COPY target/training-ms-0.0.1-SNAPSHOT.jar report.jar

ENTRYPOINT ["java","-jar","/report.jar"]
