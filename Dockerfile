FROM clojure:tools-deps-bullseye AS build

COPY . .

RUN clj -T:build uber
RUN ls
RUN ls target
RUN pwd

FROM eclipse-temurin:22-jre
COPY --from=build /tmp/target/ .
CMD java -jar sqrt-site-standalone.jar
