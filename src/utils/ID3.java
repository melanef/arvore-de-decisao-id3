package utils;

import java.lang.Integer;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.Event;
import models.Sample;
import tree.Node;

public class ID3 implements AIAlgorithm
{
    public final static double LOG2 = Math.log(2.0);
    protected Sample sample;
    protected Node root;
    protected String categoryName;
    protected Logger logger;

    public ID3(String categoryName)
    {
        this.sample = null;
        this.categoryName = categoryName;
        this.root = null;
        this.logger = null;
    }

    public ID3(String categoryName, Sample sample)
    {
        this(categoryName);
        this.setSample(sample);
    }

    public void setSample(Sample sample)
    {
        this.sample = new Sample(sample);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void init()
    {
        this.buildTree();
    }

    public void buildTree()
    {
        if (this.logger != null) {
            this.logger.onInit(new Classifier(null, this.sample.getMajorCategory(), this.categoryName));
        }

        this.root = this.createNode(this.sample, this.sample.getFieldList());

        if (this.logger != null) {
            this.logger.afterAddNode(new Classifier(this.root, this.sample.getMajorCategory(), this.categoryName));
        }

        this.buildSubTree(this.root, this.sample.getFieldList());
    }

    public Node createNode(Sample sample, List<String> fields)
    {
        if (fields.size() <= 0 || sample.hasSingleCategory()) {
            return new Node(null, sample);
        }

        double biggestGain = 0.0;
        String biggestGainField = null;
        Iterator<String> fieldIterator = fields.iterator();
        while (fieldIterator.hasNext()) {
            String currentField = fieldIterator.next();
            double currentFieldGain = ID3.gain(sample, currentField);

            if (biggestGainField == null) {
                biggestGainField = new String(currentField);
            }

            if (currentFieldGain > biggestGain) {
                biggestGainField = new String(currentField);
                biggestGain = currentFieldGain;
            }
        }

        Node node = new Node(biggestGainField, sample);
        return node;
    }

    public void buildSubTree(Node root, List<String> fields)
    {
        if (fields.size() > 0 && root.getProperty() != null) {
            String field = root.getProperty();
            Map<String, Sample> rootSubsets = root.getSample().buildSubsets(field);
            Iterator<String> subsetIterator = rootSubsets.keySet().iterator();

            List<String> remainingFields = new ArrayList<String>();
            Iterator<String> fieldIterator = fields.iterator();
            while (fieldIterator.hasNext()) {
                String currentField = fieldIterator.next();
                if (!currentField.equals(field)) {
                    remainingFields.add(currentField);
                }
            }

            while (subsetIterator.hasNext()) {
                String currentKey = subsetIterator.next();
                Sample subset = rootSubsets.get(currentKey);

                Node subTree = this.createNode(subset, remainingFields);
                if (subTree != null) {
                    subTree.setValue(currentKey);
                    subTree.setParent(root);
                    root.add(subTree);
                }

                if (this.logger != null) {
                    this.logger.afterAddNode(new Classifier(this.root, this.sample.getMajorCategory(), this.categoryName));
                }
            }

            Iterator<Node> nodeIterator = root.getNodes().iterator();
            while (nodeIterator.hasNext()) {
                Node currentNode = nodeIterator.next();
                this.buildSubTree(currentNode, remainingFields);
            }
        }
    }

    public Classifier getClassifier()
    {
        if (this.root == null) {
            this.buildTree();
        }

        return new Classifier(this.root, this.sample.getMajorCategory(), this.categoryName);
    }

    public static double entropy(Sample sample)
    {
        Map<String, Integer> frequencies = new HashMap<String, Integer>();

        Iterator<Event> eventIterator = sample.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            String currentCategory = currentEvent.getCategory();
            if (!frequencies.containsKey(currentCategory)) {
                frequencies.put(currentCategory, new Integer(0));
            }

            frequencies.put(
                currentCategory,
                new Integer(frequencies.get(currentCategory).intValue() + 1)
            );
        }

        double entropy = 0.0;
        Iterator<String> frequencyIterator = frequencies.keySet().iterator();
        while (frequencyIterator.hasNext()) {
            String currentKey = frequencyIterator.next();
            double currentFrequency = frequencies.get(currentKey).doubleValue() / (new Integer(sample.size())).doubleValue();
            entropy -= currentFrequency * (Math.log(currentFrequency) / ID3.LOG2);
        }

        return entropy;
    }

    public static double gain(Sample sample, String property)
    {
        double gain = ID3.entropy(sample);

        Map<String, Sample> subsets = sample.buildSubsets(property);

        Iterator<String> subsetIterator = subsets.keySet().iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();

            Sample currentSubset = subsets.get(currentKey);
            double currentSubsetGain = ((new Integer(currentSubset.size())).doubleValue() / (new Integer(sample.size())).doubleValue()) * ID3.entropy(currentSubset);
            gain = gain - currentSubsetGain;
        }

        return gain;
    }
}
