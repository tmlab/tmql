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