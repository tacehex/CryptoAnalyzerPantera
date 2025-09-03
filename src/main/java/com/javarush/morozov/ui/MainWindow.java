package com.javarush.morozov.ui;

import com.javarush.morozov.core.CaesarCipher;
import com.javarush.morozov.core.FileHandler;
import com.javarush.morozov.ui.listeners.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton bruteForceButton;
    private JButton loadFileButton;
    private JButton saveFileButton;
    JComboBox<CaesarCipher.Language> languageComboBox;


    public MainWindow() {
        super("Шифр цезаря");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout()); // автоматическое управление расположением компонентов внутри контейнера

        initUI();
        setupListeners();
    }

    /**
     * Инициализация UI заданными компонентами.
     */
    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        languageComboBox = new JComboBox<>(CaesarCipher.Language.values());
        languageComboBox.setSelectedItem(CaesarCipher.Language.RUSSIAN);
        headerPanel.add(new JLabel("Символы шифровки:"));
        headerPanel.add(languageComboBox);
        topPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        encryptButton = new JButton("Зашифровать");
        decryptButton = new JButton("Расшифровать");
        bruteForceButton = new JButton("Brute Force");
        loadFileButton = new JButton("Загрузить файл");
        saveFileButton = new JButton("Сохранить в файл");

        buttonPanel.add(loadFileButton);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(bruteForceButton);
        buttonPanel.add(saveFileButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        inputTextArea = new JTextArea();
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        textPanel.add(inputScrollPane);
        textPanel.add(outputScrollPane);

        add(topPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
    }

    /**
     * Установка прослушиваний при нажатии на кнопки
     */
    private void setupListeners() {
        FileHandler handler = new FileHandler();
        CaesarCipher cipher = new CaesarCipher(getSelectedLanguage());

        LoadFileButtonListener loadListener = new LoadFileButtonListener(inputTextArea, outputTextArea, handler);
        SaveFileButtonListener saveListener = new SaveFileButtonListener(outputTextArea, handler);
        EncryptButtonListener encryptListener = new EncryptButtonListener(inputTextArea, outputTextArea, cipher);
        DecryptButtonListener decryptListener = new DecryptButtonListener(inputTextArea, outputTextArea, cipher);
        BruteForceButtonListener bruteForceListener = new BruteForceButtonListener(inputTextArea, outputTextArea, cipher);

        loadFileButton.addActionListener(loadListener);
        saveFileButton.addActionListener(saveListener);
        encryptButton.addActionListener(encryptListener);
        decryptButton.addActionListener(decryptListener);
        bruteForceButton.addActionListener(bruteForceListener);
        languageComboBox.addActionListener(_ -> cipher.setLanguage(getSelectedLanguage()));

    }

    private CaesarCipher.Language getSelectedLanguage() {
        return (CaesarCipher.Language) languageComboBox.getSelectedItem();
    }
}
