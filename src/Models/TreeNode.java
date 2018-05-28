import java.lang.String;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TreeNode
{
    protected String property;
    protected Set<TreeBranch> branches;

    public TreeNode(String property)
    {
        this.property = property;
        this.branches = new TreeSet<TreeBranch>(new TreeBranchComparator());
    }

    public void add(TreeBranch branch)
    {
        this.branches.add(branch);
    }

    public void assignNewBranch(String value, List<Event> events, TreeNode node)
    {
        TreeBranch newBranch = new TreeBranch(value);
        newBranch.addEvents(events);
        newBranch.setNode(node);
        this.add(newBranch);
    }
}
