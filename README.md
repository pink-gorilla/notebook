# Pink Gorilla Notebook


Pink Gorilla Notebook is a decendant of [Gorilla REPL](http://gorilla-repl.org). It is a rich browser based notebook
 style REPL for Clojure and ClojureScript which aims at extensibility (development- and runtime) and user experience
  while being very lightweight. Extensibility primarily revolves around UI widgets and data. As of October 2019, it already
   enhances its parent project in various ways.

Use cases we are trying to cover are

- Persistent experiments and demos (Clojure/ClojureScript libraries!)
- Courses and education on all matters related to clojure
- Data science
- Pluggable "alien" JEE webapp instrumentation

We'll try to borrow from other ecosystems where bridging from Clojure/ClojureScript appears reasonable. Wrapping and
leveraging R is one example that comes to mind.

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

## Development

```
./run-figwheel-with-jpda.sh
```
spins up the figwheel based development environment. The script uses `rlwrap` for convenience.

Alternatively, you can simply run

```
lein run -m clojure.main script/figwheel.clj
```

Once things runnig, you can jump in at
 either `http://localhost:3449/worksheet.html` for the app or `http://localhost:3449/devcards.html`
if you want to play with devcards.

```
lein do cljsbuild once, run
```
compile cljs and run main entrypoint.

```
lein do clean, ring uberwar
```

should give you the standalone war file. Drop it into your servlet container
 and visit the root url of the webapp.

```
LEIN_SNAPSHOTS_IN_RELEASE=1 lein do clean, uberjar
```
should give you the all-in uberjar. It is used by the docker image and can be run
executing `java -jar target/gorilla-workbook-standalone.jar`. It may also work dropped
 into a webapp (in `WEB-INF/lib`). Whether you are lucky or depends on the dependencies
(if you run into conflicts). If all goes well, Pink Gorilla will appear at
`.../your-app-context/gorilla-repl/worksheet.html`.

TODO : Explain delegation mode

In case you are unlucky, you might want to try
Docker
```
lein do clean, install
```

add a dependency in your project and tweak dependencies until things work. This is
 what I do with [lambdalf](https://github.com/deas/lambdalf). Again, if things go well,
 gorilla REPL will appear at `.../your-app-context/gorilla-repl/worksheet.html`.

## Contributing

Contribution of pretty much any kind is welcome. Feel free to get in touch. We are on [Clojurians Slack](http://clojurians.net/)
and also on [Clojurians Zulip](https://clojurians.zulipchat.com/#narrow/stream/212578-pink-gorilla-dev).

## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2019- Jony Hudson, Andreas Steffan and contributors
