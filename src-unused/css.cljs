(ns unused.css
   (:require
   [webly.user.css.helper :refer [add-themes]]
[pinkgorilla.notebook-ui.eval-result.code-viewer :as highlight]
  )


(def components 
  {:highlight (add-themes
               {}
               "highlight.js/styles/%s.css"
               highlight/themes-main)}
  
  )


(def config 
  { :highlight "github" ; "default"
   
  })