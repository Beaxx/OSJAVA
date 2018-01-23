package Gruppe7.Logic;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import Gruppe7.Data.*;

public class Planer
{
    int anzahlSaele = SaalVerwaltung.getSize();

    //Spielplan ist ein Array der Länge 3(Wochen) * 7(Tage) * Anzahl der Säle *  4(Spielzeiten)
    private Vorstellung[][][][] spielplan = new Vorstellung[3][7][anzahlSaele][4];
    private int spielplaneinnahmen = 0;
    private int spielplanAusgaben = 0;

    /**
     * Erstellung eines zufälligen Spielplans durch Iteration durch das leere Vorstellungs-Array
     *
     * @return Ein dreidimensionales Vorstellungsarray [tag][saal][timeslot]
     */
    public Planer(){
        spielplan = CreateRandomSpielplan();
        spielplanAusgaben(spielplan);
    }

    /**
     * Erstellteinen zufälligen Spielplan
     * @return Ein zufälliger Spielplan
     */
    public Vorstellung[][][][] CreateRandomSpielplan() {
        for (int wochenIndex = 0; wochenIndex < 3; wochenIndex++)
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                for (int saalIndex = 0; saalIndex < SaalVerwaltung.getSize(); saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                        spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex] = new Vorstellung();
                        spielplanEinnahmen(spielplan[wochenIndex][tagIndex][saalIndex][vorstellungIndex], tagIndex, vorstellungIndex);
                    }
                }
            }
            return spielplan;
        }

    /**
     * Berechnet die zu erwartenden Einnahmen einer Vorstellung und fügt sie den Spielplaneinnahmen hinzu
     */
    private void spielplanEinnahmen(Vorstellung vorstellung, int tagIndex, int vorstellungIndex) {
        spielplaneinnahmen += vorstellung.getEintrittspreis() * andrang(vorstellung, tagIndex, vorstellungIndex);
    }

    /**
     * Berechnet die zu erwartenden Ausgaben für einen Spielplan
     */
    private void spielplanAusgaben(Vorstellung[][][][] spielplan) {

        //Die Betrachtung findet tagweise statt.
        int[] kosten = {0,0,0};
        for (int wochenindex = 0; wochenindex < 3; wochenindex++) {
            for (int tagIndex = 0; tagIndex < 7; tagIndex++) {
                ArrayList<Vorstellung> tagesVorstellungen = new ArrayList<>();

                /*
                 * Alle vorstellungen eines Tages werden in einer Liste zusammengefasst.
                 */
                for (int saalIndex = 0; saalIndex < anzahlSaele; saalIndex++) {
                    for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                        tagesVorstellungen.add(spielplan[wochenindex][tagIndex][saalIndex][vorstellungIndex]);
                    }
                }

                /*
                 * Es wird überprüft ob Filme parallel am gleichen Tag gezeigt werden.
                 */
                for (Vorstellung vorstellung: tagesVorstellungen){
                    int dupecount = findeParralelLaufendeVorstellungen(vorstellung, tagesVorstellungen);
                        kosten[wochenindex] += vorstellung.getKinofilm().getVerleihpreisProWoche() * dupecount;
                        System.out.println(dupecount);
                }

                /*
                 *
                 */
                for
            }
        }
        spielplanAusgaben = IntStream.of(kosten).sum();
    }

    /**
     * Findet zeitgleich stattfindende Filmvorführungen
     * @param input Eine Vorstellung, für die Überprüft werden soll, ob parallel in einem anderen Saal läuft
     * @param tagesVorstellungen Alle Vorstellung die an einem Tag stattfinden
     * @return Die anzahl der Dopplungen (Keine Dopplung = 1)
     */
    private int findeParralelLaufendeVorstellungen(Vorstellung input, ArrayList<Vorstellung> tagesVorstellungen){
        int counter = 0;
        for (Vorstellung vorstellung : tagesVorstellungen)
        {
            if (input.getKinofilm().equals(vorstellung.getKinofilm()) &&
                    input.getSpielzeiten().equals(vorstellung.getSpielzeiten())){
            counter++;
            }
        }
        return counter;
    }

    /**
     *
     */
    public static void Improve() {

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

        //TODO: Wird in Woche 2 (bzw. 3) ein Film gezeigt, der bereits in der ersten (bzw. ersten oder zweiten) Woche gezeigt wurde, werden nur 80% des Werts erreicht; wird in Woche 3 ein Film gezeigt der bereits in der ersten UND zweiten Woche gezeigt wurde, nur 50%.
        //TODO: Der Normalpreis (Parkett) beträgt 7 Euro. Für jeden Euro, den der Preis erhöht wird, sinkt der Zuschauerandrang um 5%. Für jeden Euro, den der Preis gesenkt wird, steigt der Besucherandrang um 2%.
    }


    //Check Methode
    // TODO: Genrecheck in die Spielplanerstellung einbinden.
//    private boolean checkGenre() {
//        //Temporäre Genre-Liste
//        List<Genre> enumerationList = Arrays.asList(Genre.values());
//
//        // Prüfung, ob jedes Genre im Spielplan mindestens einmal vertreten ist.
//        for (int tag = 0; tag <= 20; tag++) {
//            for (int saal = 0; saal < SaalVerwaltung.getSize() - 1; saal++) {
//                for (int vorstellung = 0; vorstellung < 4; vorstellung++) {
////                    if (enumerationList.contains(spielplan[tag][saal][vorstellung].getKinofilm().getGenre())) {
////                        enumerationList.remove(spielplan[tag][saal][vorstellung].getKinofilm().getGenre());
//                    }
//
//                    // Abbruchbedingung
//                    if (enumerationList.isEmpty()) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    //??
    public int SpielplanGewinn() {
        return 0;
    }

    //Getter
    public Vorstellung[][][][] getSpielplan() {
        return spielplan;
    }

    public int getSpielplaneinnahmen() {
        return spielplaneinnahmen;
    }

    public int getSpielplanAusgaben() {
        return spielplanAusgaben;
        //Setter
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