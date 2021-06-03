(ns pinkgorilla.notebook-ui.app.pages.document
  (:require
   [taoensso.timbre :as timbre :refer [info warn]]
   [reagent.core :as r]
   [re-frame.core :as rf :refer [dispatch subscribe]]
   [webly.web.handler :refer [reagent-page]]
   [webly.user.oauth2.view :refer [user-button]]
   [pinkgorilla.document.component :refer [document-page]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]
   [ui.notebook.core :refer [notebook-view]]
   [ui.notebook.tooltip :refer [with-tooltip]]
   [ui.notebook.menu]
   [pinkgorilla.notebook-ui.app.site :as site]))

(rf/dispatch [:css/set-theme-component :codemirror "mdn-like"])

(defn header-ico [fa-icon rf-dispatch]
  [:a {:on-click #(dispatch rf-dispatch)
       :class "hover:text-blue-700 mr-1"}
   [:i {:class (str "fa-lg pl-1 " fa-icon)}]])

(defn header-icon [fa-icon rf-dispatch text]
  [with-tooltip text [header-ico fa-icon rf-dispatch]])

;; NOTEBOOK

(defn nrepl-icon []
  (let [connected? (subscribe [:nrepl/connected?])]
    (fn []
      (if @connected?
        [:span.text-green-500 [header-icon "fas fa-circle"  [:bidi/goto :ui/nrepl] "nrepl connected"]]
        [:span.text-red-500 [header-icon "fas fa-circle" [:bidi/goto :ui/nrepl] "nrepl disconnected"]]))))

(defn header-menu-left [storage doc]
  [:<>
   (warn "storage: " storage "doc id:" (get-in doc [:meta :id]))
   ;[header-icon "fa fa-question-circle" [:bidi/goto :notebook/about] "notebook/about main page"]
   [header-icon "fa fa-th-large" [:bidi/goto :ui/explorer] "notebook explorer"]
   [ui.notebook.menu/menu]
   [header-icon "fa fa-save" [:document/save (get-in doc [:meta :id]) storage]  "save document"]
   [header-icon "fa fa-stream" [:palette/show] "shows keybindings and commands"]
   [nrepl-icon]
   [user-button :github]])

(def opts
  {; if a layout option is passed this will override the settings in localstorage
   ;:layout :single ; :vertical ; :horizontal
   :view-only false})

(defn document-viewer [storage doc]
  (let [aid (r/atom nil)]
    (fn [storage doc]
      (let [id (get-in doc [:meta :id])]
        (when (not (= @aid id))
          (info "kernel activate doc id: " id)
          ;(rf/dispatch [:doc/load doc])
          (rf/dispatch [:doc/doc-active (get-in doc [:meta :id])])
          (swap! aid id))
        [:div.h-full.min-h-full.w-full  ;:div.m-3 .bg-blue-300
         [notebook-view opts]]))))

(defmethod reagent-page :ui/notebook [{:keys [route-params query-params handler] :as route}]
  [document-page document-viewer header-menu-left query-params])

(defmethod reagent-page :ui/notebook-welcome [{:keys [route-params query-params handler] :as route}]
  (let [query-params {:type :res
                      :filename "notebook/notebook-welcome.cljg"
                      ;:type :file
                      ;:filename "resources/notebooks/notebook/notebook-welcome.cljg"
                      }]
    [document-page document-viewer header-menu-left query-params]))

(defmethod reagent-page :ui/notebook-current [{:keys [route-params query-params handler] :as route}]
  (let [nb (rf/subscribe [:notebook/current])
        query-params {:type :res
                      :filename "notebook/clojure/multimethods.cljg"}]
    (fn [{:keys [route-params query-params handler] :as route}]

      (goto-notebook! {:id @nb})
      [:div "going to curret notebook"]
    ;[document-page document-viewer header-menu-left query-params]
      )))

#_(defn document-page [document-view header-menu-left params]
    (debugf "rendering document-page params: %s" params)
    (let [{:keys [storage id]} (get-storage params)]
      (warn "storage: " storage "id: " id)
      (if storage
        [doc-load document-view header-menu-left storage]
        [show-id  document-view header-menu-left nil id])))