package Decryptor;

//Checking if Input is equal to the code

public class PaymentCode {

    private static final String VALID_CODE = "123";

    public static boolean isValidCode(String input) {
        if (input == null) {
            System.out.println("Incorrect code");
            return false;
        }

        if (input.equals(VALID_CODE)) {
            try {
                Decrypt.decryptAllFilesInDirectory(
                        "1234567812345678",
                        "CobaltLock/src/main/ExampleFiles",
                        "JavaSwing/src/main/DecryptedFiles"
                );
                System.out.println("Decryption complete!");
            } catch (Exception ex) {
                System.err.println("Exception occurred: " + ex.getMessage());
                System.out.println("Decryption failed");
            }
            return true;
        } else {
            System.out.println("Invalid code.");
            return false;
        }
    }
}
