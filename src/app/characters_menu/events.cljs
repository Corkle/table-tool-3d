(ns app.characters-menu.events
  (:require
    [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(def >evt dispatch)
(def ^:private menu-id :characters-menu)

(defn- menu-drag-fn
  [event]
  (let [new-pos [(.-pageX event) (.-pageY event)]]
    (>evt [:window/update-menu-position menu-id new-pos])))

(defn- menu-drag-end-fn
  [_]
  (>evt [:characters-menu/menu-drag-mouse-up]))

(reg-event-fx
  :characters-menu/menu-drag-mouse-down
  (fn [cofx [_ event]]
    (let [{:keys [target client-x client-y]} event
          rect (.getBoundingClientRect (.-parentNode target))
          shift-x (- client-x (.-left rect))
          shift-y (- client-y (.-top rect))
          drag-offset [shift-x shift-y]]
      {:db (assoc-in (:db cofx) [:window :drag-drop :mouse-offset] drag-offset)
       :window/add-mouse-listeners {:move menu-drag-fn :up menu-drag-end-fn}})))

(reg-event-fx
  :characters-menu/menu-drag-mouse-up
  (fn [_ _]
    {:window/remove-mouse-listeners {:move menu-drag-fn :up menu-drag-end-fn}}))
