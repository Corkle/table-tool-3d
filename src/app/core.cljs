(ns app.core
  (:require  [reagent.core :as reagent]
             [re-frame.core :refer [dispatch dispatch-sync]]
             [app.events]
             [app.subs]
             [app.views]
             [app.webgl.core]))

(defn reload! []
  (println "Code updated."))

(defn ^:export init []
  (dispatch-sync [:initialize-db])
  (reagent/render [app.views/main-app]
                  (.getElementById js/document "app")))
