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
    private int spielplanEinnahmen = 0;
    private int spielplanAusgaben = 0;
    private int spielplanGewinn = 0;

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
            spielplanEinnahmen = spielplanEinnahmen(spielplan);
            spielplanGewinn = spielplanEinnahmen - spielplanAusgaben;
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
                        spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex] = new Vorstellung();

                        if (!checkGenre){
                            checkGenre(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].getKinofilm().getGenre(), localGenreList);
                        }
                    }
                }
            }
            return spielplan;
        }

    /**
     * Berechnet die zu erwartenden Einnahmen einer Vorstellung und fügt sie den Spielplaneinnahmen hinzu
     */
    private int spielplanEinnahmen(Vorstellung[][][][] spielplan) {
        int localSpielplaneinnahmen = 0;
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                        localSpielplaneinnahmen += spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex].getEintrittspreis() *
                                andrang(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex], tagIndex, vorstellungIndex);
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

    /**
     * Berechnet den Andrang für eine Vorstellung.
     * @param vorstellung Die Vorstellung, für die der Andrang berechnet werden soll.
     * @param tagIndex Der Index des Tages, an dem die Vorstellung stattfindet.
     * @param vorstellungIndex Der Index des Timeslots, zu dem die Vorstellung stattfindet.
     * @return Die Zahl der für die Vorstellung erwarteten Zuschauer.
     */
    private int andrang(Vorstellung vorstellung, int tagIndex, int vorstellungIndex){
        int basisandrang = (int)Math.round((plaetzteInGroestemUndZweitgroeßtemSaal()[0] +
                                            plaetzteInGroestemUndZweitgroeßtemSaal()[1]) *
                                            ((double)(vorstellung.getKinofilm().getBeliebtheit()) / 85));

        int vorstellungsabHaengigerAndrang;

        //15 Uhr Vorstellung 90%
        if (vorstellungIndex == 0){
            vorstellungsabHaengigerAndrang = (int)Math.round(basisandrang * .9);
        }
        //17:30 Uhr Vorstellung 95%
        else if (vorstellungIndex == 1){
            vorstellungsabHaengigerAndrang = (int)Math.round(basisandrang * .95);
        }
        //23 Uhr Vorstellung 85%
        else if (vorstellungIndex == 3){
            vorstellungsabHaengigerAndrang = (int)Math.round(basisandrang * .85);
        }

        //20 Uhr und Catch-All 100%
        else { return basisandrang; }


        //Montag 100%
        if (tagIndex == 0 || tagIndex == 7 || tagIndex == 14 || tagIndex == 21)
            return vorstellungsabHaengigerAndrang;

        //Dienstag, Mittwoch, Donnerstag 60%
        else if ((tagIndex > 0 && tagIndex < 4) || (tagIndex > 7 && tagIndex < 11) || (tagIndex > 14 && tagIndex < 18)){
            return (int)Math.round(vorstellungsabHaengigerAndrang * .6);
        }

        //Freitag, Samstag, Sonntag 80%
        else if ((tagIndex > 3 && tagIndex < 7) || (tagIndex > 10 && tagIndex < 14) || (tagIndex > 17 && tagIndex < 21)) {
            return (int) Math.round(vorstellungsabHaengigerAndrang * .8);
        }

        //Montag und Catch-All 100%
        else {return basisandrang;}

        //TODO: Wird in Woche 2 (bzw. 3) ein Film gezeigt, der bereits in der ersten (bzw. ersten oder zweiten) Woche
        // gezeigt wurde, werden nur 80% des Werts erreicht; wird in Woche 3 ein Film gezeigt der bereits in der ersten UND zweiten Woche gezeigt wurde, nur 50%.

        //TODO: Der Normalpreis (Parkett) beträgt 7 Euro. Für jeden Euro, den der Preis erhöht wird, sinkt der Zuschauerandrang um 5%. Für jeden Euro, den der Preis gesenkt wird, steigt der Besucherandrang um 2%.
    }


    //Getter
    public Vorstellung[][][][] getSpielplan() {
        return spielplan;
    }

    public int getSpielplanEinnahmen() {
        return spielplanEinnahmen;
    }

    public int getSpielplanAusgaben() {
        return spielplanAusgaben;
    }

    public int getSpielplanGewinn() {
        return spielplanGewinn;
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