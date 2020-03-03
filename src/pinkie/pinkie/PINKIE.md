PINKIE

the pinkie and the brain  [User=Brain Pinkie=visualisation worker]

You work in your preferred ide, and can visualize output from the repl to a webbrowser.
All renderers written for pink-gorilla notebook are available.


## Render Plugin to your project
- Add pinkgorilla-notebook to your project.clj
- [pinkie.app]

## Dev mode

```
1.
lein build-tailwind-dev
lein build-shadow-pinkie

2.
lein run-pinkie
or
in vs code -> Jack in-> profile pinkie
in src/pinkie/pinkie/app.clj
    a) load all deps
    b) evaluate the expressions in the comment in the bottom

```

## Todo
- embed cljs bundle in jar file 
- custom renderer implementation:
  that is based on different package.json / clj deps.
  uses shadow-cljs to build it.
- can we send the code back to the IDE ??
  So the IDE could change the expression that was evaluated.
  does this make sense? 
- add notebook explorer to support multiple namespaces
- markdown cells are missing, deleting cells missing
- show namespace