package Gruppe7.Data;
import java.util.ArrayList;

/**
 * @author Lennart Völler
 * Erbt von Film.
 *
 * Ein Kinofilm ist ein von der Vorstellung losgelöstes Objekt. Kinofilme sind austauschbare Einheiten, die
 * unabhängig vom Spielplan sind. Sie sind eine der Komponenten zur Erstellung von Spielplänen.
 */
public class Kinofilm extends Film {
    private boolean threeD;
    private String sprache, regisseur, erscheinungsland;
    private int erscheinungsjahr, beliebtheit, verleihpreisProWoche, kostenGesamt;
    private Fsk fsk;
    private ArrayList<Genre> genre = new ArrayList<>();
    // TODO: Kostenattribut für die Verleihkosten des films auf den gesamten Spielplan
    /**
     * Konstruktor
     *
     * @param in_titel            der Titel des Films.
     * @param in_laufzeit         die Laufzeit des Films in Minuten.
     * @param in_threeD           die 3D-Eigenschaft eines Films.
     * @param in_sprache          die Sprache des Films.
     * @param in_Regisseur        der Name des Regisseurs des Filmes.
     * @param in_erscheinungsjahr das Erscheinungsjahr des Filmes.
     * @param in_erscheinungsland das Erscheinungsland des Filmes.
     * @param in_beliebtheit      der Beliebtheitswert des Filmes (0-100).
     * @param in_verleipreis      der Verleihpreis des Filmes in ganzzahligen Euro.
     * @param in_fsk              das FSK-Rating des Filmes als Element des FSK-Enums.
     * @param in_genre            das Genre des Filmes als Emenet des Genre-Enums.
     */
    public Kinofilm(
            String in_titel,
            int in_laufzeit,
            boolean in_threeD,
            String in_sprache,
            String in_Regisseur,
            int in_erscheinungsjahr,
            String in_erscheinungsland,
            int in_beliebtheit,
            int in_verleipreis,
            Fsk in_fsk,
            ArrayList<Genre> in_genre) {
        super(in_titel, in_laufzeit);
        threeD = in_threeD;
        sprache = in_sprache;
        regisseur = in_Regisseur;
        erscheinungsjahr = in_erscheinungsjahr;
        erscheinungsland = in_erscheinungsland;
        beliebtheit = in_beliebtheit;
        verleihpreisProWoche = in_verleipreis;
        fsk = in_fsk;
        genre = in_genre;

        kostenGesamt= 0;
    }



    // Getter

    /**
     * Gettermethode für die 3D-Eigenschaft des Films.
     *
     * @return True/False basierend auf der 3d-Eigenschaft.
     */
    public boolean getThreeD() {
        return threeD;
    }

    /**
     * Getmethode für die Sprache des Filmes.
     *
     * @return Sprache des Films.
     */
    public String getSprache() {
        return sprache;
    }

    /**
     * Getmethode für den Namen des Regisseurs des Filmes.
     *
     * @return Name des Regisseurs.
     */
    public String getRegisseur() {
        return regisseur;
    }

    /**
     * Getmethode für das erscheinungsjahr des Filmes.
     *
     * @return Erscheinungsjahr des Filmes.
     */
    public int getErscheinungsjahr() {
        return erscheinungsjahr;
    }

    /**
     * Getmethode für die Beliebtheit des Filmes.
     *
     * @return Beliebtheitswert des Filmes (0-100)
     */
    public int getBeliebtheit() {
        return beliebtheit;
    }

    /**
     * Getmethode für den Verleihpreis des Filmes pro Woche.
     *
     * @return Verleihpreis des Filmes pro woche als Ganzzahl.
     */
    public int getVerleihpreisProWoche() {
        return verleihpreisProWoche;
    }

    /**
     * Getmethode für das FSK-Siegel des Filmes.
     *
     * @return FSK-Siegel des Filmes als Element des FSK-Enums
     */
    public Fsk getFsk() {
        return fsk;
    }

    /**
     * Getmethode für das Genre des Filmes.
     *
     * @return Genre des Films als Element des Genre Enums.
     */
    public ArrayList<Genre> getGenre() {
        return genre;
    }

    /**
     * Getmehoden für das Erscheinungsjahr des Films
     *
     * @return Erscheinungsjahr als Ganzzahl
     */
    public String getErscheinungsland() {
        return erscheinungsland;
    }

    public int getKostenGesamt() {return  kostenGesamt;}


    //Setter
    public int setKostenGesamt(int in_kosten){
        kostenGesamt=in_kosten;
        return kostenGesamt;
    }
}
