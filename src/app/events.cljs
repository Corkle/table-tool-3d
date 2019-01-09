(ns app.events
  (:require
    [re-frame.core :refer [reg-event-db reg-event-fx reg-fx dispatch]]
    [app.db :refer [default-db]]))

(def >evt dispatch)

(reg-event-db
  :initialize-db
  (fn [_ _]
    default-db))

(reg-event-db
  :window/change-panel
  (fn [db [_ panel]]
    (assoc-in db [:window :current-panel] panel)))

(reg-event-db
  :window/show-left-nav-text
  (fn [db _]
    (assoc-in db [:window :left-nav :text-visible?] true)))

(reg-event-db
  :window/hide-left-nav-text
  (fn [db _]
    (assoc-in db [:window :left-nav :text-visible?] false)))

(defn- move-to-front
  "If item has index, remove it and add at top (X99), otherwise add it at top. Dec existing indexes if needed."
  [coll item]
  (if-let [i (get coll item)]
    (reduce-kv (fn [m k v]
                 (assoc m k (if (> v i) (dec v) v))) {item 699} (dissoc coll item))
    (reduce-kv (fn [m k v] (assoc m k (dec v))) {item 699} coll)))

(defn- move-menu-to-front
  "Changes menu-id value to topmost value (X99). Returns original hashmap if already at top."
  [indexes menu-id]
  (if (= 699 (menu-id indexes))
    indexes
    (move-to-front indexes menu-id)))

(reg-event-db
  :window/bring-menu-to-front
  (fn [db [_ menu-id]]
    (let [indexes (get-in db [:window :menu-indexes])
          new-indexes (move-menu-to-front indexes menu-id)]
      (if (= indexes new-indexes)
        db
        (assoc-in db [:window :menu-indexes] new-indexes)))))

(reg-event-db
  :window/bring-menu-to-front-or-hide
  (fn [db [_ menu-id]]
    (let [indexes (get-in db [:window :menu-indexes])
          new-indexes (move-menu-to-front indexes menu-id)]
      (if (= indexes new-indexes)
        (do (>evt [:window/hide-menu menu-id]) db)
        (assoc-in db [:window :menu-indexes] new-indexes)))))

(defn- remove-menu-index
  [indexes menu-id]
  (if-let [i (get indexes menu-id)]
    (reduce-kv (fn [m k v]
                 (assoc m k (if (< v i) (inc v)))) {} (dissoc indexes menu-id))
    indexes))

(reg-event-db
  :window/hide-menu
  (fn [db [_ menu-id]]
    (let [indexes (get-in db [:window :menu-indexes])
          new-indexes (remove-menu-index indexes menu-id)]
      (assoc-in db [:window :menu-indexes] new-indexes))))

(reg-event-db
  :window/update-drag-position
  (fn [db [_ [mouse-x mouse-y]]]
    (let [[offset-x offset-y] (get-in db [:window :drag-drop :mouse-offset])
          img-pos [(- mouse-x offset-x) (- mouse-y offset-y)]]
      (update-in db [:window :drag-drop] merge {:dragging? true :image-pos img-pos}))))

(reg-event-db
  :window/update-menu-position
  (fn [db [_ menu-id [x y]]]
    (let [[shift-x shift-y] (get-in db [:window :drag-drop :mouse-offset])
          new-x (- x shift-x)
          new-y (- y shift-y)]
      (assoc-in db [:window :menus menu-id :pos] [new-x new-y]))))

(reg-fx
  :window/add-mouse-listeners
  (fn [listeners]
    (let [{:keys [move up]} listeners]
      (.addEventListener js/document "mousemove" move)
      (.addEventListener js/document "mouseup" up))))

(reg-fx
  :window/remove-mouse-listeners
  (fn [listeners]
    (let [{:keys [move up]} listeners]
      (.removeEventListener js/document "mousemove" move)
      (.removeEventListener js/document "mouseup" up))))
