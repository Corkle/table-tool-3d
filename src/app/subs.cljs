(ns app.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :window/current-panel
  (fn [db _]
    (:current-panel (:window db))))

(reg-sub
  :window/players-menu
  (fn [db _]
    (get-in db [:window :menus :players-menu])))
