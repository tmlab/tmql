# Specification #

## Query Result Set (root) ##

The query result set contains the following members:

  * **version** - the version of JTMQR (should be "1.0")
  * **metadata** - some information on the sequence data
  * **seq** - an array of _Tuple Hashes_
  * **ordered** - (optional, default: false) true if tuples in **seq** are ordered

## Metadata ##

Gives information about rows and columns used for the result sequence:

  * **columns** - the number of result columns (length of a tuple)
  * **rows** - the number of result sets
  * **aliases** - json object containing column headers ( keys are the index of result )

## Tuple Hash ##

Each tuple hash may contain 0..n of the following members depending on the TMQL query:

  * **s** - a literal (string)
  * **b** - a literal (boolean)
  * **n** - a literal (number)
  * **l** - a literal (locator)
  * **a** - a JSON array (list of values)
  * **i** - a construct item that is represented as [JTM 1.0](http://www.cerny-online.com/jtm/1.0/) or [JTM 1.1](http://www.cerny-online.com/jtm/1.1/)

## Array Value ##

Each array may contain a set of JSON object similar to the tuple hash syntax and members. The following members are allowed:

  * **s** - a literal (string)
  * **b** - a literal (boolean)
  * **n** - a literal (number)
  * **l** - a literal (locator)
  * **i** - a construct item that is represented as [JTM 1.0](http://www.cerny-online.com/jtm/1.0/) or [JTM 1.1](http://www.cerny-online.com/jtm/1.1/)

**Note:** Array values may never containd array values

## Pseudo Schema ##

```
{
  "version": "",
  "metadata": {"columns":0, "rows":0, "aliases":{}},
  "seq":[
    {"t":[ 
      { "s":".." | "l":".." |"b":(true|false) | "n":(integer|floating point) | "i":{ -JTM- } | "a":[...]}, 

      ..
    ]},
    ..
  ]
  "ordered":true
}
```

# Samples #

```
// possible result for TMQL query: tm:subject >> instances 

{
  "version":"1.0",
  "metadata": {"columns":1, "rows":20, "aliases":{}},
  "seq":[
    {"t":[ 
      {"i": // each topic is serialized with JTM
        { "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}}
    ]},
    {"t":[ 
      {"i":
        { "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}}
    ]},
    {"t":[ 
      { ... }
    ]}
  ],
  "ordered":true
}
```

### Sample with projection ###

```
// possible result for TMQL query: 
//   tm:subject >> instances ( 
//      . , 
//      . / tm:name,
//      fn:count( . >> characteristics ),
//      fn:count( . >> characteristics ) > 2)

{
  "version":"1.0", 
  "metadata": {"columns":4, "rows":20, "aliases":{}},
  "seq":[
    {"t":[ 
      {"i": // each topic is serialized with JTM
        { "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}},
      {"s":"Foo"},
      {"n":2},
      {"b":false}
    ]},
    {"t":[ 
      {"i":
        { "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}},
      {"s":"Bar"},
      {"n":3},
      {"b":true}
    ]},
    {"t":[ 
      {"i": ... },
      {"s":"Zoo"},
      {"n":5},
      {"b":true}
    ]}
  ],
  "ordered":true
}
```

### Sample with array results and alias ###

```
// possible result for TMQL query: 
//   FOR $t IN tm:subject >> instances  
//   GROUP BY $0
//   RETURN $t AS "topic", $t
//      . / tm:name

{
  "version":"1.0", 
  "metadata": {"columns":2, "rows":20, "aliases":{ "0":"topic" , "1":null}},
  "seq":[
    {"t":[ 
      {"i": // each topic is serialized with JTM
        { "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}},
      {"a":[
              {"s":"Name"},
              {"s":"Other"}
           ]
      }
    ]},
    {"t":[ 
      {"i":
        { "version":"1.1", 
          "prefixes":{"tmdm":"http://psi.topicmaps.org/iso13250/model/"}, 
          "item_type":"topic", 
          "subject_identifiers":[ .. ], 
          "instance_of":[ .. ], 
          "names":[ .. ]}},
      {"a":[
              {"s":"Name"},
              {"s":"Other"}
           ]
      }
    ]}
  ],
  "ordered":true
}
```