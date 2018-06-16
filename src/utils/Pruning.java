package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tree.Node;
import models.Sample;

public class Pruning
{
    protected AIAlgorithm algorithm;

    protected Sample trainingSample;
    protected Sample validationSample;
    protected Sample testSample;

    protected Classifier fullClassifier;
    protected double fullClassifierError;

    protected ArrayList<Node> nodes;
    protected ArrayList<Double> errors;

    public Pruning(AIAlgorithm algorithm, Sample sample)
    {
        this.algorithm = algorithm;

        List<Sample> samples = sample.split(3);
        this.trainingSample = samples.get(0);
        this.validationSample = samples.get(1);
        this.testSample = samples.get(2);

        this.algorithm.setSample(trainingSample);
        this.fullClassifier = this.algorithm.getClassifier();
        this.fullClassifierError = this.fullClassifier.error(this.validationSample);

        this.nodes = new ArrayList<Node>();
        this.errors = new ArrayList<Double>();
    }

    public Classifier prune()
    {
        Classifier classifier = new Classifier(this.fullClassifier);
        Node root = classifier.getTree();

        this.evaluate(root);

        System.out.println(this.errors);
        /*

        double overallBestError = this.fullClassifierError;
        int bestErrorIndex = 0;
        double bestError = this.errors.get(0).doubleValue();
        boolean firstRun = true;
        while (overallBestError > bestError || firstRun) {
            firstRun = false;
            for (int i = 1; i < this.errors.size(); i++) {
                double error = this.errors.get(i).doubleValue();
                if (error < bestError) {
                    bestErrorIndex = i;
                    bestError = error;
                }
            }

            Node bestErrorNode = this.nodes.get(bestErrorIndex);
            Node parent = bestErrorNode.getParent();
            parent.remove(bestErrorNode);
            this.nodes.remove(bestErrorIndex);
            this.errors.remove(bestErrorIndex);

            overallBestError = bestError;

            this.evaluate(parent);

            if (this.nodes.size() == 0) {
                break;
            }

            bestErrorIndex = 0;
            bestError = this.errors.get(0).doubleValue();
        }

        */

        return classifier;
    }

    public void evaluate(Node subtree)
    {
        Set<Node> nodes = subtree.getNodes();
        if (nodes == null || nodes.size() == 0) {
            this.evaluateLeafNode(subtree);
        }

        Iterator<Node> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            Node current = iterator.next();
            this.evaluate(subtree);
        }
    }

    protected void evaluateLeafNode(Node leaf)
    {
        Node parent = leaf.getParent();
        parent.remove(leaf);

        double error = this.fullClassifier.error(this.validationSample);
        this.nodes.add(leaf);
        this.errors.add(new Double(error));

        parent.add(leaf);
    }
}
