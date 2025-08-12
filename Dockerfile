FROM eclipse-temurin:21-jdk
WORKDIR /app

# 소스 코드 복사
COPY . /app

ENV JAVA_OPTS="-Xms512m -Xmx2048m"
ENV SPRING_PROFILES_ACTIVE=prod

# Playwright 환경변수 설정
#ENV PLAYWRIGHT_BROWSERS_PATH=$HOME/.cache/ms-playwright
#ENV PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=false

RUN apt-get update && apt-get install -y \
    curl \
    ca-certificates \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/* \
RUN npm ci
RUN npx playwright install --with-deps
RUN npx playwright --version

# Gradle 래퍼를 사용하여 Playwright 설치
#RUN chmod +x ./gradlew
#RUN ./gradlew installPlaywright

CMD ["sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Seoul -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE build/libs/confeti-0.0.1-SNAPSHOT.jar"]
