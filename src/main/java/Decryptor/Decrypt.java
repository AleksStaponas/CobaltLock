package Decryptor;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

//discovering and decrypting all discovered files

public class Decrypt {

    public static void decryptAllFilesInDirectory(String keyStr, String inputDir, String outputDir) {
        File dir = new File(inputDir);
        File[] files = dir.listFiles();

        if (files == null) {
            System.out.println("No encrypted files found.");
            return;
        }

        for (File file : files) {
            if (file.getName().endsWith(".encrypted")) {
                try {
                    String outputFileName = file.getName().replace(".encrypted", ".decrypted.txt");
                    String outputPath = outputDir + "/" + outputFileName;
                    decryptFile(keyStr, file.getAbsolutePath(), outputPath);
                } catch (Exception e) {
                    System.err.println("Failed to decrypt: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void decryptFile(String keyStr, String inputPath, String outputPath)
            throws Exception {

        SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[16]; // zero IV (must match encryption)
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] encryptedData = readAllBytes(inputPath);
        byte[] decrypted = cipher.doFinal(encryptedData);

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(decrypted);
        }

        System.out.println("Decrypted: " + outputPath);
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
