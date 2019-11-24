(ns pinkgorilla.views.auth
  (:require
   [re-frame.core :as rf]
   [pinkgorilla.views.misc :as misc]))

;; stolen from:
;; https://github.com/timgilbert/haunting-refrain-posh/blob/develop/src/cljs/haunting_refrain/views/foursquare.cljs

(defn- login-button []
  [misc/big-button
   {:dispatch [:auth/login :foursquare]
    :icon "fa-foursquare"}
   "Log In to Foursquare"])

(defn- logout-button []
  [misc/big-button
   {:dispatch [:auth/logout :foursquare]
    :icon "fa-foursquare"
    :class "is-warning"}
   "Log Out of Foursquare"])

(defn- logged-out-page []
  [:div.container.content
   [:h1.title "Log In to Foursquare"]
   [:div
    [:p "To log in to foursquare, hit the link below. You'll be redirected to foursquare to authorize the "
     "application, after which you'll be returned to Haunting Refrain."]
    [login-button]]])

(defn- logged-in-page []
  [:div.container.content
   [:h1.title "Foursquare"]
   [:div
    [:p "Your browser has been authenticated with Foursquare. To log out, use this button:"]
    [logout-button]]])

(defn foursquare-page []
  (let [logged-in (rf/subscribe [:auth/logged-in? :foursquare])]
    (fn []
      (if @logged-in
        [logged-in-page]
        [logged-out-page]))))

(defn hello-page
  "This page is the entry point into hr when the user returns from foursquare authentication."
  []
  (rf/dispatch [:auth/parse-token :foursquare])
  (fn []
    [:div.container.content ""]))


; [:button.github {:on-click (fn [_] (set! (.-location js/window) "/github-login"))}
;          "Login with Github"]]



