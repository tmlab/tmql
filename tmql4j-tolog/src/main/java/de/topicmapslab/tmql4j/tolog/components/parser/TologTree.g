tree grammar TologTree;

options{
	// We're going to process an AST whose nodes are of type CommonTree.
	ASTLabelType = CommonTree;
	
  	// We're going to use the tokens defined in
	// both our MathLexer and MathParser grammars.
	// The MathParser grammar already includes
	// the tokens defined in the MathLexer grammar.
	tokenVocab = TologLexer;
	}

@header { package de.topicmapslab.tmql4j.converter.tolog.parser; }

query:		head? clauselist tail? QUESTIONMARK;
clauselist:   	clause (WHITESPACE? COMMA WHITESPACE? clause)*;
clause:   	predclause; 
// | opclause | orclause | notclause;
predclause:   	IDENT ROUND_BRACKETS_OPEN pair (WHITESPACE? COMMA WHITESPACE? pair)* WHITESPACE? ROUND_BRACKETS_CLOSE;
pair:   	expr (WHITESPACE? DOUBLEDOT WHITESPACE? ref)?;
opclause:   	expr OPERATOR expr;
orclause:   	ANGLE_BRACKETS_OPEN WHITESPACE? clauselist WHITESPACE?(OR WHITESPACE? clauselist)* ANGLE_BRACKETS_CLOSE;
notclause:   	NOT WHITESPACE? ROUND_BRACKETS_OPEN WHITESPACE? clauselist WHITESPACE? ROUND_BRACKETS_CLOSE;
expr:   	VARIABLE | ref | value | parameter;
parameter:   	PERCENT IDENT PERCENT;
ref:   		OBJID | QNAME | uriref;
uriref:   	INDICATOR | ADDRESS | SRCLOC;
value:   	STRING;
ruleset:   	(usingpart | importpart)* rule+;
rule:   	IDENT ROUND_BRACKETS_OPEN WHITESPACE? varlist WHITESPACE? ROUND_BRACKETS_CLOSE WHITESPACE? RULE WHITESPACE? clauselist WHITESPACE? DOT;
varlist:   	VARIABLE (WHITESPACE? COMMA WHITESPACE? VARIABLE)*;
head:   	((usingpart WHITESPACE) | (importpart WHITESPACE))+ (selectpart WHITESPACE)?;
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

