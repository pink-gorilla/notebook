(ns pinkgorilla.notebook-ui.app-bundel.pages.about
  (:require
   [goog.string :as gstring]
   [goog.string.format]
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]))

(defn credit [user txt]
  [:li
   [:span
    [:a.w-16 {:href (str "https://github.com/" user)} user]
    [:span.ml-5 txt]]])

(defn action [{:keys [on-click href]} text]
  [:div.w-48.h-24.p-5.border-2.border-solid.border-gray-500.rounded.text-center.text-lg.cursor-pointer.bg-pink-100.hover:bg-pink-400
   {:on-click on-click
    :href href}
   (if href
     [:a {:href href} text]
     text)])

(defn block [{:keys [name class]}  & children]
  (into [:div.bg-blue-400.m-5.inline-block
         {:class class}
         [:h1.text-2xl name]]
        children))

(defn svg [url-link url-img p]
  [:a {:href (gstring/format url-link p)}
   [:img.inline-block
    {:src
     (gstring/format url-img p)}]])

(defn project [p]
  [:div
   [:p ; p
    [svg "https://github.com/pink-gorilla/%s/actions?workflow=CI"
     "https://github.com/pink-gorilla/%s/workflows/CI/badge.svg" p]

    [svg "https://codecov.io/gh/pink-gorilla/%s"
     "https://codecov.io/gh/pink-gorilla/%s/branch/master/graph/badge.svg" p]

    [svg "https://clojars.org/org.pinkgorilla/%s"
     "https://img.shields.io/clojars/v/org.pinkgorilla/%s.svg" p]]])

(defn open-source [p]
  [block {:name "open source"
          :class "min-w-min w-2/3"}
   [project "webly"]
   [project "goldly"]
   [project "gorilla-ui"]
   [project "nrepl-middleware"]
   [project "notebook-encoding"]
   [project "gorilla-explore"]
   [project "notebook-ui"]
   [project "lein-pinkgorilla"]
   [project "pinkie"]
   [project "picasso"]
   [project "kernel-cljs-shadow"]])

(defn action-box []
  [:div.mt-5
   [:div.flex.flex-column.justify-evenly
    [action {:on-click #(rf/dispatch [:bidi/goto :ui/explorer])} "Explorer"]
    [action {:on-click #(rf/dispatch [:document/new])} "New Notebook"]
    [action {:on-click #(rf/dispatch [:bidi/goto :ui/nrepl])} "nrepl"]
    [action {:on-click #(rf/dispatch [:bidi/goto :gorilla-ui/example :category "viz"])} "gorilla ui"]
    [action {:on-click #(rf/dispatch [:bidi/goto :goldly/system-list])} "goldly"]
    [action {:on-click #(rf/dispatch [:bidi/goto :ui/markdown :file "webly.md"])} "docs"]
    [action {:href "https://clojurians.zulipchat.com/#narrow/stream/212578-pink-gorilla-dev"} "Zulip Chat"]
    [action {:href "https://github.com/pink-gorilla/gorilla-notebook/issues"} "Ticket"]]
   [action {:on-click #(rf/dispatch [:reframe10x-toggle])} "10x"]])

(defn features-box []
  [:div.bg-yellow-300.mt-5.p-5
   [:h1.text-2xl "Features"]
   [:div.prose
    [:ul.list-disc
     [:li "Notebook with clj kernel"]
     [:li "Many visualization components"]]]])

(defn keybindings-box []
  [:div.bg-yellow-300.mt-5.p-5
   [:h1.text-2xl "Keybindings"]
   [:p "shift enter - eval current cell"]
   [:p "alt-g k - keybinding dialog"]
   [:p "alt-g e - explorer window"]
     ;[:p "alt-g m - main window"]
     ;[:p "alt-g d - docs window"]
     ;[:p "alt-g n - notebook window"]
   [:p "alt-g r - repl info window"]])

(defn nrepl-ip-port []
  (let [config (rf/subscribe [:webly/config])]
    (fn []
      (let [{:keys [bind port]} (get-in @config [:nrepl :server])
            enabled (get-in @config [:nrepl :enabled])]
        (if enabled
          [:span "ip:" bind " : " port]
          [:span "nrepl disabled"])))))

(defn sniffer-box []
  [:div.bg-yellow-300.mt-5.p-5
   [:h1.text-2xl "Eval Sniffer"]
   [:div.prose
    [:ul.list-discs
     [:li "Work in your favorite ide, and build a notebook with gorilla visualisations."]
     [:li "connect your ide to: " [nrepl-ip-port]]
     [:li "Eval :gorilla/on in the repl"]
     [:li "A new notebook appears in /explorer (in unsaved)"]
     [:li "In /nrepl/info you can see the sessions that are currently being sniffed."]
     [:li "To stop sniffing eval :gorilla/off in the repl"]]]])

(defn credits-box []
  [:div.bg-green-300.mt-5.p-5
   [:h1.text-2xl "Credits"]
   [:div.prose
    [:ul.list-disc
     [credit "JonyEpsilon" "legacy gorilla-repl (with js frontend)"]
     [credit "deas" "port to re-frame, nrepl-relay, build-automation, unit-tests"]
     [credit "awb99" "gorilla-ui, goldly"]
     [credit "mauricioszabo" "goldly sci compiler, nrepl-tooling"]
     [credit "daslu" "notworking and testing, clojisr"]]]])

(defn notebook-about []
  [:div.container.mx-auto.gorilla-markdown
   [:h1.mt-5.text-2xl.italic "Welcome to" [:span.pl-5.text-pink-800.text-3xl.non-italic "PinkGorilla Notebook"]]
   [action-box]
   [:div.flex.flex-wrap
    [features-box]
    [keybindings-box]
    [sniffer-box]
    [credits-box]
    [open-source]]])

(defmethod reagent-page :notebook/about [{:keys [route-params query-params handler] :as route}]
  [notebook-about])