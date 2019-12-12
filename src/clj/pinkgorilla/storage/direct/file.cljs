(ns pinkgorilla.storage.direct.file
  (:require
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.storage.direct.direct :refer [Direct]]
   [pinkgorilla.storage.file :refer [StorageFile]]))

(extend-type StorageFile
  Direct

  (load-url [self base-path]
    (info "local-storage.load")
    (str base-path "load?worksheet-filename=" (js/encodeURIComponent (:filename self))))

  (decode-content [self content]
    (get content "worksheet-data")))



