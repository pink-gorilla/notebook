(ns pinkgorilla.explore.form-helpers
  (:require
   [re-frame.core :refer [dispatch-sync dispatch subscribe]]
   [cljs-time.format :as tf]
   [cljs-time.core :as ct]
   [pinkgorilla.explore.utils :as u])
  (:require-macros [pinkgorilla.explore.utils :refer [tv]]))

(defn form-path
  [partial-path]
  (into [:forms] partial-path))

(defn progress-indicator
  "Show a progress indicator when a form is submitted"
  [form]
  (let [state (:state @form)]
    [:span
     (cond (= state :submitting)
           [:span.submission-progress.visible "submitting..."]

           (= state :success)
           [:span.submission-progress.visible
            [:i {:class "fa fa-check-circle"}]
            " success"])]))

(defn label-text [attr]
  (u/hkey-text attr))

(defn dispatch-change
  [dk attr-name val]
  (dispatch-sync [:edit-field (conj dk :data attr-name) val]))

(defn handle-change*
  [v dk attr-name]
  (dispatch-change dk attr-name v))

(defn handle-change
  "Meant for input fields, where your keystrokes should update the
  field"
  [e dk attr-name]
  (handle-change* (tv e) dk attr-name))

(defn label-for [form-id attr-name]
  (str form-id (name attr-name)))

;;~~~~~~~~~~~~~~~~~~
;; input
;;~~~~~~~~~~~~~~~~~~
(defn input-opts
  [{:keys [data form-id dk attr-name] :as opts}] ; placeholder
  (merge opts
         {:value (get-in @data [:data attr-name])
          :id (label-for form-id attr-name)
          :on-change #(handle-change % dk attr-name)
          :class (str "input " (name attr-name))}))

(defmulti input (fn [type _] type))

(defmethod input :textarea
  [_ opts] ; type
  [:textarea (input-opts opts)])

(defmethod input :select
  [_ {:keys [options] :as opts}] ; type
  [:select (input-opts opts)
   (map (fn [x] ^{:key (gensym)} [:option x]) options)])

(defmethod input :checkbox
  [_ {:keys [data dk attr-name] :as opts}] _ type
  (let [value (get-in @data [:data attr-name])
        opts (input-opts opts)]
    [:input (merge opts
                   {:type "checkbox"
                    :checked value
                    :on-change #(handle-change* (not value) dk attr-name)})]))

(defn toggle-set-membership
  [s v]
  ((if (s v) disj conj) s v))

(defmethod input :checkbox-set
  [_ {:keys [data dk attr-name value] :as opts}]
  (let [checkbox-set (or (get-in @data [:data attr-name]) #{})
        opts (input-opts opts)]
    [:input (merge opts
                   {:type "checkbox"
                    :checked (checkbox-set value)
                    :on-change #(handle-change* (toggle-set-membership checkbox-set value) dk attr-name)})]))

;; date handling
(defn unparse [fmt x]
  (when x (tf/unparse fmt (js/goog.date.DateTime. x))))

(def date-fmt (:date tf/formatters))

(defn handle-date-change [e dk attr-name]
  (let [v (tv e)
        ;; handler (u/strk dk "-edit")
        ]
    (if (empty? v)
      (dispatch-change dk attr-name nil)
      (let [date (tf/parse date-fmt v)
            date (js/Date. (ct/year date) (dec (ct/month date)) (ct/day date))]
        (dispatch-change dk attr-name date)))))

(defmethod input :date
  [_ {:keys [data form-id dk attr-name]}] ; type
  [:input {:type "date"
           :value (unparse date-fmt (get-in @data [:data attr-name]))
           :id (label-for form-id attr-name)
           :on-change #(handle-date-change % dk attr-name)}])

(defmethod input :default
  [type {:keys [data form-id dk attr-name] :as opts}]
  [:input (merge opts
                 {:type (name type)
                  :id (label-for form-id attr-name)
                  :value (get-in @data [:data attr-name])
                  :on-change #(handle-change % dk attr-name)})])
;;~~~~~~~~~~~~~~~~~~
;; end input
;;~~~~~~~~~~~~~~~~~~

(defn field-row [type {:keys [data form-id attr-name required] :as opts}]
  (let [errors (get-in @data [:errors attr-name])]
    [:tr {:class (when errors "error")}
     [:td [:label {:for (label-for form-id attr-name) :class "label"}
           (label-text attr-name)
           (when required [:span {:class "required"} "*"])]]
     [:td [input type opts]
      (when errors
        [:ul {:class "error-messages"}
         (map (fn [x] ^{:key (gensym)} [:li x]) errors)])]]))

(defmulti field (fn [type _] type))

(defmethod field :default
  [type {:keys [data form-id tip dk attr-name required label no-label] :as opts}]
  (let [errors (get-in @data [:errors attr-name])]
    [:div.field {:class (str (u/kabob (name attr-name)) (when errors "error"))}
     (when-not no-label
       [:label {:for (label-for form-id attr-name) :class "label"}
        (or label (label-text attr-name))
        (when required [:span {:class "required"} "*"])])
     (when tip [:div.tip tip])
     [:div {:class (str (apply str (map name dk)) " " (name attr-name))}
      [input type (dissoc opts :tip)]
      (when errors
        [:ul {:class "error-messages"}
         (map (fn [x] ^{:key (gensym)} [:li x]) errors)])]]))

(defmethod field :checkbox
  [type {:keys [data form-id tip dk attr-name required label no-label] :as opts}]
  (let [errors (get-in @data [:errors attr-name])]
    [:div.field {:class (str (u/kabob (name attr-name)) (when errors "error"))}
     [:div {:class (str (apply str (map name dk)) " " (name attr-name))}
      [input type (dissoc opts :tip)]
      (when-not no-label
        [:label {:for (label-for form-id attr-name) :class "label"}
         (or label (label-text attr-name))
         (when required [:span {:class "required"} "*"])])
      (when tip [:div.tip tip])
      (when errors
        [:ul {:class "error-messages"}
         (map (fn [x] ^{:key (gensym)} [:li x]) errors)])]]))

(defn builder
  "creates a function that builds inputs"
  [path]
  (let [path (form-path path)
        data (subscribe (into [:key] path))]
    (fn [type attr-name & {:as opts}]
      [field type (merge {:data data
                          :dk path
                          :attr-name attr-name}
                         opts)])))

(defn on-submit
  [form-path]
  {:on-submit (u/prevent-default #(dispatch [:submit-form form-path]))})