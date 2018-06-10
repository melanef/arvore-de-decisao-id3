package utils;

import models.Sample;

public interface AIAlgorithm
{
    public void setSample(Sample sample);
    public Classifier getClassifier();
}
