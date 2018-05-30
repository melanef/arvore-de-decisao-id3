package models;

import java.lang.Cloneable;
import java.lang.Comparable;
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

    public Event(String [] fields, String [] data)
    {
        this();
        this.category = data[data.length - 1];
        for (int i = 0; i < fields.length; i++) {
            this.properties.setProperty(fields[i], data[i]);
        }
    }

    public Event(Event example)
    {
        this.properties = example.getProperties();
        this.category = new String(example.getCategory());
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

    public Properties getProperties()
    {
        Properties copy = new Properties();
        copy.putAll(this.properties);

        return copy;
    }
}
