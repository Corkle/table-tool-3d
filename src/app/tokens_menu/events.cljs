(ns app.tokens-menu.events
  (:require
    [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(def >evt dispatch)
(def ^:private menu-id :tokens-menu)

(reg-event-db
  :tokens-menu/token-drag-start
  (fn [db [_ token-id]]
    (assoc-in db [:window :menus menu-id :dragging-item] token-id)))

(reg-event-db
  :tokens-menu/token-drag-end
  (fn [db _]
    (assoc-in db [:window :menus menu-id :dragging-item] nil)))

(defn- get-mouse-offset
  [html-target client-x client-y]
  (let [rect (.getBoundingClientRect html-target)
        shift-x (- client-x (.-left rect))
        shift-y (- client-y (.-top rect))]
    [shift-x shift-y]))

(defn- menu-drag-fn
  [event]
  (let [new-pos [(.-pageX event) (.-pageY event)]]
    (>evt [:window/update-menu-position menu-id new-pos])))

(defn- menu-drag-end-fn
  [_]
  (>evt [:tokens-menu/menu-drag-mouse-up]))

(reg-event-fx
  :tokens-menu/menu-drag-mouse-down
  (fn [cofx [_ event]]
    (let [{:keys [client-x client-y target]} event
          drag-offset (get-mouse-offset (.-parentNode target) client-x client-y)]
      {:db (assoc-in (:db cofx) [:window :drag-drop :mouse-offset] drag-offset)
       :window/add-mouse-listeners {:move menu-drag-fn :up menu-drag-end-fn}})))

(reg-event-fx
  :tokens-menu/menu-drag-mouse-up
  (fn [_ _]
    {:window/remove-mouse-listeners {:move menu-drag-fn :up menu-drag-end-fn}}))

(defn- token-drag-fn
  [event]
  (let [mouse-pos [(.-pageX event) (.-pageY event)]]
    (>evt [:window/update-drag-position mouse-pos])))
  

(defn- token-drop-fn
  [_]
  (println "Drop token")
  (>evt [:tokens-menu/token-drag-mouse-up]))

(reg-event-fx
  :tokens-menu/token-mouse-down
  (fn [cofx [_ token-id event]]
    (let [tokens (get-in (:db cofx) [:window :menus menu-id :tokens-list])
          selected-item (first (filter #(= (:id %) token-id) tokens))
          db (assoc-in (:db cofx) [:window :menus menu-id :selected-item] selected-item)
          {:keys [client-x client-y target]} event
          drag-offset (get-mouse-offset target client-x client-y)
          db (assoc-in db [:window :drag-drop :mouse-offset] drag-offset)]
      {:db db
       :window/add-mouse-listeners {:move token-drag-fn :up token-drop-fn}})))

(reg-event-fx
  :tokens-menu/token-drag-mouse-up
  (fn [cofx _]
    (let []
      {:db (assoc-in (:db cofx) [:window :drag-drop :dragging?] false)
       :window/remove-mouse-listeners {:move token-drag-fn :up token-drop-fn}})))
