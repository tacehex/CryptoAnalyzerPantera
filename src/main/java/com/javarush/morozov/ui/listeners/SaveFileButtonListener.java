package com.javarush.morozov.ui.listeners;

import com.javarush.morozov.core.FileHandler;
import com.javarush.morozov.ui.handlers.MessageHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SaveFileButtonListener implements ActionListener {
    private final JTextArea outputTextArea;
    private final FileHandler handler;

    public SaveFileButtonListener(JTextArea outputTextArea, FileHandler handler) {
        this.outputTextArea = outputTextArea;
        this.handler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (outputTextArea == null || outputTextArea.getText().trim().isEmpty()) {
            MessageHandler.showErrorDialog("Нет данных для сохранения");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileHandler.getTextFileFilter());
        fileChooser.setDialogTitle("Сохранить файл");

        int userFileSelection = fileChooser.showSaveDialog(null);

        if (userFileSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = fileChooser.getSelectedFile();

                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                }

                handler.saveTextFile(fileToSave, outputTextArea.getText());

                MessageHandler.showSuccessDialog("Файл успешно сохранён по пути: " + fileToSave.getPath());
            } catch (IOException ex) {
                MessageHandler.showErrorDialog("Ошибка при сохранении: " + ex.getMessage());
            }
        }
    }
}
