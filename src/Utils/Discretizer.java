import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;

package utils;
public class Discretizer
{
    protected HashMap<String, ArrayList<Interval>> propertyIntervals;

    public Discretizer(ArrayList<String []> grossData, String[] fields, String[] continuousFields)
    {
        for (int i = 0; i < fields.length; i++) {
            boolean continuous = false;

            for (int j = 0; j < continuousFields.length; j++) {
                if (fields[i].equals(continuousFields[j])) {
                    continuous = true;
                    break;
                }
            }

            if (!continuous) {
                continue;
            }

            ArrayList<Interval> intervals =
            propertyIntervals.put(fields[i], intervals);
        }
    }
}
