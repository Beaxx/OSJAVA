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

    Genre(String genreName){
        this.genereName = genreName;
    }

    public String getGenereName(){
        return this.genereName;
    }
}


