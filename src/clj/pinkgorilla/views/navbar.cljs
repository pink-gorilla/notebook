(ns pinkgorilla.views.navbar
  (:require
   [reagent.core :as r :refer [atom]]
   [re-frame.core :as rf]
   [pinkgorilla.events.views :as views-events]
   [pinkgorilla.events :as events]
   [pinkgorilla.subs :as s]))

;; stolen from:
;; https://github.com/baskeboler/cljs-karaoke-client/blob/master/src/main/cljs_karaoke/views/navbar.cljs

(defn navbar-component []
  (let [is-active? (rf/subscribe [::s/navbar-menu-active?])]
    [:nav.navbar.is-fixed-top.is-transparent
     [:div.navbar-brand
      [:a.navbar-item
       {:href "#/"}
       [:object.header-logo
        {:data "favicon.ico"
         :title "header logo"}]]
      [:a
       {:role :button
        :class (concat
                ["navbar-burger" "burger"]
                (if @is-active? ["is-active"] []))
        :on-click #(rf/dispatch [::events/set-navbar-menu-active? (not @is-active?)])}
       [:span] [:span] [:span]]]
     [:div
      {:class (concat
               ["navbar-menu"]
               (if @is-active? ["is-active"] []))}
      [:div.navbar-start


       [:a.navbar-item
        {;:href "#/"
         :on-click #(do (rf/dispatch [:goto-main])
                        (rf/dispatch [:initialize-new-worksheet])
                        (rf/dispatch [:nav-to-storage]))}
        "new-notebook"]

       [:a.navbar-item
        {;:href "#/"
         :on-click #(do (rf/dispatch [:goto-main])
                        (rf/dispatch [:nav-to-storage]))}
        "notebook"]

       [:a.navbar-item
        {;:href "#/"
         :on-click #(rf/dispatch [:nav "/explore"])}
        "explorer"]

       [:a.navbar-item
        {;:href "#/playlist"
         :on-click #(rf/dispatch [:dialog-show :settings])}
        "settings"]

       [:a.navbar-item
        {:on-click #(do
                      (rf/dispatch [:dialog-show :meta])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                      )}
        "meta"]

       [:a.navbar-item
        {:on-click #(do
                      (rf/dispatch [:app:saveas])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                      )}
        "save-as"]]]]))