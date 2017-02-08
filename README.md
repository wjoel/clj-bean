# clj-bean

The better way to create JavaBeans from Clojure.

A tiny library to create standards compliant JavaBeans with good performance.
Supports primitive types and has the API you always wanted.

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

[Get it from Clojars](https://clojars.org/com.wjoel/clj-bean) with
```clojure
[com.wjoel/clj-bean "0.1.0"]
```

Note that you need to use an [AOT-compiled namespace](https://clojure.org/reference/compilation)
with `defbean` to generate the necessary Java class files.

## License

Copyright © 2017 Joel Wilsson

Distributed under the MIT license.
