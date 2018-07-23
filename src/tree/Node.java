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

    protected int hits;
    protected int misses;

    public Node(String property)
    {
        this.property = property;
        this.nodes = new TreeSet<Node>(new NodeComparator());
        this.hits = 0;
        this.misses = 0;
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
            this.hits = node.getHits();
            this.misses = node.getMisses();
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

    public boolean equals(Object o)
    {
        if (o instanceof Node) {
            Node n = (Node) o;

            if (n.getProperty() == null && this.property != null) {
                return false;
            }

            if (n.getProperty() != null && this.property == null) {
                return false;
            }

            if (!n.getProperty().equals(this.property)) {
                return false;
            }

            if (n.getValue() == null && this.value != null) {
                return false;
            }

            if (n.getValue() != null && this.value == null) {
                return false;
            }

            if (!n.getValue().equals(this.value)) {
                return false;
            }

            return true;
        }

        return false;
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
        /*
        Set<Node> nodes = new TreeSet<Node>(new NodeComparator());
        nodes.addAll(this.nodes);
        */
        return this.nodes;
    }

    public int getHits()
    {
        return this.hits;
    }

    public int getMisses()
    {
        return this.misses;
    }

    public Node getNode(String value)
    {
        Iterator<Node> iterator = this.nodes.iterator();
        while (iterator.hasNext()) {
            Node current = iterator.next();
            if (current.getValue().equals(value)) {
                /*return new Node(current);*/
                return current;
            }
        }

        return null;
    }

    public void add(Node node)
    {
        this.nodes.add(node);
    }

    public boolean remove(Node node)
    {
        return this.nodes.remove(node);
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

        Deque<String> currentRules = new ArrayDeque<String>(parentRules);

        String rule = "";
        while (!currentRules.isEmpty()) {
            if (rule.equals("")) {
                rule = "(" + currentRules.pop() + ")";
                continue;
            }

            rule = "(" + currentRules.pop() + ") ^ " + rule;
        }

        rule = "IF " + rule + " THEN " + categoryName + ": " + this.getMajorCategory();
        rules.add(rule);

        Iterator<Node> iterator = this.nodes.iterator();
        while (iterator.hasNext()) {
            Node current = iterator.next();

            currentRules = new ArrayDeque<String>(parentRules);

            String currentRule = this.property + "=" + current.getValue();
            currentRules.push(currentRule);
            rules.addAll(current.getRules(categoryName, currentRules));
        }

        return rules;
    }

    public String getCategory(Event event)
    {
        return this.getCategory(event, null);
    }

    public String getCategory(Event event, String expectedCategory)
    {
        if (this.property == null) {
            String result = new String(this.getMajorCategory());

            if (expectedCategory != null) {
                if (result.equals(expectedCategory)) {
                    this.hits++;
                } else {
                    this.misses++;
                }
            }

            return result;
        }

        Node child = this.getNode(event.get(this.property));

        if (child == null) {
            String result = new String(this.getMajorCategory());

            if (expectedCategory != null) {
                if (result.equals(expectedCategory)) {
                    this.hits++;
                } else {
                    this.misses++;
                }
            }

            return result;
        }

        return child.getCategory(event, expectedCategory);
    }

    public int getNodeCount()
    {
        int nodeCount = 1;

        Iterator<Node> iterator = this.nodes.iterator();
        while (iterator.hasNext()) {
            nodeCount = nodeCount + iterator.next().getNodeCount();
        }

        return nodeCount;
    }

    public ArrayList<Node> getAllNodes()
    {
        ArrayList<Node> nodes = new ArrayList<Node>();

        nodes.add(this);

        Iterator<Node> iterator = this.nodes.iterator();
        while (iterator.hasNext()) {
            Node current = iterator.next();
            nodes.addAll(current.getAllNodes());
        }

        return nodes;
    }

    public String getRule(String categoryName)
    {
        Deque<String> parts = new ArrayDeque<String>();
        Node current = this;
        Node parent = current.getParent();
        while (current != null && parent != null) {
            String property = parent.getProperty();
            if (property != null) {
                parts.addFirst(new String(property + "=" + current.getValue()));
            }

            current = current.getParent();

            if (current == null) {
                break;
            }

            parent = current.getParent();
        }

        String rule = null;
        while (!parts.isEmpty()) {
            if (rule == null) {
                rule = "IF (" + parts.pop() + ")";
                continue;
            }

            rule = rule + " ^ (" + parts.pop() + ")";
        }

        if (rule == null) {
            rule = "IF (true)";
        }

        return new String(rule + " THEN " + categoryName + ": " + this.getMajorCategory());
    }
}
