(ns pinkgorilla.notebook-ui.app.site)

(defn main-with-header [header main]
  [:div
   {:style {:width "100vw"
            :height "100vh"
            :display "grid"
            :grid-template-rows "30px 1fr"}}
   [:div {:style {:grid-column "1/-1"}}
    header]
   [:div {:style {:height "100%"
                  :max-height "100%"
                  :width "100%"
                  :overflow "auto"}}
    main]])

(def demo-header
  [:div.bg-blue-400
   {:style {:min-height "100%"}}
   [:a {:href "/"}
    [:span.bg-yellow-300.w-12.border.border-round.p-2
     "main"]]])