FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# pom.xml 먼저 복사해서 의존성 캐싱
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 전체 소스 복사 후 빌드 (설치된 mvn 사용)
COPY src src
RUN mvn clean package -DskipTests

# 2. 실행 스테이지
FROM eclipse-temurin:21-jre-alpine

# 비루트 사용자 생성
RUN addgroup -g 1000 appgroup \
    && adduser -D -u 1000 -G appgroup appuser

WORKDIR /app

# JAR 복사 + 소유권 변경
COPY --chown=appuser:appgroup --from=build /app/target/*.jar app.jar

USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]