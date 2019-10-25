(ns gorilla-repl.util
  (:require [clojure.string :as str]))

(defn ws-origin
  [path app-url]
  (let [proto (if (= (:protocol app-url) "http") "ws" "wss")
        port-postfix  (let [port (:port app-url)]
                        (if (< 0 port)
                          (str ":" port)
                          ""))]
                       (str proto ":" (:host app-url) port-postfix (str/replace (:path app-url) #"[^/]+$" path))))
