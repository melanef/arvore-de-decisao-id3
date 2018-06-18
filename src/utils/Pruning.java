package utils;

import java.util.Arrays;
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
    protected AccuracyLogger logger;

    public Pruning(AIAlgorithm algorithm, Sample sample)
    {
        this.algorithm = algorithm;

        List<Sample> samples = sample.split(3);
        this.trainingSample = samples.get(0);
        this.validationSample = samples.get(1);
        this.testSample = samples.get(2);

        this.logger = new AccuracyLogger(this.trainingSample, this.testSample);

        this.algorithm.setSample(trainingSample);
        this.algorithm.setLogger(this.logger);
        this.algorithm.init();
        this.fullClassifier = this.algorithm.getClassifier();

        this.fullClassifierError = this.fullClassifier.error(this.validationSample);

        System.out.println("Erro base: " + this.fullClassifierError);

        this.nodes = new ArrayList<Node>();
        this.errors = new ArrayList<Double>();
    }

    public Classifier prune()
    {
        System.out.println("Iniciando poda");

        Classifier classifier = new Classifier(this.fullClassifier);
        Node root = classifier.getTree();
        int classifierNodeCount = classifier.getNodeCount();

        double overallBestError = this.fullClassifierError;

        this.logger.afterPrune(classifier.accuracy(this.testSample), classifierNodeCount);

        this.evaluate(root, classifier);

        System.out.println("Erros calculados");

        int bestErrorIndex = 0;
        double bestError = this.errors.get(0).doubleValue();
        while (true) {
            for (int i = 1; i < this.errors.size(); i++) {
                double error = this.errors.get(i).doubleValue();
                if (error < bestError) {
                    bestErrorIndex = i;
                    bestError = error;
                }
            }

            if (bestError >= overallBestError) {
                System.out.println("Best error: " + bestError + " -- Overall Best Error: " + overallBestError);
                break;
            }

            overallBestError = bestError;

            Node bestErrorNode = this.nodes.get(bestErrorIndex);
            Node parent = bestErrorNode.getParent();
            parent.remove(bestErrorNode);
            classifierNodeCount--;

            this.nodes = new ArrayList<Node>();
            this.errors = new ArrayList<Double>();

            this.logger.afterPrune(classifier.accuracy(this.testSample), classifierNodeCount);

            this.evaluate(root, classifier);

            if (this.nodes.size() == 0) {
                break;
            }

            bestErrorIndex = 0;
            bestError = this.errors.get(0).doubleValue();
        }

        return classifier;
    }

    public void evaluate(Node subtree, Classifier classifier)
    {
        Set<Node> nodes = subtree.getNodes();
        if (nodes == null || nodes.size() == 0) {
            this.evaluateLeafNode(subtree, classifier);
        }

        Object childrenNodes[] = nodes.toArray();
        for (int i = 0; i < childrenNodes.length; i++) {
            Node node = (Node) childrenNodes[i];
            this.evaluate(node, classifier);
        }
    }

    protected void evaluateLeafNode(Node leaf, Classifier classifier)
    {
        Node parent = leaf.getParent();
        parent.remove(leaf);

        double error = classifier.error(this.validationSample);

        this.nodes.add(leaf);
        this.errors.add(new Double(error));

        parent.add(leaf);
    }

    public AccuracyLogger getLogger()
    {
        return this.logger;
    }
}
