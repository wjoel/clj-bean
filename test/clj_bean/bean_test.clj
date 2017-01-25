(ns clj-bean.bean-test
  (:require [clojure.test :refer :all]
            [clj-bean.core :refer :all]))

(defbean clj_bean.bean_test.Edit
  [[long timestamp]
   [String diffUrl]
   [long byteDiff]])

(deftest javabean-test
  (let [bean (clj_bean.bean_test.Edit. 1485378314 "https://wjoel.com" 123)]
    (is (= 1485378314 (.getTimestamp bean)))
    (is (= "https://wjoel.com" (.getDiffUrl bean)))
    (is (= 123 (.getByteDiff bean)))))
