(ns pinkgorilla.storage.direct.bitbucket
  (:require
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.storage.direct.direct :refer [Direct]]
   [pinkgorilla.storage.bitbucket :refer [StorageBitbucket]]))

(extend-type StorageBitbucket
  Direct

  (load-url [self base-path]
    (info "bitbucket.load")
    (str "https://bitbucket.org/api/1.0/repositories/" (:user self) "/" (:repo self) "/raw/" (:revision self) "/" (:path self)))

  (decode-content [self content]
    content))




