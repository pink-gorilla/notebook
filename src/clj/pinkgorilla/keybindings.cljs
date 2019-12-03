(ns pinkgorilla.keybindings
  (:require
    ;; ["mousetrap-global-bind"]
   [pinkgorilla.routes :as routes]
   [re-frame.core :refer [dispatch-sync]]))

;; Experimental externs inference
;; https://gist.github.com/swannodette/4fc9ccc13f62c66456daf19c47692799
(defn kb-bind
  [^js/Mousetrap.bindGlobal mousetrap command]
  (if-let [kb (:kb command)]
    (let [kb-val (if (vector? kb) (clj->js kb) kb)
          handler (if (vector? (:handler command)) (:handler command) [(:handler command)])]
      (.bindGlobal mousetrap kb-val
                   #(dispatch-sync handler)))))


(defn install-commands
  [command-keymap]
  ;; ** Patch Mousetrap **
  ;;  Install a custom stopCallback so that our keyboard shortcuts work in the codeMirror textareas.
  ;;  This also lets us disable mousetrap processing when we show dialogs
  ;;  (this idea shamelessly stolen from the Mousetrap 'pause' plugin).
  (if-let [mousetrap (if (and (exists? js/window) (.-Mousetrap js/window))
                       (.-Mousetrap js/window))]
    (do
      (set! (.-enabled mousetrap) true)
      (set! (.-enable mousetrap) (fn [x] (set! (.-enabled mousetrap) x)))
      (set! (.-stopCallback mousetrap) #(not (.-enabled mousetrap)))
      (doall (map (partial kb-bind mousetrap) command-keymap)))))



;; TODO: Hacky editor access
#_(defn clojuredocs
    [app]
    (let [active-id (get-in app [:worksheet :active-segment])
          active-segment (get-in app [:worksheet :segments active-id])]
      (if (= :code (:type active-segment))
        (if-let [token (editor/get-token-at-cursor active-id)]
        ;; http://stackoverflow.com/questions/19026162/javascript-window-open-from-callback
        ;; We (should) try and resolve the symbol's namespace to jump directly to the clojuredocs page.
        ;; This is async, so we open the window now so as not to be stymied by the popup-blocker
          (let [win (.open js/window "" "_blank")]
            (dispatch [:docs:clojuredocs win token]))))
      app))

;; (-> (get-in initial-db [:palette :all-visible-commands]) cljs.pprint/pprint)

;; handler is an argument that gets dispatched to reframe. Can be keyword (no args) or vector (multiple args)

(def all-commands
  [{:name    "command:app:commands"
    :desc    "Show the command list."
    :kb      "alt+g alt+g"
    :handler [:app:commands]}
   #_{:name       "command:worksheet:leaveBack"
      :desc       "Move to the previous segment."
      :showInMenu false
      :handler    :worksheet:leaveBack}
   #_{:name       "command:worksheet:leaveForward"
      :desc       "Move to the next segment."
      :showInMenu false
      :handler    :worksheet:leaveForward}
   {:name    "command:evaluator:evaluate"
    :desc    "Evaluate the highlighted segment."
    :kb      "shift+enter"
    :handler [:worksheet:evaluate]}
   {:name    "command:evaluator:evaluate-all"
    :desc    "Evaluate all segments."
    :kb      "alt+shift+enter"
    :handler [:worksheet:evaluate-all]}
   {:name    "command:worksheet:clear-output"
    :desc    "Clear the output of the highlighted segment."
    :kb      "alt+g alt+o"
    :handler [:worksheet:clear-output]}
   {:name    "command:worksheet:clear-all"
    :desc    "Clear the output of all code segments."
    :kb      "alt+g alt+z"
    :handler [:worksheet:clear-all-output]}
   {:name    "command:worksheet:delete"
    :desc    "Delete the highlighted segment."
    :kb      "alt+g alt+x"
    :handler [:worksheet:delete]}
   {:name    "command:worksheet:undo"
    :desc    "Undo the last segment operation."
    :kb      "alt+g alt+\\"
    :handler [:undo] #_"worksheet:undelete"}
   {:name    "command:worksheet:changeToFree"
    :desc    "Convert the highlighted segment to a markdown segment."
    :kb      "alt+g alt+m"
    :handler [:worksheet:changeToFree]}
   {:name    "command:worksheet:changeToCode"
    :desc    "Convert the highlighted segment to a clojure segment."
    :kb      "alt+g alt+j"
    :handler [:worksheet:changeToCode]}
   {:name    "command:app:open"
    :desc    "Load a Notebook"
    :kb      "alt+g alt+l"
    :handler [:browse-files-local]}
   {:name    "command:app:save"
    :desc    "Save the notebook"
    :kb      "alt+g alt+s"
    :handler [:save-notebook]}
   {:name    "command:app:showsettings"
    :desc    "Settings Edit."
    :kb      ["ctrl+1" "alt+g alt+1"]
    :handler [:dialog-show :settings]}
   {:name    "command:app:kernel-toggle"
    :desc    "Kernel Toggle."
    :kb      ["ctrl+3" "alt+g alt+3"]
    :handler [:app:kernel-toggle]}
   {:name    "command:app:showmeta"
    :desc    "Meta Edit."
    :kb      ["ctrl+2" "alt+g alt+2"]
    :handler [:dialog-show :meta]}
   {:name    "command:nav:explore"
    :desc    "Explore Notebooks."
    :kb      ["ctrl+4" "alt+g alt+4"]
    :handler [:nav "/explore"]}

   {:name    "command:app:saveas"
    :desc    "Save the worksheet to a new filename."
    :kb      "alt+g alt+e"
    :handler [:app:saveas]}
   {:name    "command:worksheet:newBelow"
    :desc    "Create a new segment below the highlighted segment."
    :kb      "alt+g alt+n"
    :handler [:worksheet:newBelow]}
   {:name    "command:worksheet:newAbove"
    :desc    "Create a new segment above the highlighted segment."
    :kb      "alt+g alt+b"
    :handler [:worksheet:newAbove]}
   {:name    "command:worksheet:moveUp"
    :desc    "Move the highlighted segment up the worksheet."
    :kb      "alt+g alt+u"
    :handler [:worksheet:moveUp]}
   {:name    "command:worksheet:moveDown"
    :desc    "Move the highlighted segment down the worksheet."
    :kb      "alt+g alt+d"
    :handler [:worksheet:moveDown]}
   #_{:name    "command:docs:clojuredocs"
      :desc    "Look up the symbol under the cursor in ClojureDocs."
      :kb      "alt+g alt+c"
      :handler clojuredocs}
   {:name    "command:app:reset-worksheet"
    :desc    "Reset the worksheet - a fresh start."
    :handler [:nav "/reset"]}
   {:name    "command:worksheet:completions"
    :desc    "Show possible auto-completions."
    :kb      ["ctrl+space" "alt+g alt+a"]
    :handler [:worksheet:completions]}])



;; visible commands (used in db)

(defn- command-item
  [item]
  (let [kb (:kb item)
        shortcut (or kb "&nbsp;")]
    (merge item {:text (:desc item)
                 :desc (str "<div class= \"command\" >" (:desc item) "</div><div class= \"command-shortcut\" >" shortcut "</div>")})))


(def visible-commands
  (->> all-commands
       #_(filter #(:showInMenu %))
       (map command-item)
       (into [])))
