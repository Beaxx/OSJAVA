package Gruppe7;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;
import Gruppe7.Data.Werbefilm;
import Gruppe7.Data.WerbefilmVerwaltung;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;

public class Main {
    public static void main(String[] args) throws IOException {

        //Datenimport
        new WerbefilmImporter("C:/import/werbespots.csv");
        new SaalImporter("C:/import/saele.csv");
        new KinofilmImporter("C:/import/filme.csv");

        //Werbeplan sortieren und Standard 20 Minunten Block festlegen
        WerbefilmVerwaltung.werbeplanSortieren();
        WerbefilmVerwaltung.standardWerbeblock();

        // Saele sortieren
        SaalVerwaltung.saalplanSortieren();

        //Performance Wrapper
        long startTime = System.currentTimeMillis();

            Planer planer = new Planer();
            int durchläufe = 10000;
            for (int i = 0; i < durchläufe; i++)

            {
                Planer tempPlaner = new Planer();

                if (tempPlaner.getSpielplanGewinn() > planer.getSpielplanGewinn()) {
                    planer = tempPlaner;
                    System.out.println("Ticket Einnahmen: " + planer.getSpielplanEinnahmenAusKartenverkaeufen() + "\n" +
                                        "Werbungs Einnahmen: " + planer.getSpielplanWerbungsEinnahmen() + "\n" +
                                        "Ausgaben: " + planer.getSpielplanAusgaben() + "\n" +
                                        "Gewinn:" + planer.getSpielplanGewinn() + "\n" +

                                        "--------------------------------");
                }
            }

        // Performance Messung
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long totalTimeS = totalTime/1000;

        // Ausgabe
        System.out.println(Arrays.deepToString(planer.getSpielplan()));

        System.out.println(totalTimeS + " Sekunden für " + durchläufe + " Durchläufe" + "\n" +
                (double)durchläufe/totalTimeS + " pro Sekunde");
    }


}