import java.lang.String;
import java.util.Properties;

public class Event
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
}
