(ns fake-market.core-test
  (:require [clojure.test :refer :all]
            [fake-market.core :refer :all]))



(deftest order-matching
         (testing "An order with empty book adds order to book"
                  (is (= {:book [(order :buy 1)] :trades nil}
                         (match (order :buy 1) []))))

         (testing "An order with the same price as another on the book should trade"
                  (is (= {:book []
                          :trades {:side :sell :price 1}}
                         (match {:side :buy :price 1} [{:side :sell :price 1}]))))

         (testing "An order with a better price than another order on the book should trade"
                  (is (= {:book []
                          :trades {:side :buy :price 75}}
                         (match {:side :sell :price 50} [{:side :buy :price 75}]))))

         (testing "An order with a worse price than the other order should not trade"
                  (is (= {:book [{:side :buy :price 25} {:side :sell :price 50}]
                          :trades nil}
                         (match {:side :sell :price 50} [{:side :buy :price 25}]))))

         (testing "Best buy price from book should be returned"
                  (is (= {:book [(order :buy 110)] :trades (order :buy 120)}
                         (match (order :sell 100) [(order :buy 110) (order :buy 120)])
                         )))

         (testing "Best sell price from book should be returned"
                  (is (= {:book [(order :sell 120)] :trades (order :sell 110)}
                         (match (order :buy 130) [(order :sell 110) (order :sell 120)])
                         )))
         )

