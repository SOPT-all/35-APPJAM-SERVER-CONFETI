FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/confeti-0.0.1-SNAPSHOT.jar /app/confeti.jar

ENV JAVA_OPTS="-Xms512m -Xmx2048m"
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Seoul -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE confeti.jar"]
