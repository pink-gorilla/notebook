(ns pinkgorilla.notebook-ui.events.events-move
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [dispatch reg-event-db]]))



;(.scrollTo js/window (0, tesNode.offsetTop);


#_(reg-event-db
   :notebook/scroll-to
   (fn [db [_]]
     (let [storage (:notebook db)
           notebook (get-in db [:document :documents storage])
           active (:active notebook)
           active-id (str active)]
       (when active
         (when-let [node (.getElementById js/document active-id)]
           (doto node
             (.scrollIntoView))))
       db)))


