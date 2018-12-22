(ns app.db)

(def default-db
  {:window {:current-panel :webgl
            :menus {:players-menu {:mouse-drag-offset [0 0]
                                   :pos [90 20]
                                   :visible? false}}}
   :webgl {:initialized? false
           :mounted? false
           :visible? false}})
