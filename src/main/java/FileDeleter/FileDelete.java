package FileDeleter;

import java.io.File;

//file deleter method
public class FileDelete {
    public static void main(String[] args) {
       FileDeleter();
    }
    public static void FileDeleter() {
        File dir = new File("JavaSwing/src/main/ExampleFiles");
        File[] files = dir.listFiles();

        if (files == null) {
            System.out.println("No encrypted files found.");
            return;
        }

        for (File file : files) {
            if (file.getName().endsWith(".encrypted")) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName());
                } else {
                    System.err.println("Failed to delete: " + file.getName());
                }
            }
        }

    }
}
