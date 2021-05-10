(ns pinkgorilla.notebook-ui.eval-result.error
  (:require
   [pinkie.text :refer [text]]
   [reepl.helpers :as helpers]))

(def styles
  {:error-text {:font-family "monospace"
                :border-style "solid"
                :border-width "1px"
                :border-color "#ff0000"
                :color "#ff0000"
                :clear "both"
                :padding "0.5em 1em 0.5em 1em"
                :margin-bottom "0.3em"
               ;background-color "#faf0f2"
                }})

(def view (partial helpers/view styles))

(defn error-text [err root-ex]
  (when (and err root-ex)
    [view :error-text
     (when err
       [text err])
     (when root-ex
       [text root-ex])]))
