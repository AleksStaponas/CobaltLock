package NetworkShell;

import com.moandjiezana.toml.Toml;

import java.io.File;

public class tomlFinder {
    public static void main(String[] args) {

        //Toml data finder
        Toml toml = new Toml().read(new File("config.toml"));
        long fileServerPort = toml.getTable("ports").getLong("file_server");
        System.out.println(fileServerPort);

    }
}
