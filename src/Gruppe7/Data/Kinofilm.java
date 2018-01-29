package Gruppe7.Data;
import java.util.ArrayList;

/**
 * @author Lennart Völler
 * Erbt von Film.
 *
 * Ein Kinofilm ist ein von der Vorstellung losgelöstes Objekt. Kinofilme sind austauschbare Einheiten, die
 * unabhängig vom Spielplan sind. Sie sind eine der Komponenten zur Erstellung von Vorstellungen.
 */
public class Kinofilm extends Film {
    private boolean threeD;
    private String sprache, regisseur, erscheinungsland;
    private int erscheinungsjahr, beliebtheit, verleihpreisProWoche, gesamtkostenInSpielplan;
    private Fsk fsk;
    private ArrayList<Genre> genre = new ArrayList<>();

    /**
     * Konstruktor
     *
     * gesamtkostenInSpielplan wird mit 0 initialisierunt und während der Spielplanerstellung befüllt.
     *
     * @param in_Titel            der Titel des Films.
     * @param in_Laufzeit         die Laufzeit des Films in Minuten.
     * @param in_3D           die 3D-Eigenschaft eines Films.
     * @param in_Sprache          die Sprache des Films.
     * @param in_Regisseur        der Name des Regisseurs des Filmes.
     * @param in_Erscheinungsjahr das Erscheinungsjahr des Filmes.
     * @param in_Erscheinungsland das Erscheinungsland des Filmes.
     * @param in_Beliebtheit      der Beliebtheitswert des Filmes (0-100).
     * @param in_Verleihpreis     der Verleihpreis des Filmes in ganzzahligen Euro.
     * @param in_Fsk              das FSK-Rating des Filmes als Element des FSK-Enums.
     * @param in_Genre            das Genre des Filmes als Emenet des Genre-Enums.
     */
    public Kinofilm(
            String in_Titel,
            int in_Laufzeit,
            boolean in_3D,
            String in_Sprache,
            String in_Regisseur,
            int in_Erscheinungsjahr,
            String in_Erscheinungsland,
            int in_Beliebtheit,
            int in_Verleihpreis,
            Fsk in_Fsk,
            ArrayList<Genre> in_Genre) {
        super(in_Titel, in_Laufzeit);
        threeD = in_3D;
        sprache = in_Sprache;
        regisseur = in_Regisseur;
        erscheinungsjahr = in_Erscheinungsjahr;
        erscheinungsland = in_Erscheinungsland;
        beliebtheit = in_Beliebtheit;
        verleihpreisProWoche = in_Verleihpreis;
        fsk = in_Fsk;
        genre = in_Genre;
        gesamtkostenInSpielplan = 0;
    }

    /**
     * Die Berechnung der Kosten, die ein Kinofilm im Spielplan hervorruft basiert stehts auf dem Ferleihpreis pro Woche
     * dieses Filmes. Entsprechend ist dieser Verleihpreis mit einem Faktor (für gewöhnlich 1.0) zu multiplizieren, je
     * nach zusammenhang. Werden die Kosten des Films lediglich einfach hinzugefügt ist der Faktor 1.0. Werden
     * jedoch Rabatte für das Mehrfach-zeigen eines Filmes gewährt, so ist der Faktor -0.3 (Siehe Planer).
     *
     * @param in_Faktor Faktor, der mit dem VerleihpreisProWoche des Filmes multipliziert wird.
     */
    public void InkrementGesamtkostenInSpielplan(double in_Faktor) {
        this.gesamtkostenInSpielplan += (int) Math.round(verleihpreisProWoche * in_Faktor);
    }

    // Setter
    /**
     * Setmethode für die Gesamtkosten, die ein Kinofilm im Spielplan verursacht.
     * Wird ausschließlich dazu verwendet die Gesamtkosten auf 0 zu setzen. Für das tatsächliche erhöhen der Kosten
     * wird InkrementGesamtkostenInSpielplan() verwendet.
     */
    void SetGesamtkostenInSpielplan() {
        this.gesamtkostenInSpielplan = 0;
    }

    // Getter

    /**
     * Gettermethode für die 3D-Eigenschaft des Films.
     *
     * @return True/False basierend auf der 3d-Eigenschaft.
     */
    public boolean GetThreeD() {
        return threeD;
    }

    /**
     * Getmethode für die Sprache des Filmes.
     *
     * @return Sprache des Films.
     */
    public String GetSprache() {
        return sprache;
    }

    /**
     * Getmethode für den Namen des Regisseurs des Filmes.
     *
     * @return Name des Regisseurs.
     */
    public String GetRegisseur() {
        return regisseur;
    }

    /**
     * Getmethode für die Beliebtheit des Filmes.
     *
     * @return Beliebtheitswert des Filmes (0-100)
     */
    public int GetBeliebtheit() {
        return beliebtheit;
    }

    /**
     * Getmethode für den Verleihpreis des Filmes pro Woche.
     *
     * @return Verleihpreis des Filmes pro woche als Ganzzahl.
     */
    public int GetVerleihpreisProWoche() {
        return verleihpreisProWoche;
    }

    /**
     * Getmethode für das FSK-Siegel des Filmes.
     *
     * @return FSK-Siegel des Filmes als Element des FSK-Enums
     */
    public Fsk GetFsk() {
        return fsk;
    }

    /**
     * Getmethode für das Genre des Filmes.
     *
     * @return Genre des Films als Element des Genre Enums.
     */
    public ArrayList<Genre> GetGenre() {
        return genre;
    }

    /**
     * Getmehoden für das Erscheinungsjahr des Films
     *
     * @return Erscheinungsjahr als Ganzzahl
     */
    public String GetErscheinungsland() {
        return erscheinungsland;
    }

    /**
     * Getmethode für die Gesamtkosten, die der Kinofilm im Spielplan verursacht.
     *
     * @return Gesamtkosten als Ganzzahl
     */
    public int GetGesamtkostenInSpielplan() {
        return gesamtkostenInSpielplan;
    }
}
