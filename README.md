# Pink Gorilla Notebook [![GitHub Actions status |pink-gorilla/notebook](https://github.com/pink-gorilla/notebook/workflows/CI/badge.svg)](https://github.com/pink-gorilla/notebook/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/notebook.svg)](https://clojars.org/org.pinkgorilla/notebook)

Pink Gorilla Notebook is a rich browser based notebook REPL for Clojure and ClojureScript, which aims at extensibility
 (development- and runtime) and user experience while being very lightweight. Extensibility primarily revolves around
  UI widgets and data.

Documentation has been moved [over here](https://pink-gorilla.github.io/)

[Notebook Bundel](https://github.com/pink-gorilla/notebook-bundel)

## Notebook (Bundel version)

The notebook-bundel contains:
- gorilla ui
- gorilla plot
- goldly

Run `lein notebook watch` to run the demo app. 

This will run a web server at port 8000 with showcase of some notebook components, and notebook-explorer and notebook-viewer.

You can also run:

```
lein notebook npm-install
lein notebook release
lein notebook
```

## Demo (for development)

Run `lein demo` to run the demo app. 

This is useful for development.


## Use an external nrepl relay

If you want to eval clj code, then to run a nrepl websocket relay:
- run `lein relay-jetty` (in nrepl-middleware or 
- run `lein relay` lein-pinkgorilla) 



## Docker Image
<!-- [![dockeri.co](https://dockeri.co/image/pinkgorillawb/gorilla-notebook)](https://hub.docker.com/r/pinkgorillawb/gorilla-notebook) -->
[![](https://images.microbadger.com/badges/version/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own version badge on microbadger.com")
[![](https://images.microbadger.com/badges/image/pinkgorillawb/gorilla-notebook.svg)](https://microbadger.com/images/pinkgorillawb/gorilla-notebook "Get your own image badge on microbadger.com")


## Licence

Gorilla is licensed to you under the MIT licence. See LICENCE.txt for details.

Copyright © 2019- Jony Hudson, Andreas Steffan and contributors
