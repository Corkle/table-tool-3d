(ns app.tokens-menu.views
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [app.tokens-menu.events]
    [app.tokens-menu.subs]))

(def <sub (comp deref subscribe))
(def >evt dispatch)

(defn- get-event-props
  [event]
  (let [target (.-target event)
        x (.-clientX event)
        y (.-clientY event)]
    {:target target :client-x x :client-y y}))

(defn- token-list-item
  [token selected? dragging?]
  (let [{:keys [id img]} token]
    [:img.token-list-item {:src img
                           :class (if selected? (if dragging? "selected dragging" "selected"))
                           :draggable false
                           :on-mouse-down #(>evt [:tokens-menu/token-mouse-down id (get-event-props %)])
                           :style {:height 100
                                   :width 100
                                   :padding 5
                                   :margin 5}}]))

(defn- token-list
  [tokens selection dragging?]
  (fn [tokens selection dragging?]
    [:div.token-list
     (for [token tokens]
       (let [selected? (= token selection)]
         ^{:key token} [token-list-item token selected? dragging?]))]))

(defn- menu-content
  []
  (let [tokens (<sub [:tokens-menu/tokens-list])
        selection (<sub [:tokens-menu/selected-item])
        dragging? (<sub [:window/dragging?])]
      [:div.menu-contents {:style {:display "flex"
                                   :flex-wrap "wrap"}}
       [token-list tokens selection dragging?]]))

(defn tokens-menu
  []
  (let [z-index (<sub [:window/get-menu-index :tokens-menu])
        ui (<sub [:tokens-menu])
        [x y] (:pos ui)]
    (if z-index
      [:div#tokens-menu.menu-container {:on-mouse-down #(>evt [:window/bring-menu-to-front :tokens-menu])
                                        :style {:top y
                                                :left x
                                                :z-index z-index
                                                :max-width 390
                                                :min-width 200}}
       [:div.menu-title-bar
        {:on-mouse-down #(>evt [:tokens-menu/menu-drag-mouse-down (get-event-props %)])}
        "Tokens"]
       [:a.menu-close-button {:on-click #(>evt [:window/hide-menu :tokens-menu])}
        [:span.icon
         [:i.fas.fa-times]]]
       [menu-content]])))
