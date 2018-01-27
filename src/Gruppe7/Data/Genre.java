package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 */
public enum Genre {

    ACTION("Action"),
    DOKUMENTATION("Dokumentation"),
    DRAMA("Drama"),
    HORROR("Horror"),
    KOMOEDIE("Komödie"),
    KRIMI("Krimi"),
    SCIENCE_FICTION("Science Fiction"),
    ZEICHENTRICK("Zeichentrick"),
    THRILLER("Thriller");

    private String genereName;

    /**
     * @param genreName der Name eines Genres.
     */
    Genre(String genreName) {
        this.genereName = genreName;
    }

    /**
     * Die Getmethode für den Namen eines Genres.
     *
     * @return den Genrenamen.
     */
    public String getGenereName() {
        return this.genereName;
    }
}


