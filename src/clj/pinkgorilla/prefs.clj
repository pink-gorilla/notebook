(ns pinkgorilla.prefs
  (:require [cljs.env]
    ;; [env-config.core :as env-config]
            ))

(defn gorilla-cljs-compiler-config []
  (if cljs.env/*compiler*
    (or
      (get-in @cljs.env/*compiler* [:options :external-config :gorilla/config])
      {})))

(defmacro emit-gorilla-cljs-compiler-config []
  `'~(or (gorilla-cljs-compiler-config) {}))

(defmacro if-cljs-kernel [with without]
  (list 'do
        (if (:with-cljs-kernel (gorilla-cljs-compiler-config))
          with
          without)))

; -- environmental config ---------------------------------------------------------------------------------------------------

#_(def ^:dynamic env-config-prefix "gorilla")

#_(defn get-env-vars []
  (-> {}
      (into (System/getenv))
      (into (System/getProperties))))

#_(defn read-env-config []
  (env-config/make-config-with-logging env-config-prefix (get-env-vars)))

#_(def memoized-read-env-config (memoize read-env-config))

#_(defmacro emit-env-config []
  `'~(or (memoized-read-env-config) {}))

; -- macro config api -------------------------------------------------------------------------------------------------------

#_(defn read-config []
  (merge (memoized-read-env-config) (gorilla-cljs-compiler-config)))

#_(def memoized-read-config (memoize read-config))

#_(defn get-pref [key]
  (key (memoized-read-config)))

