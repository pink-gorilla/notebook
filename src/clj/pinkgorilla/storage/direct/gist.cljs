(ns pinkgorilla.storage.direct.gist
  (:require
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.storage.direct.direct :refer [Direct]]
   [pinkgorilla.storage.gist :refer [StorageGist]]))

(extend-type StorageGist
  Direct

  (load-url [self base-path]
    (info "gist-storage.load-url for storage settings" self)
            ;https://api.github.com/gists/55b101d84d9b3814c46a4f9fbadcf2f8
    (str "https://api.github.com/gists/" (:id self)))

  (decode-content [self response]
    (let [files (get response "files")
          name (if (= 1 (count files))
                 (first (keys files))
                 (:filename self))
          content (-> (get files name)
                      (get "content"))]
      content)))



