(ns app.debug.core
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cljs.pprint :refer [pprint]]))

(def <sub (comp deref rf/subscribe))
(def >evt rf/dispatch)

(defn- debug-panel
  []
  (let [db (<sub [:debug/db-state])]
    [:div.notification {:style {:width "100%"}}
     [:button {:on-click #(>evt [:debug/hide "debug"])
               :style {:margin 5
                       :top 0
                       :right 0
                       :position "absolute"}}
      "-"]
     [:button {:on-click #(>evt [:debug/add-cube])}
      "Add Cube"]
     [:pre (with-out-str (pprint db))]]))

(defn- debug-hidden
  []
  (let []
    [:button {:on-click #(>evt [:debug/show "debug"])
              :style {:margin 5}}
     "+"]))

(defn debug-component
  []
  (let []
    [:div#debug {:style {:bottom 0
                         :position "absolute"}}
     [debug-hidden]]))

(rf/reg-sub
  :debug/db-state
  (fn [db _]
    db))

(rf/reg-fx
  :mount-debug-panel
  (fn [id]
    (r/render [debug-panel] (.getElementById js/document id))))

(rf/reg-fx
  :hide-debug-panel
  (fn [id]
    (r/render [debug-hidden] (.getElementById js/document id))))

(rf/reg-event-fx
  :debug/show
  (fn [_ [_ id]]
    {:mount-debug-panel id}))

(rf/reg-event-fx
  :debug/hide
  (fn [_ [_ id]]
    {:hide-debug-panel id}))

(rf/reg-event-db
  :debug/add-cube
  (fn [db _]
    (assoc db :cube {:pos [0 0 0]})))

