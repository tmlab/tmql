/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Sven Krosse
 * 
 */
public class OldTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.topicmapslab.tmql4j.testsuite");
		// $JUnit-BEGIN$
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.AssociationPatternTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.AxisTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.FilterTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.FunctionCallTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.LiteralTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.LiteralUtilsTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.NumericalExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.PathLanguageDraft2010ParserTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.draft2010.SetExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.associationdef.TestAssociationDef.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.delete.DeleteAllTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.delete.DeleteExpressionExtendedSytleTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.delete.DeleteExpressionPathSytleTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.insert.InsertExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.merge.MergeExpressionExtendedStyleTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.merge.MergeExpressionPathStyleTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.merge.TestMergeExpressionAssociation.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.extensions.tmml.update.UpdateExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.flwr.CTMExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.flwr.ExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.flwr.XMLExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.AutoResultTypeTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.DatatypeInterpretations.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.FunctionInvocationTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.PropertyTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.ResultSetImplementationClassesTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.TestCommentLine.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.TestPragma.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.other.TokenRegistryTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.redmine.TestBugs_0_1000.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.redmine.TestBugs_1001_2000.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.redmine.TestBugs_2001_3000.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.select.SelectExpressionTest.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.select.ValueExpressionTest.class);
		suite.addTestSuite(de.topicmapslab.tmql4j.testsuite.users.Test.class);
		suite
				.addTestSuite(de.topicmapslab.tmql4j.testsuite.users.TestCaseXML.class);
		// $JUnit-END$
		return suite;
	}

}
