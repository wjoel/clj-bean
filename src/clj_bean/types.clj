(ns clj-bean.types)

(def primitives
  {'boolean {:default false :array-type 'booleans}
   'byte {:default (byte 0) :array-type 'bytes}
   'char {:default (char \u0000) :array-type 'chars}
   'short {:default (short 0) :array-type 'shorts}
   'int {:default (int 0) :array-type 'ints}
   'long {:default (long 0) :array-type 'longs}
   'float {:default (float 0) :array-type 'floats}
   'double {:default (double 0) :array-type 'doubles}})

(defn default-value [type]
  (get-in primitives [type :default] nil))

(defn array [type]
  (get-in primitives [type :array-type] 'objects))

(defn primitive? [type]
  (contains? primitives type))

(defn canonical
  "Returns type, if it's primitive, or 'object."
  [type]
  (if (primitive? type)
    type
    'object))
