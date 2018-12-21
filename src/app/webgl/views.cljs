(ns app.webgl.views
  (:require [re-frame.core :refer [subscribe dispatch]]))

(def <sub (comp deref subscribe))
(def >evt dispatch)

(defn webgl-mount
  "Mounts RENDERER to DOM containers if not mounted."
  []
  (let [mounted? (<sub [:webgl/mounted?])]
    (>evt [:webgl/mount "webgl-container"])
    [:div#webgl-container {:style {:position "relative"}}]))

(defn- show-webgl
  []
  (let [initialized? (<sub [:webgl/initialized?])]
    (if-not initialized?
      (do
        (>evt [:webgl/initialize])
        [:p "Not initialized"])
      [webgl-mount])))

(defn webgl-component
  []
  (let [visible? (<sub [:webgl/visible?])]
    [:div#webgl {:style {:position "absolute"
                         :background-color "#242424"
                         :top 0
                         :left 0 
                         :padding-left 80
                         :width "100%"
                         :height "100%"}}
     (if-not visible?
       [:div {:style {:display "flex"
                      :justify-content "center"
                      :align-items "center"
                      :height "100%"}}
        [:button {:on-click #(>evt [:webgl/visible true])} "Join"]]
       [show-webgl])]))
