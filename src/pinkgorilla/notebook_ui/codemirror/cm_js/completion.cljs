(ns pinkgorilla.notebook-ui.codemirror.cm-js.completion
  (:require
   [taoensso.timbre :refer-macros [info warn]]
   [re-frame.core :refer [dispatch]]
   [cljs.reader]
   [cljs.tools.reader]))

#_(defn cycle-pos
    "Cycle through positions. Returns [active? new-pos].
  count
    total number of completions
  current
    current position
  go-back?
    should we be going in reverse
  initial-active
    if false, then we return not-active when wrapping around"
    [count current go-back? initial-active]
    (if go-back?
      (if (>= 0 current)
        (if initial-active
          [true (dec count)]
          [false 0])
        [true (dec current)])
      (if (>= current (dec count))
        (if initial-active
          [true 0]
          [false 0])
        [true (inc current)])))

#_(defn cycle-completions
    "Cycle through completions, changing the codemirror text accordingly. Returns
  a new state map.
  state
    the current completion state
  go-back?
    whether to cycle in reverse (generally b/c shift is pressed)
  cm
    the codemirror instance
  evt
    the triggering event. it will be `.preventDefault'd if there are completions
    to cycle through."
    [{:keys [num pos active from to list initial-text] :as state}
     go-back? cm evt]
    (when (and state (or (< 1 (count list))
                         (and (< 0 (count list))
                              (not (= initial-text (get (first list) 2))))))
      (.preventDefault evt)
      (let [initial-active (= initial-text (get (first list) 2))
            [active pos] (if active
                           (cycle-pos num pos go-back? initial-active)
                           [true (if go-back? (dec num) pos)])
            text (if active
                   (get (get list pos) 2)
                   initial-text)]
      ;; TODO don't replaceRange here, instead watch the state atom and react to
      ;; that.
        (.replaceRange cm text from to)
        (assoc state
               :pos pos
               :active active
               :to #js {:line (.-line from)
                        :ch (+ (count text)
                               (.-ch from))}))))

(def wordChars
  "[^\\s\\(\\)\\[\\]\\{\\},`']*")

(defn- word-in-line
  [line lno cno]
  (let [back (get (.match (.slice line 0 cno) (js/RegExp. (str wordChars "$"))) 0)
        forward (get (.match (.slice line cno) (js/RegExp. (str "^" wordChars))) 0)]
    {:start #js {:line lno
                 :ch (- cno (count back))}
     :end #js {:line lno
               :ch (+ cno (count forward))}}))

(defn cm-get-range [cm range]
  (.getRange cm (:start range) (:end range)))

(defn- cm-current-word-range
  "Find the current 'word' according to CodeMirror's `wordChars' list"
  [cm]
  (let [pos (.getCursor cm)
        line-no (.-line pos)
        column-no (.-ch pos)
        line-range (.getLine cm line-no)]
    ;; findWordAt doesn't work w/ clojure-parinfer mode
    ;; (.findWordAt cm back)
    ;(info "cm-current-word " "line-no:" line-no "col no:" column-no)
    ;(info "line: " line-range)
    (when line-range
      ;(info "line-range: " line-range)
      (word-in-line line-range line-no column-no))))

(defn cm-current-word
  "Find the current 'word' according to CodeMirror's `wordChars' list"
  [cm]
  (let [word-range (cm-current-word-range cm)]
    (cm-get-range cm word-range)))

(defn cm-replace-current-word
  [cm new-word]
  (let [word-range (cm-current-word-range cm)
        doc (.getDoc cm)
        start (:start word-range)
        end (:end word-range)]
    (info "replacing range " start " - " end " with " new-word)
    (.replaceRange doc new-word
                   (clj->js start)
                   (clj->js end))
    nil))

(defn get-completion-candidates [cm]
  (let [word (cm-current-word cm)]
    (if (empty? word)
      (warn "no current word - cannot get hint!")
      (do
        (info "getting code-completion for word: " word)
        (dispatch [:completion/hint word "user" ""])
        true))))

(defn hint
  "Get a new completion state."
  [{:keys [cm]} evt]
  (info "hint-get .. ")
  (when cm
    (get-completion-candidates cm))
  (.preventDefault evt))


