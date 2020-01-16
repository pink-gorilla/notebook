

(defn message-event-handler
  "Message handler for oauth login (from ::open-oauth-window).
  This is a named function to prevent the handler from being added
  multiple times."
  [e]
  (dispatch [::remote-oauth (.. e -data -code) (.. e -data -state)]))

(reg-event-fx
 ::open-oauth-window
 (fn [{state :db} [_ provider]]
   (js/window.addEventListener "message" message-event-handler)
   (case provider
     :github
     (.open js/window
            "/gateway/oauth/github/auth"
            "GitHub OAuth"
            "width=300,height=400"))
   {:db (-> state
            clear-error
            (assoc-in [:user-auth :oauth-provider] provider))}))

(reg-event-fx
 ::remote-oauth
 (fn [_ [_ code state]]
   {:edn-xhr {:uri "/session/oauth/github"
              :method :put
              :params {:code code
                       :state state}
              :on-complete
              (fn [user]
                (dispatch [::remote-check-auth]))
              :on-error
              (fn [error]
                (when-let [k (get-in error [:response :error])]
                  (dispatch [::set-error k]))
                (dispatch [::set-user nil]))}}))


(defn auth-providers-view []
  [:span.auth-providers
   (doall
    (for [provider [:github]]
      ^{:key provider}
      [:button
       {:type "button"
        :class (name provider)
        :on-click (fn [e]
                    (.preventDefault e)
                     ; must use dispatch-sync
                     ; b/c dispatch triggers pop-up blocker
                    (dispatch-sync [:braid.core.client.gateway.forms.user-auth.events/open-oauth-window provider]))}
       (string/capitalize (name provider))]))])


(defn checking-user-view []
  [:div.section.user-auth.checking
   [:div
    [:span "Authenticating..."]]])

(defn oauth-in-progress-view []
  [:div.section.user-auth.authorizing
   [:div
    [:span "Authenticating with " (string/capitalize (name @(subscribe [:braid.core.client.gateway.forms.user-auth.subs/oauth-provider]))) "..."]]])

(defn user-auth-view []
  (case @(subscribe [:braid.core.client.gateway.forms.user-auth.subs/user-auth-mode])
    :checking [checking-user-view]
    :register [new-user-view]
    :request-password-reset [request-password-reset-view]
    :log-in [returning-user-view]
    :authed [authed-user-view]
    :oauth-in-progress [oauth-in-progress-view]))


(defn authed-user-view []
  (let [user @(subscribe [:braid.core.client.gateway.forms.user-auth.subs/user])]
    [:div.section.user-auth.authed-user
     [:div.profile
      [:img.avatar {:src (user :avatar)}]
      [:div.info
       [:div.nickname "@" (user :nickname)]
       [:div.email (user :email)]]]
     [:p "Not you?"
      [:button
       {:type "button"
        :on-click (fn []
                    (dispatch [:braid.core.client.gateway.forms.user-auth.events/switch-account]))}
       "Sign in with a different account"]]]))


;; https://github.com/braidchat/braid/blob/fb491250d1397c66d385f607058706551e31b51b/src/braid/core/client/gateway/forms/user_auth/views.cljs




   ; OAuth dance
    (GET "/oauth/github"  [code state :as req]
      (println "GITHUB OAUTH" (pr-str code) (pr-str state))
      (if-let [{tok :access_token scope :scope :as resp}
               (github/exchange-token code state)]
        (do (println "GITHUB TOKEN" tok)
            ; check scope includes email permission? Or we could just see if
            ; getting the email fails
            (let [email (github/email-address tok)
                  user (user/user-with-email email)]
              (cond
                (nil? email) {:status 401
                              :headers {"Content-Type" "text/plain"}
                              :body "Couldn't get email address from github"}

                user {:status 302
                      ; TODO: when we have mobile, redirect to correct site
                      ; (maybe part of state?)
                      :headers {"Location" (config :site-url)}
                      :session (assoc (req :session) :user-id (user :id))}

                (:braid.server.api/register? resp)
                (let [user-id (register-user email (:braid.server.api/group-id resp))]
                  {:status 302
                   ; TODO: when we have mobile, redirect to correct site
                   ; (maybe part of state?)
                   :headers {"Location" (config :site-url)}
                   :session (assoc (req :session) :user-id user-id)})

                :else
                {:status 401
                 ; TODO: handle failure better
                 :headers {"Content-Type" "text/plain"}
                 :body "No such user"
                 :session nil})))
        {:status 400
         :body "Couldn't exchange token with github"})))


