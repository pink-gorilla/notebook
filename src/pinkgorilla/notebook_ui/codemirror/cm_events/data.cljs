(ns pinkgorilla.notebook-ui.codemirror.cm-events.data
  (:require
   [taoensso.timbre :refer-macros [debugf info error]]
   [pinkgorilla.notebook-ui.editor.data :refer [save-data-debounced]]))

(defn move-to-last-line [cm]
  (let [last-line (.lastLine cm)
        last-ch (count (.getLine cm last-line))]
    (.setCursor cm last-line last-ch)))

; load/save from/to buffer

(defn editor-load-content [editor content]
  (.setValue editor content)
      ;(move-to-last-line @cm)
  )

(defn on-change [{:keys [cm id] :as context} sender evt]
  (let [code (.getValue cm)]
    (debugf "saving cm text to reframe segment: %s" id)
    (save-data-debounced :code id code)))

