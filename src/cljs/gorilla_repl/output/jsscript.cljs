(ns gorilla-repl.output.jsscript
  (:require
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [cljs-uuid-utils.core :as uuid]
   ))


; this is the container of loaded scripts
(def jsscript-container (reagent/atom {}))
(def jsscript-root (.getElementById js/document "jsscript-root"))
  

;jscalc
  
(defn inject-script 
  "inject a javascript snippet to the dom
   Does not keep track of which scripts were loaded or not."
  [javascript-snippet]
  (let [script (.createElement js/document "script")]
    (.setAttribute script "type" "text/javascript")
    ;(.setAttribute script "src" "helper.js")
    (set! (.-innerHTML script) javascript-snippet)
    (.appendChild jsscript-root script)
    ))
  

(defn execute-render [id data module-js]
  (let [;_ (println "executing script!")
        module (js->clj module-js :keywordize-keys true)
        data-js (clj->js data)
        ;_ (println "module:" module)
        render-fn (:render module)]
    ;(println render-fn)
    (render-fn id data-js)
    ;(println "rendering: " (render-fn id data))
    ))

;; This works:
;; (.require js/window (clj->js ["demo"]) #(println "result: " %))
;; (run-script "define([],function(){return 'world!'})")
;; (run-script "andreas" "define([],function(){return {render: function (name) {return 'hello, ' + name}}})")


(defn run-script [id data javascript-snippet]
  (let [module (str "loadstring!" javascript-snippet)
        ;module "demo"
        modules-js (clj->js [module])
        ;_ (println "js module source: " modules-js)
        ]
    (.require js/window modules-js (partial execute-render id data))
    ;x (js/require modules-js #(println "rcvd2: " %))    ; it should also work this way.
    nil ; suppress returning the requirejs module definition
    ))
  
  


(defn output-jsscript
  [output _]
  (let [uuid (uuid/uuid-string (uuid/make-random-uuid))
        div-uuid (keyword (str "div#" uuid))
        content (:content output)
        ;_ (println "content: " content)
        snippet (:module content)
        data (:data content)
        ]
        (reagent/create-class 
         {:display-name "output-jsscript"
          :reagent-render (fn []
                            [div-uuid
                             ;[:p "js-script"]
                             
                             ]
                            )
          :component-did-mount (fn [this]
            (run-script uuid data snippet))
          })))


 