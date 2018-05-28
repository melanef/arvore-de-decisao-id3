import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;

import models.Event;
import utils.ID3;

public class PlayTennis
{
    public static final String SEPARATOR = ", ";
    public static final String [] FIELDS = {
        "tempo",
        "temperatura",
        "umidade",
        "vento",
    };
    public static ArrayList<String []> grossData = new ArrayList<String[]>();
    public static ArrayList<Event> sample = new ArrayList<Event>();

    public static void main(String [] args)
    {
        if (args.length < 1) {
            System.out.println("É necessário informar o caminho do arquivo de dados como parâmetro de execução");
            return;
        }

        PlayTennis.readInput(args[0]);
        ID3 builder = new ID3("PlayTennis");
        builder.setSample(PlayTennis.sample);
        builder.buildTree();
        PlayTennis.printRules(builder);
    }

    public static void readInput(String filepath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            String [] parts;
            while ((line = reader.readLine()) != null) {
                parts = line.split(PlayTennis.SEPARATOR);

                PlayTennis.grossData.add(parts);
                PlayTennis.addToSample(parts);
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de dados não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo de dados");
        }
    }

    public static void addToSample(String [] eventData)
    {
        if (PlayTennis.isValidEvent(eventData)) {
            PlayTennis.sample.add(Event.createEvent(eventData, PlayTennis.FIELDS));
        }
    }

    public static boolean isValidEvent(String [] eventData)
    {
        if (eventData.length < (PlayTennis.FIELDS.length + 1)) {
            return false;
        }

        for (int i = 0; i < eventData.length; i++) {
            if (eventData[i].equals("?")) {
                return false;
            }
        }

        return true;
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
