# 1. Java 11을 사용할 Spring Boot 베이스 이미지 선택
FROM openjdk:11-jdk-slim

# 2. 빌드된 jar 파일을 컨테이너 내부로 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 포트 오픈
EXPOSE 8876

# 4. 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app.jar"]