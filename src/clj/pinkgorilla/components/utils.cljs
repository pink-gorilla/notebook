(ns pinkgorilla.components.utils
  (:require
   [clojure.string :as s]
   [cljs-time.format :as tf]))

(defn capitalize-words
  "Capitalize every word in a string"
  [s]
  (->> (s/split (str s) #"\b")
       (map s/capitalize)
       (s/join)))

(defn hkey-text
  [hkey]
  (-> (name hkey)
      (s/replace #"-" " ")
      capitalize-words))

(defn prevent-default
  [f]
  (fn [e]
    (.preventDefault e)
    (f e)))

(defn strk
  [key & args]
  (keyword (apply str (name key) args)))

(defn kabob
  [s]
  (s/replace s #"[^a-zA-Z]" "-"))

(defn format-date
  [date]
  (tf/unparse (tf/formatter "MMM d, YYYY") (js/goog.date.DateTime. date)))

(defn short-date
  [date]
  (tf/unparse (tf/formatter "MMM d") (js/goog.date.DateTime. date)))

(defn el-by-id [id]
  (.getElementById js/document id))

(defn toggle [v x y]
  (if (= v x) y x))

(defn flatv
  [& args]
  (into [] (flatten args)))

(defn now
  []
  (js/Date.))

(defn nav
  [fragment]
  (aset js/window "location" fragment))

(defn scroll-top
  []
  (aset (js/document.querySelector "body") "scrollTop" 0))

(defn slugify
  [txt]
  (-> txt
      s/lower-case
      (s/replace #"[^a-zA-Z0-9]" "-")))

(defn slug
  [listing]
  (str "/projects/" (:db/id listing) "/" (slugify (str (:project/name listing)))))





