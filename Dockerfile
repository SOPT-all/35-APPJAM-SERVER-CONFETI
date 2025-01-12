FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/confeti-0.0.1-SNAPSHOT.jar /app/confeti.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "confeti.jar"]