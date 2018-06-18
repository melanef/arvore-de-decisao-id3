package utils;

public interface Logger
{
    public void onInit(Classifier classifier);
    public void afterAddNode(Classifier classifier);
    public void output();
}
