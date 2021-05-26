(ns se.jherrlin.rock-bag-scissor.client.semantic.form.managed
  "Managed forms/inputs have default opinions on how state should be managed. They
  can be used in most cases. The behavior can always be overridden."
  (:require [re-frame.core :as re-frame]
            [clojure.spec.alpha :as s]
            se.jherrlin.rock-bag-scissor.client.form.events
            ["semantic-ui-react" :as semantic-ui]
            ["semantic-ui-calendar-react" :as semantic-ui-calendar]
            [clojure.string :as str]))


(defn input [{:keys [attr error-fn form label read-only required spec validate?]
              :as   prop}]
  {:pre [(keyword? form) (keyword? attr)]}
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str attr value*)})))]
    [:> semantic-ui/Form.Input
     (merge
      {:fluid    true
       :required required
       :label    label
       :readOnly read-only
       :value    (or @value "")
       :onChange #(let [v (.. % -target -value)]
                    (re-frame/dispatch [:form-value form attr v]))
       :error    (error-fn @value)}
      prop)]))

(defn select [{:keys [label form attr options validate? spec error-fn] :as props}]
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str attr value*)})))]
    [:> semantic-ui/Form.Select
     (merge
      {:fluid     true
       :label     label
       :value     @value
       :search    true
       :selection true
       :options   options
       :onChange  (fn [_ data]
                    (let [{:keys [value]} (js->clj data :keywordize-keys true)]
                      (re-frame/dispatch [:form-value form attr value])))
       :error     (error-fn @value)}
      props)]))
