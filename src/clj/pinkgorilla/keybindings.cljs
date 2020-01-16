(ns pinkgorilla.keybindings
  (:require
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
  (if-let [mousetrap (when (and (exists? js/window) (.-Mousetrap js/window))
                       (.-Mousetrap js/window))]
    (do
      (set! (.-enabled mousetrap) true)
      (set! (.-enable mousetrap) (fn [x] (set! (.-enabled mousetrap) x)))
      (set! (.-stopCallback mousetrap) #(not (.-enabled mousetrap)))
      (doall (map (partial kb-bind mousetrap) command-keymap)))))



;; TODO: What was actually wrong with clojuredocs?


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

;; Moved to default.edn
;; (def all-commands  [])

;; visible commands (used in db)
(defn- command-item
  [item]
  (let [kb (:kb item)
        shortcut (or kb "&nbsp;")]
    (merge item {:text (:desc item)
                 :desc (str "<div class= \"command\" >" (:desc item) "</div><div class= \"command-shortcut\" >" shortcut "</div>")})))

(defn visible-commands
  [commands]
  (->> commands
       #_(filter #(:showInMenu %))
       (map command-item)
       (into [])))
