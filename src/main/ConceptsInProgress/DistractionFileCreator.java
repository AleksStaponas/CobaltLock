
//Disclamer!
//To prevent any harm to your machine use this code in a safe/testing environment only.
//I do not take any liability for the codes use.

//Usage
//Only run this code in compliance with local laws and on systems you own or are authorized to

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DirAndFileCreator {

    public static String randomHex(int length) {
        String chars = "0123456789abcdef";
        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars.charAt(r.nextInt(chars.length())));
        }
        return stringBuilder.toString();
    }

    private static void branchCreator(Path basePath, int depth, ExecutorService executorService){
        if (depth > 7) return;

        try{
            Random random = new Random();
            int fileNum = random.nextInt(7) + 1;
            for (int frp = 0; frp < fileNum; frp++) {
                String fileName = randomHex(16) + ".encrypted";
                Path testFile = basePath.resolve(fileName);

                Files.createFile(testFile);
                setRandomFileTime(testFile);
                System.out.println("File created: " + testFile);

                try (FileWriter myWriter = new FileWriter(testFile.toFile())) {
                    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                    int num = random.nextInt(30);
                    for (; num > 0; num--) {
                        for (int i = 0; i < 64; i++) {
                            myWriter.write(chars.charAt(random.nextInt(chars.length())));
                        }
                    }
                }
            }

            int dirNum = new Random().nextInt(3) + 1;
            for (int i = 0; i < dirNum; i++) {
                String dirName = randomHex(16) + ".encrypted";
                Path subDir = basePath.resolve(dirName);
                Files.createDirectory(subDir);
                setRandomFileTime(subDir);
                System.out.println("Directory created: " + subDir);

                branchCreator(subDir, depth + 1, executorService);
            }

        } catch (Exception exceptions) {
            throw new RuntimeException(exceptions);
        }
    }

    private static void setRandomFileTime(Path path) throws Exception{
        Random random = new Random();
        FileTime randomTime = FileTime.from(
                Instant.now().minusSeconds(random.nextInt(60 * 60 * 24 * 365))
        );
        BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        view.setTimes(randomTime, randomTime, randomTime);
    }

    private static void printTree(Path path, boolean isLast) {

        if (Files.isDirectory(path)) {
            try (var stream = Files.list(path).sorted()) {
                var children = stream.toList();
                for (int i = 0; i < children.size(); i++) {
                    printTree(children.get(i), i == children.size() - 1);
                }
            } catch (IOException e) {
                System.out.println("Error printing tree: " + e);
            }
        }
    }

    public static void shutdown(){
        System.out.println("Process complete");
    }

    public static void DirectoryCreator(){

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Path base = Paths.get("distractionFiles");
        try {
            if (!Files.exists(base)) {
                Files.createDirectory(base);
            }
        } catch (IOException e) {
            System.out.println("Error creating base folder: " + e.getMessage());
        }

        for (int rp = 0; rp < 15; rp++) {

            executor.submit(() -> {
                Random dir = new Random();
                String dirChars = "0123456789abcdef";
                StringBuilder name = new StringBuilder();

                for (int i = 0; i < 16; i++) {
                    name.append(dirChars.charAt(dir.nextInt(dirChars.length())));
                }

                Path path = base.resolve(name + ".encrypted"); 
                
                try {
                    Files.createDirectory(path);
                    System.out.println("Directory created " + path);
                    setRandomFileTime(path);

                    branchCreator(path, 0, executor);

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

                        Path testFile = path.resolve(fileName.toString());

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

                        myWriter.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error " + e);
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException exception) {
            System.out.println("Error "+exception);
        }

        try{
            Files.list(base)
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().endsWith(".encrypted"))
                    .forEach(root -> printTree(root,  true));
        } catch (IOException e){
            System.out.println("Error creating files"+e.getMessage());
        }

        while (!executor.isTerminated()){
            System.out.println("Thread "+executor+"complete");
        }

        shutdown();

    }

    public static void main(String[] args) {

        long start = System.nanoTime();

        DirectoryCreator();

        long finish = System.nanoTime();
        double timeElapsed = (finish - start)/ 1_000_000_000.0;

        System.out.println("Time taken "+timeElapsed+" seconds");
    }
}
