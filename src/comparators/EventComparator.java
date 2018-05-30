package comparators;

import java.util.Comparator;

import models.Event;

public class EventComparator implements Comparator<Event>
{
    String property;

    public EventComparator(String property)
    {
        this.property = property;
    }

    public int compare(Event e1, Event e2)
    {
        return e1.get(this.property).compareTo(e2.get(this.property));
    }
}
