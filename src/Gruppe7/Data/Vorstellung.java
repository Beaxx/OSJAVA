package Gruppe7.Data;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lennart Völler
 * @date 25.01.2018
 */
public class Vorstellung {

    //Attribute
    private Kinofilm vorstellungsFilm;
    private ArrayList<Werbefilm> werbungen = new ArrayList<>();
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;
    private int eintrittspreis;

    // TODO: Beliebtheit random iterieren
    // TODO: Was ist mit der Sprache?

    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex) {
        vorstellungsSaal = SaalVerwaltung.getSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];

        ArrayList<Kinofilm> filmSet = FilmVerwaltung.getFilme(vorstellungsSaal.getThreeD(), vorstellungsTimeslot);

        // Zufälligen Film aus dem Set auswählen.
        vorstellungsFilm = (Kinofilm)filmSet.toArray()[ThreadLocalRandom.current().nextInt(0, filmSet.size() - 1)];

        eintrittspreis = ThreadLocalRandom.current().nextInt(8, 23);

        //Werbung hinzufügen
        werbungen = werbungAnhaengen();
    }

    /**
     * Konstruktor überladung bei der Eintrittspreis eingabeparameter ist
     * @param in_saalIndex
     * @param in_vorstellungsTimeslotIndex
     * @param in_eintrittspreis
     */
    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex, int in_eintrittspreis, Kinofilm in_film) {
        vorstellungsSaal = SaalVerwaltung.getSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];
        eintrittspreis = in_eintrittspreis;

        // Zufälligen Film aus dem Set auswählen.
        vorstellungsFilm = in_film;

        //Werbung hinzufügen
        werbungen = werbungAnhaengen();
    }

    /**
     * Je nach verbleibender Zeit zum Zeigen von Werbung wird eine Liste mit den besten Profitabilitätswerten
     * (UmsatzProZuschauer/Laufzeit) erstellt. Die Zeit zum Zeigen von Werbung ist auf 20 Minuten begrenzt. Für
     * den Fall, dass 20 Minuten Werbung gezeigt werden können wird ein Standard-Werbeblock verwendet.
     * @return Eine ArrayList der Werbung einer Vorstellung
     */
    private ArrayList<Werbefilm> werbungAnhaengen() {
        int werbeDauerSoll = vorstellungsTimeslot.getSlotDuration() - vorstellungsFilm.getLaufzeit();

        if (werbeDauerSoll >= 20) {
            return WerbefilmVerwaltung.getWerbefilme20MinutenStandard();
        } else {
            int werbeDauerIst = 0; // TODO: Subtraktionsansatz, solange vom 20Min standard subtrahieren bis es passt.

            ArrayList<Werbefilm> output = new ArrayList<>();

            for (Werbefilm werbung : WerbefilmVerwaltung.getWerbefilme()) {
                if ((werbeDauerIst + werbung.getLaufzeit()) <= werbeDauerSoll) {
                    output.add(werbung);
                    werbeDauerIst += werbung.getLaufzeit();
                }
            }
            return output;
        }
    }

    //Getter
    public Kinofilm getKinofilm(){ return vorstellungsFilm; }
    public Saal getSaal(){ return vorstellungsSaal; }
    public Spielzeiten getSpielzeiten(){ return vorstellungsTimeslot; }
    public ArrayList<Werbefilm> getWerbefilme(){ return werbungen; }
    public int GetEintrittspreis() { return eintrittspreis; }

    //Setter
    public void SetEintrittspreis(int in_eintrittspreis) { eintrittspreis = in_eintrittspreis; }

    @Override
    public String toString() {
        String output = "";
        // Saal +  Uhrzeit
        output += "Saal: " + vorstellungsSaal.getSaalNummer() + " um "+ vorstellungsTimeslot + "\n";

        // Film
        output += "Titel: " + vorstellungsFilm.getTitel() + "\n" +
                  "Regisseur: " + vorstellungsFilm.getRegisseur() + "\n" +
                  "Laufzeit: " + vorstellungsFilm.getLaufzeit() + "\n" +
                  "FSK: " + vorstellungsFilm.getFsk( )+ "\n" +
                  "Genre: " + vorstellungsFilm.getGenre() + "\n" +
                  "Sprache: " + vorstellungsFilm.getSprache() + "\n" +
                  "Land: " + vorstellungsFilm.getLaufzeit() + "\n";

        // Financials
        output += "Beliebtheit: " + vorstellungsFilm.getBeliebtheit() + "\n"+
                  "Verleihpreis: " + vorstellungsFilm.getVerleihpreisProWoche() + "\n";

        output += "-----------------------------\n";
        return output;
    }
}
