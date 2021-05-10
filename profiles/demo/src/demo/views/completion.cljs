(ns demo.views.completion
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.completion.component :refer [completion-component]]))

(rf/reg-event-db
 :nrepl/completion-demo
 (fn [db [_]]
   (assoc-in db [:completion]
             {:docstring  "clojure.core/doseq\n([seq-exprs & body])\nMacro\n  Repeatedly executes body (presumably for side-effects) with\n  bindings and filtering as provided by \"for\".  Does not retain\n  the head of the sequence. Returns nil.\n"
              :candidates [{:candidate "map", :type :function, :ns "clojure.core"}
                           {:candidate "max", :type :function, :ns "clojure.core"}
                           {:candidate "map?", :type :function, :ns "clojure.core"}
                           {:candidate "mapv", :type :function, :ns "clojure.core"}
                           {:candidate "mapcat", :type :function, :ns "clojure.core"}
                           {:candidate "max-key", :type :function, :ns "clojure.core"}
                           {:candidate "make-array", :type :function, :ns "clojure.core"}
                           {:candidate "map-entry?", :type :function, :ns "clojure.core"}
                           {:candidate "macroexpand", :type :function, :ns "clojure.core"}
                           {:candidate "map-indexed", :type :function, :ns "clojure.core"}
                           {:candidate "macroexpand-1", :type :function, :ns "clojure.core"}
                           {:candidate "make-hierarchy", :type :function, :ns "clojure.core"}]})))


(def docstring-res
  "clojure.core/doseq\n([seq-exprs & body])\nMacro\n  Repeatedly executes body (presumably for side-effects) with\n  bindings and filtering as provided by \"for\".  Does not retain\n  the head of the sequence. Returns nil.\n")


(defn completion-demo []
  (rf/dispatch [:nrepl/completion-demo])
  [:div {:class "text-red-500 text-lg"}
   [:p "For this to work nrepl needs to be connected!"]
   [:input {:on-change (fn [s evt]
                         (println "evt:" evt)
                             ;(dispatch [:nrepl/completion (:text @state) "user" ""])
                         )}]

   [completion-component]])


