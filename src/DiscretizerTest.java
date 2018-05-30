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

public class DiscretizerTest
{
    public static final String SEPARATOR = ", ";
    public static final String [] FIELDS = {
        "age",
    };
    public static final String [] CONTINUOUS_FIELDS = {
        "age",
    };

    public static Sample sample = new Sample(DiscretizerTest.FIELDS);

    public static void main(String [] args)
    {
        if (args.length < 1) {
            System.out.println("É necessário informar o caminho do arquivo de dados como parâmetro de execução");
            return;
        }

        DiscretizerTest.readInput(args[0]);
        ID3 builder = new ID3("Age");

        DiscretizerTest.sample = Discretizer.discretizeProperty(DiscretizerTest.sample, DiscretizerTest.CONTINUOUS_FIELDS[0]);

        System.out.println(DiscretizerTest.sample);
    }

    public static void readInput(String filepath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
                DiscretizerTest.sample.addEvent(line.split(DiscretizerTest.SEPARATOR));
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
