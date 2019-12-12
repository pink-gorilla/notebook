# docker build --rm -f docker/tools-deps/Dockerfile -t pinkgorillawb/gorilla-notebook:tools-deps .
# String interpolation does not appear to be working in CMD instruction
#ARG NOTEBOOK_VERSION=0.4.0
FROM clojure:tools-deps
#ARG NOTEBOOK_VERSION
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>
LABEL vendor="Pink Gorilla" \
      maintainer="a.steffan@contentreich.de" \
      description="Pink Gorilla Notebook - Tools deps version" \
      version="1.0"

EXPOSE 9000

RUN mkdir /work

WORKDIR /work

# CMD ["clojure", "-Sdeps", "{:deps {org.pinkgorilla/gorilla-notebook {:mvn/version \"${NOTEBOOK_VERSION}\"}}}", "-m", "pinkgorilla.core" ]
CMD ["clojure", "-Sdeps", "{:deps {org.pinkgorilla/gorilla-notebook {:mvn/version \"0.4.0\"}}}", "-m", "pinkgorilla.core" ]
