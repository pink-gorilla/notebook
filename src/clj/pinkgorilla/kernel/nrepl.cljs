(ns pinkgorilla.kernel.nrepl
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
    [cljs-uuid-utils.core :as uuid]
    ;; [cljs.core.match :refer-macros [match]]
    [cljs.core.async :as async :refer (<! >! put! chan)]
    [re-frame.core :refer [dispatch]]
    [clojure.walk :as w]
    [taoensso.timbre :as timbre
     :refer-macros (log trace debug info warn error fatal report
                        logf tracef debugf infof warnf errorf fatalf reportf
                        spy get-env log-env)]
    ;; [com.stuartsierra.component :as component]
    ;; [system.components.sente :refer [new-channel-socket-client]]
    ;; [taoensso.sente :as sente :refer (cb-success?)]
    [chord.client :refer [ws-ch]] ; websockets with core.async
    [pinkgorilla.util :refer [ws-origin]]
    ))

;; TODO : Fixme handle breaking websocket connections
(defonce ws-repl
  (atom {:channel     nil ; created by start-ws-repl!
         :session-id  nil
         :evaluations {}
         :ciders      {}}))


(defn- send-message!
  "awb99: TODO: if websocket is nil, this will throw! (or not?).
   This could be the error of some of the notebooks not loading.
   Why dont we keep the core.async channels open all the time. And when we have a
   websocket connection, then the messages get sent. Or we just dump messages that happen
   before the socket gets opened. But in this case we have to LOG message dumping.
   I have seen situations where the first eval does not go through. Might be this issue.
   "
  [key message storeval]
  (let [uuid (uuid/uuid-string (uuid/make-random-uuid))
        nrepl-msg (clj->js (merge message
                                  {:id      uuid
                                   :session (:session-id @ws-repl)}))]
    (swap! ws-repl assoc-in [key (keyword uuid)] storeval)  ;; Store segment-id value
    (put! (:channel @ws-repl) nrepl-msg)
    uuid))

(defn send-eval-message!
  [segment-id content]
  (send-message! :evaluations
                 {:op   "eval"
                  :code content}
                 segment-id))

(defn send-cider-message!
  [message storeval]
  (send-message! :ciders message storeval))


(defn get-completions
  "Query the REPL server for autocompletion suggestions. Relies on the cider-nrepl middleware.
  We call the given callback with the list of symbols once the REPL server replies."
  [symbol ns context callback]
  (send-cider-message! {:op "complete" :symbol symbol :ns ns :context context}
                       (fn [msg]
                         (callback (->> (get msg "completions")
                                        (map #(get % "candidate")))))))

(defn get-completion-doc
  "Queries the REPL server for docs for the given symbol. Relies on the cider-nrepl middleware.
  Calls back with the documentation text"
  [symbol ns callback]
  (send-cider-message! {:op "complete-doc" :symbol symbol :ns ns}
                       (fn [msg]
                         (callback (get msg "completion-doc")))))

(defn resolve-symbol
  "resolve a symbol to get its namespace takes the symbol and the namespace that should be used as context.
  Relies on the cider-nrepl middleware. Calls back with the symbol and the symbol's namespace"
  [symbol ns callback]
  (send-cider-message! {:op "info" :symbol symbol :ns ns}
                       (fn [msg]
                         (callback {:symbol (get msg "name")
                                    :ns (get msg "ns")}))))


(defn- process-msg
  "processes an incoming message from websocket that comes from nrepl (and has cider enhancements)
   dispatches events to reagent to update notebook state ui.
  "
  [message]
  (let [id (keyword (get message "id")) ; a good Json parser can keywordize. (see metosins parser)
        out (get message "out")         ; cannot we do one assignment for all the parts?, a proper keyword destructuring
        err (get message "err")
        root-ex (get message "root-ex")
        ns (get message "ns")
        value (get message "value")
        status (get message "status")
        segment-id (get-in @ws-repl [:evaluations id]) ; awb99: should clj evals be separate or not?
        cider-cb (get-in @ws-repl [:ciders id])]
    (info "Got message" id "for segment" segment-id)
    (cond
      segment-id
      (cond
        ns
        (dispatch [:evaluator:value-response
                   segment-id {;; TODO: Never ask!
                               :value-response (-> (.parse js/JSON (.parse js/JSON value))
                                                   js->clj
                                                   w/keywordize-keys)} ns]) ;; :ns ns
        out
        (dispatch [:evaluator:console-response segment-id {:console-response out}])

        err
        ;; The logic here is a little complicated as cider-nrepl will send the stacktrace information back to
        ;; us in installments. So what we do is we register a handler for cider replies that accumulates the
        ;; information into a single data structure, and when cider-nrepl sends us a done message, indicating
        ;; it has finished sending stacktrace information, we fire an event which will cause the worksheet to
        ;; render the stacktrace data in the appropriate place.
        (let [error (atom {:error-text err
                           :segment-id segment-id})]
          (send-cider-message! {:op "stacktrace"}
                               (fn [msg] (let [status (get msg "status")]
                                           (if (and status (>= (.indexOf status "done") 0))
                                             (dispatch [:evaluator:error-response @error])
                                             (swap! error
                                                    (fn [err ex]
                                                      (if (get err :exception)
                                                        (assoc-in err [:exception :cause] (:exception ex))
                                                        (merge ex err)))
                                                    {:exception msg}))))))


        root-ex
        (info "Got root-ex" root-ex "for" segment-id)
        (>= (.indexOf status "done") 0)
        (do
          (swap! ws-repl dissoc [:evaluations id])
          (dispatch [:evaluator:done-response segment-id])))
      cider-cb
      (do
        (cider-cb message)
        (if (and status (>= (.indexOf status "done") 0))
          (swap! ws-repl dissoc [:ciders id]))))))


(defn- receive-msgs!
  [server-ch]
  (go
    (let [{:keys [message error] :as msg} (<! server-ch)]
      (info "Got initial message " message)
      (if-let [new-session (get message "new-session")]
        (do
          (swap! ws-repl assoc :session-id new-session)
          (go-loop []
                   (let [{:keys [message error] :as msg} (<! server-ch)]
                     (if message
                       (do
                         (process-msg message)
                         (recur))
                       (dispatch [:display-message (str "Fatal Error: " error " - Game over")])))))
        (dispatch [:display-message (str "Fatal Error : " error " - Unable to create session. Game over")])))))

(defn- send-msgs! [new-msg-ch server-ch]
  (go-loop []
           (when-let [msg (<! new-msg-ch)]
             (>! server-ch msg)
             (recur))))

(defn start-ws-repl!
  [path app-url]
  (info "clj kernel starting at" path)
  (go
    (let [ws-url (ws-origin path app-url)
          {:keys [ws-channel error]} (<! (ws-ch ws-url {:format :json}))]
      (if error
        (dispatch [:display-message error])
        #_(do
            (print msg "to " ws-channel)
            (>! ws-channel msg))
        #_(js/alert (str "Error" error)))
      (let [new-msg-ch (doto (chan)
                         (send-msgs! ws-channel))]
        (swap! ws-repl assoc :channel new-msg-ch)
        (receive-msgs! ws-channel)
        (>! new-msg-ch #js {:op "clone"})))))
