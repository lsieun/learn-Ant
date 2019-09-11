package d1.core;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Event implements Comparable<Event>, Serializable {
    private UUID id;
    private Date date;
    private String name;
    private String text;

    public Event() {
    }

    public Event(UUID id, Date date, String name, String text) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int compareTo(Event that) {
        return 0;
    }

    @Override
    public String toString() {
        return "Event #" +
                id + " - " +
                (date != null ? date.toString() : "(no date)") + " - " +
                (text != null ? text : "");
    }
}
