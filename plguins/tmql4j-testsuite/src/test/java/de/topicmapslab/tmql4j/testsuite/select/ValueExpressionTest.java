package de.topicmapslab.tmql4j.testsuite.select;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import org.junit.Assert;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class ValueExpressionTest extends BaseTest {

	public void testSign() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT -1 * 1  ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(-1, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(-1, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT - -1 * 1 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(1, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(1, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
	}
	
	public void testAddition() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 + 2 ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(3, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(3, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 1 + 2 + 5 + 10 ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(18, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(18, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 1 + 2 + 99 ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(102, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(102, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 1.0 + 2.0  ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(3.0, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(3.0, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT \"abc\" + \"def\" ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof String ){
			Assert.assertTrue("abcdef".equalsIgnoreCase(o.toString()));
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue("abcdef".equalsIgnoreCase(((Collection<?>) o).iterator().next().toString()));			
		}else{
			Assert.fail();
		}
	}
	
	public void testSubtraction() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 - 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(-1, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(-1, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 20 - 20 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(0, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(0, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 99 - 2 - 99 ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(196, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(196, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 3.0 - 2.0  ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(1.0, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(1.0, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}	
	
	}
	
	public void testMultiplication() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 * 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(2, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(2, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 20 * 20 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(400, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(400, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT - 20 * 20 ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigInteger ){
			Assert.assertEquals(-400, ((BigInteger) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(-400, ((BigInteger)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 3.0 * 2.0  ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(6.0, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(6.0, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}	
	}
	
	public void testDivision() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 % 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(0.5, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(0.5, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 20 % 20 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(1.0, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(1.0, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT - 20 % 20 ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(-1.0, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(-1.0, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 3.0 % 2.0  ";
		set = execute(new TMQLQuery(query));
		System.out.println();
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(1.5, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(1.5, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}	
	}
	
	public void testModulo() throws Exception {

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 mod 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(1, ((BigDecimal) o).longValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(1, ((BigDecimal)((Collection<?>) o).iterator().next()).longValue());
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT 20 mod 20 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof BigDecimal ){
			Assert.assertEquals(0, ((BigDecimal) o).doubleValue(),0);
		}else if ( o instanceof Collection<?> ){
			Assert.assertEquals(0, ((BigDecimal)((Collection<?>) o).iterator().next()).doubleValue(),0);
		}else{
			Assert.fail();
		}

	}
	
	public void testLowerThan() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 < 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT \"a\" < \"b\" ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT o:Puccini [ . / tm:name < \"a\" ]";		
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		
	}
	
	public void testLowerEquals() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 1 <= 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT \"a\" <= \"b\" ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT o:Puccini [ . / tm:name <= \"a\" ]";		
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		
	}
	
	public void testGreaterThan() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 10 > 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT \"a\" > \"B\" ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT o:Puccini [ . / tm:name > \"B\" ]";		
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		
	}
	
	public void testGreaterEquals() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		query = prefix + " SELECT 10 >= 2 ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT \"a\" >= \"B\" ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT o:Puccini [ . / tm:name >= \"B\" ]";		
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		
	}
	
	public void testMatchesRegExp() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;
		
		query = prefix + " SELECT \"aaaa\" =~ \".*a.*\" ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		Object o = set.first().first();
		if ( o instanceof Boolean ){
			Assert.assertTrue(((Boolean) o).booleanValue());
		}else if ( o instanceof Collection<?> ){
			Assert.assertTrue(((Boolean) ((Collection<?>) o).iterator().next()).booleanValue());			
		}else{
			Assert.fail();
		}
		
		query = prefix + " SELECT o:Puccini [ . / tm:name =~ \"P.*\" ]";		
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
		
	}
}
