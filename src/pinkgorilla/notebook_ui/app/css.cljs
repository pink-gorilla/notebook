(ns pinkgorilla.notebook-ui.app.css)

(def components
  {:punk {true ["notebook-ui/punk.css"]}
   :recom {true [;"notebook-ui/bootstrap.css"
                 "notebook-ui/re-com.css"]}
   ;:tailwind-prose {true ["https://unpkg.com/@tailwindcss/typography@0.2.x/dist/typography.min.css"]}
   })

(def config
  {:punk true ; rebl / datafy/nav browser
   :recom true
   ;:tailwind-prose true
   })