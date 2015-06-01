# TMQL4J #

TMQL4J is the first TMQL engine written in Java. The current version supports the draft of 2010 and 2008. As extensions the query suite tmql4j supports the new topic maps modification language and the topic maps template language. In addition it realizes additional features to support the process of development and designing of applications using the data model of Topic Maps.

## Documentation ##

The documentation is available on http://tmql4j.topicmapslab.de/

## Architecture ##

The TMQL4J engine offers a new abstraction layer on top of the TMAPI. Instead of accessing the Topic Maps engine directly applications may use a simple TMQL query. The architecture of Topic Maps applications is layered. The base of all applications are the Topic Maps backends, which are administrated by the Topic Maps engines. To abstract from the real implementation, TMAPI is used as a standardized interface. The last layer under the application is the TMQL4J engine. The access to the topic maps engine is encapsulated and can be local or remote.

## Modularization ##

TMQL4J is designed in a couple of modules which are proceeded in a virtual chain. Each module provides a simple or atomic function in context of the querying process, like the lexical scanner. The new interface and service framework of the query suite enables the implementation of your own query translator, runtime, processor and data accessor.

## Optimization ##

TMQL4J realizes an additional abstraction layer over the TMAPI and implements additional features to speed up the querying process by using new index implementations and extension of the TMAPI. Some modules directly access the data structure without calling the TMAPI. Using the new processing model of the version 3.0.0, the engine speeds up a set of interpretation tasks, by using multiple threads.