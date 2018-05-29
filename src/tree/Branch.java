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

    public Node getNode()
    {
        return this.node;
    }

    public String getMajorCategory()
    {
        return this.sample.getMajorCategory();
    }

    public String getValue()
    {
        return this.value;
    }
}
