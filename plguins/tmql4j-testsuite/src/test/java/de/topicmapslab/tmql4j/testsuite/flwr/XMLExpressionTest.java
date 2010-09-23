/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.flwr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.topicmapslab.tmql4j.resultprocessing.core.xml.XMLResult;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XMLExpressionTest extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testSimpleXTM() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		XMLResult set = null;

		query = prefix
				+ " RETURN <composers> { FOR $composer IN // o:Composer RETURN <composer> <name>{ $composer / tm:name [0] }</name> <name>{ $composer / tm:name [1] }</name> </composer> } </composers>";
		set = execute(query);

		System.out.println(set);
		System.out.println(set.getResultType());
	}

	/*
	 * dreifache verschachtlung
	 */
	public void testComplexXTM() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		XMLResult set = null;

		query = prefix
				+ " RETURN <composers> "
				+ " { "
				+ "	FOR $composer IN // o:Composer RETURN "
				+ "	<composer class=\"abc\">"
				+ "		<name> "
				+ "			{ $composer / tm:name [0] } "
				+ "		</name> "
				+ "		<operas> "
				+ "			{ "
				+ "				FOR $opera IN $composer >> traverse o:composed_by RETURN "
				+ "				<opera> " + "					{ $opera / tm:name [0] } "
				+ "				</opera> " + "			} " + "		</operas>" + "	</composer> } "
				+ "</composers>";
		set = execute(query);

		System.out.println(set);
	}

	/*
	 * dreifache verschachtlung
	 */
	public void testExtremeComplexXTM() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		XMLResult set = null;

		query = prefix
				+ " RETURN "
				+ "<composers> "
				+ " { "
				+ "	FOR $composer IN // o:Composer RETURN "
				+ "	<composer> "
				+ "		<names> "
				+ "			<name> "
				+ "				{ $composer / tm:name [0] } "
				+ "			</name>"
				+ "			<name> "
				+ "				{ $composer / tm:name [1] } "
				+ "			</name>"
				+ "			<name> "
				+ "				{ $composer / tm:name [2] } "
				+ "			</name> "
				+ "			<birth>"
				+ "				<place>"
				+ "					{ $composer >> traverse o:born_in / tm:name [0] } "
				+ "				</place> "
				+ " 			<date>"
				+ "					{ $composer >> characteristics o:date_of_birth >> atomify }"
				+ "				</date>"
				+ "			</birth>"
				+ "			<death>"
				+ "				<place>"
				+ "					{ $composer >> traverse o:died_in / tm:name [0] } "
				+ "				</place> "
				+ " 			<date>"
				+ "					{ $composer >> characteristics o:date_of_death >> atomify }"
				+ "				</date>"
				+ "			</death>"
				+ "			<webpages> "
				+ "				{ $composer / o:webpage } "
				+ "			</webpages>"
				+ "		</names> "
				+ "		<operas> "
				+ "			{ "
				+ "				FOR $opera IN $composer >> traverse o:composed_by RETURN "
				+ "				<opera> " + "					{ $opera / tm:name [0] } "
				+ "				</opera> " + "			} " + "		</operas>" + "	</composer> } "
				+ "</composers>";
		set = execute(query);

		try {
			File file = new File("opera.xml");
			FileWriter writer = new FileWriter(file);
			writer.write(set.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(query);
	}
}
