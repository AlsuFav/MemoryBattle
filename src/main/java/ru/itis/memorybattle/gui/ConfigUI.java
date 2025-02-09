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
        setTitle("Memory Battle - Configuration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель заголовка
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(190, 147, 250));
        headerPanel.setPreferredSize(new Dimension(400, 60));
        JLabel titleLabel = new JLabel("Memory Battle");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Основная панель
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Поля для ввода
        mainPanel.add(new JLabel("IP:"));
        ipField = new JTextField(IP);
        mainPanel.add(ipField);

        mainPanel.add(new JLabel("Port:"));
        portField = new JTextField(Integer.toString(PORT));
        mainPanel.add(portField);

        mainPanel.add(new JLabel("Name:"));
        nameField = new JTextField("");
        mainPanel.add(nameField);

        // Кнопка подключения
        JButton connectButton = new JButton("Connect");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(new Color(60, 179, 113)); // Зеленый цвет
        connectButton.setForeground(Color.WHITE);
        connectButton.addActionListener(this::handleConnect);

        // Панель для кнопки
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(connectButton);

        // Добавление панелей в JFrame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleConnect(ActionEvent e) {
        try {
            ip = ipField.getText();
            port = Integer.parseInt(portField.getText());
            name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!");
                return;
            }
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

    public static void main(String[] args) {
        new ConfigUI();
    }
}