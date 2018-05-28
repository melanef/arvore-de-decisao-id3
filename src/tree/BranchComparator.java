package tree;

import java.util.Comparator;

public class BranchComparator implements Comparator<Branch>
{
    public int compare(Branch b1, Branch b2)
    {
        return b1.getValue().compareTo(b2.getValue());
    }
}
