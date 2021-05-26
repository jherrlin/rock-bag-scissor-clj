(ns se.jherrlin.rock-bag-scissor.server.handler
  (:require [se.jherrlin.rock-bag-scissor.server.pages :as pages]
            [muuntaja.core :as m]
            [reitit.coercion.malli]
            reitit.coercion.spec
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            reitit.ring.malli
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            ring.middleware.anti-forgery
            ring.middleware.keyword-params
            ring.middleware.params
            ring.middleware.session
            ring.middleware.cookies
            [taoensso.timbre :as timbre]))

(def handler
  (ring/ring-handler
   (ring/router
    [""
     ["/" {:summary "Index page"
           :get (fn [req] {:status 200 :body (pages/index-html req)})}]
     ["/health" {:summary "Health checking endpoint"
                 :get (constantly {:status 200 :body "ok"})}]]
    {:exception pretty/exception
     :data      {:middleware [ring.middleware.anti-forgery/wrap-anti-forgery]}})
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found 666"})}))
   ))
