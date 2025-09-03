package com.javarush.morozov.core;

import com.javarush.morozov.ui.handlers.MessageHandler;

import java.util.*;

public class CaesarCipher {
    public enum Language {
        RUSSIAN("RUS", "Русские"),
        ENGLISH("ENG", "Английские");

        private final String code;
        private final String displayName;

        Language(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public String getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private static final char[] RUSSIAN_ALPHABET = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    private static final char[] ENGLISH_ALPHABET = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    private char[] currentAlphabet;
    private Language currentLanguage;
    private Set<Character> allowedChars;

    public CaesarCipher(Language language) {
        setLanguage(language);
    }

    public void setLanguage(Language language) {
        this.currentLanguage = language;
        this.currentAlphabet = language == Language.RUSSIAN ? RUSSIAN_ALPHABET : ENGLISH_ALPHABET;
        this.allowedChars = new HashSet<>();

        for (char c : currentAlphabet) {
            allowedChars.add(c);
        }
    }

    public String encrypt(String text, int key) throws IllegalArgumentException {
        validateText(text);
        return processText(text, key);
    }

    public String decrypt(String text, int key) throws IllegalArgumentException {
        validateText(text);
        return processText(text, -key);
    }

    private String processText(String text, int key) {
        StringBuilder result = new StringBuilder();
        text = text.toLowerCase();

        for (char c : text.toCharArray()) {
            int index = Arrays.binarySearch(currentAlphabet, c);

            if (index >= 0) {
                int newIndex = (index + key) % currentAlphabet.length;
                if (newIndex < 0) newIndex += currentAlphabet.length;

                result.append(currentAlphabet[newIndex]);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private void validateText(String text) throws IllegalArgumentException {
        for (int i = 0; i < text.length(); i++) {
            char c = Character.toLowerCase(text.charAt(i));

            if (!allowedChars.contains(c)) {
                String errorMessage = "Текст содержит недопустимые символы. Разрешены только "
                        + currentLanguage.getDisplayName() + " символы и знаки пунктуации.";

                MessageHandler.showErrorDialog(errorMessage);

                throw new IllegalArgumentException(
                        String.format("Недопустимый символ '%c' в позиции %d", text.charAt(i), i)
                );
            }
        }
    }

    /**
     * Расшифровка методом BruteForce
     * @param encryptedText расшифровываемый текст.
     * @return Список результатов оценки метода BruteForce.
     */
    public List<BruteForceResult> bruteForce(String encryptedText) {
        List<BruteForceResult> results = new ArrayList<>();

        for (int key = 1; key < currentAlphabet.length; key++) {
            String decrypted = processText(encryptedText, -key);
            int score = calculateTextScore(decrypted);
            results.add(new BruteForceResult(decrypted, key, score));
        }

        results.sort((a, b) -> Integer.compare(b.score, a.score));

        return results;
    }

        public record BruteForceResult(String text, int key, int score) {
    }

    private int calculateTextScore(String text) {
        int score = 0;

        long spaceCount = text.chars().filter(c -> c == ' ').count();
        score += (int) (spaceCount * 10);

        String vowels = currentLanguage == Language.RUSSIAN ? "аеёиоуыэюя" : "aeiouy";
        long vowelCount = text.toLowerCase().chars()
                .filter(c -> vowels.indexOf(c) >= 0)
                .count();
        score += (int) (vowelCount * 3);

        String commonChars = currentLanguage == Language.RUSSIAN ? "оае" : "eta";
        long commonCount = text.toLowerCase().chars()
                .filter(c -> commonChars.indexOf(c) >= 0)
                .count();
        score += (int) (commonCount * 2);

        String punctuation = ".,!?;:\"'";

        if (text.matches(".*[a-zA-Zа-яА-Я][" + punctuation + "][a-zA-Zа-яА-Я].*")) {
            score -= 20;
        }

        return score;
    }
}