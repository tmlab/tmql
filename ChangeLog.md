# Version 3.1.0 #

  * new behavior of roles axis
    * forward navigation return the roles of an association
    * backward navigation returns the association acts as parent of a role
  * new behavior of players axis
    * forward navigation return the players of a role or all roles of the association
    * backward navigation returns the roles played by the topic
  * new roletypes axis
    * forward navigation results in the role types of an association
    * backward navigation results in the associations having a role with this type
  * add new update operator 'REMOVE' to update clause
    * avaible for: names, occurrences, characteristics, locators, indicators, item, scope, topics, types, supertypes, subtypes, instances
  * characteristics added as alias for names and occurrences modification
  * add variants anchor as update context
  * new method @IResultSet
    * **toTopicMap**: If the result set supports this operation, it will return a topic map copy containing only the topics and association contained in
    * **toCTM**: If the result set supports this operation, it will return a CTM string or stream containing only the topics and association contained in
    * **toXTM**: If the result set supports this operation, it will return an XTM string or stream containing only the topics and association contained in
    * **toJTMQR**: If the result set supports this operation, it will return a JTMQR string or stream containing only the topics and association contained in
  * CTMResult
    * rename method **resultsAsMergedCTM** to **toCTM**
    * rename method **resultsAsTopicMap** to **toTopicMap**
  * XMLResult
    * rename method **resultsAsMergedXML** to **toXML**
    * add stream variant of method **toXML**
  * the arguments on update roles are modified argument before operator is the role type, the value after is the player
  * update-clause allow any value-expression in front of the operator
  * move classes
    * IResult to de.topicmapslab.tmql4j.components.processor.results.model
    * IResultSet to de.topicmapslab.tmql4j.components.processor.results.model
    * IResultProcessor to de.topicmapslab.tmql4j.components.processor.results.model
    * ProjectionUtils to de.topicmapslab.tmql4j.components.processor.results.model
    * Result to de.topicmapslab.tmql4j.components.processor.results.model
    * ResultSet to de.topicmapslab.tmql4j.components.processor.results.model
    * ResultType to de.topicmapslab.tmql4j.components.processor.results.model
    * SimpleResult to de.topicmapslab.tmql4j.components.processor.results.tmdm
    * SimpleResultSet to de.topicmapslab.tmql4j.components.processor.results.tmdm
    * TmqlResultProcessor to de.topicmapslab.tmql4j.components.processor.results
    * CTMResult to de.topicmapslab.tmql4j.components.processor.results.ctm
    * XMLResult to de.topicmapslab.tmql4j.components.processor.results.xtm

# Version 3.0.0 #

  * refactor processing model
    * remove stack based interpreter calling
  * refactor interfaces of runtime usage
  * remove environment map
  * add new 2d-reduction mechanism
  * split language parts into different projects
  * split old and new draft runtime
  * add Group-By expression for FLWR and SELECT
    * ... GROUP BY $0 ... $n ....
  * add naming result columns by using an alias expression
    * ... AS "column" ...
    * new method @IResult **get(String)**
    * new method @IResult **isNullValue(String)**
    * new method @IResultSet **get(int, String)**
    * new method @IResultSet **isNullValue(int, String)**
    * alias are allowed after a value-expression
      * as part of select clause
      * as part of return clause
      * as part of projection
      * as simple navigation
  * adding prepared and protected statements
    * using wildcard ? as placeholder for later binding
    * anonymous wildcard ? and named wildcards ?(a-zA-Z0-9)+
    * statement will be parsed onetime ( speed up to 1/5 )
    * run() methods are extended to pass arguments to set for wildcards
    * special setter for wildcard values
      * by index **set(int,Object)**
      * by name for named and anonymous wildcards to set the same value for more than one wildcard **set(String,Object)** or **set(Object)**
  * add new functions
    * array: returns an array of parameter values
    * topics-by-(subjectidentifier|subjectlocator|itemidentifier): returns an array of all topics get by the given identifiers
  * new pragma handling
    * the language context supports a new pragma registry **getPragmaRegistry**
    * new interface **IPragma** to define a new pragma by any extension
    * taxonometry pragma registered as default
  * new module tmql4j-template
    * new template pragma
    * allows definition of templates by name or anoynmous using the new pragma
    * new keywords **USE**, **DEFINE**, **REDEFINE**, **TEMPLATE**
    * use-clause at the end of path, flwr and select to transform templates, JTMQR or CTM
  * add new function to tmql4j-majortom fn:best-identifier(boolean), see MaJorToM docs for more information
  * prepared statements allow to define datatype as part of the argument like `value^^datatype` or as separate token in query like ` ... ? ^^datatype`
  * add variants axis with optional argument
    * if the current node is a name the forward navigation results in all variants of this name. The optional argumet has no relevance.
    * If the current node is a variant the backward navigation resukts in the parent name item. The optional argument will be interpreted as name type.
  * add new optional argument to all get-**-types function to implicate the transitive flag**

# Version 2.6.5 #

  * rename set operation keywords
    * '==' to INTERSECT
    * '++' to UNION
    * '--' to MINUS
  * add comparison operators
    * '==' as equals
    * '!=' as unequals
  * add new methods to extension point registry to enable or disable an extension point for current execution
    * **enableExtensionPoint(String)**
    * **disableExtensionPoint(String)**
  * add new method @IQuery **beforeQuery** called by runtime before execution of this query
  * add new method @IQuery **afterQuery** called by runtime after execution of this query
  * new escaping syntax for string literals \" escapes the existence of quote as part of a string literal


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
