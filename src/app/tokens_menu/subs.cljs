(ns app.tokens-menu.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :tokens-menu
  (fn [db _]
    (get-in db [:window :menus :tokens-menu])))

(reg-sub
  :tokens-menu/selected-item
  (fn [db _]
    (get-in db [:window :menus :tokens-menu :selected-item])))

(reg-sub
  :tokens-menu/tokens-list
  (fn [db _]
    (get-in db [:window :menus :tokens-menu :tokens-list])))
