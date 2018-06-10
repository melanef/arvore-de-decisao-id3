import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;

import models.Event;
import models.Sample;
import utils.Discretizer;
import utils.ID3;
import utils.Interval;
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
            System.out.println("É necessário informar o caminho do arquivo de dados como parâmetro de execução");
            return;
        }

        int linesRead = Adult.readInput(args[0]);

        System.out.println("Linhas lidas no arquivo: " + linesRead);
        System.out.println("Eventos adicionados: " + Adult.sample.size());
        System.out.println("Missing data: " + (linesRead - Adult.sample.size()));

        for (int i = 0; i < Adult.CONTINUOUS_FIELDS.length; i++) {
            Adult.sample = Discretizer.discretizeProperty(Adult.sample, Adult.CONTINUOUS_FIELDS[i]);
        }

        RFoldCrossValidation validator = new RFoldCrossValidation(Adult.R, new ID3("Income"), Adult.sample, Adult.TRUST_INTERVAL_FACTOR);
        System.out.println("Erro obtido: " + validator.error());
        System.out.println("Erro estimado no intervalo: " + validator.errorInterval().toString());
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
            System.out.println("Arquivo de dados não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo de dados");
        }

        return linesRead;
    }

    public static void printRules(ID3 built)
    {
        ArrayList<String> rules = built.getRules();
        Iterator<String> iterator = rules.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
