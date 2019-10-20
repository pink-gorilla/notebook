(ns gorilla-repl.views
  (:require [clojure.string :as str]
    ;; [hickory.core :as hick]
            [gorilla-repl.webpack-include]
            [gorilla-repl.webpack-extern]
            [gorilla-repl.editor :as editor]
            [cljs-uuid-utils.core :as uuid]
    ;; [clojure.walk :as w]
            [cljsjs.marked]
    ;; TODO : vega 2.6 does  not quite work yet - throw spec at http://vega.github.io/vega-editor/?mode=vega
    ;; https://github.com/vega/vega/wiki/Upgrading-to-2.0
    ;; data. prefix removed
            ;[cljsjs.d3]    2019-10-20 awb99 removed as it fucks up the new vega
            ;[cljsjs.d3geo]  2019-10-20 awb99 removed as it fucks up the new vega
            ;[cljsjs.vega] 2019-10-20 awb99 removed as it fucks up the new vega
            [gorilla-repl.subs]
            [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [re-com.core :as re-com]
            [goog.dom :as gdom]
            [dommy.core :as dom :refer-macros [sel1]]
            [taoensso.timbre :as timbre
             :refer-macros (log trace debug info warn error fatal report
                                logf tracef debugf infof warnf errorf fatalf reportf
                                spy get-env log-env)]


            [gorilla-repl.output.hack :refer [temp-comp-hack]]
            [gorilla-repl.output.mathjax :refer [queue-mathjax-rendering]]
            [gorilla-repl.output.core :refer [output-fn]]

            [gorilla-repl.dialog.save :refer [save-dialog]]
            [gorilla-repl.dialog.palette :refer [palette-dialog]]

            [gorilla-repl.worksheet.core :refer [worksheet]]

            ;widgets are only included here so they get compiled to the bundle.js
            [widget.hello]))



;; <!--script src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
;; <script type="text/x-mathjax-config">
;; MathJax.Hub.Config({
;;                    tex2jax: {
;;                              inlineMath: [['$','$']],
;;                              processClass: "mathjax",
;;                              ignoreClass: "no-mathjax"
;;                              }
;;                    });
;; </script-->


;; TODO: MathJax does not kick in with advanced optimization
(defn init-mathjax-globally!
  "Initialize MathJax globally"
  []
  (if-let [mathjax (.-MathJax js/window)]
    (doto (.-Hub mathjax)
      (.Config (clj->js {:messageStyle           "none"
                         :showProcessingMessages false
                         :skipStartupTypeset     true
                         :tex2jax                {:inlineMath (clj->js [["@@", "@@"]])}}))
      (.Configured))
    (print "MathJax unavailable")))


;; Components
(defn hamburger []
  [:div.menu-icon {:on-click                #(dispatch [:app:commands])
                   :dangerouslySetInnerHTML {:__html "&#9776;"}}])

(defn doc-viewer
  []
  (let [docs (subscribe [:docs])]
    (reagent/create-class
      {;; :component-did-mount  (fn [this])
       :display-name         "doc-viewer"                   ;; for more helpful warnings & errors
       ;; :component-will-unmount (fn [this])
       :component-did-update (fn [this old-argv]
                               ;; TODO Ugly, but a bit more tricky to get right in reagent-render/render
                               (if-let [hint-el (gdom/getElementByClass "CodeMirror-hints")]
                                 (let [rect (.getBoundingClientRect hint-el)
                                       el (reagent/dom-node this)
                                       top (.-top rect)
                                       right (.-right rect)]
                                   (dom/set-px! el :top top :left right)
                                   ;;
                                   ;; (set! (.-top el-style) top)
                                   ;; (set! (.-left el-style) right)
                                   )))
       :reagent-render
                             (fn []
                               [:div.DocViewer.doc-viewer {:style (if (str/blank? (:content @docs))
                                                                    {:display "none"} {})}
                                [:div.DocViewer.doc-viewer-content {}
                                 [:div {
                                        :dangerouslySetInnerHTML {:__html (:content @docs)}}]]])})))



(defn app-status
  []
  (let [message (subscribe [:message])]
    (fn []
      [:div.status {:style (if (str/blank? @message) {:display "none"} {})}
       [:h3 @message]])))







(defn gorilla-app
  []
  (let [config (subscribe [:config])]
    (reagent/create-class
      {:display-name   "gorilla-app"
       :reagent-render (fn []
                         (let [container [:div.Gorilla {}]
                               rw (not (:read-only @config))
                               hamburger-comp (if rw ^{:key :hamburger} [hamburger])
                               palette-comp (if rw ^{:key :palette-dialog} [palette-dialog])
                               save-comp (if rw ^{:key :save-dialog} [save-dialog])
                               doc-comp (if rw ^{:key :doc-viewer} [doc-viewer])
                               other-children [^{:key :status} [app-status]
                                               hamburger-comp
                                               palette-comp
                                               save-comp
                                               doc-comp
                                               ^{:key :worksheet} [worksheet
                                                                   rw
                                                                   (get-in @config [:project :gorilla-options :editor] {})]
                                               ]]
                           (into container (filter some? other-children))))})))
