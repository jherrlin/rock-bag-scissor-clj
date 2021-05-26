(ns se.jherrlin.rock-bag-scissor.server.system
  "This namespace declair the different type of systems available.
  One reason for having multiple systems is to be able to run two different
  systems at the same time."
  (:refer-clojure :exclude [test])
  (:require [com.stuartsierra.component :as component]
            [se.jherrlin.rock-bag-scissor.server.handler :as handler]
            [se.jherrlin.rock-bag-scissor.server.components.httpkit :as components.httpkit]
            [se.jherrlin.rock-bag-scissor.server.components.router :as components.router]))

(defn create
  "Create a new system.

  This system contains two components and the relationship between them are
  described. `:webserver` has a dependency on `:router`."
  [{:keys [webserver router]}]
  (component/system-map
   :router      (components.router/create router)
   :webserver   (component/using
                 (components.httpkit/create webserver)
                 [:router])))

;; This is the production system instance.
(defonce production
  (create
   {:router    {:handler handler/handler}
    :webserver {:port 8080}}))

;; This is the test system instance. It's not used in this app but it's here to
;; show how a test system can be run in parallel with the production.
(defonce test
  (create
   {:router    {:handler handler/handler}
    :webserver {:port 8081}}))

(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)
)
