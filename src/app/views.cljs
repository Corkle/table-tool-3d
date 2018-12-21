(ns app.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [app.webgl.views :as webgl]
            [app.debug.core :refer [debug-component]]))

(def <sub (comp deref subscribe))
(def >evt dispatch)

(defn- start-panel
  []
  [:div
   [:p "START"]
   [:button {:on-click #(>evt [:window/change-panel :webgl])}
    "WEBGL"]])

(defn- get-drag-offset
  [event]
  (let [container (.-parentNode (.-target event))
        rect (.getBoundingClientRect container)
        shift-x (- (.-clientX event) (.-left rect))
        shift-y (- (.-clientY event) (.-top rect))]
    [shift-x shift-y]))

(defn- players-menu
  []
  (let [ui (<sub [:window/players-menu])
        visibility (if (:visible? ui) "visible" "hidden") 
        [x y] (:pos ui)]
    [:div#players-menu.menu-container {:style {:min-width 300
                                               :visibility visibility
                                               :top y
                                               :left x}}
     [:div.menu-title-bar
      {:on-mouse-down #(>evt [:window/draggable-menu-mouse-down :players-menu (get-drag-offset %)])
       :on-mouse-up #(>evt [:window/draggable-menu-mouse-up :players-menu])}
      "Players"]
     [:div.menu-contents
      [:a 
       [:span "New player"]]
      [:a
       [:span "Open player list"]]]]))

(defn- nav-icon
  [icon-class text]
  [:a.nav-icon
   [:span.icon.is-large
    [:i.fas.fa-2x {:class icon-class}]]
   [:span {:style {:display "block"}} text]])

(defn- left-nav-players
  []
  [:div.left-nav-item
   [nav-icon "fa-users" "Players"]])

(defn- left-nav-monsters
  []
  [:div.left-nav-item
   [nav-icon "fa-dragon" "Monsters"]])

(defn- left-nav-spells
  []
  [:div.left-nav-item
   [nav-icon "fa-book" "Spells"]])

(defn- left-nav-dungeon
  []
  [:div.left-nav-item
   [nav-icon "fa-dungeon" "Dungeon"]])

(defn- left-navbar
  []
  (let []
    [:nav#left-nav.menu
     [left-nav-players]
     [left-nav-monsters]
     [left-nav-spells]
     [left-nav-dungeon]
     [:button {:style {:bottom 0}
               :on-click #(>evt [:window/change-panel :start])}
      "START"]]))

(defn- menus-overlay
  []
  (let []
    [:div#menus-overlay
     [players-menu]]))

(defn- app-panel
  []
  (let []
    [:div {:style {:height "100vh"}}
     [webgl/webgl-component]
     [left-navbar]
     [menus-overlay]
     [debug-component]]))

(defn main-app
  []
  (let [panel (<sub [:window/current-panel])]
    (case panel
      :start [start-panel]
      :webgl [app-panel]
      [:div "ERROR"])))
