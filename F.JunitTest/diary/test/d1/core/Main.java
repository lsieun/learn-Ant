package d1.core;

import java.util.Date;
import java.util.UUID;

public class Main {
    public static void main(String args[]) throws Exception {
        Events events = new Events();
        events.add(new Event(UUID.randomUUID(), new Date(), "now", null));
        events.add(new Event(UUID.randomUUID(), new Date(System.currentTimeMillis() + 5 * 60000), "Future", "Five minutes ahead"));
        System.out.println(events);
    }
}
