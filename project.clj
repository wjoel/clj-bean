(defproject com.wjoel/clj-bean "0.2.0"
  :description "A better way to create JavaBeans from Clojure"
  :url "https://github.com/wjoel/clj-bean"
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :aot [clj-bean.bean-test]

  :deploy-repositories {"releases" {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                                    :creds :gpg}
                        "snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                     :creds :gpg}}
  :scm {:url "git@github.com:wjoel/clj-bean.git"}
  :classifiers {:javadoc {:source-paths ^:replace []
                          :aot ^:replace []}
                :sources {:aot ^:replace []}}
  :pom-addition [:developers [:developer
                              [:name "Joel Wilsson"]
                              [:url "https://wjoel.com"]
                              [:email "joel.wilsson@gmail.com"]]])
