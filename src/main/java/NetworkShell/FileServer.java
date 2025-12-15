/**
 * ============================================
 * DISCLAIMER / LEGAL NOTICE
 * ============================================
 *
 * This software is provided strictly for educational purposes, ethical testing,
 * and controlled lab environments. It is intended to demonstrate secure file 
 * serving concepts, logging, and basic TLS communication.
 *
 * DO NOT use this software on public networks, against systems you do not 
 * own, or in any way that violates local laws or regulations. Unauthorized 
 * use may be illegal and could result in criminal or civil penalties.
 *
 * The author assumes no liability for any damage, data loss, or legal consequences 
 * arising from the use or misuse of this software.
 *
 * By using this code, you agree to use it responsibly, ethically, and legally.
 *
 * ============================================
 */

package NetworkShell;

import com.moandjiezana.toml.Toml;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.net.Socket;
import java.time.LocalDate;

public class FileServer {

    private static final String Base_DIR = "/ShellSandbox";

    public static long dataCheck(){
        try {
            Toml toml = new Toml().read(new File("config.toml"));
            long shellConnector = toml.getTable("ports").getLong("file_server");
            System.out.println("Set port:" + shellConnector);
            return shellConnector;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            System.out.println("No set port using default port 4444");
            return 4444;
        }
    }

    public static void main(String[] args) throws Exception {

        long lPort = dataCheck();
        int port = (int)lPort;

        char[] password = "password".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fileInputStream = new FileInputStream("/NetworkShell/server.keystore")) {
            keyStore.load(fileInputStream, password);
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, password);

        //protocol used
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        //server port
        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        System.out.println("TLS File server started on port 4444");

        while (true) {
            SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
            new Thread(() -> handleRequest(clientSocket)).start();
        }
    }

    private static void handleRequest(Socket clientSocket) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String filename = in.readLine();
            File file = new File(Base_DIR,filename).getCanonicalFile();
            File base = new File(Base_DIR).getCanonicalFile();

            if (!file.getPath().startsWith(base.getPath())) {
                clientSocket.close();

            } else {

                LocalDate date = LocalDate.now();
                String ip = clientSocket.getInetAddress().getHostAddress();
                String fileRequested = filename;
                Boolean connected = false;

                String clientIP = clientSocket.getInetAddress().getHostAddress();

                if (!file.getPath().startsWith(base.getPath() + File.separator)) {
                    EventLogger(LocalDate.now().toString(), ip, false, filename);
                    System.out.println("Blocked path traversal attempt: " + filename + " from " + ip);
                    clientSocket.close();
                    return;
                }

                if (!file.getPath().startsWith(base.getPath())) {
                    EventLogger(LocalDate.now().toString(), ip, false, fileRequested);
                    clientSocket.close();
                    return;
                }

                if (file.exists() && !file.isDirectory()) {
                    connected = true;
                    EventLogger(LocalDate.now().toString(), ip, connected, fileRequested);
                    System.out.println("Sending file: " + filename +" to "+clientIP);
                    sendFile(out, file);
                } else {
                    connected = false;
                    EventLogger(LocalDate.now().toString(), ip, connected, fileRequested);
                    System.out.println("Not existing file IP:"+clientIP);
                    out.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void EventLogger(String date, String ip, boolean connected, String fileRequested) {
        File logFile = new File("/NetworkShell/IPLogs.json");
        try {
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
            }

            //Implemented logging
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
                bw.write("{"
                        + "\"date\":\"" + date + "\","
                        + "\"ip\":\"" + ip + "\","
                        + "\"event\":\"" + connected + "\","
                        + "\"fileRequested\":\"" + fileRequested + "\""
                        + "}");
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("An error occurred in EventLogger.");
            e.printStackTrace();
        }
    }

    private static void sendFile(OutputStream out, File file) throws IOException {
        try (BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            System.out.println("File: "+file+" has been sent");
        }
    }
}