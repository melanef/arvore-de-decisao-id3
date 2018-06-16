package models;

import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Sample
{
    protected String [] fields;
    protected List<Event> events;
    protected String majorCategory;

    public Sample(String [] fields)
    {
        this.events = new ArrayList<Event>();
        this.fields = fields;
        this.majorCategory = null;
    }

    public Sample(Sample example)
    {
        this(example.getFields());
        this.fillEvents(example.getEvents());
    }

    public Sample(String [] fields, List<Event> events)
    {
        this(fields);
        this.fillEvents(events);
    }

    public void fillEvents(List<Event> events)
    {
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            this.events.add(new Event(current));
        }

        this.defineMajorCategory();
    }

    public int size()
    {
        return this.events.size();
    }

    public boolean contains(Event event) {
        return this.events.contains(event);
    }

    public Iterator<Event> iterator()
    {
        return this.events.iterator();
    }

    public String toString()
    {
        String out = "{";
        Iterator<Event> iterator = this.iterator();
        while (iterator.hasNext()) {
            out = out + iterator.next().toString();

            /*
            if (iterator.hasNext()) {
                out = out + ", ";
            }
            */
        }

        out = out + " -- Major category: " + this.getMajorCategory() + "}";

        return out;
    }

    public Event getEvent(int index)
    {
        return this.events.get(index);
    }

    public List<Event> getEvents()
    {
        return new ArrayList<Event>(this.events);
    }

    public void addEvent(String [] eventData)
    {
        if (this.isValidEventData(eventData)) {
            this.events.add(new Event(this.fields, eventData));
        }
    }

    public void addEvent(Event event)
    {
        this.events.add(event);
    }

    public boolean isValidEventData(String [] eventData)
    {
        if (eventData.length < (this.fields.length + 1)) {
            return false;
        }

        for (int i = 0; i < eventData.length; i++) {
            if (eventData[i].equals("?")) {
                return false;
            }
        }

        return true;
    }

    public String [] getFields()
    {
        return this.fields;
    }

    public void defineMajorCategory()
    {
        HashMap<String, Integer> categories = new HashMap<String, Integer>();
        Iterator<Event> iterator = this.events.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            String key = current.getCategory();
            if (!categories.containsKey(key)) {
                categories.put(key, new Integer(0));
            }

            categories.put(key, new Integer(categories.get(key).intValue() + 1));
        }

        int biggest = 0;
        String majorCategory = "";
        Iterator<String> keyIterator = categories.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            int current = categories.get(key).intValue();
            if (current > biggest) {
                biggest = current;
                majorCategory = new String(key);
            }
        }

        this.majorCategory = majorCategory;
    }

    public String getMajorCategory()
    {
        if (this.majorCategory == null) {
            this.defineMajorCategory();
        }

        return new String(this.majorCategory);
    }

    public List<String> getFieldList()
    {
        ArrayList<String> fieldList = new ArrayList<String>(this.fields.length);
        for (int i = 0; i < this.fields.length; i++) {
            fieldList.add(new String(this.fields[i]));
        }

        return fieldList;
    }

    public boolean hasSingleCategory()
    {
        String category = this.events.get(0).getCategory();
        Iterator<Event> iterator = this.events.iterator();
        while (iterator.hasNext()) {
            Event currentEvent = iterator.next();
            String currentCategory = currentEvent.getCategory();
            if (!category.equals(currentCategory)) {
                return false;
            }
        }

        return true;
    }

    public Map<String, Sample> buildSubsets(String property)
    {
        HashMap<String, Sample> subsets = new HashMap<String, Sample>();

        Iterator<Event> eventIterator = this.events.iterator();
        while (eventIterator.hasNext()) {
            Event currentEvent = eventIterator.next();
            String currentPropertyValue = currentEvent.get(property);
            if (!subsets.containsKey(currentPropertyValue)) {
                subsets.put(currentPropertyValue, new Sample(this.fields));
            }

            subsets.get(currentPropertyValue).addEvent(new Event(currentEvent));
        }

        return subsets;
    }

    public List<Sample> split(int parts)
    {
        int size = this.size() / parts;
        List<Sample> samples = new ArrayList<Sample>(parts);

        for (int i = 0; i < parts; i++) {
            samples.add(i, new Sample(this.getFields()));
            for (int j = 0; j < this.size(); j++) {
                if (j % parts == i) {
                    samples.get(i).addEvent(this.getEvent(j));
                }
            }
        }

        return samples;
    }

    public Sample getComplement(Sample sample)
    {
        Sample complement = new Sample(this.getFields());
        Iterator<Event> iterator = this.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            if (!sample.contains(current)) {
                complement.addEvent(current);
            }
        }

        return complement;
    }
}
