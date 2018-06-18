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
        double trainingSampleAccuracy = classifier.accuracy(this.trainingSample);
        double testSampleAccuracy = classifier.accuracy(this.testSample);

        this.trainingAccuracy.add(0, new Double(trainingSampleAccuracy));
        this.testAccuracy.add(0, new Double(testSampleAccuracy));
        this.pruneAccuracy.add(0, new Double(testSampleAccuracy));
    }

    public void afterAddNode(Classifier classifier)
    {
        double trainingSampleAccuracy = classifier.accuracy(this.trainingSample);
        double testSampleAccuracy = classifier.accuracy(this.testSample);

        int nodeCount = classifier.getNodeCount();
        this.trainingAccuracy.add(nodeCount, new Double(trainingSampleAccuracy));
        this.testAccuracy.add(nodeCount, new Double(testSampleAccuracy));
        this.pruneAccuracy.add(nodeCount, new Double(testSampleAccuracy));
    }

    public void afterPrune(Double accuracy, int count)
    {
        this.pruneAccuracy.remove(count);
        this.pruneAccuracy.add(count, accuracy);
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

    public void output()
    {
        for (int i = 0; i < this.trainingAccuracy.size(); i++) {
            Double trainingAccuracy = this.trainingAccuracy.get(i);
            Double testAccuracy = this.testAccuracy.get(i);
            Double pruneAccuracy = this.pruneAccuracy.get(i);
            System.out.println(i + "; "
             + ((trainingAccuracy != null) ? trainingAccuracy : "") + "; "
             + ((testAccuracy != null) ? testAccuracy : "") + "; "
             + ((pruneAccuracy != null) ? pruneAccuracy : "") + "; "
            );
        }
    }
}
