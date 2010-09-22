/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.draft2010;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathLanguageDraft2010ParserTest extends BaseTest {

	public void testPathExpressionWithScopeFilter() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* @o:short_name";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testPathExpressionWithScopeFilterInTheMiddle() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* @o:short_name / variant::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testPathExpressionWithBooleanFilter() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* @o:short_name / variant::* [ ( . / type::* = a ) AND b ] / value::* ";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testPathExpressionWithAssocPattern() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / o:composed_by( o:Composer -> o:Work )";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testSetExpressionWithUnion() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* union / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testSetExpressionWithIntersect() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* intersect / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testSetExpressionWithMinus() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name:: * minus / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testNumericalExpressionWithPlus() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / occurrence::* + / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testNumericalExpressionWithMinus() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / occurrence::* + / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testNumericalExpressionWithMultiplication() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / occurrence::* * / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testNumericalExpressionWithDivision() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / occurrence::* div / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testNumericalExpressionWithModulo() throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / occurrence::* mod / topic::o:Composer / occurrence::*";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testPathExpressionWithFunctionAsBooleanFilter()
			throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* @o:short_name / variant::* [ find ( \" abc \" , \"a\" ) = b ] / value::* ";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}

	public void testPathExpressionWithEmbedFunctionAsBooleanFilter()
			throws Exception {
		String q = "%prefix o http://psi.ontopedia.net/ / topic::o:Composer / name::* @o:short_name / variant::* [ find ( \" abc \" , substring ( \"acbcbc\" , 0 , 1 ) ) = b ] / value::* ";
		execute(new TMQLQuery(q));
		StringBuilder builder = new StringBuilder();
		runtime.getValueStore().getParserTree().toStringTree(builder);
		System.out.println(q);
		System.out.println("------------------");
		System.out.println(builder.toString());
		System.out.println("------------------");
	}
}
