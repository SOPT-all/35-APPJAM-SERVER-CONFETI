FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/confeti-0.0.1.jar /app/confeti.jar

# Container Memory Limit : 2g, Best Practice of Heap Memory : 512m ~ 1280m (60 ~ 70%)
ENV JAVA_OPTS="-Xms512m -Xmx1280m"

CMD ["sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Seoul -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE confeti.jar"]
