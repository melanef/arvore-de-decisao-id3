import java.lang.Integer;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ID3
{
    public final static double LOG2 = Math.log(2.0);
    protected Tree decisionTree;
    protected List<Event> sample;
    protected TreeNode root;

    public ID3()
    {
        this.decisionTree = new Tree();
        this.sample = new ArrayList<Event>();
    }

    public void buildTree()
    {
        Set<String> fields = this.sample.get(0).getPropertyNames();
        this.root = ID3.buildSubTree(this.sample, fields);
    }

    public static TreeNode buildSubTree(List<Event> sample, Set<String> fields)
    {
        TreeNode root = NULL;

        if (fields.size() <= 0) {
            return root;
        }

        double biggestGain = 0.0;
        String biggestGainField;
        Iterator<String> fieldIterator = fields.iterator();
        while (fieldIterator.hasNext()) {
            String currentField = fieldIterator.next();
            double currentFieldGain = ID3.gain(sample, currentField);

            if (currentFieldGain > biggestGain) {
                biggestGainField = new String(currentField);
                biggestGain = currentFieldGain;
            }
        }

        Set<Strings> remainingFields = new TreeSet<String>();
        fieldIterator = fields.iterator();
        while (fieldIterator.hasNext()) {
            String currentField = fieldIterator.next();
            if (!currentField.equals(biggestGainField)) {
                remainingFields.add(currentField);
            }
        }

        root = new TreeNode(biggestGainField);
        HashMap<String, ArrayList<Event>> rootSubsets = ID3.buildSubsets(sample, biggestGainField);
        Iterator<String> subsetIterator = rootSubsets.keySet().iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();
            List<Event> subset = rootSubsets.get(currentKey);
            root.assignNewBranch(currentKey, subset, ID3.buildSubTree(subset, remainingFields));
        }

        return root;
    }

    public String[] getRules()
    {

    }

    public static double entropy(ArrayList<Event> sample)
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
                new Integer(((Integer) frequencies.get(currentCategory)).intValue() + 1)
            );
        }

        double entropy = 0.0;
        Iterator<String> frequencyIterator = frequencyKeys.iterator();
        while (frequencyIterator.hasNext()) {
            String currentKey = frequencyIterator.next();
            double currentFrequency = ((Integer) frequencies.get(currentKey)).doubleValue() / sample.size();
            entropy -= currentFrequency * (Math.log(currentFrequency) / ID3.LOG2);
        }

        return entropy;
    }

    public static double gain(ArrayList<Event> sample, String property)
    {
        double gain = ID3.entropy(sample);

        HashMap<String, ArrayList<Event>> subsets = ID3.buildSubsets(sample, property);

        subsetIterator = subsets.keySet().iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();

            String currentSubset = (ArrayList<Event>) subsets.get(currentPropertyValue);
            gain -= (currentSubset.size() / sample.size()) * ID3.entropy(currentSubset);
        }

        return gain;
    }

    public static HashMap<String, ArrayList<Event>> buildSubsets(ArrayList<Event> sample, String property)
    {
        HashMap<String, ArrayList<Event>> subsets = new HashMap<String, ArrayList<Event>>();

        Iterator<Event> eventIterator = sample.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            String currentPropertyValue = event.get(property);
            if (!subsets.containsKey(currentPropertyValue)) {
                subsets.put(currentPropertyValue, new ArrayList<Event>());
            }

            ((ArrayList<Event>) subsets.get(currentPropertyValue)).add(currentEvent.getClone());
        }

        return subsets;
    }
}
