(ns gorilla-repl.views
  (:require [clojure.string :as str]
    ;; [hickory.core :as hick]
            [gorilla-repl.webpack-include]
            [gorilla-repl.editor :as editor]
            [cljs-uuid-utils.core :as uuid]
    ;; [clojure.walk :as w]
            [cljsjs.marked]
    ;; TODO : vega 2.6 does  not quite work yet - throw spec at http://vega.github.io/vega-editor/?mode=vega
    ;; https://github.com/vega/vega/wiki/Upgrading-to-2.0
    ;; data. prefix removed
            [cljsjs.d3]
            [cljsjs.d3geo]
            [cljsjs.vega]
            [gorilla-repl.subs]
            [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [re-com.core :as re-com]
            [goog.dom :as gdom]
            [dommy.core :as dom :refer-macros [sel1]]
            [taoensso.timbre :as timbre
             :refer-macros (log trace debug info warn error fatal report
                                logf tracef debugf infof warnf errorf fatalf reportf
                                spy get-env log-env)]))



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

(defn init-cm!
  [segment-id content-type opts seg-comp]
  (let [text-area (-> (reagent/dom-node seg-comp)
                      (sel1 :textarea))]
    (editor/create-editor! text-area
                           :content-type content-type
                           :segment-id segment-id
                           :opts opts)))

(defn colorize-cm!
  [seg-comp]
  (let [pre (-> (reagent/dom-node seg-comp)
                (sel1 :pre))]
    (.colorize js/CodeMirror #js [pre] "text/x-clojure")))


(defn queue-mathjax-rendering
  [id]
  (if-let [mathjax (.-MathJax js/window)]
    (doto (.-Hub mathjax)
      (.Queue #js ["Typeset" (.-Hub mathjax) id]))))


;; TODO: Should only fire when we are active!
(defn focus-active-segment
  [component active]
  (let [el (reagent/dom-node component)
        ;; TODO : Dedupe and move to editor
        cm-el (gdom/getElementByClass "CodeMirror" el)
        cm (and active (if cm-el (.-CodeMirror cm-el) nil))]
    (if cm
      (.focus cm))))

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


(defn palette-dialog
  []
  (let [palette (subscribe [:palette])]
    (reagent/create-class
      {:display-name         "palette-dialog"

       :component-did-update (fn [this old-argv]
                               ;; TODO: Focus or not?
                               (let [el (reagent/dom-node this)]
                                 (if (:show @palette)
                                   (-> el
                                       (sel1 :input)
                                       .focus))
                                 (if-let [actEl (gdom/getElementByClass "highlight" el)]
                                   (if (.-scrollIntoViewIfNeeded actEl)
                                     (.scrollIntoViewIfNeeded actEl false)
                                     (.scrollIntoView actEl false)))))

       :reagent-render
                             (fn []
                               (let [cmd-items (:visible-items @palette)
                                     highlight (:highlight @palette)
                                     items (map-indexed (fn [i x]
                                                          [(if (= i highlight)
                                                             :div.palette-item.highlight>li
                                                             :div.palette-item>li)
                                                           {:on-click                #(dispatch-sync [:palette-action x])
                                                            :dangerouslySetInnerHTML {:__html (:desc x)}}])
                                                        cmd-items)
                                     ul (into [:ul] items)]
                                 [:div.PaletteDialog {:style (if (false? (:show @palette)) {:display "none"} {})}
                                  [:div.PaletteDialog.modal-overlay
                                   {:on-click #(dispatch [:palette-blur])}]
                                  [:div.PaletteDialog.modal
                                   [:h3 {:dangerouslySetInnerHTML {:__html (:label @palette)}}]
                                   [:div.modal-content
                                    [:input {:type        "text"
                                             :value       (:filter @palette)
                                             :on-change   #(dispatch [:palette-filter-changed (-> % .-target .-value)])
                                             :on-key-down #(dispatch-sync [:palette-filter-keydown (.-which %)])
                                             ;; TODO  : on-blur kicks in before menu gets the click, but we want
                                             ;; :on-blur   #(dispatch [:palette-blur])
                                             ;; :on-mouse-down #(dispatch [:palette-blur])
                                             :ref         "filterText"}]
                                    [:div.palette-items ul]]]]))})))



(defn save-dialog
  []
  (let [dialog (subscribe [:save-dialog])]
    (reagent/create-class
      {
       :display-name         "save-dialog"

       :component-did-update (fn [this old-argv]
                               (let [el (reagent/dom-node this)]
                                 (if (:show @dialog)
                                   (-> el
                                       (sel1 :input)
                                       .focus))))
       :reagent-render       (fn []
                               [:div.SaveDialog {:style (if-not (:show @dialog) {:display "none"} {})}
                                ;; react-save-template
                                [:div {:class "modal-overlay"}]
                                [:div {:class "modal"}
                                 [:h3 "Filename (relative to project directory)"]
                                 [:div {:class "modal-content"}
                                  [:input {:type          "text"
                                           :value         (:filename @dialog)
                                           ;; blur does not work - prevents the click
                                           ;; :on-blur     #(dispatch [:save-as-cancel])
                                           :on-mouse-down #(dispatch [:save-as-cancel])
                                           :on-key-down   #(dispatch [:save-as-keydown (.-which %)])

                                           :on-change     #(dispatch [:save-as-change (-> % .-target .-value)])}]
                                  ;; :ref         "filterText"

                                  [:div>div {:class    "modal-button"
                                             :on-click #(dispatch [:save-as-cancel])}
                                   "Cancel"]
                                  [:div {:class    "modal-button highlight"
                                         :on-click #(dispatch [:save-file (:filename @dialog)])}
                                   "OK"]]]])})))


(defn app-status
  []
  (let [message (subscribe [:message])]
    (fn []
      [:div.status {:style (if (str/blank? @message) {:display "none"} {})}
       [:h3 @message]])))

(defn exception [e]
  (let [header (if (:cause e) "An exception was caused by: " "Exception thrown")
        ex (if (:cause e) (:cause e) e)
        frame-components (map-indexed (fn [idx frame]
                                        (let [type (get frame "type")
                                              tooling (.indexOf (get frame "flags") "tooling")
                                              li-classes (str type (if tooling " tooling-stackframe"))]
                                          (if (= type "clj")
                                            ^{:key idx}
                                            [:li {:class li-classes}
                                             ^{:key "fn"} [:span (get frame "fn")]
                                             ^{:key "ns"} [:span {:class "stackframe-ns"} (get frame "ns")]
                                             ^{:key "fileloc"} [:span {:class "stackframe-fileloc"}
                                                                (str " - ("
                                                                     (get frame "file")
                                                                     ":"
                                                                     (get frame "line")
                                                                     ")")]]
                                            ^{:key idx}
                                            [:li {:class li-classes}
                                             ^{:key "method"} [:span (get frame "method")]
                                             ^{:key "fileloc"} [:span {:class "stackframe-fileloc"}
                                                                (str " - ("
                                                                     (get frame "file")
                                                                     ":"
                                                                     (get frame "line")
                                                                     ")")]]
                                            )))
                                      (get ex "stacktrace"))]
    [:div {:class "stack-trace"}
     [:div.exception
      [:div {:class "exception-header"} header]
      [:span (get ex "class")]
      [:span (get ex "class")]]
     [:ul frame-components]]))

(defn error-text [text]
  [:div.error-text text])

(defn console-text [txt]
  [:div.console-text txt])


(defn free-output-view
  [seg-id content]
  (let [prev-uuid (uuid/uuid-string (uuid/make-random-uuid))
        prev-div-kw (keyword (str "div#" prev-uuid))]
    (reagent/create-class
      {:display-name   "free-output-view"
       :reagent-render (fn [seg-id content]                 ;; repeat
                         [:div.segment-main
                          [:div.free-markup
                           [prev-div-kw {:class                   "free-preview"
                                         :dangerouslySetInnerHTML {:__html (js/marked (:value content))}}]]])})))

(defn free-output
  [active seg-id content editor-options]
  (let [prev-uuid (uuid/uuid-string (uuid/make-random-uuid))
        prev-div-kw (keyword (str "div#" prev-uuid))
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])]
    (reagent/create-class
      {:component-did-mount  (fn [this]
                               (if active
                                 (do
                                   ((partial init-cm!
                                             seg-id
                                             (get-in content [:type])
                                             editor-options) this)
                                   (focus-active-segment this true))))
       :display-name         "free-output"
       :component-did-update (fn [this]
                               (if @is-active
                                 (do
                                   ((partial init-cm!
                                             seg-id
                                             (get-in content [:type])
                                             editor-options) this)
                                   (focus-active-segment this true))
                                 #_(let [el (gdom/getElement prev-uuid)])
                                 (if-let [mathjax (.-MathJax js/window)]
                                   (doto (.-Hub mathjax)
                                     (.Queue #js ["Typeset" (.-Hub mathjax) prev-uuid])))
                                 #_(let [el (gdom/getElement prev-uuid)])))

       ;; if ("MathJax" in window) MathJax.Hub.Queue(["Typeset", MathJax.Hub, $(element).attr('id')]);
       ;; :reagent-render      nil
       :reagent-render       (fn [active seg-id content]
                               (if active
                                 [:div.segment-main
                                  [:div.free-markup
                                   [:textarea {:value     (get-in @segment [:content :value])
                                               :read-only true}]]]
                                 [prev-div-kw {:class                   "free-preview"
                                               :dangerouslySetInnerHTML {:__html (js/marked (:value content))}}]))})))

(defn value-wrap
  [value content]
  [:span.value {:data-value value} content])

(defn temp-comp-hack
  [no-kw]
  (if no-kw (into [(keyword (first no-kw))]
                  (rest no-kw))))

;; TODO Ugh, old stylesheets persist as html so we get a string
(defn output-html
  [output _]
  (if-let [content (:content output)]
    (cond
      (string? content)
      [:span.value {:data-value              (:value output)
                    :dangerouslySetInnerHTML {:__html content}}]
      :else
      [:span.value {:data-value (:value output)} (temp-comp-hack (:content output))])))

(declare output-fn)

(defn output-list-like-other
  [output seg-id]
  (let [separator (temp-comp-hack (:separator output))
        open (temp-comp-hack (:open output))
        close (temp-comp-hack (:close output))
        value-comps (->> (map
                           #((output-fn %) % seg-id)
                           (:items output))
                         (interpose separator))
        all (filter some? (-> (into [open] value-comps) (conj close)))

        child-components (map-indexed (fn [i x]
                                        (with-meta x {:key (keyword (str "other-list-item-" i))}))
                                      all)]
    (into [:span.value {:data-value (:value output)}] child-components)))

(defn output-list-like-table
  [output seg-id row-wrap col-wrap]
  ;; (print "like table " seg-id row-wrap col-wrap)
  (let [value-comps (->> (map-indexed
                           (fn [i item]
                             (with-meta
                               (if col-wrap
                                 [col-wrap [(output-fn item) item seg-id]]
                                 [(output-fn item) item seg-id])
                               {:key (keyword (str "table-list-item-" i))}))
                           (:items output)))]
    (into [row-wrap] value-comps)))

;; TODO : Ugly, should probably rewrite the open, close, separator list like rendering
;; Before going further, we want it working and compatible
(defn output-list-like
  [output seg-id]
  (case (:open output)
    "<tr><td>"
    (output-list-like-table output seg-id :tr :td)
    "<tr><th>"
    (output-list-like-table output seg-id :tr :th)
    "<center><table>"
    (output-list-like-table output seg-id :center>table>tbody nil)
    (output-list-like-other output seg-id)))
;; (map hick/as-hiccup (hick/parse-fragment "<h1>HELLO WORLD!</h1>"))
;; return wrapWithValue(data, data.open + renderedItems.join(data.separator) + data.close);


;; see renderer.js
(defn output-vega
  [output seg-id]
  ;; TODO: Check vega error handling
  ;; for some reason, Vega (1.3.3?) will sometimes try and pop up an alert if there's an error, which is not a
  ;; great user experience. Here we patch the error handling function to re-route any generated message
  ;; to the segment.
  ;; vg.error = function (msg) { errorCallback("Vega error (js): " + msg); };
  ;; (set! (.-doNothing cm-commands) #())
  (let [uuid (uuid/uuid-string (uuid/make-random-uuid))
        span-kw (keyword (str "span#" uuid))]
    (reagent/create-class
      {:component-did-mount (fn [this]
                              (try                          ;; vg.parse.spec(data.content, function (chart) {
                                ;; (js-debugger)
                                ;; (print (.stringify js/JSON (clj->js (:content output))))
                                (.spec (.-parse js/vg)
                                       (clj->js (:content output))
                                       (fn [chart]
                                         (let [s (str uuid)
                                               ;; TODO: Huh? WTF?
                                               el (js/document.getElementById s)
                                               ;; el (sel1 (keyword (str "#" uuid)))
                                               ;; el (by-id s)
                                               ]
                                           (-> (chart #js {:el el :renderer "svg"})
                                               .update)
                                           )))              ;
                                (catch js/Object e
                                  (dispatch [:output-error seg-id (.-message e)]))))
       :reagent-render      (fn []
                              [value-wrap (get output :value)
                               [span-kw {:class "vega-span"}]])})))

(defn output-latex
  [output seg-id]
  (let [uuid (uuid/uuid-string (uuid/make-random-uuid))
        span-kw (keyword (str "span#" uuid))]
    (reagent/create-class
      {:component-did-mount  (fn [this]
                               (queue-mathjax-rendering uuid))
       :component-did-update (fn [this old-argv])
       :reagent-render       (fn []
                               [value-wrap
                                (get output :value)
                                [span-kw {:class                   "latex-span"
                                          :dangerouslySetInnerHTML {:__html (str "@@" (:content output) "@@")}
                                          }]])})))

;;     return wrapWithValue(data, "<span class='latex-span' id='" + uuid + "'>@@" + data.content + "@@</span>");

(defn output-fn
  [value-output]
  (case (:type value-output)
    "html" output-html
    "list-like" output-list-like
    "vega" output-vega
    "latex" output-latex))

(defn code-segment-view
  [seg-data]
  ;; TODO: active <=> selected, executing <=> running
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        footer-comp ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
      {:component-did-mount (fn [this]
                              (colorize-cm! this))
       :display-name        "code-segment-view"
       :reagent-render      (fn [seg-data]                  ;; repeat
                              (let [main-component
                                    ^{:key :segment-main} [:div.segment-main
                                                           [:pre {:class "static-code clojure"}
                                                            (get-in @segment [:content :value])]]
                                    ;; TODO : Factor out error, ex, console, output
                                    error-comp (if-let [err-text (:error-text @segment)]
                                                 ^{:key :error-text} [error-text err-text])
                                    ex-comp (if-let [ex (:exception @segment)]
                                              ^{:key :exception} [exception ex])
                                    console-comp (if-let [cons-text (not-empty (:console-response @segment))]
                                                   ^{:key :console-response} [console-text cons-text])
                                    output-comp (if-let [value-output (not-empty (:value-response @segment))]
                                                  (let [output-value (output-fn value-output)]
                                                    ^{:key :value-response}
                                                    [:div.output>pre [output-value value-output seg-id]]))
                                    div-kw (keyword (str "div#" (name seg-id))) ;; Aid with debugging
                                    other-children [main-component
                                                    error-comp
                                                    ex-comp
                                                    console-comp
                                                    output-comp
                                                    footer-comp]]
                                (apply conj [div-kw {:class "segment code"}]
                                       (filter some? other-children))))})))

(defn code-segment
  [seg-data editor-options]
  ;; TODO: active <=> selected, executing <=> running
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])
        is-queued (subscribe [:is-queued-query seg-id])
        footer-comp ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
      {:component-did-mount  (fn [this]
                               ((partial init-cm!
                                         seg-id
                                         (get-in seg-data [:content :type])
                                         editor-options) this)
                               (focus-active-segment this
                                                     @is-active))
       ;; :component-will-mount #()
       :display-name         "code-segment"
       :component-did-update #(focus-active-segment %1 @is-active)
       :reagent-render       (fn [seg-data]
                               (let [main-component
                                     ^{:key :segment-main} [:div.segment-main
                                                            [:textarea {:class     "codeTextArea mousetrap"
                                                                        :value     (get-in @segment [:content :value])
                                                                        :read-only true}]]
                                     error-comp (if-let [err-text (:error-text @segment)]
                                                  ^{:key :error-text} [error-text err-text])
                                     ex-comp (if-let [ex (:exception @segment)]
                                               ^{:key :exception} [exception ex])
                                     console-comp (if-let [cons-text (not-empty (:console-response @segment))]
                                                    ^{:key :console-response} [console-text cons-text])
                                     output-comp (if-let [value-output (not-empty (:value-response @segment))]
                                                   (let [output-value (output-fn value-output)]
                                                     ^{:key :value-response}
                                                     [:div.output>pre [output-value value-output seg-id]]))
                                     div-kw (keyword (str "div#" (name seg-id))) ;; Aid with debugging
                                     class (str "segment code"
                                                (if @is-active
                                                  " selected"
                                                  "")
                                                (if @is-queued
                                                  " running"
                                                  ""))
                                     other-children [main-component
                                                     error-comp
                                                     ex-comp
                                                     console-comp
                                                     output-comp
                                                     footer-comp]]
                                 (apply conj [div-kw
                                              {:class    class
                                               :on-click #(dispatch [:worksheet:segment-clicked seg-id])}]
                                        (filter some? other-children))))})))



(defn free-segment-view
  [seg-data]
  (let [seg-id (:id seg-data)
        footer ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
      {:display-name   "markup-segment-view"
       :reagent-render (fn [seg-data]
                         (let [free-value (:content seg-data)
                               ;; Aid with debugging
                               div-kw (keyword (str "div#" (name seg-id)))
                               other-children [[free-output-view seg-id free-value]
                                               footer]]
                           (apply conj
                                  [div-kw {:class "segment free"}]
                                  (filter some? other-children))))})))

(defn free-segment
  [seg-data editor-options]
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])
        footer ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
      {:display-name         "markup-segment"
       :component-did-update #(focus-active-segment %1 @is-active)
       :reagent-render       (fn [seg-data]
                               (let [free-value (:content @segment)
                                     div-kw (keyword (str "div#" (name seg-id)))
                                     class (str "segment free"
                                                (if @is-active
                                                  " selected"
                                                  ""))
                                     other-children [[free-output @is-active seg-id free-value editor-options]
                                                     footer]]
                                 (apply conj
                                        [div-kw {:class    class
                                                 :on-click #(dispatch [:worksheet:segment-clicked seg-id])}]
                                        (filter some? other-children))))})))

(defn worksheet
  [read-write editor-options]
  (let [worksheet (subscribe [:worksheet])]
    (fn [read-write editor-options]
      (let [segment-order (:segment-order @worksheet)
            segments (:segments @worksheet)]
        [:div.WorkSheet {}
         [:div {:class "segment container-segment"}
          [:div.container-children
           (for [seg-id segment-order]
             (do
               (let [segment (seg-id segments)]
                 (if read-write
                   (if (= :code (:type segment))
                     ^{:key seg-id} [code-segment segment editor-options]
                     ^{:key seg-id} [free-segment segment editor-options])
                   (if (= :code (:type segment))
                     ^{:key seg-id} [code-segment-view segment]
                     ^{:key seg-id} [free-segment-view segment])))))]]]))))

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
