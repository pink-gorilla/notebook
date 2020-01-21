# docker build --rm -f docker/jdk-13-python/Dockerfile -t pinkgorillawb/gorilla-notebook:jdk-python .
FROM openjdk:13-buster
# FROM openjdk:8-jre
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
LABEL vendor="Pink Gorilla" \
      maintainer="a.steffan@contentreich.de" \
      description="Pink Gorilla Notebook - JRE 8 version with python" \
      version="1.0"

EXPOSE 9000

# Check: https://github.com/cnuernber/libpython-clj/blob/master/dockerfiles/CondaDockerfile

RUN apt-get update && apt-get install -y python3 python3-dev python3-pip \
        && rm -rf /var/lib/apt/lists/* \
        && python3 -m pip install numpy pandas

#RUN apt-get -qq update && apt-get -qq -y install curl wget bzip2 openjdk-8-jdk-headless \
#    && curl -sSL https://repo.continuum.io/miniconda/Miniconda3-latest-Linux-x86_64.sh -o /tmp/miniconda.sh \
#    && bash /tmp/miniconda.sh -bfp /usr/local \
#    && rm -rf /tmp/miniconda.sh \
#    && conda install -y python=3 \
#    && conda update conda \
#    && curl -O https://download.clojure.org/install/linux-install-1.10.1.492.sh \
#    && chmod +x linux-install-1.10.1.492.sh \
#    && ./linux-install-1.10.1.492.sh && rm linux-install-1.10.1.492.sh \
#    && wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein \
#    && chmod a+x lein \
#    && mv lein /usr/bin \
#    && apt-get -qq -y autoremove \
#    && apt-get autoclean \
#    && rm -rf /var/lib/apt/lists/* /var/log/dpkg.log \
#    && conda clean --all --yes

COPY target/gorilla-notebook-*-standalone.jar /gorilla-notebook-standalone.jar
ADD docker/gorilla-notebook.sh /usr/bin

ENV GORILLA_HOME /
RUN mkdir /work
# RUN adduser --home /work --disabled-login --uid 2000 --gecos "" gorilla
# USER gorilla

WORKDIR /work

CMD ["gorilla-notebook.sh"]
