# JTMQR 1.0 vs. JTMQR 2.0 #

**Query:**

```
# Comparing results for TMQL query: 
  tm:subject >> instances 
    ( 
       . AS "Instance", 
       . / tm:name AS "Names",
       fn:count( . >> characteristics )     AS "#Chr",
       fn:count( . >> characteristics ) > 2 AS ">2?"  
    )
```

**JTMQR 1.0**

```
{ "version":"1.0",    
  "metadata": {       
    "columns":4,      
    "rows":20,        
    "aliases":{       
      "0":"Instance", 
      "1":"Names",    
      "2":"#Chr",     
      "3":">2?"}},    
  "seq":[             
    {"t":[            
      {"i":{-JTM-}}   
      {"s":"Foo"},    
      {"n":2},        
      {"b":false}     
    ]},               
    {"t":[..]}        
  ],                  
  "ordered":true      
}                     
```

**JTMQR 2.0**

```
{ "version":"2.0", 
  "metadata": {
    "columns":4, 
    "rows":20, 
    "headers":[
      "Instance",
      "Names", 
      "#Chr", 
      ">2?"]},
  "tuples":[ 
    [
      {"jtm":{-JTM-}}
      "Foo",
      2,
      false
    ],
    [..]
  ],
  "ordered":true
}
```