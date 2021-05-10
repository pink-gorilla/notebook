(ns pinkgorilla.notebook-ui.eval-result.datafy
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.datafy.events-nrepl :refer [decode]]))

(defn decode-safe [datafy]
  (try
    (decode datafy)
    (catch js/Error _
      nil)))

(defn datafy-link [datafy]
  (when datafy ; datafy is a string
    ;(info "datafy str:" datafy)
    (when-let [datafy-val (when datafy
                            (decode-safe datafy))]
      (info "datafy val:" datafy-val)
      (when-let [v (:value datafy-val)] ;{:idx 13, :value nil, :meta nil}
        [:div.text-blue-700
         [:a {:on-click #(rf/dispatch [:datafy/show datafy-val])}
          [:i.fa.fa-search-plus]]]))))



