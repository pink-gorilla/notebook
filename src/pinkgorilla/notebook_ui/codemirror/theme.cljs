(ns pinkgorilla.notebook-ui.codemirror.theme
  (:require
   [re-frame.core :refer [subscribe]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]))

(def themes
  ["default" "3024-day" "3024-night" "abcdef" "ambiance" "ayu-dark" "ayu-mirage"
   "base16-dark" "base16-light" "bespin" "blackboard" "cobalt" "colorforth"
   "darcula" "dracula" "duotone-dark" "duotone-light" "eclipse" "elegant"
   "erlang-dark" "gruvbox-dark" "hopscotch" "icecoder" "idea" "isotope"
   "lesser-dark" "liquibyte" "lucario" "material" "material-darker"
   "material-palenight" "material-ocean" "mbo" "mdn-like" "midnight" "monokai"
   "moxer" "neat" "neo" "night" "nord" "oceanic-next" "panda-syntax"
   "paraiso-dark" "paraiso-light" "pastel-on-dark" "railscasts" "rubyblue"
   "seti" "shadowfox" "solarized dark" "solarized light" "the-matrix"
   "tomorrow-night-bright" "tomorrow-night-eighties" "ttcn" "twilight"
   "vibrant-ink" "xq-dark" "xq-light" "yeti" "yonce" "zenburn"])

;(def themes (->> theme-names (mapv vector (map keyword theme-names)) (into {})))


;; styles for theme

(defn style-codemirror-inline []
  ; Set height, width, borders, and global font properties here
  ; height: 200px;
  ; height: auto;    auto = adjust height to fit content 
  ; height: 100%
  [:style ".my-codemirror > .CodeMirror { 
              font-family: monospace;
              height: auto;
            }"])

(defn style-codemirror-fullscreen []
  ; height: auto; "400px" "100%"  height: auto;
  [:style ".my-codemirror > .CodeMirror { 
              font-family: monospace;
              height: 100% ;
            }"])

#_(defn styles-codemirror-for-setttings [layout]
    [:div
     (if (= layout :single)
       style-codemirror-fullscreen
       style-codemirror-inline)])

