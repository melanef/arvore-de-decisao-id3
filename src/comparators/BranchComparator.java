package comparators;

import java.util.Comparator;

import tree.Branch;

public class BranchComparator implements Comparator<Branch>
{
    public int compare(Branch b1, Branch b2)
    {
        return b1.getValue().compareTo(b2.getValue());
    }
}
