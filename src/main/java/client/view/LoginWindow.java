package client.view;


import client.controller.ControllerActionsClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

class LoginWindow extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton ok = new JButton() ;
    private JButton reg = new JButton();
    private ControllerActionsClient controller;

    LoginWindow(ControllerActionsClient controller) {
        super("Messenger");
        this.controller = controller;
        createGUI();
    }
    public LoginWindow() {
        super("Edit");
        createGUI();
    }
    public void createGUI(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Box box1 = Box.createHorizontalBox();
        final JLabel loginLabel = new JLabel("Login:");
        loginLabel.setForeground(Color.WHITE);
        loginField = new JTextField(15);
        box1.add(loginLabel);
        box1.add(Box.createHorizontalStrut(6));
        box1.add(loginField);

        Box box2 = Box.createHorizontalBox();
        final JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField(15);
        box2.add(passwordLabel);
        box2.add(Box.createHorizontalStrut(6));
        box2.add(passwordField);

        Box box3 = Box.createHorizontalBox();
        box3.setBorder(new EmptyBorder( 7, 17, 7, 17));
        setEnterListener();
        ok.setBorder(new EmptyBorder(7, 52, 7, 52));
        ok.setBackground(Color.GREEN.darker());
        ok.setForeground(Color.WHITE);
        box3.add(ok);

        Box box4 = Box.createHorizontalBox();
        box4.setBorder(new EmptyBorder( 7, 7, 7, 7));
        reg.setBackground(Color.RED.darker());
        reg.setBorder(new EmptyBorder(7, 40, 7, 40));
        reg.setForeground(Color.WHITE);
        setRegListener();
        box4.add(reg);

        loginLabel.setPreferredSize(passwordLabel.getPreferredSize());

        Box mainBox = Box.createVerticalBox();

        mainBox.setBorder(new EmptyBorder(12,12,12,12));
        mainBox.add(box1);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(box2);
        mainBox.add(Box.createVerticalStrut(17));
        mainBox.add(box3);
        mainBox.add(box4);
        JPanel p = new JPanel();
        p.add(mainBox);
        p.setBackground(Color.DARK_GRAY.brighter());
        setContentPane(p);
        pack();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                    controller.closeChat();
                    System.exit(2);
            }
        });
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setEnterListener(){
        ok.setText("Login In");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginField.getText() != null && !loginField.getText().trim().equals("") && passwordField!=null) {
                    controller.authentication(loginField.getText(),new String(passwordField.getPassword()));
                }
            }

        });

    }
    public void setRegListener() {
        reg.setText("Registration");
        reg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginField.getText() != null && !loginField.getText().trim().equals("") && passwordField!=null) {
                   controller.registration(loginField.getText(),new String(passwordField.getPassword()));
                }
            }
        });
    }
    public void closeFrame(){
        this.setVisible(false);
        this.dispose();
    }

}
