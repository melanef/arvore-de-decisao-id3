package utils;

import java.util.Iterator;

import models.Event;
import models.Sample;
import tree.Node;

public class Classifier
{
    protected Node tree;
    protected String majorCategory;

    public Classifier(Node tree, String majorCategory)
    {
        this.tree = new Node(tree);
        this.majorCategory = new String(majorCategory);
    }

    public String getCategory(Event event)
    {
        if (this.tree == null) {
            return new String(this.majorCategory);
        }

        return this.tree.getCategory(event, this.majorCategory);
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
        return new Node(this.tree);
    }
}
