(ns sqrt-site.main
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [ring.util.http-response :as response]
            [ring.middleware.defaults :as defaults]
            [sqrt-site.views :as views]
            [sqrt-site.verification :as verify]
            [clojure.math :as math]
            [sqrt-site.newton :as newton]))

(defonce server (atom nil))

(defn html-response [content]
  (-> (str content)
      response/ok
      (response/content-type "text/html")))

(defn parse-input
  ( [x default] (if (nil? x)
                  default
                  (parse-double x)))
  ( [x] (parse-input x nil)))

(defn home-page [request]
  (html-response (views/home-view)))

(defn about-page [request]
  (html-response (views/about-view)))

(defn sqrt-post [request]
  (let [to-sqrt (-> (get-in request [:params :to-sqrt] nil) parse-input)
        precision (-> (get-in request [:params :precision] nil) parse-input)
        response (if (verify/verify-request to-sqrt precision)
                   (views/sqrt-calc-view to-sqrt precision (newton/sqrt to-sqrt precision))
                   (views/error-alert "There was an error processing this request."))]
    (html-response response)))

(def handler
  (ring/ring-handler
   (ring/router
    [["/" {:get home-page :post home-page}]
     ["/about" {:get about-page}]
     ["/calc" {:post sqrt-post}]])))

(defn start! []
  (reset!
   server
   (jetty/run-jetty (-> #'handler (defaults/wrap-defaults defaults/site-defaults) )
                    {:join? false
                     :port 8080})))

(defn stop! []
  (.stop @server)
  (reset! server nil))

(defn -main [& args]
  (start!))
