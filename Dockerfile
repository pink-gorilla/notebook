# Instructions:
# 1. build uber jar:
#       LEIN_SNAPSHOTS_IN_RELEASE=1 lein do clean, uberjar
# 2. build docker image
#       docker build --rm -t deas/gorilla-repl:latest .
# 3. run docker container
#       docker run --name gorilla-repl -d -p 9000:9000 deas/gorilla-repl:latest
#       docker run -v /conf:/conf -e CONFIG_FILE=/conf/docker.properties --rm -p 9090:9090 deas/gorilla-repl:latest

FROM java:8-jre-alpine
# FROM java:8
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
# EXPOSE 8080

CMD ["java", "-Dlog_level=info", "-jar", "/gorilla-repl-ng-standalone.jar"]


# instead of logging to stdout, you may log to file in /log. create volume or mount host volume to /log
# RUN mkdir /log && chown daemon /log

ADD target/gorilla-repl-ng-standalone.jar /
