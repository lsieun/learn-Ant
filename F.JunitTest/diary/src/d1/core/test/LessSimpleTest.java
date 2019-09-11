package d1.core.test;

import java.util.Date;
import java.util.UUID;

import d1.core.Event;
import junit.framework.TestCase;

public class LessSimpleTest extends TestCase {
    public LessSimpleTest(String name) {
        super(name);
    }

    public void testAssignment() {
        final Date date = new Date();

        Event event = new Event(UUID.randomUUID(), date, "now", "Text");
        assertEquals("self equality failed", event, event);
        assertEquals("date not retained", date, event.getDate());
        String eventinfo = event.toString();
        assertTrue("Event.name in toString() " + eventinfo, eventinfo.contains("now"));
    }
}
