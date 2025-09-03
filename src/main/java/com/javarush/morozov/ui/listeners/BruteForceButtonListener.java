package com.javarush.morozov.ui.listeners;

import com.javarush.morozov.core.CaesarCipher;
import com.javarush.morozov.ui.handlers.MessageHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BruteForceButtonListener implements ActionListener {
    private final JTextArea inputTextArea;
    private final JTextArea outputTextArea;
    private final CaesarCipher cipher;

    public BruteForceButtonListener(JTextArea inputTextArea, JTextArea outputTextArea, CaesarCipher cipher) {
        this.inputTextArea = inputTextArea;
        this.outputTextArea = outputTextArea;
        this.cipher = cipher;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = inputTextArea.getText();

        if (text == null || text.trim().isEmpty()) {
            MessageHandler.showErrorDialog("Текст для расшифровки методом BruteForce оказался пустым.");
            return;
        }

        List<CaesarCipher.BruteForceResult> results = cipher.bruteForce(text);

        StringBuilder sb = new StringBuilder("Возможные варианты: \n\n");

        for (int i = 0; i < Math.min(3, results.size()); i++) {
            CaesarCipher.BruteForceResult result = results.get(i);
            sb.append(String.format("Ключ %d (уверенность %d): \n%s\n\n",
                    result.key(), result.score(), result.text()));
        }

        outputTextArea.setText(sb.toString());
    }
}
