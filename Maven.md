If you use [Maven](http://maven.apache.org/) or  [Apache Ivy](http://ant.apache.org/ivy/) in your [Ant](http://ant.apache.org/)-based project, you can include the following dependencies:


```
  <repositories>
      <repository>
          <id>tmlab</id>
          <url>http://maven.topicmapslab.de/public/</url>
      </repository>
  </repositories>

  <dependencies>
      <dependency>
          <groupId>de.topicmapslab.tmql4j</groupId>
          <artifactId>tmql4j-path</artifactId>
          <version>3.1.0</version>
      </dependency>
  </dependencies>
```