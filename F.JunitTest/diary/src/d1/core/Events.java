package d1.core;

import java.io.Serializable;
import java.util.*;

public class Events implements Serializable, Iterable<Event> {
    private Map<UUID, Event> map = new HashMap<>();

    public void load(String filename) {
        //
    }

    public void save(String filename) {
        //
    }

    public void add(Event event) {
        map.put(event.getId(), event);
    }

    public Event lookup(UUID eventId) {
        return null;
    }

    public void delete(UUID eventId) {
        return;
    }

    public void delete(Event event) {
        return;
    }

    public int size() {
        return map.size();
    }

    @Override
    public Iterator<Event> iterator() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<UUID, Event>> entries = map.entrySet();
        Iterator<Map.Entry<UUID, Event>> it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Event> entry = it.next();
            Event event = entry.getValue();
            sb.append(event.toString() + System.lineSeparator());
        }
        return sb.toString();
    }
}
