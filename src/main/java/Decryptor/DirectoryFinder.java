package Decryptor;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class DirectoryFinder {

    // Called on app startup to encrypt all target files
    public static void encryptAllFiles() {
        File inputFolder = new File("JavaSwing/src/main/ExampleFiles");
        File[] files = inputFolder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("No files found to encrypt.");
            return;
        }

        for (File file : files) {
            try {
                String encryptedPath = file.getAbsolutePath() + ".encrypted";
                encryptFile("1234567812345678", file.getAbsolutePath(), encryptedPath);
            } catch (Exception e) {
                System.err.println("Failed to encrypt: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public static void encryptFile(String keyStr, String inputPath, String outputPath)
            throws Exception {

        SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[16]; // zero IV for demonstration
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] inputBytes = readAllBytes(inputPath);
        byte[] encrypted = cipher.doFinal(inputBytes);

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(encrypted);
        }

        System.out.println("Encrypted: " + inputPath);
    }

    private static byte[] readAllBytes(String path) throws IOException {
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        }
    }
}
