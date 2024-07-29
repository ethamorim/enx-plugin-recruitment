FROM gradle:8.9.0-jdk21-jammy AS build
WORKDIR /usr/app
COPY . .
RUN gradle :mc-betterwindcharge:shadowJar && gradle :mc-home:shadowJar

FROM bellsoft/liberica-openjre-debian:21-cds AS prod

ENV BETTERWINDCHARGE_ARTIFACT_NAME=mc-betterwindcharge-1.0-all.jar
ENV HOME_ARTIFACT_NAME=mc-home-1.0-all.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME

COPY --from=BUILD $APP_HOME .

RUN mkdir $APP_HOME/server/plugins \
    && cp $APP_HOME/mc-betterwindcharge/build/libs/$BETTERWINDCHARGE_ARTIFACT_NAME $APP_HOME/server/plugins/ \
    && cp $APP_HOME/mc-home/build/libs/$HOME_ARTIFACT_NAME $APP_HOME/server/plugins

EXPOSE 25565
WORKDIR $APP_HOME/server
ENTRYPOINT exec java -jar spigot.jar nogui