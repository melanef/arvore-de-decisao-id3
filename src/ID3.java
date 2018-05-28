import java.lang.Integer;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

public class ID3
{
    public final static double LOG2 = Math.log(2.0);
    protected Tree decisionTree;
    protected List<Event> sample;

    public ID3()
    {
        this.decisionTree = new Tree();
        this.sample = new ArrayList<Event>();
    }

    public void buildTree()
    {

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

        HashMap<String, ArrayList<Event>> subsets = new HashMap<String, ArrayList<Event>>();
        ArrayList<String> subsetKeys = new ArrayList<String>();

        Iterator<Event> eventIterator = sample.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            String currentPropertyValue = event.get(property);
            if (!subsets.containsKey(currentPropertyValue)) {
                subsets.put(currentPropertyValue, new ArrayList<Event>());
                subsetKeys.add(currentPropertyValue);
            }

            ((ArrayList<Event>) subsets.get(currentPropertyValue)).add(currentEvent.getClone());
        }

        subsetIterator = subsetKeys.iterator();
        while (subsetIterator.hasNext()) {
            String currentKey = subsetIterator.next();

            String currentSubset = (ArrayList<Event>) subsets.get(currentPropertyValue);
            gain -= (currentSubset.size() / sample.size()) * ID3.entropy(currentSubset);
        }

        return gain;
    }
}
