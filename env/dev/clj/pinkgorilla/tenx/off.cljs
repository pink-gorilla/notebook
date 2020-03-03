(ns pinkgorilla.tenx.off
  "reframe events related to 10x

   this ns gets changed in in shadow cljs config with 
   :ns-aliases to pinkgorilla.tenx.config 

   note: calling ns 10x does lead to errors (at runtime in the bundle) "
  (:require
   [taoensso.timbre :refer-macros (info)]))

(def use-10x false)

(defn configure-10x! []
  (info "NOT adding 10x event handlers!"))
