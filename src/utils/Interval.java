package utils;

import java.lang.Double;

public class Interval
{
    protected Double lowerEdge;
    protected Double upperEdge;

    public Interval(Double lowerEdge, Double upperEdge)
    {
        this.lowerEdge = lowerEdge;
        this.upperEdge = upperEdge;
    }

    public boolean contains(Double value) {
        if (value == null) {
            return false;
        }

        if (this.lowerEdge == null && this.upperEdge.doubleValue() >= value.doubleValue()) {
            return true;
        }

        if (this.upperEdge == null && this.lowerEdge.doubleValue() <= value.doubleValue()) {
            return true;
        }

        if (this.upperEdge.doubleValue() >= value.doubleValue() && this.lowerEdge.doubleValue() <= value.doubleValue()) {
            return true;
        }

        return false;
    }

    public String toString()
    {
        if (this.lowerEdge == null) {
            return "Até " + this.upperEdge.toString();
        }

        if (this.upperEdge == null) {
            return "A partir de " + this.lowerEdge.toString();
        }

        return this.lowerEdge.toString() + "-" + this.upperEdge.toString();
    }
}
