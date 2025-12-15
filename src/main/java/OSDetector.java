import NetworkShell.NetworkConfigure;

import java.lang.management.ManagementFactory;
import java.util.Scanner;

public class OSDetector {
    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("This script will detect the operating system of the machine and show the correct screen");

        System.out.println("Detected OS:"+ManagementFactory.getOperatingSystemMXBean().getName());
        String detectOs = ManagementFactory.getOperatingSystemMXBean().getName();

        System.out.println("Enter 1 to configure network settings. Enter 2 to skip. Enter 3 to quit");

        int option = scanner.nextInt();

        if (option == 1){
            NetworkConfigure.main(args);
        } else if (option == 2) {
            System.out.println();
        } else if (option == 3) {
            System.exit(0);
        }

        if (detectOs.contains("Windows")) {
            System.out.println("Windows system detected");
            Windows10FakeUpdate.main(args);
        } else {

            System.out.println("Using linux screen");
            GrubLoader.main(args);
        }
    }
}
