import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.Iterator;
import java.util.List;

import models.Event;
import models.Sample;
import utils.AIAlgorithm;
import utils.Classifier;
import utils.Discretizer;
import utils.ID3;
import utils.Interval;
import utils.Pruning;
import utils.RFoldCrossValidation;

public class Adult
{
    public static final int R = 10;
    public static final double TRUST_INTERVAL_FACTOR = 1.96;
    public static final String SEPARATOR = ", ";
    public static final String [] FIELDS = {
        "age",
        "workclass",
        "fnlwgt",
        "education",
        "education-num",
        "marital-status",
        "occupation",
        "relationship",
        "race",
        "sex",
        "capital-gain",
        "capital-loss",
        "hours-per-week",
        "native-country",
    };
    public static final String [] CONTINUOUS_FIELDS = {
        "age",
        "fnlwgt",
        "education-num",
        "capital-gain",
        "capital-loss",
        "hours-per-week",
    };

    public static Sample sample = new Sample(Adult.FIELDS);

    public static void main(String [] args)
    {
        if (args.length < 1) {
            System.out.println("E necessario informar o caminho do arquivo de dados como parametro de execucao");
            return;
        }

        int linesRead = Adult.readInput(args[0]);

        System.out.println("Linhas lidas no arquivo: " + linesRead);
        System.out.println("Eventos adicionados: " + Adult.sample.size());
        System.out.println("Missing data: " + (linesRead - Adult.sample.size()));

        for (int i = 0; i < Adult.CONTINUOUS_FIELDS.length; i++) {
            Adult.sample = Discretizer.discretizeProperty(Adult.sample, Adult.CONTINUOUS_FIELDS[i]);
        }

        AIAlgorithm id3 = new ID3("Income");

        /*
        System.out.println("*************************************************");
        System.out.println("K-Fold CrossValidation");
        System.out.println("*************************************************");
        RFoldCrossValidation validator = new RFoldCrossValidation(Adult.R, id3, Adult.sample, Adult.TRUST_INTERVAL_FACTOR);
        System.out.println("Erro obtido: " + validator.error());
        System.out.println("Erro estimado no intervalo: " + validator.errorInterval().toString());
        System.out.println("");
        */

        System.out.println("*************************************************");
        System.out.println("Poda");
        System.out.println("*************************************************");
        Pruning pruning = new Pruning(id3, Adult.sample);
        Classifier pruned = pruning.prune();

        pruning.getLogger().output();

        /*
        System.out.println("*************************************************");
        System.out.println("Regras resultantes após poda");
        System.out.println("*************************************************");
        Adult.printRules(pruned);
        */
    }

    public static int readInput(String filepath)
    {
        int linesRead = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
                Adult.sample.addEvent(line.split(Adult.SEPARATOR));
                linesRead++;
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de dados nao encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo de dados");
        }

        return linesRead;
    }

    public static void printRules(Classifier built)
    {
        List<String> rules = built.getRules();
        Iterator<String> iterator = rules.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
