package d1.core.test;

import java.util.Date;
import java.util.UUID;

import d1.core.Event;
import d1.core.Events;

public class Main {
    public static void main(String args[]) throws Exception {
        Events events = new Events();
        events.add(new Event(UUID.randomUUID(), new Date(), "now", null));
        events.add(new Event(UUID.randomUUID(), new Date(System.currentTimeMillis() + 5 * 60000), "Future", "Five minutes ahead"));
        System.out.println(events);
    }
}
