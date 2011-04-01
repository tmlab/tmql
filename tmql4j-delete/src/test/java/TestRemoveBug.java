import junit.framework.Assert;

import org.junit.Test;

import de.topicmapslab.tmql4j.delete.tests.Tmql4JTestCase;


/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */

/**
 * @author Sven Krosse
 * 
 */
public class TestRemoveBug extends Tmql4JTestCase {

	@Test
	public void testRemoveAllAssociationsBug() throws Exception {		
		fromCtm("src/test/resources/toytm.ctm");
		long cnt = topicMap.getAssociations().size();
		Assert.assertNotSame(0, cnt);
		System.out.println(cnt);
		
		execute("DELETE CASCADE http://en.wikipedia.org/wiki/Paris");
		cnt = topicMap.getAssociations().size();
		System.out.println(cnt);
		Assert.assertNotSame(0L, cnt);
	}
	
}
