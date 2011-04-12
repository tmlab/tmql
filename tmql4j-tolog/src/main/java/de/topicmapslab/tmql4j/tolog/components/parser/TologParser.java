// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g 2009-11-17 14:35:40
package de.topicmapslab.tmql4j.tolog.components.parser;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

public class TologParser extends Parser {
	public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMMA", "ROUND_BRACKETS_OPEN", "ROUND_BRACKETS_CLOSE", "ANGLE_BRACKETS_OPEN",
			"ANGLE_BRACKETS_CLOSE", "DOUBLEDOT", "OR", "OPERATOR", "QUESTIONMARK", "UNDERSCORE", "DOT", "HYPEN", "WHITESPACE", "PERCENT", "RULE", "DOLLAR", "LOWER", "UPPER", "DIGIT", "SYMBOLS",
			"ALPHANUMERIC", "NOT", "LIMIT", "ORDERBY", "USING", "IMPORT", "FOR", "AS", "SELECT", "FROM", "COUNT", "ASC", "DESC", "OFFSET", "OBJID", "IDENT", "QNAME", "URL", "INDICATOR", "ADDRESS",
			"SRCLOC", "STRING", "INTEGER", "VARIABLE", "CLAUSE" };
	public static final int DOLLAR = 19;
	public static final int LIMIT = 26;
	public static final int FOR = 30;
	public static final int COUNT = 34;
	public static final int ORDERBY = 27;
	public static final int ANGLE_BRACKETS_OPEN = 7;
	public static final int DOUBLEDOT = 9;
	public static final int NOT = 25;
	public static final int EOF = -1;
	public static final int AS = 31;
	public static final int IMPORT = 29;
	public static final int USING = 28;
	public static final int COMMA = 4;
	public static final int OFFSET = 37;
	public static final int ALPHANUMERIC = 24;
	public static final int ROUND_BRACKETS_OPEN = 5;
	public static final int QUESTIONMARK = 12;
	public static final int IDENT = 39;
	public static final int DIGIT = 22;
	public static final int CLAUSE = 48;
	public static final int DOT = 14;
	public static final int SELECT = 32;
	public static final int INTEGER = 46;
	public static final int OBJID = 38;
	public static final int RULE = 18;
	public static final int PERCENT = 17;
	public static final int HYPEN = 15;
	public static final int ASC = 35;
	public static final int OPERATOR = 11;
	public static final int INDICATOR = 42;
	public static final int WHITESPACE = 16;
	public static final int UNDERSCORE = 13;
	public static final int ROUND_BRACKETS_CLOSE = 6;
	public static final int URL = 41;
	public static final int ANGLE_BRACKETS_CLOSE = 8;
	public static final int QNAME = 40;
	public static final int VARIABLE = 47;
	public static final int ADDRESS = 43;
	public static final int OR = 10;
	public static final int SRCLOC = 44;
	public static final int LOWER = 20;
	public static final int DESC = 36;
	public static final int SYMBOLS = 23;
	public static final int FROM = 33;
	public static final int UPPER = 21;
	public static final int STRING = 45;

	// delegates
	// delegators

	public TologParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}

	public TologParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);

	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}

	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}

	@Override
	public String[] getTokenNames() {
		return TologParser.tokenNames;
	}

	@Override
	public String getGrammarFileName() {
		return "D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g";
	}

	public static class query_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "query"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:1:
	// query : ( head )? clauselist ( WHITESPACE )? ( tail )? QUESTIONMARK ;
	public final TologParser.query_return query() throws RecognitionException {
		TologParser.query_return retval = new TologParser.query_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE3 = null;
		Token QUESTIONMARK5 = null;
		TologParser.head_return head1 = null;

		TologParser.clauselist_return clauselist2 = null;

		TologParser.tail_return tail4 = null;

		Object WHITESPACE3_tree = null;
		Object QUESTIONMARK5_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:6:
			// ( ( head )? clauselist ( WHITESPACE )? ( tail )? QUESTIONMARK )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:9:
			// ( head )? clauselist ( WHITESPACE )? ( tail )? QUESTIONMARK
			{
				root_0 = adaptor.nil();

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:9:
				// ( head )?
				int alt1 = 2;
				alt1 = dfa1.predict(input);
				switch (alt1) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:9:
						// head
					{
						pushFollow(FOLLOW_head_in_query81);
						head1 = head();

						state._fsp--;

						adaptor.addChild(root_0, head1.getTree());

					}
						break;

				}

				pushFollow(FOLLOW_clauselist_in_query84);
				clauselist2 = clauselist();

				state._fsp--;

				adaptor.addChild(root_0, clauselist2.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:26:
				// ( WHITESPACE )?
				int alt2 = 2;
				int LA2_0 = input.LA(1);

				if ((LA2_0 == WHITESPACE)) {
					alt2 = 1;
				}
				switch (alt2) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:26:
						// WHITESPACE
					{
						WHITESPACE3 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_query86);
						WHITESPACE3_tree = adaptor.create(WHITESPACE3);
						adaptor.addChild(root_0, WHITESPACE3_tree);

					}
						break;

				}

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:38:
				// ( tail )?
				int alt3 = 2;
				int LA3_0 = input.LA(1);

				if (((LA3_0 >= LIMIT && LA3_0 <= ORDERBY) || LA3_0 == OFFSET)) {
					alt3 = 1;
				}
				switch (alt3) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:67:38:
						// tail
					{
						pushFollow(FOLLOW_tail_in_query89);
						tail4 = tail();

						state._fsp--;

						adaptor.addChild(root_0, tail4.getTree());

					}
						break;

				}

				QUESTIONMARK5 = (Token) match(input, QUESTIONMARK, FOLLOW_QUESTIONMARK_in_query92);
				QUESTIONMARK5_tree = adaptor.create(QUESTIONMARK5);
				adaptor.addChild(root_0, QUESTIONMARK5_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "query"

	public static class clauselist_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "clauselist"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:1:
	// clauselist : clause ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )* ;
	public final TologParser.clauselist_return clauselist() throws RecognitionException {
		TologParser.clauselist_return retval = new TologParser.clauselist_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE7 = null;
		Token COMMA8 = null;
		Token WHITESPACE9 = null;
		TologParser.clause_return clause6 = null;

		TologParser.clause_return clause10 = null;

		Object WHITESPACE7_tree = null;
		Object COMMA8_tree = null;
		Object WHITESPACE9_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:11:
			// ( clause ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )* )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:16:
			// clause ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )*
			{
				root_0 = adaptor.nil();

				pushFollow(FOLLOW_clause_in_clauselist101);
				clause6 = clause();

				state._fsp--;

				adaptor.addChild(root_0, clause6.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:23:
				// ( ( WHITESPACE )? COMMA ( WHITESPACE )? clause )*
				loop6: do {
					int alt6 = 2;
					int LA6_0 = input.LA(1);

					if ((LA6_0 == WHITESPACE)) {
						int LA6_1 = input.LA(2);

						if ((LA6_1 == COMMA)) {
							alt6 = 1;
						}

					} else if ((LA6_0 == COMMA)) {
						alt6 = 1;
					}

					switch (alt6) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:24:
							// ( WHITESPACE )? COMMA ( WHITESPACE )? clause
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:24:
							// ( WHITESPACE )?
							int alt4 = 2;
							int LA4_0 = input.LA(1);

							if ((LA4_0 == WHITESPACE)) {
								alt4 = 1;
							}
							switch (alt4) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:24:
									// WHITESPACE
								{
									WHITESPACE7 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_clauselist104);
									WHITESPACE7_tree = adaptor.create(WHITESPACE7);
									adaptor.addChild(root_0, WHITESPACE7_tree);

								}
									break;

							}

							COMMA8 = (Token) match(input, COMMA, FOLLOW_COMMA_in_clauselist107);
							COMMA8_tree = adaptor.create(COMMA8);
							adaptor.addChild(root_0, COMMA8_tree);

							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:42:
							// ( WHITESPACE )?
							int alt5 = 2;
							int LA5_0 = input.LA(1);

							if ((LA5_0 == WHITESPACE)) {
								alt5 = 1;
							}
							switch (alt5) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:68:42:
									// WHITESPACE
								{
									WHITESPACE9 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_clauselist109);
									WHITESPACE9_tree = adaptor.create(WHITESPACE9);
									adaptor.addChild(root_0, WHITESPACE9_tree);

								}
									break;

							}

							pushFollow(FOLLOW_clause_in_clauselist112);
							clause10 = clause();

							state._fsp--;

							adaptor.addChild(root_0, clause10.getTree());

						}
							break;

						default:
							break loop6;
					}
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "clauselist"

	public static class clause_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "clause"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:69:1:
	// clause : ( predclause | opclause | orclause | notclause );
	public final TologParser.clause_return clause() throws RecognitionException {
		TologParser.clause_return retval = new TologParser.clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		TologParser.predclause_return predclause11 = null;

		TologParser.opclause_return opclause12 = null;

		TologParser.orclause_return orclause13 = null;

		TologParser.notclause_return notclause14 = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:69:7:
			// ( predclause | opclause | orclause | notclause )
			int alt7 = 4;
			switch (input.LA(1)) {
				case OBJID: {
					int LA7_1 = input.LA(2);

					if ((LA7_1 == ROUND_BRACKETS_OPEN)) {
						alt7 = 1;
					} else if ((LA7_1 == WHITESPACE)) {
						alt7 = 2;
					} else {
						NoViableAltException nvae = new NoViableAltException("", 7, 1, input);

						throw nvae;
					}
				}
					break;
				case QNAME: {
					int LA7_2 = input.LA(2);

					if ((LA7_2 == WHITESPACE)) {
						alt7 = 2;
					} else if ((LA7_2 == ROUND_BRACKETS_OPEN)) {
						alt7 = 1;
					} else {
						NoViableAltException nvae = new NoViableAltException("", 7, 2, input);

						throw nvae;
					}
				}
					break;
				case INDICATOR:
				case ADDRESS:
				case SRCLOC: {
					int LA7_3 = input.LA(2);

					if ((LA7_3 == WHITESPACE)) {
						alt7 = 2;
					} else if ((LA7_3 == ROUND_BRACKETS_OPEN)) {
						alt7 = 1;
					} else {
						NoViableAltException nvae = new NoViableAltException("", 7, 3, input);

						throw nvae;
					}
				}
					break;
				case IDENT: {
					int LA7_4 = input.LA(2);

					if ((LA7_4 == WHITESPACE)) {
						alt7 = 2;
					} else if ((LA7_4 == ROUND_BRACKETS_OPEN)) {
						alt7 = 1;
					} else {
						NoViableAltException nvae = new NoViableAltException("", 7, 4, input);

						throw nvae;
					}
				}
					break;
				case PERCENT:
				case STRING:
				case VARIABLE: {
					alt7 = 2;
				}
					break;
				case ANGLE_BRACKETS_OPEN: {
					alt7 = 3;
				}
					break;
				case NOT: {
					alt7 = 4;
				}
					break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 7, 0, input);

					throw nvae;
			}

			switch (alt7) {
				case 1:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:69:12:
					// predclause
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_predclause_in_clause123);
					predclause11 = predclause();

					state._fsp--;

					adaptor.addChild(root_0, predclause11.getTree());

				}
					break;
				case 2:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:69:25:
					// opclause
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_opclause_in_clause127);
					opclause12 = opclause();

					state._fsp--;

					adaptor.addChild(root_0, opclause12.getTree());

				}
					break;
				case 3:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:69:36:
					// orclause
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_orclause_in_clause131);
					orclause13 = orclause();

					state._fsp--;

					adaptor.addChild(root_0, orclause13.getTree());

				}
					break;
				case 4:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:69:47:
					// notclause
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_notclause_in_clause135);
					notclause14 = notclause();

					state._fsp--;

					adaptor.addChild(root_0, notclause14.getTree());

				}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "clause"

	public static class predclause_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "predclause"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:1:
	// predclause : ref ROUND_BRACKETS_OPEN ( WHITESPACE )? pair ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )* (
	// WHITESPACE )? ROUND_BRACKETS_CLOSE ;
	public final TologParser.predclause_return predclause() throws RecognitionException {
		TologParser.predclause_return retval = new TologParser.predclause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ROUND_BRACKETS_OPEN16 = null;
		Token WHITESPACE17 = null;
		Token WHITESPACE19 = null;
		Token COMMA20 = null;
		Token WHITESPACE21 = null;
		Token WHITESPACE23 = null;
		Token ROUND_BRACKETS_CLOSE24 = null;
		TologParser.ref_return ref15 = null;

		TologParser.pair_return pair18 = null;

		TologParser.pair_return pair22 = null;

		Object ROUND_BRACKETS_OPEN16_tree = null;
		Object WHITESPACE17_tree = null;
		Object WHITESPACE19_tree = null;
		Object COMMA20_tree = null;
		Object WHITESPACE21_tree = null;
		Object WHITESPACE23_tree = null;
		Object ROUND_BRACKETS_CLOSE24_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:11:
			// ( ref ROUND_BRACKETS_OPEN ( WHITESPACE )? pair ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )* (
			// WHITESPACE )? ROUND_BRACKETS_CLOSE )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:16:
			// ref ROUND_BRACKETS_OPEN ( WHITESPACE )? pair ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )* ( WHITESPACE
			// )? ROUND_BRACKETS_CLOSE
			{
				root_0 = adaptor.nil();

				pushFollow(FOLLOW_ref_in_predclause144);
				ref15 = ref();

				state._fsp--;

				adaptor.addChild(root_0, ref15.getTree());
				ROUND_BRACKETS_OPEN16 = (Token) match(input, ROUND_BRACKETS_OPEN, FOLLOW_ROUND_BRACKETS_OPEN_in_predclause146);
				ROUND_BRACKETS_OPEN16_tree = adaptor.create(ROUND_BRACKETS_OPEN16);
				adaptor.addChild(root_0, ROUND_BRACKETS_OPEN16_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:40:
				// ( WHITESPACE )?
				int alt8 = 2;
				int LA8_0 = input.LA(1);

				if ((LA8_0 == WHITESPACE)) {
					alt8 = 1;
				}
				switch (alt8) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:40:
						// WHITESPACE
					{
						WHITESPACE17 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_predclause148);
						WHITESPACE17_tree = adaptor.create(WHITESPACE17);
						adaptor.addChild(root_0, WHITESPACE17_tree);

					}
						break;

				}

				pushFollow(FOLLOW_pair_in_predclause151);
				pair18 = pair();

				state._fsp--;

				adaptor.addChild(root_0, pair18.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:57:
				// ( ( WHITESPACE )? COMMA ( WHITESPACE )? pair )*
				loop11: do {
					int alt11 = 2;
					int LA11_0 = input.LA(1);

					if ((LA11_0 == WHITESPACE)) {
						int LA11_1 = input.LA(2);

						if ((LA11_1 == COMMA)) {
							alt11 = 1;
						}

					} else if ((LA11_0 == COMMA)) {
						alt11 = 1;
					}

					switch (alt11) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:58:
							// ( WHITESPACE )? COMMA ( WHITESPACE )? pair
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:58:
							// ( WHITESPACE )?
							int alt9 = 2;
							int LA9_0 = input.LA(1);

							if ((LA9_0 == WHITESPACE)) {
								alt9 = 1;
							}
							switch (alt9) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:58:
									// WHITESPACE
								{
									WHITESPACE19 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_predclause154);
									WHITESPACE19_tree = adaptor.create(WHITESPACE19);
									adaptor.addChild(root_0, WHITESPACE19_tree);

								}
									break;

							}

							COMMA20 = (Token) match(input, COMMA, FOLLOW_COMMA_in_predclause157);
							COMMA20_tree = adaptor.create(COMMA20);
							adaptor.addChild(root_0, COMMA20_tree);

							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:76:
							// ( WHITESPACE )?
							int alt10 = 2;
							int LA10_0 = input.LA(1);

							if ((LA10_0 == WHITESPACE)) {
								alt10 = 1;
							}
							switch (alt10) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:76:
									// WHITESPACE
								{
									WHITESPACE21 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_predclause159);
									WHITESPACE21_tree = adaptor.create(WHITESPACE21);
									adaptor.addChild(root_0, WHITESPACE21_tree);

								}
									break;

							}

							pushFollow(FOLLOW_pair_in_predclause162);
							pair22 = pair();

							state._fsp--;

							adaptor.addChild(root_0, pair22.getTree());

						}
							break;

						default:
							break loop11;
					}
				} while (true);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:95:
				// ( WHITESPACE )?
				int alt12 = 2;
				int LA12_0 = input.LA(1);

				if ((LA12_0 == WHITESPACE)) {
					alt12 = 1;
				}
				switch (alt12) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:70:95:
						// WHITESPACE
					{
						WHITESPACE23 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_predclause166);
						WHITESPACE23_tree = adaptor.create(WHITESPACE23);
						adaptor.addChild(root_0, WHITESPACE23_tree);

					}
						break;

				}

				ROUND_BRACKETS_CLOSE24 = (Token) match(input, ROUND_BRACKETS_CLOSE, FOLLOW_ROUND_BRACKETS_CLOSE_in_predclause169);
				ROUND_BRACKETS_CLOSE24_tree = adaptor.create(ROUND_BRACKETS_CLOSE24);
				adaptor.addChild(root_0, ROUND_BRACKETS_CLOSE24_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "predclause"

	public static class pair_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "pair"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:1:
	// pair : expr ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )? ;
	public final TologParser.pair_return pair() throws RecognitionException {
		TologParser.pair_return retval = new TologParser.pair_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE26 = null;
		Token DOUBLEDOT27 = null;
		Token WHITESPACE28 = null;
		TologParser.expr_return expr25 = null;

		TologParser.ref_return ref29 = null;

		Object WHITESPACE26_tree = null;
		Object DOUBLEDOT27_tree = null;
		Object WHITESPACE28_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:5:
			// ( expr ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )? )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:10:
			// expr ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )?
			{
				root_0 = adaptor.nil();

				pushFollow(FOLLOW_expr_in_pair178);
				expr25 = expr();

				state._fsp--;

				adaptor.addChild(root_0, expr25.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:15:
				// ( ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref )?
				int alt15 = 2;
				int LA15_0 = input.LA(1);

				if ((LA15_0 == WHITESPACE)) {
					int LA15_1 = input.LA(2);

					if ((LA15_1 == DOUBLEDOT)) {
						alt15 = 1;
					}
				} else if ((LA15_0 == DOUBLEDOT)) {
					alt15 = 1;
				}
				switch (alt15) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:16:
						// ( WHITESPACE )? DOUBLEDOT ( WHITESPACE )? ref
					{
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:16:
						// ( WHITESPACE )?
						int alt13 = 2;
						int LA13_0 = input.LA(1);

						if ((LA13_0 == WHITESPACE)) {
							alt13 = 1;
						}
						switch (alt13) {
							case 1:
								// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:16:
								// WHITESPACE
							{
								WHITESPACE26 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_pair181);
								WHITESPACE26_tree = adaptor.create(WHITESPACE26);
								adaptor.addChild(root_0, WHITESPACE26_tree);

							}
								break;

						}

						DOUBLEDOT27 = (Token) match(input, DOUBLEDOT, FOLLOW_DOUBLEDOT_in_pair184);
						DOUBLEDOT27_tree = adaptor.create(DOUBLEDOT27);
						adaptor.addChild(root_0, DOUBLEDOT27_tree);

						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:38:
						// ( WHITESPACE )?
						int alt14 = 2;
						int LA14_0 = input.LA(1);

						if ((LA14_0 == WHITESPACE)) {
							alt14 = 1;
						}
						switch (alt14) {
							case 1:
								// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:71:38:
								// WHITESPACE
							{
								WHITESPACE28 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_pair186);
								WHITESPACE28_tree = adaptor.create(WHITESPACE28);
								adaptor.addChild(root_0, WHITESPACE28_tree);

							}
								break;

						}

						pushFollow(FOLLOW_ref_in_pair189);
						ref29 = ref();

						state._fsp--;

						adaptor.addChild(root_0, ref29.getTree());

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "pair"

	public static class opclause_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "opclause"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:72:1:
	// opclause : expr WHITESPACE OPERATOR WHITESPACE expr ;
	public final TologParser.opclause_return opclause() throws RecognitionException {
		TologParser.opclause_return retval = new TologParser.opclause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE31 = null;
		Token OPERATOR32 = null;
		Token WHITESPACE33 = null;
		TologParser.expr_return expr30 = null;

		TologParser.expr_return expr34 = null;

		Object WHITESPACE31_tree = null;
		Object OPERATOR32_tree = null;
		Object WHITESPACE33_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:72:9:
			// ( expr WHITESPACE OPERATOR WHITESPACE expr )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:72:14:
			// expr WHITESPACE OPERATOR WHITESPACE expr
			{
				root_0 = adaptor.nil();

				pushFollow(FOLLOW_expr_in_opclause200);
				expr30 = expr();

				state._fsp--;

				adaptor.addChild(root_0, expr30.getTree());
				WHITESPACE31 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_opclause202);
				WHITESPACE31_tree = adaptor.create(WHITESPACE31);
				adaptor.addChild(root_0, WHITESPACE31_tree);

				OPERATOR32 = (Token) match(input, OPERATOR, FOLLOW_OPERATOR_in_opclause204);
				OPERATOR32_tree = adaptor.create(OPERATOR32);
				adaptor.addChild(root_0, OPERATOR32_tree);

				WHITESPACE33 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_opclause206);
				WHITESPACE33_tree = adaptor.create(WHITESPACE33);
				adaptor.addChild(root_0, WHITESPACE33_tree);

				pushFollow(FOLLOW_expr_in_opclause208);
				expr34 = expr();

				state._fsp--;

				adaptor.addChild(root_0, expr34.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "opclause"

	public static class orclause_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "orclause"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:1:
	// orclause : ANGLE_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ( OR ( WHITESPACE )? clauselist (
	// WHITESPACE )? )* ANGLE_BRACKETS_CLOSE ;
	public final TologParser.orclause_return orclause() throws RecognitionException {
		TologParser.orclause_return retval = new TologParser.orclause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ANGLE_BRACKETS_OPEN35 = null;
		Token WHITESPACE36 = null;
		Token WHITESPACE38 = null;
		Token OR39 = null;
		Token WHITESPACE40 = null;
		Token WHITESPACE42 = null;
		Token ANGLE_BRACKETS_CLOSE43 = null;
		TologParser.clauselist_return clauselist37 = null;

		TologParser.clauselist_return clauselist41 = null;

		Object ANGLE_BRACKETS_OPEN35_tree = null;
		Object WHITESPACE36_tree = null;
		Object WHITESPACE38_tree = null;
		Object OR39_tree = null;
		Object WHITESPACE40_tree = null;
		Object WHITESPACE42_tree = null;
		Object ANGLE_BRACKETS_CLOSE43_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:9:
			// ( ANGLE_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ( OR ( WHITESPACE )? clauselist (
			// WHITESPACE )? )* ANGLE_BRACKETS_CLOSE )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:14:
			// ANGLE_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ( OR ( WHITESPACE )? clauselist (
			// WHITESPACE )? )* ANGLE_BRACKETS_CLOSE
			{
				root_0 = adaptor.nil();

				ANGLE_BRACKETS_OPEN35 = (Token) match(input, ANGLE_BRACKETS_OPEN, FOLLOW_ANGLE_BRACKETS_OPEN_in_orclause217);
				ANGLE_BRACKETS_OPEN35_tree = adaptor.create(ANGLE_BRACKETS_OPEN35);
				adaptor.addChild(root_0, ANGLE_BRACKETS_OPEN35_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:34:
				// ( WHITESPACE )?
				int alt16 = 2;
				int LA16_0 = input.LA(1);

				if ((LA16_0 == WHITESPACE)) {
					alt16 = 1;
				}
				switch (alt16) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:34:
						// WHITESPACE
					{
						WHITESPACE36 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orclause219);
						WHITESPACE36_tree = adaptor.create(WHITESPACE36);
						adaptor.addChild(root_0, WHITESPACE36_tree);

					}
						break;

				}

				pushFollow(FOLLOW_clauselist_in_orclause222);
				clauselist37 = clauselist();

				state._fsp--;

				adaptor.addChild(root_0, clauselist37.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:57:
				// ( WHITESPACE )?
				int alt17 = 2;
				int LA17_0 = input.LA(1);

				if ((LA17_0 == WHITESPACE)) {
					alt17 = 1;
				}
				switch (alt17) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:57:
						// WHITESPACE
					{
						WHITESPACE38 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orclause224);
						WHITESPACE38_tree = adaptor.create(WHITESPACE38);
						adaptor.addChild(root_0, WHITESPACE38_tree);

					}
						break;

				}

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:68:
				// ( OR ( WHITESPACE )? clauselist ( WHITESPACE )? )*
				loop20: do {
					int alt20 = 2;
					int LA20_0 = input.LA(1);

					if ((LA20_0 == OR)) {
						alt20 = 1;
					}

					switch (alt20) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:69:
							// OR ( WHITESPACE )? clauselist ( WHITESPACE )?
						{
							OR39 = (Token) match(input, OR, FOLLOW_OR_in_orclause227);
							OR39_tree = adaptor.create(OR39);
							adaptor.addChild(root_0, OR39_tree);

							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:72:
							// ( WHITESPACE )?
							int alt18 = 2;
							int LA18_0 = input.LA(1);

							if ((LA18_0 == WHITESPACE)) {
								alt18 = 1;
							}
							switch (alt18) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:72:
									// WHITESPACE
								{
									WHITESPACE40 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orclause229);
									WHITESPACE40_tree = adaptor.create(WHITESPACE40);
									adaptor.addChild(root_0, WHITESPACE40_tree);

								}
									break;

							}

							pushFollow(FOLLOW_clauselist_in_orclause232);
							clauselist41 = clauselist();

							state._fsp--;

							adaptor.addChild(root_0, clauselist41.getTree());
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:95:
							// ( WHITESPACE )?
							int alt19 = 2;
							int LA19_0 = input.LA(1);

							if ((LA19_0 == WHITESPACE)) {
								alt19 = 1;
							}
							switch (alt19) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:73:95:
									// WHITESPACE
								{
									WHITESPACE42 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orclause234);
									WHITESPACE42_tree = adaptor.create(WHITESPACE42);
									adaptor.addChild(root_0, WHITESPACE42_tree);

								}
									break;

							}

						}
							break;

						default:
							break loop20;
					}
				} while (true);

				ANGLE_BRACKETS_CLOSE43 = (Token) match(input, ANGLE_BRACKETS_CLOSE, FOLLOW_ANGLE_BRACKETS_CLOSE_in_orclause240);
				ANGLE_BRACKETS_CLOSE43_tree = adaptor.create(ANGLE_BRACKETS_CLOSE43);
				adaptor.addChild(root_0, ANGLE_BRACKETS_CLOSE43_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "orclause"

	public static class notclause_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "notclause"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:1:
	// notclause : NOT ( WHITESPACE )? ROUND_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )?
	// ROUND_BRACKETS_CLOSE ;
	public final TologParser.notclause_return notclause() throws RecognitionException {
		TologParser.notclause_return retval = new TologParser.notclause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NOT44 = null;
		Token WHITESPACE45 = null;
		Token ROUND_BRACKETS_OPEN46 = null;
		Token WHITESPACE47 = null;
		Token WHITESPACE49 = null;
		Token ROUND_BRACKETS_CLOSE50 = null;
		TologParser.clauselist_return clauselist48 = null;

		Object NOT44_tree = null;
		Object WHITESPACE45_tree = null;
		Object ROUND_BRACKETS_OPEN46_tree = null;
		Object WHITESPACE47_tree = null;
		Object WHITESPACE49_tree = null;
		Object ROUND_BRACKETS_CLOSE50_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:10:
			// ( NOT ( WHITESPACE )? ROUND_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ROUND_BRACKETS_CLOSE
			// )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:15:
			// NOT ( WHITESPACE )? ROUND_BRACKETS_OPEN ( WHITESPACE )? clauselist ( WHITESPACE )? ROUND_BRACKETS_CLOSE
			{
				root_0 = adaptor.nil();

				NOT44 = (Token) match(input, NOT, FOLLOW_NOT_in_notclause249);
				NOT44_tree = adaptor.create(NOT44);
				adaptor.addChild(root_0, NOT44_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:19:
				// ( WHITESPACE )?
				int alt21 = 2;
				int LA21_0 = input.LA(1);

				if ((LA21_0 == WHITESPACE)) {
					alt21 = 1;
				}
				switch (alt21) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:19:
						// WHITESPACE
					{
						WHITESPACE45 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_notclause251);
						WHITESPACE45_tree = adaptor.create(WHITESPACE45);
						adaptor.addChild(root_0, WHITESPACE45_tree);

					}
						break;

				}

				ROUND_BRACKETS_OPEN46 = (Token) match(input, ROUND_BRACKETS_OPEN, FOLLOW_ROUND_BRACKETS_OPEN_in_notclause254);
				ROUND_BRACKETS_OPEN46_tree = adaptor.create(ROUND_BRACKETS_OPEN46);
				adaptor.addChild(root_0, ROUND_BRACKETS_OPEN46_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:51:
				// ( WHITESPACE )?
				int alt22 = 2;
				int LA22_0 = input.LA(1);

				if ((LA22_0 == WHITESPACE)) {
					alt22 = 1;
				}
				switch (alt22) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:51:
						// WHITESPACE
					{
						WHITESPACE47 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_notclause256);
						WHITESPACE47_tree = adaptor.create(WHITESPACE47);
						adaptor.addChild(root_0, WHITESPACE47_tree);

					}
						break;

				}

				pushFollow(FOLLOW_clauselist_in_notclause259);
				clauselist48 = clauselist();

				state._fsp--;

				adaptor.addChild(root_0, clauselist48.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:74:
				// ( WHITESPACE )?
				int alt23 = 2;
				int LA23_0 = input.LA(1);

				if ((LA23_0 == WHITESPACE)) {
					alt23 = 1;
				}
				switch (alt23) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:74:74:
						// WHITESPACE
					{
						WHITESPACE49 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_notclause261);
						WHITESPACE49_tree = adaptor.create(WHITESPACE49);
						adaptor.addChild(root_0, WHITESPACE49_tree);

					}
						break;

				}

				ROUND_BRACKETS_CLOSE50 = (Token) match(input, ROUND_BRACKETS_CLOSE, FOLLOW_ROUND_BRACKETS_CLOSE_in_notclause264);
				ROUND_BRACKETS_CLOSE50_tree = adaptor.create(ROUND_BRACKETS_CLOSE50);
				adaptor.addChild(root_0, ROUND_BRACKETS_CLOSE50_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "notclause"

	public static class expr_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "expr"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:75:1:
	// expr : ( VARIABLE | ref | value | parameter );
	public final TologParser.expr_return expr() throws RecognitionException {
		TologParser.expr_return retval = new TologParser.expr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token VARIABLE51 = null;
		TologParser.ref_return ref52 = null;

		TologParser.value_return value53 = null;

		TologParser.parameter_return parameter54 = null;

		Object VARIABLE51_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:75:5:
			// ( VARIABLE | ref | value | parameter )
			int alt24 = 4;
			switch (input.LA(1)) {
				case VARIABLE: {
					alt24 = 1;
				}
					break;
				case OBJID:
				case IDENT:
				case QNAME:
				case INDICATOR:
				case ADDRESS:
				case SRCLOC: {
					alt24 = 2;
				}
					break;
				case STRING: {
					alt24 = 3;
				}
					break;
				case PERCENT: {
					alt24 = 4;
				}
					break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 24, 0, input);

					throw nvae;
			}

			switch (alt24) {
				case 1:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:75:10:
					// VARIABLE
				{
					root_0 = adaptor.nil();

					VARIABLE51 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_expr273);
					VARIABLE51_tree = adaptor.create(VARIABLE51);
					adaptor.addChild(root_0, VARIABLE51_tree);

				}
					break;
				case 2:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:75:21:
					// ref
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_ref_in_expr277);
					ref52 = ref();

					state._fsp--;

					adaptor.addChild(root_0, ref52.getTree());

				}
					break;
				case 3:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:75:27:
					// value
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_value_in_expr281);
					value53 = value();

					state._fsp--;

					adaptor.addChild(root_0, value53.getTree());

				}
					break;
				case 4:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:75:35:
					// parameter
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_parameter_in_expr285);
					parameter54 = parameter();

					state._fsp--;

					adaptor.addChild(root_0, parameter54.getTree());

				}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "expr"

	public static class parameter_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "parameter"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:76:1:
	// parameter : PERCENT IDENT PERCENT ;
	public final TologParser.parameter_return parameter() throws RecognitionException {
		TologParser.parameter_return retval = new TologParser.parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PERCENT55 = null;
		Token IDENT56 = null;
		Token PERCENT57 = null;

		Object PERCENT55_tree = null;
		Object IDENT56_tree = null;
		Object PERCENT57_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:76:10:
			// ( PERCENT IDENT PERCENT )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:76:15:
			// PERCENT IDENT PERCENT
			{
				root_0 = adaptor.nil();

				PERCENT55 = (Token) match(input, PERCENT, FOLLOW_PERCENT_in_parameter294);
				PERCENT55_tree = adaptor.create(PERCENT55);
				adaptor.addChild(root_0, PERCENT55_tree);

				IDENT56 = (Token) match(input, IDENT, FOLLOW_IDENT_in_parameter296);
				IDENT56_tree = adaptor.create(IDENT56);
				adaptor.addChild(root_0, IDENT56_tree);

				PERCENT57 = (Token) match(input, PERCENT, FOLLOW_PERCENT_in_parameter298);
				PERCENT57_tree = adaptor.create(PERCENT57);
				adaptor.addChild(root_0, PERCENT57_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "parameter"

	public static class ref_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "ref"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:77:1:
	// ref : ( OBJID | QNAME | uriref | IDENT );
	public final TologParser.ref_return ref() throws RecognitionException {
		TologParser.ref_return retval = new TologParser.ref_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OBJID58 = null;
		Token QNAME59 = null;
		Token IDENT61 = null;
		TologParser.uriref_return uriref60 = null;

		Object OBJID58_tree = null;
		Object QNAME59_tree = null;
		Object IDENT61_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:77:4:
			// ( OBJID | QNAME | uriref | IDENT )
			int alt25 = 4;
			switch (input.LA(1)) {
				case OBJID: {
					alt25 = 1;
				}
					break;
				case QNAME: {
					alt25 = 2;
				}
					break;
				case INDICATOR:
				case ADDRESS:
				case SRCLOC: {
					alt25 = 3;
				}
					break;
				case IDENT: {
					alt25 = 4;
				}
					break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 25, 0, input);

					throw nvae;
			}

			switch (alt25) {
				case 1:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:77:10:
					// OBJID
				{
					root_0 = adaptor.nil();

					OBJID58 = (Token) match(input, OBJID, FOLLOW_OBJID_in_ref308);
					OBJID58_tree = adaptor.create(OBJID58);
					adaptor.addChild(root_0, OBJID58_tree);

				}
					break;
				case 2:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:77:18:
					// QNAME
				{
					root_0 = adaptor.nil();

					QNAME59 = (Token) match(input, QNAME, FOLLOW_QNAME_in_ref312);
					QNAME59_tree = adaptor.create(QNAME59);
					adaptor.addChild(root_0, QNAME59_tree);

				}
					break;
				case 3:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:77:26:
					// uriref
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_uriref_in_ref316);
					uriref60 = uriref();

					state._fsp--;

					adaptor.addChild(root_0, uriref60.getTree());

				}
					break;
				case 4:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:77:35:
					// IDENT
				{
					root_0 = adaptor.nil();

					IDENT61 = (Token) match(input, IDENT, FOLLOW_IDENT_in_ref320);
					IDENT61_tree = adaptor.create(IDENT61);
					adaptor.addChild(root_0, IDENT61_tree);

				}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "ref"

	public static class uriref_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "uriref"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:78:1:
	// uriref : ( INDICATOR | ADDRESS | SRCLOC );
	@SuppressWarnings("unused")
	public final TologParser.uriref_return uriref() throws RecognitionException {
		TologParser.uriref_return retval = new TologParser.uriref_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set62 = null;

		Object set62_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:78:7:
			// ( INDICATOR | ADDRESS | SRCLOC )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:
			{
				root_0 = adaptor.nil();

				set62 = input.LT(1);
				if ((input.LA(1) >= INDICATOR && input.LA(1) <= SRCLOC)) {
					input.consume();
					adaptor.addChild(root_0, adaptor.create(set62));
					state.errorRecovery = false;
				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					throw mse;
				}

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "uriref"

	public static class value_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "value"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:79:1:
	// value : STRING ;
	public final TologParser.value_return value() throws RecognitionException {
		TologParser.value_return retval = new TologParser.value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING63 = null;

		Object STRING63_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:79:6:
			// ( STRING )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:79:11:
			// STRING
			{
				root_0 = adaptor.nil();

				STRING63 = (Token) match(input, STRING, FOLLOW_STRING_in_value346);
				STRING63_tree = adaptor.create(STRING63);
				adaptor.addChild(root_0, STRING63_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "value"

	public static class ruleset_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "ruleset"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:1:
	// ruleset : ( usingpart WHITESPACE | importpart WHITESPACE | rule WHITESPACE )+ ;
	public final TologParser.ruleset_return ruleset() throws RecognitionException {
		TologParser.ruleset_return retval = new TologParser.ruleset_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE65 = null;
		Token WHITESPACE67 = null;
		Token WHITESPACE69 = null;
		TologParser.usingpart_return usingpart64 = null;

		TologParser.importpart_return importpart66 = null;

		TologParser.rule_return rule68 = null;

		Object WHITESPACE65_tree = null;
		Object WHITESPACE67_tree = null;
		Object WHITESPACE69_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:8:
			// ( ( usingpart WHITESPACE | importpart WHITESPACE | rule WHITESPACE )+ )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:13:
			// ( usingpart WHITESPACE | importpart WHITESPACE | rule WHITESPACE )+
			{
				root_0 = adaptor.nil();

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:13:
				// ( usingpart WHITESPACE | importpart WHITESPACE | rule WHITESPACE )+
				int cnt26 = 0;
				loop26: do {
					int alt26 = 4;
					switch (input.LA(1)) {
						case USING: {
							alt26 = 1;
						}
							break;
						case IMPORT: {
							alt26 = 2;
						}
							break;
						case OBJID:
						case IDENT:
						case QNAME:
						case INDICATOR:
						case ADDRESS:
						case SRCLOC: {
							alt26 = 3;
						}
							break;

					}

					switch (alt26) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:14:
							// usingpart WHITESPACE
						{
							pushFollow(FOLLOW_usingpart_in_ruleset356);
							usingpart64 = usingpart();

							state._fsp--;

							adaptor.addChild(root_0, usingpart64.getTree());
							WHITESPACE65 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_ruleset358);
							WHITESPACE65_tree = adaptor.create(WHITESPACE65);
							adaptor.addChild(root_0, WHITESPACE65_tree);

						}
							break;
						case 2:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:37:
							// importpart WHITESPACE
						{
							pushFollow(FOLLOW_importpart_in_ruleset362);
							importpart66 = importpart();

							state._fsp--;

							adaptor.addChild(root_0, importpart66.getTree());
							WHITESPACE67 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_ruleset364);
							WHITESPACE67_tree = adaptor.create(WHITESPACE67);
							adaptor.addChild(root_0, WHITESPACE67_tree);

						}
							break;
						case 3:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:80:61:
							// rule WHITESPACE
						{
							pushFollow(FOLLOW_rule_in_ruleset368);
							rule68 = rule();

							state._fsp--;

							adaptor.addChild(root_0, rule68.getTree());
							WHITESPACE69 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_ruleset370);
							WHITESPACE69_tree = adaptor.create(WHITESPACE69);
							adaptor.addChild(root_0, WHITESPACE69_tree);

						}
							break;

						default:
							if (cnt26 >= 1) {
								break loop26;
							}
							EarlyExitException eee = new EarlyExitException(26, input);
							throw eee;
					}
					cnt26++;
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "ruleset"

	public static class rule_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "rule"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:1:
	// rule : ref ROUND_BRACKETS_OPEN ( WHITESPACE )? varlist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ( WHITESPACE )? RULE
	// ( WHITESPACE )? clauselist ( WHITESPACE )? DOT ( WHITESPACE )? ;
	public final TologParser.rule_return rule() throws RecognitionException {
		TologParser.rule_return retval = new TologParser.rule_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ROUND_BRACKETS_OPEN71 = null;
		Token WHITESPACE72 = null;
		Token WHITESPACE74 = null;
		Token ROUND_BRACKETS_CLOSE75 = null;
		Token WHITESPACE76 = null;
		Token RULE77 = null;
		Token WHITESPACE78 = null;
		Token WHITESPACE80 = null;
		Token DOT81 = null;
		Token WHITESPACE82 = null;
		TologParser.ref_return ref70 = null;

		TologParser.varlist_return varlist73 = null;

		TologParser.clauselist_return clauselist79 = null;

		Object ROUND_BRACKETS_OPEN71_tree = null;
		Object WHITESPACE72_tree = null;
		Object WHITESPACE74_tree = null;
		Object ROUND_BRACKETS_CLOSE75_tree = null;
		Object WHITESPACE76_tree = null;
		Object RULE77_tree = null;
		Object WHITESPACE78_tree = null;
		Object WHITESPACE80_tree = null;
		Object DOT81_tree = null;
		Object WHITESPACE82_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:5:
			// ( ref ROUND_BRACKETS_OPEN ( WHITESPACE )? varlist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ( WHITESPACE )?
			// RULE ( WHITESPACE )? clauselist ( WHITESPACE )? DOT ( WHITESPACE )? )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:10:
			// ref ROUND_BRACKETS_OPEN ( WHITESPACE )? varlist ( WHITESPACE )? ROUND_BRACKETS_CLOSE ( WHITESPACE )? RULE
			// ( WHITESPACE )? clauselist ( WHITESPACE )? DOT ( WHITESPACE )?
			{
				root_0 = adaptor.nil();

				pushFollow(FOLLOW_ref_in_rule382);
				ref70 = ref();

				state._fsp--;

				adaptor.addChild(root_0, ref70.getTree());
				ROUND_BRACKETS_OPEN71 = (Token) match(input, ROUND_BRACKETS_OPEN, FOLLOW_ROUND_BRACKETS_OPEN_in_rule384);
				ROUND_BRACKETS_OPEN71_tree = adaptor.create(ROUND_BRACKETS_OPEN71);
				adaptor.addChild(root_0, ROUND_BRACKETS_OPEN71_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:34:
				// ( WHITESPACE )?
				int alt27 = 2;
				int LA27_0 = input.LA(1);

				if ((LA27_0 == WHITESPACE)) {
					alt27 = 1;
				}
				switch (alt27) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:34:
						// WHITESPACE
					{
						WHITESPACE72 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_rule386);
						WHITESPACE72_tree = adaptor.create(WHITESPACE72);
						adaptor.addChild(root_0, WHITESPACE72_tree);

					}
						break;

				}

				pushFollow(FOLLOW_varlist_in_rule389);
				varlist73 = varlist();

				state._fsp--;

				adaptor.addChild(root_0, varlist73.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:54:
				// ( WHITESPACE )?
				int alt28 = 2;
				int LA28_0 = input.LA(1);

				if ((LA28_0 == WHITESPACE)) {
					alt28 = 1;
				}
				switch (alt28) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:54:
						// WHITESPACE
					{
						WHITESPACE74 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_rule391);
						WHITESPACE74_tree = adaptor.create(WHITESPACE74);
						adaptor.addChild(root_0, WHITESPACE74_tree);

					}
						break;

				}

				ROUND_BRACKETS_CLOSE75 = (Token) match(input, ROUND_BRACKETS_CLOSE, FOLLOW_ROUND_BRACKETS_CLOSE_in_rule394);
				ROUND_BRACKETS_CLOSE75_tree = adaptor.create(ROUND_BRACKETS_CLOSE75);
				adaptor.addChild(root_0, ROUND_BRACKETS_CLOSE75_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:87:
				// ( WHITESPACE )?
				int alt29 = 2;
				int LA29_0 = input.LA(1);

				if ((LA29_0 == WHITESPACE)) {
					alt29 = 1;
				}
				switch (alt29) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:87:
						// WHITESPACE
					{
						WHITESPACE76 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_rule396);
						WHITESPACE76_tree = adaptor.create(WHITESPACE76);
						adaptor.addChild(root_0, WHITESPACE76_tree);

					}
						break;

				}

				RULE77 = (Token) match(input, RULE, FOLLOW_RULE_in_rule399);
				RULE77_tree = adaptor.create(RULE77);
				adaptor.addChild(root_0, RULE77_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:104:
				// ( WHITESPACE )?
				int alt30 = 2;
				int LA30_0 = input.LA(1);

				if ((LA30_0 == WHITESPACE)) {
					alt30 = 1;
				}
				switch (alt30) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:104:
						// WHITESPACE
					{
						WHITESPACE78 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_rule401);
						WHITESPACE78_tree = adaptor.create(WHITESPACE78);
						adaptor.addChild(root_0, WHITESPACE78_tree);

					}
						break;

				}

				pushFollow(FOLLOW_clauselist_in_rule404);
				clauselist79 = clauselist();

				state._fsp--;

				adaptor.addChild(root_0, clauselist79.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:127:
				// ( WHITESPACE )?
				int alt31 = 2;
				int LA31_0 = input.LA(1);

				if ((LA31_0 == WHITESPACE)) {
					alt31 = 1;
				}
				switch (alt31) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:127:
						// WHITESPACE
					{
						WHITESPACE80 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_rule406);
						WHITESPACE80_tree = adaptor.create(WHITESPACE80);
						adaptor.addChild(root_0, WHITESPACE80_tree);

					}
						break;

				}

				DOT81 = (Token) match(input, DOT, FOLLOW_DOT_in_rule409);
				DOT81_tree = adaptor.create(DOT81);
				adaptor.addChild(root_0, DOT81_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:144:
				// ( WHITESPACE )?
				int alt32 = 2;
				int LA32_0 = input.LA(1);

				if ((LA32_0 == WHITESPACE)) {
					int LA32_1 = input.LA(2);

					if ((LA32_1 == WHITESPACE)) {
						alt32 = 1;
					}
				}
				switch (alt32) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:81:144:
						// WHITESPACE
					{
						WHITESPACE82 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_rule412);
						WHITESPACE82_tree = adaptor.create(WHITESPACE82);
						adaptor.addChild(root_0, WHITESPACE82_tree);

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "rule"

	public static class varlist_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "varlist"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:1:
	// varlist : VARIABLE ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )* ;
	public final TologParser.varlist_return varlist() throws RecognitionException {
		TologParser.varlist_return retval = new TologParser.varlist_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token VARIABLE83 = null;
		Token WHITESPACE84 = null;
		Token COMMA85 = null;
		Token WHITESPACE86 = null;
		Token VARIABLE87 = null;

		Object VARIABLE83_tree = null;
		Object WHITESPACE84_tree = null;
		Object COMMA85_tree = null;
		Object WHITESPACE86_tree = null;
		Object VARIABLE87_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:8:
			// ( VARIABLE ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )* )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:13:
			// VARIABLE ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )*
			{
				root_0 = adaptor.nil();

				VARIABLE83 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_varlist422);
				VARIABLE83_tree = adaptor.create(VARIABLE83);
				adaptor.addChild(root_0, VARIABLE83_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:22:
				// ( ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE )*
				loop35: do {
					int alt35 = 2;
					int LA35_0 = input.LA(1);

					if ((LA35_0 == WHITESPACE)) {
						int LA35_1 = input.LA(2);

						if ((LA35_1 == COMMA)) {
							alt35 = 1;
						}

					} else if ((LA35_0 == COMMA)) {
						alt35 = 1;
					}

					switch (alt35) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:23:
							// ( WHITESPACE )? COMMA ( WHITESPACE )? VARIABLE
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:23:
							// ( WHITESPACE )?
							int alt33 = 2;
							int LA33_0 = input.LA(1);

							if ((LA33_0 == WHITESPACE)) {
								alt33 = 1;
							}
							switch (alt33) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:23:
									// WHITESPACE
								{
									WHITESPACE84 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_varlist425);
									WHITESPACE84_tree = adaptor.create(WHITESPACE84);
									adaptor.addChild(root_0, WHITESPACE84_tree);

								}
									break;

							}

							COMMA85 = (Token) match(input, COMMA, FOLLOW_COMMA_in_varlist428);
							COMMA85_tree = adaptor.create(COMMA85);
							adaptor.addChild(root_0, COMMA85_tree);

							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:41:
							// ( WHITESPACE )?
							int alt34 = 2;
							int LA34_0 = input.LA(1);

							if ((LA34_0 == WHITESPACE)) {
								alt34 = 1;
							}
							switch (alt34) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:82:41:
									// WHITESPACE
								{
									WHITESPACE86 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_varlist430);
									WHITESPACE86_tree = adaptor.create(WHITESPACE86);
									adaptor.addChild(root_0, WHITESPACE86_tree);

								}
									break;

							}

							VARIABLE87 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_varlist433);
							VARIABLE87_tree = adaptor.create(VARIABLE87);
							adaptor.addChild(root_0, VARIABLE87_tree);

						}
							break;

						default:
							break loop35;
					}
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "varlist"

	public static class head_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "head"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:1:
	// head : ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) | ( rule WHITESPACE ) | ( selectpart WHITESPACE )
	// )+ ;
	public final TologParser.head_return head() throws RecognitionException {
		TologParser.head_return retval = new TologParser.head_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE89 = null;
		Token WHITESPACE91 = null;
		Token WHITESPACE93 = null;
		Token WHITESPACE95 = null;
		TologParser.usingpart_return usingpart88 = null;

		TologParser.importpart_return importpart90 = null;

		TologParser.rule_return rule92 = null;

		TologParser.selectpart_return selectpart94 = null;

		Object WHITESPACE89_tree = null;
		Object WHITESPACE91_tree = null;
		Object WHITESPACE93_tree = null;
		Object WHITESPACE95_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:5:
			// ( ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) | ( rule WHITESPACE ) | ( selectpart WHITESPACE
			// ) )+ )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:10:
			// ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) | ( rule WHITESPACE ) | ( selectpart WHITESPACE )
			// )+
			{
				root_0 = adaptor.nil();

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:10:
				// ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) | ( rule WHITESPACE ) | ( selectpart
				// WHITESPACE ) )+
				int cnt36 = 0;
				loop36: do {
					int alt36 = 5;
					alt36 = dfa36.predict(input);
					switch (alt36) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:11:
							// ( usingpart WHITESPACE )
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:11:
							// ( usingpart WHITESPACE )
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:12:
							// usingpart WHITESPACE
							{
								pushFollow(FOLLOW_usingpart_in_head446);
								usingpart88 = usingpart();

								state._fsp--;

								adaptor.addChild(root_0, usingpart88.getTree());
								WHITESPACE89 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_head448);
								WHITESPACE89_tree = adaptor.create(WHITESPACE89);
								adaptor.addChild(root_0, WHITESPACE89_tree);

							}

						}
							break;
						case 2:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:36:
							// ( importpart WHITESPACE )
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:36:
							// ( importpart WHITESPACE )
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:37:
							// importpart WHITESPACE
							{
								pushFollow(FOLLOW_importpart_in_head454);
								importpart90 = importpart();

								state._fsp--;

								adaptor.addChild(root_0, importpart90.getTree());
								WHITESPACE91 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_head456);
								WHITESPACE91_tree = adaptor.create(WHITESPACE91);
								adaptor.addChild(root_0, WHITESPACE91_tree);

							}

						}
							break;
						case 3:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:62:
							// ( rule WHITESPACE )
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:62:
							// ( rule WHITESPACE )
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:64:
							// rule WHITESPACE
							{
								pushFollow(FOLLOW_rule_in_head463);
								rule92 = rule();

								state._fsp--;

								adaptor.addChild(root_0, rule92.getTree());
								WHITESPACE93 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_head465);
								WHITESPACE93_tree = adaptor.create(WHITESPACE93);
								adaptor.addChild(root_0, WHITESPACE93_tree);

							}

						}
							break;
						case 4:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:85:
							// ( selectpart WHITESPACE )
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:85:
							// ( selectpart WHITESPACE )
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:83:86:
							// selectpart WHITESPACE
							{
								pushFollow(FOLLOW_selectpart_in_head473);
								selectpart94 = selectpart();

								state._fsp--;

								adaptor.addChild(root_0, selectpart94.getTree());
								WHITESPACE95 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_head475);
								WHITESPACE95_tree = adaptor.create(WHITESPACE95);
								adaptor.addChild(root_0, WHITESPACE95_tree);

							}

						}
							break;

						default:
							if (cnt36 >= 1) {
								break loop36;
							}
							EarlyExitException eee = new EarlyExitException(36, input);
							throw eee;
					}
					cnt36++;
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "head"

	public static class usingpart_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "usingpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:84:1:
	// usingpart : USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref ;
	public final TologParser.usingpart_return usingpart() throws RecognitionException {
		TologParser.usingpart_return retval = new TologParser.usingpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token USING96 = null;
		Token WHITESPACE97 = null;
		Token IDENT98 = null;
		Token WHITESPACE99 = null;
		Token FOR100 = null;
		Token WHITESPACE101 = null;
		TologParser.uriref_return uriref102 = null;

		Object USING96_tree = null;
		Object WHITESPACE97_tree = null;
		Object IDENT98_tree = null;
		Object WHITESPACE99_tree = null;
		Object FOR100_tree = null;
		Object WHITESPACE101_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:84:10:
			// ( USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:84:15:
			// USING WHITESPACE IDENT WHITESPACE FOR WHITESPACE uriref
			{
				root_0 = adaptor.nil();

				USING96 = (Token) match(input, USING, FOLLOW_USING_in_usingpart487);
				USING96_tree = adaptor.create(USING96);
				adaptor.addChild(root_0, USING96_tree);

				WHITESPACE97 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_usingpart489);
				WHITESPACE97_tree = adaptor.create(WHITESPACE97);
				adaptor.addChild(root_0, WHITESPACE97_tree);

				IDENT98 = (Token) match(input, IDENT, FOLLOW_IDENT_in_usingpart491);
				IDENT98_tree = adaptor.create(IDENT98);
				adaptor.addChild(root_0, IDENT98_tree);

				WHITESPACE99 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_usingpart493);
				WHITESPACE99_tree = adaptor.create(WHITESPACE99);
				adaptor.addChild(root_0, WHITESPACE99_tree);

				FOR100 = (Token) match(input, FOR, FOLLOW_FOR_in_usingpart495);
				FOR100_tree = adaptor.create(FOR100);
				adaptor.addChild(root_0, FOR100_tree);

				WHITESPACE101 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_usingpart497);
				WHITESPACE101_tree = adaptor.create(WHITESPACE101);
				adaptor.addChild(root_0, WHITESPACE101_tree);

				pushFollow(FOLLOW_uriref_in_usingpart499);
				uriref102 = uriref();

				state._fsp--;

				adaptor.addChild(root_0, uriref102.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "usingpart"

	public static class importpart_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "importpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:85:1:
	// importpart : IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT ;
	public final TologParser.importpart_return importpart() throws RecognitionException {
		TologParser.importpart_return retval = new TologParser.importpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IMPORT103 = null;
		Token WHITESPACE104 = null;
		Token URL105 = null;
		Token WHITESPACE106 = null;
		Token AS107 = null;
		Token WHITESPACE108 = null;
		Token IDENT109 = null;

		Object IMPORT103_tree = null;
		Object WHITESPACE104_tree = null;
		Object URL105_tree = null;
		Object WHITESPACE106_tree = null;
		Object AS107_tree = null;
		Object WHITESPACE108_tree = null;
		Object IDENT109_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:85:11:
			// ( IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:85:16:
			// IMPORT WHITESPACE URL WHITESPACE AS WHITESPACE IDENT
			{
				root_0 = adaptor.nil();

				IMPORT103 = (Token) match(input, IMPORT, FOLLOW_IMPORT_in_importpart508);
				IMPORT103_tree = adaptor.create(IMPORT103);
				adaptor.addChild(root_0, IMPORT103_tree);

				WHITESPACE104 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_importpart510);
				WHITESPACE104_tree = adaptor.create(WHITESPACE104);
				adaptor.addChild(root_0, WHITESPACE104_tree);

				URL105 = (Token) match(input, URL, FOLLOW_URL_in_importpart512);
				URL105_tree = adaptor.create(URL105);
				adaptor.addChild(root_0, URL105_tree);

				WHITESPACE106 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_importpart514);
				WHITESPACE106_tree = adaptor.create(WHITESPACE106);
				adaptor.addChild(root_0, WHITESPACE106_tree);

				AS107 = (Token) match(input, AS, FOLLOW_AS_in_importpart516);
				AS107_tree = adaptor.create(AS107);
				adaptor.addChild(root_0, AS107_tree);

				WHITESPACE108 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_importpart518);
				WHITESPACE108_tree = adaptor.create(WHITESPACE108);
				adaptor.addChild(root_0, WHITESPACE108_tree);

				IDENT109 = (Token) match(input, IDENT, FOLLOW_IDENT_in_importpart520);
				IDENT109_tree = adaptor.create(IDENT109);
				adaptor.addChild(root_0, IDENT109_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "importpart"

	public static class selectpart_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "selectpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:86:1:
	// selectpart : SELECT WHITESPACE selectlist WHITESPACE FROM ;
	public final TologParser.selectpart_return selectpart() throws RecognitionException {
		TologParser.selectpart_return retval = new TologParser.selectpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SELECT110 = null;
		Token WHITESPACE111 = null;
		Token WHITESPACE113 = null;
		Token FROM114 = null;
		TologParser.selectlist_return selectlist112 = null;

		Object SELECT110_tree = null;
		Object WHITESPACE111_tree = null;
		Object WHITESPACE113_tree = null;
		Object FROM114_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:86:11:
			// ( SELECT WHITESPACE selectlist WHITESPACE FROM )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:86:16:
			// SELECT WHITESPACE selectlist WHITESPACE FROM
			{
				root_0 = adaptor.nil();

				SELECT110 = (Token) match(input, SELECT, FOLLOW_SELECT_in_selectpart529);
				SELECT110_tree = adaptor.create(SELECT110);
				adaptor.addChild(root_0, SELECT110_tree);

				WHITESPACE111 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_selectpart531);
				WHITESPACE111_tree = adaptor.create(WHITESPACE111);
				adaptor.addChild(root_0, WHITESPACE111_tree);

				pushFollow(FOLLOW_selectlist_in_selectpart533);
				selectlist112 = selectlist();

				state._fsp--;

				adaptor.addChild(root_0, selectlist112.getTree());
				WHITESPACE113 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_selectpart535);
				WHITESPACE113_tree = adaptor.create(WHITESPACE113);
				adaptor.addChild(root_0, WHITESPACE113_tree);

				FROM114 = (Token) match(input, FROM, FOLLOW_FROM_in_selectpart537);
				FROM114_tree = adaptor.create(FROM114);
				adaptor.addChild(root_0, FROM114_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "selectpart"

	public static class selectlist_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "selectlist"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:1:
	// selectlist : selpart ( WHITESPACE )? ( COMMA ( WHITESPACE )? selpart )* ;
	public final TologParser.selectlist_return selectlist() throws RecognitionException {
		TologParser.selectlist_return retval = new TologParser.selectlist_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE116 = null;
		Token COMMA117 = null;
		Token WHITESPACE118 = null;
		TologParser.selpart_return selpart115 = null;

		TologParser.selpart_return selpart119 = null;

		Object WHITESPACE116_tree = null;
		Object COMMA117_tree = null;
		Object WHITESPACE118_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:11:
			// ( selpart ( WHITESPACE )? ( COMMA ( WHITESPACE )? selpart )* )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:16:
			// selpart ( WHITESPACE )? ( COMMA ( WHITESPACE )? selpart )*
			{
				root_0 = adaptor.nil();

				pushFollow(FOLLOW_selpart_in_selectlist546);
				selpart115 = selpart();

				state._fsp--;

				adaptor.addChild(root_0, selpart115.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:24:
				// ( WHITESPACE )?
				int alt37 = 2;
				int LA37_0 = input.LA(1);

				if ((LA37_0 == WHITESPACE)) {
					int LA37_1 = input.LA(2);

					if ((LA37_1 == COMMA || LA37_1 == WHITESPACE)) {
						alt37 = 1;
					}
				}
				switch (alt37) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:24:
						// WHITESPACE
					{
						WHITESPACE116 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_selectlist548);
						WHITESPACE116_tree = adaptor.create(WHITESPACE116);
						adaptor.addChild(root_0, WHITESPACE116_tree);

					}
						break;

				}

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:36:
				// ( COMMA ( WHITESPACE )? selpart )*
				loop39: do {
					int alt39 = 2;
					int LA39_0 = input.LA(1);

					if ((LA39_0 == COMMA)) {
						alt39 = 1;
					}

					switch (alt39) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:37:
							// COMMA ( WHITESPACE )? selpart
						{
							COMMA117 = (Token) match(input, COMMA, FOLLOW_COMMA_in_selectlist552);
							COMMA117_tree = adaptor.create(COMMA117);
							adaptor.addChild(root_0, COMMA117_tree);

							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:43:
							// ( WHITESPACE )?
							int alt38 = 2;
							int LA38_0 = input.LA(1);

							if ((LA38_0 == WHITESPACE)) {
								alt38 = 1;
							}
							switch (alt38) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:87:43:
									// WHITESPACE
								{
									WHITESPACE118 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_selectlist554);
									WHITESPACE118_tree = adaptor.create(WHITESPACE118);
									adaptor.addChild(root_0, WHITESPACE118_tree);

								}
									break;

							}

							pushFollow(FOLLOW_selpart_in_selectlist557);
							selpart119 = selpart();

							state._fsp--;

							adaptor.addChild(root_0, selpart119.getTree());

						}
							break;

						default:
							break loop39;
					}
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "selectlist"

	public static class selpart_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "selpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:88:1:
	// selpart : ( VARIABLE | aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE );
	public final TologParser.selpart_return selpart() throws RecognitionException {
		TologParser.selpart_return retval = new TologParser.selpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token VARIABLE120 = null;
		Token ROUND_BRACKETS_OPEN122 = null;
		Token VARIABLE123 = null;
		Token ROUND_BRACKETS_CLOSE124 = null;
		TologParser.aggfun_return aggfun121 = null;

		Object VARIABLE120_tree = null;
		Object ROUND_BRACKETS_OPEN122_tree = null;
		Object VARIABLE123_tree = null;
		Object ROUND_BRACKETS_CLOSE124_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:88:8:
			// ( VARIABLE | aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE )
			int alt40 = 2;
			int LA40_0 = input.LA(1);

			if ((LA40_0 == VARIABLE)) {
				alt40 = 1;
			} else if ((LA40_0 == COUNT)) {
				alt40 = 2;
			} else {
				NoViableAltException nvae = new NoViableAltException("", 40, 0, input);

				throw nvae;
			}
			switch (alt40) {
				case 1:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:88:13:
					// VARIABLE
				{
					root_0 = adaptor.nil();

					VARIABLE120 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_selpart568);
					VARIABLE120_tree = adaptor.create(VARIABLE120);
					adaptor.addChild(root_0, VARIABLE120_tree);

				}
					break;
				case 2:
					// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:88:24:
					// aggfun ROUND_BRACKETS_OPEN VARIABLE ROUND_BRACKETS_CLOSE
				{
					root_0 = adaptor.nil();

					pushFollow(FOLLOW_aggfun_in_selpart572);
					aggfun121 = aggfun();

					state._fsp--;

					adaptor.addChild(root_0, aggfun121.getTree());
					ROUND_BRACKETS_OPEN122 = (Token) match(input, ROUND_BRACKETS_OPEN, FOLLOW_ROUND_BRACKETS_OPEN_in_selpart574);
					ROUND_BRACKETS_OPEN122_tree = adaptor.create(ROUND_BRACKETS_OPEN122);
					adaptor.addChild(root_0, ROUND_BRACKETS_OPEN122_tree);

					VARIABLE123 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_selpart576);
					VARIABLE123_tree = adaptor.create(VARIABLE123);
					adaptor.addChild(root_0, VARIABLE123_tree);

					ROUND_BRACKETS_CLOSE124 = (Token) match(input, ROUND_BRACKETS_CLOSE, FOLLOW_ROUND_BRACKETS_CLOSE_in_selpart578);
					ROUND_BRACKETS_CLOSE124_tree = adaptor.create(ROUND_BRACKETS_CLOSE124);
					adaptor.addChild(root_0, ROUND_BRACKETS_CLOSE124_tree);

				}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "selpart"

	public static class aggfun_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "aggfun"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:89:1:
	// aggfun : COUNT ;
	public final TologParser.aggfun_return aggfun() throws RecognitionException {
		TologParser.aggfun_return retval = new TologParser.aggfun_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COUNT125 = null;

		Object COUNT125_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:89:7:
			// ( COUNT )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:89:12:
			// COUNT
			{
				root_0 = adaptor.nil();

				COUNT125 = (Token) match(input, COUNT, FOLLOW_COUNT_in_aggfun587);
				COUNT125_tree = adaptor.create(COUNT125);
				adaptor.addChild(root_0, COUNT125_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "aggfun"

	public static class tail_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "tail"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:1:
	// tail : ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+ ;
	public final TologParser.tail_return tail() throws RecognitionException {
		TologParser.tail_return retval = new TologParser.tail_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE127 = null;
		Token WHITESPACE129 = null;
		Token WHITESPACE131 = null;
		TologParser.orderpart_return orderpart126 = null;

		TologParser.limitpart_return limitpart128 = null;

		TologParser.offsetpart_return offsetpart130 = null;

		Object WHITESPACE127_tree = null;
		Object WHITESPACE129_tree = null;
		Object WHITESPACE131_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:5:
			// ( ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+ )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:10:
			// ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+
			{
				root_0 = adaptor.nil();

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:10:
				// ( orderpart ( WHITESPACE )? | limitpart ( WHITESPACE )? | offsetpart ( WHITESPACE )? )+
				int cnt44 = 0;
				loop44: do {
					int alt44 = 4;
					switch (input.LA(1)) {
						case ORDERBY: {
							alt44 = 1;
						}
							break;
						case LIMIT: {
							alt44 = 2;
						}
							break;
						case OFFSET: {
							alt44 = 3;
						}
							break;

					}

					switch (alt44) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:11:
							// orderpart ( WHITESPACE )?
						{
							pushFollow(FOLLOW_orderpart_in_tail597);
							orderpart126 = orderpart();

							state._fsp--;

							adaptor.addChild(root_0, orderpart126.getTree());
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:21:
							// ( WHITESPACE )?
							int alt41 = 2;
							int LA41_0 = input.LA(1);

							if ((LA41_0 == WHITESPACE)) {
								alt41 = 1;
							}
							switch (alt41) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:21:
									// WHITESPACE
								{
									WHITESPACE127 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_tail599);
									WHITESPACE127_tree = adaptor.create(WHITESPACE127);
									adaptor.addChild(root_0, WHITESPACE127_tree);

								}
									break;

							}

						}
							break;
						case 2:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:35:
							// limitpart ( WHITESPACE )?
						{
							pushFollow(FOLLOW_limitpart_in_tail604);
							limitpart128 = limitpart();

							state._fsp--;

							adaptor.addChild(root_0, limitpart128.getTree());
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:45:
							// ( WHITESPACE )?
							int alt42 = 2;
							int LA42_0 = input.LA(1);

							if ((LA42_0 == WHITESPACE)) {
								alt42 = 1;
							}
							switch (alt42) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:45:
									// WHITESPACE
								{
									WHITESPACE129 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_tail606);
									WHITESPACE129_tree = adaptor.create(WHITESPACE129);
									adaptor.addChild(root_0, WHITESPACE129_tree);

								}
									break;

							}

						}
							break;
						case 3:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:59:
							// offsetpart ( WHITESPACE )?
						{
							pushFollow(FOLLOW_offsetpart_in_tail611);
							offsetpart130 = offsetpart();

							state._fsp--;

							adaptor.addChild(root_0, offsetpart130.getTree());
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:70:
							// ( WHITESPACE )?
							int alt43 = 2;
							int LA43_0 = input.LA(1);

							if ((LA43_0 == WHITESPACE)) {
								alt43 = 1;
							}
							switch (alt43) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:90:70:
									// WHITESPACE
								{
									WHITESPACE131 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_tail613);
									WHITESPACE131_tree = adaptor.create(WHITESPACE131);
									adaptor.addChild(root_0, WHITESPACE131_tree);

								}
									break;

							}

						}
							break;

						default:
							if (cnt44 >= 1) {
								break loop44;
							}
							EarlyExitException eee = new EarlyExitException(44, input);
							throw eee;
					}
					cnt44++;
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "tail"

	public static class orderpart_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "orderpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:91:1:
	// orderpart : ORDERBY WHITESPACE orderlist ;
	public final TologParser.orderpart_return orderpart() throws RecognitionException {
		TologParser.orderpart_return retval = new TologParser.orderpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ORDERBY132 = null;
		Token WHITESPACE133 = null;
		TologParser.orderlist_return orderlist134 = null;

		Object ORDERBY132_tree = null;
		Object WHITESPACE133_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:91:10:
			// ( ORDERBY WHITESPACE orderlist )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:91:15:
			// ORDERBY WHITESPACE orderlist
			{
				root_0 = (Object) adaptor.nil();

				ORDERBY132 = (Token) match(input, ORDERBY, FOLLOW_ORDERBY_in_orderpart625);
				ORDERBY132_tree = (Object) adaptor.create(ORDERBY132);
				adaptor.addChild(root_0, ORDERBY132_tree);

				WHITESPACE133 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orderpart627);
				WHITESPACE133_tree = (Object) adaptor.create(WHITESPACE133);
				adaptor.addChild(root_0, WHITESPACE133_tree);

				pushFollow(FOLLOW_orderlist_in_orderpart629);
				orderlist134 = orderlist();

				state._fsp--;

				adaptor.addChild(root_0, orderlist134.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "orderpart"

	public static class orderlist_return extends ParserRuleReturnScope {
		Object tree;

		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "orderlist"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:1:
	// orderlist : ordpart ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )* ;
	public final TologParser.orderlist_return orderlist() throws RecognitionException {
		TologParser.orderlist_return retval = new TologParser.orderlist_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WHITESPACE136 = null;
		Token COMMA137 = null;
		Token WHITESPACE138 = null;
		TologParser.ordpart_return ordpart135 = null;

		TologParser.ordpart_return ordpart139 = null;

		Object WHITESPACE136_tree = null;
		Object COMMA137_tree = null;
		Object WHITESPACE138_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:10:
			// ( ordpart ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )* )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:15:
			// ordpart ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )*
			{
				root_0 = (Object) adaptor.nil();

				pushFollow(FOLLOW_ordpart_in_orderlist638);
				ordpart135 = ordpart();

				state._fsp--;

				adaptor.addChild(root_0, ordpart135.getTree());
				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:23:
				// ( ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart )*
				loop47: do {
					int alt47 = 2;
					int LA47_0 = input.LA(1);

					if ((LA47_0 == WHITESPACE)) {
						int LA47_1 = input.LA(2);

						if ((LA47_1 == COMMA)) {
							alt47 = 1;
						}

					} else if ((LA47_0 == COMMA)) {
						alt47 = 1;
					}

					switch (alt47) {
						case 1:
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:24:
							// ( WHITESPACE )? COMMA ( WHITESPACE )? ordpart
						{
							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:24:
							// ( WHITESPACE )?
							int alt45 = 2;
							int LA45_0 = input.LA(1);

							if ((LA45_0 == WHITESPACE)) {
								alt45 = 1;
							}
							switch (alt45) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:24:
									// WHITESPACE
								{
									WHITESPACE136 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orderlist641);
									WHITESPACE136_tree = (Object) adaptor.create(WHITESPACE136);
									adaptor.addChild(root_0, WHITESPACE136_tree);

								}
									break;

							}

							COMMA137 = (Token) match(input, COMMA, FOLLOW_COMMA_in_orderlist644);
							COMMA137_tree = (Object) adaptor.create(COMMA137);
							adaptor.addChild(root_0, COMMA137_tree);

							// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:42:
							// ( WHITESPACE )?
							int alt46 = 2;
							int LA46_0 = input.LA(1);

							if ((LA46_0 == WHITESPACE)) {
								alt46 = 1;
							}
							switch (alt46) {
								case 1:
									// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:92:42:
									// WHITESPACE
								{
									WHITESPACE138 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_orderlist646);
									WHITESPACE138_tree = (Object) adaptor.create(WHITESPACE138);
									adaptor.addChild(root_0, WHITESPACE138_tree);

								}
									break;

							}

							pushFollow(FOLLOW_ordpart_in_orderlist649);
							ordpart139 = ordpart();

							state._fsp--;

							adaptor.addChild(root_0, ordpart139.getTree());

						}
							break;

						default:
							break loop47;
					}
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "orderlist"

	public static class ordpart_return extends ParserRuleReturnScope {
		Object tree;

		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "ordpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:93:1:
	// ordpart : VARIABLE ( WHITESPACE ( ASC | DESC ) )? ;
	@SuppressWarnings("unused")
	public final TologParser.ordpart_return ordpart() throws RecognitionException {
		TologParser.ordpart_return retval = new TologParser.ordpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token VARIABLE140 = null;
		Token WHITESPACE141 = null;
		Token set142 = null;

		Object VARIABLE140_tree = null;
		Object WHITESPACE141_tree = null;
		Object set142_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:93:8:
			// ( VARIABLE ( WHITESPACE ( ASC | DESC ) )? )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:93:13:
			// VARIABLE ( WHITESPACE ( ASC | DESC ) )?
			{
				root_0 = (Object) adaptor.nil();

				VARIABLE140 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_ordpart660);
				VARIABLE140_tree = (Object) adaptor.create(VARIABLE140);
				adaptor.addChild(root_0, VARIABLE140_tree);

				// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:93:22:
				// ( WHITESPACE ( ASC | DESC ) )?
				int alt48 = 2;
				int LA48_0 = input.LA(1);

				if ((LA48_0 == WHITESPACE)) {
					int LA48_1 = input.LA(2);

					if (((LA48_1 >= ASC && LA48_1 <= DESC))) {
						alt48 = 1;
					}
				}
				switch (alt48) {
					case 1:
						// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:93:23:
						// WHITESPACE ( ASC | DESC )
					{
						WHITESPACE141 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_ordpart663);
						WHITESPACE141_tree = (Object) adaptor.create(WHITESPACE141);
						adaptor.addChild(root_0, WHITESPACE141_tree);

						set142 = (Token) input.LT(1);
						if ((input.LA(1) >= ASC && input.LA(1) <= DESC)) {
							input.consume();
							adaptor.addChild(root_0, (Object) adaptor.create(set142));
							state.errorRecovery = false;
						} else {
							MismatchedSetException mse = new MismatchedSetException(null, input);
							throw mse;
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "ordpart"

	public static class limitpart_return extends ParserRuleReturnScope {
		Object tree;

		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "limitpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:94:1:
	// limitpart : LIMIT WHITESPACE INTEGER ;
	public final TologParser.limitpart_return limitpart() throws RecognitionException {
		TologParser.limitpart_return retval = new TologParser.limitpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LIMIT143 = null;
		Token WHITESPACE144 = null;
		Token INTEGER145 = null;

		Object LIMIT143_tree = null;
		Object WHITESPACE144_tree = null;
		Object INTEGER145_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:94:10:
			// ( LIMIT WHITESPACE INTEGER )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:94:15:
			// LIMIT WHITESPACE INTEGER
			{
				root_0 = (Object) adaptor.nil();

				LIMIT143 = (Token) match(input, LIMIT, FOLLOW_LIMIT_in_limitpart680);
				LIMIT143_tree = (Object) adaptor.create(LIMIT143);
				adaptor.addChild(root_0, LIMIT143_tree);

				WHITESPACE144 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_limitpart682);
				WHITESPACE144_tree = (Object) adaptor.create(WHITESPACE144);
				adaptor.addChild(root_0, WHITESPACE144_tree);

				INTEGER145 = (Token) match(input, INTEGER, FOLLOW_INTEGER_in_limitpart684);
				INTEGER145_tree = (Object) adaptor.create(INTEGER145);
				adaptor.addChild(root_0, INTEGER145_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "limitpart"

	public static class offsetpart_return extends ParserRuleReturnScope {
		Object tree;

		public Object getTree() {
			return tree;
		}
	};

	// $ANTLR start "offsetpart"
	// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:95:1:
	// offsetpart : OFFSET WHITESPACE INTEGER ;
	public final TologParser.offsetpart_return offsetpart() throws RecognitionException {
		TologParser.offsetpart_return retval = new TologParser.offsetpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OFFSET146 = null;
		Token WHITESPACE147 = null;
		Token INTEGER148 = null;

		Object OFFSET146_tree = null;
		Object WHITESPACE147_tree = null;
		Object INTEGER148_tree = null;

		try {
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:95:11:
			// ( OFFSET WHITESPACE INTEGER )
			// D:\\Krosse\\TMQL4J_Ex\\tolog2tmql\\src\\de\\topicmapslab\\tmql4j\\converter\\tolog\\parser\\TologParser.g:95:16:
			// OFFSET WHITESPACE INTEGER
			{
				root_0 = (Object) adaptor.nil();

				OFFSET146 = (Token) match(input, OFFSET, FOLLOW_OFFSET_in_offsetpart693);
				OFFSET146_tree = (Object) adaptor.create(OFFSET146);
				adaptor.addChild(root_0, OFFSET146_tree);

				WHITESPACE147 = (Token) match(input, WHITESPACE, FOLLOW_WHITESPACE_in_offsetpart695);
				WHITESPACE147_tree = (Object) adaptor.create(WHITESPACE147);
				adaptor.addChild(root_0, WHITESPACE147_tree);

				INTEGER148 = (Token) match(input, INTEGER, FOLLOW_INTEGER_in_offsetpart697);
				INTEGER148_tree = (Object) adaptor.create(INTEGER148);
				adaptor.addChild(root_0, INTEGER148_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "offsetpart"

	// Delegated rules

	protected DFA1 dfa1 = new DFA1(this);
	protected DFA36 dfa36 = new DFA36(this);
	static final String DFA1_eotS = "\21\uffff";
	static final String DFA1_eofS = "\21\uffff";
	static final String DFA1_minS = "\1\7\1\uffff\4\5\1\uffff\1\20\1\21\2\4\1\20\1\4\1\21\3\4";
	static final String DFA1_maxS = "\1\57\1\uffff\4\20\1\uffff\2\57\1\20\1\11\1\57\1\45\1\57\1\20\1" + "\45\1\11";
	static final String DFA1_acceptS = "\1\uffff\1\1\4\uffff\1\2\12\uffff";
	static final String DFA1_specialS = "\21\uffff}>";
	static final String[] DFA1_transitionS = { "\1\6\11\uffff\1\6\7\uffff\1\6\2\uffff\2\1\2\uffff\1\1\5\uffff" + "\1\2\1\5\1\3\1\uffff\3\4\1\6\1\uffff\1\6", "", "\1\7\12\uffff\1\6",
			"\1\7\12\uffff\1\6", "\1\7\12\uffff\1\6", "\1\7\12\uffff\1\6", "", "\1\10\1\6\24\uffff\3\6\1\uffff\4\6\1\uffff\1\11", "\1\6\24\uffff\3\6\1\uffff\4\6\1\uffff\1\11",
			"\1\13\1\uffff\1\14\2\uffff\1\6\6\uffff\1\12", "\1\13\1\uffff\1\14\2\uffff\1\6", "\1\15\1\6\24\uffff\3\6\1\uffff\4\6\1\uffff\1\16",
			"\1\6\7\uffff\1\6\3\uffff\1\17\1\uffff\1\1\7\uffff\2\6\11\uffff" + "\1\6", "\1\6\24\uffff\3\6\1\uffff\4\6\1\uffff\1\16", "\1\13\1\uffff\1\14\2\uffff\1\6\6\uffff\1\20",
			"\1\6\7\uffff\1\6\5\uffff\1\1\7\uffff\2\6\11\uffff\1\6", "\1\13\1\uffff\1\14\2\uffff\1\6" };

	static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
	static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
	static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
	static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
	static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
	static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
	static final short[][] DFA1_transition;

	static {
		int numStates = DFA1_transitionS.length;
		DFA1_transition = new short[numStates][];
		for (int i = 0; i < numStates; i++) {
			DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
		}
	}

	class DFA1 extends DFA {

		public DFA1(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 1;
			this.eot = DFA1_eot;
			this.eof = DFA1_eof;
			this.min = DFA1_min;
			this.max = DFA1_max;
			this.accept = DFA1_accept;
			this.special = DFA1_special;
			this.transition = DFA1_transition;
		}

		public String getDescription() {
			return "67:9: ( head )?";
		}
	}

	static final String DFA36_eotS = "\24\uffff";
	static final String DFA36_eofS = "\24\uffff";
	static final String DFA36_minS = "\1\7\4\5\4\uffff\1\20\1\21\2\4\1\20\1\4\1\21\2\4\1\uffff\1\4";
	static final String DFA36_maxS = "\1\57\4\20\4\uffff\2\57\1\20\1\11\1\57\1\45\1\57\1\20\1\45\1\uffff" + "\1\11";
	static final String DFA36_acceptS = "\5\uffff\1\5\1\1\1\2\1\4\11\uffff\1\3\1\uffff";
	static final String DFA36_specialS = "\24\uffff}>";
	static final String[] DFA36_transitionS = { "\1\5\11\uffff\1\5\7\uffff\1\5\2\uffff\1\6\1\7\2\uffff\1\10" + "\5\uffff\1\1\1\4\1\2\1\uffff\3\3\1\5\1\uffff\1\5", "\1\11\12\uffff\1\5",
			"\1\11\12\uffff\1\5", "\1\11\12\uffff\1\5", "\1\11\12\uffff\1\5", "", "", "", "", "\1\12\1\5\24\uffff\3\5\1\uffff\4\5\1\uffff\1\13", "\1\5\24\uffff\3\5\1\uffff\4\5\1\uffff\1\13",
			"\1\15\1\uffff\1\16\2\uffff\1\5\6\uffff\1\14", "\1\15\1\uffff\1\16\2\uffff\1\5", "\1\17\1\5\24\uffff\3\5\1\uffff\4\5\1\uffff\1\20",
			"\1\5\7\uffff\1\5\3\uffff\1\21\1\uffff\1\22\7\uffff\2\5\11" + "\uffff\1\5", "\1\5\24\uffff\3\5\1\uffff\4\5\1\uffff\1\20", "\1\15\1\uffff\1\16\2\uffff\1\5\6\uffff\1\23",
			"\1\5\7\uffff\1\5\5\uffff\1\22\7\uffff\2\5\11\uffff\1\5", "", "\1\15\1\uffff\1\16\2\uffff\1\5" };

	static final short[] DFA36_eot = DFA.unpackEncodedString(DFA36_eotS);
	static final short[] DFA36_eof = DFA.unpackEncodedString(DFA36_eofS);
	static final char[] DFA36_min = DFA.unpackEncodedStringToUnsignedChars(DFA36_minS);
	static final char[] DFA36_max = DFA.unpackEncodedStringToUnsignedChars(DFA36_maxS);
	static final short[] DFA36_accept = DFA.unpackEncodedString(DFA36_acceptS);
	static final short[] DFA36_special = DFA.unpackEncodedString(DFA36_specialS);
	static final short[][] DFA36_transition;

	static {
		int numStates = DFA36_transitionS.length;
		DFA36_transition = new short[numStates][];
		for (int i = 0; i < numStates; i++) {
			DFA36_transition[i] = DFA.unpackEncodedString(DFA36_transitionS[i]);
		}
	}

	class DFA36 extends DFA {

		public DFA36(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 36;
			this.eot = DFA36_eot;
			this.eof = DFA36_eof;
			this.min = DFA36_min;
			this.max = DFA36_max;
			this.accept = DFA36_accept;
			this.special = DFA36_special;
			this.transition = DFA36_transition;
		}

		public String getDescription() {
			return "()+ loopback of 83:10: ( ( usingpart WHITESPACE ) | ( importpart WHITESPACE ) | ( rule WHITESPACE ) | ( selectpart WHITESPACE ) )+";
		}
	}

	public static final BitSet FOLLOW_head_in_query81 = new BitSet(new long[] { 0x0000BDC002020080L });
	public static final BitSet FOLLOW_clauselist_in_query84 = new BitSet(new long[] { 0x000000200C011000L });
	public static final BitSet FOLLOW_WHITESPACE_in_query86 = new BitSet(new long[] { 0x000000200C001000L });
	public static final BitSet FOLLOW_tail_in_query89 = new BitSet(new long[] { 0x0000000000001000L });
	public static final BitSet FOLLOW_QUESTIONMARK_in_query92 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_clause_in_clauselist101 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_WHITESPACE_in_clauselist104 = new BitSet(new long[] { 0x0000000000000010L });
	public static final BitSet FOLLOW_COMMA_in_clauselist107 = new BitSet(new long[] { 0x0000BDC002030080L });
	public static final BitSet FOLLOW_WHITESPACE_in_clauselist109 = new BitSet(new long[] { 0x0000BDC002020080L });
	public static final BitSet FOLLOW_clause_in_clauselist112 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_predclause_in_clause123 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_opclause_in_clause127 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_orclause_in_clause131 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_notclause_in_clause135 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ref_in_predclause144 = new BitSet(new long[] { 0x0000000000000020L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_predclause146 = new BitSet(new long[] { 0x0000BDC000030000L });
	public static final BitSet FOLLOW_WHITESPACE_in_predclause148 = new BitSet(new long[] { 0x0000BDC000030000L });
	public static final BitSet FOLLOW_pair_in_predclause151 = new BitSet(new long[] { 0x0000000000010050L });
	public static final BitSet FOLLOW_WHITESPACE_in_predclause154 = new BitSet(new long[] { 0x0000000000000010L });
	public static final BitSet FOLLOW_COMMA_in_predclause157 = new BitSet(new long[] { 0x0000BDC000030000L });
	public static final BitSet FOLLOW_WHITESPACE_in_predclause159 = new BitSet(new long[] { 0x0000BDC000030000L });
	public static final BitSet FOLLOW_pair_in_predclause162 = new BitSet(new long[] { 0x0000000000010050L });
	public static final BitSet FOLLOW_WHITESPACE_in_predclause166 = new BitSet(new long[] { 0x0000000000000040L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_predclause169 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expr_in_pair178 = new BitSet(new long[] { 0x0000000000010202L });
	public static final BitSet FOLLOW_WHITESPACE_in_pair181 = new BitSet(new long[] { 0x0000000000000200L });
	public static final BitSet FOLLOW_DOUBLEDOT_in_pair184 = new BitSet(new long[] { 0x00001DC000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_pair186 = new BitSet(new long[] { 0x00001DC000000000L });
	public static final BitSet FOLLOW_ref_in_pair189 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expr_in_opclause200 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_opclause202 = new BitSet(new long[] { 0x0000000000000800L });
	public static final BitSet FOLLOW_OPERATOR_in_opclause204 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_opclause206 = new BitSet(new long[] { 0x0000BDC000020000L });
	public static final BitSet FOLLOW_expr_in_opclause208 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ANGLE_BRACKETS_OPEN_in_orclause217 = new BitSet(new long[] { 0x0000BDC002030080L });
	public static final BitSet FOLLOW_WHITESPACE_in_orclause219 = new BitSet(new long[] { 0x0000BDC002020080L });
	public static final BitSet FOLLOW_clauselist_in_orclause222 = new BitSet(new long[] { 0x0000000000010500L });
	public static final BitSet FOLLOW_WHITESPACE_in_orclause224 = new BitSet(new long[] { 0x0000000000000500L });
	public static final BitSet FOLLOW_OR_in_orclause227 = new BitSet(new long[] { 0x0000BDC002030080L });
	public static final BitSet FOLLOW_WHITESPACE_in_orclause229 = new BitSet(new long[] { 0x0000BDC002020080L });
	public static final BitSet FOLLOW_clauselist_in_orclause232 = new BitSet(new long[] { 0x0000000000010500L });
	public static final BitSet FOLLOW_WHITESPACE_in_orclause234 = new BitSet(new long[] { 0x0000000000000500L });
	public static final BitSet FOLLOW_ANGLE_BRACKETS_CLOSE_in_orclause240 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NOT_in_notclause249 = new BitSet(new long[] { 0x0000000000010020L });
	public static final BitSet FOLLOW_WHITESPACE_in_notclause251 = new BitSet(new long[] { 0x0000000000000020L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_notclause254 = new BitSet(new long[] { 0x0000BDC002030080L });
	public static final BitSet FOLLOW_WHITESPACE_in_notclause256 = new BitSet(new long[] { 0x0000BDC002020080L });
	public static final BitSet FOLLOW_clauselist_in_notclause259 = new BitSet(new long[] { 0x0000000000010040L });
	public static final BitSet FOLLOW_WHITESPACE_in_notclause261 = new BitSet(new long[] { 0x0000000000000040L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_notclause264 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_VARIABLE_in_expr273 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ref_in_expr277 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_value_in_expr281 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_parameter_in_expr285 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_PERCENT_in_parameter294 = new BitSet(new long[] { 0x0000008000000000L });
	public static final BitSet FOLLOW_IDENT_in_parameter296 = new BitSet(new long[] { 0x0000000000020000L });
	public static final BitSet FOLLOW_PERCENT_in_parameter298 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_OBJID_in_ref308 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_QNAME_in_ref312 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_uriref_in_ref316 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENT_in_ref320 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_set_in_uriref0 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_STRING_in_value346 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_usingpart_in_ruleset356 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_ruleset358 = new BitSet(new long[] { 0x00001DC030000002L });
	public static final BitSet FOLLOW_importpart_in_ruleset362 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_ruleset364 = new BitSet(new long[] { 0x00001DC030010002L });
	public static final BitSet FOLLOW_rule_in_ruleset368 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_ruleset370 = new BitSet(new long[] { 0x00001DC030010002L });
	public static final BitSet FOLLOW_ref_in_rule382 = new BitSet(new long[] { 0x0000000000000020L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_rule384 = new BitSet(new long[] { 0x0000800000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_rule386 = new BitSet(new long[] { 0x0000800000010000L });
	public static final BitSet FOLLOW_varlist_in_rule389 = new BitSet(new long[] { 0x0000000000010040L });
	public static final BitSet FOLLOW_WHITESPACE_in_rule391 = new BitSet(new long[] { 0x0000000000000040L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_rule394 = new BitSet(new long[] { 0x0000000000050000L });
	public static final BitSet FOLLOW_WHITESPACE_in_rule396 = new BitSet(new long[] { 0x0000000000040000L });
	public static final BitSet FOLLOW_RULE_in_rule399 = new BitSet(new long[] { 0x0000BDC002030080L });
	public static final BitSet FOLLOW_WHITESPACE_in_rule401 = new BitSet(new long[] { 0x0000BDC002020080L });
	public static final BitSet FOLLOW_clauselist_in_rule404 = new BitSet(new long[] { 0x0000000000014000L });
	public static final BitSet FOLLOW_WHITESPACE_in_rule406 = new BitSet(new long[] { 0x0000000000004000L });
	public static final BitSet FOLLOW_DOT_in_rule409 = new BitSet(new long[] { 0x0000000000010002L });
	public static final BitSet FOLLOW_WHITESPACE_in_rule412 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_VARIABLE_in_varlist422 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_WHITESPACE_in_varlist425 = new BitSet(new long[] { 0x0000000000000010L });
	public static final BitSet FOLLOW_COMMA_in_varlist428 = new BitSet(new long[] { 0x0000800000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_varlist430 = new BitSet(new long[] { 0x0000800000000000L });
	public static final BitSet FOLLOW_VARIABLE_in_varlist433 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_usingpart_in_head446 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_head448 = new BitSet(new long[] { 0x00001DC130010002L });
	public static final BitSet FOLLOW_importpart_in_head454 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_head456 = new BitSet(new long[] { 0x00001DC130010002L });
	public static final BitSet FOLLOW_rule_in_head463 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_head465 = new BitSet(new long[] { 0x00001DC130010002L });
	public static final BitSet FOLLOW_selectpart_in_head473 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_head475 = new BitSet(new long[] { 0x00001DC130010002L });
	public static final BitSet FOLLOW_USING_in_usingpart487 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_usingpart489 = new BitSet(new long[] { 0x0000008000000000L });
	public static final BitSet FOLLOW_IDENT_in_usingpart491 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_usingpart493 = new BitSet(new long[] { 0x0000000040000000L });
	public static final BitSet FOLLOW_FOR_in_usingpart495 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_usingpart497 = new BitSet(new long[] { 0x00001C0000000000L });
	public static final BitSet FOLLOW_uriref_in_usingpart499 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IMPORT_in_importpart508 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_importpart510 = new BitSet(new long[] { 0x0000020000000000L });
	public static final BitSet FOLLOW_URL_in_importpart512 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_importpart514 = new BitSet(new long[] { 0x0000000080000000L });
	public static final BitSet FOLLOW_AS_in_importpart516 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_importpart518 = new BitSet(new long[] { 0x0000008000000000L });
	public static final BitSet FOLLOW_IDENT_in_importpart520 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SELECT_in_selectpart529 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_selectpart531 = new BitSet(new long[] { 0x0000800400000000L });
	public static final BitSet FOLLOW_selectlist_in_selectpart533 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_selectpart535 = new BitSet(new long[] { 0x0000000200000000L });
	public static final BitSet FOLLOW_FROM_in_selectpart537 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_selpart_in_selectlist546 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_WHITESPACE_in_selectlist548 = new BitSet(new long[] { 0x0000000000000012L });
	public static final BitSet FOLLOW_COMMA_in_selectlist552 = new BitSet(new long[] { 0x0000800400010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_selectlist554 = new BitSet(new long[] { 0x0000800400000000L });
	public static final BitSet FOLLOW_selpart_in_selectlist557 = new BitSet(new long[] { 0x0000000000000012L });
	public static final BitSet FOLLOW_VARIABLE_in_selpart568 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_aggfun_in_selpart572 = new BitSet(new long[] { 0x0000000000000020L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_OPEN_in_selpart574 = new BitSet(new long[] { 0x0000800000000000L });
	public static final BitSet FOLLOW_VARIABLE_in_selpart576 = new BitSet(new long[] { 0x0000000000000040L });
	public static final BitSet FOLLOW_ROUND_BRACKETS_CLOSE_in_selpart578 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_COUNT_in_aggfun587 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_orderpart_in_tail597 = new BitSet(new long[] { 0x000000200C010002L });
	public static final BitSet FOLLOW_WHITESPACE_in_tail599 = new BitSet(new long[] { 0x000000200C000002L });
	public static final BitSet FOLLOW_limitpart_in_tail604 = new BitSet(new long[] { 0x000000200C010002L });
	public static final BitSet FOLLOW_WHITESPACE_in_tail606 = new BitSet(new long[] { 0x000000200C000002L });
	public static final BitSet FOLLOW_offsetpart_in_tail611 = new BitSet(new long[] { 0x000000200C010002L });
	public static final BitSet FOLLOW_WHITESPACE_in_tail613 = new BitSet(new long[] { 0x000000200C000002L });
	public static final BitSet FOLLOW_ORDERBY_in_orderpart625 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_orderpart627 = new BitSet(new long[] { 0x0000800000000000L });
	public static final BitSet FOLLOW_orderlist_in_orderpart629 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ordpart_in_orderlist638 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_WHITESPACE_in_orderlist641 = new BitSet(new long[] { 0x0000000000000010L });
	public static final BitSet FOLLOW_COMMA_in_orderlist644 = new BitSet(new long[] { 0x0000800000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_orderlist646 = new BitSet(new long[] { 0x0000800000000000L });
	public static final BitSet FOLLOW_ordpart_in_orderlist649 = new BitSet(new long[] { 0x0000000000010012L });
	public static final BitSet FOLLOW_VARIABLE_in_ordpart660 = new BitSet(new long[] { 0x0000000000010002L });
	public static final BitSet FOLLOW_WHITESPACE_in_ordpart663 = new BitSet(new long[] { 0x0000001800000000L });
	public static final BitSet FOLLOW_set_in_ordpart665 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LIMIT_in_limitpart680 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_limitpart682 = new BitSet(new long[] { 0x0000400000000000L });
	public static final BitSet FOLLOW_INTEGER_in_limitpart684 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_OFFSET_in_offsetpart693 = new BitSet(new long[] { 0x0000000000010000L });
	public static final BitSet FOLLOW_WHITESPACE_in_offsetpart695 = new BitSet(new long[] { 0x0000400000000000L });
	public static final BitSet FOLLOW_INTEGER_in_offsetpart697 = new BitSet(new long[] { 0x0000000000000002L });

}