(ns pinkgorilla.notebook-ui.codemirror.extension.completion-inline
  (:require
   [taoensso.timbre :refer-macros [info warn]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [cljs.reader]
   [reagent.core :as r]
   [cljs.tools.reader]))


;; Docstrings / auto complete


(defn do-completions
  "@param {CodeMirror} cm"
  [cur start end callback ns compl]
  (let [ln (.-line cur)
        completions #js {:list (clj->js compl)
                         :from (.Pos js/CodeMirror ln start)
                         :to   (.Pos js/CodeMirror ln end)}]
    (.on js/CodeMirror
         completions
         "select"
         (fn [s]
           (if-not (str/starts-with? s "/")
             (kernel/get-completion-doc :clj s   ;awb99 :clj is a hack
                                        ns
                                        (fn [docs]
                                          (if-not (str/blank? docs)
                                            (dispatch [:show-doc docs])
                                            (dispatch [:hide-doc])))))))
    (.on js/CodeMirror
         completions
         "close"
         #(dispatch [:hide-doc]))
    ;; Show the UI
    (callback completions)))

(defn completer
  "@param {CodeMirror} cm"
  [ns cm callback _] ;; options
  (let [cur (.getCursor cm)
        token (.getTokenAt cm cur)
        word (.-string token)
        start (.-start token)
        end (.-end token)
        line (.-line cur)
        doc (.copy (.getDoc cm) false)]
    (.replaceRange doc "__prefix__" #js {:line line :ch start} #js {:line line :ch end})
    ;; TODO: this is a workaround for https://github.com/alexander-yakushev/compliment/issues/15
    ;; Should be fixed, no?
    ;; if (word[0] != "/")
    ;awb99 clj is a hack - dont know where segment var is
    (kernel/get-completions :clj word ns (.getValue doc) (partial do-completions cur start end callback ns))))

(defn complete
  [segment-id ns]
  (let [dom-id (name segment-id)
        cm (-> (gdom/getElement dom-id) el-editor)]
    (.showHint js/CodeMirror cm (partial completer ns) #js {:async true :completeSingle false :alignWithWord false})))

(defn get-token-at-cursor
  [segment-id]
  (let [dom-id (name segment-id)
        cm (-> (gdom/getElement dom-id) el-editor)]
    ;; var token = self.codeMirror.getTokenAt(self.codeMirror.getCursor());
    ;; if (token != null) return token.string;
    (.-string (->> (.getCursor cm)
                   (.getTokenAt cm)))))
