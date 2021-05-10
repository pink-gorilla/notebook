(ns pinkgorilla.notebook-ui.eval-result.code-viewer
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   ["highlight.js/lib/core" :as hljs]
   ["highlight.js/lib/languages/javascript" :as javascript]
   ["highlight.js/lib/languages/markdown" :as markdown]
   ["highlight.js/lib/languages/plaintext" :as plaintext]
   ["highlight.js/lib/languages/clojure" :as clojure]
   ["highlight.js/lib/languages/clojure-repl" :as clojure-repl]))

(.registerLanguage hljs "javascript" javascript)
(.registerLanguage hljs "markdown" markdown)
(.registerLanguage hljs "plaintext" plaintext)
(.registerLanguage hljs "clojure" clojure)
(.registerLanguage hljs "clojure-repl" clojure-repl)


;; todo https://github.com/baskeboler/cljs-karaoke-client/blob/master/package.json
;;  "react-highlight.js": "^1.0.7",

;(.initHighlightingOnLoad hljs)

; nicely formatted notebooks:
; https://nextjournal.com/gigasquid/parens-for-python---seaborn-visualizations
; https://www.maria.cloud/gallery?eval=true

; https://codepen.io/elomatreb/pen/hbgxp

; span {
;    display: block;
;    line-height: 1.5rem;
;    
;    &:before {
;      counter-increment: line;
;      content: counter(line);
;      display: inline-block;
;      border-right: 1px solid #ddd;
;      padding: 0 .5em;
;      margin-right: .5em;
;      color: #888
;    }


(def themes-main
  ["default"
   "github"
   "zenburn"])

(def themes
  ["default"
   "zenburn"
   "vs2015"
   "github-gist"
   "github"
   "googlecode"
   "codepen-embed"
   "xcode"
   "vs"

   "a11y-dark"
   "atelier-seaside-dark"
   "gruvbox-light"
   "purebasic"
   "a11y-light"
   "atelier-seaside-light"
   "hopscotch"
   "qtcreator_dark"
   "agate"
   "atelier-sulphurpool-dark"
   "hybrid"
   "qtcreator_light"
   "androidstudio"
   "atelier-sulphurpool-light"
   "idea"
   "railscasts"
   "an-old-hope"
   "atom-one-dark"
   "ir-black"
   "rainbow"
   "arduino-light"
   "atom-one-dark-reasonable"
   "isbl-editor-dark"
   "routeros"
   "arta"
   "atom-one-light"
   "isbl-editor-light"
   "school-book"
   "ascetic"
   "brown-paper"
   "kimbie.dark"
   "shades-of-purple"
   "atelier-cave-dark"
   "kimbie.light"
   "solarized-dark"
   "atelier-cave-light"
   "color-brewer"
   "lightfair"
   "solarized-light"
   "atelier-dune-dark"
   "darcula"
   "lioshi"
   "srcery"
   "atelier-dune-light"
   "dark"
   "magula"
   "sunburst"
   "atelier-estuary-dark"
   "mono-blue"
   "tomorrow"
   "atelier-estuary-light"
   "docco"
   "monokai"
   "tomorrow-night-blue"
   "atelier-forest-dark"
   "dracula"
   "monokai-sublime"
   "tomorrow-night-bright"
   "atelier-forest-light"
   "far"
   "night-owl"
   "tomorrow-night"
   "atelier-heath-dark"
   "foundation"
   "nnfx"
   "tomorrow-night-eighties"
   "atelier-heath-light"
   "nnfx-dark"
   "atelier-lakeside-dark"
   "nord"
   "atelier-lakeside-light"
   "gml"
   "obsidian"
   "atelier-plateau-dark"
   "ocean"
   "xt256"
   "atelier-plateau-light"
   "gradient-dark"
   "paraiso-dark"
   "atelier-savanna-dark"
   "grayscale"
   "paraiso-light"
   "atelier-savanna-light"
   "gruvbox-dark"
   "pojoaque"])

(defn code-view-code [code]
  [:pre ;.clojure
   [:code {:ref  #(when % (.highlightBlock hljs %))}
    ;.w-full.font-mono
    ;[:p
    code
    ; ]
    ]])

(defn code-view [{:keys [code] :as eval-result}]
  [code-view-code code] ; minimize re-frame re-renders
  )

