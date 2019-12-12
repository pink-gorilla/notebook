(ns pinkgorilla.dev-preload)

(.setItem js/localStorage "day8.re-frame-10x.show-panel" "false")

(enable-console-print!)

; TODO: Dont remove this println; otherwise println will not work in the rest of the application. why is this???
(println "wunderbar!")
