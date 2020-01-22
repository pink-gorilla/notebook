# Instructions:
# 1. build uber jar:
#       LEIN_SNAPSHOTS_IN_RELEASE=1 lein do clean, uberjar
# 2. build docker image
#       docker build --rm -f docker/jdk-13-alpine/Dockerfile -t pinkgorillawb/gorilla-notebook:jdk-alpine .
# 3. run docker container
#       docker run --name gorilla-notebook -d -p 9000:9000 pinkgorillawb/gorilla-notebook:jdk-alpine
#       docker run -v /conf:/conf -e CONFIG_FILE=/conf/docker.properties --rm -p 9090:9090 pinkgorillawb/gorilla-notebook:jdk-alpine
#
# Spark does not work with alpine version
# UnsatisfiedLinkError /tmp/snappy-unknown-4324a351-29e8-4c89-8305-5bf8d05168cf-libsnappyjava.so: Error loading shared library ld-linux-x86-64.so.2: No such file or directory (needed by /tmp/snappy-unknown-4324a351-29e8-4c89-8305-5bf8d05168cf-libsnappyjava.so) java.lang.ClassLoader$NativeLibrary.load (ClassLoader.java:-2)

# FROM openjdk:8-jre
FROM openjdk:13-jdk-alpine
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
LABEL vendor="Pink Gorilla" \
      maintainer="a.steffan@contentreich.de" \
      description="Pink Gorilla Notebook - JDK 13 Alpine version" \
      version="1.0"

EXPOSE 9000

# instead of logging to stdout, you may log to file in /log. create volume or mount host volume to /log
# RUN mkdir /log && chown daemon /log

COPY target/gorilla-notebook-*-standalone.jar /gorilla-notebook-standalone.jar
ADD docker/gorilla-notebook.sh /usr/bin

ENV GORILLA_HOME /
RUN mkdir /work
# RUN adduser --home /work --disabled-login --uid 2000 --gecos "" gorilla
# USER gorilla

WORKDIR /work

CMD ["gorilla-notebook.sh"]
