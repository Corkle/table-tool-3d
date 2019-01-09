(ns app.characters-menu.views
  (:require [re-frame.core :refer [dispatch subscribe]]
            [app.characters-menu.events]
            [app.characters-menu.subs]))

(def <sub (comp deref subscribe))
(def >evt dispatch)

(defn- get-event-props
  [event]
  (let [target (.-target event)
        x (.-clientX event)
        y (.-clientY event)]
    {:target target :client-x x :client-y y}))

(defn- menu-content
  []
  [:div.menu-contents
   [:div {:style {:width "100%"
                  :background-color "#E0E0E0"}}
    [:a {:style {:width "100%"}}
     [:span.icon {:style {:padding 30}}
      [:i.fas.fa-plus]]
     [:span "New Character"]]]
   [:div
    [:a
     [:span.icon {:style {:padding 30}}
      [:i.fas.fa-plus]]
     [:span "New Character"]]]])

(defn characters-menu
  []
  (let [z-index (<sub [:window/get-menu-index :characters-menu])
        ui (<sub [:characters-menu])
        [x y] (:pos ui)]
    (if z-index 
      [:div#characters-menu.menu-container {:on-mouse-down #(>evt [:window/bring-menu-to-front :characters-menu])
                                            :style {:min-width 300
                                                    :z-index z-index
                                                    :top y
                                                    :left x}}
       [:div.menu-title-bar
        {:on-mouse-down #(>evt [:characters-menu/menu-drag-mouse-down (get-event-props %)])}
        "Player Characters"]
       [:a.menu-close-button {:on-click #(>evt [:window/hide-menu :characters-menu])}
        [:span.icon
         [:i.fas.fa-times]]]
       [menu-content]])))
