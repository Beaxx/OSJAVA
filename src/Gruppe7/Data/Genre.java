package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 * Enum zur Verwaltung von Filmgenres.
 */
public enum Genre {

    ACTION("ACTION"),
    DOKUMENTATION("DOKUMENTATION"),
    DRAMA("DRAMA"),
    HORROR("HORROR"),
    KOMOEDIE("KOMOEDIE"),
    KRIMI("KRIMI"),
    SCIENCE_FICTION("SCIENCE_FICTION"),
    ZEICHENTRICK("ZEICHENTRICK"),
    THRILLER("THRILLER");

    /**
     * in_GenreName wird nicht genutzt, muss für enum-Definition jedoch verwendet werden.
     *
     * @param in_GenreName Die Bezeichnung des Genres.
     */
    Genre(String in_GenreName) {
    }
}