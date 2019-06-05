FROM openjdk:9-jre
# the http-port the app is using
ARG http_port=8080
# the home directory where the app is located
ENV APP_HOME /var/i18nedit

# create home directory
RUN mkdir -p $APP_HOME
RUN mkdir -p /i18nedit
# copy the current distribution-build of the app in the home directory
COPY backend/build/libs/i18nedit-1.0-SNAPSHOT-all.jar $APP_HOME
COPY frontend/dist/i18nedit-frontend $APP_HOME/www
COPY backend/config.docker.json /i18nedit/config.json
# copy startup-script and make it executable
COPY scripts/startup.sh $APP_HOME/startup.sh
RUN chmod +x $APP_HOME/startup.sh

WORKDIR $APP_HOME

# when container is launched start the distribution start script
ENTRYPOINT $APP_HOME/startup.sh
