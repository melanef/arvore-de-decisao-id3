package tree;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import comparators.BranchComparator;
import models.Event;
import models.Sample;
import utils.ID3;

public class Node
{
    protected String property;
    protected Set<Branch> branches;

    public Node(String property)
    {
        this.property = property;
        this.branches = new TreeSet<Branch>(new BranchComparator());
    }

    public void add(Branch branch)
    {
        this.branches.add(branch);
    }

    public void assignNewBranch(String value, Sample sample, Node node)
    {
        Branch newBranch = new Branch(value, sample, node);
        this.add(newBranch);
    }

    public ArrayList<String> getRules(String categoryName, Stack<String> parentRules)
    {
        ArrayList<String> rules = new ArrayList<String>();

        Iterator<Branch> iterator = this.branches.iterator();
        while (iterator.hasNext()) {
            Branch current = iterator.next();
            Node currentNode = current.getNode();

            Stack<String> currentRules = new Stack<String>();
            Stack<String> temp = new Stack<String>();

            while (!parentRules.empty()) {
                String top = parentRules.pop();
                currentRules.push(top);
                temp.push(top);
            }

            parentRules = temp;

            String currentRule = this.property + "=" + current.getValue();
            if (currentNode == null) {
                String rule = "IF";
                String stackedRule = "(" + currentRule + ")" ;
                while (!currentRules.empty()) {
                    stackedRule = "(" + currentRules.pop() + ") ^ " + stackedRule;
                }

                rule = rule + " " + stackedRule + " THEN " + categoryName + ": " + current.getMajorCategory();
                rules.add(rule);
                continue;
            }

            currentRules.push(currentRule);
            rules.addAll(currentNode.getRules(categoryName, currentRules));
        }

        return rules;
    }
}
