package Gruppe7.Data;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lennart Völler
 * @@version  25.01.2018
 */
public class Vorstellung {

    private Kinofilm vorstellungsFilm;
    private ArrayList<Werbefilm> werbungen = new ArrayList<>();
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;
    private int eintrittspreis = 11; // Ausgangswert

    /**
     * Basis-Konstruktor, erstellt eine zufällige Vorstellung aus der Menge der möglichen, an dieser Stelle
     * erlaubten Vorstellungen
     * @param in_saalIndex
     * @param in_vorstellungsTimeslotIndex
     */
    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex) {
        vorstellungsSaal = SaalVerwaltung.getSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];

        // Aus der Filmverwaltung wird ein FilmSet geholt, dessen Filme die Kriterien hinsichtlich Technik und Timeslot erfüllt
        ArrayList<Kinofilm> filmSet = FilmVerwaltung.getFilme(vorstellungsSaal.getThreeD(), vorstellungsTimeslot);

        // Zufälligen Film aus dem Set auswählen.
        vorstellungsFilm = (Kinofilm)filmSet.toArray()[ThreadLocalRandom.current().nextInt(0, filmSet.size() - 1)];

        //Werbung hinzufügen
        werbungen = werbungAnhaengen();
    }

    /** Debugged
     * Konstruktor Überladung bei der der Eintrittspresi der Vorstellung niht zufällig ist, sondern mit übergeben
     * wird. Der Film ist nicht zufällig sondern steht ebenfalls schon fest. Dieser Konstruktor findet bei der
     * inkrementellen Verbesserung von Vorstellungen anwendung.
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

    /** Debugged
     * Je nach verbleibender Zeit zum Zeigen von Werbung wird eine Liste mit den besten Profitabilitätswerten
     * (UmsatzProZuschauer/Laufzeit) erstellt. Die Zeit zum Zeigen von Werbung ist auf 20 Minuten begrenzt. Für
     * den Fall, dass 20 Minuten Werbung gezeigt werden können wird ein Standard-Werbeblock verwendet.
     * @return Eine ArrayList der Werbung einer Vorstellung
     */
    private ArrayList<Werbefilm> werbungAnhaengen() {
        int werbeDauerSoll = vorstellungsTimeslot.getSlotDuration() - vorstellungsFilm.getLaufzeit();

        if (werbeDauerSoll >= 20) {
            return WerbefilmVerwaltung.getWerbefilme20MinutenStandard();
        }
        else {
            int werbeDauerIst = WerbefilmVerwaltung.getWerbefilme20MinutenStandardDauer();

            ArrayList<Werbefilm> output = WerbefilmVerwaltung.getWerbefilme20MinutenStandard();

            for (Werbefilm werbung : output) {
                if ((werbeDauerIst - werbung.getLaufzeit()) <= werbeDauerSoll) {
                    output.remove(werbung);
                    werbeDauerIst -= werbung.getLaufzeit();
                }
            }
            return output; // TODO: Case noch nicht getestet, kommt quasi nie vor.
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

//    @Override
//    public boolean equals(Object in_Vorstellung) {
//        Vorstellung castIn_Vorstellung;
//        if (in_Vorstellung.getClass().getName() == this.getClass().getName()) {
//            castIn_Vorstellung = (Vorstellung) in_Vorstellung;
//        } else {
//            return false;
//        }
//
//        if (castIn_Vorstellung.getKinofilm() == getKinofilm()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
