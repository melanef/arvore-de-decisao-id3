package tree;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Event;
import models.Sample;

public class Branch
{
    protected String value;
    protected Sample sample;
    protected Node node;

    public Branch(String value, Sample sample, Node node)
    {
        this.value = value;
        this.sample = new Sample(sample);
        this.node = node;
    }

    public Branch(Branch branch)
    {
        this.value = branch.getValue();
        this.sample = branch.getSample();
        this.node = branch.getNode();
    }

    public Node getNode()
    {
        if (this.node == null) {
            return null;
        }

        return new Node(this.node);
    }

    public String getMajorCategory()
    {
        return new String(this.sample.getMajorCategory());
    }

    public String getValue()
    {
        return new String(this.value);
    }

    public Sample getSample()
    {
        return new Sample(this.sample);
    }
}
