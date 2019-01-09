(ns app.db
  (:require [app.characters-menu.db :refer [characters-menu-db]]
            [app.tokens-menu.db :refer [tokens-menu-db]]))

(def default-db
  {:window {:current-panel :webgl
            :drag-drop {:dragging? false
                        :mouse-offset [0 0]
                        :image-pos [0 0]}
            :left-nav {:text-visible? false}
            :menu-indexes {}
            :menus {:characters-menu characters-menu-db
                    :tokens-menu tokens-menu-db}}
   :webgl {:initialized? false
           :mounted? false
           :visible? false}})
