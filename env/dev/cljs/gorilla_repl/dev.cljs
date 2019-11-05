(ns ^:figwheel-no-load gorilla-repl.dev
  (:require 
     [devtools.core :as devtools]
     [re-frisk.core :refer [enable-re-frisk!]]
    ;;        [cemerick.url :refer [url]]
    ;; [figwheel.client :as figwheel :include-macros true]
    ;; [dirac.runtime]
     [pinkgorilla.core :as core]
    ;;[pinkgorilla.util :refer [ws-origin]]
            ))

(devtools/install!)
;; (dirac.runtime/install!)
(enable-re-frisk!)
;; (.log js/console (range 10))
(enable-console-print!)

#_(figwheel/watch-and-reload
    :websocket-url (ws-origin "figwheel-ws" (url (-> js/window .-location .-href)))
    :jsload-callback core/mount-root)

(core/init!)
