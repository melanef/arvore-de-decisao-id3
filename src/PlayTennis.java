import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;

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
    public static ArrayList<String []> grossData = new ArrayList<String[]>();
    public static ArrayList<Event> sample = new ArrayList<Event>();

    public static void main(String [] args)
    {
        if (args.length < 1) {
            System.out.println("É necessário informar o caminho do arquivo de dados como parâmetro de execução");
            return;
        }

        Main.readInput(args[0]);
    }

    public static void readInput(String filepath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            String [] parts;
            while ((line = reader.readLine()) != null) {
                parts = line.split(Main.SEPARATOR);

                Main.grossData.add(parts);
                //Main.addToSample(parts);
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
        if (Main.isValidEvent(eventData)) {
            Main.sample.add(Main.createEvent(eventData));
        }
    }

    public static boolean isValidEvent(String [] eventData)
    {
        if (eventData.length < 15) {
            return false;
        }

        for (int i = 0; i < eventData.length; i++) {
            if (eventData[i].equals("?")) {
                return false;
            }
        }

        return true;
    }

    public static Event createEvent(String [] eventData)
    {

    }
}
