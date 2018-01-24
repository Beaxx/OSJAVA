package Gruppe7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

        //Werbeplan sortieren
        WerbefilmVerwaltung.werbeplanSortieren();

        // Saal sortieren
        SaalVerwaltung.saalplanSortieren();
        for (Saal saal : SaalVerwaltung.getSaele()){
            System.out.println(saal.getThreeD());
        }

        //Performance Wrapper
        long startTime = System.currentTimeMillis();

        Planer planer = new Planer();
        int durchläufe = 100000;
        for (int i = 0; i < 100000; i++)

        {
            Planer tempPlaner = new Planer();

            if (tempPlaner.getSpielplanGewinn() > planer.getSpielplanGewinn()) {
                planer = tempPlaner;
                System.out.println("Einnahmen: " + planer.getSpielplanEinnahmenAusKartenverkaeufen() + "\n" +
                        "Werbungs Einnahmen: " + planer.getSpielplanWerbungsEinnahmen() + "\n" +
                        "Ausgaben: " + planer.getSpielplanAusgaben() + "\n" +
                        "Gewinn:" + planer.getSpielplanGewinn() + "\n" +
                        "--------------------------------");
            }
            if (i % 10000 == 0){ System.out.println("Durchlauf" + i);}
        }

        // Performance Messung
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime / 1000 + " Sekunden für " + durchläufe + " Durchläufe");
    }


}