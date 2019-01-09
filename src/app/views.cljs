(ns app.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [app.characters-menu.views :refer [characters-menu]]
            [app.tokens-menu.views :refer [tokens-menu]]
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

(defn- nav-item
  [icon-class text on-click-fn]
  [:a.left-nav-item {:on-click on-click-fn}
   [:span.icon.is-large
    [:i.fas.fa-2x {:class icon-class}]]
   [:span.nav-text {:style {:display "block"}} text]])

(defn- left-nav-characters
  []
  [nav-item "fa-users" "Characters" #(>evt [:window/bring-menu-to-front-or-hide :characters-menu])])

(defn- left-nav-tokens
  []
  [nav-item "fa-dragon" "Tokens" #(>evt [:window/bring-menu-to-front-or-hide :tokens-menu])])

(defn- left-nav-spells
  []
  [nav-item "fa-book" "Spells"])

(defn- left-nav-dungeon
  []
  [nav-item "fa-dungeon" "Dungeon"])

(defn- left-navbar
  []
  (let [ui (<sub [:window/left-nav])
        text-visible? (:text-visible? ui)]
    [:nav#left-nav.menu {:class (if text-visible? "show-text" "")
                         :on-mouse-over #(>evt [:window/show-left-nav-text])
                         :on-mouse-out #(>evt [:window/hide-left-nav-text])}
     [left-nav-characters]
     [left-nav-tokens]
     [left-nav-spells]
     [left-nav-dungeon]
     [:button {:style {:bottom 0}
               :on-click #(>evt [:window/change-panel :start])}
      "START"]]))

(defn- drag-overlay
  []
  (let [drag-drop (<sub [:window/drag-drop])
        img (:image drag-drop)
        [x y] (:image-pos drag-drop)]
    [:div#drag-overlay
     (if (:dragging? drag-drop)
       [:div {:style {:position "absolute"
                      :top y
                      :left x
                      :width 100
                      :height 100
                      :opacity 0.8
                      :background-repeat "no-repeat"
                      :background-size "cover"
                      :background-image (str "url(" img ")")}}])]))

(defn- menus-overlay
  []
  (let []
    [:div#menus-overlay
     [characters-menu]
     [tokens-menu]]))

(defn- app-panel
  []
  (let []
    [:div {:style {:height "100vh"}}
     [webgl/webgl-component]
     [left-navbar]
     [drag-overlay]
     [menus-overlay]
     [debug-component]]))

(defn main-app
  []
  (let [panel (<sub [:window/current-panel])]
    (case panel
      :start [start-panel]
      :webgl [app-panel]
      [:div "ERROR"])))
