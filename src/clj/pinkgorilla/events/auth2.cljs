(ns pinkgorilla.events.auth2
  (:require
   [re-frame.core :refer [reg-event-fx dispatch]]))

(defn clear-error [state]
  (assoc-in state [:user-auth :error] nil))

(defn message-event-handler
  "Message handler for oauth login (from ::open-oauth-window).
  This is a named function to prevent the handler from being added
  multiple times."
  [e]
  (dispatch [::remote-oauth (.. e -data -code) (.. e -data -state)]))

(reg-event-fx
 :open-oauth-window
 (fn [{db :db} [_ provider]]
   (js/window.addEventListener "message" message-event-handler)
   (case provider
     :github
     (.open js/window
            "/gateway/oauth/github/auth"
            "GitHub OAuth"
            "width=300,height=400"))
   {:db (-> db
            clear-error
            (assoc-in [:user-auth :oauth-provider] provider))}))