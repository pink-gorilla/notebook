(ns pinkgorilla.notebook-ui.codemirror.extension.eval
  (:require
   [taoensso.timbre :refer-macros [info]]
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [cljs.reader]
   [cljs.tools.reader]))

(defn is-valid-cljs? [source]
  (try
    (do
      (cljs.tools.reader/read-string source)
      true)
    (catch js/Error _
      false)))

(defn should-eval [cm evt source]
  (let [lines (.lineCount cm)
        in-place (or (= 1 lines)
                     (let [pos (.getCursor cm)
                           last-line (dec lines)]
                       (and
                        (= last-line (.-line pos))
                        (= (.-ch pos)
                           (count (.getLine cm last-line))))))]
    (and in-place
         (is-valid-cljs? source))))





