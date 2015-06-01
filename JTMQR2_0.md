# JTM Query Result (JTMQR) 2.0 (Draft) #

The main targets for version 2.0 (compared to JTMQR 1.0) are:

  * Reduction of duplicated, explicit type definition for simple value types to achieve a more compact and lightweight specification
  * Final support for arrays within tuple columns

Compared to version 1.0, JTMQR 2.0 has the following changes:

  * removed **t** hash and converted the previous @t@ hash value list into a simple JSON array
  * removed **s**, **n**, **b** hashes, as the value for each hash does not need an explicit type declaration
  * renamed **seq** to **tuples**
  * renamed **l** to **ref** to achieve a more meaningful name for a locator hash
  * renamed **i** to **jtm** to achieve a more meaningful name for a JTM serialized Topic Map construct
  * renamed **aliases** to @headers@ in the @metadata@ hash
  * changed **version** field in the root hash to **2.0**

Compared to the fundamental RelaxNG description for the _Tuple Sequence Encoding_, JTMQR 2.0 has the following changes:

  * the XTM 1.0 fragments will be replaced by [JTM 1.1](http://www.cerny-online.com/jtm/1.1/) ([JTM 1.0](http://www.cerny-online.com/jtm/1.0/) should be accepted as valid too).
  * removed **xmlns** namespace definition
  * removed **t** as the JSON supports [.md](.md) arrays out of the box
  * removed **s** as the JSON format does not need explicit type information for simple value types.
  * renamed **seq** to **tuples**
  * renamed **id** to **ref** as it seems to give more information about the real meaning of its value - a locator.
  * renamed **i** to **jtm** to achieve a more meaningful name for a JTM serialized Topic Map construct
  * added support for more simple value types like boolean and numbers (see **fn:count()** or **fn:count() > x**)
  * added support for labeling the columns of the result set using **headers**.

# Specification #

## **{}** is the root of the JTM Query Result Document ##

The query result document contains the following members:

  * **version** - the version of JTMQR (MUST be **"2.0"**)
  * **metadata** - some information on the sequence data
  * **tuples** - an array of _Tuples_ (each tuple is an array containing the values)
  * **ordered** - (optional, default: false) true if tuples in **tuples** are ordered

## **metadata** - Information about the tuple sequence ##

Gives information about rows and columns used for the result sequence:

  * **columns** - the number of result columns (length of a tuple)
  * **rows** - the number of result sets
  * **headers** - simple array containing strings representing the column headers

## **tuples** - The array containing the results ##

Each tuple inside the @tuples@ array contains 0..n of the following members depending on the TMQL query:

|`"foo"`|a JSON string (quoted literal)|
|:------|:-----------------------------|
|`true` or `false`|a JSON boolean                |
|`0.0`  |a JSON float                  |
|`0`    |a JSON integer                |
|`{"ref":"http://foo"}`|a locator                     |
|`[ ]`  |an JSON array (list of mixed values)|
|`{"jtm":{ /*JTM*/ }}`|a Topic Map construct item that is represented as [JTM 1.1](http://www.cerny-online.com/jtm/1.1/) or [JTM 1.0](http://www.cerny-online.com/jtm/1.0/)|

## Array Value ##
Each array may contain a set of JSON object similar to the tuple members.

**Note:** Array values may never containd array values

## JSON Schema v3 ##

```
{
  "$schema" : "http://json-schema.org/draft-03/schema#",
  "id" : "http://code.google.com/p/tmql/wiki/JTMQR2_0#",

  "type":"object", 
  "title":"A JTMQR 2.0 document", 
  "properties":{
    "version"   :{"type":"string",  "required":true, "pattern":"2.0"},
    "metadata"  :{"type":"object",  "required":true, "properties":{
      "columns"   :{"type":"integer", "required":true, "minimum":0},
      "rows"      :{"type":"integer", "required":true, "minimum":0},
      "headers"   :{"type":"array",   "required":true, "items":{"type":["string","null"]}}
    }},
    "tuples"    :{"type":"array",   "required":true, 
      "items":{"type":"array",
        "items":{[
          "type":[
            "string",
            "number",
            "boolean",
            "null",
            {"type":"object", "properties":{
              "jtm":{"required":true, "type":[
                // As for JTM 1.x no schema definitions are available 
                // the type of "jtm" property was set to "object" to enable
                // validation passing. Otherwise the following references
                // to JTM schemas MAY work.
                // {"$ref" : "http://www.cerny-online.com/jtm/1.0/#"},
                // {"$ref" : "http://www.cerny-online.com/jtm/1.1/#"}
                "object"
              ]}
            }}
          ]
        ]}
      }
    },
    "ordered"   :{"type":"boolean", "optional":true}
  }
}
```

## Samples ##

```
// possible result for TMQL query: tm:subject >> instances 

{ "version":"2.0",
  "metadata":{"columns":1, "rows":20, "headers":[null]},
  "tuples":[ 
      [ {"jtm":{ // each topic is serialized with JTM
          "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}} ],
      [ {"jtm":{/*JTM*/}} ],
      [ {"jtm":{/*JTM*/}} ],
      ..
  ],
  "ordered":true
}
```

### Sample with projection ###

```
// possible result for TMQL query: 
//   tm:subject >> instances ( 
//      . AS "Instance", 
//      . / tm:name AS "Names",
//      fn:count( . >> characteristics )     AS "#Chr",
//      fn:count( . >> characteristics ) > 2 AS ">2?")

{ "version":"2.0", 
  "metadata": {
    "columns":4, 
    "rows":20, 
    "headers":["Instance", "Names", "#Chr", ">2?"]},
  "tuples":[ 
      [ {"jtm":{ // each topic is serialized with JTM
          "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}},
        "Foo",
        2,
        false ],
      [ {"jtm":{/*JTM*/}},
        "Bar",
        5,
        true ],
      [{"jtm":{/*JTM*/}},
        "Zoo",
        3,
        true ]
  ],
  "ordered":true
}
```

### Sample with array results and headers ###

```
// possible result for TMQL query: 
//   FOR $t IN tm:subject >> instances  
//   GROUP BY $0
//   RETURN $t AS "topic", $t / tm:name

{ "version":"2.0", 
  "metadata":{"columns":2, "rows":20, "headers": ["topic", null]},
  "tuples":[ 
      [ {"jtm":{
          "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}},
        ["Name", "Other"] ],
      [ {"jtm":{/*JTM*/}},
        ["Name", "Other"] ],
  ],
  "ordered":true
}
```