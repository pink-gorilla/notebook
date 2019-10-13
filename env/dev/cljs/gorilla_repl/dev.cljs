(ns ^:figwheel-no-load gorilla-repl.dev
  (:require 
   [gorilla-repl.core :as core]
   [figwheel.client :as figwheel :include-macros true]
   [devtools.core :as devtools]
   ;; [dirac.runtime]
   [re-frisk.core :refer [enable-re-frisk!]]))

(devtools/install!)
;; (dirac.runtime/install!)
(enable-re-frisk!)
;; (.log js/console (range 10))
(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback core/mount-root)

(core/init!)
