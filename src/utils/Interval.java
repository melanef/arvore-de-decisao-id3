import java.lang.Integer;

package utils;
public class Interval
{
    protected Integer lowerEdge;
    protected Integer upperEdge;

    public Interval(Integer lowerEdge, Integer upperEdge)
    {
        this.lowerEdge = lowerEdge;
        this.upperEdge = upperEdge;
    }

    public boolean contains(Integer value) {
        if (value == NULL) {
            return false;
        }

        if (this.lowerEdge == NULL && this.upperEdge.intValue() >= value.intValue()) {
            return true;
        }

        if (this.upperEdge == NULL && this.lowerEdge.intValue() <= value.intValue()) {
            return true;
        }

        if (this.upperEdge.intValue() >= value.intValue() && this.lowerEdge.intValue() <= value.intValue()) {
            return true;
        }

        return false;
    }

    public String toString()
    {
        if (this.lowerEdge == NULL) {
            return "Até " + this.upperEdge.toString();
        }

        if (this.upperEdge == NULL) {
            return "A partir de " + this.lowerEdge.toString();
        }

        return this.lowerEdge.toString() + "-" + this.upperEdge.toString();
    }
}
