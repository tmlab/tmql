// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g 2009-10-14 11:04:51
 package de.topicmapslab.tmql4j.tolog.components.parser; 

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;

public class TologTree extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMMA", "ROUND_BRACKETS_OPEN", "ROUND_BRACKETS_CLOSE", "ANGLE_BRACKETS_OPEN", "ANGLE_BRACKETS_CLOSE", "DOUBLEDOT", "OR", "OPERATOR", "QUESTIONMARK", "UNDERSCORE", "DOT", "HYPEN", "WHITESPACE", "PERCENT", "RULE", "DOLLAR", "LOWER", "UPPER", "DIGIT", "SYMBOLS", "ALPHANUMERIC", "NOT", "LIMIT", "ORDERBY", "USING", "IMPORT", "FOR", "AS", "SELECT", "FROM", "COUNT", "ASC", "DESC", "OFFSET", "OBJID", "IDENT", "QNAME", "URL", "INDICATOR", "ADDRESS", "SRCLOC", "STRING", "INTEGER", "VARIABLE"
    };
    public static final int DOLLAR=19;
    public static final int LIMIT=26;
    public static final int FOR=30;
    public static final int COUNT=34;
    public static final int ORDERBY=27;
    public static final int ANGLE_BRACKETS_OPEN=7;
    public static final int DOUBLEDOT=9;
    public static final int NOT=25;
    public static final int EOF=-1;
    public static final int AS=31;
    public static final int IMPORT=29;
    public static final int USING=28;
    public static final int COMMA=4;
    public static final int OFFSET=37;
    public static final int ALPHANUMERIC=24;
    public static final int ROUND_BRACKETS_OPEN=5;
    public static final int QUESTIONMARK=12;
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
    public static final int DESC=36;
    public static final int SYMBOLS=23;
    public static final int FROM=33;
    public static final int UPPER=21;
    public static final int STRING=45;

    // delegates
    // delegators


        public TologTree(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public TologTree(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return TologTree.tokenNames; }
    public String getGrammarFileName() { return "D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g"; }



    // $ANTLR start "query"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:1: query : ( head )? clauselist ( tail )? QUESTIONMARK ;
    public final void query() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:6: ( ( head )? clauselist ( tail )? QUESTIONMARK )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:9: ( head )? clauselist ( tail )? QUESTIONMARK
            {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:9: ( head )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>=USING && LA1_0<=IMPORT)) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:9: head
                    {
                    pushFollow(FOLLOW_head_in_query54);
                    head();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_clauselist_in_query57);
            clauselist();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:26: ( tail )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0>=LIMIT && LA2_0<=ORDERBY)||LA2_0==OFFSET) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:16:26: tail
                    {
                    pushFollow(FOLLOW_tail_in_query59);
                    tail();

                    state._fsp--;


                    }
                    break;

            }

            match(input,QUESTIONMARK,FOLLOW_QUESTIONMARK_in_query62); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "query"


    // $ANTLR start "clauselist"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:1: clauselist : clause ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )* ;
    public final void clauselist() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:11: ( clause ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )* )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:16: clause ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )*
            {
            pushFollow(FOLLOW_clause_in_clauselist71);
            clause();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:23: ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==WHITESPACE) ) {
                    int LA5_2 = input.LA(2);

                    if ( (LA5_2==COMMA) ) {
                        alt5=1;
                    }


                }
                else if ( (LA5_0==COMMA) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:24: ( WHITESPACE )? COMMA ( WHITESPACE )? clause
            	    {
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:24: ( WHITESPACE )?
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==WHITESPACE) ) {
            	        alt3=1;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:24: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_clauselist74); 

            	            }
            	            break;

            	    }

            	    match(input,COMMA,FOLLOW_COMMA_in_clauselist77); 
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:42: ( WHITESPACE )?
            	    int alt4=2;
            	    int LA4_0 = input.LA(1);

            	    if ( (LA4_0==WHITESPACE) ) {
            	        alt4=1;
            	    }
            	    switch (alt4) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:17:42: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_clauselist79); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_clause_in_clauselist82);
            	    clause();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "clauselist"


    // $ANTLR start "clause"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:18:1: clause : predclause ;
    public final void clause() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:18:7: ( predclause )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:18:12: predclause
            {
            pushFollow(FOLLOW_predclause_in_clause93);
            predclause();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "clause"


    // $ANTLR start "predclause"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:1: predclause : IDENT ROUND_BRACKETS_OPEN pair ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )* ( WHITESPACE )? ROUND_BRACKETS_CLOSE ;
    public final void predclause() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:11: ( IDENT ROUND_BRACKETS_OPEN pair ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )* ( WHITESPACE )? ROUND_BRACKETS_CLOSE )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:16: IDENT ROUND_BRACKETS_OPEN pair ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )* ( WHITESPACE )? ROUND_BRACKETS_CLOSE
            {
            match(input,IDENT,FOLLOW_IDENT_in_predclause104); 
            match(input,ROUND_BRACKETS_OPEN,FOLLOW_ROUND_BRACKETS_OPEN_in_predclause106); 
            pushFollow(FOLLOW_pair_in_predclause108);
            pair();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:47: ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==WHITESPACE) ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1==COMMA) ) {
                        alt8=1;
                    }


                }
                else if ( (LA8_0==COMMA) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:48: ( WHITESPACE )? COMMA ( WHITESPACE )? pair
            	    {
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:48: ( WHITESPACE )?
            	    int alt6=2;
            	    int LA6_0 = input.LA(1);

            	    if ( (LA6_0==WHITESPACE) ) {
            	        alt6=1;
            	    }
            	    switch (alt6) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:48: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_predclause111); 

            	            }
            	            break;

            	    }

            	    match(input,COMMA,FOLLOW_COMMA_in_predclause114); 
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:66: ( WHITESPACE )?
            	    int alt7=2;
            	    int LA7_0 = input.LA(1);

            	    if ( (LA7_0==WHITESPACE) ) {
            	        alt7=1;
            	    }
            	    switch (alt7) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:66: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_predclause116); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_pair_in_predclause119);
            	    pair();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:85: ( WHITESPACE )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==WHITESPACE) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:20:85: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_predclause123); 

                    }
                    break;

            }

            match(input,ROUND_BRACKETS_CLOSE,FOLLOW_ROUND_BRACKETS_CLOSE_in_predclause126); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "predclause"


    // $ANTLR start "pair"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:1: pair : expr ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )? ;
    public final void pair() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:5: ( expr ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )? )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:10: expr ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )?
            {
            pushFollow(FOLLOW_expr_in_pair135);
            expr();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:15: ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==WHITESPACE) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==DOUBLEDOT) ) {
                    alt12=1;
                }
            }
            else if ( (LA12_0==DOUBLEDOT) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:16: ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref
                    {
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:16: ( WHITESPACE )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==WHITESPACE) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:16: WHITESPACE
                            {
                            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_pair138); 

                            }
                            break;

                    }

                    match(input,DOUBLEDOT,FOLLOW_DOUBLEDOT_in_pair141); 
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:38: ( WHITESPACE )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WHITESPACE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:21:38: WHITESPACE
                            {
                            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_pair143); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_ref_in_pair146);
                    ref();

                    state._fsp--;


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "pair"


    // $ANTLR start "opclause"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:22:1: opclause : expr OPERATOR expr ;
    public final void opclause() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:22:9: ( expr OPERATOR expr )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:22:14: expr OPERATOR expr
            {
            pushFollow(FOLLOW_expr_in_opclause157);
            expr();

            state._fsp--;

            match(input,OPERATOR,FOLLOW_OPERATOR_in_opclause159); 
            pushFollow(FOLLOW_expr_in_opclause161);
            expr();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "opclause"


    // $ANTLR start "orclause"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:1: orclause : ANGLE_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ( OR ( WHITESPACE )? clauselist )* ANGLE_BRACKETS_CLOSE ;
    public final void orclause() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:9: ( ANGLE_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ( OR ( WHITESPACE )? clauselist )* ANGLE_BRACKETS_CLOSE )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:14: ANGLE_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ( OR ( WHITESPACE )? clauselist )* ANGLE_BRACKETS_CLOSE
            {
            match(input,ANGLE_BRACKETS_OPEN,FOLLOW_ANGLE_BRACKETS_OPEN_in_orclause170); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:34: ( WHITESPACE )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WHITESPACE) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:34: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_orclause172); 

                    }
                    break;

            }

            pushFollow(FOLLOW_clauselist_in_orclause175);
            clauselist();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:57: ( WHITESPACE )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==WHITESPACE) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:57: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_orclause177); 

                    }
                    break;

            }

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:68: ( OR ( WHITESPACE )? clauselist )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==OR) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:69: OR ( WHITESPACE )? clauselist
            	    {
            	    match(input,OR,FOLLOW_OR_in_orclause180); 
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:72: ( WHITESPACE )?
            	    int alt15=2;
            	    int LA15_0 = input.LA(1);

            	    if ( (LA15_0==WHITESPACE) ) {
            	        alt15=1;
            	    }
            	    switch (alt15) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:23:72: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_orclause182); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_clauselist_in_orclause185);
            	    clauselist();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            match(input,ANGLE_BRACKETS_CLOSE,FOLLOW_ANGLE_BRACKETS_CLOSE_in_orclause189); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "orclause"


    // $ANTLR start "notclause"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:1: notclause : NOT ( WHITESPACE )? ROUND_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ;
    public final void notclause() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:10: ( NOT ( WHITESPACE )? ROUND_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ROUND_BRACKETS_CLOSE )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:15: NOT ( WHITESPACE )? ROUND_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ROUND_BRACKETS_CLOSE
            {
            match(input,NOT,FOLLOW_NOT_in_notclause198); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:19: ( WHITESPACE )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==WHITESPACE) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:19: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_notclause200); 

                    }
                    break;

            }

            match(input,ROUND_BRACKETS_OPEN,FOLLOW_ROUND_BRACKETS_OPEN_in_notclause203); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:51: ( WHITESPACE )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==WHITESPACE) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:51: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_notclause205); 

                    }
                    break;

            }

            pushFollow(FOLLOW_clauselist_in_notclause208);
            clauselist();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:74: ( WHITESPACE )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==WHITESPACE) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:24:74: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_notclause210); 

                    }
                    break;

            }

            match(input,ROUND_BRACKETS_CLOSE,FOLLOW_ROUND_BRACKETS_CLOSE_in_notclause213); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "notclause"


    // $ANTLR start "expr"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:25:1: expr : ( VARIABLE | ref | value | parameter );
    public final void expr() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:25:5: ( VARIABLE | ref | value | parameter )
            int alt20=4;
            switch ( input.LA(1) ) {
            case VARIABLE:
                {
                alt20=1;
                }
                break;
            case OBJID:
            case QNAME:
            case INDICATOR:
            case ADDRESS:
            case SRCLOC:
                {
                alt20=2;
                }
                break;
            case STRING:
                {
                alt20=3;
                }
                break;
            case PERCENT:
                {
                alt20=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:25:10: VARIABLE
                    {
                    match(input,VARIABLE,FOLLOW_VARIABLE_in_expr222); 

                    }
                    break;
                case 2 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:25:21: ref
                    {
                    pushFollow(FOLLOW_ref_in_expr226);
                    ref();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:25:27: value
                    {
                    pushFollow(FOLLOW_value_in_expr230);
                    value();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:25:35: parameter
                    {
                    pushFollow(FOLLOW_parameter_in_expr234);
                    parameter();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "expr"


    // $ANTLR start "parameter"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:26:1: parameter : PERCENT IDENT PERCENT ;
    public final void parameter() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:26:10: ( PERCENT IDENT PERCENT )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:26:15: PERCENT IDENT PERCENT
            {
            match(input,PERCENT,FOLLOW_PERCENT_in_parameter243); 
            match(input,IDENT,FOLLOW_IDENT_in_parameter245); 
            match(input,PERCENT,FOLLOW_PERCENT_in_parameter247); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "parameter"


    // $ANTLR start "ref"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:27:1: ref : ( OBJID | QNAME | uriref );
    public final void ref() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:27:4: ( OBJID | QNAME | uriref )
            int alt21=3;
            switch ( input.LA(1) ) {
            case OBJID:
                {
                alt21=1;
                }
                break;
            case QNAME:
                {
                alt21=2;
                }
                break;
            case INDICATOR:
            case ADDRESS:
            case SRCLOC:
                {
                alt21=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:27:10: OBJID
                    {
                    match(input,OBJID,FOLLOW_OBJID_in_ref257); 

                    }
                    break;
                case 2 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:27:18: QNAME
                    {
                    match(input,QNAME,FOLLOW_QNAME_in_ref261); 

                    }
                    break;
                case 3 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:27:26: uriref
                    {
                    pushFollow(FOLLOW_uriref_in_ref265);
                    uriref();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "ref"


    // $ANTLR start "uriref"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:28:1: uriref : ( INDICATOR | ADDRESS | SRCLOC );
    public final void uriref() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:28:7: ( INDICATOR | ADDRESS | SRCLOC )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:
            {
            if ( (input.LA(1)>=INDICATOR && input.LA(1)<=SRCLOC) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "uriref"


    // $ANTLR start "value"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:29:1: value : STRING ;
    public final void value() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:29:6: ( STRING )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:29:11: STRING
            {
            match(input,STRING,FOLLOW_STRING_in_value291); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "value"


    // $ANTLR start "ruleset"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:1: ruleset : ( usingpart | importpart )* ( rule )+ ;
    public final void ruleset() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:8: ( ( usingpart | importpart )* ( rule )+ )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:13: ( usingpart | importpart )* ( rule )+
            {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:13: ( usingpart | importpart )*
            loop22:
            do {
                int alt22=3;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==USING) ) {
                    alt22=1;
                }
                else if ( (LA22_0==IMPORT) ) {
                    alt22=2;
                }


                switch (alt22) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:14: usingpart
            	    {
            	    pushFollow(FOLLOW_usingpart_in_ruleset301);
            	    usingpart();

            	    state._fsp--;


            	    }
            	    break;
            	case 2 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:26: importpart
            	    {
            	    pushFollow(FOLLOW_importpart_in_ruleset305);
            	    importpart();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:39: ( rule )+
            int cnt23=0;
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==IDENT) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:30:39: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset309);
            	    rule();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt23 >= 1 ) break loop23;
                        EarlyExitException eee =
                            new EarlyExitException(23, input);
                        throw eee;
                }
                cnt23++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "ruleset"


    // $ANTLR start "rule"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:1: rule : IDENT ROUND_BRACKETS_OPEN ( WHITESPACE )? varlist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ( WHITESPACE )? RULE ( WHITESPACE )? clauselist ( WHITESPACE )? DOT ;
    public final void rule() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:5: ( IDENT ROUND_BRACKETS_OPEN ( WHITESPACE )? varlist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ( WHITESPACE )? RULE ( WHITESPACE )? clauselist ( WHITESPACE )? DOT )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:10: IDENT ROUND_BRACKETS_OPEN ( WHITESPACE )? varlist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ( WHITESPACE )? RULE ( WHITESPACE )? clauselist ( WHITESPACE )? DOT
            {
            match(input,IDENT,FOLLOW_IDENT_in_rule319); 
            match(input,ROUND_BRACKETS_OPEN,FOLLOW_ROUND_BRACKETS_OPEN_in_rule321); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:36: ( WHITESPACE )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==WHITESPACE) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:36: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_rule323); 

                    }
                    break;

            }

            pushFollow(FOLLOW_varlist_in_rule326);
            varlist();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:56: ( WHITESPACE )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==WHITESPACE) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:56: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_rule328); 

                    }
                    break;

            }

            match(input,ROUND_BRACKETS_CLOSE,FOLLOW_ROUND_BRACKETS_CLOSE_in_rule331); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:89: ( WHITESPACE )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==WHITESPACE) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:89: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_rule333); 

                    }
                    break;

            }

            match(input,RULE,FOLLOW_RULE_in_rule336); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:106: ( WHITESPACE )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==WHITESPACE) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:106: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_rule338); 

                    }
                    break;

            }

            pushFollow(FOLLOW_clauselist_in_rule341);
            clauselist();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:129: ( WHITESPACE )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==WHITESPACE) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:31:129: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_rule343); 

                    }
                    break;

            }

            match(input,DOT,FOLLOW_DOT_in_rule346); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "rule"


    // $ANTLR start "varlist"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:1: varlist : VARIABLE ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )* ;
    public final void varlist() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:8: ( VARIABLE ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )* )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:13: VARIABLE ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )*
            {
            match(input,VARIABLE,FOLLOW_VARIABLE_in_varlist355); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:22: ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==WHITESPACE) ) {
                    int LA31_1 = input.LA(2);

                    if ( (LA31_1==COMMA) ) {
                        alt31=1;
                    }


                }
                else if ( (LA31_0==COMMA) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:23: ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE
            	    {
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:23: ( WHITESPACE )?
            	    int alt29=2;
            	    int LA29_0 = input.LA(1);

            	    if ( (LA29_0==WHITESPACE) ) {
            	        alt29=1;
            	    }
            	    switch (alt29) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:23: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_varlist358); 

            	            }
            	            break;

            	    }

            	    match(input,COMMA,FOLLOW_COMMA_in_varlist361); 
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:41: ( WHITESPACE )?
            	    int alt30=2;
            	    int LA30_0 = input.LA(1);

            	    if ( (LA30_0==WHITESPACE) ) {
            	        alt30=1;
            	    }
            	    switch (alt30) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:32:41: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_varlist363); 

            	            }
            	            break;

            	    }

            	    match(input,VARIABLE,FOLLOW_VARIABLE_in_varlist366); 

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "varlist"


    // $ANTLR start "head"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:1: head : ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) )+ ( selectpart WHITESPACE )? ;
    public final void head() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:5: ( ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) )+ ( selectpart WHITESPACE )? )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:10: ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) )+ ( selectpart WHITESPACE )?
            {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:10: ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) )+
            int cnt32=0;
            loop32:
            do {
                int alt32=3;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==USING) ) {
                    alt32=1;
                }
                else if ( (LA32_0==IMPORT) ) {
                    alt32=2;
                }


                switch (alt32) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:11: ( usingpart WHITESPACE )
            	    {
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:11: ( usingpart WHITESPACE )
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:12: usingpart WHITESPACE
            	    {
            	    pushFollow(FOLLOW_usingpart_in_head379);
            	    usingpart();

            	    state._fsp--;

            	    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_head381); 

            	    }


            	    }
            	    break;
            	case 2 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:36: ( importpart WHITESPACE )
            	    {
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:36: ( importpart WHITESPACE )
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:37: importpart WHITESPACE
            	    {
            	    pushFollow(FOLLOW_importpart_in_head387);
            	    importpart();

            	    state._fsp--;

            	    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_head389); 

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt32 >= 1 ) break loop32;
                        EarlyExitException eee =
                            new EarlyExitException(32, input);
                        throw eee;
                }
                cnt32++;
            } while (true);

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:62: ( selectpart WHITESPACE )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==SELECT) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:33:63: selectpart WHITESPACE
                    {
                    pushFollow(FOLLOW_selectpart_in_head395);
                    selectpart();

                    state._fsp--;

                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_head397); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "head"


    // $ANTLR start "usingpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:34:1: usingpart : USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref ;
    public final void usingpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:34:10: ( USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:34:15: USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref
            {
            match(input,USING,FOLLOW_USING_in_usingpart408); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_usingpart410); 
            match(input,IDENT,FOLLOW_IDENT_in_usingpart412); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_usingpart414); 
            match(input,FOR,FOLLOW_FOR_in_usingpart416); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_usingpart418); 
            pushFollow(FOLLOW_uriref_in_usingpart420);
            uriref();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "usingpart"


    // $ANTLR start "importpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:35:1: importpart : IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT ;
    public final void importpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:35:11: ( IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:35:16: IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT
            {
            match(input,IMPORT,FOLLOW_IMPORT_in_importpart429); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_importpart431); 
            match(input,URL,FOLLOW_URL_in_importpart433); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_importpart435); 
            match(input,AS,FOLLOW_AS_in_importpart437); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_importpart439); 
            match(input,IDENT,FOLLOW_IDENT_in_importpart441); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "importpart"


    // $ANTLR start "selectpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:36:1: selectpart : SELECT WHITESPACE selectlist WHITESPACE FROM ;
    public final void selectpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:36:11: ( SELECT WHITESPACE selectlist WHITESPACE FROM )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:36:16: SELECT WHITESPACE selectlist WHITESPACE FROM
            {
            match(input,SELECT,FOLLOW_SELECT_in_selectpart450); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_selectpart452); 
            pushFollow(FOLLOW_selectlist_in_selectpart454);
            selectlist();

            state._fsp--;

            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_selectpart456); 
            match(input,FROM,FOLLOW_FROM_in_selectpart458); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectpart"


    // $ANTLR start "selectlist"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:1: selectlist : selpart ( WHITESPACE )? ( COMMA ( WHITESPACE )? selpart )* ;
    public final void selectlist() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:11: ( selpart ( WHITESPACE )? ( COMMA ( WHITESPACE )? selpart )* )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:16: selpart ( WHITESPACE )? ( COMMA ( WHITESPACE )? selpart )*
            {
            pushFollow(FOLLOW_selpart_in_selectlist467);
            selpart();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:24: ( WHITESPACE )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==WHITESPACE) ) {
                int LA34_1 = input.LA(2);

                if ( (LA34_1==COMMA||LA34_1==WHITESPACE) ) {
                    alt34=1;
                }
            }
            switch (alt34) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:24: WHITESPACE
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_selectlist469); 

                    }
                    break;

            }

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:36: ( COMMA ( WHITESPACE )? selpart )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==COMMA) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:37: COMMA ( WHITESPACE )? selpart
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_selectlist473); 
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:43: ( WHITESPACE )?
            	    int alt35=2;
            	    int LA35_0 = input.LA(1);

            	    if ( (LA35_0==WHITESPACE) ) {
            	        alt35=1;
            	    }
            	    switch (alt35) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:37:43: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_selectlist475); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_selpart_in_selectlist478);
            	    selpart();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectlist"


    // $ANTLR start "selpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:38:1: selpart : ( VARIABLE | aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE );
    public final void selpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:38:8: ( VARIABLE | aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==VARIABLE) ) {
                alt37=1;
            }
            else if ( (LA37_0==COUNT) ) {
                alt37=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:38:13: VARIABLE
                    {
                    match(input,VARIABLE,FOLLOW_VARIABLE_in_selpart489); 

                    }
                    break;
                case 2 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:38:24: aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE
                    {
                    pushFollow(FOLLOW_aggfun_in_selpart493);
                    aggfun();

                    state._fsp--;

                    match(input,ROUND_BRACKETS_OPEN,FOLLOW_ROUND_BRACKETS_OPEN_in_selpart495); 
                    match(input,VARIABLE,FOLLOW_VARIABLE_in_selpart497); 
                    match(input,ROUND_BRACKETS_CLOSE,FOLLOW_ROUND_BRACKETS_CLOSE_in_selpart499); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "selpart"


    // $ANTLR start "aggfun"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:39:1: aggfun : COUNT ;
    public final void aggfun() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:39:7: ( COUNT )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:39:12: COUNT
            {
            match(input,COUNT,FOLLOW_COUNT_in_aggfun508); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "aggfun"


    // $ANTLR start "tail"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:1: tail : ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+ ;
    public final void tail() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:5: ( ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+ )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:10: ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+
            {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:10: ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+
            int cnt41=0;
            loop41:
            do {
                int alt41=4;
                switch ( input.LA(1) ) {
                case ORDERBY:
                    {
                    alt41=1;
                    }
                    break;
                case LIMIT:
                    {
                    alt41=2;
                    }
                    break;
                case OFFSET:
                    {
                    alt41=3;
                    }
                    break;

                }

                switch (alt41) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:11: orderpart ( WHITESPACE )?
            	    {
            	    pushFollow(FOLLOW_orderpart_in_tail518);
            	    orderpart();

            	    state._fsp--;

            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:21: ( WHITESPACE )?
            	    int alt38=2;
            	    int LA38_0 = input.LA(1);

            	    if ( (LA38_0==WHITESPACE) ) {
            	        alt38=1;
            	    }
            	    switch (alt38) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:21: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_tail520); 

            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:35: limitpart ( WHITESPACE )?
            	    {
            	    pushFollow(FOLLOW_limitpart_in_tail525);
            	    limitpart();

            	    state._fsp--;

            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:45: ( WHITESPACE )?
            	    int alt39=2;
            	    int LA39_0 = input.LA(1);

            	    if ( (LA39_0==WHITESPACE) ) {
            	        alt39=1;
            	    }
            	    switch (alt39) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:45: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_tail527); 

            	            }
            	            break;

            	    }


            	    }
            	    break;
            	case 3 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:59: offsetpart ( WHITESPACE )?
            	    {
            	    pushFollow(FOLLOW_offsetpart_in_tail532);
            	    offsetpart();

            	    state._fsp--;

            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:70: ( WHITESPACE )?
            	    int alt40=2;
            	    int LA40_0 = input.LA(1);

            	    if ( (LA40_0==WHITESPACE) ) {
            	        alt40=1;
            	    }
            	    switch (alt40) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:40:70: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_tail534); 

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt41 >= 1 ) break loop41;
                        EarlyExitException eee =
                            new EarlyExitException(41, input);
                        throw eee;
                }
                cnt41++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "tail"


    // $ANTLR start "orderpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:41:1: orderpart : ORDERBY WHITESPACE orderlist ;
    public final void orderpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:41:10: ( ORDERBY WHITESPACE orderlist )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:41:15: ORDERBY WHITESPACE orderlist
            {
            match(input,ORDERBY,FOLLOW_ORDERBY_in_orderpart546); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_orderpart548); 
            pushFollow(FOLLOW_orderlist_in_orderpart550);
            orderlist();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "orderpart"


    // $ANTLR start "orderlist"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:1: orderlist : ordpart ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )* ;
    public final void orderlist() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:10: ( ordpart ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )* )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:15: ordpart ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )*
            {
            pushFollow(FOLLOW_ordpart_in_orderlist559);
            ordpart();

            state._fsp--;

            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:23: ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==WHITESPACE) ) {
                    int LA44_1 = input.LA(2);

                    if ( (LA44_1==COMMA) ) {
                        alt44=1;
                    }


                }
                else if ( (LA44_0==COMMA) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:24: ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart
            	    {
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:24: ( WHITESPACE )?
            	    int alt42=2;
            	    int LA42_0 = input.LA(1);

            	    if ( (LA42_0==WHITESPACE) ) {
            	        alt42=1;
            	    }
            	    switch (alt42) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:24: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_orderlist562); 

            	            }
            	            break;

            	    }

            	    match(input,COMMA,FOLLOW_COMMA_in_orderlist565); 
            	    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:42: ( WHITESPACE )?
            	    int alt43=2;
            	    int LA43_0 = input.LA(1);

            	    if ( (LA43_0==WHITESPACE) ) {
            	        alt43=1;
            	    }
            	    switch (alt43) {
            	        case 1 :
            	            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:42:42: WHITESPACE
            	            {
            	            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_orderlist567); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_ordpart_in_orderlist570);
            	    ordpart();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "orderlist"


    // $ANTLR start "ordpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:43:1: ordpart : VARIABLE ( WHITESPACE ( ASC | DESC ) )? ;
    public final void ordpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:43:8: ( VARIABLE ( WHITESPACE ( ASC | DESC ) )? )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:43:13: VARIABLE ( WHITESPACE ( ASC | DESC ) )?
            {
            match(input,VARIABLE,FOLLOW_VARIABLE_in_ordpart581); 
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:43:22: ( WHITESPACE ( ASC | DESC ) )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==WHITESPACE) ) {
                int LA45_1 = input.LA(2);

                if ( ((LA45_1>=ASC && LA45_1<=DESC)) ) {
                    alt45=1;
                }
            }
            switch (alt45) {
                case 1 :
                    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:43:23: WHITESPACE ( ASC | DESC )
                    {
                    match(input,WHITESPACE,FOLLOW_WHITESPACE_in_ordpart584); 
                    if ( (input.LA(1)>=ASC && input.LA(1)<=DESC) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "ordpart"


    // $ANTLR start "limitpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:44:1: limitpart : LIMIT WHITESPACE INTEGER ;
    public final void limitpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:44:10: ( LIMIT WHITESPACE INTEGER )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:44:15: LIMIT WHITESPACE INTEGER
            {
            match(input,LIMIT,FOLLOW_LIMIT_in_limitpart601); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_limitpart603); 
            match(input,INTEGER,FOLLOW_INTEGER_in_limitpart605); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "limitpart"


    // $ANTLR start "offsetpart"
    // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:45:1: offsetpart : OFFSET WHITESPACE INTEGER ;
    public final void offsetpart() throws RecognitionException {
        try {
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:45:11: ( OFFSET WHITESPACE INTEGER )
            // D:\\Krosse\\TMQL4J_Ex\\TologConverter\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologTree.g:45:16: OFFSET WHITESPACE INTEGER
            {
            match(input,OFFSET,FOLLOW_OFFSET_in_offsetpart614); 
            match(input,WHITESPACE,FOLLOW_WHITESPACE_in_offsetpart616); 
            match(input,INTEGER,FOLLOW_INTEGER_in_offsetpart618); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "offsetpart"

    // Delegated rules


 

    public static final BitSet FOLLOW_head_in_query54 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_clauselist_in_query57 = new BitSet(new long[]{0x000000200C001000L});
    public static final BitSet FOLLOW_tail_in_query59 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_QUESTIONMARK_in_query62 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clause_in_clauselist71 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_WHITESPACE_in_clauselist74 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_COMMA_in_clauselist77 = new BitSet(new long[]{0x0000008000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_clauselist79 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_clause_in_clauselist82 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_predclause_in_clause93 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_predclause104 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_predclause106 = new BitSet(new long[]{0x0000BD4000020000L});
    public static final BitSet FOLLOW_pair_in_predclause108 = new BitSet(new long[]{0x0000000000010050L});
    public static final BitSet FOLLOW_WHITESPACE_in_predclause111 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_COMMA_in_predclause114 = new BitSet(new long[]{0x0000BD4000030000L});
    public static final BitSet FOLLOW_WHITESPACE_in_predclause116 = new BitSet(new long[]{0x0000BD4000020000L});
    public static final BitSet FOLLOW_pair_in_predclause119 = new BitSet(new long[]{0x0000000000010050L});
    public static final BitSet FOLLOW_WHITESPACE_in_predclause123 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_predclause126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expr_in_pair135 = new BitSet(new long[]{0x0000000000010202L});
    public static final BitSet FOLLOW_WHITESPACE_in_pair138 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_DOUBLEDOT_in_pair141 = new BitSet(new long[]{0x00001D4000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_pair143 = new BitSet(new long[]{0x00001D4000000000L});
    public static final BitSet FOLLOW_ref_in_pair146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expr_in_opclause157 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_OPERATOR_in_opclause159 = new BitSet(new long[]{0x0000BD4000020000L});
    public static final BitSet FOLLOW_expr_in_opclause161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANGLE_BRACKETS_OPEN_in_orclause170 = new BitSet(new long[]{0x0000008000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_orclause172 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_clauselist_in_orclause175 = new BitSet(new long[]{0x0000000000010500L});
    public static final BitSet FOLLOW_WHITESPACE_in_orclause177 = new BitSet(new long[]{0x0000000000000500L});
    public static final BitSet FOLLOW_OR_in_orclause180 = new BitSet(new long[]{0x0000008000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_orclause182 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_clauselist_in_orclause185 = new BitSet(new long[]{0x0000000000000500L});
    public static final BitSet FOLLOW_ANGLE_BRACKETS_CLOSE_in_orclause189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_notclause198 = new BitSet(new long[]{0x0000000000010020L});
    public static final BitSet FOLLOW_WHITESPACE_in_notclause200 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_notclause203 = new BitSet(new long[]{0x0000008000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_notclause205 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_clauselist_in_notclause208 = new BitSet(new long[]{0x0000000000010040L});
    public static final BitSet FOLLOW_WHITESPACE_in_notclause210 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_notclause213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VARIABLE_in_expr222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_expr226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_expr230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameter_in_expr234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_parameter243 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_IDENT_in_parameter245 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_PERCENT_in_parameter247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBJID_in_ref257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QNAME_in_ref261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_uriref_in_ref265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_uriref0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_value291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_usingpart_in_ruleset301 = new BitSet(new long[]{0x0000008030000000L});
    public static final BitSet FOLLOW_importpart_in_ruleset305 = new BitSet(new long[]{0x0000008030000000L});
    public static final BitSet FOLLOW_rule_in_ruleset309 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_IDENT_in_rule319 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_rule321 = new BitSet(new long[]{0x0000800000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_rule323 = new BitSet(new long[]{0x0000800000010000L});
    public static final BitSet FOLLOW_varlist_in_rule326 = new BitSet(new long[]{0x0000000000010040L});
    public static final BitSet FOLLOW_WHITESPACE_in_rule328 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_rule331 = new BitSet(new long[]{0x0000000000050000L});
    public static final BitSet FOLLOW_WHITESPACE_in_rule333 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_RULE_in_rule336 = new BitSet(new long[]{0x0000008000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_rule338 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_clauselist_in_rule341 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_WHITESPACE_in_rule343 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_DOT_in_rule346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VARIABLE_in_varlist355 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_WHITESPACE_in_varlist358 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_COMMA_in_varlist361 = new BitSet(new long[]{0x0000800000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_varlist363 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_VARIABLE_in_varlist366 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_usingpart_in_head379 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_head381 = new BitSet(new long[]{0x0000008130000002L});
    public static final BitSet FOLLOW_importpart_in_head387 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_head389 = new BitSet(new long[]{0x0000008130000002L});
    public static final BitSet FOLLOW_selectpart_in_head395 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_head397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_USING_in_usingpart408 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_usingpart410 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_IDENT_in_usingpart412 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_usingpart414 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_FOR_in_usingpart416 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_usingpart418 = new BitSet(new long[]{0x00001D4000000000L});
    public static final BitSet FOLLOW_uriref_in_usingpart420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importpart429 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_importpart431 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_URL_in_importpart433 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_importpart435 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_AS_in_importpart437 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_importpart439 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_IDENT_in_importpart441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_selectpart450 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_selectpart452 = new BitSet(new long[]{0x0000800400000000L});
    public static final BitSet FOLLOW_selectlist_in_selectpart454 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_selectpart456 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_FROM_in_selectpart458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selpart_in_selectlist467 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_WHITESPACE_in_selectlist469 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_COMMA_in_selectlist473 = new BitSet(new long[]{0x0000800400010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_selectlist475 = new BitSet(new long[]{0x0000800400000000L});
    public static final BitSet FOLLOW_selpart_in_selectlist478 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_VARIABLE_in_selpart489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggfun_in_selpart493 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_selpart495 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_VARIABLE_in_selpart497 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_selpart499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_aggfun508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderpart_in_tail518 = new BitSet(new long[]{0x000000200C010002L});
    public static final BitSet FOLLOW_WHITESPACE_in_tail520 = new BitSet(new long[]{0x000000200C000002L});
    public static final BitSet FOLLOW_limitpart_in_tail525 = new BitSet(new long[]{0x000000200C010002L});
    public static final BitSet FOLLOW_WHITESPACE_in_tail527 = new BitSet(new long[]{0x000000200C000002L});
    public static final BitSet FOLLOW_offsetpart_in_tail532 = new BitSet(new long[]{0x000000200C010002L});
    public static final BitSet FOLLOW_WHITESPACE_in_tail534 = new BitSet(new long[]{0x000000200C000002L});
    public static final BitSet FOLLOW_ORDERBY_in_orderpart546 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_orderpart548 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_orderlist_in_orderpart550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ordpart_in_orderlist559 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_WHITESPACE_in_orderlist562 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_COMMA_in_orderlist565 = new BitSet(new long[]{0x0000800000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_orderlist567 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_ordpart_in_orderlist570 = new BitSet(new long[]{0x0000000000010012L});
    public static final BitSet FOLLOW_VARIABLE_in_ordpart581 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_WHITESPACE_in_ordpart584 = new BitSet(new long[]{0x0000001800000000L});
    public static final BitSet FOLLOW_set_in_ordpart586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LIMIT_in_limitpart601 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_limitpart603 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_INTEGER_in_limitpart605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OFFSET_in_offsetpart614 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_WHITESPACE_in_offsetpart616 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_INTEGER_in_offsetpart618 = new BitSet(new long[]{0x0000000000000002L});

}