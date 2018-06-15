package comparators;

import java.util.Comparator;

import tree.Node;

public class NodeComparator implements Comparator<Node>
{
    public int compare(Node b1, Node b2)
    {
        return b1.getValue().compareTo(b2.getValue());
    }
}
