package utils;

import java.lang.Double;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import comparators.EventComparator;
import models.Event;
import models.Sample;

public class Discretizer
{
    public static Sample discretizeProperty(Sample sample, String property)
    {
        ArrayList<Double> breakpoints = new ArrayList<Double>();
        TreeSet<Event> events = new TreeSet<Event>(new EventComparator(property));
        Iterator<Event> iterator = sample.iterator();
        while (iterator.hasNext()) {
            Event currentEvent = iterator.next();
            events.add(currentEvent);
        }

        Double lastPropertyValue = null;
        String lastCategory = null;
        iterator = events.iterator();
        while (iterator.hasNext()) {
            Event current = iterator.next();
            if (lastCategory == null) {
                lastCategory = current.getCategory();
                lastPropertyValue = new Double(current.get(property));
                continue;
            }

            if (!lastCategory.equals(current.getCategory())) {
                breakpoints.add((new Double(current.get(property)) + lastPropertyValue) / 2.0);
                lastCategory = new String(current.getCategory());
            }

            lastPropertyValue = new Double(current.get(property));
        }

        double biggestGain = 0.0;
        Sample bestSample = null;

        for (int i = 0; i < breakpoints.size(); i++) {
            Double breakpoint = breakpoints.get(i);
            Interval [] intervals = new Interval[2];
            intervals[0] = new Interval(null, breakpoint);
            intervals[1] = new Interval(breakpoint, null);

            Sample currentSample = new Sample(sample);
            iterator = currentSample.iterator();
            while (iterator.hasNext()) {
                Event currentEvent = iterator.next();
                Double value = new Double(currentEvent.get(property));

                int comparison = value.compareTo(breakpoint);
                if (comparison < 0) {
                    currentEvent.set(property, intervals[0].toString());
                } else {
                    currentEvent.set(property, intervals[1].toString());
                }
            }

            double gain = ID3.gain(currentSample, property);
            if (gain > biggestGain) {
                biggestGain = gain;
                bestSample = new Sample(currentSample);
            }
        }

        return bestSample;
    }
}
