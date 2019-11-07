(ns pinkgorilla.db
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [cognitect.transit :as t]
   [re-frame.core :refer [dispatch-sync]]
   [cljs.reader]
   [cljs-uuid-utils.core :as uuid]
   ;[pinkgorilla.editor.core :as editor]
   [pinkgorilla.keybindings :refer [all-commands]]
   ))


(def debug?
  ^boolean js/goog.DEBUG)

;; -- Schema -----------------------------------------------------------------

;; This is a clojure.spec specification for the value in app-db. It is like a
;; Schema. See: http://clojure.org/guides/spec
;;
;; The value in app-db should always match this spec. Only event handlers
;; can change the value in app-db so, after each event handler
;; has run, we re-check app-db for correctness (compliance with the Schema).
;;
;; How is this done? Look in events.cljs and you'll notice that all handers
;; have an "after" interceptor which does the spec re-check.
;;
;; None of this is strictly necessary. It could be omitted. But we find it
;; good practice.

(s/def ::config map?)
(s/def ::docs map?)
(s/def ::worksheet map?)
;; (s/def ::segment-order vector?)
#_(s/def ::id int?)
#_(s/def ::title string?)
#_(s/def ::done boolean?)
#_(s/def ::todo (s/keys :req-un [::id ::title ::done]))
#_(s/def ::todos (s/and                                     ;; should use the :kind kw to s/map-of (not supported yet)
                   (s/map-of ::id ::todo)                   ;; in this map, each todo is keyed by its :id
                   #(instance? PersistentTreeMap %)         ;; is a sorted-map (not just a map)
                   ))
#_(s/def ::showing                                          ;; what todos are shown to the user?
    #{:all                                                  ;; all todos are shown
      :active                                               ;; only todos whose :done is false
      :done                                                 ;; only todos whose :done is true
      })
(s/def ::db (s/keys :req-un
                    ;; [::docs ::config ::segments ::segment-order]
                    [::config ::worksheet ::docs]
                    ;; [::todos ::showing]
                    ))
