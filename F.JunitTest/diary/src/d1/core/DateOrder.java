package d1.core;

import org.junit.Test;

import java.util.Comparator;

public class DateOrder implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        return 0;
    }

    @Test
    public void hello() {
        System.out.println("Hello");
    }
}
