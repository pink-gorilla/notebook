(ns pinkgorilla.notebook-ui.app.css
  (:require
   [webly.user.css.helper :refer [add-themes]]
   [pinkgorilla.notebook-ui.codemirror.theme :as codemirror]))

(def components
  {:codemirror (add-themes
                {true ["codemirror/lib/codemirror.css"]}
                "codemirror/theme/%s.css"
                codemirror/themes)
   :punk {true ["notebook-ui/punk.css"]}
   :datatypes {true ["notebook-ui/datatypes.css"]}
   :recom {true [;"notebook-ui/bootstrap.css"
                 "notebook-ui/re-com.css"]}
   :prose-mirror {true ["prosemirror-view/style/prosemirror.css"
                        "prosemirror-menu/style/menu.css"
                        "notebook-ui/prosemirror-gorilla.css"]}
   ;:tailwind-prose {true ["https://unpkg.com/@tailwindcss/typography@0.2.x/dist/typography.min.css"]}
   })

(def config
  {:codemirror "paraiso-dark" ;true
   :punk true ; rebl / datafy/nav browser
   :datatypes true ; picasso clj/cljs datatypes 
   :prose-mirror true
   :recom true
   ;:tailwind-prose true
   })