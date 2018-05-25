import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class TreeBranch
{
    protected String value;
    protected List<Event> events;
    protected TreeNode node;

    public TreeBranch(String value)
    {
        this.value = value;
        this.events = new ArrayList<Event>();
    }

    public addEvent(Event event)
    {
        this.events.add(event);
    }
}
