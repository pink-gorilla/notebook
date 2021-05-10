(ns pinkgorilla.notebook-ui.schema.sample
  (:require
   [clojure.spec.alpha :as s]))

;#pinkgorilla.storage.file.StorageFile{:filename "./notebooksnotebooksdemo.cljg"}


(def explorer
  {:config
   {:repositories [{:name "local", :url "/api/explorer", :save true} {:name "public", :url "https://raw.githubusercontent.com/pink-gorilla/gorilla-explore/master/resources/list.json"}]}
   :notebooks
   {"local" [{:meta {:tags "clj-only,clj"}, :edit-date "2021-02-16", :index 0, :type :file, :filename "./notebooks/notebooksdemo.cljg", :root-dir "./notebooks", :id "./notebooks/notebooksdemo.cljg", :storage 8, :repo "app", :user "_file", :filename-canonical "/home/andreas/pinkgorilla/notebook-ui/notebooks/notebooksdemo.cljg"} {:meta {:tags "clj,cljs"}, :edit-date "2021-02-16", :index 1, :type :file, :filename "./notebooks/hiccup/custom-tag.cljg", :root-dir "./notebooks", :id "./notebooks/hiccup/custom-tag.cljg", :storage 8, :repo "app", :user "_file", :filename-canonical "/home/andreas/pinkgorilla/notebook-ui/notebooks/hiccup/custom-tag.cljg"} {:meta {:tags "clj-only,clj"}, :edit-date "2021-02-16", :index 2, :type :file, :filename "./notebooks/hiccup/html-script.cljg", :root-dir "./notebooks", :id "./notebooks/hiccup/html-script.cljg", :storage 8, :repo "app", :user "_file", :filename-canonical "/home/andreas/pinkgorilla/notebook-ui/notebooks/hiccup/html-script.cljg"}]}
   :search {:tags #{}, :text "", :root "all"}})

(def bidi
  ["/" {"" {"explorer" :ui/explorer, "notebook" :ui/notebook, "" :demo/main, "demo/datafy" :demo/datafy}, "api/" {"explorer" {:get :api/explorer}, "notebook" {:get :api/notebook-load, :post :api/notebook-save}}, "r" :webly/resources}])

(def nrepl
  {:ws-url "ws://localhost:9000/nrepl", :connected? true, :conn {:session-id 888, :input-ch 999, :output-ch 888, :connected? 888, :requests 888}, :info {}})

(def keybindings
  {:all [{:kb "alt-g e", :handler [:bidi/goto :ui/explorer], :desc "Notebook Explorer"}
         {:kb "alt-g u", :handler [:settings/show], :desc "User Settings Edit"}
         {:kb "alt-g k", :handler [:palette/show], :desc "Keybindings dialog"}
         {:kb "alt-g r", :handler [:bidi/goto :ui/nrepl], :desc "nRepl page"}]
   :search {:highlight 0, :visible-items nil, :query ""}})

(def completion
  {:candidates [], :active nil, :show-all true, :docstring "", :resolve nil})

(def settings
  {:default-kernel :clj
   :layout :horizontal
   :code-editor :codemirror
   :code-viewer :codemirror
   :code-viewer-theme "github"
   :codemirror-theme "darcula"
   :md-editor :codemirror})

(def punk
  {:entries []
   :history []
   :current nil
   :current/loading false
   :current.view/selected nil
   :next nil, :next.view/key nil
   :next.view/selected nil})

(def document
  {:documents {1 {:active nil
                  :ns nil
                  :queued #{}
                  :meta {}
                  :segments []
                  :order []}}})

(def db
  {:explorer explorer
   :bidi bidi
   :nrepl nrepl
   :keybindings keybindings
   :completion completion
   :settings settings
   :punk  punk
   :document document
   :notebook 888})

(def demo-notebook
  ; same format as hydrated notebook.
  {:segments {:scratchpad {:id :scratchpad
                           :type :code
                           :code "(+ 2 2)(println 2)"
                           :picasso nil}
              :1 {:id :1
                  :type :code
                  :code "(+ 1 1)(println 1)\n {:a 5 :b \"ttt\"}"
                  :picasso nil}
              :3 {:id :3
                  :type :code
                  :code "(+ 3 3)(println 3)"
                  :picasso nil}}
   :order [:1
           :scratchpad
           :3]})
