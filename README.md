# Pink Gorilla Notebook

[Pink Gorilla Notebook] (http://pink-gorilla.org) is a rich browser based notebook REPL for Clojure and ClojureScript,
which aims at extensibility (development- and runtime) and user experience while being very lightweight. 
Extensibility primarily revolves around UI widgets and data. 

Use cases we are trying to cover are
- Persistent experiments and demos (Clojure/ClojureScript libraries!)
- Courses and education on all matters related to clojure
- Data science
- Pluggable "alien" JEE webapp instrumentation

We'll try to borrow from other ecosystems where bridging from Clojure/ClojureScript appears reasonable. Wrapping and
leveraging R is one example that comes to mind.

Try it out at [Online] (http://pink-gorilla.org)

We are a  is a decendant of [Gorilla REPL](http://gorilla-repl.org).

## Usage / Development

The easiest way to run it locally is by leveraging the docker image:
```
docker run --rm -p 9000:9000 pinkgorillawb/gorilla-notebook
```

If you want some samples to play with, you might want to clone and mount the samples repo
into the container:

```
git clone https://github.com/pink-gorilla/sample-notebooks
docker run --rm -p 9000:9000 -v `pwd`/sample-notebooks/samples:/work/sample-notebooks:rw pinkgorillawb/gorilla-notebook
```

If you want to bring your own java, make sure to use jdk 8 for now.

The following should get you the uberjar:
```
LEIN_SNAPSHOTS_IN_RELEASE=1 lein do clean, uberjar
```
The uberjar is what the docker image uses. It can be run by executing

```
java -jar target/gorilla-notebook-standalone.jar
```

The uberjar may also work by just dropping it into another webapp (in `WEB-INF/lib`) . Whether you are lucky
 or not depends on the dependencies of your app. If all goes well, Pink Gorilla will appear at
`.../your-app-context/gorilla-repl/worksheet.html`.

```
lein do clean, ring uberwar
```
should give you the standalone war file. Drop it into your servlet container and visit the root url of the webapp.
 
```
./run-repls-with-jpda.sh
```

runs `lein repl`, with JPDA debugging, `rlwrap` for convenience and spins up the server. NREPL should be up at
 port 4001. Once jacked in, run `(start "dev")` to launch the figwheel server.

Finally, go to
 - [`http://localhost:9000/worksheet.html`](http://localhost:9000/worksheet.html) for the app
 - [`http://localhost:9000/devcards.html`](http://localhost:9000/devcards.html) for devcards
 - [`http://localhost:3449/figwheel-extra-main/auto-testing`](http://localhost:3449/figwheel-extra-main/auto-testing)
  for figwheels built in auto-testing

To compile ClojureScript and run the main entrypoint, execute
```
lein do cljsbuild once, run
```

## Configuration

TODO : Explain delegation mode

In case you are unlucky, you might want to try
Docker
```
lein do clean, install
```

add a dependency in your project and tweak dependencies until things work. This is
 what I do with [lambdalf](https://github.com/deas/lambdalf). Again, if things go well,
 gorilla REPL will appear at `.../your-app-context/gorilla-repl/worksheet.html`.

## Tests

```
lein ci && karma start --single-run
```


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
 [here](http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj)
in case you have it running with figwheel.

## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2019- Jony Hudson, Andreas Steffan and contributors
