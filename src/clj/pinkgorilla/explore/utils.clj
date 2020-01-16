(ns pinkgorilla.explore.utils)

(defmacro tv
  [event-name]
  `(.. ~event-name -target -value))

(defn now
  []
  (java.util.Date.))