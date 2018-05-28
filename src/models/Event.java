package models;

import java.lang.Cloneable;
import java.lang.String;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class Event implements Cloneable
{
    protected String category;
    protected Properties properties;

    protected Event()
    {
        this.properties = new Properties();
    }

    public String toString()
    {
        String propertiesString = "";

        Iterator<String> iterator = this.getPropertyNames().iterator();
        while (iterator.hasNext()) {
            String property = iterator.next();
            propertiesString = propertiesString + property + "=" + this.get(property) + ",";
        }

        return "{" + propertiesString + "}" + " => " + this.category;
    }

    public String get(String property)
    {
        return this.properties.getProperty(property);
    }

    public void set(String property, Object value)
    {
        this.properties.setProperty(property, value.toString());
    }

    public String getCategory()
    {
        return new String(this.category);
    }

    public Set<String> getPropertyNames()
    {
        return this.properties.stringPropertyNames();
    }

    public Event getClone()
    {
        return (Event) this.clone();
    }

    protected Object clone()
    {
        Event clone = new Event();
        clone.category = new String(this.category);
        clone.properties = new Properties(this.properties);
        /*
        Iterator<String> iterator = this.getPropertyNames().iterator();
        while (iterator.hasNext()) {
            String property = iterator.next();
            clone.set(property, this.get(property));
        }
        */

        return (Object) clone;
    }

    public static Event createEvent(String [] data, String [] fields)
    {
        Event newEvent = new Event();
        newEvent.category = data[data.length - 1];
        for (int i = 0; i < fields.length; i++) {
            newEvent.properties.setProperty(fields[i], data[i]);
        }

        return newEvent;
    }
}
