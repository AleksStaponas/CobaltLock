import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.*;

public class GUIDemo {

    private JFrame frame;
    private JTextField input;
    private JLabel label;
    private JButton button1;
    private JButton button2;
    private int width;
    private int height;

    public GUIDemo(int w, int h){
        frame = new JFrame();
        label = new JLabel("Hello");
        input = new JTextField(10);
        button1 = new JButton("Button 1");
        button2 = new JButton("Button 2");
        width = w;
        height = h;
    }

    public void setUpGUI(){
        Container cp = frame.getContentPane();
        FlowLayout flow = new FlowLayout();
        cp.setLayout(flow);
        frame.setSize(width,height);
        frame.setTitle("JavaSwing");
        cp.add(input);
        cp.add(label);
        cp.add(button1);
        cp.add(button2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setUpButtonListeners(){
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object o = e.getSource();
                if (e.getSource() == button1){
                    System.out.println("beep");
                } else if (e.getSource() == button2) {
                    System.out.println("boop");
                }
            }

        };

      button1.addActionListener(buttonListener);
      button2.addActionListener(buttonListener);
    }

}
