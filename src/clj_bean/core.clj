(ns clj-bean.core
  (:require [clj-bean.types :as t]))

(defn ->prefix [class-name-symbol]
  (-> (name class-name-symbol)
      (clojure.string/split #"\.")
      last
      (str "-")))

(defn sym->upcase-1-str
  "Converts the first character of a symbol's name to uppercase, returns a string"
  [sym]
  (let [s (name sym)]
    (str (.toUpperCase (subs s 0 1)) (subs s 1))))

(defn tagged-sym
  "Returns a symbol with the given sym-name and :tag in metadata set to tag-value"
  [sym-name tag-value]
  (vary-meta (symbol sym-name) assoc :tag tag-value))

(defn typed-field->declarations
  [[type name]]
  (let [sym-name (sym->upcase-1-str name)]
    `([~(symbol (str "get" sym-name)) [] ~type]
      [~(symbol (str "set" sym-name)) [~type] ~(symbol "void")])))

(defn typed-field->accessors
  [[type name] class-name [type-array-index field-index]]
  (let [prefix (->prefix class-name)
        object? (nil? type-array-index)
        sym-name (sym->upcase-1-str name)
        value-sym (tagged-sym name type)
        this-sym (tagged-sym "this" class-name)
        state-sym (tagged-sym "state" 'objects)
        array-sym (tagged-sym "type-array" (t/array type))]
    `((defn ~(symbol (str prefix "get" sym-name)) [~this-sym]
        (let [~state-sym (.state ~this-sym)
              ~@(when-not object?
                  [array-sym (list `aget state-sym type-array-index)])]
          (aget ~(if object? state-sym array-sym) ~field-index)))
      (defn ~(symbol (str prefix "set" sym-name)) [~this-sym ~(symbol name)]
        (let [~state-sym (.state ~this-sym)
              ~@(when-not object?
                  [array-sym (list `aget state-sym type-array-index)])]
          (aset ~(if object? state-sym array-sym) ~field-index ~value-sym))))))

(defn coordinate-map
  "The bean's state is an (object) array of arrays. There is one array for
  each primitive type, and one element for object fields. Each primitive field
  can be looked up by the index of the array for its type, and then indexing
  that array with the field's index within it.
  Object fields can be found through their index in the object array.
  In short, each field has a two-dimensional coordinate in the bean's state.
  The first coordinate of object fields will be nil.
  This function returns a map from the field's symbol to its coordinates."
  [type->fields]
  (let [type->state-index (->> (map vector (sort (keys type->fields)) (range))
                               (into {}))
        ->type-index (fn [[type name]]
                       (.indexOf ^java.util.List (type->fields (t/canonical type)) name))]
    (reduce (fn [m [type fields]]
              (reduce (fn [m field]
                        (assoc m field [(type->state-index type)
                                        (->type-index [type field])]))
                      m fields))
            {} type->fields)))

(defn typed-fields->initializer [[type fields]]
  (let [array-type (symbol (str "clojure.core/" (name type) "-array"))]
    `(~array-type [~@fields])))

(defn initial-state-value [type->fields object-fields]
  (concat (->> (map typed-fields->initializer type->fields)
               (sort-by first))
          object-fields))

(defmacro defbean
  "Generates a Java bean. The fields are a set of field type and name."
  [class-name typed-fields]
  {:pre [(vector? typed-fields)
         (every? vector? typed-fields)
         (every? (comp even? count) typed-fields)]}
  (let [prefix (->prefix class-name)
        types (map first typed-fields)
        names (map second typed-fields)
        object-fields (->> (filter #(not (t/primitive? (first %))) typed-fields)
                           (map second)
                           sort)
        type->fields (->> (filter #(t/primitive? (first %)) typed-fields)
                          (reduce (fn [m [type name]]
                                    (assoc m type (conj (m type) name)))
                                  {})
                          (reduce-kv (fn [m k v] (assoc m k (sort v))) {}))
        initial-state-initializer (initial-state-value type->fields object-fields)
        name->coordinate (->> (map vector object-fields (range))
                              (reduce (fn [m [field index]]
                                        (assoc m field [nil (+ (count type->fields) index)]))
                                      (coordinate-map type->fields)))
        method-decls (mapcat typed-field->declarations typed-fields)
        method-impls (mapcat (fn [[type name :as typed-field]]
                               (typed-field->accessors typed-field class-name
                                                       (name->coordinate name)))
                             typed-fields)
        initial-state (gensym "initial-state")
        init-sym (symbol (str prefix "init"))
        null-constructor-values (map t/default-value types)]
    `(do
       (gen-class
        :name ~class-name
        :implements [java.io.Serializable]
        :init "init"
        :state "state"
        :prefix ~prefix
        :constructors {[] []
                       [~@types] []}
        :methods [~@method-decls])

       (defn ~init-sym
         ([] [[] (~init-sym ~@null-constructor-values)])
         ([~@names]
          [[] (object-array [~@initial-state-initializer])]))

       ~@method-impls)))
