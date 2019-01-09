(ns app.characters-menu.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :characters-menu
  (fn [db _]
    (get-in db [:window :menus :characters-menu])))
