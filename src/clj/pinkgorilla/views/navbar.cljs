(ns pinkgorilla.views.navbar
  (:require
   ;;[taoensso.timbre :refer-macros (info)]
   ;[reagent.core :as r :refer [atom]]
   [re-frame.core :as rf]
   [pinkgorilla.events.views] ;; register events
   [pinkgorilla.events] ;; register events
   [pinkgorilla.subs] ;; subs
   [pinkgorilla.kernel.cljs-tools :refer [print-loaded-shadow-namespaces]]))

;; stolen from:
;; https://github.com/baskeboler/cljs-karaoke-client/blob/master/src/main/cljs_karaoke/views/navbar.cljs

(def notebook-items
  [:span.bg-green-700.pt-2.p-2

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:bg-orange-500 mr-4"
        :on-click #(do
                     (rf/dispatch [:worksheet:evaluate-all])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    "Evaluate All"]

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:bg-orange-500 mr-4"
        :on-click #(do
                     (rf/dispatch [:worksheet:clear-all-output])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    "Clear all output"]

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:bg-orange-500 mr-4"
        :on-click #(do
                     (rf/dispatch [:dialog-show :meta])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    "Meta"]

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
        :on-click #(do
                     (rf/dispatch [:save-notebook])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    [:i.fas.fa-save]
    "Save"]

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white"
        :on-click #(do
                     (rf/dispatch [:app:saveas])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    "Save As"]])

(def developer-items
  [:span.bg-red-700.pt-2.p-2

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:bg-orange-500 mr-4"
        :on-click #(do
                     (rf/dispatch [:toggle.reframe10x])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    "Toggle reframe-10x"]

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:bg-orange-500 mr-4"
        :on-click #(do
                     (rf/dispatch [:open-oauth-window :github])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                     )}
    "Github login"]

   [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:bg-orange-500 mr-4"
        :on-click #(do
                     (print-loaded-shadow-namespaces))}
    "p-lns"]])

(defn navbar-component []
  (let [;; is-active? (rf/subscribe [:navbar-menu-is-active?])
        main (rf/subscribe [:main])
        ;; notebook  (rf/subscribe [:worksheet])
        kernel-connected (rf/subscribe [:kernel-clj-connected])
        ;_ (info "main is: " @main " notebook :" @notebook)
        ]
    [:nav {:class "flex items-center justify-between flex-wrap bg-teal-500 p-6 text-base"}

     ;; Logo
     [:div {:class "flex items-center flex-shrink-0 text-white mr-6"}
      [:img.fill-current.h-8.w-8.mr-2 {:src "favicon.ico" :width 54 :height 54}]
      #_[:svg  {:class "fill-current h-8 w-8 mr-2" :width "54" :height "54" :viewBox "0 0 54 54" :xmlns "http://www.w3.org/2000/svg"}
         [:path {:d "M13.5 22.1c1.8-7.2 6.3-10.8 13.5-10.8 10.8 0 12.15 8.1 17.55 9.45 3.6.9 6.75-.45 9.45-4.05-1.8 7.2-6.3 10.8-13.5 10.8-10.8 0-12.15-8.1-17.55-9.45-3.6-.9-6.75.45-9.45 4.05zM0 38.3c1.8-7.2 6.3-10.8 13.5-10.8 10.8 0 12.15 8.1 17.55 9.45 3.6.9 6.75-.45 9.45-4.05-1.8 7.2-6.3 10.8-13.5 10.8-10.8 0-12.15-8.1-17.55-9.45-3.6-.9-6.75.45-9.45 4.05z"}]]
      [:span.fg-pink-500 {:class "font-semibold text-xl tracking-tight"} "PinkGorilla"]]

     ;; Menu Dropdown Container
     [:div {:class "block lg:hidden"}
      [:button {:class "flex items-center px-3 py-2 border rounded text-teal-200 border-teal-400 hover:text-white hover:border-white"}
       [:svg {:class "fill-current h-3 w-3" :viewBox "0 0 20 20" :xmlns "http://www.w3.org/2000/svg"}
        [:title "Menu"]
        [:path {:d "M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"}]]]]

    ; Menu Items
     [:div {:class "w-full block flex-grow lg:flex lg:items-center lg:w-auto"}

      ;; Menu Items Left
      [:div {:class "text-base lg:flex-grow"}

       [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
            :on-click #(do (rf/dispatch [:goto-main])
                           (rf/dispatch [:initialize-new-worksheet])
                           (rf/dispatch [:nav-to-storage true]))}
        "New Notebook"]

       [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
            :on-click #(do (rf/dispatch [:goto-main])
                           (rf/dispatch [:nav-to-storage false]))}
        "Notebook"]

       [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
            :on-click #(rf/dispatch [:nav "/explore"])}
        "Explorer"]

       [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
            :on-click #(rf/dispatch [:dialog-show :settings])}
        "Settings"]

       (if @kernel-connected
         [:span {:class "block mt-4 lg:inline-block lg:mt-0 text-green-700 mr-4"}
          [:i.fas.fa-play]]
         [:a {:class "block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white text-red-600 mr-4"
              :on-click #(rf/dispatch [:kernel-clj-connect])}
          [:i.fas.fa-skull-crossbones]])

       ; show notebook-menu only when we are in notebook view and we have a valid notebook
       (when (and (= @main :notebook)
                  true ; (not (nil? notebook))
                  )
         notebook-items)

       ; developer menu - show only when in dev-mode
       ;; TODO: Add some conditional
       ;; developer-items
       ]

      ;; Menu Items Right
      [:div
       [:a {:href "#"
            :class "inline-block text-sm px-4 py-2 leading-none border rounded text-white border-white hover:border-transparent hover:text-teal-500 hover:bg-white mt-4 lg:mt-0"} "Download"]]]]))
