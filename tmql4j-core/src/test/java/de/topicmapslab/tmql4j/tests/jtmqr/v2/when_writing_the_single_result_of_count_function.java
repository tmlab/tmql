package de.topicmapslab.tmql4j.tests.jtmqr.v2;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class when_writing_the_single_result_of_count_function extends with_JTMQR2Writer {
    private JsonNode tupleNode;

    @Before public void given_a_simple_tuple_with_numbers_as_json_node() throws IOException {
        SimpleResult tuple = new SimpleResult(new SimpleResultSet(null,null));
        tuple.add(1);
        tuple.add(Long.valueOf(Long.MAX_VALUE));
        tuple.add(Double.valueOf(Double.MAX_VALUE));
        tuple.add(-4.3);

        tupleNode = writeAndRead(tuple);
        assertNotNull(tupleNode);
    }

    @Test public void it_should_write_all_tuple_items() {
        assertTrue(tupleNode.isArray());
        assertEquals(4, tupleNode.size());
    }

    @Test public void it_should_write_tuple_items_as_numerics() {
        assertEquals(1, tupleNode.get(0).getIntValue());
        assertEquals(Long.MAX_VALUE, tupleNode.get(1).getLongValue());
        assertEquals(Double.MAX_VALUE, tupleNode.get(2).getDoubleValue());
        assertEquals(-4.3, tupleNode.get(3).getDoubleValue());
    }

}
