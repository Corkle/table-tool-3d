(ns app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :window/current-panel
  (fn [db _]
    (get-in db [:window :current-panel])))

(defn- get-top-menu
  [db]
  (let [indexes (get-in db [:window :menu-indexes])]
    (reduce-kv (fn [m k v]
                 (if (> v (m indexes)) k m)) :nil indexes)))

(reg-sub
  :window/drag-drop
  (fn [db _]
    (let [menu (get-top-menu db)
          image (get-in db [:window :menus menu :selected-item :img])
          drag-drop (get-in db [:window :drag-drop])]
      (assoc drag-drop :image image))))

(reg-sub
  :window/dragging?
  (fn [db _]
    (get-in db [:window :drag-drop :dragging?])))

(reg-sub
  :window/get-menu-index
  (fn [db [_ menu-id]]
    (get-in db [:window :menu-indexes menu-id])))

(reg-sub
  :window/left-nav
  (fn [db _]
    (get-in db [:window :left-nav])))
