import java.lang.Cloneable;
import java.lang.String;
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
        return this.properties.keySet();
    }

    public Event getClone()
    {
        try {
            return (Event) this.clone();
        }
        catch (CloneNotSupportedException exception) {
            System.out.println("Clonagem não suportada na classe Event");
            return null;
        }
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
}
