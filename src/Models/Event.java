import java.lang.Cloneable;
import java.lang.String;
import java.util.Properties;


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
}
