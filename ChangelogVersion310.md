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
    * XMLResult to de.topicmapslab.tmql4j.components.processor.results.xml
  * new function **fn:max** expecting two arguments
    * context of count and counts
    * e.g.: fn:max ( // tm:subject , fn:count ( . / tm:name )) to get the maximum number of names a topic instance contains
  * new function **fn:min** expecting two arguments
    * context of count and counts
    * e.g.: fn:min ( // tm:subject , fn:count ( . / tm:name )) to get the minimum number of names a topic instance contains
  * value-expression supports boolean-expression to return true or false
  * the update handler checks the value of occ and variants according to the datatype (validate the value for the given datatype)
    * a pragma was added to disable this functionality datatype-validation
  * the update of variants and occurrences will reuse the datatype instead of setting string automatically, like TMAPI do
    * a pragma was added to disable this functionality datatype-binding
  * allow prepared argument add new positions:
    * as optional axis argument
    * as part of update value
  * change handling of players axis:
    * if the current node is a topic the axis returns a set of all players of all roles being of the given type
  * add datatype axis