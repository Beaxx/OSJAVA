package Gruppe7.Logic;

import java.util.*;
import java.util.stream.IntStream;

import Gruppe7.Data.*;

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

    private boolean checkGenre = false;

    /**
     * Erstellung eines zufälligen Spielplans durch Iteration durch das leere Vorstellungs-Array
     *
     * @return Ein dreidimensionales Vorstellungsarray [tag][saal][timeslot]
     */
    public Planer(){
        // Liste aus Enum
        List<Genre> genreList = Arrays.asList(Genre.values());

        // Kopie der Liste
        ArrayList<Genre> localGenreList = new ArrayList<>();
            localGenreList.addAll(genreList);

        while (checkGenre == false)
            spielplan = CreateRandomSpielplan(localGenreList);

        if (checkGenre == true) {
            spielplanAusgaben = spielplanAusgaben(spielplan);
            spielplanEinnahmenAusKartenverkaeufen = spielplanEinnahmen(spielplan)[0];
            spielplanWerbungsEinnahmen = spielplanEinnahmen(spielplan)[1];
            spielplanGewinn = spielplanEinnahmenAusKartenverkaeufen - spielplanAusgaben + spielplanWerbungsEinnahmen;
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
     * Erstellteinen zufälligen Spielplan
     * @return Ein zufälliger Spielplan
     */
    public Vorstellung[][][][] CreateRandomSpielplan(ArrayList<Genre> localGenreList) {
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                        spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex] = new Vorstellung(saalIndex, vorstellungIndex);
                        if (!checkGenre){
                            checkGenre(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].getKinofilm().getGenre(), localGenreList);
                        }
                    }
                }
            }
            return spielplan;
        }

    /**
     * Berechnet die zu erwartenden Einnahmen aus Ticketverkäufen und Werbung einer Vorstellung und fügt sie den Spielplaneinnahmen hinzu
     * @param spielplan der Spielplan für den die Einnahmen zu berechnen sind
     * @return ein Array mit [Einnahmen durch Kartenverkäufe][Einnahmen durch Werbung]
     */
    private int[] spielplanEinnahmen(Vorstellung[][][][] spielplan) {
        int[] localSpielplaneinnahmen = {0, 0};
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {

                        int andrang = andrang(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex], tagIndex, vorstellungIndex, wochenIndex, spielplan);
                        //Einnahmen aus Ticketverkäufen
                        // TODO: Check ob Säle überbesetzt
                        localSpielplaneinnahmen[0] += spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].getEintrittspreis() * andrang;

                        //Einnahmen aus Werbung
                        for (Werbefilm werbung : spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].getWerbefilme()) {
                        localSpielplaneinnahmen[1] += werbung.getUmsatzProZuschauer() * andrang;
                        }
                    }
                }
            }
        return localSpielplaneinnahmen;
    }


    /**
     * Berechnet die zu erwartenden Ausgaben für einen Spielplan
     */
    private int spielplanAusgaben(Vorstellung[][][][] spielplan) {

        //Die Betrachtung findet zunächst wochenweise statt.
        int[] kosten = {0, 0, 0};
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            ArrayList<Vorstellung> wochenVorstellungen = new ArrayList<>();

            // Alle vorstellungen einer Woche werden in einer Liste zusammengefasst.
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                        wochenVorstellungen.add(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex]);
                    }
                }
            }

            //Erstellung aller einzigartigen Kinofilme einer Woche.
            ArrayList<Kinofilm> wochenKinofilme = new ArrayList<>();
            for (Vorstellung vorstellung : wochenVorstellungen) {
                if (!wochenKinofilme.contains(vorstellung.getKinofilm())) {
                    wochenKinofilme.add(vorstellung.getKinofilm());
                }
            }

            // Berechnung der Leihgebühren
            for (Kinofilm film : wochenKinofilme) {
                kosten[wochenIndex] += film.getVerleihpreisProWoche();
            }

            Vorstellung[] wochenVorstellungArray = new Vorstellung[wochenVorstellungen.size()];
            wochenVorstellungArray = wochenVorstellungen.toArray(wochenVorstellungArray);

            // Überprüfung ob Filme parallel am gleichen Tag laufen. Wenn ja, einbezug der Kosten
            for (int vorstellungsIndexProTag = 0; vorstellungsIndexProTag < 4 * anzahlSaele; vorstellungsIndexProTag++) {
                for (Vorstellung vorstellung : wochenVorstellungen) {
                    if (vorstellung.getKinofilm() == wochenVorstellungArray[vorstellungsIndexProTag].getKinofilm() &&
                            vorstellung.getSpielzeiten() == wochenVorstellungArray[vorstellungsIndexProTag].getSpielzeiten() &&
                            vorstellung.getSaal() != wochenVorstellungArray[vorstellungsIndexProTag].getSaal()) {

                        kosten[wochenIndex] += vorstellung.getKinofilm().getVerleihpreisProWoche();
                    }
                }
            }
        }
        return IntStream.of(kosten).sum();
    }

    /**
     * Sucht den größten und den zweitgrößten Saal.
     * @return [PlätzeImGrößtenSaal,PlätzeImZweitgrößtenSaal]
     */
    private static int[] plaetzteInGroestemUndZweitgroeßtemSaal(){
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

        int[] outputarray = {plaetzeGroesterSaal, plaetzeZweitgroesterSaal};
        return outputarray;
    }

    private int andrang(Vorstellung vorstellung, int in_tagIndex, int in_vorstellungIndex, int in_wochenIndex, Vorstellung[][][][] spielplan) {
        // TODO: Code kopiert aus spielplanAusgaben() - Auslagern
        ArrayList<Kinofilm> woche0 = new ArrayList<>();
        ArrayList<Kinofilm> woche1 = new ArrayList<>();
        ArrayList<Kinofilm> woche2 = new ArrayList<>();

        // Alle vorstellungen jeder Woche werden in je einer Liste zusammengefasst.
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++) {
            for (int tagesIndex = 0; tagesIndex < 7; tagesIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungsIndex = 0; vorstellungsIndex < 4; vorstellungsIndex++) {

                        switch (wochenIndex) {
                            case 0:
                                woche0.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex].getKinofilm());
                                break;
                            case 1:
                                woche1.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex].getKinofilm());
                                break;
                            case 2:
                                woche2.add(spielplan[wochenIndex][tagesIndex][saalIndex][vorstellungsIndex].getKinofilm());
                                break;
                        }
                    }
                }
            }
        }

        /*
        Berechnung des Basisandrangs über die größe der beiden größten Säle.
         */
        int[] saalPlaetze = plaetzteInGroestemUndZweitgroeßtemSaal();
        int basisandrang = (int) Math.round((saalPlaetze[0] + saalPlaetze[1]) *
                ((double) (vorstellung.getKinofilm().getBeliebtheit()) / 85));


        //region Einfluss der Uhrzeit auf den Andrang
        int vorstellungsabhaengigerAndrang;
        switch (in_vorstellungIndex){
            //15 Uhr Vorstellung 90%
            case 0: {vorstellungsabhaengigerAndrang = (int) Math.round(basisandrang * .9);
                        break;}

            //17:30 Uhr Vorstellung 95%
            case 1: {vorstellungsabhaengigerAndrang = (int) Math.round(basisandrang * .95);
                        break;}

            //23 Uhr Vorstellung 85%
            case 3: {vorstellungsabhaengigerAndrang = (int) Math.round(basisandrang * .85);
                        break;}

            //20 Uhr und Catch-All 100%
            default: vorstellungsabhaengigerAndrang = basisandrang;
        }
        //endregion

        //region Einfluss des Wochentages auf den Andrang.
        int vorstellungsUndTagesabhaengigerAndrang;
        //Dienstag, Mittwoch, Donnerstag 60%
        if ((in_tagIndex > 0 && in_tagIndex < 4) || (in_tagIndex > 7 && in_tagIndex < 11) || (in_tagIndex > 14 && in_tagIndex < 18)) {
            vorstellungsUndTagesabhaengigerAndrang = (int) Math.round(vorstellungsabhaengigerAndrang * .6);
        }

        //Freitag, Samstag, Sonntag 80%
        else if ((in_tagIndex > 3 && in_tagIndex < 7) || (in_tagIndex > 10 && in_tagIndex < 14) || (in_tagIndex > 17 && in_tagIndex < 21)) {
            vorstellungsUndTagesabhaengigerAndrang = (int) Math.round(vorstellungsabhaengigerAndrang * .8);
        }

        //Montag und Catch-All 100%
        else {
            vorstellungsUndTagesabhaengigerAndrang = vorstellungsabhaengigerAndrang;
        }
        //endregion

        //region Einfluss der Wiederholung von Filmen
        /*
         * Einbezug der anderen Wochen:
         * In Woche 0 kann es keine Abzüge geben, da die Filme zum ersten Mal gezeigt werden.
         * In Woche 1 kommt es zu abzügen, wenn der Film bereits in Woche 0 gezeigt wurde.
         * In Woche 2 kommt es zu den selben Abzügen wie in Woche 1, wenn der Film in Woche 1 ODER in Woche 0
         *  gezeigt wurde. Wenn der Film sowohl in Woche 0 als auch Woche 1 gezeigt wurde kommt es zu stärkeren
         *  abzügen.
         */
        switch (in_wochenIndex) {

            case 1: {
                if (woche0.contains(vorstellung.getKinofilm())) {
                    return (int) Math.round(vorstellungsUndTagesabhaengigerAndrang * 0.8);
                } else {
                    return vorstellungsUndTagesabhaengigerAndrang;
                }
            }

            case 2: {
                if ((woche0.contains(vorstellung.getKinofilm()) && !woche1.contains(vorstellung.getKinofilm())) ||
                        (!woche0.contains(vorstellung.getKinofilm()) && woche1.contains((vorstellung.getKinofilm())))) {
                    return (int) Math.round(vorstellungsUndTagesabhaengigerAndrang * 0.8);
                } else if (woche0.contains(vorstellung.getKinofilm()) && woche1.contains(vorstellung.getKinofilm())) {
                    return (int) Math.round(vorstellungsUndTagesabhaengigerAndrang * 0.5);
                } else {
                    return vorstellungsUndTagesabhaengigerAndrang;
                }
            }

            default: return vorstellungsUndTagesabhaengigerAndrang;
            //TODO: Der Normalpreis (Parkett) beträgt 7 Euro. Für jeden Euro, den der Preis erhöht wird, sinkt der Zuschauerandrang um 5%. Für jeden Euro, den der Preis gesenkt wird, steigt der Besucherandrang um 2%.
        }
        //endregion
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

    //Setter
    public void setSpielplan(Vorstellung[][][][] spielplan) {
        this.spielplan = spielplan;
    }

    //ToString
    public String toString(Vorstellung[][][] spielplan) {
        return Arrays.deepToString(spielplan);
    }
}