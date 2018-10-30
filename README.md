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
   [boolean minor]
   [boolean new]
   [boolean unpatrolled]
   [boolean botEdit]
   [boolean special]
   [boolean talk]])
```

[Get it from Clojars](https://clojars.org/com.wjoel/clj-bean)
or [The Central Repository](https://search.maven.org/#artifactdetails%7Ccom.wjoel%7Cclj-bean%7C0.2.1%7Cjar)
at
```clojure
[com.wjoel/clj-bean "0.2.1"]
```

Note that you need to use an [AOT-compiled namespace](https://clojure.org/reference/compilation)
with `defbean` to generate the necessary Java class files.

## License

Copyright © 2017 Joel Wilsson

Distributed under the MIT license.
