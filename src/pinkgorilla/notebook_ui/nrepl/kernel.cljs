(ns pinkgorilla.notebook-ui.nrepl.kernel
  (:require
   [clojure.core.async :refer [<! >! chan close! go]]
   [taoensso.timbre :as timbre :refer [debugf info error]]
   [re-frame.core :as rf]
   [picasso.id :refer [guuid]]
   [picasso.kernel.protocol :refer [kernel-eval]]
   [pinkgorilla.nrepl.client.core :refer [send-request! op-eval op-stacktrace]]
   [pinkgorilla.notebook-ui.nrepl.subscriptions] ; side effects
   ))

(def nrepl (rf/subscribe [:nrepl/status]))

(defn stacktrace? [eval-result]
  (when (:err eval-result)
    (when (first (:err eval-result))
      true)))

(defn get-stacktrace [conn eval-result]
  (go (let [_ (info "getting stacktrace of exception.")
            st (<! (send-request! conn (op-stacktrace)))]
        (merge eval-result st))))

(defmethod kernel-eval :clj [{:keys [id code]
                              :or {id (guuid)}}]
  (let [c (chan)]
    (info "clj-eval: " code)
    (go (try (let [_ (info "nrepl: " @nrepl)
                   conn (:conn @nrepl)
                   eval-result (<! (send-request! conn (op-eval code)))
                   _ (info "nrepl eval result: " eval-result)
                   ;eval-result (if (stacktrace? eval-result)
                   ;              (get-stacktrace conn eval-result)
                   ;              eval-result)
                   ]
               (>! c (merge eval-result {:id id})))
             (catch js/Error e
               (error "nrepl eval ex: " e)
               (>! c {:id id
                      :error e})))
        (close! c))
    c))
