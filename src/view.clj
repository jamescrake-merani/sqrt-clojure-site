(ns sqrt-site.views
  (:require [hiccup2.core :as h]
            [ring.util.anti-forgery :as af]
            [sqrt-site.newton :as newton]
            [clojure.math :as math]))

(defn template [current-page]
  (h/html
   [:html {:lang "en"}
    [:head
     [:link {:rel "stylesheet"
             :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
             :integrity "sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
             :crossorigin "anonymous"}]]
    [:body
     [:div.container
      [:h1 "Template"]
      [:hr]
      current-page]]]))

(defn sqrt-form []
  (h/html
   [:form {:method "POST"}
    (h/raw (af/anti-forgery-field))
    [:input.form-control {:type "number" :name "to-sqrt"}]
    [:button.btn.btn-primary {:type "submit"} "Calculate"]]))

(defn home-view [to-sqrt]
  (template
   (h/html
    [:div
     [:h1 "This is the home page"]
     [:p "It doesn't have anything on it at the moment."]
     (sqrt-form)
     (when-not (nil? to-sqrt)
       [:div
        [:hr]
        (sqrt-calc-view (parse-double to-sqrt))])])))

(defn value-card [value to-sqrt]
  [:div.card
   [:div.card-header "Attempt"]
   [:div.card-body
    [:p.card-text (format "√%s ≈ %s" to-sqrt value)] ]])

(defn sqrt-calc-view [to-sqrt]
  (let [sqrt-steps (newton/sqrt to-sqrt 0.01)]
    (doall
     (map #(value-card % to-sqrt) sqrt-steps))))

(defn about-view []
  (template
   (h/html
    [:div
     [:h1 "About Page"]
     [:p "This will eventually have a description of Newton's method of
     calculating the sqrt but right now is empty"]])))
