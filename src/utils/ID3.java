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

    public ID3(String categoryName)
    {
        this.sample = null;
        this.categoryName = categoryName;
        this.root = null;
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

    public void buildTree()
    {
        this.root = ID3.buildSubTree(this.sample, this.sample.getFieldList());
    }

    public static Node buildSubTree(Sample sample, List<String> fields)
    {
        Node root = null;

        if (fields.size() <= 0) {
            return root;
        }

        if (sample.hasSingleCategory()) {
            return root;
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

        List<String> remainingFields = new ArrayList<String>();
        fieldIterator = fields.iterator();
        while (fieldIterator.hasNext()) {
            String currentField = fieldIterator.next();
            if (!currentField.equals(biggestGainField)) {
                remainingFields.add(currentField);
            }
        }

        root = new Node(biggestGainField);
        Map<String, Sample> rootSubsets = sample.buildSubsets(biggestGainField);
        Iterator<String> subsetIterator = rootSubsets.keySet().iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();
            Sample subset = rootSubsets.get(currentKey);

            Node subTree = ID3.buildSubTree(subset, remainingFields);
            subTree.setSample(subset);
            subTree.setValue(currentKey);
            subTree.setParent(root);
            root.add(subTree);
        }

        return root;
    }

    public ArrayList<String> getRules()
    {
        ArrayList<String> rules = new ArrayList<String>();
        if (this.root == null) {
            String rule = this.categoryName + ": " + this.sample.getMajorCategory();
            rules.add(rule);

            return rules;
        }

        rules.addAll(this.root.getRules(this.categoryName, new ArrayDeque<String>()));

        return rules;
    }

    public Classifier getClassifier()
    {
        if (root == null) {
            this.buildTree();
        }

        return new Classifier(this.root, this.sample.getMajorCategory());
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
