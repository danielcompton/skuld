(ns skuld.util-test
  (:use skuld.util
        clojure.test
        clojure.pprint))

(deftest sorted-interleave-test
  (is (= [1 2 3 4 5 6 7]
         (sorted-interleave [1 5]
                            []
                            [2 4 6 7]
                            nil
                            [3])))
  (testing "infinite"
    (is (= [1 1 2 2 3 3 4 5 6 7]
           (->> (iterate inc 1)
                (sorted-interleave [1 2 3])
                (take 10)))))

  (testing "bench"
    (time (dorun (sorted-interleave (take 1000000 (iterate inc 0))
                                    (take 1000000 (iterate inc 1))
                                    (range 500 100000))))))

(deftest compare+-test
  (let [a {:a 0 :b 1 :c 2}
        b {:a 2 :b 1 :c 0}]
    (testing "unary"
      (is (= -1 (compare+ a b :a)))
      (is (= 0  (compare+ a b :b)))
      (is (= 1  (compare+ a b :c))))

    (testing "binary"
      (is (= -1 (compare+ a b :a :c)))
      (is (= 1  (compare+ a b :c :a)))
      (is (= -1 (compare+ a b :b :a)))
      (is (= 1  (compare+ a b :b :c)))
      (is (= -1 (compare+ a b :a :b)))
      (is (= 1  (compare+ a b :c :b))))

    (testing "ternary"
      (is (= -1 (compare+ a b :b :b :a)))
      (is (= -1 (compare+ a b :b :a :c))))))

(deftest majority-test
  (are [num-nodes majority-num]
    (is (= majority-num (majority num-nodes)))
    0 0
    1 1
    2 2
    3 2
    4 3
    5 3
    10 6
    15 8))

(deftest majority-value-test
  (are [value values]
    (is (= value (majority-value values)))
    nil []
    nil [:a :b]
    :a  [:a :a :b]
    nil [:a :b :c]))

(deftest assocv-test
  (are [res vec idx val]
    (is (= res (assocv vec idx val)))
    [:b nil nil nil nil :a] [:b] 5 :a))
