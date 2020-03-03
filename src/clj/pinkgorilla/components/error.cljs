(ns pinkgorilla.components.error
  (:require
   [reagent.core :as reagent]
      ;[re-catch.core :as rc]
   ))


;; https://github.com/reagent-project/reagent/issues/466


#_(defn error-boundary [_]
    (r/create-class
     {:display-name "ErrorBoundary"
      :get-initial-state
      (fn [_]
        #js {:error nil})
      :get-derived-state-from-error
      (fn [error]
        #js {:error error})
      :render
      (fn [this]
        (-> (if-let [error (.. this -state -error)]
              [:pre [:code (pr-str error)]]
              (into [:<>] (r/children this)))
            (r/as-element)))}))

(defn error-boundary [_ #_comp]
  (let [error (reagent/atom nil)
        info (reagent/atom nil)]
    (reagent/create-class
     {:component-did-catch (fn [_ #_this _ #_e i]
                             (reset! info i))
      :get-derived-state-from-error (fn [e]
                                      (reset! error e)
                                      #js {})
      :reagent-render (fn [comp]
                        (if @error
                          [:div "Something went wrong."]
                          comp))})))

