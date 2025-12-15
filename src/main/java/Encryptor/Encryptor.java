package Encryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

//Discovers files to encrypt

public class Encryptor {

    public static void main(String[] args) {
        File f = null;
        String paths[];
        f = new File("CobaltLock/src/main/ExampleFiles");
        try {
            encryptedFile(
                    "1234567812345678",  // 16-byte AES key
                    Arrays.toString(paths = f.list()),
                    "CobaltLock/src/main/ExampleFiles"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptedFile(String secretKey,String fileInputPath, String fileOutPath)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
            IllegalBlockSizeException, BadPaddingException {
        var key = new SecretKeySpec(secretKey.getBytes(), "AES");
        var cypher = Cipher.getInstance("AES");
        cypher.init(Cipher.ENCRYPT_MODE, key);

        var fileInput = new File("CobaltLock/src/main/ExampleFiles");
        var inputStream = new FileInputStream(fileInput);
        var inputBytes = new byte[(int) fileInput.length()];
        inputStream.read(inputBytes);

        var outputBytes = cypher.doFinal(inputBytes);

        var fileEncryptOut = new File(fileOutPath);
        var outputStream = new FileOutputStream(fileEncryptOut);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

        System.out.println("File successfully encrypted!");
        System.out.println("new file: "+ fileOutPath);
    }

}