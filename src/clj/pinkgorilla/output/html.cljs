(ns pinkgorilla.output.html
  (:require
   [reagent.core :as reagent]
   [reagent.dom]
   [pinkgorilla.output.hack :refer [temp-comp-hack]]))

;; Scripts in Injected html are not being evaluated.
;; This is what worked for GorillaRepl

;; https://www.martinklepsch.org/posts/just-in-time-script-loading-with-react-and-clojuresript.html
;; https://stackoverflow.com/questions/35614809/react-script-tag-not-working-when-inserted-using-dangerouslysetinnerhtml
;; https://stackoverflow.com/questions/2592092/executing-script-elements-inserted-with-innerhtml
;; https://stackoverflow.com/questions/42233778/why-cant-i-pass-clojurescript-functions-as-callbacks-to-javascript

;; awb99 ticket on reagent: [2 tickets]
;; https://github.com/reagent-project/reagent/issues/457
;; https://github.com/reagent-project/reagent/issues/14#issuecomment-543582060


;; A BETTER WAY FOR A HTML UI:
;; Interface:
;; (require-js-scripts [...])
;; (js-render-function (cljs->js data))
;; (js-render-function (cljs->js data props))



;; TODO: refactor UI library interface to use reagent and not html
;; THEN we can use this nice just in time loader component:
;; https://www.martinklepsch.org/posts/just-in-time-script-loading-with-react-and-clojuresript.html


;; TODO Ugh, old stylesheets persist as html so we get a string


(defn process-scripts!
  "Setting innerHTML (dangerouslySetInnerHTML) or textContent does not
 execute scripts! Thats why we clone and replace the elements."
  [el]
  (let [scripts (->> (.getElementsByTagName el "SCRIPT")
                     (.from js/Array))]
    (doall
     (map (fn [script]
            ;; WTF does this not work
            ;; (.replaceWith script (.cloneNode script true))
            ;; Ugly cloning hack
            (let [newScript (doto (.createElement js/document "script")
                              (aset "textContent" (.-textContent script)))]
              (if-let [src (.getAttribute script "src")]
                (.setAttribute newScript "src" src))
              (.replaceWith script newScript)))
          scripts))))

(defn output-html
  [output _]
  (if-let [content (:content output)]
    (cond
      (string? content)
      (reagent/create-class
       {:display-name        "output-html"                 ;; for more helpful warnings & errors
         ;; :component-will-unmount (fn [this])
        :component-did-mount (fn [this] (process-scripts! (reagent.dom/dom-node this)))

         ;; :component-did-update (fn [this old-argv])
        :reagent-render      (fn []
                               [:div {:dangerouslySetInnerHTML {:__html content}}])})

      :else
      [:span.value {:data-value (:value output)} (temp-comp-hack (:content output))])))
