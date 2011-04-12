parser grammar TologParser;

options{
	// We're going to output an AST.
  	output = AST;

  	// We're going to use the tokens defined in our MathLexer grammar.
  	tokenVocab = TologLexer;

}

tokens{

	IMPORT;
	USING;
	RULE;
	SELECT;
	CLAUSE;	
/*
	COMMA;
	ROUND_BRACKETS_OPEN;
	ROUND_BRACKETS_CLOSE;
	ANGLE_BRACKETS_OPEN;
	ANGLE_BRACKETS_CLOSE;
	DOUBLEDOT;
	OR;
	OPERATOR;
	QUESTIONMARK;
	UNDERSCORE;
	DOT;
	HYPEN;
	WHITESPACE;	
	PERCENT;	
	RULE;	
	DOLLAR;
	//keywords
	NOT;
	LIMIT;
	ORDERBY;	
	USING;	
	IMPORT;
	FOR;
	AS;
	SELECT;
	FROM;
	COUNT;
	ASC;
	DESC;
	OFFSET;
	//complex
	OBJID;
	QNAME;
	IDENT;
	INDICATOR;
	ADDRESS;
	SRCLOC;
	URL;
	STRING;
	INTEGER;	
	VARIABLE;
	*/
}


@header { package de.topicmapslab.tmql4j.converter.tolog.parser; }

query:		head? clauselist WHITESPACE? tail? QUESTIONMARK;
clauselist:   	clause (WHITESPACE? COMMA WHITESPACE? clause)*;
clause:   	predclause | opclause | orclause | notclause;
predclause:   	ref ROUND_BRACKETS_OPEN WHITESPACE? pair (WHITESPACE? COMMA WHITESPACE? pair)* WHITESPACE? ROUND_BRACKETS_CLOSE;
pair:   	expr (WHITESPACE? DOUBLEDOT WHITESPACE? ref)?;
opclause:   	expr WHITESPACE OPERATOR WHITESPACE expr;
orclause:   	ANGLE_BRACKETS_OPEN WHITESPACE? clauselist WHITESPACE?(OR WHITESPACE? clauselist WHITESPACE? )* ANGLE_BRACKETS_CLOSE;
notclause:   	NOT WHITESPACE? ROUND_BRACKETS_OPEN WHITESPACE? clauselist WHITESPACE? ROUND_BRACKETS_CLOSE;
expr:   	VARIABLE | ref | value | parameter;
parameter:   	PERCENT IDENT PERCENT;
ref:   		OBJID | QNAME | uriref | IDENT;
uriref:   	INDICATOR | ADDRESS | SRCLOC;
value:   	STRING;
ruleset:   	(usingpart WHITESPACE | importpart WHITESPACE | rule WHITESPACE )+;
rule:   	ref ROUND_BRACKETS_OPEN WHITESPACE? varlist WHITESPACE? ROUND_BRACKETS_CLOSE WHITESPACE? RULE WHITESPACE? clauselist WHITESPACE? DOT  WHITESPACE?;
varlist:   	VARIABLE (WHITESPACE? COMMA WHITESPACE? VARIABLE)*;
head:   	((usingpart WHITESPACE) | (importpart WHITESPACE) | ( rule WHITESPACE )  | (selectpart WHITESPACE))+;
usingpart:   	USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref;
importpart:   	IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT;
selectpart:   	SELECT WHITESPACE selectlist WHITESPACE FROM;
selectlist:   	selpart WHITESPACE? (COMMA WHITESPACE? selpart)*;
selpart:   	VARIABLE | aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE;
aggfun:   	COUNT;
tail:   	(orderpart WHITESPACE? | limitpart WHITESPACE? | offsetpart WHITESPACE?)+;
orderpart:   	ORDERBY WHITESPACE orderlist;
orderlist:   	ordpart (WHITESPACE? COMMA WHITESPACE? ordpart)*;
ordpart:   	VARIABLE (WHITESPACE (ASC|DESC))?;
limitpart:   	LIMIT WHITESPACE INTEGER;
offsetpart:   	OFFSET WHITESPACE INTEGER;
