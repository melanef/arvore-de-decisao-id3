package utils;

import java.lang.Integer;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import models.Event;
import tree.Node;

public class ID3
{
    public final static double LOG2 = Math.log(2.0);
    protected List<Event> sample;
    protected Node root;
    protected String categoryName;
    protected String majorCategory;

    public ID3(String categoryName)
    {
        this.sample = new ArrayList<Event>();
        this.categoryName = categoryName;
        this.majorCategory = null;
        this.root = null;
    }

    public void setSample(List<Event> sample)
    {
        Iterator<Event> iterator = sample.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            this.sample.add(current.getClone());
        }

        this.majorCategory = ID3.getMajorCategory(this.sample);
    }

    public void buildTree()
    {
        Set<String> fields = this.sample.get(0).getPropertyNames();
        this.root = ID3.buildSubTree(this.sample, fields);
    }

    public static Node buildSubTree(List<Event> sample, Set<String> fields)
    {
        Node root = null;

        if (fields.size() <= 0) {
            return root;
        }

        if (ID3.sampleHasSingleCategory(sample)) {
            return root;
        }

        double biggestGain = 0.0;
        String biggestGainField = "";
        Iterator<String> fieldIterator = fields.iterator();
        while (fieldIterator.hasNext()) {
            String currentField = fieldIterator.next();
            double currentFieldGain = ID3.gain(sample, currentField);

            if (currentFieldGain > biggestGain) {
                biggestGainField = new String(currentField);
                biggestGain = currentFieldGain;
            }
        }

        Set<String> remainingFields = new TreeSet<String>();
        fieldIterator = fields.iterator();
        while (fieldIterator.hasNext()) {
            String currentField = fieldIterator.next();
            if (!currentField.equals(biggestGainField)) {
                remainingFields.add(currentField);
            }
        }

        root = new Node(biggestGainField);
        HashMap<String, ArrayList<Event>> rootSubsets = ID3.buildSubsets(sample, biggestGainField);
        Iterator<String> subsetIterator = rootSubsets.keySet().iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();
            List<Event> subset = rootSubsets.get(currentKey);
            root.assignNewBranch(currentKey, subset, ID3.buildSubTree(subset, remainingFields));
        }

        return root;
    }

    public ArrayList<String> getRules()
    {
        ArrayList<String> rules = new ArrayList<String>();
        if (this.root == null) {
            String rule = this.categoryName + " = " + this.majorCategory;
            rules.add(rule);

            return rules;
        }

        rules.addAll(this.root.getRules(this.categoryName, new Stack<String>()));

        return rules;
    }

    public static double entropy(List<Event> sample)
    {
        HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
        ArrayList<String> frequencyKeys = new ArrayList<String>();

        Iterator<Event> eventIterator = sample.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            String currentCategory = currentEvent.getCategory();
            if (!frequencies.containsKey(currentCategory)) {
                frequencies.put(currentCategory, new Integer(0));
                frequencyKeys.add(currentCategory);
            }

            frequencies.put(
                currentCategory,
                new Integer(frequencies.get(currentCategory).intValue() + 1)
            );
        }

        double entropy = 0.0;
        Iterator<String> frequencyIterator = frequencyKeys.iterator();
        while (frequencyIterator.hasNext()) {
            String currentKey = frequencyIterator.next();
            double currentFrequency = frequencies.get(currentKey).doubleValue() / (new Integer(sample.size())).doubleValue();
            entropy -= currentFrequency * (Math.log(currentFrequency) / ID3.LOG2);
        }

        return entropy;
    }

    public static double gain(List<Event> sample, String property)
    {
        double gain = ID3.entropy(sample);

        HashMap<String, ArrayList<Event>> subsets = ID3.buildSubsets(sample, property);

        Iterator<String> subsetIterator = subsets.keySet().iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();

            ArrayList<Event> currentSubset = subsets.get(currentKey);
            double currentSubsetGain = ((new Integer(currentSubset.size())).doubleValue() / (new Integer(sample.size())).doubleValue()) * ID3.entropy(currentSubset);
            gain = gain - currentSubsetGain;
        }

        return gain;
    }

    public static HashMap<String, ArrayList<Event>> buildSubsets(List<Event> sample, String property)
    {
        HashMap<String, ArrayList<Event>> subsets = new HashMap<String, ArrayList<Event>>();

        Iterator<Event> eventIterator = sample.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            String currentPropertyValue = currentEvent.get(property);
            if (!subsets.containsKey(currentPropertyValue)) {
                subsets.put(currentPropertyValue, new ArrayList<Event>());
            }

            subsets.get(currentPropertyValue).add(currentEvent.getClone());
        }

        return subsets;
    }

    public static String getMajorCategory(List<Event> sample)
    {
        HashMap<String, Integer> categories = new HashMap<String, Integer>();
        Iterator<Event> iterator = sample.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            String key = current.getCategory();
            if (!categories.containsKey(key)) {
                categories.put(key, new Integer(0));
            }

            categories.put(key, new Integer(categories.get(key).intValue() + 1));
        }

        int biggest = 0;
        String biggestKey = "";
        Set<String> keys = categories.keySet();
        Iterator<String> keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            int current = categories.get(key).intValue();
            if (current > biggest) {
                biggest = current;
                biggestKey = new String(key);
            }
        }

        return biggestKey;
    }

    public static boolean sampleHasSingleCategory(List<Event> sample)
    {
        String category = sample.get(0).getCategory();
        Iterator<Event> iterator = sample.iterator();
        while (iterator.hasNext()) {
            Event currentEvent = iterator.next();
            String currentCategory = currentEvent.getCategory();
            if (!category.equals(currentCategory)) {
                return false;
            }
        }

        return true;
    }
}
