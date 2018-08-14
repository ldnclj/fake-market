(ns fake-market.core
  (:gen-class))

(def sides [:buy :sell])

(def otherside {:buy :sell
                :sell :buy })

(def price-fn {:buy <= :sell >=})

(defn order
  ([] (order (sides (rand-int 2)) (rand-int 200)))
  ([side price]
  {:side  side
   :price price}))

(defn match [{:keys [side price] :as order} book]
  (prn "matching" order book)

  (let [other-side (otherside side)
        orders-other-side (->> book (group-by :side) other-side)
        potential-trade   (if (= side :buy)
                            (first orders-other-side)
                            (last orders-other-side))

        trade-order (when (and potential-trade ((price-fn side) (:price potential-trade) price)) potential-trade)]

    (prn "trade-order" trade-order)

  {:book   (if trade-order (remove #{trade-order} book) (sort-by :price (conj book order)))
   :trades trade-order}))

(defn -main
  [& args]
  (println "Welcome to Fake Market")
  (let [orders (repeatedly 10 order)
        book   []]
    (prn "orders" orders)
    (reduce (fn [book order]
              (-> order
                (match book)
                :book)) [] orders)
    )
  )
