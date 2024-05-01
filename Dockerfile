FROM eclipse-temurin:17 as build

WORKDIR /app
COPY . .

# Gradle 실행 권한 부여
RUN chmod +x ./gradlew

RUN <<EOF
./gradlew bootJar
mv build/libs/*.jar app.jar
EOF

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/app.jar .

CMD ["java", "-jar", "app.jar"]
EXPOSE 8080



