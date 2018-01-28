package Gruppe7.Logic;

import java.util.*;

import Gruppe7.Data.*;
import Gruppe7.Main;

/**
 * @author Lennart Völler
 * @date 24.01.2018
 *
 * Die Planerklasse stellt die zentrale Logik des Programs dar. Jedes Objekt der Klasse Planer beinhaltet ein
 * 4-dimensionales Array vom Typ Vorstellung. Nach der Erstellung eines zufälligen Spielplans wird dieser lokal
 * optimiert. Ist der Optimierungsprozess abgeschlossen beendes der Planer den Konstruktor und gibt den optimierten
 * Spielplan zurück.
 */
public class Planer {
    // Saaldaten
    private int plaetzeGroesterSaal = SaalVerwaltung.getPlaetzeGroesterSaal();
    private int plaetzeZweitgroesterSaal = SaalVerwaltung.getPlaetzeZweitgroesterSaal();
    private int anzahlSaele = SaalVerwaltung.getSize();

    // Finanzdaten
    private int spielplanEinnahmenAusKartenverkaeufen = 0;
    private int spielplanWerbungsEinnahmen = 0;
    private int spielplanAusgaben = 0;
    private int spielplanGewinn = 0;

    public ArrayList<Kinofilm> alleFilmeFinancials = new ArrayList<Kinofilm>(); //Nicole und Fabian

    // Spielplandaten @TODO: Vorstellungsarray kann nicht mit festen Werten Erstellt werden, da sich die Importdateien verändern können.
    private Vorstellung[][][][] spielplan = new Vorstellung[3][7][anzahlSaele][4]; //Spielplan ist ein Array der Länge 3(Wochen) * 7(Tage) * Anzahl der Säle *  4(Spielzeiten)

    //region Vorstellungen
    private Set<Kinofilm> filmeWoche0 = new HashSet<>();
    private Set<Kinofilm> filmeWoche1 = new HashSet<>();
    private Set<Kinofilm> filmeWoche2 = new HashSet<>();

    private Set<Vorstellung> vorstellungen0 = new HashSet<>();
    private Set<Vorstellung> vorstellungen1 = new HashSet<>();
    private Set<Vorstellung> vorstellungen2 = new HashSet<>();
    private Set<Vorstellung> vorstellungen3 = new HashSet<>();
    private Set<Vorstellung> vorstellungen4 = new HashSet<>();
    private Set<Vorstellung> vorstellungen5 = new HashSet<>();
    private Set<Vorstellung> vorstellungen6 = new HashSet<>();
    private Set<Vorstellung> vorstellungen7 = new HashSet<>();
    private Set<Vorstellung> vorstellungen8 = new HashSet<>();
    private Set<Vorstellung> vorstellungen9 = new HashSet<>();
    private Set<Vorstellung> vorstellungen10 = new HashSet<>();
    private Set<Vorstellung> vorstellungen11 = new HashSet<>();
    private Set<Vorstellung> vorstellungen12 = new HashSet<>();
    private Set<Vorstellung> vorstellungen13 = new HashSet<>();
    private Set<Vorstellung> vorstellungen14 = new HashSet<>();
    private Set<Vorstellung> vorstellungen15 = new HashSet<>();
    private Set<Vorstellung> vorstellungen16 = new HashSet<>();
    private Set<Vorstellung> vorstellungen17 = new HashSet<>();
    private Set<Vorstellung> vorstellungen18 = new HashSet<>();
    private Set<Vorstellung> vorstellungen19 = new HashSet<>();
    private Set<Vorstellung> vorstellungen20 = new HashSet<>();


    private ArrayList<Set<Vorstellung>> vorstellungTage =
            new ArrayList<>(Arrays.asList(
                    vorstellungen0, vorstellungen1, vorstellungen2, vorstellungen3, vorstellungen4,
                    vorstellungen5, vorstellungen6, vorstellungen7, vorstellungen8, vorstellungen9,
                    vorstellungen10, vorstellungen11, vorstellungen12, vorstellungen13, vorstellungen14,
                    vorstellungen15, vorstellungen15, vorstellungen16, vorstellungen17, vorstellungen18,
                    vorstellungen19, vorstellungen20));
    //endregion

