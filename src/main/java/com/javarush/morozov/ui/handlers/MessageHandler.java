package com.javarush.morozov.ui.handlers;

import javax.swing.*;

public class MessageHandler {
    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Успех",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static boolean confirmFileOverwrite() {
        return JOptionPane.showConfirmDialog(
                null,
                "Файл уже существует. Перезаписать?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}
