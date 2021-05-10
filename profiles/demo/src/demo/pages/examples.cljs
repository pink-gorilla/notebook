(ns demo.pages.examples
  (:require
   [re-frame.core :refer [dispatch]]
   
   [webly.web.handler :refer [reagent-page]]
   [demo.views.example :as example]
   ; examples
   [demo.views.notebook :refer [codemirror-demo]]
   [demo.views.completion :refer [completion-demo]]
   [demo.views.stacktrace :refer [stacktrace-demo]]
   [demo.views.datafy :refer [datafy-demo]]
   [demo.views.welcome :refer [welcome]]
   ))

(example/add "welcome" welcome)
(example/add "completion" completion-demo)
(example/add "stacktrace" stacktrace-demo)
(example/add "datafy" datafy-demo)
(example/add "notebook" codemirror-demo)

#_(def examples (r/atom
                 [{:name "a" :component [:h1 "a"]}
                  {:name "b" :component [:h1 "b"]}]))


(defmethod reagent-page :notebook/about [{:keys [route-params query-params handler] :as route}]
  [example/examples-component "welcome"])