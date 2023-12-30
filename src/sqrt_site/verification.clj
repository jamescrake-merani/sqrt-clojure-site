(ns sqrt-site.verification)

(def precision-max 0.1)
(def precision-min 0.0000001)

(def to-sqrt-min 0.0000001)
(def to-sqrt-max 100000000)

(defn verify-precision [p]
  (and (>= p precision-min) (< p precision-max)))

(defn verify-to-sqrt [s]
  (and (>= s to-sqrt-min) (< s to-sqrt-max)))

(defn verify-request
  "Returns a boolean of whether the request is valid"
  [to-sqrt precision]
  (and
   (verify-precision precision)
   (verify-to-sqrt to-sqrt)
   (not (nil? precision))
   (not (nil? to-sqrt))))
