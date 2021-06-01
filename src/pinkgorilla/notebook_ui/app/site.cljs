(ns pinkgorilla.notebook-ui.app.site
  (:require
   [ui.site.template :as template]))

(defn splash []
  [template/splash-message
   {:link-text "On Github"
    :link-url "https://github.com/pink-gorilla/goldly"
    :title ["Goldly lets you create "
            [:br]
            "realtime dashboards powered by clojure"]
    :title-small "open source"}])

(defn header []
  [template/header-menu
   {:brand "PinkGorilla Notebook"
    :brand-link "/"
    :items [{:text "about" :link "/about"}
            {:text "explorer" :link "/explorer"}
            {:text "nrepl" :link "/nrepl"}
            {:text "goldly systems" :link "/goldly"}
                 ;{:text "notebook" :link "/notebook"}
            {:text "zulip" :link "https://clojurians.zulipchat.com/#narrow/stream/212578-pink-gorilla-dev" :special? true}
            {:text "feedback" :link "https://github.com/pink-gorilla/notebook/issues" :special? true}]}])
