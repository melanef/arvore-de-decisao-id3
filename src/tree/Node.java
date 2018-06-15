package tree;

import java.lang.String;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import comparators.NodeComparator;
import models.Event;
import models.Sample;
import utils.ID3;

public class Node
{
    protected String value;
    protected String property;
    protected Set<Node> nodes = null;
    protected Sample sample;
    protected Node parent = null;

    public Node(String property)
    {
        this.property = property;
        this.nodes = new TreeSet<Node>(new NodeComparator());
    }

    public Node(String property, Sample sample)
    {
        this(property);
        this.sample = new Sample(sample);
    }

    public Node(Node node)
    {
        if (node != null) {
            this.property = node.getProperty();
            this.value = node.getValue();
            this.nodes = node.getNodes();
            this.sample = node.getSample();
            this.parent = node.getParent();
        }
    }

    public String toString()
    {
        return "Node" + " - Property: " + (this.property != null ? this.property : "NULL") +
                        " - Value: " + (this.value != null ? this.value : "NULL") +
                        " - Sample size: " + this.sample.size() +
                        " - Nodes size: " + this.nodes.size()
        ;
    }

    public String getProperty()
    {
        if (this.property == null) {
            return null;
        }

        return new String(this.property);
    }

    public String getMajorCategory()
    {
        return new String(this.sample.getMajorCategory());
    }

    public String getValue()
    {
        if (this.value == null) {
            return null;
        }

        return new String(this.value);
    }

    public void setValue(String value)
    {
        this.value = new String(value);
    }

    public Sample getSample()
    {
        return new Sample(this.sample);
    }

    public void setSample(Sample sample)
    {
        this.sample = new Sample(sample);
    }

    public Set<Node> getNodes()
    {
        Set<Node> nodes = new TreeSet<Node>(new NodeComparator());
        nodes.addAll(this.nodes);
        return nodes;
    }

    public Node getNode(String value)
    {
        Iterator<Node> iterator = this.nodes.iterator();
        while (iterator.hasNext()) {
            Node current = iterator.next();
            if (current.getValue().equals(value)) {
                return new Node(current);
            }
        }

        return null;
    }

    public void add(Node node)
    {
        this.nodes.add(node);
    }

    public Node getParent()
    {
        return this.parent;
    }

    public void setParent(Node parent)
    {
        this.parent = parent;
    }

    public ArrayList<String> getRules(String categoryName, Deque<String> parentRules)
    {
        ArrayList<String> rules = new ArrayList<String>();

        Iterator<Node> iterator = this.nodes.iterator();
        while (iterator.hasNext()) {
            Node current = iterator.next();

            Deque<String> currentRules = new ArrayDeque<String>(parentRules);

            String currentRule = this.property + "=" + current.getValue();
            if (current == null) {
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
            rules.addAll(current.getRules(categoryName, currentRules));
        }

        return rules;
    }

    public String getCategory(Event event)
    {
        if (this.property == null) {
            return this.getMajorCategory();
        }

        Node child = this.getNode(event.get(this.property));

        if (child == null) {
            return new String(this.getMajorCategory());
        }

        return child.getCategory(event);
    }
}
