# Version 2.6.4 #

  * **new maven group id**
    * the core engine and all plugins have the group id _de.topicmapslab.tmql4j_
    * all plugins are having the same version number like the core engine

  * new axis **id**
    * If the anchor is a construct in forward direction the axis computes the id of the given construct
    * If the anchor is a string literal in backward direction the axis computes the construct with the given id. The optional item has no relevance.
  * new axis **typed**
    * If the anchor is a typed item (an association item, a topic name item, an occurrence item or a role item) the axis computes the type of the construct. The optional item has no relevance.
    * If the anchor is a topic the axis computes all typed constructs of the given type. The optional type has no relevance.
  * new production **association-def**
    * similar to predicate invocation to extract all matching associations or create a new one.
    * assoc-type '(' ( role-type : role-player ','? )+ ( ',' '...' )? ')'
  * new production **topic-def**
    * to create a topic with an update clause
    * string-literal ( '!' | '=' | '~' )
  * new Method @IResult **get(int)** returning the value at the given position in the expected type
  * new Method @IResultSet **get(int)** returning the result at the given position
  * new Method @IResultSet **get(int, int)** returning the value at the given position(Row,Col)