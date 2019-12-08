(ns pinkgorilla.views
  (:require
   [clojure.string :as str]
      ;; [hickory.core :as hick]
      ;; [clojure.walk :as w]
   
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :as rf :refer [subscribe dispatch dispatch-sync]]
   [re-com.core :as re-com]
   [goog.dom :as gdom]
   [dommy.core :as dom :refer-macros [sel1]]
   [taoensso.timbre :as timbre
    :refer-macros (log trace debug info warn error fatal report
                       logf tracef debugf infof warnf errorf fatalf reportf
                       spy get-env log-env)]

   [cljs-uuid-utils.core :as uuid]
   [cljsjs.marked]
      ;; TODO : vega 2.6 does  not quite work yet - throw spec at http://vega.github.io/vega-editor/?mode=vega
      ;; https://github.com/vega/vega/wiki/Upgrading-to-2.0
      ;; data. prefix removed
      ;[cljsjs.d3]    2019-10-20 awb99 removed as it fucks up the new vega
      ;[cljsjs.d3geo]  2019-10-20 awb99 removed as it fucks up the new vega
      ;[cljsjs.vega] 2019-10-20 awb99 removed as it fucks up the new vega
   
   [pinkgorilla.subs :as s]
   [pinkgorilla.editor.core :as editor]
   [pinkgorilla.output.hack :refer [temp-comp-hack]]
   [pinkgorilla.output.mathjax :refer [queue-mathjax-rendering]]
   [pinkgorilla.output.core :refer [output-fn]]
   [pinkgorilla.dialog.save :refer [save-dialog]]
   [pinkgorilla.dialog.palette :refer [palette-dialog]]
   [pinkgorilla.dialog.settings :refer [settings-dialog]]
   [pinkgorilla.dialog.meta :refer [meta-dialog]]
   [pinkgorilla.worksheet.core :refer [worksheet]]
   [pinkgorilla.storage.core]
   [pinkgorilla.views.navbar :as navbar]
   [pinkgorilla.notifications :as notifications]
   
      ;widgets are only included here so they get compiled to the bundle.js
   [widget.hello]

   [pinkgorilla.explore.list]))

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
          [:div {:dangerouslySetInnerHTML {:__html (:content @docs)}}]]])})))



(defn app-status
  []
  (let [message (subscribe [:message])]
    (fn []
      [:div.status {:style (if (str/blank? @message) {:display "none"} {})}
       [:h3 @message]])))


(defn gorilla-app-doc
  []
  (let [config (subscribe [:config])]
    (reagent/create-class
     {:display-name   "gorilla-app"
      :reagent-render (fn []
                        (let [container [:div.Gorilla {}]
                              rw (not (:read-only @config))
                              hamburger-comp (if rw ^{:key :hamburger} [hamburger])
                              palette-comp (if rw ^{:key :palette-dialog} [palette-dialog])
                              ;save-comp (if rw ^{:key :save-dialog} [save-dialog])
                              ;settings-comp (if rw ^{:key :settings-dialog} [settings-dialog])
                              ;meta-comp (if rw ^{:key :meta-dialog} [meta-dialog])
                              doc-comp (if rw ^{:key :doc-viewer} [doc-viewer])
                              other-children [^{:key :status} [app-status]
                                              hamburger-comp
                                              palette-comp
                                              ;save-comp
                                              ;settings-comp
                                              ;meta-comp
                                              doc-comp
                                              ^{:key :worksheet} [worksheet
                                                                  rw
                                                                  (get-in @config [:project :gorilla-options :editor] {})]]]
                          (into container (filter some? other-children))))})))



(defn gorilla-app
  []
  (let [main (subscribe [:main])]
    [:<>
     (when @(rf/subscribe [::s/navbar-visible?])
       [navbar/navbar-component])
     [notifications/notifications-container-component]
     
     [meta-dialog]
     [settings-dialog]
     [save-dialog]
     
     (case @main
       :explore [pinkgorilla.explore.list/view]
       :notebook [gorilla-app-doc]
       [gorilla-app-doc])]))