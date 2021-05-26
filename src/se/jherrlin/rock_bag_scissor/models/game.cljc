(ns se.jherrlin.rock-bag-scissor.models.game
  "This namespace contains the game domain model. It's modelled in `specs` to be
  able to validate and generate entities."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test.check.generators :as gen]
   [clojure.string :as str]))


(s/def ::non-blank-string (s/and string? (complement str/blank?)))

(s/def ::id                (s/with-gen uuid? (fn [] gen/uuid)))
(s/def ::timestamp         inst?)
(s/def ::options           #{:rock :bag :scissor})
(s/def ::player1-name      ::non-blank-string)
(s/def ::player1-choosed   ::options)
(s/def ::player2-name      ::non-blank-string)
(s/def ::player2-choosed   ::options)
(s/def ::game
  (s/keys :req [::id
                ::timestamp
                ::player1-name
                ::player1-choosed
                ::player2-name
                ::player2-choosed]))

(comment
  (gen/generate (s/gen ::game))
  ,)
