(ns pinkgorilla.storage.direct.repo
  (:require
   [taoensso.timbre :refer-macros (info)]
   [goog.crypt.base64 :as b64]
   [pinkgorilla.storage.direct.direct :refer [Direct]]
   [pinkgorilla.storage.repo :refer [StorageRepo]]))

(extend-type StorageRepo
  Direct

  (load-url [self  base-path]
    (info "repo-storage.load-url")
    (str "https://api.github.com/repos/" (:user self) "/" (:repo self) "/contents/" (:filename self)))

  (decode-content [self response]
    (b64/decodeString (get response "content"))))



