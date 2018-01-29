package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 * Stellt die abstrakte Mutterklasse für Werbefilme und Kinofilme dar. Stellt die Felder "titel" und "laufzeit"
 * sowie ihre Getter bereit.
 */
public abstract class Film {
    private String titel;
    private int laufzeit;

    Film(String in_titel, int in_laufzeit) {
        titel = in_titel;
        laufzeit = in_laufzeit;
    }

    /**
     * Getter für Filmtitel
     *
     * @return Filmtitel
     */
    public String GetTitel() {
        return titel;
    }

    /**
     * Getter für Filmlaufzeit
     *
     * @return Filmlaufzeit in Minuten
     */
    public int GetLaufzeit() {
        return laufzeit;
    }
}