;; pegjs calls
(defn ^:export make-clojure-comment
  [code]
  (->> (str/split-lines code)
       (map #(str ";;; " %))
       (str/join "\n")))

;; pegjs calls
(defn ^:export unmake-clojure-comment
  [code]
  (->> (str/split-lines code)
       (map #(.slice % 4))
       (str/join "\n")))

(defmulti to-clojure :type)

(defmethod to-clojure :code
  [code-segment]
  (let [w (t/writer :json)
        start-tag ";; @@\n"
        end-tag "\n;; @@\n"
        output-start ";; =>\n"
        output-end "\n;; <=\n"
        console-start ";; ->\n"
        console-end "\n;; <-\n"
        console-text (if-let [ct (:console-response code-segment)]
                       (str console-start (make-clojure-comment ct) console-end)
                       "")
        output-text (if-let [ot (:value-response code-segment)]
                      (str output-start (make-clojure-comment (t/write w ot)) output-end)
                      "")]
    (str start-tag
         (get-in code-segment [:content :value])
         end-tag
         console-text
         output-text)))

(defmethod to-clojure :free
  [free-segment]
  (str ";; **\n"
       (make-clojure-comment (get-in free-segment [:content :value]))
       "\n;; **\n"))

(defn ws-to-clojure
  [worksheet]
  (let [segments (:segments worksheet)]
    (str ";; gorilla-repl.fileformat = 2\n\n"
         (->>
           (map #(to-clojure (get segments %))
                (:segment-order worksheet))
           (str/join "\n")))))

(defn- command-item
  [item]
  (let [kb (:kb item)
        shortcut (or kb "&nbsp;")]
    (merge item {:text (:desc item)
                 :desc (str "<div class= \"command\" >" (:desc item) "</div><div class= \"command-shortcut\" >" shortcut "</div>")})))

(defn ck
  []
  (if (re-matches #".*Win|Linux.*" (.-platform js/navigator))
    "alt"
    "ctrl"))

;; Experimental externs inference
;; https://gist.github.com/swannodette/4fc9ccc13f62c66456daf19c47692799
(defn kb-bind
  [^js/Mousetrap.bindGlobal mousetrap command]
  (if-let [kb (:kb command)]
    (let [kb-val (if (vector? kb) (clj->js kb) kb)
          handler (keyword (:handler command))]
      (.bindGlobal mousetrap kb-val
                   #(dispatch-sync [handler])))))



(defn install-commands
  [command-keymap]
  ;; ** Patch Mousetrap **
  ;;  Install a custom stopCallback so that our keyboard shortcuts work in the codeMirror textareas.
  ;;  This also lets us disable mousetrap processing when we show dialogs
  ;;  (this idea shamelessly stolen from the Mousetrap 'pause' plugin).
  (if-let [mousetrap (if (and (exists? js/window) (.-Mousetrap js/window))
                       (.-Mousetrap js/window))]
    (do
      (set! (.-enabled mousetrap) true)
      (set! (.-enable mousetrap) (fn [x] (set! (.-enabled mousetrap) x)))
      (set! (.-stopCallback mousetrap) #(not (.-enabled mousetrap)))
      (doall (map (partial kb-bind mousetrap) command-keymap)))))


;; -- Initial app-db Value  ---------------------------------------------------
(def initial-db
  {:docs
   {:content  ""
    :position []}
   :all-commands all-commands
   :palette      {;; TODO: We are (ab)using it for files and commands, "inherited" from js version
                  ;; Should probably be two instances
                  :visible-items        nil
                  :show                 false
                  :all-visible-commands (->> all-commands
                                             #_(filter #(:showInMenu %))
                                             (map command-item)
                                             (into []))
                  :all-items            nil
                  :filter               ""
                  :label                ""
                  :highlight            0}
   :worksheet    {}
   :config       {:read-only true}
   :base-path    nil
   :message      nil
   :save         {:show     false
                  :filename nil}
   :settings {:show false }
   })

;; pegjs calls
(defn ^:export create-code-segment
  ([content]
   (create-code-segment content nil nil nil))
  ([content console-text output-string version]
   (let [val-res (if (or (= "1" version) (and output-string (str/blank? (str/trim output-string))))
                   nil
                   (t/read (t/reader :json) output-string))]
     {:id               (-> (uuid/make-random-uuid) uuid/uuid-string keyword)
      :type             :code
      :kernel           :default-clj                        ;; default-cljs
      :content          {:value (or content "")
                         :type  "text/x-clojure"}
      :console-response console-text
      :value-response   val-res
      :error-text       nil
      :exception        nil})))

(defn to-code-segment
  [free-segment]
  {:id               (:id free-segment)
   :type             :code
   ;; TODO forcing :default-clj is not so nice
   :kernel           :default-clj
   :content          {:value (get-in free-segment [:content :value])
                      :type  "text/x-clojure"}
   :console-response nil
   :value-response   nil
   :error-text       nil
   :exception        nil})

;; pegjs calls
(defn ^:export create-free-segment
  [content]
  {:id             (-> (uuid/make-random-uuid) uuid/uuid-string keyword)
   :type           :free
   :markup-visible false
   :content        {:value (or content "")
                    :type  "text/x-markdown"}})

(defn to-free-segment
  [code-segment]
  {:id             (:id code-segment)
   :type           :free
   :markup-visible false
   :content        {:value (get-in code-segment [:content :value])
                    :type  "text/x-markdown"}})

(defn insert-segment-at
  [worksheet new-index new-segment]
  (let [segment-order (:segment-order worksheet)
        segments (:segments worksheet)
        new-id (:id new-segment)
        [head tail] (split-at new-index segment-order)]
    (merge worksheet {:active-segment new-id
                      :segments       (assoc segments new-id new-segment)
                      :segment-order  (into [] (concat head (conj tail new-id)))})))


(defn remove-segment
  [worksheet seg-id]
  (let [segment-order (:segment-order worksheet)
        active-id (:active-segment worksheet)
        seg-idx (.indexOf segment-order seg-id)
        next-active-idx (if (and (= active-id seg-id) (> seg-idx 0))
                          (nth segment-order (- seg-idx 1)))
        segments (:segments worksheet)]
    (merge worksheet {:active-segment next-active-idx
                      :segments       (dissoc segments seg-id)
                      :segment-order  (into [] (remove #(= seg-id %) segment-order))})))

(defn swap [v i1 i2]
  (assoc v i2 (v i1) i1 (v i2)))
