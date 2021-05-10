(ns pinkgorilla.notebook-ui.app.app
  (:require
   [taoensso.timbre :refer [info warn]]
   ; webly
   [webly.config :refer [get-in-config]]
   [webly.web.handler :refer [add-ring-handler]]
   ; explorer
   [pinkgorilla.explore.handler :refer [explore-directories-start]]
   [pinkgorilla.explorer.default-config] ; side-effects
   [pinkgorilla.explorer.handler] ; side-effects
   ; nrepl relay
   [picasso.default-config]
   [pinkgorilla.nrepl.relay.jetty :as relay]
   [pinkgorilla.nrepl.server.nrepl-server :refer [run-nrepl-server]]
   ; notebook app
   [pinkgorilla.notebook-ui.app.keybindings] ; referred to in config.edn
   [pinkgorilla.notebook-ui.app.routes]))

(defn nrepl-relay-start []
   ; relay: see resources/config.edn :jetty-ws
  (let [{:keys [server relay enabled]} (get-in-config [:nrepl])
        nrepl-ws-handler (relay/ws-handler relay)]
    (if enabled
      (do
        (run-nrepl-server server)
        (add-ring-handler :ws/nrepl nrepl-ws-handler))
      (warn "nrepl-relay is disabed!"))))

(defn explorer-start []
  (let [config-explorer-server (get-in-config [:explorer :server])]
    (explore-directories-start config-explorer-server)))

(defn run-notebook-services! []
  (nrepl-relay-start)
  (explorer-start))
