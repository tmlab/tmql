// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g 2009-11-17 14:05:18
 package de.topicmapslab.tmql4j.tolog.components.lexer; 

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class TologLexer extends Lexer {
    public static final int DOLLAR=19;
    public static final int LIMIT=26;
    public static final int FOR=30;
    public static final int COUNT=34;
    public static final int DOUBLEDOT=9;
    public static final int ANGLE_BRACKETS_OPEN=7;
    public static final int ORDERBY=27;
    public static final int NOT=25;
    public static final int EOF=-1;
    public static final int AS=31;
    public static final int IMPORT=29;
    public static final int USING=28;
    public static final int COMMA=4;
    public static final int OFFSET=37;
    public static final int ALPHANUMERIC=24;
    public static final int QUESTIONMARK=12;
    public static final int ROUND_BRACKETS_OPEN=5;
    public static final int IDENT=39;
    public static final int DIGIT=22;
    public static final int DOT=14;
    public static final int SELECT=32;
    public static final int INTEGER=46;
    public static final int OBJID=38;
    public static final int RULE=18;
    public static final int PERCENT=17;
    public static final int HYPEN=15;
    public static final int ASC=35;
    public static final int OPERATOR=11;
    public static final int INDICATOR=42;
    public static final int WHITESPACE=16;
    public static final int UNDERSCORE=13;
    public static final int ROUND_BRACKETS_CLOSE=6;
    public static final int URL=41;
    public static final int ANGLE_BRACKETS_CLOSE=8;
    public static final int QNAME=40;
    public static final int VARIABLE=47;
    public static final int ADDRESS=43;
    public static final int OR=10;
    public static final int SRCLOC=44;
    public static final int LOWER=20;
    public static final int SYMBOLS=23;
    public static final int DESC=36;
    public static final int FROM=33;
    public static final int UPPER=21;
    public static final int STRING=45;

    // delegates
    // delegators

    public TologLexer() {;} 
    public TologLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public TologLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g"; }

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:6:6: ( ',' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:6:7: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "ROUND_BRACKETS_OPEN"
    public final void mROUND_BRACKETS_OPEN() throws RecognitionException {
        try {
            int _type = ROUND_BRACKETS_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:7:20: ( '(' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:7:21: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ROUND_BRACKETS_OPEN"

    // $ANTLR start "ROUND_BRACKETS_CLOSE"
    public final void mROUND_BRACKETS_CLOSE() throws RecognitionException {
        try {
            int _type = ROUND_BRACKETS_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:8:21: ( ')' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:8:22: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ROUND_BRACKETS_CLOSE"

    // $ANTLR start "ANGLE_BRACKETS_OPEN"
    public final void mANGLE_BRACKETS_OPEN() throws RecognitionException {
        try {
            int _type = ANGLE_BRACKETS_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:9:20: ( '{' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:9:21: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANGLE_BRACKETS_OPEN"

    // $ANTLR start "ANGLE_BRACKETS_CLOSE"
    public final void mANGLE_BRACKETS_CLOSE() throws RecognitionException {
        try {
            int _type = ANGLE_BRACKETS_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:10:21: ( '}' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:10:22: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANGLE_BRACKETS_CLOSE"

    // $ANTLR start "DOUBLEDOT"
    public final void mDOUBLEDOT() throws RecognitionException {
        try {
            int _type = DOUBLEDOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:11:10: ( ':' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:11:11: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLEDOT"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:12:3: ( '|' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:12:4: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "OPERATOR"
    public final void mOPERATOR() throws RecognitionException {
        try {
            int _type = OPERATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:13:9: ( '/=' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:13:10: '/='
            {
            match("/="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPERATOR"

    // $ANTLR start "QUESTIONMARK"
    public final void mQUESTIONMARK() throws RecognitionException {
        try {
            int _type = QUESTIONMARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:14:13: ( '?' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:14:14: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUESTIONMARK"

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:15:11: ( '_' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:15:12: '_'
            {
            match('_'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNDERSCORE"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:16:4: ( '.' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:16:5: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "HYPEN"
    public final void mHYPEN() throws RecognitionException {
        try {
            int _type = HYPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:17:6: ( '-' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:17:7: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HYPEN"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:18:11: ( ( ' ' | '\\t' )+ )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:18:12: ( ' ' | '\\t' )+
            {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:18:12: ( ' ' | '\\t' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "PERCENT"
    public final void mPERCENT() throws RecognitionException {
        try {
            int _type = PERCENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:19:8: ( '%' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:19:9: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PERCENT"

    // $ANTLR start "RULE"
    public final void mRULE() throws RecognitionException {
        try {
            int _type = RULE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:20:5: ( ':-' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:20:6: ':-'
            {
            match(":-"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE"

    // $ANTLR start "DOLLAR"
    public final void mDOLLAR() throws RecognitionException {
        try {
            int _type = DOLLAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:21:7: ( '$' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:21:8: '$'
            {
            match('$'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOLLAR"

    // $ANTLR start "LOWER"
    public final void mLOWER() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:23:15: ( 'a' .. 'z' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:23:16: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "LOWER"

    // $ANTLR start "UPPER"
    public final void mUPPER() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:24:15: ( 'A' .. 'Z' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:24:16: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UPPER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:25:15: ( '0' .. '9' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:25:16: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "SYMBOLS"
    public final void mSYMBOLS() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:26:17: ( ':' | '/' | '.' | '#' | '_' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:
            {
            if ( input.LA(1)=='#'||(input.LA(1)>='.' && input.LA(1)<='/')||input.LA(1)==':'||input.LA(1)=='_' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "SYMBOLS"

    // $ANTLR start "ALPHANUMERIC"
    public final void mALPHANUMERIC() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:27:22: ( LOWER | UPPER | DIGIT )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ALPHANUMERIC"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:29:4: ( 'not' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:29:5: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "LIMIT"
    public final void mLIMIT() throws RecognitionException {
        try {
            int _type = LIMIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:30:6: ( 'limit' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:30:7: 'limit'
            {
            match("limit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LIMIT"

    // $ANTLR start "ORDERBY"
    public final void mORDERBY() throws RecognitionException {
        try {
            int _type = ORDERBY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:31:8: ( 'order by' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:31:9: 'order by'
            {
            match("order by"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ORDERBY"

    // $ANTLR start "USING"
    public final void mUSING() throws RecognitionException {
        try {
            int _type = USING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:32:6: ( 'using' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:32:7: 'using'
            {
            match("using"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "USING"

    // $ANTLR start "IMPORT"
    public final void mIMPORT() throws RecognitionException {
        try {
            int _type = IMPORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:33:7: ( 'import' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:33:8: 'import'
            {
            match("import"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IMPORT"

    // $ANTLR start "FOR"
    public final void mFOR() throws RecognitionException {
        try {
            int _type = FOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:34:4: ( 'for' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:34:5: 'for'
            {
            match("for"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FOR"

    // $ANTLR start "AS"
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:35:3: ( 'as' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:35:4: 'as'
            {
            match("as"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AS"

    // $ANTLR start "SELECT"
    public final void mSELECT() throws RecognitionException {
        try {
            int _type = SELECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:36:7: ( 'select' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:36:8: 'select'
            {
            match("select"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SELECT"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:37:5: ( 'from' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:37:6: 'from'
            {
            match("from"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FROM"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:38:6: ( 'count' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:38:7: 'count'
            {
            match("count"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "ASC"
    public final void mASC() throws RecognitionException {
        try {
            int _type = ASC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:39:4: ( 'asc' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:39:5: 'asc'
            {
            match("asc"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASC"

    // $ANTLR start "DESC"
    public final void mDESC() throws RecognitionException {
        try {
            int _type = DESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:40:5: ( 'desc' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:40:6: 'desc'
            {
            match("desc"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DESC"

    // $ANTLR start "OFFSET"
    public final void mOFFSET() throws RecognitionException {
        try {
            int _type = OFFSET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:41:7: ( 'offset' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:41:8: 'offset'
            {
            match("offset"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OFFSET"

    // $ANTLR start "OBJID"
    public final void mOBJID() throws RecognitionException {
        try {
            int _type = OBJID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:43:6: ( '@' ( ALPHANUMERIC )+ )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:43:7: '@' ( ALPHANUMERIC )+
            {
            match('@'); 
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:43:11: ( ALPHANUMERIC )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:43:11: ALPHANUMERIC
            	    {
            	    mALPHANUMERIC(); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OBJID"

    // $ANTLR start "QNAME"
    public final void mQNAME() throws RecognitionException {
        try {
            int _type = QNAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:44:6: ( IDENT ( DOUBLEDOT ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )+ )+ )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:44:8: IDENT ( DOUBLEDOT ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )+ )+
            {
            mIDENT(); 
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:44:14: ( DOUBLEDOT ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )+ )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==':') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:44:15: DOUBLEDOT ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )+
            	    {
            	    mDOUBLEDOT(); 
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:44:25: ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )+
            	    int cnt3=0;
            	    loop3:
            	    do {
            	        int alt3=2;
            	        int LA3_0 = input.LA(1);

            	        if ( ((LA3_0>='-' && LA3_0<='.')||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
            	            alt3=1;
            	        }


            	        switch (alt3) {
            	    	case 1 :
            	    	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:
            	    	    {
            	    	    if ( (input.LA(1)>='-' && input.LA(1)<='.')||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	    	        input.consume();

            	    	    }
            	    	    else {
            	    	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	    	        recover(mse);
            	    	        throw mse;}


            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt3 >= 1 ) break loop3;
            	                EarlyExitException eee =
            	                    new EarlyExitException(3, input);
            	                throw eee;
            	        }
            	        cnt3++;
            	    } while (true);


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QNAME"

    // $ANTLR start "IDENT"
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:45:6: ( ( LOWER | UPPER | UNDERSCORE ) ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )* )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:45:7: ( LOWER | UPPER | UNDERSCORE ) ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:45:32: ( ALPHANUMERIC | DOT | HYPEN | UNDERSCORE )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='-' && LA5_0<='.')||(LA5_0>='0' && LA5_0<='9')||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:
            	    {
            	    if ( (input.LA(1)>='-' && input.LA(1)<='.')||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENT"

    // $ANTLR start "INDICATOR"
    public final void mINDICATOR() throws RecognitionException {
        try {
            int _type = INDICATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:46:10: ( 'i' URL )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:46:11: 'i' URL
            {
            match('i'); 
            mURL(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INDICATOR"

    // $ANTLR start "ADDRESS"
    public final void mADDRESS() throws RecognitionException {
        try {
            int _type = ADDRESS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:47:8: ( 'a' URL )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:47:9: 'a' URL
            {
            match('a'); 
            mURL(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ADDRESS"

    // $ANTLR start "SRCLOC"
    public final void mSRCLOC() throws RecognitionException {
        try {
            int _type = SRCLOC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:48:7: ( 's' URL )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:48:8: 's' URL
            {
            match('s'); 
            mURL(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SRCLOC"

    // $ANTLR start "URL"
    public final void mURL() throws RecognitionException {
        try {
            int _type = URL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:49:4: ( '\"' ( ALPHANUMERIC | SYMBOLS )+ '\"' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:49:5: '\"' ( ALPHANUMERIC | SYMBOLS )+ '\"'
            {
            match('\"'); 
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:49:9: ( ALPHANUMERIC | SYMBOLS )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='#'||(LA6_0>='.' && LA6_0<=':')||(LA6_0>='A' && LA6_0<='Z')||LA6_0=='_'||(LA6_0>='a' && LA6_0<='z')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:
            	    {
            	    if ( input.LA(1)=='#'||(input.LA(1)>='.' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "URL"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:50:7: ( '\"' ( ALPHANUMERIC | '\"' '\"' )* '\"' )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:50:8: '\"' ( ALPHANUMERIC | '\"' '\"' )* '\"'
            {
            match('\"'); 
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:50:12: ( ALPHANUMERIC | '\"' '\"' )*
            loop7:
            do {
                int alt7=3;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\"') ) {
                    int LA7_1 = input.LA(2);

                    if ( (LA7_1=='\"') ) {
                        alt7=2;
                    }


                }
                else if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:50:13: ALPHANUMERIC
            	    {
            	    mALPHANUMERIC(); 

            	    }
            	    break;
            	case 2 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:50:27: '\"' '\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:51:8: ( ( DIGIT )+ )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:51:9: ( DIGIT )+
            {
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:51:9: ( DIGIT )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:51:9: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "VARIABLE"
    public final void mVARIABLE() throws RecognitionException {
        try {
            int _type = VARIABLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:52:9: ( DOLLAR IDENT )
            // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:52:11: DOLLAR IDENT
            {
            mDOLLAR(); 
            mIDENT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VARIABLE"

    public void mTokens() throws RecognitionException {
        // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:8: ( COMMA | ROUND_BRACKETS_OPEN | ROUND_BRACKETS_CLOSE | ANGLE_BRACKETS_OPEN | ANGLE_BRACKETS_CLOSE | DOUBLEDOT | OR | OPERATOR | QUESTIONMARK | UNDERSCORE | DOT | HYPEN | WHITESPACE | PERCENT | RULE | DOLLAR | NOT | LIMIT | ORDERBY | USING | IMPORT | FOR | AS | SELECT | FROM | COUNT | ASC | DESC | OFFSET | OBJID | QNAME | IDENT | INDICATOR | ADDRESS | SRCLOC | URL | STRING | INTEGER | VARIABLE )
        int alt9=39;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:10: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 2 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:16: ROUND_BRACKETS_OPEN
                {
                mROUND_BRACKETS_OPEN(); 

                }
                break;
            case 3 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:36: ROUND_BRACKETS_CLOSE
                {
                mROUND_BRACKETS_CLOSE(); 

                }
                break;
            case 4 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:57: ANGLE_BRACKETS_OPEN
                {
                mANGLE_BRACKETS_OPEN(); 

                }
                break;
            case 5 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:77: ANGLE_BRACKETS_CLOSE
                {
                mANGLE_BRACKETS_CLOSE(); 

                }
                break;
            case 6 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:98: DOUBLEDOT
                {
                mDOUBLEDOT(); 

                }
                break;
            case 7 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:108: OR
                {
                mOR(); 

                }
                break;
            case 8 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:111: OPERATOR
                {
                mOPERATOR(); 

                }
                break;
            case 9 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:120: QUESTIONMARK
                {
                mQUESTIONMARK(); 

                }
                break;
            case 10 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:133: UNDERSCORE
                {
                mUNDERSCORE(); 

                }
                break;
            case 11 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:144: DOT
                {
                mDOT(); 

                }
                break;
            case 12 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:148: HYPEN
                {
                mHYPEN(); 

                }
                break;
            case 13 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:154: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 14 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:165: PERCENT
                {
                mPERCENT(); 

                }
                break;
            case 15 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:173: RULE
                {
                mRULE(); 

                }
                break;
            case 16 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:178: DOLLAR
                {
                mDOLLAR(); 

                }
                break;
            case 17 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:185: NOT
                {
                mNOT(); 

                }
                break;
            case 18 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:189: LIMIT
                {
                mLIMIT(); 

                }
                break;
            case 19 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:195: ORDERBY
                {
                mORDERBY(); 

                }
                break;
            case 20 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:203: USING
                {
                mUSING(); 

                }
                break;
            case 21 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:209: IMPORT
                {
                mIMPORT(); 

                }
                break;
            case 22 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:216: FOR
                {
                mFOR(); 

                }
                break;
            case 23 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:220: AS
                {
                mAS(); 

                }
                break;
            case 24 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:223: SELECT
                {
                mSELECT(); 

                }
                break;
            case 25 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:230: FROM
                {
                mFROM(); 

                }
                break;
            case 26 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:235: COUNT
                {
                mCOUNT(); 

                }
                break;
            case 27 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:241: ASC
                {
                mASC(); 

                }
                break;
            case 28 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:245: DESC
                {
                mDESC(); 

                }
                break;
            case 29 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:250: OFFSET
                {
                mOFFSET(); 

                }
                break;
            case 30 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:257: OBJID
                {
                mOBJID(); 

                }
                break;
            case 31 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:263: QNAME
                {
                mQNAME(); 

                }
                break;
            case 32 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:269: IDENT
                {
                mIDENT(); 

                }
                break;
            case 33 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:275: INDICATOR
                {
                mINDICATOR(); 

                }
                break;
            case 34 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:285: ADDRESS
                {
                mADDRESS(); 

                }
                break;
            case 35 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:293: SRCLOC
                {
                mSRCLOC(); 

                }
                break;
            case 36 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:300: URL
                {
                mURL(); 

                }
                break;
            case 37 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:304: STRING
                {
                mSTRING(); 

                }
                break;
            case 38 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:311: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 39 :
                // D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\lexer\\TologLexer.g:1:319: VARIABLE
                {
                mVARIABLE(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\6\uffff\1\37\3\uffff\1\40\4\uffff\1\43\12\46\1\uffff\1\46\5\uffff"+
        "\1\46\3\uffff\1\46\1\uffff\5\46\1\uffff\2\46\1\101\1\uffff\1\46"+
        "\1\uffff\2\46\3\uffff\1\106\5\46\1\114\1\46\1\116\1\uffff\3\46\1"+
        "\67\1\uffff\5\46\1\uffff\1\127\1\uffff\2\46\1\132\1\133\2\46\1\136"+
        "\1\46\1\uffff\1\46\1\141\3\uffff\1\142\1\uffff\1\143\1\144\4\uffff";
    static final String DFA9_eofS =
        "\145\uffff";
    static final String DFA9_minS =
        "\1\11\5\uffff\1\55\3\uffff\1\55\4\uffff\1\101\4\55\1\42\1\55\2"+
        "\42\2\55\1\uffff\1\55\1\42\4\uffff\1\55\3\uffff\1\55\1\uffff\5\55"+
        "\1\uffff\3\55\1\uffff\1\55\1\uffff\2\55\1\42\2\uffff\11\55\1\uffff"+
        "\3\55\1\42\1\uffff\5\55\1\uffff\1\55\1\uffff\4\55\1\40\3\55\1\uffff"+
        "\2\55\3\uffff\1\55\1\uffff\2\55\4\uffff";
    static final String DFA9_maxS =
        "\1\175\5\uffff\1\55\3\uffff\1\172\4\uffff\13\172\1\uffff\2\172"+
        "\4\uffff\1\172\3\uffff\1\172\1\uffff\5\172\1\uffff\3\172\1\uffff"+
        "\1\172\1\uffff\3\172\2\uffff\11\172\1\uffff\3\172\1\42\1\uffff\5"+
        "\172\1\uffff\1\172\1\uffff\10\172\1\uffff\2\172\3\uffff\1\172\1"+
        "\uffff\2\172\4\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\uffff\1\7\1\10\1\11\1\uffff\1\13"+
        "\1\14\1\15\1\16\13\uffff\1\36\2\uffff\1\46\1\17\1\6\1\12\1\uffff"+
        "\1\37\1\20\1\47\1\uffff\1\40\5\uffff\1\41\3\uffff\1\42\1\uffff\1"+
        "\43\3\uffff\1\45\1\44\11\uffff\1\27\4\uffff\1\21\5\uffff\1\26\1"+
        "\uffff\1\33\10\uffff\1\31\2\uffff\1\34\1\22\1\23\1\uffff\1\24\2"+
        "\uffff\1\32\1\35\1\25\1\30";
    static final String DFA9_specialS =
        "\145\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\15\26\uffff\1\15\1\uffff\1\34\1\uffff\1\17\1\16\2\uffff"+
            "\1\2\1\3\2\uffff\1\1\1\14\1\13\1\10\12\35\1\6\4\uffff\1\11\1"+
            "\32\32\33\4\uffff\1\12\1\uffff\1\26\1\33\1\30\1\31\1\33\1\25"+
            "\2\33\1\24\2\33\1\21\1\33\1\20\1\22\3\33\1\27\1\33\1\23\5\33"+
            "\1\4\1\7\1\5",
            "",
            "",
            "",
            "",
            "",
            "\1\36",
            "",
            "",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "",
            "",
            "",
            "\32\44\4\uffff\1\44\1\uffff\32\44",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\16\41\1\45\13\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\10\41\1\47\21\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\5\41\1\51\13\41\1\50\10\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\22\41\1\52\7\41",
            "\1\54\12\uffff\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff"+
            "\1\41\1\uffff\14\41\1\53\15\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\16\41\1\55\2\41\1\56\10\41",
            "\1\60\12\uffff\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff"+
            "\1\41\1\uffff\22\41\1\57\7\41",
            "\1\62\12\uffff\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff"+
            "\1\41\1\uffff\4\41\1\61\25\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\16\41\1\63\13\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\4\41\1\64\25\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\1\66\1\67\12\uffff\2\67\12\65\1\67\6\uffff\32\65\4\uffff"+
            "\1\67\1\uffff\32\65",
            "",
            "",
            "",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\23\41\1\70\6\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\14\41\1\71\15\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\3\41\1\72\26\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\5\41\1\73\24\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\10\41\1\74\21\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\17\41\1\75\12\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\21\41\1\76\10\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\16\41\1\77\13\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\2\41\1\100\27\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\13\41\1\102\16\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\24\41\1\103\5\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\22\41\1\104\7\41",
            "\1\105\1\67\12\uffff\2\67\12\65\1\67\6\uffff\32\65\4\uffff"+
            "\1\67\1\uffff\32\65",
            "",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\10\41\1\107\21\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\4\41\1\110\25\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\22\41\1\111\7\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\15\41\1\112\14\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\16\41\1\113\13\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\14\41\1\115\15\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\4\41\1\117\25\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\15\41\1\120\14\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\2\41\1\121\27\41",
            "\1\66",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\23\41\1\122\6\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\21\41\1\123\10\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\4\41\1\124\25\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\6\41\1\125\23\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\21\41\1\126\10\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\2\41\1\130\27\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\23\41\1\131\6\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\1\134\14\uffff\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff"+
            "\1\41\1\uffff\32\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\23\41\1\135\6\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\23\41\1\137\6\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\23\41\1\140\6\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "\2\41\1\uffff\12\41\1\42\6\uffff\32\41\4\uffff\1\41\1\uffff"+
            "\32\41",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( COMMA | ROUND_BRACKETS_OPEN | ROUND_BRACKETS_CLOSE | ANGLE_BRACKETS_OPEN | ANGLE_BRACKETS_CLOSE | DOUBLEDOT | OR | OPERATOR | QUESTIONMARK | UNDERSCORE | DOT | HYPEN | WHITESPACE | PERCENT | RULE | DOLLAR | NOT | LIMIT | ORDERBY | USING | IMPORT | FOR | AS | SELECT | FROM | COUNT | ASC | DESC | OFFSET | OBJID | QNAME | IDENT | INDICATOR | ADDRESS | SRCLOC | URL | STRING | INTEGER | VARIABLE );";
        }
    }
 

}