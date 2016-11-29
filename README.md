# Gorilla REPL

Gorilla is a rich REPL for Clojure in the notebook style. If you're interested
 you should take a look at its [website](http://gorilla-repl.org).

This fork started out as a React migration experiment. Meanwhile it introduces
various significant changes. The overall goal is to expand use cases while keeping
development fun. I tried really hard to remain backwards compatible and preserve
existing functionality. Given the nature of reagent, this did not seem reasonably
 possible with regards to persisted html in worksheets. I ended up introducing
 version 2 persistence (transit based) while still supporting version 1 persistence
(shamelessly discarding output).
 
Even though I did not test all the things throughoutly, I consider "mostly working":
 
- Client code pretty much 100% ClojureScript (reagent/re-frame based)
- Recent versions of most (Clojure/ClojureScript/JavaScript) dependencies
- Parinfer  (optional)
- Optional separate, dedicated nREPL middleware stack
- Figwheel/Devcards/Testing (latter bare bones)
- Componentized (incl. tesla-microservice)
- Standalone uberjar / docker image
- Standalone uberwar
- Drop in uberjar capabilities (dropping the uberjar into some WEB-INF/lib should work ... sometimes)
- Roll in jar (use as a library)
- Proxy based loading of remote worksheets (in case CORS is not in place)

WIP/in experimental stage:

- Remote nREPL delegation
- Client side repl (replumb)
- Heroku
- Clojail execution
- Mobile/PWA capabilities (i.e. upup/service workers)


Ideas/On the radar:

- Tests 😁
- Interruption
- node.js backend
- Improve composabitly of (ring) handlers
- Code sharing UI/services
- Segment dependencies
- Revisit (Clo)Jupyter
- Periodically save to localStorage

The URLs have slightly changed. The viewer is at `.../worksheet.html#/view` now. You may want to try
 [here](http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj)
 in case you have it running with figwheel.

## Development

```
./run-figwheel-with-jpda.sh
```
spins up the figwheel based development environment. Once things runnig, you can jump in at
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
lein do clean, uberjar
```
should give you the all-in uberjar. It is used by the docker image and can be run
executing `java -jar target/gorilla-repl-ng-standalone.jar`. It may also work dropped
 into a webapp (in `WEB-INF/lib`). Whether you are lucky or depends on the dependencies
(if you run into conflicts). If all goes well, Gorilla REPL will appear at
`.../your-app-context/gorilla-repl/worksheet.html`.

TODO : Explain delegation mode

In case you are unlucky, you might want to try

```
lein do clean, install
```

add a dependency in your project and tweak dependencies until things work. This is
 what I do with [lambdalf](https://github.com/deas/lambdalf). Again, if things go well,
 gorilla REPL will appear at `.../your-app-context/gorilla-repl/worksheet.html`.
## Contributing

Contributions, in the form of comments, criticism, bug reports, or code are all very welcome :-) If you've got an idea
for a big change drop me an email so we can coordinate work.

## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2014- Jony Hudson and contributors

## Development
