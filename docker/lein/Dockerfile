# Instructions:
# 1. build docker image
#       docker build --rm -t deas/gorilla-repl-lein:latest .
# 3. run docker container
#       docker run --name gorilla-repl-lein -d -p 9090:9090 deas/gorilla-repl-lein:latest
#       docker run -v /your/project:/usr/src/app --rm -p 9090:9090 deas/gorilla-repl-lein:latest

FROM clojure:lein-2.7.1-alpine
MAINTAINER Andreas Steffan <a.steffan@contentreich.de>

COPY . /usr/src/app
WORKDIR /usr/src/app
EXPOSE 9090
CMD ["lein", "gorilla", ":port", "9090", ":ip", "0.0.0.0"]
