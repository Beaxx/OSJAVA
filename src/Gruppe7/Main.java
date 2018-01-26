package Gruppe7;

import java.io.IOException;
import java.util.Arrays;
import Gruppe7.Data.*;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;

/**
 * Zentraler Programeinstieg
 * @author Lennart Völler, Nicole Diestler, Fabian Ueberle
 */
public class Main {
    public static void main(String[] args) throws IOException {

        /* SETTINGS */
        int plaeneZuErstellen = 1000; // Max Geschwindigkeit: 2900 pro Sekunde (ohne Optimierung)
        int mindestPreisVorstellung = 15;
        int maximalPreisVorstellung = 20;
        int mindestBeliebtheit = 90;
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
                if (i % 100 == 0){
                    System.out.println("Durchlauf" + i);
                }
            }

        //Performance Wrapper ende
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long totalTimeS = totalTime/1000;

        // Ausgabe
        System.out.println(Arrays.deepToString(planer.getSpielplan())); //Bester Spielplan

            //Performance Auswertung
        System.out.println(totalTimeS + " Sekunden für " + plaeneZuErstellen + " Durchläufe" + "\n" +
                (double)plaeneZuErstellen/totalTimeS + " pro Sekunde");
    }
}