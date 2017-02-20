(ns clj-bean.bean-test
  (:require [clojure.test :refer :all]
            [clj-bean.core :refer :all]))

(defbean clj_bean.bean_test.Edit
  [[long timestamp]
   [String diffUrl]
   [boolean unpatrolled]
   [long byteDiff]])

(deftest javabean-test
  (let [bean (clj_bean.bean_test.Edit. 1485378314 "https://wjoel.com" true 123)]
    (do
      (is (= 1485378314 (.getTimestamp bean)))
      (.setTimestamp bean 1485378350)
      (is (= 1485378350 (.getTimestamp bean))))
    (do
      (is (= "https://wjoel.com" (.getDiffUrl bean)))
      (.setDiffUrl bean "https://clojure.org")
      (is (= "https://clojure.org" (.getDiffUrl bean))))
    (do
      (is (= true (.isUnpatrolled bean)))
      (.setUnpatrolled bean false)
      (is (= false (.isUnpatrolled bean))))
    (is (= 123 (.getByteDiff bean)))))
