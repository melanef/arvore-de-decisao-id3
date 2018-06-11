package utils;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Event;
import models.Sample;
import utils.Interval;

public class RFoldCrossValidation
{
    protected AIAlgorithm algorithm;
    protected Sample sample;
    protected int r;
    protected double trustIntervalFactor;
    protected double error;
    protected boolean isErrorCalculated = false;

    public RFoldCrossValidation(int r, AIAlgorithm algorithm, Sample sample, double trustIntervalFactor)
    {
        this.r = r;
        this.algorithm = algorithm;
        this.sample = new Sample(sample);
        this.trustIntervalFactor = trustIntervalFactor;
    }

    public double error()
    {
        if (!this.isErrorCalculated) {
            this.calculateError();
            this.isErrorCalculated = true;
        }

        return this.error;
    }

    protected void calculateError()
    {
        this.error = 0.0;
        List<Sample> samples = this.sample.split(this.r);

        List<Classifier> classifiers = new ArrayList<Classifier>(this.r);
        for (int i = 0; i < this.r; i++) {
            Sample validation = samples.get(i);
            Sample current = this.sample.getComplement(validation);
            this.algorithm.setSample(current);
            Classifier classifier = this.algorithm.getClassifier();
            error = error + classifier.error(validation);
        }

        this.error = error / 10.0;
    }

    public double stimateError()
    {
        double error = this.error();
        return Math.sqrt((error * (1.0 - error)) / (new Integer(this.sample.size())).doubleValue());
    }

    public Interval errorInterval()
    {
        double error =this.error();
        double stimateError = this.stimateError();

        return new Interval((error - (this.trustIntervalFactor * stimateError)), (error + (this.trustIntervalFactor * stimateError)));
    }
}