    // Genredaten
    private static List<Genre> genreList = Arrays.asList(Genre.values()); // Generiert eine Genre-List aus dem Genre Enum unabhängig vom Objekt
    private boolean checkGenre = false;

    /**
     * Erstellung eines zufälligen Spielplans bei Iteration durch das leere Vorstellungs-Array.
     * Erstellt:  Ein vierdimensionales Vorstellungsarray [woche][tag][saal][timeslot]
     * @param in_minPreisFuerVorstellung der Mindestpreis einer Vorstellung für die Vorstellungs-optimierung.
     * @param in_maxPreisfuerVorstellung der Maximalpreis einer Vorstellung für die Vorstellungs-optimierung.
     */
    public Planer(int in_minPreisFuerVorstellung, int in_maxPreisfuerVorstellung) {

        // Genre-Liste wird kopiert
        ArrayList<Genre> localGenreListWoche0 = new ArrayList<>();
        ArrayList<Genre> localGenreListWoche1 = new ArrayList<>();
        ArrayList<Genre> localGenreListWoche2 = new ArrayList<>();
        localGenreListWoche0.addAll(genreList);
        localGenreListWoche1.addAll(genreList);
        localGenreListWoche2.addAll(genreList);

        while (!checkGenre) {
            spielplan = createRandomSpielplan(localGenreListWoche0, localGenreListWoche1, localGenreListWoche2);
        }

        // Aufspaltung der Vorstellungen in drei 1-D Arrays zur weiteren Verarbeitung
        spielplanAufspaltung();

        // Optimierung des Vorstellungspreises jeder Vorstellung (Switch True/ false)
        if (Main.OptimierungSwitch) {
            spielplanEinnahmenOptimierung(in_minPreisFuerVorstellung, in_maxPreisfuerVorstellung);
        }

        spielplanAusgaben = spielplanAusgaben();
        int[] spielplanEinnahmen = spielplanEinnahmen(spielplan);
        spielplanEinnahmenAusKartenverkaeufen = spielplanEinnahmen[0];
        spielplanWerbungsEinnahmen = spielplanEinnahmen[1];
        spielplanGewinn = spielplanEinnahmenAusKartenverkaeufen - spielplanAusgaben + spielplanWerbungsEinnahmen;
    }

