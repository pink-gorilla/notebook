(ns pinkgorilla.storage.direct.direct
  (:require
   [taoensso.timbre :refer-macros (info)]
   ))


(defprotocol Direct
  (load-url [self base-path])
  (decode-content [self content]) ; happens after worksheet-content has been loaded via ajax/url fetch
  )
