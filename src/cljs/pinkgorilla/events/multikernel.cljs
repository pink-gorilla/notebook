(ns pinkgorilla.events.multikernel
  "events related to kernel switch"
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   [taoensso.timbre :refer-macros (info)]
   ))


(defn kernel-toggle [current-kernel]
  (case current-kernel
          :clj :mock
          :mock :cljs
          :cljs :clj
          nil))


(reg-event-db
 :app:kernel-toggle
 (fn [db _]
   (let [active-id (get-in db [:worksheet :active-segment])

         active-segment (get-in db [:worksheet :segments active-id])
         _ (info "active segment")
         current-kernel (:kernel active-segment)
         _ (info "multi-kernel-toggle for segment " active-id " current kernel: " current-kernel)
         new-kernel (kernel-toggle current-kernel)
         _ (info "new kernel: " new-kernel)]
     (if (nil? new-kernel) ; free segments dont have a kernel, so we wont toggle
       db
       (do
         (dispatch [:display-message (str "cell kernel is now" new-kernel) ])
         (assoc-in db [:worksheet :segments active-id :kernel] new-kernel )))
         )))



