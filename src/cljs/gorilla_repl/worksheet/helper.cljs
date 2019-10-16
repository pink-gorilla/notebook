(ns gorilla-repl.worksheet.helper
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [goog.dom :as gdom]
   
   [dommy.core :as dom :refer-macros [sel1]]
   [gorilla-repl.editor :as editor]
   ;[gorilla-repl.output.mathjax :refer [queue-mathjax-rendering]]
   ;[gorilla-repl.output.hack :refer [value-wrap]]
   ))

(defn init-cm!
  [segment-id content-type opts seg-comp]
  (let [text-area (-> (reagent/dom-node seg-comp)
                      (sel1 :textarea))]
    (editor/create-editor! text-area
                           :content-type content-type
                           :segment-id segment-id
                           :opts opts)))


;; TODO: Should only fire when we are active!
(defn focus-active-segment
  [component active]
  (let [el (reagent/dom-node component)
        ;; TODO : Dedupe and move to editor
        cm-el (gdom/getElementByClass "CodeMirror" el)
        cm (and active (if cm-el (.-CodeMirror cm-el) nil))]
    (if cm
      (.focus cm))))

(defn error-text [text]
  [:div.error-text text])

(defn console-text [txt]
  [:div.console-text txt])


(defn exception [e]
  (let [header (if (:cause e) "An exception was caused by: " "Exception thrown")
        ex (if (:cause e) (:cause e) e)
        frame-components (map-indexed (fn [idx frame]
                                        (let [type (get frame "type")
                                              tooling (.indexOf (get frame "flags") "tooling")
                                              li-classes (str type (if tooling " tooling-stackframe"))]
                                          (if (= type "clj")
                                            ^{:key idx}
                                            [:li {:class li-classes}
                                             ^{:key "fn"} [:span (get frame "fn")]
                                             ^{:key "ns"} [:span {:class "stackframe-ns"} (get frame "ns")]
                                             ^{:key "fileloc"} [:span {:class "stackframe-fileloc"}
                                                                (str " - ("
                                                                     (get frame "file")
                                                                     ":"
                                                                     (get frame "line")
                                                                     ")")]]
                                            ^{:key idx}
                                            [:li {:class li-classes}
                                             ^{:key "method"} [:span (get frame "method")]
                                             ^{:key "fileloc"} [:span {:class "stackframe-fileloc"}
                                                                (str " - ("
                                                                     (get frame "file")
                                                                     ":"
                                                                     (get frame "line")
                                                                     ")")]])))
                                      (get ex "stacktrace"))]
    [:div {:class "stack-trace"}
     [:div.exception
      [:div {:class "exception-header"} header]
      [:span (get ex "class")]
      [:span (get ex "class")]]
     [:ul frame-components]]))


(defn colorize-cm!
  [seg-comp]
  (let [pre (-> (reagent/dom-node seg-comp)
                (sel1 :pre))]
    (.colorize js/CodeMirror #js [pre] "text/x-clojure")))

