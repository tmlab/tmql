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
  * add new optional argument to all get-