package client.controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class EditConnection extends JFrame {
    private JTextField URLField;
    private JTextField portField;
    private JButton connect = new JButton("Connect");

    public EditConnection() throws HeadlessException {
        super("Try connect");
        createGUI();
    }

    public void createGUI(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Box urlBox = Box.createHorizontalBox();
        final JLabel URLLabel = new JLabel("URL:");
        URLField = new JTextField(15);
        urlBox.add(URLLabel);
        urlBox.add(Box.createHorizontalStrut(6));
        urlBox.add(URLField);

        Box protBox = Box.createHorizontalBox();
        final JLabel portLabel = new JLabel("Port:");
        portField = new JTextField(15);
        protBox.add(portLabel);
        protBox.add(Box.createHorizontalStrut(6));
        protBox.add(portField);

        Box connectBtnBox = Box.createHorizontalBox();

        connectBtnBox.add(connect);
        connectBtnBox.setBackground(Color.GREEN);

        URLLabel.setPreferredSize(portLabel.getPreferredSize());

        Box mainBox = Box.createVerticalBox();

        mainBox.setBorder(new EmptyBorder(12,12,12,12));
        mainBox.setBackground(Color.BLACK);
        mainBox.add(urlBox);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(protBox);
        mainBox.add(Box.createVerticalStrut(17));
        mainBox.add(connectBtnBox);
        JPanel p = new JPanel();
        p.add(mainBox);
        setContentPane(p);
        pack();

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void closeFrame(){
        setVisible(false);
        dispose();
    }

    public JButton getConnect() {
        return connect;
    }


    public JTextField getURLField() {
        return URLField;
    }

    public JTextField getPortField() {
        return portField;
    }
}
