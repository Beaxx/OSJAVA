package Gruppe7;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;
import  java.lang.String;

import Gruppe7.Data.*;
import Gruppe7.Exporter.Exporter;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;

/**
 * Zentraler Programeinstieg
 *
 * SETTINGS:
 * /plaeneZuErstellen/
 * Wie viele gültige Spielpläne sollen erstellt werden? Diese Zahl bestimmt über die Dauer des Programmablaufs.
 * Wird die Optimierung der Pläne (OptimierungSwitch = false) ausgeschaltet sind Geschwindigkeiten von
 * 2000 Plänen / Sekunde möglich.
 *
 * /mindestPreisVorstellung/
 * Der niedrigste Preis, den eine Vorstellung haben darf. Wird für die Optimierung verwendet. Ist die
 * Optimierung abgeschaltet -> Kein effekt
 *
 * /maximalPreisVorstellung/
 * Der höchste Preis, den eine Vorstellung haben darf. Wird für die Optimierung verwendet. Ist die
 * Optimierung abgeschaltet -> Keinen Effekt. Sollte nicht größer als 26 gesetzt werden, da der Andrang
 * für einen Kinofilm bei >26€ Eintrittspreis negativ wird.
 *
 * WICHTIG: Die Differenz zwischen Mindest- und Max-Eintrittspreis entscheided über Geschwindigkeit des programs
 * da jede Wert von MAx -> Min ausprobiert werden muss. Hier ein paar Beispielwerte über die Performance:
 *
 * Zufällige Erstellung ohne Optimierung: 2000 Pläne / Sekunde
 * Delta = 0: 9 Pläne / Sekunde (z. B. Min: 17, Max: 17) outdated
 * Delta = 1: 4 Pläne / Sekunde (z. B. Min: 17, Max: 18) outdated
 * Delta = 2: 3.2 Pläne / Sekunde (z. B. Min: 16, Max: 18) outdated
 * Delta = 3: 2.2 Pläne / Sekunde (z. B. Min: 16, Max: 19) outdated
 *
 * /mindestBeliebtheit/
 * Setzt den Schwellenwert für die Beliebtheit eines Films. Filme mit niedrigerer Beliebtheit werden nciht importiert.
 * Hat keinen Einfluss auf die Performanz, aber auf das Endergebnis
 *
 * @author Lennart Völler, Nicole Diestler, Fabian Ueberle
 */


public class Main {

    public static boolean OptimierungSwitch;

    public static void main(String[] args) throws IOException {

        /* SETTINGS */
        int plaeneZuErstellen = 1000; // Max Geschwindigkeit: 3700 pro Sekunde (ohne Optimierung)
        OptimierungSwitch = false;
        int mindestPreisVorstellung = 16;
        int maximalPreisVorstellung = 19;
        int mindestBeliebtheit = 93;
        /* SETTINGS */


        //Datenimport
        new WerbefilmImporter("C:/import/werbespots.csv");
        new SaalImporter("C:/import/saele.csv");
        new KinofilmImporter("C:/import/filme.csv", mindestBeliebtheit);

        //FilmArrays erstellen
        FilmVerwaltung.FilmArraysHelper();

        //Werbeplan sortieren und Standard 20 Minunten Block festlegen
        WerbefilmVerwaltung.werbeplanSortieren();
        WerbefilmVerwaltung.standardWerbeblock();

        // Saele sortieren
        SaalVerwaltung.saalplanSortieren();
        SaalVerwaltung.plaetzteInGroestemUndZweitgroestemSaal();

        //Performance Wrapper start
        long startTime = System.currentTimeMillis();

        //Algorithmus
        Planer planer = new Planer(mindestPreisVorstellung, maximalPreisVorstellung);
        for (int i = 0; i < plaeneZuErstellen; i++)

        {
            Planer tempPlaner = new Planer(mindestPreisVorstellung, maximalPreisVorstellung);

            if (tempPlaner.getSpielplanGewinn() > planer.getSpielplanGewinn()) {
                planer = tempPlaner;
                System.out.println("Ticket Einnahmen: " + planer.getSpielplanEinnahmenAusKartenverkaeufen() + "\n" +
                        "Werbungs Einnahmen: " + planer.getSpielplanWerbungsEinnahmen() + "\n" +
                        "Ausgaben: " + planer.getSpielplanAusgaben() + "\n" +
                        "Gewinn:" + planer.getSpielplanGewinn() + "\n" +
                        "--------------------------------");
            }
        }

        //Performance Wrapper ende
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long totalTimeS = totalTime / 1000;

        // Ausgabe
        System.out.println(Arrays.deepToString(planer.getSpielplan())); //Bester Spielplan

        //Performance Auswertung
        System.out.println(totalTimeS + " Sekunden für " + plaeneZuErstellen + " Durchläufe" + "\n" +
                (double) plaeneZuErstellen / totalTimeS + " pro Sekunde");


        Vorstellung[][][][] spielPlanObj;
        spielPlanObj = planer.getSpielplan();


        System.out.println("Spielplan: " + spielPlanObj);
        final Stream<Vorstellung[][][]> stream = Arrays.stream(spielPlanObj);
        System.out.println(stream.toArray());


        //[WOCHE][TAG][SAAL][SPIELZEIT]
        System.out.println(spielPlanObj.length); //Wochen 0-2
        System.out.println(spielPlanObj[0].length); //Tage 0-6
        System.out.println(spielPlanObj[0][0].length); //Kinosaal
        System.out.println(spielPlanObj[0][0][0].length); //Spielzeit
        //System.out.println("Get: "+spielPlanObj[0][0][0][0].getSaal().getSaalNummer()); //Spielzeit


        for (int iWoche = 0; iWoche <= spielPlanObj.length - 1; iWoche++) {
            System.out.println("Woche: " + (iWoche + 1));

            for (int iTag = 0; iTag <= spielPlanObj[iWoche].length - 1; iTag++) {

                String wochenTag = null;
                switch (iTag + 1) {
                    case 1:
                        wochenTag = "Montag";
                        break;
                    case 2:
                        wochenTag = "Dienstag";
                        break;
                    case 3:
                        wochenTag = "Mittwoch";
                        break;
                    case 4:
                        wochenTag = "Donnerstag";
                        break;
                    case 5:
                        wochenTag = "Freitag";
                        break;
                    case 6:
                        wochenTag = "Samstag";
                        break;
                    case 7:
                        wochenTag = "Sonntag";
                        break;

                    default:
                        wochenTag = "Invalid Day";
                        break;
                }
                System.out.println("Tag: " + (iTag + 1) + " " + wochenTag);

                for (int iSaal = 0; iSaal <= spielPlanObj[iWoche][iTag].length - 1; iSaal++) {
                    System.out.println("Saal Nummer: " + spielPlanObj[iWoche][iTag][iSaal][0].getSaal().getSaalNummer());

                    for (int iSpielzeit = 0; iSpielzeit <= spielPlanObj[iWoche][iTag][iSaal].length - 1; iSpielzeit++) {
                        System.out.println(spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].getSpielzeiten().toString() + ": " + spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].getKinofilm().getTitel());
                    }

                }

            }
        }


    }
}




