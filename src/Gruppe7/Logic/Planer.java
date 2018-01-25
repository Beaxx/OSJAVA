package Gruppe7.Logic;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;

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
public class Planer
{
    private int anzahlSaele = SaalVerwaltung.getSize();

    //Spielplan ist ein Array der Länge 3(Wochen) * 7(Tage) * Anzahl der Säle *  4(Spielzeiten)
    private Vorstellung[][][][] spielplan = new Vorstellung[3][7][anzahlSaele][4];

    private int spielplanEinnahmenAusKartenverkaeufen = 0;
    private int spielplanAusgaben = 0;
    private int spielplanGewinn = 0;
    private int spielplanWerbungsEinnahmen = 0;
    private int[] plaetzteInGroestemUndZweitgroestemSaal = plaetzteInGroestemUndZweitgroestemSaal();

    private ArrayList<Vorstellung> woche0 = new ArrayList<>();
    private ArrayList<Vorstellung> woche1 = new ArrayList<>();
    private ArrayList<Vorstellung> woche2 = new ArrayList<>();

    private boolean checkGenre = false;

    /**
     * Erstellung eines zufälligen Spielplans bei Iteration durch das leere Vorstellungs-Array
     * @return Ein vierdimensionales Vorstellungsarray [woche][tag][saal][timeslot]
     */
    public Planer(){
        // Liste aus Enum
        List<Genre> genreList = Arrays.asList(Genre.values());

        // Kopie der Liste
        ArrayList<Genre> localGenreList = new ArrayList<>();
            localGenreList.addAll(genreList);

        while (!checkGenre) // TODO: Schleife, die für x-Iterationen Vorstellungen im Spielplan austauscht bevor ein enuer erzeugt wird.
            spielplan = createRandomSpielplan(localGenreList);

        if (checkGenre) {
            spielplanAufspaltung();
            spielplanAusgaben = spielplanAusgaben();
            spielplanEinnahmenAusKartenverkaeufen = spielplanEinnahmen(spielplan)[0];
            spielplanWerbungsEinnahmen = spielplanEinnahmen(spielplan)[1];
            spielplanGewinn = spielplanEinnahmenAusKartenverkaeufen - spielplanAusgaben + spielplanWerbungsEinnahmen;
        }
    }

