(ns clj-bean.core-test
  (:require [clojure.test :refer :all]
            [clj-bean.core :refer :all]))

(deftest sym->upcase-1-str-test
  (testing "sym->upcase-1-str"
    (is (= "ByteDiff" (sym->upcase-1-str 'byteDiff)))
    (is (= "Foo" (sym->upcase-1-str 'foo)))
    (is (= "CAPS" (sym->upcase-1-str 'CAPS)))
    (is (= "CAPS" (sym->upcase-1-str 'cAPS)))))

(deftest typed-field->declarations-test
  (testing "generation of method declarations"
    (is (= '([getByteDiff [] Long]
             [setByteDiff [Long] void])
           (typed-field->declarations '[Long byteDiff])))
    (is (= '([getIsOwner [] boolean]
             [setIsOwner [boolean] void])
           (typed-field->declarations '[boolean isOwner])))
    (is (= '([getTimestamp [] long]
             [setTimestamp [long] void])
           (typed-field->declarations '[long timestamp])))))

(deftest typed-field->accessors-test
  (testing "generation of accessors"
    (is (= '((clojure.core/defn Test-getByteDiff [this]
               (clojure.core/let [state (.state this)
                                  type-array (clojure.core/aget state 0)]
                 (clojure.core/aget type-array 0)))
             (clojure.core/defn Test-setByteDiff [this byteDiff]
               (clojure.core/let [state (.state this)
                                  type-array (clojure.core/aget state 0)]
                 (clojure.core/aset type-array 0 byteDiff))))
           (typed-field->accessors '[Long byteDiff] "com.wjoel.bean.Test" [0 0])))
    (is (= '((clojure.core/defn edit-getByteDiff [this]
               (clojure.core/let [state (.state this)
                                  type-array (clojure.core/aget state 1)]
                 (clojure.core/aget type-array 2)))
             (clojure.core/defn edit-setByteDiff [this byteDiff]
               (clojure.core/let [state (.state this)
                                  type-array (clojure.core/aget state 1)]
                 (clojure.core/aset type-array 2 byteDiff))))
           (typed-field->accessors '[Long byteDiff] "com.wjoel.bean.edit" [1 2])))
    (is (= '((clojure.core/defn edit-getChannel [this]
               (clojure.core/let [state (.state this)]
                 (clojure.core/aget state 3)))
             (clojure.core/defn edit-setChannel [this channel]
               (clojure.core/let [state (.state this)]
                 (clojure.core/aset state 3 channel))))
           (typed-field->accessors '[String channel] "com.wjoel.bean.edit" [nil 3])))))

(deftest coordinate-map-test
  (testing "creation of coordinate map"
    (is (= '{byteDiff [1 0]
             timestamp [1 1]
             isMinor [0 0]
             isNew [0 1]
             isUnpatrolled [0 2]}
           (coordinate-map '{long [byteDiff timestamp]
                             boolean [isMinor isNew isUnpatrolled]})))
    (is (= '{byteDiff [0 0]
             timestamp [0 1]
             checksum [1 0]}
           (coordinate-map '{long [byteDiff timestamp]
                             short [checksum]})))
    (is (= '{} (coordinate-map '{})))))
