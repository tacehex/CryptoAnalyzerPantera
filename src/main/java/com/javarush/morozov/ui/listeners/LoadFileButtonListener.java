package com.javarush.morozov.ui.listeners;


import com.javarush.morozov.core.FileHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.javarush.morozov.ui.handlers.MessageHandler.showErrorDialog;

public class LoadFileButtonListener implements ActionListener {
    private final JTextArea inputTextArea;
    private final JTextArea outputTextArea;
    private final FileHandler handler;

    public LoadFileButtonListener(JTextArea inputTextArea, JTextArea outputTextArea, FileHandler handler) {
        this.inputTextArea = inputTextArea;
        this.outputTextArea = outputTextArea;
        this.handler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileHandler.getTextFileFilter());

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String content = handler.readTextFile(fileChooser.getSelectedFile());
                outputTextArea.setText(null);
                inputTextArea.setText(content);
            } catch (SecurityException ex) {
                showErrorDialog("Ошибка безопасности: " + ex.getMessage());
            } catch (IOException ex) {
                showErrorDialog("Ошибка при чтении файла: " + ex.getMessage());
            } catch (Exception ex) {
                showErrorDialog("Неожиданная ошибка: " + ex.getMessage());
            }
        }
    }
}
