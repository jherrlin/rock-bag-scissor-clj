(ns se.jherrlin.rock-bag-scissor.shared.business-logic
  "This is the main business logic for the game."
  (:require [se.jherrlin.rock-bag-scissor.models.game :as game]
            [clojure.spec.alpha :as s]
            #?(:clj  [clojure.core.match :refer [match]]
               :cljs [cljs.core.match :refer-macros [match]])
            [clojure.test.check.generators :as gen]))


(defn rules [a b]
  {:pre [(s/valid? ::game/options a)
         (s/valid? ::game/options a)]}
  (match [a b]
         [:rock :bag]     :bag
         [:rock :scissor] :rock
         [:bag :rock]     :bag
         [:bag :scissor]  :scissor
         [:scissor :rock] :rock
         [:scissor :bag]  :scissor
         :else            a))

(defn winner
  "Evaluate the winner of the game."
  [{::game/keys [player1-choosed player1-name
                 player2-choosed player2-name]}]
  (cond
    (= player1-choosed player2-choosed)                         nil
    (= player1-choosed (rules player1-choosed player2-choosed)) player1-name
    (= player2-choosed (rules player1-choosed player2-choosed)) player2-name))

(defn game
  "Run a game and present the winner."
  [game]
  {:pre [(s/valid? ::game/game game)]}
  {:game   game
   :winner (winner game)})

(defn new
  "Generate `id` and `timestamp` for a new game."
  []
  {::game/id        #?(:clj  (java.util.UUID/randomUUID)
                       :cljs (random-uuid))
   ::game/timestamp #?(:clj  (java.util.Date.)
                       :cljs (js/Date.))})

(comment
  (game (gen/generate (s/gen ::game/game)))
  ,)
