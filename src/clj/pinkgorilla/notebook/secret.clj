(ns pinkgorilla.notebook.secret
  "helper functions to set secrets from frontend"
  (:require
   [clojure.tools.logging :refer (info)]
   [pinkgorilla.notebook.repl :refer [secrets-atom]]))

(defn set-secrets! [secrets-map]
  (info "secrets have been received from client.")
  (reset! secrets-atom secrets-map))







