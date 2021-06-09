# Pink Gorilla Notebook [![GitHub Actions status |pink-gorilla/notebook](https://github.com/pink-gorilla/notebook/workflows/CI/badge.svg)](https://github.com/pink-gorilla/notebook/actions?workflow=CI)
[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/notebook.svg)](https://clojars.org/org.pinkgorilla/notebook)
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

## Run Notebook standalone 

The easiest way to run the notebook locally is leveraging the `clojure` cli

```
clojure -Sdeps '{:deps {org.pinkgorilla/notebook-bundel {:mvn/version "RELEASE"}}}' -m pinkgorilla.notebook-bundel
```

## Run Notebook with **default bundel**

Since the default bundel ships many default ui extensions, you want to use the **notebook-bundel** 
artefact, because the javascript frotend app has already been precompiled, which results in faster startup-time.

### in your deps.edn project

We recommend to use tools.deps over leiningen fortwo reasons:
- no dependency coflicts with tools.deps (tools deps resolves to the highest dependency version, vs leiningen which depends on the position in the project.clj
- use RELEASE so you always get the most recent notebook)

One way to configure the notebook is to pass it a edn configuration file. An example is
[notebook edn config](https://github.com/pink-gorilla/notebook/blob/master/resources/notebook-core.edn)

In your deps.edn add this alias:
```
:notebook {:extra-deps {org.pinkgorilla/notebook-bundel {:mvn/version "RELEASE"}}
           :exec-fn pinkgorilla.notebook-bundel/run
           :exec-args {:config "notebook-config.edn"}}
```
then run it with `clojure -X:notebook`.

[trateg](https://github.com/clojure-quant/trateg) uses notebook-bundel with deps.edn:
Clone trateg and run `clojure -X:notebook`

### in your leiningen project

** We don't recommend leiningen use with notebook, as leiningen does not use the highest version of  dependencies.  ** 

## Run Notebook with **custom bundel**

If you define your own ui extensions, you need to compile the javascript bundel.
This requires some extra initial compilation time.

### in your deps.edn project

[ui-quil](https://github.com/pink-gorilla/ui-quil) use deps.edn to build a custom notebook 
bundel (that includes the library that gets built).

### in your leiningen project

[gorilla-ui](https://github.com/pink-gorilla/gorilla-ui) and
[ui-vega](https://github.com/pink-gorilla/ui-vega) use leiningen to run notebooks with a 
custom build bundel, and with custom notebook folder.


## Run Notebook in Docker Image

Documentation has been moved [over here](https://pink-gorilla.github.io/)

<!-- [![dockeri.co](https://dockeri.co/image/pinkgorillawb/gorilla-notebook)](https://hub.docker.com/r/pinkgorillawb/gorilla-notebook) -->
[![](https://images.microbadger.com/badges/version/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own version badge on microbadger.com")
[![](https://images.microbadger.com/badges/image/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own image badge on microbadger.com")

## Run Notebook from cloned git repo

This option is mainly there for development of notebook. 
For regular use, the long compile-times are not really sensible.

Run `clojure -X:notebook` to run the notebook. 

This runs the notebook with ui libraries bundled:
- gorilla ui
- gorilla plot

## Run Development UI 

Run `clojure -X:develop` to run the develop ui. 

## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2019- Jony Hudson, Andreas Steffan and contributors
