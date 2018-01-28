package Gruppe7.Logic;

import Gruppe7.Data.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lennart Völler
 */
public class Vorstellung {

    private Kinofilm vorstellungsFilm;
    private ArrayList<Werbefilm> werbungen = new ArrayList<>();
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;
    private int eintrittspreis = 13; // Ausgangswert
    private int andrang = 0;

    /**
     * Basis-Konstruktor, erstellt eine zufällige Vorstellung aus der Menge der möglichen, an dieser Stelle
     * erlaubten Vorstellungen
     *
     * @param in_saalIndex
     * @param in_vorstellungsTimeslotIndex
     */
    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex) {
        vorstellungsSaal = SaalVerwaltung.getSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];

        // Aus der Filmverwaltung wird ein FilmSet geholt, dessen Filme die Kriterien hinsichtlich Technik und Timeslot erfüllt
        ArrayList<Kinofilm> filmSet = FilmVerwaltung.getFilme(vorstellungsSaal.GetThreeD(), vorstellungsTimeslot);

        // Zufälligen Film aus dem Set auswählen.
        vorstellungsFilm = (Kinofilm) filmSet.toArray()[ThreadLocalRandom.current().nextInt(0, filmSet.size() - 1)];

        //Werbung hinzufügen
        werbungen = werbungAnhaengen();
    }

    /**
     * Debugged
     * Konstruktor Überladung bei der der Eintrittspresi der Vorstellung niht zufällig ist, sondern mit übergeben
     * wird. Der Film ist nicht zufällig sondern steht ebenfalls schon fest. Dieser Konstruktor findet bei der
     * inkrementellen Verbesserung von Vorstellungen anwendung.
     *
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
     * Debugged
     * Je nach verbleibender Zeit zum Zeigen von Werbung wird eine Liste mit den besten Profitabilitätswerten
     * (UmsatzProZuschauer/Laufzeit) erstellt. Die Zeit zum Zeigen von Werbung ist auf 20 Minuten begrenzt. Für
     * den Fall, dass 20 Minuten Werbung gezeigt werden können wird ein Standard-Werbeblock verwendet.
     *
     * @return Eine ArrayList der Werbung einer Vorstellung
     */
    private ArrayList<Werbefilm> werbungAnhaengen() {
        int werbeDauerSoll = vorstellungsTimeslot.getSlotDuration() - vorstellungsFilm.GetLaufzeit();

        if (werbeDauerSoll >= 20) {
            return WerbefilmVerwaltung.getWerbefilme20MinutenStandard();
        } else {
            int werbeDauerIst = WerbefilmVerwaltung.getWerbefilme20MinutenStandardDauer();

            ArrayList<Werbefilm> output = WerbefilmVerwaltung.getWerbefilme20MinutenStandard();

            for (Werbefilm werbung : output) {
                if ((werbeDauerIst - werbung.GetLaufzeit()) <= werbeDauerSoll) {
                    output.remove(werbung);
                    werbeDauerIst -= werbung.GetLaufzeit();
                }
            }
            return output;
        }
    }


    //Getter
    public Kinofilm GetKinofilm() {
        return vorstellungsFilm;
    }

    public Saal GetSaal() {
        return vorstellungsSaal;
    }

    public Spielzeiten GetSpielzeiten() {
        return vorstellungsTimeslot;
    }

    public ArrayList<Werbefilm> GetWerbefilme() {
        return werbungen;
    }

    public int GetEintrittspreis() {
        return eintrittspreis;
    }

    //Setter
    public void SetEintrittspreis(int in_eintrittspreis) {
        eintrittspreis = in_eintrittspreis;

    }

    @Override
    public String toString() {
        String output = "";
        // Saal +  Uhrzeit
        output += "Saal: " + vorstellungsSaal.GetSaalNummer() + " um " + vorstellungsTimeslot + "\n";

        // Film
        output += "Titel: " + vorstellungsFilm.GetTitel() + "\n" +
                "Regisseur: " + vorstellungsFilm.GetRegisseur() + "\n" +
                "Laufzeit: " + vorstellungsFilm.GetLaufzeit() + "\n" +
                "FSK: " + vorstellungsFilm.GetFsk() + "\n" +
                "Genre: " + vorstellungsFilm.GetGenre() + "\n" +
                "Sprache: " + vorstellungsFilm.GetSprache() + "\n" +
                "Land: " + vorstellungsFilm.GetErscheinungsland() + "\n" +
                "Tag: " + "\n";

        // Financials
        output += "Beliebtheit: " + vorstellungsFilm.GetBeliebtheit() + "\n"+
                  "Verleihpreis: " + vorstellungsFilm.GetVerleihpreisProWoche() + "\n";
//                  "Vorstellungseinnahme aus Tickets: " + vorstellungsEinnahmenTickets + "\n" +
//                  "Zuschauer Loge: " + zuschauerLoge + "\n" +
//                  "Zuschauer Parkett: " + zuschauerParkett + "\n" +
//                  "Zuschauer Gesamt: " + zuschauerGesamt + "\n";
        output += "Beliebtheit: " + vorstellungsFilm.GetBeliebtheit() + "\n" +
                "Verleihpreis: " + vorstellungsFilm.GetVerleihpreisProWoche() + "\n" +
                "Eintrittspreis: " + GetEintrittspreis() + "\n"; // Fabian

        output += "-----------------------------\n";
        return output;
    }

    public int GetAndrang() {
        return andrang;
    }

    public void SetAndrang(int andrang) {
        this.andrang = andrang;
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
//        if (castIn_Vorstellung.GetKinofilm() == GetKinofilm()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
