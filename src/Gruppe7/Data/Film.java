package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 * Stellt die abstrakte Parentklasse für Werbefilme und Kinofilme dar. Stellt die Felder "titel" und "laufzeit"
 * sowie ihre Getter bereit.
 */
public abstract class Film {
    private String titel;
    private int laufzeit;

    Film(String in_Titel, int in_Laufzeit) {
        titel = in_Titel;
        laufzeit = in_Laufzeit;
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
