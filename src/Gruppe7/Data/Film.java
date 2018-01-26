package Gruppe7.Data;

/**
 * @author Fabian Ueberled
 * Stellt die Mutterklasse für Werbefilme und Kinofilme dar.
 * Stellt die Felder "titel" und "laufzeit" sowie ihre Getter bereit.
 */
public abstract class Film {
    private String titel;
    private int laufzeit;

    public Film(String in_titel, int in_laufzeit) {
        titel = in_titel;
        laufzeit = in_laufzeit;
    }

    // Getter
    public String getTitel() {
        return titel;
    }

    public int getLaufzeit() {
        return laufzeit;
    }
}
