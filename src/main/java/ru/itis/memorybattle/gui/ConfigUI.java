package ru.itis.memorybattle.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static ru.itis.memorybattle.utils.GameSettings.IP;
import static ru.itis.memorybattle.utils.GameSettings.PORT;

public class ConfigUI extends JFrame {
    private JTextField ipField;
    private JTextField portField;
    private JTextField nameField;


    private String ip;
    private int port;
    private String name;

    public ConfigUI() {
        setTitle("Configuration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("IP:"));
        ipField = new JTextField(IP);
        add(ipField);

        add(new JLabel("Port:"));
        portField = new JTextField(Integer.toString(PORT));
        add(portField);

        add(new JLabel("Name:"));
        nameField = new JTextField("");
        add(nameField);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(this::handleConnect);
        add(connectButton);

        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleConnect(ActionEvent e) {
        try {
            ip = ipField.getText();
            port = Integer.parseInt(portField.getText());
            name = nameField.getText();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid values!");
        }
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }
}