FROM gradle:jdk8
COPY . /app
WORKDIR /app
RUN ./gradlew build -x test
ENTRYPOINT ["./gradlew", "bootRun"]