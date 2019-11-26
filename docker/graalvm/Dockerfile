# docker build --rm -f docker/graalvm/Dockerfile -t pinkgorillawb/gorilla-notebook:graalvm .
FROM oracle/graalvm-ce
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
LABEL vendor="Ping Gorilla" \
      maintainer="a.steffan@contentreich.de" \
      description="Pink Gorilla Notebook - GraalVM flavoured" \
      version="1.0"


EXPOSE 9000

# instead of logging to stdout, you may log to file in /log. create volume or mount host volume to /log
# RUN mkdir /log && chown daemon /log

ADD target/gorilla-notebook-standalone.jar /
ADD docker/gorilla-notebook.sh /

RUN mkdir /work
# RUN adduser -d /work --uid 2000 gorilla
# USER gorilla

WORKDIR /work

CMD ["java", "-Dlog_level=info", "-jar", "../gorilla-notebook-standalone.jar"]
