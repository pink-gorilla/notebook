(ns pinkie.app
  (:require
   [clojure.java.io :as io]
   [pinkgorilla.ui.gorilla-renderable :refer [render-renderable-meta]]
   [pinkgorilla.ui.hiccup_renderer] ; bring default clj renderers into scope
   [pinkie.server :refer [start-server! start-heartbeats! send-all!] ])
  (:gen-class))

(defn start! [& port] 
  (if port
    (start-server! (Integer/parseInt port))
    (start-server!))
  (start-heartbeats!))

(def stop! pinkie.server/stop!)

(defn -main [& [port]]
  (println "pinkie-app/-main ..")
  (if port
    (start! (Integer/parseInt port))
    (start!)
    ))

(defn code-add
  "sends to browser instruction to set the code for an evaluation"
  [id code]
  (let [id-as-keyword (if (keyword? id) id (keyword (str id)))]
    (send-all!
     [:pinkie/code-add {:id id-as-keyword :code code}])))

(defn result-set
  "sends to browser instruction to set the result of an evaluation"
  [id result]
  (let [id-as-keyword (if (keyword? id) id (keyword (str id)))
        result-rendered (render-renderable-meta result)]
    (send-all!
     [:pinkie/result-set {:id id-as-keyword :result result-rendered}])))


(comment
  (start-server!)
  (start-heartbeats!)
  (println "clients: " @pinkie.server/connected-uids)
  (stop!)

  (.listFiles (io/file "./target"))

  (send-all! [:pinkie/broadcast {:a 13}])

  (code-add 1 "(- 100 5)")
  (result-set 1 95)

  (code-add 2 "[1 2 {:a 1 :b 2 :c 3}]")
  (result-set 2 [1 2 {:a 1 :b 2 :c 3}])

  (def data
    {:$schema "https://vega.github.io/schema/vega-lite/v4.json"
     :description "A simple bar chart with embedded data."
     :data {:values [{:a "A" :b 28} {:a "B" :b 55} {:a "C" :b 43} {:a "D" :b 91} {:a "E" :b 81} {:a "F" :b 53}
                     {:a "G" :b 19} {:a "H" :b 87} {:a "I" :b 52} {:a "J" :b 127}]}
     :mark "bar"
     :encoding {:x {:field "a" :type "ordinal"}
                :y {:field "b" :type "quantitative"}}})

  (code-add 3 (pr-str ^:R [:p/vega data]))
  (result-set 3 ^:R [:p/vega data])

  (code-add 4 (pr-str ^:R  [:p/player "https://www.youtube.com/watch?v=Bs44qdAX5yo"]))
  (result-set 4 ^:R [:p/player "https://www.youtube.com/watch?v=Bs44qdAX5yo"])

  ; comment end
  )

