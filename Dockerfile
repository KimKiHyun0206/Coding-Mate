# 베이스 이미지 설정 (Java 17을 사용하는 OpenJDK)
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 복사
COPY gradle /app/gradle
COPY gradlew gradlew.bat /app/

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# Gradle 캐시 레이어
COPY build.gradle settings.gradle /app/
RUN --mount=type=cache,id=gradle,target=/home/gradle/.gradle ./gradlew dependencies

# 프로젝트 소스 코드 복사
COPY . /app

# Gradle 빌드 (bootJar 태스크 실행)
RUN ./gradlew bootJar

# 빌드 결과물 (jar 파일) 경로 설정
ARG JAR_FILE=build/libs/*.jar

# 실행할 jar 파일 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행 포트 노출
EXPOSE 8080

# 애플리케이션 실행 사용자 설정 및 소유권 변경
RUN chown -R root:root /app

USER root

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]