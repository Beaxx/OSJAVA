package Gruppe7.Logic;

import java.util.*;
import Gruppe7.Data.*;

/**
 * @author Lennart Völler
 *
 * Die Planerklasse stellt die zentrale Logik des Programs dar. Jedes Objekt der Klasse Planer beinhaltet ein
 * 4-dimensionales Array vom Typ Vorstellung. Nach der Erstellung eines zufälligen Spielplans wird dieser lokal
 * optimiert. Ist der Optimierungsprozess abgeschlossen beendes der Planer den Konstruktor und gibt den optimierten
 * Spielplan zurück.
 */
public class Planer {
    // Saaldaten
    private int plaetzeGroesterSaal = SaalVerwaltung.GetPlaetzeGroesterSaal();
    private int plaetzeZweitgroesterSaal = SaalVerwaltung.GetPlaetzeZweitgroesterSaal();
    private int anzahlSaele = SaalVerwaltung.GetSaele().size();

    // Finanzdaten
    private int spielplanGewinn = 0;
    private int spielplanTicketeinnahmen = 0;
    private int spielplanWerbeEinnahmen = 0;
    private int spielplanAusgaben = 0;

    // Spielplandaten
    private Vorstellung[][][][] spielplan = new Vorstellung[3][7][anzahlSaele][4];

    private Set<Kinofilm> filmeWoche0 = new HashSet<>();
    private Set<Kinofilm> filmeWoche1 = new HashSet<>();
    private Set<Kinofilm> filmeWoche2 = new HashSet<>();

    // Genredaten
    private static List<Genre> genreList = Arrays.asList(Genre.values());
    private boolean checkGenre = false;

    /**
     * Erstellung eines zufälligen Spielplans bei Iteration durch das leere Vorstellungs-Array.
     * Erstellt:  Ein vierdimensionales Vorstellungsarray [woche][tag][saal][timeslot]
     */
    public Planer() {

        // Genre-Liste wird kopiert
        Set<Genre> localGenreListWoche0 = new HashSet<>();
        Set<Genre> localGenreListWoche1 = new HashSet<>();
        Set<Genre> localGenreListWoche2 = new HashSet<>();

        while (!checkGenre) {
            filmeWoche0.clear();
            filmeWoche1.clear();
            filmeWoche2.clear();

            localGenreListWoche0.addAll(genreList);
            localGenreListWoche1.addAll(genreList);
            localGenreListWoche2.addAll(genreList);

            FilmVerwaltung.CleanUpGesamtkosten();

            spielplan = createRandomSpielplan(localGenreListWoche0, localGenreListWoche1, localGenreListWoche2);
        }
        spielplanGewinn = spielplanWerbeEinnahmen + spielplanTicketeinnahmen - spielplanAusgaben;
    }

