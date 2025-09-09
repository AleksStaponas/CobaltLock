package NetworkShell;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ServerFirewall {

    public static List<String> readFileInList(String path){
        List<String> lines = Collections.emptyList();

        try {
            lines = Files.readAllLines(
                    Paths.get(path),
                    StandardCharsets.UTF_8);
        } catch(IOException e) {
            System.out.println("IOException "+e);
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {

        String path = "/NetworkShell/IPLogs.json";
        List<String> lines = readFileInList(path);
        
        for (String line : lines) {
            int ipIndex = line.indexOf("\"ip\"");
            if (ipIndex != -1) {
                int colonIndex = line.indexOf(":", ipIndex);
                int commaIndex = line.indexOf(",", colonIndex);
                if (commaIndex == -1) {
                    commaIndex = line.indexOf("}", colonIndex);
                }

                String ipField = line.substring(colonIndex + 1, commaIndex).replace("\"", "").trim();
                System.out.println(ipField);
            }
        }
    }
}
