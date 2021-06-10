(ns pinkgorilla.notebook-ui.sniffer.events
  (:require
   [taoensso.timbre :as timbre :refer-macros [info errorf debug warn error]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.sniffer.dump :refer [dump]]
   [pinkgorilla.nrepl.client.op.eval :refer [process-fragment initial-value]]
   [notebook.core :refer [code-segment]]))

(rf/reg-event-fx
 :nrepl/register-sniffer-sink
 (fn [{:keys [db] :as cofx}  [_]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (info "registering sniffer-sink")
     ;(dispatch [:nrepl/op-dispatch-rolling {:op "eval" :code "(+ 7 7)"} [:sniffer/rcvd]])
     (rf/dispatch [:nrepl/op-dispatch-rolling {:op "sniffer-sink"} [:sniffer/rcvd]])
     nil)))

(def evals (atom {}))

(defn ns-only? [code]
  (or (re-matches #"^\(ns .*\)$" code)
      (re-matches #"^\(in-ns '.*\)$" code)
      (re-matches #"^\*ns\*$" code)))

(defn set-code  [{:keys [op id code ns picasso value out err] :as msg}]
  (if (ns-only? code)
    (warn "ns only: " code)
    (do
      (swap! evals assoc id {:code code :id id :er initial-value})
      (rf/dispatch [:doc/exec [:add-segment
                               (-> (code-segment :clj code)
                                   (assoc :id id))]])
      (rf/dispatch [:doc/exec [:set-state-segment id initial-value]]))))

(defn set-result [{:keys [op id code ns picasso value out err] :as msg}]
  (warn "(partial) result: " msg)
  (let [er {:ns ns  ; this triggers picasso/value
            :picasso picasso
            :value value
            :out out
            :err err}
        result (get-in @evals [id :er])
        _ (warn "result :" result)
        er (process-fragment result er)
        _ (swap! evals assoc-in [id :er] er)]
    (info "er id: " id " result: " er)
    (rf/dispatch [:doc/exec [:set-state-segment id er]])))

(defn process [{:keys [op id code ns picasso value out err] :as msg}]
  (cond
    (= "eval" op)
    (set-code msg)

    (or picasso value out err)
    (set-result msg)

    :else
    (warn "sniffer unprocessed msg: " msg)))

(rf/reg-event-db
 :sniffer/rcvd
 (fn [db [_ msg]]
   (info "sniffer rcvd: " msg)
   (let [{:keys [session-id-sink session-id-source]} msg]
     (if (or session-id-sink session-id-source)
       ; admin message
       db
       ; eval or eval result
       (do ;
         (dump msg)
         (process msg)
         db)))))


