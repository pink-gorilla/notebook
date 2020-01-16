# https://ctr.run/documentation
# docker run ctr.run/github.com/pink-gorilla/gorilla-notebook[:commit-hash, branch-name, tag-name]
FROM clojure:openjdk-8 as build
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
LABEL vendor="Pink Gorilla" \
      maintainer="a.steffan@contentreich.de" \
      description="Pink Gorilla Notebook Builder" \
      version="1.0"

ARG GIT_REF=master

ENV REPO=git://github.com/pink-gorilla/gorilla-notebook.git
ENV NVM_VERSION v0.31.2
ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION stable

# TODO avoid duping with ./scripts (ci/cd)!
RUN curl --silent -o- https://raw.githubusercontent.com/creationix/nvm/${NVM_VERSION}/install.sh | bash \
    && . $NVM_DIR/nvm.sh \
    && nvm install $NODE_VERSION \
    && nvm alias default $NODE_VERSION \
    && nvm use default

# TODO : Not quite sure what ctr.run actually prefers
# WTF? COPY/ADD
# https://stackoverflow.com/questions/26504846/copy-directory-to-other-directory-at-docker-using-add-command
#ADD src /tmp/src
#ADD resources /tmp/resources
#COPY project.clj package-lock.json package.json /tmp/
COPY . /tmp/gorilla-notebook
#RUN git clone ${REPO} && \
#    cd gorilla-notebook && \

WORKDIR /tmp/gorilla-notebook
RUN . $NVM_DIR/nvm.sh && npm install
# RUN lein deps
# cat /etc/clojure-*.edn if lein threw up an it exists
RUN . $NVM_DIR/nvm.sh && lein with-profile +cljs uberjar

#FROM openjdk:13-jre
FROM openjdk:8-jre
COPY --from=build /tmp/gorilla-notebook/target/gorilla-notebook-*-standalone.jar /gorilla-notebook-standalone.jar
COPY --from=build /tmp/gorilla-notebook/docker/gorilla-notebook.sh /usr/bin/gorilla-notebook.sh

ENV GORILLA_HOME /
RUN mkdir /work
# RUN adduser --home /work --disabled-login --uid 2000 --gecos "" gorilla
# USER gorilla

WORKDIR /work

CMD ["gorilla-notebook.sh"]

