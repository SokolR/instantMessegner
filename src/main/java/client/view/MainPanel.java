package client.view;

import client.controller.ControllerActionsClient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class MainPanel  extends JPanel{
    private int[] element;
    private JList list;
    private JTextArea memo;
    private java.util.List<String> activeUsers = new ArrayList<>();
    private JTextArea edit;
    private JButton send;
    private ControllerActionsClient controller;

    public MainPanel(java.util.List<String> activeUsers, ControllerActionsClient controller) {
        this.activeUsers = activeUsers;
        this.controller = controller;
        createGUI();
        setSendListener();
    }

    public JButton getSend() {
        return send;
    }

    public String getMessage(){
        if(edit.getText()==null || edit.getText().trim().equals("")){
            return null;
        }else {
            String message = edit.getText();
            edit.setText("");
            return message;
        }
    }

    public int[] getElement() {
        return element;
    }

    public JTextArea getMemo() {
        return memo;
    }

    public JList setList(){
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String s: activeUsers){
            model.addElement(s);
        }
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        element = list.getSelectedIndices();
                    }
                });
        return list;
    }

    public JTextArea getEdit() {
        return edit;
    }

    public JPanel setListPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        JLabel listLabel = new JLabel("Active users");
        listLabel.setForeground(Color.WHITE);
        listLabel.setBackground(Color.DARK_GRAY);
        list =  setList();
        JScrollPane jsp = new JScrollPane(list);
        jsp.setPreferredSize(new Dimension(120, 300));
        panel.setBackground(Color.DARK_GRAY);
        panel.add(listLabel,"wrap");
        panel.add(jsp);
        return panel;
    }


    public void createGUI(){

        memo = new JTextArea(20,32);
        memo.setLineWrap(true);
        memo.setWrapStyleWord(true);
        memo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                memo.setEnabled(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                memo.setEnabled(true);
            }
        });
        edit = new JTextArea(2,32);
        edit.setWrapStyleWord(true);
        edit.setLineWrap(true);

        send = new JButton("Send");
        send.setBackground(Color.YELLOW);
        send.setBorder(new EmptyBorder(9, 45, 9,45));

        JScrollPane jsp1 = new JScrollPane(memo);
        JScrollPane jsp2 = new JScrollPane(edit);

        Box box = Box.createHorizontalBox();
        box.setBorder(new EmptyBorder( 0, 7, 0, 20));
        box.add(send);

        JPanel  listPanel = setListPanel();
        setLayout(new MigLayout());
        add(jsp1);
        add(listPanel,"wrap");
        add(jsp2);
        add(box);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.DARK_GRAY);

    }

    public List<String> getActiveUsers() {
        return activeUsers;
    }

    public void setSendListener(){
        getSend().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sendMessage = getMessage();
                if(sendMessage == null){
                    return;
                }else{
                    controller.sendAllMessage(sendMessage);
                }
            }
        });
        getEdit().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    String sendMessage = getMessage();
                    if(sendMessage == null){
                        return;
                    }else{
                        controller.sendAllMessage(sendMessage);
                    }
                }

            }

        });

    }

    public void  setActiveUsers(List<String> activeUsers){
        this.activeUsers = activeUsers;
        DefaultListModel<String> activeUser = new DefaultListModel<>();
        for (String s: activeUsers){
            activeUser.addElement(s);
        }
        list.setModel(activeUser);
    }


}
