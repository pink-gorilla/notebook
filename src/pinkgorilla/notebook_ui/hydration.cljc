(ns pinkgorilla.notebook-ui.hydration
  (:require
   #?(:clj [taoensso.timbre :refer [debugf info infof error]]
      :cljs [taoensso.timbre :refer-macros [debugf info infof error]])
   [pinkgorilla.notebook.uuid :refer [id]]
   [pinkgorilla.notebook.core :refer [code->segment md->segment]]))

;; helper functions

(defn- assoc-when [r key val]
  (if val
    (assoc r key val)
    r))

(defn- process-type [segment type fun]
  (if (= type (:type segment))
    (fun segment)
    segment))

(defn- dissoc-in
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (assoc m k newmap))
      m)
    (dissoc m k)))

 ;; hydration (load persisted notebook)

(defn hydrate-md [s]
  (let [md (get-in s [:content :value])]
    {:type :md
     :md md}))

(defn hydrate-code [s]
  (let [code (get-in s [:content :value])
        picasso-spec (get-in s [:value-response])
        out (get-in s [:console-response])]
    (-> s
        (dissoc :content)
        (assoc-when :code code)

        (dissoc :value-response)
        (assoc-when :picasso picasso-spec)

        (dissoc :console-response)
        (assoc-when :out out))))

(defn- hydrate-segment [segment]
  (-> segment
      (process-type :code hydrate-code)
      (process-type :free hydrate-md)
      (assoc :id (id))))

(defn- to-key [segment]
  {(:id segment) segment})

(defn hydrate [notebook]
  (let [segments (:segments notebook)
        segments-hydrated (vec (map hydrate-segment segments))
        order (vec (map :id segments-hydrated))]
    (info "hydrating notebook ..")
    {:ns       nil  ; current namespace
     :queued   #{} ; code segments that are qued for evaluation
     :meta     (:meta notebook)
     :segments (reduce conj (map to-key segments-hydrated))
     :order    order
     :active   (first order)}))

; dehydrate / save

(defn segments-ordered [notebook]
  (let [segments (:segments notebook)
        segment-ids-ordered (:order notebook)]
    (vec (map #(get segments %) segment-ids-ordered))))

(defn dehydrate-md [s]
  ;(info "dehydrating md segment: " s)
  (let [md (get-in s [:md])]
    {:type :free
     :markup-visible false
     :content {:type "text/x-markdown"
               :value md}}))

(defn dehydrate-code [segment]
  ;(info "dehydrating code segment: " s)
  (let [{:keys [kernel code picasso out]} segment]
    ; #(dissoc-in % [:value-response :reagent])
    ; #(dissoc % :id :exception :error-text)
    (-> {:type :code
         :kernel kernel
         :content {:type "text/x-clojure"
                   :value code}}
        (assoc-when :value-response picasso)
        (assoc-when :console-response out))))

(defn dehydrate-segment [segment]
  (-> segment
      (process-type :code dehydrate-code)
      (process-type :md dehydrate-md)
      (dissoc :id)))

(defn dehydrate [notebook]
  (let [segments (segments-ordered notebook)
        ;_ (info "segments ordered: " segments)
        segments-dehydrated (vec (map dehydrate-segment segments))
        notebook-dehydrated {;:version (:version notebook)
                             :meta (:meta notebook)
                             :segments segments-dehydrated}]
    ;(info "dehydrated: " notebook-dehydrated)
    notebook-dehydrated))


;; manipulate hydrated notebook


(defn create-md-segment
  [md]
  (-> (md->segment md)
      hydrate-segment))

(defn create-code-segment
  [code]
  (->
   (code->segment :clj code)
   hydrate-segment))

(defn toggle-view-segment
  [{:keys [id type] :as segment}]
  (let [segment (if (= type :code)
                  (create-md-segment (:code segment))
                  (create-code-segment (:md segment)))]
    (-> segment
        (assoc :id id))))

(defn- update-segment
  [fun notebook seg-id]
  (let [segment (get-in notebook [:segments seg-id])
        segment-new (fun segment)]
    (assoc-in notebook [:segments seg-id] segment-new)))

(defn- update-segment-active
  [notebook fun]
  (let [seg-id (:active notebook)]
    (update-segment fun notebook seg-id)))

(defn- update-segment-all
  [notebook fun]
  (reduce (partial update-segment fun) notebook (:order notebook)))

(defn- clear-output [segment]
  (dissoc segment
          :out
          :picasso
          :value
          :err
          :ex
          :root-ex
          :datafy))

(defn clear-active
  [notebook]
  (update-segment-active notebook clear-output))

(defn clear-all
  [notebook]
  (update-segment-all notebook clear-output))

(defn insert-segment-at
  [notebook new-index new-segment]
  (let [{:keys [order active segments]} notebook
        new-id (:id new-segment)
        [head tail] (split-at new-index order)]
    (merge notebook {:active new-id
                     :segments       (assoc segments new-id new-segment)
                     :order  (into [] (concat head (conj tail new-id)))})))

(defn insert-segment-bottom
  [notebook new-segment]
  (let [{:keys [order active segments]} notebook
        new-id (:id new-segment)]
    (merge notebook {:active new-id
                     :segments (assoc segments new-id new-segment)
                     :order  (into [] (conj order new-id))})))

(defn remove-segment
  [notebook seg-id]
  (let [{:keys [order active segments]} notebook
        seg-idx (.indexOf order seg-id)
        next-active-idx (if (and (= active seg-id) (> seg-idx 0))
                          (nth order (- seg-idx 1)))]
    (merge notebook {:active next-active-idx
                     :segments       (dissoc segments seg-id)
                     :order  (into [] (remove #(= seg-id %) order))})))

(defn code? [segment]
  (= :code (:type segment)))

(defn code-segment-ids [{:keys [segments order] :as notebook}]
  (debugf "order: %s segments: %s" order segments)
  (let [ids-code  (->> order
                       (map #(get segments %))
                       (filter code?)
                       (map :id))]
    (infof "code-ids: %s" ids-code)
    ids-code))




