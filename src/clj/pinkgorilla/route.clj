(ns pinkgorilla.route
  (:require
   [taoensso.timbre :refer [info]]
   ;[clojure.java.io :as io] ; used in old implementation of document-utf8
   [compojure.route :as compojure-route]
   [compojure.core :as compojure :refer [GET POST]]
   [selmer.parser :as sel]
   [ring.middleware.session :refer [wrap-session]]
   ;;PinkGorilla Libraries
   [pinkgorilla.middleware.cider :as mw-cider]
   [pinkgorilla.ui.hiccup_renderer :as renderer]   ; this is needed to bring the render implementations into scope
   ;;Pinkorilla Notebook   
   [pinkgorilla.notebook-app.handle :refer [wrap-api-handler wrap-cors-handler redirect-app config get-war-prefix]]
   [pinkgorilla.storage.storage-handler :refer [save-notebook load-notebook]]
   [pinkgorilla.explore.explore-handler :refer [gorilla-files req-explore-directories-async]]))

;; TODO Somebody clean up the routes!
(defn create-api-handlers
  [prefix]
  (info "Creating api handlers with prefix: " prefix)
  [(GET (str prefix "load") [] ((comp wrap-cors-handler wrap-api-handler) load-notebook))
   (POST (str prefix "save") [] (wrap-api-handler save-notebook))
   (GET (str prefix "gorilla-files") [] (wrap-api-handler gorilla-files))
   (GET (str prefix "explore") [] (wrap-api-handler req-explore-directories-async))
   (GET (str prefix "config") [] (wrap-api-handler config))])

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
   :body    (sel/render-file (str "gorilla-repl-client/" filename)
                             {:name "Pink Gorilla"})
   #_(slurp (io/resource (str "gorilla-repl-client/" filename)))})

(defn create-resource-handlers
  [prefix]
  [(compojure-route/resources prefix)                                 ;; Needed during development
   ;; ^{:name "res2"} (compojure-route/resources "/" {:root "public"}) ; Yuck, seems to be required for uberwar
   ; (wrap-webjars handler "/webjars") ;; TODO Fix this
   ;; ^{:name ":res2"} (compojure-route/resources "/" {:root "META-INF/resources/"}) ;; webjars servlet 3 style
   (GET (str prefix ":document.html") [document]
     ;; Beware! Wrap session appears to be in place already!
     #_(wrap-session
        (partial document-utf8 (str document ".html"))
        {:cookie-name  "gorilla-session"
         :cookie-attrs {:max-age 3600}})
     (partial document-utf8 (str document ".html")))
   (compojure-route/resources prefix {:root "gorilla-repl-client"})
   (compojure-route/files (str prefix "project-files") {:root "."})
   (compojure-route/not-found "Bummer, not found")])

(def nrepl-handler (atom (mw-cider/cider-handler) #_(cljs/cljs-handler)))
(def default-api-handlers (create-api-handlers "/"))
#_(def default-repl-handlers (create-repl-handlers "/" (partial ws-relay/on-receive-mem nrepl-handler)))
#_(def remote-repl-handlers (create-repl-handlers "/" ws-relay/on-receive-net))
(def default-resource-handlers (create-resource-handlers "/"))


;; Only wrap session once - Figwheel (no longer used) did that already, so this handler 
;; should not be used with figwheel


(def default-handler (wrap-session
                      (apply compojure/routes (concat default-api-handlers
                                                       ;; default-repl-handlers
                                                      default-resource-handlers))))
;; TODO: Implement this - but beware that (web) resources from extension jars don't work with a remote repl
(def remote-repl-handler (apply compojure/routes (concat default-api-handlers
                                                         ;; remote-repl-handlers
                                                         default-resource-handlers)))
;; Used by uberwar (Not sure whether it works as of Jan 2020)
(def redirect-handler (GET "/" [] redirect-app))

(defn war-handler [prefix]
  (apply compojure/routes
         (concat (create-api-handlers (str prefix (get-war-prefix)))
                 (create-resource-handlers (str prefix (get-war-prefix))))))

