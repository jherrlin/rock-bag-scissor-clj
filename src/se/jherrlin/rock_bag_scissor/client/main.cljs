(ns se.jherrlin.rock-bag-scissor.client.main
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rd]
            [se.jherrlin.rock-bag-scissor.client.semantic.form.managed :as form.managed]
            [se.jherrlin.rock-bag-scissor.models.game :as game]
            [se.jherrlin.rock-bag-scissor.shared.business-logic :as business-logic]
            ["semantic-ui-react" :as semantic-ui]
            [clojure.spec.alpha :as s]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _] {}))

(defn player
  "Player form component"
  [form name choosed]
  [:<>
   [form.managed/input
    {:form        form
     :attr        name
     :placeholder "Name"
     :label       "Player 1"}]
   [form.managed/select {:label   "Choose"
                         :form    form
                         :attr    choosed
                         :options (->> [nil :rock :bag :scissor]
                                       (map (fn [v] {:value v :text v})))}]])

(defn root []
  (let [form   :game
        values @(re-frame/subscribe [:form-values form])]
    [:div
     [:> semantic-ui/Container {:style {:height "100%"}}
      [:> semantic-ui/Segment {:style {:height "100%"}}
       [:> semantic-ui/Grid {:columns 2 :relaxed :very}
        [:> semantic-ui/Grid.Column
         [player form ::game/player1-name ::game/player1-choosed]]
        [:> semantic-ui/Grid.Column
         [player form ::game/player2-name ::game/player2-choosed]]]
       [:> semantic-ui/Divider {:vertical true} "VS"]]
      [:div {:style {:display "flex"
                     :justify-content "center"}}
       [:> semantic-ui/Button
        {:onClick  #(->> (business-logic/game values)
                         :winner
                         (str "Winner is:")
                         (js/alert))
         :disabled (not (s/valid? ::game/game values))} "Play!"]]]
     [:div
      [:pre
       (str "Form values for debug:\n"
            (with-out-str (cljs.pprint/pprint values)))]]]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [root] (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch [:form-values :game (business-logic/new)])
  (mount-root))
