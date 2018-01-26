package Gruppe7.Data;
import java.util.ArrayList;

/**
 * @author Lennart Völler
 * @version  25.01.2018
 *
 */
public class Kinofilm extends Film {
    private boolean threeD;
    private String sprache, regisseur, erscheinungsland;
    private int erscheinungsjahr, beliebtheit, verleihpreisProWoche;
    private Fsk fsk;
    private ArrayList<Genre> genre = new ArrayList<>();

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
    }

    // Getter
    public boolean getThreeD() {
        return threeD;
    }

    public String getSprache() {
        return sprache;
    }

    public String getRegisseur() {
        return regisseur;
    }

    public int getErscheinungsjahr() {
        return erscheinungsjahr;
    }

    public int getBeliebtheit() {
        return beliebtheit;
    }

    public int getVerleihpreisProWoche() {
        return verleihpreisProWoche;
    }

    public Fsk getFsk() {
        return fsk;
    }

    public ArrayList<Genre> getGenre() {
        return genre;
    }

    public String getErscheinungsland() {
        return erscheinungsland;
    }
}
