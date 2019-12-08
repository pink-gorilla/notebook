(ns pinkgorilla.events
  (:require
   [day8.re-frame.http-fx]
   [day8.re-frame.undo :as undo :refer [undoable]]

   ;; [cljsjs.mousetrap]
   ;; [cljsjs.mousetrap-global-bind]
   ["mousetrap"]
   ["mousetrap-global-bind"]

   ;; event requires produce side effects (they register the event handlers)
   [pinkgorilla.events.worksheet]
   [pinkgorilla.events.palette]
   [pinkgorilla.events.kernel]
   [pinkgorilla.events.message]
   [pinkgorilla.events.notifications]
   [pinkgorilla.events.doc]
   [pinkgorilla.events.settings]
   [pinkgorilla.events.config]
   [pinkgorilla.events.explore]
   [pinkgorilla.events.multikernel]
   [pinkgorilla.events.notebook]

   [pinkgorilla.events.storage]
   [pinkgorilla.events.storage-save-dialog]
   [pinkgorilla.events.storage-file]

   [pinkgorilla.explore.subs]
   [pinkgorilla.explore.handlers]
   ;[pinkgorilla.explore.list]
   

   ))


;; TODO Should move evaluation state out of worksheet
(undo/undo-config! {:max-undos    3
                    :harvest-fn   (fn [ratom] (some-> @ratom :worksheet))
                    :reinstate-fn (fn [ratom value] (swap! ratom assoc-in [:worksheet] value))})
