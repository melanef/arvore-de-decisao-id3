package utils;

import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Sample;
import tree.Node;

public class Classifier
{
    protected String categoryName;
    protected Node tree;
    protected String majorCategory;

    public Classifier(Node tree, String majorCategory, String categoryName)
    {
        if (tree != null) {
            this.tree = new Node(tree);
        }

        this.majorCategory = new String(majorCategory);
        this.categoryName = new String(categoryName);
    }

    public Classifier(Classifier classifier)
    {
        if (classifier.getTree() != null) {
            this.tree = new Node(classifier.getTree());
        }

        this.majorCategory = new String(classifier.getMajorCategory());
        this.categoryName = new String(classifier.getCategoryName());
    }

    public String getMajorCategory()
    {
        return this.majorCategory;
    }

    public String getCategory(Event event)
    {
        if (this.tree == null) {
            return new String(this.majorCategory);
        }

        return this.tree.getCategory(event);
    }

    public double error(Sample testSample)
    {
        int mistakes = 0;
        Iterator<Event> iterator = testSample.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            String currentGuessedCategory = this.getCategory(current);
            if (!currentGuessedCategory.equals(current.getCategory())) {
                mistakes++;
            }
        }

        return (new Integer(mistakes)).doubleValue() / (new Integer(testSample.size())).doubleValue();
    }

    public double accuracy(Sample testSample)
    {
        return 1.0 - this.error(testSample);
    }

    public Node getTree()
    {
        return this.tree;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public List<String> getRules()
    {
        List<String> rules = new ArrayList<String>();
        if (this.tree == null) {
            String rule = this.categoryName + ": " + this.majorCategory;
            rules.add(rule);

            return rules;
        }

        rules.addAll(this.tree.getRules(this.categoryName, new ArrayDeque<String>()));

        return rules;
    }

    public int getNodeCount()
    {
        if (this.tree == null) {
            return 0;
        }

        return this.tree.getNodeCount();
    }

    public List<String> sortedRules(Sample testSample)
    {
        Iterator<Event> eventIterator = testSample.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            this.checkRule(currentEvent, currentEvent.getCategory());
        }

        List<Node> leafs = this.tree.getAllNodes();

        ArrayList<String> rules = new ArrayList<String>();

        while (leafs.size() > 0) {
            boolean found = true;

            Node best = null;
            int bestIndex = 0;
            double bestScore = 0.0;
            for (int i = 0; i < leafs.size(); i++) {
                Node current = leafs.get(i);
                int total = current.getHits() + current.getMisses();
                if (total == 0) {
                    leafs.remove(i);
                    continue;
                }

                double currentScore = current.getHits() * current.getHits() / total;

                if (best == null) {
                    best = current;
                    bestIndex = i;
                    bestScore = currentScore;
                }

                if (currentScore > bestScore) {
                    best = current;
                    bestScore = currentScore;
                    bestIndex = i;
                }
            }

            if (best != null) {
                rules.add(new String(best.getRule(this.getCategoryName()) + " - " + best.getHits() + "/" + (best.getHits() + best.getMisses())));
                leafs.remove(bestIndex);
            }
        }

        return rules;
    }

    protected void checkRule(Event event, String expectedCategory)
    {
        if (this.tree != null) {
            this.tree.getCategory(event, expectedCategory);
        }
    }
}
