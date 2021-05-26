(ns se.jherrlin.rock-bag-scissor.client.main
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rd]
            ["semantic-ui-react" :as semantic-ui]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _] {}))

(defn root []
  [:> semantic-ui/Container {:style {:height "100%"}}
   [:> semantic-ui/Segment {:style {:height "100%"}}

    [:> semantic-ui/Grid {:columns 2 :relaxed :very}
     [:> semantic-ui/Grid.Column
      ]
     [:> semantic-ui/Grid.Column]]
    [:> semantic-ui/Divider {:vertical true} "VS"]]])

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [root] (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::initialize-db])
  (mount-root))
