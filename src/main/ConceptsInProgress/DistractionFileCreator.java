
//Disclamer!
//To prevent any harm to your machine use this code in a safe/testing environment only.
//I do not take any liability for the codes use.

//Usage
//Only run this code in compliance with local laws and on systems you own or are authorized to

import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.file.*;

import java.nio.file.attribute.BasicFileAttributeView;

import java.nio.file.attribute.FileTime;

import java.time.Instant;

import java.util.Random;

public class DistractionFileCreator {

// Write random bytes into a file to mimic encrypted content

public static void writeRandomBytes(Path file, int minBlocks, int maxBlocks) throws IOException {

    Random random = new Random();

    int blocks = random.nextInt(maxBlocks - minBlocks + 1) + minBlocks;

    try (FileOutputStream fos = new FileOutputStream(file.toFile())) {

        for (int i = 0; i < blocks; i++) {

            byte[] randomBytes = new byte[64]; // 64 bytes per block

            random.nextBytes(randomBytes);

            fos.write(randomBytes);

        }

    }

}



// Set random file creation/modification times

private static void setRandomFileTime(Path path) throws Exception {

    Random random = new Random();

    FileTime randomTime = FileTime.from(

            Instant.now().minusSeconds(random.nextInt(60 * 60 * 24 * 365))

    );

    BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);

    view.setTimes(randomTime, randomTime, randomTime);

}



public static void shutdown() {

    System.out.println("Process complete!");

    System.exit(0);

}



public static void DirectoryCreator() {

    Random rand = new Random();



    for (int rp = 0; rp < 15; rp++) {

        // Safe characters for Windows folder names

        String dirChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder name = new StringBuilder();

        for (int i = 0; i < 16; i++) {

            name.append(dirChars.charAt(rand.nextInt(dirChars.length())));

        }



        Path path = Paths.get(name + ".encrypted");



        try {

            Files.createDirectory(path);

            System.out.println("Directory created: " + path);

            setRandomFileTime(path);



            int fnum = rand.nextInt(20); // number of files per directory



            for (int frp = 0; frp < fnum; frp++) {

                // Generate safe file name

                StringBuilder fileName = new StringBuilder();

                for (int i = 0; i < 16; i++) {

                    fileName.append(dirChars.charAt(rand.nextInt(dirChars.length())));

                }

                fileName.append(".encrypted");



                Path testFile = Paths.get(path.toString(), fileName.toString());

                Files.createFile(testFile);

                setRandomFileTime(testFile);

                System.out.println("File created: " + testFile);



                // Fill the file with random bytes

                writeRandomBytes(testFile, 5, 30); // 5-30 blocks of 64 bytes

            }



        } catch (Exception e) {

            System.out.println("Error: " + e);

        }

    }

    shutdown();

}



public static void main(String[] args) {

    DirectoryCreator();

}
}  



