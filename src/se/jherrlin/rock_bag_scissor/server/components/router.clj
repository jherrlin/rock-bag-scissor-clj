(ns se.jherrlin.rock-bag-scissor.server.components.router
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))

(defrecord Router [args]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting router component.")
    (let [handler (:handler args)]
      (assoc this :handler handler)))

  (stop [this]
    (timbre/info "Stopping router component.")
    (assoc this :handler nil)))

(defn create
  "Create a new router component."
  [config]
  (map->Router {:args config}))
