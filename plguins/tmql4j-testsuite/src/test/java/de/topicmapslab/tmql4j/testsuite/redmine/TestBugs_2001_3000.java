package de.topicmapslab.tmql4j.testsuite.redmine;

import java.util.Set;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.resultprocessing.core.ctm.CTMResult;
import de.topicmapslab.tmql4j.resultprocessing.core.xml.XMLResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

public class TestBugs_2001_3000 extends RedmineBugTests {

	public void testBug2258() throws Exception {
		final String query = "return \"\"\" {tm:name} \"\"\"";
		IResultSet<?> set = execute(query);
		System.out.println("Redmine-Ticket #2258:");
		System.out.println("---------------------");
		System.out.println(set);
		System.out.println("---------------------");
		final String result = ((CTMResult) set).resultsAsMergedCTM();
		System.out.println(result);
		System.out.println("---------------------");
	}

	public void testBug2265() throws Exception {
		final String query = "%prefix wen http://en.wikipedia.org/wiki/ for $p in // wen:Republic RETURN <id>{$#}</id>";
		IResultSet<?> set = execute(query);
		System.out.println("Redmine-Ticket #2265:");
		System.out.println("---------------------");
		System.out.println(set);
		System.out.println("---------------------");

		boolean result = true;
		Set<String> results = HashUtil.getHashSet();
		results.add("<id>0</id>");
		results.add("<id>1</id>");
		results.add("<id>2</id>");
		results.add("<id>3</id>");
		results.add("<id>4</id>");
		results.add("<id>5</id>");
		for (IResult r : set) {
			result &= results.contains(r.first());
		}
		Assert.assertTrue(result);
	}

	public void testBug2267() throws Exception {
		final String query = "%prefix wen http://en.wikipedia.org/wiki/ for $p in // wen:Republic RETURN <id>{$p / tm:name }</id>";
		IResultSet<?> set = execute(query);
		System.out.println("Redmine-Ticket #2267:");
		System.out.println("---------------------");
		System.out.println(((XMLResult) set).resultsAsMergedXML());
		System.out.println("---------------------");
	}

	public void testBug2268() throws Exception {
		final String query = " tm:name ";
		IResultSet<?> set = execute(query);
		System.out.println("Redmine-Ticket #2268:");
		System.out.println("---------------------");
		System.out.println(set);
		System.out.println("---------------------");
	}

	public void testBug2383() throws Exception {
		/*
		 * empty string as name value
		 */
		String query = " %prefix wen http://en.wikipedia.org/wiki/ UPDATE names ADD \"\" WHERE wen:Germany";
		IResultSet<?> set = execute(query);
		System.out.println("Redmine-Ticket #2383:");
		System.out.println("---------------------");
		Assert.assertEquals(1, set.size());
		Assert
				.assertEquals(1, Integer.parseInt(set.first().first()
						.toString()));
		System.out.println("---------------------");
		query = " %prefix wen http://en.wikipedia.org/wiki/ wen:Germany >> characteristics tm:name >> atomify";
		set = runtime.run(query).getResults();
		Assert.assertEquals(3, set.size());
		query = " %prefix wen http://en.wikipedia.org/wiki/ wen:Germany >> characteristics tm:name [ . >> atomify == \"\" ]";
		set = runtime.run(query).getResults();
		Assert.assertEquals(1, set.size());

		/*
		 * empty string as occurrence value
		 */
		query = " %prefix wen http://en.wikipedia.org/wiki/ UPDATE occurrences ADD \"\" WHERE wen:Germany";
		set = execute(query);
		System.out.println("Redmine-Ticket #2383:");
		System.out.println("---------------------");
		Assert.assertEquals(1, set.size());
		Assert
				.assertEquals(2, Integer.parseInt(set.first().first()
						.toString()));
		System.out.println("---------------------");
		query = " %prefix wen http://en.wikipedia.org/wiki/ wen:Germany >> characteristics tm:occurrence ";
		set = runtime.run(query).getResults();
		System.out.println(set);
		Assert.assertEquals(2, set.size());

		/*
		 * empty string as IRI value
		 */
		query = " %prefix wen http://en.wikipedia.org/wiki/ UPDATE occurrences ADD \"\"^^http://www.w3.org/2001/XMLSchema#anyURI WHERE wen:Germany";
		set = execute(query);
		System.out.println("Redmine-Ticket #2383:");
		System.out.println("---------------------");
		Assert.assertEquals(1, set.size());
		Assert
				.assertEquals(2, Integer.parseInt(set.first().first()
						.toString()));
		System.out.println("---------------------");
		query = " %prefix wen http://en.wikipedia.org/wiki/ wen:Germany >> characteristics tm:occurrence >> atomify ";
		set = runtime.run(query).getResults();
		Assert.assertEquals(2, set.size());
		System.out.println(set);

	}

}
