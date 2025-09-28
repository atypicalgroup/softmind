package br.com.atypical.Softmind.shared.utils;

public class EmojiUtils {

    private EmojiUtils() {}

    public static String mapEmojiToDescription(String emoji) {
        return switch (emoji){
            case "😀" -> "Feliz";
            case "😐" -> "Neutro";
            case "😢" -> "Triste";
            case "😡" -> "Raiva";
            case "😍" -> "Apaixonado";
            case "😴" -> "Cansado";
            default -> emoji;
        };
    }

    public static String mapDescriptionToEmoji(String description) {
        return switch (description){
            case "Feliz" -> "😀";
            case "Neutro" -> "😐";
            case "Triste" -> "😢";
            case "Raiva" -> "😡";
            case "Apaixonado" -> "😍";
            case "Cansado" -> "😴";
            default -> description;
        };
    }
}
