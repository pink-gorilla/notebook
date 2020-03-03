;; gorilla-repl.fileformat = 2

;; @@ [meta]
{}

;; @@

;; **
;;; # Custom UI Component with RegisterTag
;;; - This example shows how it is possible to define custom tags for renderers. 
;;; - Custom Tags have to be defined in a clojurescript kernel.
;;; - To display custom-tags when opening a saved notebook, we do need auto-executing cells, so that the renderer can be compiled prior to rendering the other cells.
;; **

;; @@ [cljs]
; clojurescript

(ns demo-tag-render
  (:require 
   [pinkgorilla.ui.pinkie :refer [register-tag]])) 
;; @@
;; =>
;;; ["^ ","~:type","~:html","~:content",["~:span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@ [clj]
; from clojure we can only access existing renderers via :p/keywords
^:R [:p/text "h2\nkllj\njj"]
;; @@
;; =>
;;; ["^ ","~:type","~:reagent","~:content",["^ ","~:hiccup",["~:p/text","h2\nkllj\njj"],"~:map-keywords",true,"~:widget",true]]
;; <=

;; @@ [cljs]
; but from a clojurescript kernel, we can generate new renders easily.
; custom renderer definition
(defn bongo [] [:div [:h1 "bongo"] [:p "trott"]])
;; @@
;; =>
;;; ["^ ","~:type","~:html","~:content",["~:span",["^ ","~:class","clj-unknown"],"#'demo-tag-render/bongo"],"~:value","#'demo-tag-render/bongo"]
;; <=

;; @@ [cljs]
; register the tag, and ONLY show the definiton of the new renderer
(:p/bongo (register-tag :p/bongo bongo))
;; @@
;; =>
;;; ["^ ","~:type","~:html","~:content",["~:span",["^ ","~:class","clj-unknown"],"#object[demo_tag_render$bongo]"],"~:value","#object[demo_tag_render$bongo]"]
;; <=

;; @@ [cljs]
; render custom tag from cljs
^:R [:p/bongo]
;; @@
;; =>
;;; ["^ ","~:type","~:reagent","~:content",["^ ","~:hiccup",["~:p/bongo"],"~:map-keywords",true,"~:widget",true]]
;; <=

;; @@ [clj]
; render custom tag from clj
^:R [:p/bongo]
;; @@
;; =>
;;; ["^ ","~:type","~:reagent","~:content",["^ ","~:hiccup",["~:p/bongo"],"~:map-keywords",true,"~:widget",true]]
;; <=

;; @@ [cljs]
; THE BELOW SHOULD NOT BE USED.
; from clojurescript we can access the component by name
; however, since this output will produce a function, on reopening this output cannot be shown and will produce errors.
;^:R [text "hi\nhi"]
nil
;; @@
;; =>
;;; ["^ ","~:type","~:html","~:content",["~:span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@ [clj]

;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:value",["~:span"]]
;; <=
