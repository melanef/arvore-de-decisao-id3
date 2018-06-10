package tree;

import java.lang.String;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import comparators.BranchComparator;
import models.Event;
import models.Sample;
import utils.ID3;

public class Node
{
    protected String property;
    protected Set<Branch> branches = null;

    public Node(String property)
    {
        this.property = property;
        this.branches = new TreeSet<Branch>(new BranchComparator());
    }

    public Node(Node node)
    {
        if (node != null) {
            this.property = node.getProperty();
            this.branches = node.getBranches();
        }
    }

    public void add(Branch branch)
    {
        this.branches.add(branch);
    }

    public String getProperty()
    {
        return new String(this.property);
    }

    public Set<Branch> getBranches()
    {
        Set<Branch> branches = new TreeSet<Branch>(new BranchComparator());
        branches.addAll(this.branches);
        return branches;
    }

    public void assignNewBranch(String value, Sample sample, Node node)
    {
        Branch newBranch = new Branch(value, sample, node);
        this.add(newBranch);
    }

    public ArrayList<String> getRules(String categoryName, Deque<String> parentRules)
    {
        ArrayList<String> rules = new ArrayList<String>();

        Iterator<Branch> iterator = this.branches.iterator();
        while (iterator.hasNext()) {
            Branch current = iterator.next();
            Node currentNode = current.getNode();

            Deque<String> currentRules = new ArrayDeque<String>(parentRules);

            String currentRule = this.property + "=" + current.getValue();
            if (currentNode == null) {

                String rule = "";
                while (!currentRules.isEmpty()) {
                    rule = "(" + currentRules.pop() + ") ^ " + rule;
                }

                rule = rule + "(" + currentRule + ")";

                rule = "IF " + rule + " THEN " + categoryName + ": " + current.getMajorCategory();
                rules.add(rule);
                continue;
            }

            currentRules.push(currentRule);
            rules.addAll(currentNode.getRules(categoryName, currentRules));
        }

        return rules;
    }

    public Branch getBranch(String propertyValue)
    {
        Iterator<Branch> iterator = this.branches.iterator();
        while (iterator.hasNext()) {
            Branch current = iterator.next();
            if (current.getValue().equals(propertyValue)) {
                return new Branch(current);
            }
        }

        return null;
    }

    public String getCategory(Event event, String previousMajorCategory)
    {
        Branch eventBranch = this.getBranch(event.get(this.property));

        if (eventBranch == null) {
            return new String(previousMajorCategory);
        }

        Node nextNode = eventBranch.getNode();
        if (nextNode == null) {
            return eventBranch.getMajorCategory();
        }

        return nextNode.getCategory(event, eventBranch.getMajorCategory());
    }
}