    /**
     * Aufspaltung des Spielplans in Vorstellungen und Kinofilme
     */
    private void spielplanAufspaltung() {
        // Alle vorstellungen jeder Woche werden in je einer Liste zusammengefasst.
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            for (int tagesIndex = 0; tagesIndex < 7; tagesIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungsIndex = 0; vorstellungsIndex < 4; vorstellungsIndex++) {

                        switch (wochenIndex) {
                            case 0:{
                                filmeWoche0.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex].GetKinofilm());
                                break;
                            }

                            case 1:{
                                filmeWoche1.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex].GetKinofilm());
                                break;
                            }

                            case 2:{
                                filmeWoche2.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex].GetKinofilm());
                                break;
                            }
                        }

                        int wochenIndexAddition = 0;
                        if(wochenIndex == 1)
                            wochenIndexAddition += 7;
                        else if (wochenIndex == 2)
                            wochenIndexAddition += 14;

                        switch ((tagesIndex + wochenIndexAddition)) {
                            case 0: {
                                vorstellungen0.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 1: {
                                vorstellungen1.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 2: {
                                vorstellungen2.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 3: {
                                vorstellungen3.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 4: {
                                vorstellungen4.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 5: {
                                vorstellungen5.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 6: {
                                vorstellungen6.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 7: {
                                vorstellungen7.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 8: {
                                vorstellungen8.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 9: {
                                vorstellungen9.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 10: {
                                vorstellungen10.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 11: {
                                vorstellungen11.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 12: {
                                vorstellungen12.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 13: {
                                vorstellungen13.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 14: {
                                vorstellungen14.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 15: {
                                vorstellungen15.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 16: {
                                vorstellungen16.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 17: {
                                vorstellungen17.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 18: {
                                vorstellungen18.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 19: {
                                vorstellungen19.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                            case 20: {
                                vorstellungen20.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**Debugged
     */
    private boolean checkGenre(
            ArrayList<Genre> in_vorstellungsGenres,
            ArrayList<Genre> in_localGenreListWoche0,
            ArrayList<Genre> in_localGenreListWoche1,
            ArrayList<Genre> in_localGenreListWoche2,
            int in_wochenindex) {

        switch (in_wochenindex){
            case 0:{
                in_localGenreListWoche0.removeAll(in_vorstellungsGenres);
                break;
            }
            case 1:{
                if (!in_localGenreListWoche0.isEmpty())
                {return true; }
                in_localGenreListWoche1.removeAll(in_vorstellungsGenres);
                break;
            }
            case 2:{
                if (!in_localGenreListWoche1.isEmpty())
                {return true; }
                in_localGenreListWoche2.removeAll(in_vorstellungsGenres);
                break;
            }
        }

        if (in_localGenreListWoche2.isEmpty()) {
            checkGenre = true;
        }
        else {return false;}

        return false;// Catch all
    }

    /**Debugged
     */
    private Vorstellung[][][][] createRandomSpielplan(ArrayList<Genre> in_localGenreListWoche0,
                                                      ArrayList<Genre> in_localGenreListWoche1,
                                                      ArrayList<Genre> in_localGenreListWoche2) {

        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex] = new Vorstellung(saalIndex, vorstellungIndex);

                        boolean breakstatement;

                        if (!checkGenre) {
                            breakstatement = checkGenre(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].GetKinofilm().GetGenre(),
                                    in_localGenreListWoche0, in_localGenreListWoche1, in_localGenreListWoche2, wochenIndex);

                            if (breakstatement) {
                                break;
                            }
                        }
                    }
                }
            }
        return spielplan;
    }

    /**Debugged
     * Errechnet die durch einen Spielplan entstehenden Einnahmen. Greift dabei auf die Optimierung
     * dees Vorstellungspreises zu. Überhängende plätze in der Loge gehen ins Parkett
     * @param spielplan
     * @return array [Einnahmen aus Ticketverkauf][Einnahmen aus Werbung]
     */
    private int[] spielplanEinnahmen(Vorstellung[][][][] spielplan) {
        int[] localSpielplaneinnahmen = {0, 0};
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        // Wählt die aktuelle Vorstellung
                        Vorstellung vorstellung = spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex];
                        int eintrittspreis = vorstellung.GetEintrittspreis();

                        // Berechnet den Andrang für die aktuelle Vorstellung
                        int andrang = andrang(vorstellung, tagIndex, vorstellungIndex, wochenIndex, eintrittspreis);
                        int andrang50p = (int) Math.round((double) andrang * 0.5);
                        int zuschauerLoge;
                        int zuschauerParkett;
                        int ueberhang = 0;

                        // Andrang in der Loge. Wenn Andrang > Plätze: Andrang = Plätze + Überhang
                        if (andrang50p > SaalVerwaltung.getSaele().get(saalIndex).GetPlaetzeLoge()) {
                            zuschauerLoge = SaalVerwaltung.getSaele().get(saalIndex).GetPlaetzeLoge();

                            ueberhang = andrang50p - zuschauerLoge;

                        } else {
                            zuschauerLoge = andrang50p;
                        }

                        //Andrang im Parkett. Wenn Andrang < Plätze: Plätze = Andrang + Überhang
                        if (andrang50p > SaalVerwaltung.getSaele().get(saalIndex).GetPlaetzeParkett()) {
                            zuschauerParkett = SaalVerwaltung.getSaele().get(saalIndex).GetPlaetzeParkett();
                        } else {
                            zuschauerParkett = andrang50p;

                            int freiePlaetze = ((SaalVerwaltung.getSaele().get(saalIndex).GetPlaetzeParkett() - zuschauerParkett));

                            if (freiePlaetze <= ueberhang) {
                                zuschauerParkett += freiePlaetze;
                            } else {
                                zuschauerParkett += ueberhang;
                            }
                        }

                        //Einnahmen durch Ticketsverkäufe
                        int ticketverkaeufeLoge = (eintrittspreis + 2) * zuschauerLoge;
                        int ticketverkaeufeParkett = eintrittspreis * zuschauerParkett;

                        localSpielplaneinnahmen[0] += ticketverkaeufeLoge + ticketverkaeufeParkett;

//                        //@TODO for Schleife über die länge der WerbeListe
//                        /**@author  Nicole & Fabian
//                           Hier werden die Einnahmen je Werbespot pro Vorstellung ermittelt und ins Objekt Werbefilm gespeichert.
//                           Wird für den Finanzplan benötigt.
//                         */
//                        for(Werbung werbung : vorstellung.GetWerbefilme()){
//                        vorstellung.GetWerbefilme().get(iWerbung).setEinnahmenProWerbeSpot((vorstellung.GetZuschauerGesamt()*vorstellung.getWerbefilme().get(iWerbung).getUmsatzProZuschauer()));
//                        }
//
                        //Einnahmen aus Werbung
                        for (Werbefilm werbung : vorstellung.GetWerbefilme()) {
                            localSpielplaneinnahmen[1] += werbung.getUmsatzProZuschauer() * (zuschauerLoge + zuschauerParkett);
                        }
                    }
                }
            }
        }
        return localSpielplaneinnahmen;
    }

    /**Debugged
     * Geht für jede Vorstellung durch den Spielplan unv sucht den Eintrittspreis, mit dem sich der Gewinn für
     * diese Vorstellung optimieren lässt nutzt dafür den überladenen Konstruktor von Vorstellung
     * @param in_minPreisFuerVorstellung
     * @param in_maxPreisFuerVorstellung
     */
    private void spielplanEinnahmenOptimierung(int in_minPreisFuerVorstellung, int in_maxPreisFuerVorstellung) {

        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        int besterVorstellungspreis = spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].GetEintrittspreis();
                        int besteSpielplanEinnahmen= spielplanEinnahmen(spielplan)[0] + spielplanEinnahmen(spielplan)[1];

                        // Für jede Vorstellung werden alle Eintrittspreise innerhalb der Range ausprobiert, um den besten zu finden.
                        for (int eintrittsPreis = in_minPreisFuerVorstellung; eintrittsPreis <= in_maxPreisFuerVorstellung; eintrittsPreis++) {

                            //Neuer Vorstellungspreis wird gesetzt.
                            spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].SetEintrittspreis(eintrittsPreis);

                            //Wenn sich die Einnahmen des Spielplans durch die Vorstellung verbessern, wird der temporäre plan zum besten Plan.
                            if ((spielplanEinnahmen(spielplan)[0] + spielplanEinnahmen(spielplan)[1]) < besterVorstellungspreis){
                                spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].SetEintrittspreis(besterVorstellungspreis);
                            }
                        }
                    }
                }
            }
        }
    }

    /**Debugged
     * Berechnet den Andrang, der für eine Vorstellung zu erwartetn ist.
     */
    private int andrang(Vorstellung vorstellung, int in_tagIndex, int in_vorstellungIndex, int in_wochenIndex, int eintrittspreis) {

        //Berechnung des Basisandrangs über die größe der beiden größten Säle.
        int basisandrang = (int) Math.round((plaetzeGroesterSaal + plaetzeZweitgroesterSaal) *
                ((double) (vorstellung.GetKinofilm().GetBeliebtheit()) / 85));

        //region Einfluss der Uhrzeit auf den Andrang
        int zeitabhaengigerAndrang;
        switch (in_vorstellungIndex) {
            //15 Uhr Vorstellung 90%
            case 0: {
                zeitabhaengigerAndrang = (int) Math.round(basisandrang * .9);
                break;
            }

            //17:30 Uhr Vorstellung 95%
            case 1: {
                zeitabhaengigerAndrang = (int) Math.round(basisandrang * .95);
                break;
            }

            //23 Uhr Vorstellung 85%
            case 3: {
                zeitabhaengigerAndrang = (int) Math.round(basisandrang * .85);
                break;
            }

            //20 Uhr und Catch-All 100%
            default:
                zeitabhaengigerAndrang = basisandrang;
        }
        //endregion

        //region Einfluss des Wochentages auf den Andrang
        int zeitUndTagesabhaengigerAndrang;
        //Dienstag, Mittwoch, Donnerstag 60%
        if ((in_tagIndex > 0 && in_tagIndex < 4) || (in_tagIndex > 7 && in_tagIndex < 11) || (in_tagIndex > 14 && in_tagIndex < 18)) {
            zeitUndTagesabhaengigerAndrang = (int) Math.round(zeitabhaengigerAndrang * .6);
        }

        //Freitag, Samstag, Sonntag 80%
        else if ((in_tagIndex > 3 && in_tagIndex < 7) || (in_tagIndex > 10 && in_tagIndex < 14) || (in_tagIndex > 17 && in_tagIndex < 21)) {
            zeitUndTagesabhaengigerAndrang = (int) Math.round(zeitabhaengigerAndrang * .8);
        }

        //Montag und Catch-All 100%
        else {
            zeitUndTagesabhaengigerAndrang = zeitabhaengigerAndrang;
        }
        //endregion

        //region Einfluss der Wiederholung von Filmen auf den Andrang
        /*
         * Einbezug der anderen Wochen:
         * In Woche 0 kann es keine Abzüge geben, da die Filme zum ersten Mal gezeigt werden.
         * In Woche 1 kommt es zu abzügen, wenn der Film bereits in Woche 0 gezeigt wurde.
         * In Woche 2 kommt es zu den selben Abzügen wie in Woche 1, wenn der Film in Woche 1 ODER in Woche 0
         *  gezeigt wurde. Wenn der Film sowohl in Woche 0 als auch Woche 1 gezeigt wurde kommt es zu stärkeren
         *  abzügen.
         */

        int zeitUndTagesUndWiederholungsabhaengigerAndrang;
        switch (in_wochenIndex) {

            case 1: {
                if (filmeWoche0.contains(vorstellung.GetKinofilm())) {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = (int) Math.round(zeitUndTagesabhaengigerAndrang * 0.8);
                } else {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
                }
                break;
            }

            case 2: {
                if ((filmeWoche0.contains(vorstellung.GetKinofilm()) && !filmeWoche1.contains(vorstellung.GetKinofilm())) ||
                        (!filmeWoche0.contains(vorstellung.GetKinofilm())) && filmeWoche1.contains((vorstellung.GetKinofilm()))) {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = (int) Math.round(zeitUndTagesabhaengigerAndrang * 0.8);
                } else if (filmeWoche0.contains(vorstellung.GetKinofilm()) && filmeWoche1.contains(vorstellung.GetKinofilm())) {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = (int) Math.round(zeitUndTagesabhaengigerAndrang * 0.5);
                } else {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
                }
                break;
            }
            default:
                zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
                break;
        }
        //endregion

        int zeitUndTagesUndWiederholungsUndPreisabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;

        //region Einfluss des Preises auf den Andrang
        if (eintrittspreis > 7) {
            zeitUndTagesUndWiederholungsUndPreisabhaengigerAndrang =
                    (int) Math.round(zeitUndTagesUndWiederholungsabhaengigerAndrang * (1 - (eintrittspreis - 7) * 0.05));
        } else if (eintrittspreis < 7) {
            zeitUndTagesUndWiederholungsUndPreisabhaengigerAndrang =
                    (int) Math.round(zeitUndTagesUndWiederholungsabhaengigerAndrang * (1 + (7 - eintrittspreis) * 0.02));
        }
        vorstellung.SetAndrang(zeitUndTagesUndWiederholungsUndPreisabhaengigerAndrang);
        return zeitUndTagesUndWiederholungsUndPreisabhaengigerAndrang;
        //endregion
    }

    /**Wochenweise Errechnung der Spielplankosten
     */
    private int spielplanAusgaben() {

        int kosten = 0;

        // Filme, die zur gleichen Zeit in unterschiedlichen Sälen gezeigt werdend doppelt buchen.
        for (Set<Vorstellung> vorStellungsTag : vorstellungTage) {
            Set<Kinofilm> slot1500 = new HashSet<>();
            Set<Kinofilm> slot1730 = new HashSet<>();
            Set<Kinofilm> slot2000 = new HashSet<>();
            Set<Kinofilm> slot2300 = new HashSet<>();

            for (Vorstellung vorstellung : vorStellungsTag) {
                switch (vorstellung.GetSpielzeiten()) {
                    case SLOT_1500: {
                        int presize = slot1500.size();
                        slot1500.add(vorstellung.GetKinofilm());
                        if (slot1500.size() == presize) {
                            kosten += vorstellung.GetKinofilm().GetVerleihpreisProWoche();
                        }
                        break;
                    }
                    case SLOT_1730: {
                        int presize = slot1730.size();
                        slot1730.add(vorstellung.GetKinofilm());
                        if (slot1730.size() == presize) {
                            kosten += vorstellung.GetKinofilm().GetVerleihpreisProWoche();
                        }
                        break;
                    }
                    case SLOT_2000: {
                        int presize = slot2000.size();
                        slot2000.add(vorstellung.GetKinofilm());
                        if (slot2000.size() == presize) {
                            kosten += vorstellung.GetKinofilm().GetVerleihpreisProWoche();
                        }
                        break;
                    }
                    case SLOT_2300: {
                        int presize = slot2300.size();
                        slot2300.add(vorstellung.GetKinofilm());
                        if (slot2300.size() == presize) {
                            kosten += vorstellung.GetKinofilm().GetVerleihpreisProWoche();
                        }
                        break;
                    }
                }
            }
        }


        //Zusammenfassung aller Kinofilme der drei Wochen (mit doppelterfassung, wenn Filme in mehreren Wochen vorkommen)
        ArrayList<Kinofilm> alleFilme;
        alleFilme = new ArrayList<>();
        alleFilme.addAll(filmeWoche0);
        alleFilme.addAll(filmeWoche1);
        alleFilme.addAll(filmeWoche2);

        alleFilmeFinancials.addAll(alleFilme);

        for (Kinofilm kinofilm : alleFilme) {
            kosten += kinofilm.GetVerleihpreisProWoche();
        }

        // Identifizieren der Filme die alle drei wochen Gespielt werden.
        HashMap<Kinofilm, Integer> dreifachFilme = new HashMap<Kinofilm, Integer>();
        for (Kinofilm film : alleFilme) {
            if (dreifachFilme.containsKey(film)) {
                dreifachFilme.put(film, dreifachFilme.get(film) + 1);
            } else {
                dreifachFilme.put(film, 1);
            }
        }

        // Geht durch die Hashmap und sicht ide Filme die drei mal vorkommen. Entsprechende rabattverrechnung
        for(Map.Entry<Kinofilm, Integer> film: dreifachFilme.entrySet()){
            if (film.getValue() == 3){
                kosten -= film.getKey().GetVerleihpreisProWoche() * 0.1;
            }
        }
        return kosten;
    }

    //Getter
    public Vorstellung[][][][] getSpielplan() {
        return spielplan;
    }

    public int getSpielplanEinnahmenAusKartenverkaeufen() {
        return spielplanEinnahmenAusKartenverkaeufen;
    }

    public int getSpielplanAusgaben() {
        return spielplanAusgaben;
    }

    public int getSpielplanGewinn() {
        return spielplanGewinn;
    }

    public int getSpielplanWerbungsEinnahmen() {
        return spielplanWerbungsEinnahmen;
    }

}