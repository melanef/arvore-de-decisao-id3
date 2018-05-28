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

    public void setNode(TreeNode node)
    {
        this.node = node;
    }
}
