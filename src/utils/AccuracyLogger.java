package utils;

import java.lang.Double;
import java.util.ArrayList;
import java.util.List;

import models.Sample;

public class AccuracyLogger implements Logger
{
    protected List<Double> trainingAccuracy;
    protected List<Double> testAccuracy;
    protected List<Double> pruneAccuracy;
    protected Sample trainingSample;
    protected Sample testSample;

    public AccuracyLogger(Sample trainingSample, Sample testSample)
    {
        this.trainingSample = trainingSample;
        this.testSample = testSample;
        this.trainingAccuracy = new ArrayList<Double>();
        this.testAccuracy = new ArrayList<Double>();
        this.pruneAccuracy = new ArrayList<Double>();
    }

    public void onInit(Classifier classifier)
    {
        this.trainingAccuracy.add(0, new Double(classifier.accuracy(this.trainingSample)));
        this.testAccuracy.add(0, new Double(classifier.accuracy(this.testSample)));
    }

    public void afterAddNode(Classifier classifier)
    {
        int nodeCount = classifier.getNodeCount();
        this.trainingAccuracy.add(nodeCount, new Double(classifier.accuracy(this.trainingSample)));
        this.testAccuracy.add(nodeCount, new Double(classifier.accuracy(this.testSample)));
    }

    public List<Double> getTrainingAccuracy()
    {
        return this.trainingAccuracy;
    }

    public List<Double> getTestAccuracy()
    {
        return this.testAccuracy;
    }

    public List<Double> getPruneAccuracy()
    {
        return this.pruneAccuracy;
    }
}
