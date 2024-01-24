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

(defn parse-input [x default]
  (if (nil? x)
    default
    (parse-double x)))

(defn home-page [request]
  (let [input-to-sqrt (get-in request [:params :to-sqrt] nil)
        to-sqrt (parse-input input-to-sqrt nil)
        input-precision (get-in request [:params :precision] nil)
        precision (parse-input input-precision 0.01)
        is-valid? (verify/verify-request to-sqrt precision)
        is-post? (= (:request-method request) :post)
        sqrt-steps (when (and is-post? is-valid?)
                     (newton/sqrt to-sqrt precision))
        error-msg (when (and is-post? (not is-valid?))
                    "Validation failed on the request")]
    (html-response (views/home-view
                    to-sqrt
                    precision
                    sqrt-steps
                    error-msg))))

(defn about-page [request]
  (html-response (views/about-view)))

(def handler
  (ring/ring-handler
   (ring/router
    [["/" {:get home-page :post home-page}]
     ["/about" {:get about-page}]])))

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
