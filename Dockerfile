FROM openjdk:11-slim
ADD target/test-0.0.1-SNAPSHOT.jar /app/application.jar
CMD java -Dspring.config.location=classpath:/application.properties,file:/app/application.properties -jar /app/application.jar