    private void spielplanAufspaltung(){
        // Alle vorstellungen jeder Woche werden in je einer Liste zusammengefasst.
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            for (int tagesIndex = 0; tagesIndex < 7; tagesIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungsIndex = 0; vorstellungsIndex < 4; vorstellungsIndex++) {

                        switch (wochenIndex) {
                            case 0:
                                woche0.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            case 1:
                                woche1.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                            case 2:
                                woche2.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex]);
                                break;
                        }
                    }
                }
            }
        }
    }

    //Check Genre Methode
    private boolean checkGenre(ArrayList<Genre> vorstellungsGenres, ArrayList<Genre> localGenreList) {

        for (Genre genre: vorstellungsGenres){
            localGenreList.removeIf(g -> g == genre);
            localGenreList.remove(Genre.DRAMA); // TODO: Wenn nur noch das Genre Drama übrig ist werden ur noch Vorstellungen anderer Genres erzeugt.
        }
        if (localGenreList.isEmpty()){
            checkGenre = true;
            return true;
        }
        else{return false;}
    }

    /**
     * Erstellt einen zufälligen Spielplan
     * @return Ein zufälliger Spielplan
     */
    private Vorstellung[][][][] createRandomSpielplan(ArrayList<Genre> localGenreList) {
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex] = new Vorstellung(saalIndex, vorstellungIndex);
                        if (!checkGenre) {
                            checkGenre(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].getKinofilm().getGenre(), localGenreList);
                        }
                    }
                }
            }
            return spielplan;
        } // TODO: Genrecheck effizienter?

    /**
     * Berechnet die zu erwartenden Einnahmen aus Ticketverkäufen und Werbung einer Vorstellung
     * @param spielplan der Spielplan für den die Einnahmen zu berechnen sind
     * @return ein Array mit [Einnahmen durch Kartenverkäufe][Einnahmen durch Werbung]
     */
    private int[] spielplanEinnahmen(Vorstellung[][][][] spielplan) {
        int[] localSpielplaneinnahmen = {0, 0};
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        Vorstellung vorstellung = spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex];
                        int eintrittspreis = vorstellung.getEintrittspreis();
                        int andrang = andrang(vorstellung, tagIndex, vorstellungIndex, wochenIndex, eintrittspreis);
                        int andrangLoge;
                        int andrangParkett;

                        // Andrang in der Loge
                        if (andrang * 0.5 > SaalVerwaltung.getSaele().get(saalIndex).getPlaetzeLoge()){
                            andrangLoge = SaalVerwaltung.getSaele().get(saalIndex).getPlaetzeLoge();
                        }
                        else { andrangLoge = (int)Math.round((double)andrang * 0.5); }

                        //Andrang im Parkett
                        if (andrang * 0.5 > SaalVerwaltung.getSaele().get(saalIndex).getPlaetzeParkett()){
                            andrangParkett = SaalVerwaltung.getSaele().get(saalIndex).getPlaetzeParkett();
                        }
                        else { andrangParkett = (int)Math.round((double)andrang * 0.5); }

                        //Einnahmen durch Ticketsverkäufe
                        int ticketverkaeufeLoge = (eintrittspreis + 2) * andrangLoge;
                        int ticketverkaeufeParkett = eintrittspreis * andrangParkett;

                        localSpielplaneinnahmen[0] += ticketverkaeufeLoge + ticketverkaeufeParkett;

                        //Einnahmen aus Werbung
                        for (Werbefilm werbung : vorstellung.getWerbefilme()) {
                            localSpielplaneinnahmen[1] += werbung.getUmsatzProZuschauer() * (andrangLoge + andrangParkett);
                        }
                    }
                }
            }
        return localSpielplaneinnahmen;
    }

    private int andrang(Vorstellung vorstellung, int in_tagIndex, int in_vorstellungIndex, int in_wochenIndex, int eintrittspreis) {

        //Berechnung des Basisandrangs über die größe der beiden größten Säle.
        int basisandrang = (int) Math.round((plaetzteInGroestemUndZweitgroestemSaal[0] + plaetzteInGroestemUndZweitgroestemSaal[1]) *
                ((double) (vorstellung.getKinofilm().getBeliebtheit()) / 85));


        //region Einfluss der Uhrzeit auf den Andrang
        int zeitabhaengigerAndrang;
        switch (in_vorstellungIndex){
            //15 Uhr Vorstellung 90%
            case 0: {zeitabhaengigerAndrang = (int) Math.round(basisandrang * .9);
                break;}

            //17:30 Uhr Vorstellung 95%
            case 1: {zeitabhaengigerAndrang = (int) Math.round(basisandrang * .95);
                break;}

            //23 Uhr Vorstellung 85%
            case 3: {zeitabhaengigerAndrang = (int) Math.round(basisandrang * .85);
                break;}

            //20 Uhr und Catch-All 100%
            default: zeitabhaengigerAndrang = basisandrang;
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

        int zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
        switch (in_wochenIndex) {

            case 1: {
                if (woche0.contains(vorstellung.getKinofilm())) {
                    zeitUndTagesabhaengigerAndrang =  (int) Math.round(zeitUndTagesabhaengigerAndrang * 0.8);
                } else {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
                }
                break;
            }


            case 2: {
                if ((woche0.contains(vorstellung.getKinofilm()) && !woche1.contains(vorstellung.getKinofilm())) ||
                        (!woche0.contains(vorstellung.getKinofilm()) && woche1.contains((vorstellung.getKinofilm())))) {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = (int) Math.round(zeitUndTagesabhaengigerAndrang * 0.8);
                } else if (woche0.contains(vorstellung.getKinofilm()) && woche1.contains(vorstellung.getKinofilm())) {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = (int) Math.round(zeitUndTagesabhaengigerAndrang * 0.5);
                } else {
                    zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
                }
                break;
            }
            default: zeitUndTagesUndWiederholungsabhaengigerAndrang = zeitUndTagesabhaengigerAndrang;
            break;
        }
        //endregion

        //region Einfluss des Preises auf den Andrang
        if (eintrittspreis > 7){
            return (int)Math.round(zeitUndTagesUndWiederholungsabhaengigerAndrang * (1 - (eintrittspreis - 7) * 0.05));
        }
        else if (eintrittspreis < 7){
            return (int)Math.round(zeitUndTagesUndWiederholungsabhaengigerAndrang * (1 + (7 - eintrittspreis) * 0.02));
        }
        else {return zeitUndTagesUndWiederholungsabhaengigerAndrang;}
        //endregion
    }

    /**
     * Berechnet die durch den Spielplan entstehenden Kosten
     * @return die Kosten für den Spielplan
     */
    private int spielplanAusgaben() {

        //Die Betrachtung findet zunächst wochenweise statt.
        int kosten = 0;

        Set<Kinofilm> filmeWoche0 = new HashSet<>();
        Set<Kinofilm> filmeWoche1 = new HashSet<>();
        Set<Kinofilm> filmeWoche2 = new HashSet<>();

        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            ArrayList<Vorstellung> wochenVorstellungen;

            switch (wochenIndex) {
                case 0:
                    wochenVorstellungen = woche0;
                    break;
                case 1:
                    wochenVorstellungen = woche1;
                    break;
                case 2:
                    wochenVorstellungen = woche2;
                    break;
                default:
                    wochenVorstellungen = null;
                    break;
            }

            //Ermittlung der Kinofilme einer Woche (ohne Dopplung)
            for (Vorstellung vorstellung : wochenVorstellungen) {
                switch(wochenIndex){
                    case 0: filmeWoche0.add(vorstellung.getKinofilm());
                        break;
                    case 1: filmeWoche1.add(vorstellung.getKinofilm());
                        break;
                    case 2: filmeWoche2.add(vorstellung.getKinofilm());
                        break;
                }
            }

            // Überprüfung ob Filme parallel am gleichen Tag laufen. Wenn ja, einbezug der Kosten
            for (int vorstellungsIndexProTag = 0; vorstellungsIndexProTag < 4 * anzahlSaele; vorstellungsIndexProTag++) {
                for (Vorstellung vorstellung : wochenVorstellungen) {
                    if (vorstellung.getKinofilm() == wochenVorstellungen.get(vorstellungsIndexProTag).getKinofilm() &&
                            vorstellung.getSpielzeiten() == wochenVorstellungen.get(vorstellungsIndexProTag).getSpielzeiten() &&
                            vorstellung.getSaal() != wochenVorstellungen.get(vorstellungsIndexProTag).getSaal()) {
                        kosten += vorstellung.getKinofilm().getVerleihpreisProWoche();
                    }
                }
            }
        }

        // Zusammenfassung aller Kinofilme der drei Wochen (mit doppelterfassung, wenn Filme in mehreren Wochen vorkommen)
        ArrayList<Kinofilm> alleFilme = new ArrayList<>();
        alleFilme.addAll(filmeWoche0);
        alleFilme.addAll(filmeWoche1);
        alleFilme.addAll(filmeWoche2);

        for (Kinofilm kinofilm : alleFilme){
            kosten += kinofilm.getVerleihpreisProWoche();
        }

        // Erstellung eines Set's, dass nur die dreifach gezeigten Filme enthält.
        Set<Kinofilm> alleFilmeDreifach = new HashSet<>(alleFilme);
        for (Kinofilm film : alleFilmeDreifach){
            kosten -= film.getVerleihpreisProWoche() * 0.1;
        }
        return kosten;
    }

    /**
     * Sucht den größten und den zweitgrößten Saal.
     * @return [PlätzeImGrößtenSaal,PlätzeImZweitgrößtenSaal]
     */
    private static int[] plaetzteInGroestemUndZweitgroestemSaal(){
        int plaetzeLoge = 0;
        int plaetzeParkett = 0;

        int plaetzeGroesterSaal = 0;
        int plaetzeZweitgroesterSaal = 0;

            for (Saal saal: SaalVerwaltung.getSaele())
            {
                if((plaetzeLoge + plaetzeParkett) < saal.getPlaetzeLoge() + saal.getPlaetzeParkett()){
                    plaetzeLoge = saal.getPlaetzeLoge();
                    plaetzeParkett = saal.getPlaetzeParkett();
                }
            }
            plaetzeGroesterSaal = plaetzeLoge + plaetzeParkett;

            plaetzeLoge = 0;
            plaetzeParkett = 0;

            for (Saal saal: SaalVerwaltung.getSaele())
            {
                if(((plaetzeLoge + plaetzeParkett) < (saal.getPlaetzeLoge() + saal.getPlaetzeParkett())) &&
                        ((saal.getPlaetzeLoge() + saal.getPlaetzeParkett()) < plaetzeGroesterSaal)){
                    plaetzeLoge = saal.getPlaetzeLoge();
                    plaetzeParkett = saal.getPlaetzeParkett();
                }
                plaetzeZweitgroesterSaal = plaetzeLoge + plaetzeParkett;
            }
        return new int[]{plaetzeGroesterSaal, plaetzeZweitgroesterSaal};
    }

    //Getter
    public Vorstellung[][][][] getSpielplan() { return spielplan; }
    public int getSpielplanEinnahmenAusKartenverkaeufen() { return spielplanEinnahmenAusKartenverkaeufen; }
    public int getSpielplanAusgaben() { return spielplanAusgaben; }
    public int getSpielplanGewinn() { return spielplanGewinn; }
    public int getSpielplanWerbungsEinnahmen() { return spielplanWerbungsEinnahmen; }

    //Setter
    public void setSpielplan(Vorstellung[][][][] spielplan) {
        this.spielplan = spielplan;
    }

    //ToString
    public String toString(Vorstellung[][][] spielplan) {
        return Arrays.deepToString(spielplan);
    }
}