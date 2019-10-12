package filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractTestFilter {
    /**
     * @return IFilter
     */
    public abstract IFilter getIFilter();

    @Test
    public void testFilterNull() {
        getIFilter().filter(null, null);
    }

    @Test
    public void testFilterNullValues() {
        getIFilter().filter(null, "test");
    }

    @Test
    public void testFilterNullPrefix() {
        getIFilter().filter(new String[]{"test"}, null);
    }

    @Test
    public void testFilter() {
        String[] result = getIFilter().filter(new String[]{"test",
                "nogood", "mustbe filtered"}, "t");
        assertNotNull(result);
        assertEquals(result.length, 1);
    }

    @Test
    public void testFilterWithNullValues() {
        String[] result = getIFilter().filter(new String[]{"test",
                null, "mustbe filtered"}, "t");
        assertNotNull(result);
        assertEquals(result.length, 1);
    }
}
