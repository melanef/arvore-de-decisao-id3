import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.Iterator;
import java.util.List;

import models.Event;
import models.Sample;
import utils.ID3;
import utils.Classifier;

public class PlayTennis
{
    public static final String SEPARATOR = ", ";
    public static final String [] FIELDS = {
        "tempo",
        "temperatura",
        "umidade",
        "vento",
    };

    public static Sample sample = new Sample(PlayTennis.FIELDS);

    public static void main(String [] args)
    {
        if (args.length < 1) {
            System.out.println("É necessário informar o caminho do arquivo de dados como parâmetro de execução");
            return;
        }

        PlayTennis.readInput(args[0]);
        ID3 builder = new ID3("PlayTennis");
        builder.setSample(PlayTennis.sample);
        Classifier classifier = builder.getClassifier();
        PlayTennis.printRules(classifier);
    }

    public static void readInput(String filepath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
                PlayTennis.sample.addEvent(line.split(PlayTennis.SEPARATOR));
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de dados não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo de dados");
        }
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