    /**
     *
     */
    private Vorstellung[][][][] createRandomSpielplan(Set<Genre> in_localGenreListWoche0,
                                                      Set<Genre> in_localGenreListWoche1,
                                                      Set<Genre> in_localGenreListWoche2) {

        boolean breakstatement;
        spielplanWerbeEinnahmen = 0;
        spielplanTicketeinnahmen = 0;
        spielplanAusgaben = 0;
        spielplanGewinn = 0;

        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        // Erstellung einer zufälligen Vorstellung
                        Vorstellung vorstellung = new Vorstellung(saalIndex, vorstellungIndex);

                        if (!checkGenre) {
                            breakstatement = checkGenre(vorstellung.GetKinofilm().GetGenre(),
                                    in_localGenreListWoche0, in_localGenreListWoche1, in_localGenreListWoche2, wochenIndex);

                            if (breakstatement) {
                                break;
                            }
                        }

                        // Hinzufügen der Vorstellung zur filmWoche (für Ausgabenberechnung)
                        wochenKinofilme(vorstellung, wochenIndex);

                        // Andrangskalkulation
                        // Iteration über Eintrittspreise, lokale Optimierung
                        for (int eintrittspreis = 13; eintrittspreis <= 15; eintrittspreis++) { // TODO Range verkleinern für mehr Performance

                            // Backup
                            int backupEinnahmen = vorstellung.GetVorstellungTicketeinnahmen()[0] +
                                    vorstellung.GetVorstellungTicketeinnahmen()[1] +
                                    vorstellung.GetVorstellungWerbeeinnahmen();

                            int backupAndrang = vorstellung.GetAndrang();
                            int backupEintrittspreis = vorstellung.GetEintrittspreis();
                            vorstellung.SetEintrittspreis(eintrittspreis);

                            // Andrangsberechnung
                            vorstellung.SetAndrang((int) Math.round(basisAndrang(vorstellung) * uhrzeitAndrangFaktor(vorstellung) *
                                    wochenTagAndrangFaktor(tagIndex) * wiederholungAndrangFaktor(vorstellung, wochenIndex) *
                                    preisAndrangFaktor(vorstellung)));

                            // Ticketeinnahmen und Werbeeinnahmen
                            vorstellung.VorstellungsTicketEinnahmen();
                            vorstellung.VorstellungWerbeeinnahmen();


                            if (backupEinnahmen > (vorstellung.GetVorstellungTicketeinnahmen()[0] +
                                    vorstellung.GetVorstellungTicketeinnahmen()[1] +
                                    vorstellung.GetVorstellungWerbeeinnahmen())) {
                                vorstellung.SetEintrittspreis(backupEintrittspreis);
                                vorstellung.SetAndrang(backupAndrang);
                            }
                        }

                        // Einnahmenkalkulation für spätere Gewinnberechnung
                        spielplanTicketeinnahmen += vorstellung.GetVorstellungTicketeinnahmen()[0] +
                                vorstellung.GetVorstellungTicketeinnahmen()[1];
                        spielplanWerbeEinnahmen += vorstellung.GetVorstellungWerbeeinnahmen();

                        spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex] = vorstellung;
                    }
                }
                // Ausgaben Tagesabhängig bei parallelem Zeigen eines Films in unterschiedlichen Sälen.
                spielplanAusgaben += spielplanAusgabenParallel(spielplan[wochenIndex][tagIndex]);

            }
        }
        spielplanAusgaben += spielplanAusgabenGesamtzeitraum();

        return spielplan;
    }

    /**
     * Debugged
     */
    private boolean checkGenre(
            ArrayList<Genre> in_vorstellungsGenres,
            Set<Genre> in_localGenreListWoche0,
            Set<Genre> in_localGenreListWoche1,
            Set<Genre> in_localGenreListWoche2,
            int in_wochenindex) {

        switch (in_wochenindex) {
            case 0: {
                in_localGenreListWoche0.removeAll(in_vorstellungsGenres);
                break;
            }
            case 1: {
                if (!in_localGenreListWoche0.isEmpty()) {
                    return true;
                }
                in_localGenreListWoche1.removeAll(in_vorstellungsGenres);
                break;
            }
            case 2: {
                if (!in_localGenreListWoche1.isEmpty()) {
                    return true;
                }
                in_localGenreListWoche2.removeAll(in_vorstellungsGenres);
                break;
            }
        }

        if (in_localGenreListWoche2.isEmpty()) {
            checkGenre = true;
        } else {
            return false;
        }

        return false;// Catch all
    }

    /**
     * Fügt Kinofilme in Sets pro Woche zusammen
     */
    private void wochenKinofilme(Vorstellung in_Vorstellung, int in_WochenIndex) {
        switch (in_WochenIndex) {
            case 0: {
                filmeWoche0.add(in_Vorstellung.GetKinofilm());
                break;
            }

            case 1: {
                filmeWoche1.add(in_Vorstellung.GetKinofilm());
                break;
            }

            case 2: {
                filmeWoche2.add(in_Vorstellung.GetKinofilm());
                break;
            }
        }
    }

    /**
     * ermittlung ob ein Kinofilm parallel am selben tag in zwei unterschiedlichen Sälen läuft. Wenn ja, entsprechende
     * Kostenrückgabe
     * <p>
     * Funktion: Über hashset, wird versucht ein kinofilm hinzuzufügen, der bereits in der Liste ist, kann diese nicht
     * wachsen, wenn die Größte also unverändert bleibt ist der film doppelt vorhanden. + Fügt dem kinofilmkosten hinzu
     */
    private int spielplanAusgabenParallel(Vorstellung[][] in_VorstellungsTag) {

        Set<Kinofilm> slot1500 = new HashSet<>();
        Set<Kinofilm> slot1730 = new HashSet<>();
        Set<Kinofilm> slot2000 = new HashSet<>();
        Set<Kinofilm> slot2300 = new HashSet<>();

        int kosten = 0;

        for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
            for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                switch (in_VorstellungsTag[saalIndex][vorstellungIndex].GetSpielzeiten()) {
                    case SLOT_1500: {
                        int presize = slot1500.size();
                        slot1500.add(in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm());
                        if (slot1500.size() == presize) {
                            kosten += in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().GetVerleihpreisProWoche();
                            in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().InkrementGesamtkostenInSpielplan(1);
                        }
                        break;
                    }
                    case SLOT_1730: {
                        int presize = slot1730.size();
                        slot1730.add(in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm());
                        if (slot1730.size() == presize) {
                            kosten += in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().GetVerleihpreisProWoche();
                            in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().InkrementGesamtkostenInSpielplan(1);
                        }
                        break;
                    }
                    case SLOT_2000: {
                        int presize = slot2000.size();
                        slot2000.add(in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm());
                        if (slot2000.size() == presize) {
                            kosten += in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().GetVerleihpreisProWoche();
                            in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().InkrementGesamtkostenInSpielplan(1);
                        }
                        break;
                    }
                    case SLOT_2300: {
                        int presize = slot2300.size();
                        slot2300.add(in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm());
                        if (slot2300.size() == presize) {
                            kosten += in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().GetVerleihpreisProWoche();
                            in_VorstellungsTag[saalIndex][vorstellungIndex].GetKinofilm().InkrementGesamtkostenInSpielplan(1);
                        }
                        break;
                    }
                }
            }
        }
        return kosten;
    }

    /**
     * Ermittlung der Kosten für die Filmleite aller drei wochen mit rabattverrechnung.
     *
     * @return
     */
    private int spielplanAusgabenGesamtzeitraum() {
        int kosten = 0;

        /*
        Jeder Film, der in der Woche gespielt wird, wird zu einer Arraylist hinzugefügt. So sind die Filme, die
        mehrere Wochen gespielt werden doppelt vertreten und müssen Doppelt gezahlt werden. Wenn Filme innerhalb
        einer Woche mehrmals gezeigt werdem, fällt das hier nicht ins Gewicht, da es sich beim Datenytp um
        Hashsets handelt.
         */
        ArrayList<Kinofilm> alleFilme = new ArrayList<>();
        alleFilme.addAll(filmeWoche0);
        alleFilme.addAll(filmeWoche1);
        alleFilme.addAll(filmeWoche2);

        for (Kinofilm kinofilm : alleFilme) {
            kosten += kinofilm.GetVerleihpreisProWoche();
            kinofilm.InkrementGesamtkostenInSpielplan(1);
        }

        // Identifizieren der Filme die alle drei wochen Gespielt werden.
        HashMap<Kinofilm, Integer> dreifachFilme = new HashMap<>();
        for (Kinofilm film : alleFilme) {
            if (dreifachFilme.containsKey(film)) {
                dreifachFilme.put(film, dreifachFilme.get(film) + 1);
            } else {
                dreifachFilme.put(film, 1);
            }
        }

        /*
        Filme, die ale drei Wochen gespielt werden erhalten einen 10% Rabat auf den Verleihpreis in jeder Woche
        Also 3 * 0.1 = 0.3 = 30% auf den Verleihpreis (einmalig)
        */
        for (Map.Entry<Kinofilm, Integer> film : dreifachFilme.entrySet()) {
            if (film.getValue() == 3) {
                kosten -= Math.round(film.getKey().GetVerleihpreisProWoche() * 0.3);
                film.getKey().InkrementGesamtkostenInSpielplan(-0.3);
            }
        }
        return kosten;
    }

    /**
     * Berechnung des Basisandrangs basierend auf der Beliebtheit eines Films
     *
     * @param in_Vorstellung Vorstellung für Andrangsberechnung
     * @return Basisandrang der Vorstellung
     */
    private int basisAndrang(Vorstellung in_Vorstellung) {
        return (int) Math.round((plaetzeGroesterSaal + plaetzeZweitgroesterSaal) *
                ((double) (in_Vorstellung.GetKinofilm().GetBeliebtheit()) / 85));
    }

    /**
     * Ermittelt den uhrzeitabhängigen Faktor auf den Andrang.
     *
     * @param in_Vorstellung eine Vorstellung
     * @return Einflussfaktor auf den Vorstellungsandrang. [0.85, 0.9, 0.95, 1]
     */
    private double uhrzeitAndrangFaktor(Vorstellung in_Vorstellung) {

        switch (in_Vorstellung.GetSpielzeiten()) {
            case SLOT_1500: {
                return .9;
            }

            case SLOT_1730: {
                return .95;
            }

            case SLOT_2300: {
                return .85;
            }

            default:
                return 1;
        }
    }

    /**
     * Ermittelt den wochentagsabhängigen Faktor auf den Vorstellungsandrang.
     *
     * @param in_tagIndex der Index des jeweiligen Tages.
     * @return Faktor auf den Vorstellungsandrang in Abhängigkeit vom Wochentag. [0.6, 0.8, 1]
     */
    private double wochenTagAndrangFaktor(int in_tagIndex) {

        //Dienstag, Mittwoch, Donnerstag 60%
        if ((in_tagIndex > 0 && in_tagIndex < 4) || (in_tagIndex > 7 && in_tagIndex < 11) || (in_tagIndex > 14 && in_tagIndex < 18)) {
            return .6;
        }

        //Freitag, Samstag, Sonntag 80%
        else if ((in_tagIndex > 3 && in_tagIndex < 7) || (in_tagIndex > 10 && in_tagIndex < 14) || (in_tagIndex > 17 && in_tagIndex < 21)) {
            return .8;
        }

        //Montag und Catch-All 100%
        else {
            return 1;
        }
    }

    /**
     * Ermittelt ob ein Kinofilm bereits in vorherigen Wochen gezeigt wurde und ermittelt den entsprechenden Faktor
     * auf den Vorstellungsandrang.
     *
     * @param in_Vorstellung eien Vorstellung
     * @param in_WochenIndex der Index der Woche in der die Vorstellung stattfindet.
     * @return Faktor auf den Andrang [0.5, 0.8, 1]
     */
    private double wiederholungAndrangFaktor(Vorstellung in_Vorstellung, int in_WochenIndex) {
        /*
         * In Woche 0 kann es keine Abzüge geben, da die Filme zum ersten Mal gezeigt werden.
         * In Woche 1 kommt es zu abzügen, wenn der Film bereits in Woche 0 gezeigt wurde.
         * In Woche 2 kommt es zu den selben Abzügen wie in Woche 1, wenn der Film in Woche 1 ODER in Woche 0
         *  gezeigt wurde. Wenn der Film sowohl in Woche 0 als auch Woche 1 gezeigt wurde kommt es zu stärkeren
         *  abzügen.
         */

        switch (in_WochenIndex) {

            case 1: {
                if (filmeWoche0.contains(in_Vorstellung.GetKinofilm())) {
                    return 0.8;
                } else {
                    return 1;
                }
            }

            case 2: {
                if ((filmeWoche0.contains(in_Vorstellung.GetKinofilm()) && !filmeWoche1.contains(in_Vorstellung.GetKinofilm())) ||
                        (!filmeWoche0.contains(in_Vorstellung.GetKinofilm())) && filmeWoche1.contains((in_Vorstellung.GetKinofilm()))) {
                    return 0.8;
                } else if (filmeWoche0.contains(in_Vorstellung.GetKinofilm()) && filmeWoche1.contains(in_Vorstellung.GetKinofilm())) {
                    return 0.5;
                } else {
                    return 1;
                }
            }
            default:
                return 1;
        }
    }

    /**
     * Ermittelt den preisabhängigen Faktor auf den Andrang einer Vorstellung.
     *
     * @param in_Vorstellung eine Vorstellung
     * @return Faktor auf den Andrang in abhängigkeit vom Eintrittspreis.
     */

    private double preisAndrangFaktor(Vorstellung in_Vorstellung) {

        if (in_Vorstellung.GetEintrittspreis() > 7) {
            return (1 - (in_Vorstellung.GetEintrittspreis() - 7) * 0.05);
        } else if (in_Vorstellung.GetEintrittspreis() < 7) {
            return (1 + (7 - in_Vorstellung.GetEintrittspreis()) * 0.02);
        } else {
            return 1;
        }
    }

    //Getter
    public Vorstellung[][][][] GetSpielplan() {
        return spielplan;
    }

    public int GetSpielplanAusgaben() {
        return spielplanAusgaben;
    }

    public int GetSpielplanTicketeinnahmen() {
        return spielplanTicketeinnahmen;
    }

    public int GetSpielplanWerbeEinnahmen() {
        return spielplanWerbeEinnahmen;
    }

    public int GetSpielplanGewinn() {
        return spielplanGewinn;
    }

    public int GetAnzahlSaele() {
        return anzahlSaele;
    }
}