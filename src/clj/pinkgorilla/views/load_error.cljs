(ns pinkgorilla.views.load-error
  (:require
   [pinkgorilla.events.views] ;; register event handler
   [pinkgorilla.events :as events]
   [pinkgorilla.subs :as s]))

(defn load-error [msg]
  [:div.container.mx-auto
   [:div.flex.justify-center.px-6.my-12
	;; Row 
    [:div {:class "w-full xl:w-3/4 lg:w-11/12 flex"}

	 ;; Col
     [:div
      {:class "w-full h-auto bg-gray-400 hidden lg:block lg:w-1/2 bg-cover rounded-l-lg"
       :style "background-image: url('https://source.unsplash.com/K4mSJ7kc0As/600x800')"}]

	 ;; Col 
     [:div {:class "w-full lg:w-1/2 bg-white p-5 rounded-lg lg:rounded-l-none"}
      [:h3 {:class "pt-4 text-2xl text-center"} "Notebook Load Error!"]

      [:div {:class "px-8 pt-6 pb-8 mb-4 bg-white rounded"}
       [:label {:class "block mb-2 text-sm font-bold text-gray-700" :for "username"}
        "Error Details"]

       [:textarea {:cols "50" :rows "5"
                   :class "w-full px-3 py-2 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"}

        msg]]]]]])

#_(defn load-error-dialog []
    (let [dialog (subscribe [:dialog])
          meta (subscribe [:meta])
          closefn (fn [event] (dispatch [:dialog-hide :meta]))]
      (when (:meta @dialog))))