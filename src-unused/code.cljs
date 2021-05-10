(ns pinkgorilla.notebook-ui.eval-result.code
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [dispatch]]))

(defn autoResize [el & args]
  ; this.style.height = 'auto'; 
  ; this.style.height = this.scrollHeight + 'px'; 
  (info "autoResize.." el)
  (let [style (.-style el)
        _ (info "style: " style)]
    (set! (.-height style) "auto")
    (set! (.-height style) (str (.-scrollHeight el) "px"))))

(defn code-edit [{:keys [id code] :as eval-result}]
  #_[:pre ;.clojure
     [:code {:ref  #(when % (.highlightBlock hljs %))
             :contenteditable "true"
             :on-input (fn [evt]
                         (let [v (-> evt .-target .-value)] ; 
                           (info "code changed!" v)))
           ;:value code
             }
      code]]
  (info "eval result: " eval-result)
  [:textarea.font-mono.text-left.w-full.text-gray-70
   {; textarea.addEventListener ('input', autoResize, false); 
    :ref #(when %
            (.addEventListener % "input" (partial autoResize %) false)
            (autoResize %))

    :value (or code "")
    :style {:resize "none"}
    :on-change (fn [evt]
                 (let [v (-> evt .-target .-value)] ; 
                   (info "code changed!" v)
                   (dispatch [:segment/set-code id v])))}])

