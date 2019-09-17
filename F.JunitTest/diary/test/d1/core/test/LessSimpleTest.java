package d1.core.test;

import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;

public class LessSimpleTest extends TestCase {
    public LessSimpleTest(String name) {
        super(name);
    }


    public void testAssert() {
        System.out.println("Hello World");

        Date now = new Date();
        assertEquals("Two Dates not equal", now, now);

        boolean flag = true;
        assertTrue("flag is not true", flag);

        System.out.println("Hello JUnit");
    }

//    public void testAssignment() {
//        final Date date = new Date();
//
//        Event event = new Event(UUID.randomUUID(), date, "now", "Text");
//        assertEquals("self equality failed", event, event);
//        assertEquals("date not retained", date, event.getDate());
//        String eventinfo = event.toString();
//        System.out.println(eventinfo);
//        System.out.println("firstline");
//        assertTrue("Event.name in toString() " + eventinfo, eventinfo.contains("now"));
//        System.out.println("Hello World");
//    }

    public void testOK() {
        //
    }

}
