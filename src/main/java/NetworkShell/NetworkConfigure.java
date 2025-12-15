package NetworkShell;

import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NetworkConfigure {

    public static boolean ConfigFileCreator() {
        try {
            File config = new File("config.toml");
            if (config.createNewFile()) {
                System.out.println("Config file created");
            } else {
                System.out.println("File already exists");
            }
            return true;

        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();

            return false;
        }
    }

    public static void FileHandler(int option) {

        //port file writer
        if (option == 1) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter device ip address");
            String SIP = scanner.next();

            System.out.println("Enter desired port for the connector");
            String SCP = scanner.next();
            if (!isValidPort(SCP)) return;

            System.out.println("Enter desired port for the listener");
            String SLP = scanner.next();
            if (!isValidPort(SLP)) return;

            System.out.println("Enter desired port for the file server");
            String SFS = scanner.next();
            if (!isValidPort(SFS)) return;

            Map<String, Object> data = new HashMap<>();
            Map<String, Object> ports = new HashMap<>();
            Map<String, Object> ip = new HashMap<>();

            ip.put("ip", SIP);
            ports.put("connector", Integer.parseInt(SCP));
            ports.put("listener", Integer.parseInt(SLP));
            ports.put("file_server", Integer.parseInt(SFS));

            data.put("ports",ports);
            data.put("ip",ip);

            try {
                TomlWriter writer = new TomlWriter();
                writer.write(data, new File("config.toml"));
                System.out.println("All ports saved to config.toml");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isValidPort(String EnteredPort){
        try {
             int port = Integer.parseInt(EnteredPort);

            if (port >= 0 && port <= 65535) {
                System.out.println("Valid port");
                return true;

            } else {
                System.out.println("Invalid port number");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Invalid port");
            return false;
        }
    }

    public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    System.out.println("""
    This is the network configurer for the:
    - Shell Connector
    - Shell Listener
    - File Server
    """);

    ConfigFileCreator();
    System.out.println("This will overwrite your current or previous file to set ports if ran again");
    System.out.println("""
    - Enter 1 to overwrite/set ports & device IP address
    - Enter 2 to backup file as config.toml.bak
    - Enter 3 to quit
    """);

    int option = scanner.nextInt();

    if (option == 1) {
        FileHandler(1);
    }
    else if (option == 2){

        String file = "config.toml";
        String bakFile = "config.toml.bak";

        try(FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(bakFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }

            System.out.println("Backup file created successfully as config.toml.bak (backup file will not be overwritten)");

            } catch (Exception e) {
            throw new RuntimeException(e);
            }
        } else {
            System.out.println("Exiting...");
            System.exit(0);
       }
    }
}
