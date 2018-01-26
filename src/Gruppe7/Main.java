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
        int durchlaeufe = 100000; // Geschwindigkeit: 2750 pro Sekunde
        int mindestPreisVorstellung = 17;
        int maximalPreisVorstellung = 17;
        int mindestBeliebtheit = 95;
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
            for (int i = 0; i < durchlaeufe; i++)

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
        long totalTimeS = totalTime/1000;

        // Ausgabe
        System.out.println(Arrays.deepToString(planer.getSpielplan())); //Bester Spielplan

        System.out.println(totalTimeS + " Sekunden für " + durchlaeufe + " Durchläufe" + "\n" + //Performance Auswertung
                (double)durchlaeufe/totalTimeS + " pro Sekunde");
    }
}