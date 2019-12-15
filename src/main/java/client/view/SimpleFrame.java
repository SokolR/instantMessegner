package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SimpleFrame extends JFrame {
    private JLabel label;
    private JButton button;
    private JTextField title;
    public SimpleFrame(String title, JLabel label, JButton button) throws HeadlessException {
        super(title);
        this.label = label;
        this.button = button;
        createGUI();
    }

    public JButton getButton() {
        return button;
    }

    public JTextField getTextField() {
        return title;
    }

    void  createGUI() {

        Box box1 = Box.createHorizontalBox();
        label.setForeground(Color.WHITE);
        box1.add(label);

        Box box2 = Box.createHorizontalBox();
        title = new JTextField(15);
        box2.add(title);

        Box box3 = Box.createHorizontalBox();
        button.setBackground(Color.GREEN);
        button.setBorder(new EmptyBorder(7, 30, 7, 30));
        box3.add(button);

        Box mainBox = Box.createVerticalBox();

        mainBox.setBorder(new EmptyBorder(12,12,12,12));
        mainBox.add(box1);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(box2);
        mainBox.add(Box.createVerticalStrut(17));
        mainBox.add(box3);

        JPanel panel = new JPanel();
        panel.add(mainBox);
        panel.setBackground(Color.DARK_GRAY);
        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void close(){
        setVisible(false);
        dispose();
    }
}
