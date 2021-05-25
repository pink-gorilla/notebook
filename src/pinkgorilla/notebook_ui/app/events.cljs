(ns pinkgorilla.notebook-ui.app.events
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :as rf]
   ;[pinkgorilla.notebook-ui.hydration :refer [hydrate dehydrate]]
   [pinkgorilla.notebook-ui.app.css :as notebook-css]))

(rf/reg-event-db
 :notebook/init
 (fn [db [_ dispatch-init-done]]
   (let [db (or db {})
         {:keys [nrepl-endpoint]} (:config db)]
     (info "notebook-ui/init")

     ; explorer
     (rf/dispatch [:explorer/init  {;:fn-hydrate nil ; hydrate
                                    ;:fn-dehydrate nil ; dehydrate
                                    }])

     ; nrepl


     (rf/dispatch [:nrepl/init nrepl-endpoint])

     ; from notebook-ui
     (rf/dispatch [:css/add-components notebook-css/components notebook-css/config])

     ;(dispatch [:sniffer/init]) ; hack so sniffer notebook route works at startup.
     (rf/dispatch [:punk/init])
     (rf/dispatch [:completion/init])
     (rf/dispatch [:schema/check-on])
     (when dispatch-init-done
       (rf/dispatch dispatch-init-done))

     (-> db
         (assoc-in [:notebook] nil)))))


