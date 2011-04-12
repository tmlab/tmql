package de.topicmapslab.tmql4j.tolog.tests;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import de.topicmapslab.tmql4j.tolog.core.TologConverter;

public class Tolog2tmqlConverterTest extends TestCase {

	private final List<String> queries = new LinkedList<String>();

	@Override
	protected void setUp() throws Exception {
		queries.add("select $SCOPE from topic($TOPIC), occurrence($TOPIC, $OCC), scope($OCC, $SCOPE)? ");

		queries.add("using o for i\"http://psi.ontopedia.net/\" o:composed_by( $OPERA : o:Work, o:Puccini : o:Composer)?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" o:composed_by($OPERA  : o:Work, o:Puccini : o:Composer) order by $OPERA?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" o:composed_by($OPERA : o:Work, $COMPOSER : o:Composer), o:based_on($OPERA : o:Result, $WORK : o:Source), o:written_by($WORK : o:Work, o:Shakespeare : o:Writer) order by $COMPOSER?");

		queries.add("select $PLACE, $PERSON from i\"http://psi.ontopedia.net/born_in\"( $PERSON : i\"http://psi.ontopedia.net/Person\", $PLACE : i\"http://psi.ontopedia.net/Place\" ), i\"http://psi.ontopedia.net/died_in\"( $PERSON : i\"http://psi.ontopedia.net/Person\", $PLACE : i\"http://psi.ontopedia.net/Place\" ) order by $PLACE, $PERSON?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $PLACE, $PERSON from o:born_in( $PERSON : o:Person, $PLACE : o:Place ), o:died_in( $PERSON : o:Person, $PLACE : o:Place ) order by $PLACE, $PERSON?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $COMPOSER, count($OPERA) from o:composed_by($OPERA : o:Work, $COMPOSER : o:Composer) order by $OPERA desc?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $CITY, count($OPERA) from instance-of($CITY, o:City), { o:premiere($OPERA : o:Work, $CITY : o:Place) | o:premiere($OPERA : o:Work, $THEATRE : o:Place), o:located_in($THEATRE : o:Containee, $CITY : o:Container) } order by $OPERA desc?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $THEATRE, count($OPERA) from instance-of($THEATRE, o:Theatre), o:premiere($OPERA : o:Work, $THEATRE : o:Place) order by $OPERA desc?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $OPERA, $PREMIERE-DATE from instance-of($OPERA, o:Opera), o:premiere_date($OPERA, $PREMIERE-DATE) order by $PREMIERE-DATE desc limit 20?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" using lang for i\"http://www.topicmaps.org/xtm/1.0/language.xtm#\" select $OPERA, $ENGLISH-TITLE from instance-of($OPERA, o:Opera), topic-name($OPERA, $NAME), value($NAME, $ENGLISH-TITLE), scope($NAME, lang:en) order by $OPERA?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $WORK , $SUICIDE from o:appears_in($SUICIDE : o:Character, $WORK : o:Work), o:killed_by($SUICIDE : o:Victim, $SUICIDE : o:Perpetrator) order by $WORK?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" ext-located-in($CONTAINEE, $CONTAINER) :- { o:located_in($CONTAINEE : o:Containee, $CONTAINER : o:Container) | o:located_in($CONTAINEE : o:Containee, $MID : o:Container), ext-located-in($MID, $CONTAINER) }. select $COUNTRY, count($OPERA) from instance-of($COUNTRY, o:Country), { o:takes_place_in($OPERA : o:Opera, $COUNTRY : o:Place) | o:takes_place_in($OPERA : o:Opera, $PLACE : o:Place), ext-located-in($PLACE, $COUNTRY) } order by $OPERA desc?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $OPERA, $ARIA, count($CHARACTERS) from o:part_of($ARIA : o:Part, $OPERA : o:Whole), o:sung_by($CHARACTERS : o:Person, $ARIA : o:Aria), o:sung_by($CHARACTER2 : o:Person, $ARIA : o:Aria), $CHARACTERS /= $CHARACTER2 order by $CHARACTERS desc, $OPERA?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" inspired-by($COMPOSER, $WRITER) :- o:composed_by($OPERA : o:Work, $COMPOSER : o:Composer), o:based_on($OPERA : o:Result, $WORK : o:Source), o:written_by($WORK : o:Work, $WRITER : o:Writer). inspired-by(o:Giuseppe_Verdi, $WHO) order by $WHO?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $TOPIC, $BIBREF from { o:bibref($TOPIC, $BIBREF) | role-player($ROLE, $TOPIC), association-role($ASSOC, $ROLE), reifies($REIFIER, $ASSOC), o:bibref($REIFIER, $BIBREF) } order by $TOPIC desc, $BIBREF?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $COMPOSER, $OPERA, $RECORDING from o:audio_recording($OPERA, $RECORDING), o:composed_by($OPERA : o:Work, $COMPOSER : o:Composer) order by $COMPOSER, $OPERA, $RECORDING?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $COMPOSER, $OPERA from instance-of($OPERA, o:Opera), o:composed_by($OPERA : o:Work, $COMPOSER : o:Composer), not( o:appears_in( $CHAR : o:Character, $OPERA : o:Work )) order by $COMPOSER, $OPERA?");

		queries.add("using o for i\"http://psi.ontopedia.net/\" select $OPERA from o:appears_in($character : o:Character, $OPERA : o:Work), not(o:has_voice($character : o:Character, $voice-type : o:Voice_type)) order by $OPERA?");

		queries.add("select $TYPE, count($SCRIPT), $CATEGORY from subclass-of(script : superclass, $TYPE : subclass), direct-instance-of($SCRIPT, $TYPE), belongs-to($SCRIPT : containee, $CATEGORY : container) order by $TYPE?");

		queries.add("select $FAMILY, count($SCRIPT) from instance-of($FAMILY, script-family), belongs-to($SCRIPT : containee, $FAMILY : container) order by $SCRIPT desc?");

		queries.add("select $SCRIPT, count($LANGUAGE) from instance-of($LANGUAGE, language), written-in($LANGUAGE : language, $SCRIPT : script) order by $LANGUAGE desc?");

		queries.add("select $STD, count($PRODUCT) from TMAT_StandardsUse($STD : TMAR_UsedIn, $PRODUCT : TMAR_UsedBy) order by $PRODUCT desc?");

	}

	@Test
	public void testTologQueries() {

		for (String query : queries) {
			try {
				System.out.println("tolog: " + query);
				System.out.print("TMQL: ");
				System.out.println(TologConverter.convert(query));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}
