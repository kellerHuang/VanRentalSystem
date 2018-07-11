import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class VanRentalSystem {
    public VanRentalSystem() {
    }

    public static void main(String[] args) {
        ArrayList<String> input = new ArrayList();
        new ArrayList();
        new ArrayList();
        new ArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            Throwable var6 = null;

            try {
                String line;
                while((line = reader.readLine()) != null) {
                    input.add(line);
                }

                reader.close();
            } catch (Throwable var17) {
                var6 = var17;
                throw var17;
            } finally {
                if (reader != null) {
                    if (var6 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var16) {
                            var6.addSuppressed(var16);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (FileNotFoundException var19) {
            var19.printStackTrace();
        } catch (IOException var20) {
            var20.printStackTrace();
        }

        ReadToCommand reader = new ReadToCommand();
        ArrayList<Campervan> campervans = reader.findCampervans(input);
        ArrayList<Depot> depots = reader.compileDepots(campervans);
        reader.findCommands(input, campervans, depots);
    }
}
