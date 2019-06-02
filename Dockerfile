FROM openjdk:9-jre
# the http-port the app is using
ARG http_port=80
# the home directory where the app is located
ENV APP_HOME /var/i18nedit
# create home directory
RUN mkdir -p $APP_HOME
# copy the current distribution-build of the app in the home directory
COPY backend/build/distributions/alpas.tar $APP_HOME/app.tar
COPY frontend/dist/i18nedit-frontend $APP_HOME/www
# copy startup-script and make it executable
COPY scripts/startup.sh $APP_HOME/startup.sh
RUN chmod +x $APP_HOME/startup.sh
# unpack the distribution
RUN cd $APP_HOME && tar xf app.tar
# when container is launched start the distribution start script
ENTRYPOINT $APP_HOME/startup.sh
