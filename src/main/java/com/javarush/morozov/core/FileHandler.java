package com.javarush.morozov.core;

import com.javarush.morozov.ui.handlers.MessageHandler;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    public String readTextFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("Файл не существует");
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("Файл слишком большой (макс. " + MAX_FILE_SIZE/1024/1024 + " МБ)");
        }

        if (!file.canRead()) {
            throw new IOException("Нет прав на чтение файла");
        }

        String content = Files.readString(Paths.get(file.getPath()));

        if (content.trim().isEmpty()) {
            throw new IOException("Файл пуст");
        }

        return content;
    }

    public void saveTextFile(File file, String content) throws IOException {
        if (file.exists() && !file.canWrite()) {
            throw new IOException("Нет прав на запись в файл");
        }

        if (file.exists()) {
            if (!MessageHandler.confirmFileOverwrite()) {
                throw new IOException("Сохранение отменено");
            }
        }

        Files.writeString(file.toPath(), content);
    }

    public static FileNameExtensionFilter getTextFileFilter() {
        return new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt");
    }
}
