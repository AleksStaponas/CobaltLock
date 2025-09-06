## Legal Disclaimer

This software is provided **strictly for educational, research, and ethical practice purposes only**.

-  **Do NOT use this tool on any system you do not own or have explicit permission to test.**
-  **Unauthorized use of this software may be illegal and is strictly prohibited.**
-  **The author takes no responsibility for any damage, data loss, or legal consequences resulting from misuse.**

By using this software, you agree to use it **ethically, lawfully**, and in **controlled environments such as CTFs, virtual labs, or testing networks**.

This tool is intended to help raise awareness and understanding of security threats — **not to promote or enable criminal activity**.

## Safety Features

- **Safe Decrypt Option:** Instantly restores files to prevent damage during testing.  
- **Reverse Shell:** Runs via Windows CMD for controlled simulation and has a connector for testing.  
- **Limited Impact:** Only files in a safe test folder are affected.
- **Sandboxed Directory** Preventing any other files being accessed in the file server.

## Windows10FakeUpdate  
A fake Windows update screen used as a distraction that encrypts discovered files and estimates a time so it is ready and not as alarming to users. Furthermore, this behaviour can often be related to other malware types, such as viruses, that use that time to spread through vulnerabilities such as zero-days, or even install rootkits onto a compromised machine.

<p align="center">
  <img src="images/Windows10FakeUpdate.JPG" alt="WindowsFakeUpdate" />
</p>

## InitialPhase  
Initial lock screen phase with timer and shell connection. This is demonstrated as it is often used in real-world TTPs (Tactics, Techniques, and Procedures), and is commonly shown in attackers' OPSEC operations, which they can use to further escalate privileges or obfuscate logs.

<p align="center">
  <img src="images/FileLocker_BluePhase.JPG" alt="FirstPhase" />
</p>

## SecondPhase  
Critical phase with timer and increased demands, file deletion if requirements are met with simulated payment code. If demands are not met, this ethical ransomware runs a wipedown where the user's encrypted files are deleted, simulating similar behaviours to the NotPetya ransomware that would delete a compromised machine's files. In this case, it is simulated and only targets files in a specific directory.  

<p align="center">
  <img src="images/FileLocker_RedPhase.JPG" alt="SecondPhase" />
</p>

# Updates
Cross-platform server with automated command deployment and privilege escalation support for Windows and Linux using LinPEAS and WinPEAS.

## Features included in dir & file creator prototype

- directory and file creation with random dates and times within a year period.
- multi threading added to improve speed
<details>
<summary>Show/Hide Code</summary>

```java

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

```
</details> 

# Current isues
The current file discovery process fails when the specified path includes a directory. Once it encounters a directory, it is unable to encrypt any files contained within its subdirectories.

## Current file discovery
<p align="center">
  <img src="images/Diagrams/CurrentFileDiscoverer.png" alt="CurrentFileDiscovery" />
</p>

## Updated file discovery

<p align="center">
  <img src="images/Diagrams/ImprovedFileDiscoverer.png" alt="UpdatedFileDiscovery" />
</p>
