(ns sqrt-site.newton
  (:require [clojure.math :as math]))

(defn average [x y]
  (/ (+ x y) 2))

(defn improve [guess squared]
  (double (average guess (/ squared guess))))

(defn good-enough? [guess squared precision]
  (< (abs (- (math/pow guess 2) squared)) precision))

(defn sqrt [squared precision]
  (loop [current-guess (/ squared 2)
         all-attempts []]
    (let [new-attempts (conj all-attempts current-guess)]
      (if (good-enough? current-guess squared precision)
        new-attempts
        (recur (improve current-guess squared) new-attempts)))))
