(ns ^:figwheel-no-load gorilla-repl.dev
  (:require [gorilla-repl.core :as core]
    ;;        [gorilla-repl.util :refer [ws-origin]]
    ;;        [cemerick.url :refer [url]]
    ;; [figwheel.client :as figwheel :include-macros true]
            [devtools.core :as devtools]
    ;; [dirac.runtime]
            [re-frisk.core :refer [enable-re-frisk!]]))

(devtools/install!)
;; (dirac.runtime/install!)
(enable-re-frisk!)
;; (.log js/console (range 10))
(enable-console-print!)

#_(figwheel/watch-and-reload
    :websocket-url (ws-origin "figwheel-ws" (url (-> js/window .-location .-href)))
    :jsload-callback core/mount-root)

(core/init!)
