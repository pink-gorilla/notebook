(ns pinkgorilla.newnotebook
  (:require 
   [pinkgorilla.db :as db]))

;; awb99:
;; Create a new notebook
;; "Take a sample notebook, and give it a unique namespace name"
;;
;; TODO: 
;;   1. Remove js dependency so that this code can run also in Clojure.
;;   2. Load a TEMPLATE from disk, and then inject the hip-namespace name.
;;   3. Move the new-notebook functionality to the SERVER.

(def hip-adjective ["affectionate" "amiable" "arrogant" "balmy" "barren" "benevolent"
                    "billowing" "blessed" "breezy" "calm" "celestial" "charming" "combative"
                    "composed" "condemned" "divine" "dry" "energized" "enigmatic" "exuberant"
                    "flowing" "fluffy" "fluttering" "frightened" "fuscia" "gentle" "greasy"
                    "grieving" "harmonious" "hollow" "homeless" "icy" "indigo" "inquisitive"
                    "itchy" "joyful" "jubilant" "juicy" "khaki" "limitless" "lush" "mellow"
                    "merciful" "merry" "mirthful" "moonlit" "mysterious" "natural" "outrageous"
                    "pacific" "parched" "placid" "pleasant" "poised" "purring" "radioactive"
                    "resilient" "scenic" "screeching" "sensitive" "serene" "snowy" "solitary"
                    "spacial" "squealing" "stark" "stunning" "sunset" "talented" "tasteless"
                    "teal" "thoughtless" "thriving" "tranquil" "tropical" "undisturbed" "unsightly"
                    "unwavering" "uplifting" "voiceless" "wandering" "warm" "wealthy" "whispering"
                    "withered" "wooden" "zealous"])

(def hip-nouns ["abyss" "atoll" "aurora" "autumn" "badlands" "beach" "briars" "brook" "canopy"
                "canyon" "cavern" "chasm" "cliff" "cove" "crater" "creek" "darkness" "dawn"
                "desert" "dew" "dove" "drylands" "dusk" "farm" "fern" "firefly" "flowers" "fog"
                "foliage" "forest" "galaxy" "garden" "geyser" "grove" "hurricane" "iceberg" "lagoon"
                "lake" "leaves" "marsh" "meadow" "mist" "moss" "mountain" "oasis" "ocean" "peak"
                "pebble" "pine" "plateau" "pond" "reef" "reserve" "resonance" "sanctuary" "sands"
                "shelter" "silence" "smokescreen" "snowflake" "spring" "storm" "stream" "summer"
                "summit" "sunrise" "sunset" "sunshine" "surf" "swamp" "temple" "thorns" "tsunami"
                "tundra" "valley" "volcano" "waterfall" "willow" "winds" "winter"])

(defn make-hip-nsname
  []
  (let [adj-index (-> (* (count hip-adjective) (rand)) js/Math.floor)
        noun-index (-> (* (count hip-nouns) (rand)) js/Math.floor)]
    (str (get hip-adjective adj-index) "-" (get hip-nouns noun-index))))


(defn create-new-worksheet 
  "A pure function that creates a new worksheet in the browser.
  All db functions used are pure functions!"
  []
  (let [worksheet {:ns                   nil
                   :segments             {}
                   :segment-order        []
                   :queued-code-segments #{}
                   :active-segment       nil}
        markdown-howto
        (db/create-free-segment
         (str "# PinkGorilla \n\n"
              "Shift + enter evaluates code. "
              "Hit " (db/ck) "+g twice in quick succession or click the menu icon (upper-right corner) for more commands.\n\n"
              "It's a good habit to run each worksheet in its own namespace. We created a random namespace for you; you can keep using it.\n\n"
              ))
        code-dependencies
        (db/create-code-segment
          (str  "; Automatically Download Dependencies (if they are not installed already) \n "
                "(use '[pinkgorilla.helper]) \n "
                "(pinkgorilla.helper/add-dependencies '[pinkgorilla.ui.gorilla-plot \"0.8.6\"])"
                ))
        
        code-namespace
        (db/create-code-segment
         (str 
          "; Define Namespace for your notebook and require namespaces \n"
          "(ns " (make-hip-nsname) "  \n"
          "  (:require \n"
          "     [pinkgorilla.ui.hiccup :refer [html!]] \n"
          "     [pinkgorilla.ui.vega :refer [vega!]] \n"
          "     [pinkgorilla.ui.gorilla-plot.core :refer [list-plot bar-chart compose histogram plot]])) \n"
          ))
        
        
        code-html
        (db/create-code-segment
         (str
          "(html! \n"
          "  [:div \n"
          "    [:h4 \"Hiccup Markup\"] \n"
          "    [:div {:style \"color:green;font-weight:bold;background-color:pink\"} \"World!\" \n"
          "    [:ol \n"
          "       [:li \"The Pinkie\"] \n"
          "       [:li \"The Pinkie and the Brain\"]  \n"
          "       [:li \"What will we be doing today?\"]]  \n"
          "    [:img {:height 100 :width 100 :src \"https://images-na.ssl-images-amazon.com/images/I/61LeuO%2Bj0xL._SL1500_.jpg\"}]]])
            "))
        
        code-plot
        (db/create-code-segment
         " (list-plot [5 6 7 3 9 20] ) \n"
         )
        
        code-vega
        (db/create-code-segment
         (str
          " (vega! \"https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json\" ) \n"
          ))
        
        ]
    (-> worksheet
        (db/insert-segment-at 0 markdown-howto)
        (db/insert-segment-at 1 code-dependencies)
        (db/insert-segment-at 2 code-namespace)
        (db/insert-segment-at 3 code-html)
        (db/insert-segment-at 4 code-plot)
        (db/insert-segment-at 5 code-vega)

        )))

