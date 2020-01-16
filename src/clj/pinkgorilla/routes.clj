(ns pinkgorilla.routes)

;; awb99: route helper with google analytics 

#_(defmacro defroute-ga
    [route params & body]
    `(secretary.core/defroute ~route ~params
       (if (= "open-source.braveclojure.com" (aget js/window "location" "host"))
         (js/ga "send" "pageview" (aget js/window "location" "pathname")))
       ~@body))

#_(defmacro defroute-nga
    [name route params & body]
    `(secretary.core/defroute ~name ~route ~params
       (if (= "clojurework.com" (aget js/window "location" "host"))
         (js/ga "send" "pageview" (aget js/window "location" "pathname")))
       ~@body))