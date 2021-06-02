# Pink Gorilla Notebook [![GitHub Actions status |pink-gorilla/notebook](https://github.com/pink-gorilla/notebook/workflows/CI/badge.svg)](https://github.com/pink-gorilla/notebook/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/notebook.svg)](https://clojars.org/org.pinkgorilla/notebook)

[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/notebook-bundel.svg)](https://clojars.org/org.pinkgorilla/notebook-bundel)

Pink Gorilla Notebook is a rich browser based notebook REPL for Clojure and ClojureScript, which aims at extensibility
 (development- and runtime) and user experience while being very lightweight. Extensibility primarily revolves around
  UI vizulisations and data.

### Use cases
- Data science
- Persistent experiments and demos (Clojure/ClojureScript libraries)
- Courses and education on all matters related to clojure

### Web Interface

Whichever method you use to start the notebook, you should reach it at [`http://localhost:8000/`](http://localhost:8000/).

## Run - standalone 

The easiest way to run releases locally is leveraging the `clojure` cli

```
clojure -Sdeps '{:deps {org.pinkgorilla/notebook-bundel {:mvn/version "RELEASE"}}}' -m pinkgorilla.notebook-bundel
```

## Run - cloned git repo

Run `clojure -X:notebook` to run the notebook. 
This will run a web server at port 8000 with showcase of some notebook components, 
and notebook-explorer and notebook-viewer.

Thos runs the notebook with ui libraries bundled:
- gorilla ui
- gorilla plot
- goldly

## Run - in your deps.edn project

One way to configure the notebook is to pass it a edn configuration file. An example is
[test config](https://github.com/pink-gorilla/notebook/blob/master/resources/notebook-bundel.edn)

In your deps.edn add this alias:
```
:notebook {:extra-deps {org.pinkgorilla/notebook-bundel {:mvn/version "0.5.5"}}
           :exec-fn pinkgorilla.notebook-bundel/run!
           :exec-args {:config "notebook-config.edn"}}
```
then run it with `clojure -M:notebook`.


## Docker Image

Documentation has been moved [over here](https://pink-gorilla.github.io/)

<!-- [![dockeri.co](https://dockeri.co/image/pinkgorillawb/gorilla-notebook)](https://hub.docker.com/r/pinkgorillawb/gorilla-notebook) -->
[![](https://images.microbadger.com/badges/version/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own version badge on microbadger.com")
[![](https://images.microbadger.com/badges/image/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own image badge on microbadger.com")


## Development UI 

Run `clojure -X:develop` to run the develop ui. 



## Use an external nrepl relay

If you want to eval clj code, then to run a nrepl websocket relay:
- run `lein relay-jetty` (in nrepl-middleware or 
- run `lein relay` lein-pinkgorilla) 


## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2019- Jony Hudson, Andreas Steffan and contributors
