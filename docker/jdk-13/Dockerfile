# docker build --rm -f docker/jdk-13/Dockerfile -t pinkgorillawb/gorilla-notebook:jdk .
FROM openjdk:13-buster
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
LABEL vendor="Pink Gorilla" \
      maintainer="a.steffan@contentreich.de" \
      description="Pink Gorilla Notebook - JDK 13 version" \
      version="1.0"

EXPOSE 9000

# instead of logging to stdout, you may log to file in /log. create volume or mount host volume to /log
# RUN mkdir /log && chown daemon /log

COPY target/gorilla-notebook-*-standalone.jar /gorilla-notebook-standalone.jar
ADD docker/gorilla-notebook.sh /usr/bin

RUN mkdir /work
# RUN adduser --gecos "" --home /bar --uid 2000  --disabled-login gorilla
# USER gorilla

ENV GORILLA_HOME /
WORKDIR /work

CMD ["gorilla-notebook.sh"]
