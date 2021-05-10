(ns pinkgorilla.notebook-ui.tooltip
  (:require
   [reagent.core :as r]))

; stolen from:
; https://www.creative-tim.com/learning-lab/tailwind-starter-kit/documentation/react/popovers/left

; ref handling from:
; https://gist.github.com/pesterhazy/4d9df2edc303e5706d547aeabe0e17e1

(defn box-with-title [{:keys [title color]} & children]
  [:div
   [:div {:class (str "bg-" color "-600 text-white opacity-75 font-semibold p-3 mb-0 border-b border-solid border-gray-200 uppercase rounded-t-lg")}
    title]
   [:div {:class "text-white p-3"}
    children]])

(defn with-tooltip [text & children]
  (let [showing? (r/atom false)
        el-parent (r/atom nil)
        el-tooltip (r/atom nil)
        open-tooltip (fn [& args]
                       (reset! showing? true))
        close-tooltip (fn [&args]
                        (reset! showing? false))]
    (fn [text & children]
      (into [:div.inline-block {:on-mouse-enter open-tooltip
                                :on-mouse-leave close-tooltip
                                :ref #(reset! el-parent %)}
             (when @showing?
               [:div {:style {:top "40px"
                            ;:left "10px"
                              }
                      :class  "absolute bg-blue-300 p-2 top-3 border-0 mr-3 block z-50 font-normal leading-normal text-sm max-w-xs text-left no-underline break-words rounded-lg"
                      :ref  #(reset! el-tooltip %)}
                text])]
            children))))

