# clj-bean

The better way to create JavaBeans from Clojure.

A tiny library to create standards compliant JavaBeans with good performance
and a small footprint. Supports primitive types and has the API you always
wanted.

## Usage

```clojure
(defbean your.java.package.MyBean
  [[long timestamp]
   [String channel]
   [String title]
   [String diffUrl]
   [String user]
   [long byteDiff]
   [String summary]
   [boolean isMinor]
   [boolean isNew]
   [boolean isUnpatrolled]
   [boolean isBotEdit]
   [boolean isSpecial]
   [boolean isTalk]])
```

## License

Copyright © 2017 Joel Wilsson

Distributed under the MIT license.