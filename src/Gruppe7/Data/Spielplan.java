package Gruppe7.Data;
import java.util.Arrays;
import java.util.List;
import Gruppe7.Logic.*;

public class Spielplan {
    //Constant
    static final int gesamtZahlVorstellungen = 21 * SaalVerwaltung.getSize() * 4;

    /**
     * Konstruktor erstellt einen zufälligen Spielplan,
     * berechnet die Einnahmen durch diesen Plan,
     * berechnet die Ausgaben für diesen Plan
     */
    public Spielplan() {
        spielplan = CreateRandomSpielplan();
        spielplanEinnahmen();
        spielplanAusgaben();
    }

    //Spielplan ist ein Array der Länge 21(Tage) * Anzahl der Säle *  4(Spielzeiten)
    private Vorstellung[][][] spielplan = new Vorstellung[21][SaalVerwaltung.getSize()][4];
    private int spielplaneinnahmen = 0;
    private int spielplanAusgaben = 0;


    /**
     * Erstellung eines zufälligen Spielplans durch Iteration durch das leere Vorstellungs-Array
     *
     * @return Ein dreidimensionales Vorstellungsarray [tag][saal][timeslot]
     */
    public Vorstellung[][][] CreateRandomSpielplan() {
        for (int tagIndex = 0; tagIndex <= 20; tagIndex++) {
            for (int saalIndex = 0; saalIndex <= SaalVerwaltung.getSize() - 1; saalIndex++) {
                for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                    spielplan[tagIndex][saalIndex][vorstellungIndex] = new Vorstellung();
                }
            }
        }
        return spielplan;
    }

    /**
     * Berechnet die zu erwartenden Einnahmen durch den Spielplan
     */
    private void spielplanEinnahmen() {
        for (int tagIndex = 0; tagIndex <= 20; tagIndex++) {
            for (int saalIndex = 0; saalIndex <= SaalVerwaltung.getSize() - 1; saalIndex++) {
                for (int vorstellungIndex = 0; vorstellungIndex < 4; vorstellungIndex++) {
                    spielplaneinnahmen += spielplan[tagIndex][saalIndex][vorstellungIndex].getEintrittspreis() *
                            Planer.Andrang(spielplan[tagIndex][saalIndex][vorstellungIndex], tagIndex, vorstellungIndex);
                }
            }
        }
    }

    /**
     * Berechnet die zu erwartenden Ausgaben durch den Spielplan
     */
    private void spielplanAusgaben() {
        Vorstellung[][] wochenweiseVorstellungen = new Vorstellung[3][SaalVerwaltung.getSize()];
        for (int wochenIndex = 0; wochenIndex <= 20; wochenIndex += 7) {
            for (int tagProWoche = 0; tagProWoche < 7; tagProWoche++) {
                wochenweiseVorstellungen[wochenIndex][tagProWoche] = ???
                        // TOOD: Die Vorstellungen einer Woche sammeln
            }
        }
    }

    //??
    public int SpielplanGewinn() {
        return 0;
    }

    //Check Methode
    // TODO: Genrecheck in die Spielplanerstellung einbinden.
    private boolean checkGenre() {
        //Temporäre Genre-Liste
        List<Genre> enumerationList = Arrays.asList(Genre.values());

        // Prüfung, ob jedes Genre im Spielplan mindestens einmal vertreten ist.
        for (int tag = 0; tag <= 20; tag++) {
            for (int saal = 0; saal < SaalVerwaltung.getSize() - 1; saal++) {
                for (int vorstellung = 0; vorstellung < 4; vorstellung++) {
                    if (enumerationList.contains(spielplan[tag][saal][vorstellung].getKinofilm().getGenre())) {
                        enumerationList.remove(spielplan[tag][saal][vorstellung].getKinofilm().getGenre());
                    }

                    // Abbruchbedingung
                    if (enumerationList.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Getter
    public Vorstellung[][][] getSpielplan() {
        return spielplan;
    }

    public int getSpielplaneinnahmen() {
        return spielplaneinnahmen;
    }

    public int getSpielplanAusgaben() {
        return spielplanAusgaben;
        //Setter


        //Setter

    public void setSpielplan(Vorstellung[][][] spielplan) {
        this.spielplan = spielplan;
    }

    //ToString
    public String toString(Vorstellung[][][] spielplan) {
        return Arrays.deepToString(spielplan);
    }
}