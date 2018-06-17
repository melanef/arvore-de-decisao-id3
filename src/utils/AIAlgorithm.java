package utils;

import models.Sample;

public interface AIAlgorithm
{
    public void setLogger(Logger logger);
    public void setSample(Sample sample);
    public void init();
    public Classifier getClassifier();
}
