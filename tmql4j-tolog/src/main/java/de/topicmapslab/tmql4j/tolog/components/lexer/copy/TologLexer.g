lexer grammar TologLexer;

@header { package de.topicmapslab.tmql4j.converter.tolog.lexer; }

//symbols
COMMA:',';
ROUND_BRACKETS_OPEN:'(';
ROUND_BRACKETS_CLOSE:')';
ANGLE_BRACKETS_OPEN:'{';
ANGLE_BRACKETS_CLOSE:'}';
DOUBLEDOT:':';
OR:'|';
OPERATOR:'/=';
QUESTIONMARK:'?';
UNDERSCORE:'_';
DOT:'.';
HYPEN:'-';
WHITESPACE:(' '|'\t')+;	
PERCENT:'%';	
RULE:':-';	
DOLLAR:'$';
//fragments	
fragment LOWER:'a'..'z';
fragment UPPER:'A'..'Z';
fragment DIGIT:'0'..'9';
fragment SYMBOLS:':' | '/' | '.' | '#' | '_';
fragment ALPHANUMERIC: LOWER | UPPER | DIGIT ;	
//keywords
NOT:'not';
LIMIT:'limit';
ORDERBY:'order by';	
USING:'using';	
IMPORT:'import';
FOR:'for';
AS:'as';
SELECT:'select';
FROM:'from';
COUNT:'count';
ASC:'asc';
DESC:'desc';
OFFSET:'offset';
//complex
OBJID:'@' ALPHANUMERIC+;
QNAME: IDENT (DOUBLEDOT (ALPHANUMERIC|DOT|HYPEN|UNDERSCORE)+)+;
IDENT:(LOWER|UPPER|UNDERSCORE) (ALPHANUMERIC|DOT|HYPEN|UNDERSCORE)*;
INDICATOR:'i' URL;
ADDRESS:'a' URL;
SRCLOC:'s' URL;
URL:'"' (ALPHANUMERIC | SYMBOLS )+ '"';
STRING:'"' (ALPHANUMERIC| '"' '"')* '"';
INTEGER:DIGIT+;
VARIABLE: DOLLAR IDENT;
