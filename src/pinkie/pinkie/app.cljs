(ns pinkie.app
  "entry point to pinkie renderer (that works with notespace)"
  (:require
   [reagent.core :as r]
   [reagent.dom]
   [re-frame.core :refer [reg-event-db dispatch-sync dispatch]]
   [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf)]
   ; pinkgorilla libraries   
   [pinkgorilla.notebook.core :refer [empty-notebook
                                      insert-segment-at remove-segment
                                      create-code-segment create-free-segment]] ; manipulate notebook
   ; pinkgorilla current project
   [pinkgorilla.components.markdown :refer [markdown]]
   [pinkgorilla.worksheet.core :refer [worksheet]]
   [pinkgorilla.events] ; register event handlers
   [pinkgorilla.subs] ; register subscriptions
   [pinkgorilla.codemirror.core :as editor]
   [pinkgorilla.views.renderer :refer [renderer]]

   [pinkgorilla.config.notebook] ; bring pinkie renderers into scope
   [pinkie.notebook] ; add pinkie event handlers to reframe
   
   [pinkie.ws]))

(timbre/set-level! :debug)
;(timbre/set-level! :info)
(enable-console-print!)

(defn- log [a-thing]
  (.log js/console a-thing))

(defn console! [segment output]
  (assoc-in segment [:console-response] output))


(defn value-html! [segment content]
  (assoc-in segment [:value-response] {:type :html
                                       :content content}))

(defn value-reagent! [segment hiccup]
  (assoc-in segment [:value-response] {:type :reagent
                                       :content {:hiccup hiccup
                                                 :map-keywords true
                                                 :widget true}}))

(defn create-dummy-notebook []
  (let [worksheet (empty-notebook)
        md-howto (create-free-segment "# Pinkie Renderer \n\nRender visuals from clj.")
        code-add (-> (create-code-segment "(+ 1 1 )")
                     (console! "the calculation was performed")
                     (value-html! [:span.clj-long 2]))
        code-vec (-> (create-code-segment "[ 1 2 3]")
                     (value-html! [:span.clj-long "[ 1 2 3]"]))
        code-h   (-> (create-code-segment "^:R [:h1 \"hello world\"] ")
                     (value-reagent! [:h1 "hello world!"]))
        code-r   (-> (create-code-segment "^:R [:json {:a 1 :b 2}]")
                     (value-reagent! [:json {:a 1 :b 2}]))]
    (-> worksheet
        (insert-segment-at 0 md-howto)
        ;(insert-segment-at 1 code-add)
        ;(insert-segment-at 2 code-vec)
        ;(insert-segment-at 3 code-h)
        ;(insert-segment-at 4 code-r)
        )))


(def initial-db
  {:storage nil
   :config {:read-only true} ; important for app-db spec checks
   :docs {:content  "" :position []} ; important for app-db spec checks
   :worksheet (create-dummy-notebook)})

(reg-event-db
 :initialize-pinkie
 (fn [_ [_ _]]
   (println "initializing pinkie-db ..")
   initial-db))


(def show-renderer (r/atom false))

(defn toggle-renderer []
  (swap! show-renderer not))


(defn ^:export pinkie-app []
  (let [read-write true
        editor-options {}]
    [:<>
     ;[:script {:type "text/javascript"
      ;         :src "https://cdnjs.cloudflare.com/ajax/libs/require.js/2.3.6/require.min.js"}]
      ;<script type="text/javascript" src='/js/requireconfig.js'></script>


     [:h1 [:a {:on-click toggle-renderer :style {:border "solid" :padding 10 :margin 10}} 
           (if @show-renderer "hide renderer" "show renderer")]]
     (when @show-renderer
       [renderer])
     [markdown "## render your favorite visuals"]
     [worksheet read-write editor-options]]))

(defn mount-root
  []
  (reagent.dom/render [pinkie-app] (.getElementById js/document "app")))


;; CSRF check

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

;; see: https://github.com/ptaoussanis/sente/blob/master/example-project/src/example/client.cljs

(defn sente-csrf-warning []
  (if ?csrf-token
    (println "CSRF token detected in HTML, great!")
    (println "CSRF token NOT detected in HTML, default Sente config will reject requests")))


(defn ^:export start! []
  (println "pinkie.app/start!")
  (sente-csrf-warning)
  (editor/configure-cm-globally!)
  (dispatch-sync [:initialize-pinkie])
  (mount-root)
  (pinkie.ws/start-router!)
  (pinkie.ws/send! [:pinkie/a {:a 12}])
  )


(comment 
 (start!) 
  
  )

