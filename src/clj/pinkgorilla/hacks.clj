(ns pinkgorilla.hacks
  "hacks that will be removed sooner or later"
  (:require
   [clojure.pprint]
   [clojure.core]
   [clojure.repl]
   [pinkgorilla.ui.text :refer [text]]
   [pinkgorilla.ui.hiccup :refer [html!]]))



(defn print-table 
  ([ks list]
   (-> (with-out-str (clojure.pprint/print-table ks list))
       (text)
       (html!)))
  ([list]
   (-> (with-out-str (clojure.pprint/print-table list))
       (text)
       (html!))))



(defn nil-to-empty-string [s]
  (if (nil? s) "" s))

(defn doc [s]
  (->
   (with-out-str (clojure.repl/doc s))
   (nil-to-empty-string)
   (text)
   (html!)))

(comment
  (.render (print-table [{:planet "pluto"} {:planet "earth"} {:planet "saturn"}]))
  
  (.render (doc clojure.pprint/print-table))
  
  
  (def s 'clojure.pprint/print-table)
  `s
  
  (clojure.repl/doc `(symbol "clojure.pprint/print-table"))
  
  (clojure.repl/doc clojure.pprint/print-table)
  
  )

