(ns gorilla-repl.events
  (:require
    [gorilla-repl.db :as db :refer [initial-db]]
    [gorilla-repl.worksheet-parser]
    [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
    [gorilla-repl.editor :as editor]
    [gorilla-repl.nrepl-kernel :as nrepl]
    [gorilla-repl.routes :as routes]
    [cljsjs.mousetrap]
    [cljsjs.mousetrap-global-bind]
    [cljs.spec :as s]
    ;; [cemerick.url :as url]
    [clojure.string :as str]
    [goog.crypt.base64 :as b64]
    [clojure.walk :as w]
    [day8.re-frame.http-fx]
    [day8.re-frame.undo :as undo :refer [undoable]]
    [taoensso.timbre :as timbre
     :refer-macros (log trace debug info warn error fatal report
                        logf tracef debugf infof warnf errorf fatalf reportf
                        spy get-env log-env)]
    [ajax.core :as ajax :refer [GET POST]]))

(defn text-matches-re
  [val item]
  (let [res (str/join ".*" (str/split (str/lower-case val) #""))
        re (re-pattern (str res ".*"))]
    (re-matches re (str/lower-case (:text item)))))

(def hip-adjective ["affectionate" "amiable" "arrogant" "balmy" "barren" "benevolent"
                    "billowing" "blessed" "breezy" "calm" "celestial" "charming" "combative"
                    "composed" "condemned" "divine" "dry" "energized" "enigmatic" "exuberant"
                    "flowing" "fluffy" "fluttering" "frightened" "fuscia" "gentle" "greasy"
                    "grieving" "harmonious" "hollow" "homeless" "icy" "indigo" "inquisitive"
                    "itchy" "joyful" "jubilant" "juicy" "khaki" "limitless" "lush" "mellow"
                    "merciful" "merry" "mirthful" "moonlit" "mysterious" "natural" "outrageous"
                    "pacific" "parched" "placid" "pleasant" "poised" "purring" "radioactive"
                    "resilient" "scenic" "screeching" "sensitive" "serene" "snowy" "solitary"
                    "spacial" "squealing" "stark" "stunning" "sunset" "talented" "tasteless"
                    "teal" "thoughtless" "thriving" "tranquil" "tropical" "undisturbed" "unsightly"
                    "unwavering" "uplifting" "voiceless" "wandering" "warm" "wealthy" "whispering"
                    "withered" "wooden" "zealous"])

(def hip-nouns ["abyss" "atoll" "aurora" "autumn" "badlands" "beach" "briars" "brook" "canopy"
                "canyon" "cavern" "chasm" "cliff" "cove" "crater" "creek" "darkness" "dawn"
                "desert" "dew" "dove" "drylands" "dusk" "farm" "fern" "firefly" "flowers" "fog"
                "foliage" "forest" "galaxy" "garden" "geyser" "grove" "hurricane" "iceberg" "lagoon"
                "lake" "leaves" "marsh" "meadow" "mist" "moss" "mountain" "oasis" "ocean" "peak"
                "pebble" "pine" "plateau" "pond" "reef" "reserve" "resonance" "sanctuary" "sands"
                "shelter" "silence" "smokescreen" "snowflake" "spring" "storm" "stream" "summer"
                "summit" "sunrise" "sunset" "sunshine" "surf" "swamp" "temple" "thorns" "tsunami"
                "tundra" "valley" "volcano" "waterfall" "willow" "winds" "winter"])

(defn make-hip-nsname
  []
  (let [adj-index (-> (* (count hip-adjective) (rand)) js/Math.floor)
        noun-index (-> (* (count hip-nouns) (rand)) js/Math.floor)]
    (str (get hip-adjective adj-index) "-" (get hip-nouns noun-index))))

(defn default-error-handler
  [{:keys [status status-text]}]
  (dispatch [:process-error-response status status-text]))

;; http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj
;; http://localhost:3449/worksheet.html#/view?source=gist&id=2c210794185e9d8c0c80564db581b136&filename=new-render.clj


;; -- the register of event handlers --------------------------------------------------------------

;; -- Interceptors  --------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/wiki/Using-Handler-Middleware
;;

(defn check-and-throw
  "throw an exception if db doesn't match the spec."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

;; Event handlers change state, that's their job. But what happens if there's
;; a bug which corrupts db state in some subtle way? This interceptor is run after
;; each event handler has finished, and it checks app-db against a spec.  This
;; helps us detect event handler bugs early.
(def check-spec-interceptor (after (partial check-and-throw :gorilla-repl.db/db)))

;; the chain of interceptors we use for all handlers that manipulate todos
(def standard-interceptors [(when db/debug? debug)          ;; look in your browser console for debug logs
                            check-spec-interceptor          ;; ensure the spec is still valid
                            #_trim-v])                      ;; removes first (event id) element from the event vec

;; -- Helpers -----------------------------------------------------------------

#_(defn allocate-next-id
    "Returns the next todo id.
    Assumes todos are sorted.
    Returns one more than the current largest id."
    [todos]
    ((fnil inc 0) (last (keys todos))))


(def install-commands
  (re-frame.core/->interceptor
    :id :install-commands
    :after (fn
             [context]
             (db/install-commands (get-in context [:coeffects :db :all-commands]))
             context)))

;; TODO : Remove all the side effects from the handlers
;; -- Event Handlers ----------------------------------------------------------
(reg-event-db
  :process-config-response
  [install-commands]
  (fn [db [_ response]]
    (-> (assoc-in db [:config] response)
        (assoc :message nil))))


(defn- view-db
  [app-url]
  (let [base-path (str/replace (:path app-url) #"[^/]+$" "")
        db (merge initial-db {:base-path base-path})]
    db))


(reg-event-db
  :initialize-view-db
  (fn [_ [_ app-url]]
    (view-db app-url)))

(reg-event-db
  :initialize-new-worksheet
  (fn [db _]
    (let [init-free-segment
          (db/create-free-segment
            (str "# Gorilla REPL\n\nWelcome to gorilla :-)\n\nShift + enter evaluates code. "
                 "Hit " (db/ck) "+g twice in quick succession or click the menu icon (upper-right corner) "
                 "for more commands ...\n\nIt's a good habit to run each worksheet in its own namespace: feel "
                 "free to use the declaration we've provided below if you'd like."))
          init-code-segment
          (db/create-code-segment
            (str "(ns " (make-hip-nsname) "\n  (:require [gorilla-plot.core :as plot]))"))
          worksheet {:ns                   nil
                     :segments             {}
                     :segment-order        []
                     :queued-code-segments #{}
                     :active-segment       nil}]
      (assoc db :worksheet (-> worksheet
                               (db/insert-segment-at 0 init-free-segment)
                               (db/insert-segment-at 1 init-code-segment))))))



(defn display-message
  [db [_ message timeout]]
  (if timeout
    (js/setTimeout #(dispatch [:display-message nil]) timeout))
  (merge db {:message message}))

(reg-event-db
  :display-message
  [standard-interceptors]
  display-message)

(reg-event-db
  :process-error-response
  (fn [db [_ response]]
    (display-message db [:process-error-response (str "Ugh, got bad response : "
                                                      (:status-text response) " ("
                                                      (:status response) ")")])))

(reg-event-fx                                               ;; note the trailing -fx
  :initialize-config
  (fn [{:keys [db]} _]
    {:db         (merge db {:message "Loading configuration ..."})
     :http-xhrio {:method          :get
                  :uri             (str (:base-path db) "config")
                  :timeout         5000                     ;; optional see API docs
                  :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                  :on-success      [:process-config-response]
                  :on-failure      [:process-error-response]}}))

(reg-event-db
  :show-doc
  (fn [db [_ doc]]
    (assoc-in db [:docs] {:content doc})))

(reg-event-db
  :hide-doc
  (fn [db _]
    (assoc-in db [:docs :content] "")))

(defmulti loaded-worksheet identity)

(defmethod loaded-worksheet :default
  [_ _ response]
  response)

(defmethod loaded-worksheet :gist
  [_ filename response]
  (let [files (get response "files")
        name (if (= 1 (count files))
               (first (keys files))
               filename)
        content (-> (get files name)
                    (get "content"))]
    content))

(defmethod loaded-worksheet :github
  [_ _ response]
  (b64/decodeString (get response "content")))

(defmethod loaded-worksheet :local
  [_ _ content]
  (get content "worksheet-data"))

(reg-event-db
  :process-load-file-response
  [standard-interceptors]
  (fn
    [db [_ source filename content]]
    (let [content (loaded-worksheet source filename content)
          segs (.parse js/worksheetParser content)
          segments (->> segs
                        (map (fn [x] [(:id x) x]))
                        (into {}))
          segment-order (->> segs
                             (map #(:id %))
                             (into []))
          save (assoc (:save db) :filename filename)]
      (assoc db :worksheet
                {:segments             segments
                 :segment-order        segment-order
                 :url                  filename
                 :queued-code-segments #{}
                 :active-segment       nil}
                :save
                save))))


(reg-event-db
  :process-files-response
  [standard-interceptors]
  (fn
    [db [_ response]]
    (let [file-items (->> (:files response)
                          (map (fn [x] {:text    x
                                        :desc    (str "<div class=\"command\">" x "</div>")
                                        ;; For now, we have to take/return db due to clojuredocs sync window.open
                                        :handler (fn [db]
                                                   (routes/nav! (str "/edit?worksheet-filename=" x))
                                                   db)})))
          palette (:palette db)]
      (assoc-in db [:palette] (merge palette {:all-items     file-items
                                              :visible-items file-items
                                              :show          true
                                              :label         "Choose a file to load:"})))))


(reg-event-fx                                               ;; note the trailing -fx
  :edit-file
  (fn [{:keys [db]} [_ filename]]
    {:db         (update-in db [:save] dissoc :saved)
     :http-xhrio {:method          :get
                  :uri             (str (:base-path db)
                                        "load?worksheet-filename="
                                        (js/encodeURIComponent filename))
                  :timeout         5000
                  :response-format (ajax/json-response-format)
                  :on-success      [:process-load-file-response :local filename]
                  :on-failure      [:process-error-response]}}))


(reg-event-fx                                               ;; note the trailing -fx
  :view-file
  (fn [{:keys [db]} [_ params]]
    (let [source (:source params)
          user (:user params)
          repo (:repo params)
          path (:path params)
          id (:id params)
          filename (:filename params)
          revision (or (:revision params) "HEAD")
          response-type (case source
                          "github"
                          :github
                          "gist"
                          :gist
                          :default)
          url
          (case source
            "github"
            (str "https://api.github.com/repos/" user "/" repo "/contents/" path)
            "gist"
            (str "https://api.github.com/gists/" id)
            "bitbucket"
            (str "https://bitbucket.org/api/1.0/repositories/" user "/" repo "/raw/" revision "/" path)
            "test"
            "/test.clj"
            nil)]
      {:db         db
       :http-xhrio {:method          :get
                    :uri             url
                    :timeout         5000
                    :response-format (ajax/json-response-format)
                    :on-success      [:process-load-file-response response-type filename]
                    :on-failure      [:process-error-response]}})))

;;
;; App commands
;;
(reg-event-db
  :app:commands
  [standard-interceptors]
  (fn [db _]
    (let [palette (:palette db)]
      (assoc-in db [:palette]
                (merge palette {:show          true
                                :all-items     (:all-visible-commands palette)
                                :visible-items (:all-visible-commands palette)
                                :label         "Choose a command:"})))))


(reg-event-fx                                               ;; note the trailing -fx
  :app:load
  (fn [{:keys [db]} _]
    {:db         db
     :http-xhrio {:method          :get
                  :uri             (str (:base-path db) "gorilla-files")
                  :timeout         5000                     ;; optional see API docs
                  :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                  :on-success      [:process-files-response]
                  :on-failure      [:process-error-response]}}))


(reg-event-db
  :app:save
  [standard-interceptors]
  (fn [db _]
    (if-let [filename (get-in db [:save :filename])]
      (dispatch [:save-file filename])
      (dispatch [:app:saveas]))
    db))

(reg-event-db
  :app:saveas
  [standard-interceptors]
  (fn [db _]
    (assoc-in db [:save :show] true)))


(reg-event-db
  :docs:clojuredocs
  [standard-interceptors]
  (fn [db [_ win token]]
    (nrepl/resolve-symbol token
                          (get-in db [:worksheet :ns])
                          (fn [msg]
                            (if-let [ns (:ns msg)]
                              (set! (.-location win) (str "http://clojuredocs.org/clojure_core/" ns "/" (get msg "symbol")))
                              (set! (.-location win) (str "http://clojuredocs.org/search?q=" token)))))))


(defn change-to
  [db change-fn]
  (if-let [active-id (get-in db [:worksheet :active-segment])]
    (assoc-in db
              [:worksheet :segments active-id]
              (change-fn (get-in db [:worksheet :segments active-id])))
    db))

(reg-event-db
  :worksheet:changeToCode
  [standard-interceptors]
  (fn [db _]
    (change-to db db/to-code-segment)))

(reg-event-db
  :worksheet:changeToFree
  [standard-interceptors]
  (fn [db _]
    (change-to db db/to-free-segment)))

(reg-event-db
  :worksheet:clear-all-output
  [standard-interceptors]
  (fn [db _]
    (assoc-in db
              [:worksheet :segments]
              (into {}
                    (for [[k v] (get-in db [:worksheet :segments])]
                      [k (apply dissoc v [:console-response :value-response :error-text :exception])])))))

(reg-event-db
  :worksheet:clear-output
  [standard-interceptors]
  (fn [db _]
    (let [active-id (get-in db [:worksheet :active-segment])]
      (update-in db [:worksheet :segments active-id]
                 #(apply dissoc % [:console-response :value-response :error-text :exception])))))

(reg-event-db
  :worksheet:completions
  [standard-interceptors]
  (fn [db _]
    (let [active-id (get-in db [:worksheet :active-segment])
          active-segment (get-in db [:worksheet :segments active-id])
          ;;kernel (:kernel active-segment)
          ]
      (if (= :code (:type active-segment))
        (editor/complete active-id (get-in db [:worksheet :ns])))
      db)))

(reg-event-db
  :worksheet:delete
  [(conj standard-interceptors (undoable "Delete segment"))]
  (fn [db _]
    (let [worksheet (get db :worksheet)
          active-id (get-in db [:worksheet :active-segment])]
      (assoc db :worksheet (db/remove-segment worksheet active-id)))))

(reg-event-db
  :worksheet:deleteBackspace
  [standard-interceptors]
  (fn [db _]
    (let [worksheet (get db :worksheet)
          active-id (get-in db [:worksheet :active-segment])]
      (assoc db :worksheet (db/remove-segment worksheet active-id)))))

(reg-event-db
  :worksheet:evaluate
  [standard-interceptors]
  (fn [db _]
    (let [active-id (get-in db [:worksheet :active-segment])
          active-segment (get-in db [:worksheet :segments active-id])
          new-active-segment (merge active-segment {:console-response nil
                                                    :value-response nil
                                                    :error-text     nil
                                                    :exception      nil})
          kernel (:kernel active-segment)
          queued-segs (get-in db [:worksheet :queued-code-segments])]
      (if (= kernel :default-clj)
        (nrepl/send-eval-message! active-id (get-in active-segment [:content :value])))
      (-> (assoc-in db [:worksheet :segments active-id] new-active-segment)
          (assoc-in [:worksheet :queued-code-segments] (conj queued-segs (:id new-active-segment)))))))

(reg-event-db
  :worksheet:evaluate-all
  [standard-interceptors]
  (fn [db _]
    (let [segments (get-in db [:worksheet :segments])
          segment-order (get-in db [:worksheet :segment-order])
          sorted-code-segments (->> (map #(% segments) segment-order)
                                    (filter (fn [segment] (= :code (:type segment)))))]
      (doall (map #(nrepl/send-eval-message!
                     (:id %)
                     (get-in % [:content :value])) sorted-code-segments))
      (assoc-in db [:worksheet :queued-code-segments] (-> (map #(:id %) sorted-code-segments)
                                                       set)))))

(defn leave-active
  [db next-fn]
  (let [segment-order (get-in db [:worksheet :segment-order])
        active-id (get-in db [:worksheet :active-segment])
        next-active-idx (next-fn (.indexOf segment-order active-id))
        next-active-id (if (> next-active-idx -1) (nth segment-order next-active-idx))]
    (if next-active-id
      (assoc-in db [:worksheet :active-segment] next-active-id)
      db)))

(reg-event-db
  :worksheet:leaveBack
  [standard-interceptors]
  (fn [db _]
    (leave-active db dec)))

(reg-event-db
  :worksheet:leaveForward
  [standard-interceptors]
  (fn [db _]
    (leave-active db inc)))


(defn move-active
  [db next-fn]
  (let [segment-order (get-in db [:worksheet :segment-order])
        active-id (get-in db [:worksheet :active-segment])
        active-idx (.indexOf segment-order active-id)
        next-idx (next-fn active-idx)]
    (if (get segment-order next-idx)
      (->> (db/swap segment-order active-idx next-idx)
           (assoc-in db [:worksheet :segment-order]))
      db)))

;; TODO alt+g alt+d works only once for me, might be intercepted by browser
;; or window manager
(reg-event-db
  :worksheet:moveDown
  [standard-interceptors]
  (fn [db _]
    (move-active db inc)))

(reg-event-db
  :worksheet:moveUp
  [standard-interceptors]
  (fn [db _]
    (move-active db dec)))

(defn insert-segment
  [index-fn db _]
  (let [segment-order (get-in db [:worksheet :segment-order])
        active-id (get-in db [:worksheet :active-segment])
        active-idx (.indexOf segment-order active-id)
        new-segment (db/create-code-segment "")]
    (merge db {:worksheet
               (-> (:worksheet db)
                   (db/insert-segment-at (index-fn active-idx) new-segment))})))

(reg-event-db
  :worksheet:newAbove
  [(conj standard-interceptors (undoable "Insert segment"))]
  (partial insert-segment identity))

(reg-event-db
  :worksheet:newBelow
  [(conj standard-interceptors (undoable "Insert segment"))]
  (partial insert-segment inc))

;; Using re-frame undo instead
#_(reg-event-db
    :worksheet:undelete
    [standard-interceptors]
    (fn [db _]
      db))

;;
;; End db commands
;;

(defn- reset-palette
  [db]
  (let [palette (:palette db)]
    (assoc-in db
              [:palette]
              (merge palette {:show          false
                              :highlight     0
                              :visible-items (:all-visible-commands palette)
                              :filter        ""}))))

(reg-event-db
  :palette-blur
  [standard-interceptors]
  (fn [db [_]]
    (reset-palette db)))

(reg-event-db
  :palette-filter-changed
  (fn [db [_ val]]
    (let [palette (:palette db)]
      (assoc-in db
                [:palette]
                (merge palette {:show          true
                                :visible-items (->> (:all-items palette)
                                                    (filter (partial text-matches-re val)))
                                :filter        val})))))

(reg-event-db
  :palette-filter-keydown
  (fn [db [_ keycode]]
    (let [palette (:palette db)
          hl (:highlight palette)
          items (:visible-items palette)
          maxidx (- (count items) 1)]
      (case keycode
        38 (assoc-in db [:palette] (merge palette
                                          {:highlight (if (> hl 0)
                                                        (- hl 1)
                                                        hl)})) ;; up
        40 (assoc-in db [:palette] (merge palette
                                          {:highlight (if (< hl maxidx)
                                                        (+ hl 1)
                                                        hl)})) ;; down
        27 (reset-palette db)                               ;; esc
        13 (let [item (if (not-empty items) (nth items hl))
                 handler (:handler item)]
             (if handler
               (if (string? handler)
                 (do
                   ;; Gotcha Cannot call dispatch-sync in event handler
                   (dispatch [(keyword handler)])
                   (reset-palette db))
                 (do
                   (handler (reset-palette db))))
               db))
        db))))


(reg-event-db
  :after-save
  [standard-interceptors]
  (fn [db [_ filename]]
    (dispatch [:display-message (str filename " saved") 2000])
    (routes/nav! (str "/edit?worksheet-filename=" filename))
    (assoc-in db [:save :saved] true)))

(reg-event-fx                                               ;; note the trailing -fx
  :save-file
  (fn [{:keys [db]} [_ filename]]
    {:db         (assoc-in db [:save :show] false)
     :http-xhrio {:method          :post
                  :body            (str "worksheet-filename=" (js/encodeURIComponent filename)
                                        "&worksheet-data=" (-> (db/ws-to-clojure (:worksheet db))
                                                               js/encodeURIComponent))
                  :uri             (str (:base-path db) "save")
                  :timeout         5000                     ;; optional see API docs
                  :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                  :on-success      [:after-save filename]
                  :on-failure      [:process-error-response]}}))

(reg-event-db
  :save-as-keydown
  (fn [db [_ keycode]]
    (case keycode
      27 (do                                                ;; esc
           (dispatch [:save-as-cancel])
           db)
      13 (do                                                ;; Enter
           (dispatch [:save-file (get-in db [:save :filename])])
           db)
      db)))

(reg-event-db
  :save-as-change
  [standard-interceptors]
  (fn [db [_ value]]
    (assoc-in db [:save :filename] value)))

(reg-event-db
  :save-as-cancel
  [standard-interceptors]
  (fn [db _]
    (assoc-in db [:save] {:show     false
                          :filename nil})))


(reg-event-db
  :palette-action
  (fn [db [_ command]]
    (let [handler (:handler command)]
      (if (string? handler)
        ;; Gotcha : no dispatch-sync in handler
        (do
          (dispatch [(keyword handler)])
          (reset-palette db))
        (handler (reset-palette db))))))


(reg-event-db
  :worksheet:segment-clicked
  (fn [db [_ segment-id]]
    (assoc-in db [:worksheet :active-segment] segment-id)))

(reg-event-db
  :segment-value-changed
  (fn [db [_ seg-id value]]
    (assoc-in db [:worksheet :segments seg-id :content :value] value)))

(reg-event-db
  :evaluator:value-response
  (fn [db [_ seg-id response ns]]
    (let [segment (get-in db [:worksheet :segments seg-id])]
      (-> db
          (assoc-in [:worksheet :ns] ns)
          (assoc-in [:worksheet :segments seg-id] (merge segment response))))))

(reg-event-db
  :evaluator:console-response
  (fn [db [_ seg-id response]]
    (let [segment (get-in db [:worksheet :segments seg-id])]
      (assoc-in db [:worksheet :segments seg-id] (merge segment response)))))

(reg-event-db
  :evaluator:error-response
  ;; TODO: We should get the seg-id here instead of grabbing it from the details
  (fn [db [_ error-details]]
    (let [segment-id (:segment-id error-details)
          segment (get-in db [:worksheet :segments segment-id])]
      (assoc-in db [:worksheet :segments segment-id] (merge segment error-details)))))

(reg-event-db
  :evaluator:done-response
  (fn [db [_ seg-id]]
    (let [segment-order (get-in db [:worksheet :segment-order])
          seg-count (count segment-order)
          active-id (get-in db [:worksheet :active-segment])
          queued-segs (get-in db [:worksheet :queued-code-segments])]
      (if (= active-id seg-id)
        (if (= (- seg-count 1) (.indexOf segment-order active-id))
          (dispatch [:worksheet:newBelow])
          (dispatch [:worksheet:leaveForward])))
      (assoc-in db [:worksheet :queued-code-segments] (-> (remove #(= seg-id %) queued-segs)
                                                          set)))))

(reg-event-db
  :output-error
  (fn [db [_ seg-id e]]
    ;; TODO Should probably write to output cell
    ;; (js-debugger)
    (js/alert (.-message e) "for cell " seg-id)
    db))

;; TODO Should move evaluation state out of worksheet
(undo/undo-config! {:max-undos    3
                    :harvest-fn   (fn [ratom] (some-> @ratom :worksheet))
                    :reinstate-fn (fn [ratom value] (swap! ratom assoc-in [:worksheet] value))})
