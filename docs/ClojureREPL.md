# Architecture of the Clojure REPL


## gorilla middleware
https://github.com/pink-gorilla/gorilla-middleware
[org.pinkgorilla/gorilla-middleware "0.2.1"]
- uses  [nrepl "0.6.0"] and [cider/cider-nrepl "0.22.4"]
- NRepl defines the nrepl messenging protocol
- cider extends operations to the nrepl protocol (example: interruptable eval)
https://github.com/pink-gorilla/gorilla-notebook/blob/master/src/clj/gorilla_repl/nrepl.clj

## gorilla notebook
- runs the repl server in https://github.com/pink-gorilla/gorilla-notebook/blob/master/src/clj/gorilla_repl/nrepl.clj
- the reagent app consumes a websocket that runs the nrepl messenging protocol
