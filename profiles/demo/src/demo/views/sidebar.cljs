(ns demo.views.sidebar
  (:require
   [reagent.core :as r]
   ;[pinkgorilla.ui.config :refer [link-css]]
   ))

; from:
; https://github.com/tailwindtoolbox/Sidebar-Bottom/blob/master/index.html

; https://tailwindcomponents.com/component/app-sidebar
; https://github.com/creativetimofficial/tailwind-starter-kit/blob/master/Dashboard%20Page/react-dashboard-page/src/components/Sidebar.js


(defn sidebar-item [{:as item
                     :keys [name fas page active? goto-page]
                     :or {fas "link"}}]
  (let [active? (active? item)]
    [:li {:class "mr-3 flex-1"}
     [:a {;:href "#"
          :on-click (fn [& _] (goto-page item))
          :class (str "block py-1 md:py-3 pl-1 align-middle no-underline border-b-2 "
                      (if active?
                        "text-white hover:text-white border-pink-600"
                        "text-gray-600 hover:text-pink-500 border-gray-800 md:border-gray-900 hover:border-pink-500"))}
      [:i {:class (str "fas fa-" fas " pr-0 md:pr-3 "
                       (if active?
                         "text-pink-500"
                         ""))}]
      [:span {:class (str "pb-1 md:pb-0 text-xs md:text-base block md:inline-block "
                          (if active?
                            "text-white md:font-bold"
                            "text-gray-600 md:text-gray-400"))} name]]]))

(defn  sidebar
  "a sidebar menu; on mobile moves to bottom"
  [items default-name]
  (let [pg (first (filter  #(= (:name %) default-name) items))
        active-name (r/atom (:name pg))
        active-page (r/atom (:page pg))
        goto-page (fn [item]
                    (println "page selected: " (:name item))
                    (reset! active-page (:page item))
                    (reset! active-name (:name item)))
        active? (fn [item]
                  ;(println "active? " (:name item) (= (:name item) @active-name))
                  (= (:name item) @active-name))]
    (fn []
      [:div
      ;flexbox container
       [:div {:class (str "flex flex-wrap h-screen w-screen "
                          "md:flex-row-reverse")}
       ; Main Content
        [:div {:class (str "w-full h-full bg-blue-100 " ; sm: w-full
                           "lg:w-5/6 "
                           "md:w-4/5 ")}
         [:div {:class "p-0 m-0"} ; "pt-16 px-6"} ; bg-gray-100 container
          (when @active-page
            @active-page)]]
        ; Sidebar
        [:div {:class (str "w-full bg-blue-500 px-2 text-center fixed bottom-0 " ; sm: w-full bg-blue-500
                           "lg:w-1/6 "
                           "md:w-1/5 md:pt-8 md:top-0 md:left-0 h-16 md:h-screen md:border-r-4 md:border-pink-600 md:bg-teal-800 ")}
         [:div {:class (str " mx-auto "
                            "lg:float-right lg:px-6 "
                            "md:relative")}
          (into [:ul {:class (str "list-reset flex flex-row text-center "
                                  "md:flex-col  md:text-left")}]
                (map (fn [item]
                       [sidebar-item (assoc item
                                            :goto-page goto-page
                                            :active? active?)]) items))]]]])))



#_(defn example-menu [{:keys [name goto-page] :as example}]
    [:a {:href "#"
         :on-click #(goto-page example)
         :class "block text-left xl:flex xl:items-center shadow xl:shadow-none py-3 px-3 xl:px-4 border-l-4 border-transparent text-red hover:text-blue-300 hover:bg-green-300 text-xs"}
     name])

#_(defn sidebar [{:keys [examples goto-page]}]
    [:div {:class "bg-yellow-300 h-full w-1/12 min-h-screen xl:py-2"}
     [:div {:class "xl:block uppercase font-bold text-grey-darker text-xs px-4 py-2"}

      (map-indexed
       (fn [i example]
         ^{:key i}
         [example-menu (assoc example :goto-page goto-page)])
       examples)]])

#_[:div.flex.font-sans.antialiased.h-screen
   [sidebar {:examples @examples :goto-page goto-page}]
   [:div.bg-white.h-full.w-full.text-center.text-grey-darkest
    [@current]]]

; [:div.flex.font-sans.antialiased.h-screen
;        [sidebar {:examples @examples :goto-page goto-page}]
;        [:div.bg-white.h-full.w-full.text-center.text-grey-darkest
