(ns pinkgorilla.notebook-ui.app.app
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [taoensso.timbre :refer [info warn]]
   ; webly
   [webly.config :refer [load-config! get-in-config add-config]]
   [webly.profile :refer [compile? server?]]
   [webly.user.app.app :refer [webly-run!]]
   ; notebook
   [pinkgorilla.notebook-ui.app.services :refer [run-notebook-services!]]
   ; goldly
   [goldly.app :refer [goldly-compile! goldly-run!]]
   ; bundel
   [pinkgorilla.notebook-ui.app.routes])
  (:gen-class))

(defn run-goldly! []
  (let [goldly-enabled (get-in-config [:goldly :enabled])]
    (if goldly-enabled
      (goldly-run!)
      (warn "goldly is disabed!"))))

(defn print-git-version []
  (if-let [r (io/resource "notebook_version.edn")]
    (let [data (-> (slurp r) (edn/read-string))]
      (info "notebook " (:version data) "generated: " (:generated-at data)))
    (warn "notebook version unknown!")))
(defn notebook-run!
  [{:keys [config profile] ; a map so it can be consumed by tools deps -X
    :or {profile "jetty"
         config {}}}]
  (print-git-version)
  (let [config (add-config "notebook-core.edn" config)]
    (load-config! config)
    (when (compile? profile)
      (goldly-compile!))
    (if (server? profile)
      (do (run-goldly!)
          (run-notebook-services!))
      (warn "no server mode. not running explorer/goldly/nrepl"))
    (webly-run! profile config)))

(defn -main ; for lein alias
  ([]
   (notebook-run! {}))
  ([profile]
   (notebook-run! {:profile profile}))
  ([config profile]
   (notebook-run! {:profile profile
                   :config config})))