(ns pinkgorilla.route
  (:use compojure.core)
  (:require
   [clojure.tools.logging :refer (info)]
   [clojure.java.io :as io]
   [compojure.route :as route]
   [compojure.core :as compojure]
   [ring.middleware.session :refer [wrap-session]]
   [pinkgorilla.middleware.cider :as cider]
     ;; [gorilla-middleware.cljs :as cljs]
   [pinkgorilla.jetty9-ws-relay :as ws-relay]
   [pinkgorilla.ui.hiccup_renderer :as renderer]   ; this is needed to bring the render implementations into scope
   [pinkgorilla.handle :as handle]
   [pinkgorilla.storage.storage-handler :refer [save-notebook load-notebook]]
   [pinkgorilla.storage.explore-handler :refer [gorilla-files]]))

;; TODO Somebody clean up the routes!
(defn create-api-handlers
  [prefix]
  (info "creating api handlers wih prefix: " prefix)
  [(GET (str prefix "load") [] ((comp handle/wrap-cors-handler handle/wrap-api-handler) load-notebook))
   (POST (str prefix "save") [] (handle/wrap-api-handler save-notebook))
   (GET (str prefix "gorilla-files") [] (handle/wrap-api-handler gorilla-files))
   (GET (str prefix "config") [] (handle/wrap-api-handler handle/config))])

#_(defn create-repl-handlers
    [prefix receive-fn]
    [(GET (str prefix "repl") [] (ws-relay/jetty-repl-ring-handler receive-fn))])


(defn document-utf8
  [filename req]
  {:status  200
   ;; utf-8 needed HERE, content sets ISO-8859-1 default which
   ;; supercedes meta header in document
   ;; Session key is required to force setting the cookie
   :session (:session req)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (slurp (io/resource
                    (str "gorilla-repl-client/" filename)))})


(defn create-resource-handlers
  [prefix]
  [(route/resources prefix)                                 ;; Needed during development
   ;; ^{:name "res2"} (route/resources "/" {:root "public"}) ; Yuck, seems to be required for uberwar
   ; (wrap-webjars handler "/webjars") ;; TODO Fix this
   ;; ^{:name ":res2"} (route/resources "/" {:root "META-INF/resources/"}) ;; webjars servlet 3 style
   (GET (str prefix ":document.html") [document]
     ;; Beware! Wrap session appears to be in place already!
     #_(wrap-session
        (partial document-utf8 (str document ".html"))
        {:cookie-name  "gorilla-session"
         :cookie-attrs {:max-age 3600}})
     (partial document-utf8 (str document ".html")))
   (route/resources prefix {:root "gorilla-repl-client"})
   (route/files (str prefix "project-files") {:root "."})
   (route/not-found "Bummer, not found")])

(def nrepl-handler (atom cider/cider-handler #_cljs/cljs-handler))
(def default-api-handlers (create-api-handlers "/"))
#_(def default-repl-handlers (create-repl-handlers "/" (partial ws-relay/on-receive-mem nrepl-handler)))
#_(def remote-repl-handlers (create-repl-handlers "/" ws-relay/on-receive-net))
(def default-resource-handlers (create-resource-handlers "/"))
;; Only wrap session once - Figwheel does that already, so this handler should not be used with Figwheel
(def default-handler (wrap-session
                      (apply compojure/routes (concat default-api-handlers
                                                       ;; default-repl-handlers
                                                      default-resource-handlers))))
(def remote-repl-handler (apply compojure/routes (concat default-api-handlers
                                                         ;; remote-repl-handlers
                                                         default-resource-handlers)))
;; Used by uberwar
(def redirect-handler (GET "/" [] handle/redirect-app))

(defn war-handler [prefix]
  (apply compojure/routes
         (concat (create-api-handlers (str prefix gorilla_repl.GorillaReplListener/PREFIX))
                 (create-resource-handlers (str prefix gorilla_repl.GorillaReplListener/PREFIX)))))

