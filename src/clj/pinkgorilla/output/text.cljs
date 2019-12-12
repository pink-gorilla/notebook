(ns pinkgorilla.output.text
  (:require
   [clojure.string :as string]
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.output.html :refer [output-html]]))

(defn line-with-br [t]
  [:div
   [:span.font-mono.text-lg.whitespace-pre t]
   [:br]])

(defn text [t]
  (let [lines (string/split t #"\n")]
    (into [:div {:gorilla_ui "text"}] (map line-with-br lines))))

(defn output-text
  [output seg-id]
  (if-let [content (:content output)]
    (let [h (text (:text content))]
      (info "rendering text: " content "h: " h)
      (output-html {:content h} seg-id))))