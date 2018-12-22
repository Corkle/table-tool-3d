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
  :window/close-menu
  (fn [db [_ menu-id]]
    (assoc-in db [:window :menus menu-id :visible?] false)))

(reg-event-db
  :window/toggle-menu-visible
  (fn [db [_ menu-id]]
    (update-in db [:window :menus menu-id :visible?] not)))

(reg-event-db
  :window/update-menu-position
  (fn [db [_ menu-id [x y]]]
    (let [menu (get-in db [:window :menus menu-id])
          [shift-x shift-y] (:mouse-drag-offset menu)
          new-x (- x shift-x)
          new-y (- y shift-y)
          new-menu (assoc-in menu [:pos] [new-x new-y])]
      (assoc-in db [:window :menus menu-id] new-menu))))

(defn- move-players-menu-fn
  [event]
  (let [new-pos [(.-pageX event) (.-pageY event)]]
    (>evt [:window/update-menu-position :players-menu new-pos])))

(reg-event-fx
  :window/draggable-menu-mouse-down
  (fn [cofx [_ menu-id drag-offset]]
    (let [listener-fn (case menu-id
                        :players-menu move-players-menu-fn)]
      {:db (assoc-in (:db cofx) [:window :menus menu-id :mouse-drag-offset] drag-offset)
       :add-menu-drag-listener listener-fn})))

(reg-fx
  :add-menu-drag-listener
  (fn [listener-fn]
    (.addEventListener js/document "mousemove" listener-fn)))

(reg-event-fx
  :window/draggable-menu-mouse-up
  (fn [_ [_ menu-id]]
    (case menu-id
      :players-menu {:remove-menu-drag-listener move-players-menu-fn})))

(reg-fx
  :remove-menu-drag-listener
  (fn [listener-fn]
    (.removeEventListener js/document "mousemove" listener-fn)))
