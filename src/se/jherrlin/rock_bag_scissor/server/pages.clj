(ns se.jherrlin.rock-bag-scissor.server.pages
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [hiccup.page :refer [html5 include-js include-css]]))


(defn- read-resource-edn-file
  "Read file from filesystem and parse it to edn."
  [resource-filesystem-path]
  (try
    (edn/read-string (slurp (io/resource resource-filesystem-path)))
    (catch java.io.IOException e
      (printf "Couldn't open '%s': %s\n" resource-filesystem-path (.getMessage e)))
    (catch Exception e
      (printf "Error parsing edn file '%s': %s\n" resource-filesystem-path (.getMessage e)))))

(defn index-html
  "Create an index page with a CSRF token attached to it.

  This is mainly used if you wanna use re-frame as a frontend."
  [req]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name    "viewport"
            :content "width=device-width, initial-scale=1"}]
    (include-css "//cdn.jsdelivr.net/npm/semantic-ui@2.4.1/dist/semantic.min.css")]
   [:body {:style "height: 100%" }
    [:div#app {:style           "height: 100%"
               :data-csrf-token (:anti-forgery-token req)} "loading..."]
    (->> "public/js/manifest.edn"
         (read-resource-edn-file)
         (map :output-name)
         (mapv #(str "js/" %))
         (apply include-js))]))
