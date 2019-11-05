(ns pinkgorilla.output.html
  (:require
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
(defn output-html
  [output _]
  (if-let [content (:content output)]
    (cond
      (string? content)
      [:span.value {:data-value              (:value output)
                    :dangerouslySetInnerHTML {:__html content}}]
      :else
      [:span.value {:data-value (:value output)} (temp-comp-hack (:content output))])))