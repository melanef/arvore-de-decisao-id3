import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;

import models.Event;
import models.Sample;
import utils.ID3;

public class Adult
{
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

        Adult.readInput(args[0]);
        ID3 builder = new ID3("Income");

        /*
        for (int i = 0; i < Adult.CONTINUOUS_FIELDS.length; i++) {
        */
        int i = 0;
            Adult.sample = Discretizer.discretize(Adult.sample, Adult.CONTINUOUS_FIELDS[i]);
        /*
        }
        */

        System.out.println(sample);

        /*
        builder.setSample(Adult.sample);
        builder.buildTree();
        Adult.printRules(builder);
        */
    }

    public static void readInput(String filepath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
                Adult.sample.addEvent(line.split(Adult.SEPARATOR));
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de dados não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo de dados");
        }
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
