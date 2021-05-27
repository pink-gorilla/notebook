(ns pinkgorilla.notebook-ui.nrepl.kernel
  (:require
   [clojure.core.async :refer [<! >! chan close! go]]
   [taoensso.timbre :as timbre :refer [debugf info error]]
   [re-frame.core :as rf]
   [picasso.id :refer [guuid]]
   [picasso.kernel.protocol :refer [kernel-eval]]
   [pinkgorilla.nrepl.client.core :refer [send-request! op-eval]]
   [pinkgorilla.notebook-ui.nrepl.subscriptions] ; side effects
   ))

(def nrepl (rf/subscribe [:nrepl/status]))



(defmethod kernel-eval :clj [{:keys [id code]
                              :or {id (guuid)}}]
  (let [c (chan)]
    (info "clj-eval: " code)
    (go (try (let [_ (info "nrepl: " @nrepl)
                   conn (:conn @nrepl)
                   eval-result (<! (send-request! conn (op-eval code)))
                   _ (info "nrepl eval result: " eval-result)]
               (>! c (merge eval-result {:id id})))
             (catch js/Error e
               (error "nrepl eval ex: " e)
               (>! c {:id id
                      :error e})))
        (close! c))
    c))
