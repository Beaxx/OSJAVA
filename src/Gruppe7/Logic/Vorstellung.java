package Gruppe7.Logic;

import Gruppe7.Data.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lennart Völler
 *
 * Die Vorstellung ist die Basis-Einheit des Spielplans. Jeder Spielplan setzt sich aus 21*4*[Anzahl der Säle]
 * Vorstellungen zusammen.
 */
public class Vorstellung {

    private Kinofilm vorstellungsFilm;
    private ArrayList<Werbefilm> werbungen = new ArrayList<>();
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;

    private int eintrittspreis = 0;
    private int andrang = 0;
    private int zuschauerLoge = 0;
    private int zuschauerParkett = 0;
    private int vorstellungWerbeeinnahmen = 0;
    private int[] vorstellungTicketeinnahmen = {0, 0};

    /**
     * Basis-Konstruktor, erstellt eine zufällige Vorstellung aus der Menge der möglichen, an dieser Stelle
     * erlaubten Vorstellungen. Ist ein Kinofilm für die Vorstellung gewählt, wird der Werbeblock angehängt.
     *
     * @param in_saalIndex                 die Saalnummer
     * @param in_vorstellungsTimeslotIndex der Index des Timeslots zu dem die Vorstellung stattfindet.
     */
    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex) {
        vorstellungsSaal = SaalVerwaltung.GetSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];

        // Aus der Filmverwaltung wird ein FilmSet geholt, dessen Filme die Kriterien hinsichtlich Technik und Timeslot erfüllt
        ArrayList<Kinofilm> filmSet = FilmVerwaltung.GetFilme(vorstellungsSaal.GetThreeD(), vorstellungsTimeslot);

        // Zufälligen Film aus dem Set auswählen.
        vorstellungsFilm = (Kinofilm) filmSet.toArray()[ThreadLocalRandom.current().nextInt(0, filmSet.size() - 1)];

        //Werbung hinzufügen
        werbungen = werbungAnhaengen();
    }

    /**
     * Je nach verbleibender Zeit zum Zeigen von Werbung wird eine Liste aus Werbungen mit den besten
     * Profitabilitätswerten (UmsatzProZuschauer/Laufzeit) erstellt. Die Zeit zum Zeigen von Werbung ist auf
     * 20 Minuten begrenzt. Für den Fall, dass 20 Minuten Werbung gezeigt werden können wird ein
     * Standard-Werbeblock verwendet.
     *
     * @return Eine ArrayList der Werbung einer Vorstellung
     */
    private ArrayList<Werbefilm> werbungAnhaengen() {
        int werbeDauerSoll = vorstellungsTimeslot.GetSlotDuration() - vorstellungsFilm.GetLaufzeit();

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

    public void VorstellungsTicketEinnahmen(Vorstellung this) {

        int andrang50p = (int) Math.round((double) andrang * 0.5);
        int ueberhang = 0;

        // Andrang in der Loge. Wenn Andrang > Plätze: Andrang = Plätze + Überhang
        if (andrang50p > vorstellungsSaal.GetPlaetzeLoge()) {
            zuschauerLoge = vorstellungsSaal.GetPlaetzeLoge();

            ueberhang = andrang50p - zuschauerLoge;

        } else {
            zuschauerLoge = andrang50p;
        }

        //Andrang im Parkett. Wenn Andrang < Plätze: Plätze = Andrang + Überhang
        if (andrang50p > vorstellungsSaal.GetPlaetzeParkett()) {
            zuschauerParkett = vorstellungsSaal.GetPlaetzeParkett();
        } else {
            zuschauerParkett = andrang50p;

            int freiePlaetze = (vorstellungsSaal.GetPlaetzeParkett() - zuschauerParkett);

            if (freiePlaetze <= ueberhang) {
                zuschauerParkett += freiePlaetze;
            } else {
                zuschauerParkett += ueberhang;
            }
        }

        //Einnahmen durch Ticketverkäufe
        vorstellungTicketeinnahmen[0] = (eintrittspreis + 2) * zuschauerLoge;
        vorstellungTicketeinnahmen[1] = eintrittspreis * zuschauerParkett;
    }

    public void VorstellungWerbeeinnahmen(Vorstellung this) {
        if (werbungen == WerbefilmVerwaltung.getWerbefilme20MinutenStandard()) {
            vorstellungWerbeeinnahmen = WerbefilmVerwaltung.GetWerbefilme20MinutenStandardUmsatzProZuschauer() * (zuschauerLoge + zuschauerParkett);
        } else {
            for (Werbefilm werbung : werbungen) {
                vorstellungWerbeeinnahmen += werbung.GetUmsatzProZuschauer() * (zuschauerLoge + zuschauerParkett);
            }
        }
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
        output += "Beliebtheit: " + vorstellungsFilm.GetBeliebtheit() + "\n" +
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

    public int GetAndrang() {
        return andrang;
    }

    public int GetVorstellungWerbeeinnahmen() {
        return vorstellungWerbeeinnahmen;
    }

    public int[] GetVorstellungTicketeinnahmen() {
        return vorstellungTicketeinnahmen;
    }

    public int GetZuschauerLoge() {
        return zuschauerLoge;
    }

    public int GetZuschauerParkett() {
        return zuschauerParkett;
    }

    //Setter
    public void SetEintrittspreis(int in_eintrittspreis) {
        eintrittspreis = in_eintrittspreis;
    }

    public void SetAndrang(int andrang) {
        this.andrang = andrang;
    }
}
