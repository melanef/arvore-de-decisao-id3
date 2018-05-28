package tree;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Event;

public class Branch
{
    protected String value;
    protected List<Event> events;
    protected Node node;
    protected String majorCategory;

    public Branch(String value)
    {
        this.value = value;
        this.events = new ArrayList<Event>();
        this.majorCategory = null;
    }

    public void addEvent(Event event)
    {
        this.events.add(event);
    }

    public void addEvents(List<Event> events)
    {
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            this.events.add(current.getClone());
        }
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    public Node getNode()
    {
        return this.node;
    }

    public void setMajorCategory(String category)
    {
        this.majorCategory = category;
    }

    public String getMajorCategory()
    {
        return this.majorCategory;
    }

    public String getValue()
    {
        return this.value;
    }
}
