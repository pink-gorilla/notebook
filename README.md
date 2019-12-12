# Pink Gorilla Notebook [![GitHub Actions status |pink-gorilla/gorilla-notebook](https://github.com/pink-gorilla/gorilla-notebook/workflows/CI/badge.svg)](https://github.com/pink-gorilla/gorilla-notebook/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/gorilla-notebook.svg)](https://clojars.org/org.pinkgorilla/gorilla-notebook)
## Docker Image
<!-- [![dockeri.co](https://dockeri.co/image/pinkgorillawb/gorilla-notebook)](https://hub.docker.com/r/pinkgorillawb/gorilla-notebook) -->
[![](https://images.microbadger.com/badges/version/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own version badge on microbadger.com")
[![](https://images.microbadger.com/badges/image/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own image badge on microbadger.com")

Pink Gorilla Notebook is a rich browser based notebook REPL for Clojure and ClojureScript,
which aims at extensibility (development- and runtime) and user experience while being very lightweight.
Extensibility primarily revolves around UI widgets and data.

Use cases we are trying to cover are
- Persistent experiments and demos (Clojure/ClojureScript libraries!)
- Courses and education on all matters related to clojure
- Data science
- Pluggable "alien" JEE webapp instrumentation

We'll try to borrow from other ecosystems where bridging from Clojure/ClojureScript appears reasonable. Wrapping and
leveraging R is one example that comes to mind.

We are a  is a decendant of [Gorilla REPL](http://gorilla-repl.org).

## Status (January 2020)
You can probably imagine that ideas come up way faster than we can explore them. Therefore, we consider it essential
being able to move fast and safe.

Basic building blocks for moving safe fast (Tests, CI, CD, Code Quality Tooling) are pretty much in place across
our repos. We appear to have caught up with recent version of the libraries and tools we use.

- Clojure/ClojureScript
- Shadow CLJS
- Jetty/Ring
- Tailwind
- React/Reagent/Reframe
- Cider
- Karma

Not everything is rainbows yet and there is still some yak to shave and things are missing, including

- Tests
- Routing (Clojure/ClojureScript)
- re-frame
- UI Visual appearance ;)
- Various things from dependency hell and the ClojureScript Kernel

## Extensibility

We try to keep the code shipping with the bare notebook application minimal and aim at runtime customization where
 possible. The application (jar/uberjar) currently ships two flavors:

- `:advanced` optimization without ClojureScript Kernel Support
- `:none` optimization with ClojureScript Kernel support and runtime extensibility

We support JVM library (`pomegranate`)-, ClojureScript- and JavaScript (`requirejs`) extensibility at runtime.

## Run Gorilla

### Via clojure / clojars

The easiest (but not the fastest ;) way to run releases locally is leveraging the cli (directly)
```
clojure -Sdeps '{:deps {org.pinkgorilla/gorilla-notebook {:mvn/version "0.4.1"}}}' -m pinkgorilla.core
```
You'll get available command line options appending `--help`:
```
clojure -Sdeps '{:deps {org.pinkgorilla/gorilla-notebook {:mvn/version "0.4.1"}}}' -m pinkgorilla.core --help
```
so
```
clojure -Sdeps '{:deps {org.pinkgorilla/gorilla-notebook {:mvn/version "0.4.1"}}}' -m pinkgorilla.core -P 9111
```
will start up the HTTP server at port 9111.

### Via Docker Image

Alternatively, you can use the clojure docker image:
```
docker run -p 9000:9000 -v `pwd`/.m2:/root/.m2:rw -v `pwd`/notebooks:/tmp/notebooks:rw --rm clojure:tools-deps clojure -Sdeps '{:deps {org.pinkgorilla/gorilla-notebook {:mvn/version "0.4.1"}}}' -m pinkgorilla.core
```
You may want to use the two bind mounts to retain your work and to prevent downloading half of the internet.

We also provide uberjar docker images which can be run as follows:
```
docker run --rm -p 9000:9000 pinkgorillawb/gorilla-notebook
```

If you aim at running a docker image built on demand (by ctr.run) from git on demand you can do
```
docker run -p 9000:9000 -v `pwd`/.m2:/root/.m2:rw ctr.run/github.com/pink-gorilla/gorilla-notebook:a-branch-name gorilla-notebook.sh -c /root/.m2/custom.edn
```

You are free to build that image yourself:

```
docker build --rm -t me/gorilla-notebook:builder .
```

If you want some samples to play with, you might want to clone and mount the samples repo
into the container:

```
git clone https://github.com/pink-gorilla/sample-notebooks
docker run --rm -p 9000:9000 -v `pwd`/sample-notebooks/samples:/work/sample-notebooks:rw pinkgorillawb/gorilla-notebook
```
### JAR - DropIn to external web app

Install `npm` dependencies:
```
./script/prepare.sh
```

The following should then get you the uberjar:
```
./script/build-uberjar.sh
```
The uberjar is what the docker image uses. It can be run by executing

```
java -jar target/gorilla-notebook-standalone.jar
```

The uberjar may also work by just dropping it into another webapp (in `WEB-INF/lib`) . Whether you are lucky
 or not depends on the dependencies of your app. If all goes well, Pink Gorilla will appear at
`.../your-app-context/gorilla-repl/worksheet.html`.

```
./script/build-uberwar.sh
```
should give you the standalone war file. Drop it into your servlet container and visit the root url of the webapp.

```
lein with-profile +cljs tailwind-development
```
will build CSS once.

### Compile/Run from source

```
lein build-tailwind-dev
./script/run-repls-with-jpda.sh
```

runs `lein repl`, with JPDA debugging, `rlwrap` for convenience and spins up the server. NREPL should be up at
 port 4001.

## Web Interface

Finally, go to [`http://localhost:9000/worksheet.html`](http://localhost:9000/worksheet.html) for the app

This repository comes with various [test notebooks](notebooks/) to try and the explorer should have some more.


There are various scripts in `./script` and there is also a bunch of aliases in `project.clj` you might want
 to check.

## Configuration

In case you are unlucky, you might want to try
Docker
```
lein do clean, install
```

add a dependency in your project and tweak dependencies until things work. This is
 what I do with [lambdalf](https://github.com/deas/lambdalf). Again, if things go well,
 Pink Gorilla will appear at `.../your-app-context/gorilla-repl/worksheet.html`.

## Tests

[This is how we do it](.github/workflows/ci.yml)


## Contributing

Contribution of pretty much any kind is welcome. Feel free to get in touch. We are on [Clojurians Slack](http://clojurians.net/)
and also on [Clojurians Zulip](https://clojurians.zulipchat.com/#narrow/stream/212578-pink-gorilla-dev).

## History

In 2016, Andreas was working on the [first iteration of Gorilla REPL modernisation](https://www.contentreich.de/pimping-gorilla-repl-with-react-clojurescript-and-beyond). Amongst other
  things, [Reagent](http://reagent-project.github.io/) was introduced at that time. Unfortunately, it went silent again -
  for almost three years. [This issue](https://github.com/pink-gorilla/gorilla-notebook/issues/2) revived the project.

As of October 2019, we are primarily focused on cleaning up the house to build a solid foundation for future work. We'll
try hard to keep master branches stable at all time and we will also publish releases to Clojars. We assume that this fork
is at least as stable as the "original" and transition is painless. We also try to remain backwards compatible and preserve
existing functionality. Given the nature of reagent, this did not seem reasonably
 possible with regards to persisted html in worksheets. We ended up introducing  version 2 persistence (transit based)
  while still supporting version 1 persistence (shamelessly discarding output). Other than that, URLs have slightly changed.
 The viewer is at `.../worksheet.html#/view` now. You may want to try
 [here](http://localhost:9000/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj)
in case you have it running at port 9000.

## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2019- Jony Hudson, Andreas Steffan and contributors
