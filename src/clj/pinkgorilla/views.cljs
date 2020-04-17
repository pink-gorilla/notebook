(ns pinkgorilla.views
  (:require
   [clojure.string :as str]
   [reagent.core :as reagent]
   [reagent.dom]
   [re-frame.core :as rf :refer [subscribe dispatch]]
   [goog.dom :as gdom]
   [dommy.core :as dom]
   [pinkgorilla.components.markdown]
   [pinkgorilla.subs :as s]
   [pinkgorilla.dialog.save :refer [save-dialog]]
   [pinkgorilla.dialog.palette :refer [palette-dialog]]
   [pinkgorilla.dialog.settings :refer [settings-dialog]]
   [pinkgorilla.dialog.meta :refer [meta-dialog]]
   [pinkgorilla.worksheet.core :refer [worksheet]]
   [pinkgorilla.storage.core]
   [pinkgorilla.views.navbar :refer [navbar-component]]
   [pinkgorilla.dialog.notifications :refer [notifications-container-component message-container]]
   [pinkgorilla.explore.core :refer [notebook-explorer]]
   [pinkgorilla.views.renderer :refer [renderer]]))

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
      :component-did-update (fn [this _] ; old_argv
                               ;; TODO Ugly, but a bit more tricky to get right in reagent-render/render
                              (if-let [hint-el (gdom/getElementByClass "CodeMirror-hints")]
                                (let [rect (.getBoundingClientRect hint-el)
                                      el (reagent.dom/dom-node this)
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

(defn gorilla-app-doc
  []
  (let [config (subscribe [:config])]
    (reagent/create-class
     {:display-name   "gorilla-app"
      :reagent-render (fn []
                        (let [container [:div.Gorilla {}]
                              rw (not (:read-only @config))
                              ;; hamburger-comp (when rw ^{:key :hamburger} [hamburger])
                              palette-comp (when rw ^{:key :palette-dialog} [palette-dialog])
                              ;save-comp (when rw ^{:key :save-dialog} [save-dialog])
                              ;settings-comp (when rw ^{:key :settings-dialog} [settings-dialog])
                              ;meta-comp (when rw ^{:key :meta-dialog} [meta-dialog])
                              doc-comp (when rw ^{:key :doc-viewer} [doc-viewer])
                              other-children [^{:key :status} [message-container]
                                              ;; hamburger-comp
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
       [navbar-component])
     [notifications-container-component]
     [meta-dialog]
     [settings-dialog]
     [save-dialog]

     (case @main
       :explore [notebook-explorer]
       :notebook [gorilla-app-doc]
       :renderer [renderer]
       [gorilla-app-doc])]))
