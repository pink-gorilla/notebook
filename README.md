# Gorilla REPL

(defn bongo []
  (html! [:div
          [:p "bongo"]
          (hiccup.page/include-js "https://code.highcharts.com/highcharts.js")          
          ]))
 



Gorilla is a rich REPL for Clojure in the notebook style. If you're interested
 you should take a look at its [website](http://gorilla-repl.org).

This fork started out as a React migration experiment. Meanwhile it introduces
a lot of significant changes. The overall goal is to expand use cases while keeping
development fun.

There is an introductionary [blogpost](https://www.contentreich.de/pimping-gorilla-repl-with-react-clojurescript-and-beyond).
 
I tried really hard to remain backwards compatible and preserve
existing functionality. Given the nature of reagent, this did not seem reasonably
 possible with regards to persisted html in worksheets. I ended up introducing
 version 2 persistence (transit based) while still supporting version 1 persistence
(shamelessly discarding output). Other than that, the URLs have slightly changed.
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
executing `java -jar target/gorilla-repl-ng-standalone.jar`. It may also work dropped
 into a webapp (in `WEB-INF/lib`). Whether you are lucky or depends on the dependencies
(if you run into conflicts). If all goes well, Gorilla REPL will appear at
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

Contributions, in the form of comments, criticism, bug reports, or code are all very welcome :-) If you've got an idea
for a big change drop me an email so we can coordinate work.

## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2016- Jony Hudson, Andreas Steffan and contributors

## Development
