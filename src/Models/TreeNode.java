import java.lang.String;
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
}
